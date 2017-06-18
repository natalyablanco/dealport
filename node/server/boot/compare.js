const http = require('http');
const amazon = require('amazon-product-api');

const CURRENCY = {
    'EUR': {
        
    },
    'USD': {

    }
};

const OUTCOME = {
    CHEAPER: 0,
    CATEGORIES: {

    }
}


function saveOrUpdate() {


}

function currencyExc(from, to, fn) {
    http.get('http://api.fixer.io/latest?symbols=' + to + '&base=' + from, (res) => {
        let rawData = '';
        res.on('data', (chunk) => { rawData += chunk; });
        res.on('end', () => {
            const data = JSON.parse(rawData);
            if(data && data.rates) {
                fn(data.rates[to])
            }
        });
    })
}

function fielder(val, pattern) {
    const patt = pattern.split('.');
    for(let field in patt) {
        val = val[patt[field]]
    } return val
}

const PARSER = {
    JSON: function(data, pattern) {
        let val = JSON.parse(data);
        let nxt = false, real = val, tmp = pattern.next; 
        while(typeof tmp === 'object') {
            for(let ind in tmp) {
                var rind = real[ind];
                if(ind === '[]') rind = real[real.length - 1];
                if(tmp[ind] === rind) {
                    nxt = real[pattern.forward]; break;
                }  tmp = tmp[ind];
                real = rind;
            } if (nxt) break;
        }
        
        

        return { items: fielder(val, pattern.items), next: nxt };
    },
    HTML: function(data, pattern, next) {

    }
}

const CLIENTS = {
    AMAZON: function amazonClient(item, attrs, cb) {
    
        var client = amazon.createClient(attrs.aws);
        client.itemSearch({
            keywords: item.title,
            responseGroup: 'Small,OfferSummary',
            condition: 'New',
            domain: attrs.domain
        }).then(function(results){
            var am = results[0];
            cb({
                category: am.ItemAttributes[0].ProductGroup[0],
                price: am.OfferSummary[0].LowestNewPrice[0].Amount[0]/100,
                currency: am.OfferSummary[0].LowestNewPrice[0].CurrencyCode[0],
                region: attrs.country
            });
        }).catch(function(err){
            // console.log(err);
            // if (ind >= parsedData.collection.items.length) {
            //     ress.json(result);
            // }
        });
    },
    HTTP: function httpClient(opt, parser, cb) {
        http.request(opt, (res) => {
            const { statusCode } = res;
            const contentType = res.headers['content-type'];
            let error;
            if (statusCode !== 200) {
                error = new Error('Request Failed.\n' +
                                `Status Code: ${statusCode}`);
            } else if (!/^application\/json/.test(contentType)) {
                error = new Error('Invalid content-type.\n' +
                                `Expected application/json but received ${contentType}`);
            }
            if (error) {
                console.error(error.message);
                // consume response data to free up memory
                res.resume();
                return;
            }

            res.setEncoding('utf8');
            let rawData = '';
            res.on('data', (chunk) => { rawData += chunk; });
            res.on('end', () => {
                cb(PARSER[parser.fn](rawData, parser.pattern));
            });
        }).on('error', (e) => {
            console.error(`Got error: ${e.message}`);
        }).end();
    }
}


module.exports = function (app) {
    const today = new Date().toLocaleDateString('en-US');

    function compare(item, ext, extprice) {
        const diff = (extprice || ext.price) - item.price;
        if( diff > 0) {
            app.models.diff.upsert({
                name: item.title,
                source: item.src,
                url: item.url,
                img: item.img,
                desc: item.desc,
                external: ext.region,
                date: today,
                type: 'PRODUCT'
            }, (err, obj) => {});
            
            var catg = OUTCOME.CATEGORIES[ext.category] = OUTCOME.CATEGORIES[ext.category] || { count: 0, delta: 0 };
            catg.count++; catg.delta += diff;
            
            OUTCOME.CATEGORIES[ext.region] = OUTCOME.CATEGORIES[ext.region] || {};
            var cat = OUTCOME.CATEGORIES[ext.region][ext.category] = OUTCOME.CATEGORIES[ext.region][ext.category] || { count: 0, delta: 0 };
            cat.count++; cat.delta += diff;
        }
    }
    
    app.get('/compare/:code', function(req, res, next) {
        app.models.source.findOne({ where: { name: req.params.code, type: 'INVENTORY' }}, function(err, src) {
            if(!err && src) {
                var items = [];
                const loader = CLIENTS[src.connector];
                var parse = function(data, doNotUpsert) {
                    items = items.concat(doNotUpsert && data || data.items);
                    if(data.next) {
                        const opts = data.next.split(src.attrs.opt.host);
                        src.attrs.opt.path = opts[opts.length -1 ];
                        loader(src.attrs.opt, src.attrs.parser, parse)
                    } else {
                        app.models.source.find({ where: { type: 'EXTERNAL' }}, function(err, exts) {
                            const pattern = src.attrs.parser.pattern;
                            for(let ind in exts) {
                                const ext = exts[ind];
                                Promise.all(
                                    items.map((item) => doNotUpsert && item || app.models.product.upsert({
                                        info: item,
                                        source: src.name,
                                        updated: today
                                    }))
                                ).then(prods => {
                                    prods.map((prod) => {
                                        const item = prod.info;
                                        const desc = fielder(item, pattern.desc);
                                        const title = fielder(item, pattern.title);
                                        const price = fielder(item, pattern.price);
                                        const url = fielder(item, pattern.url);
                                        const img = fielder(item, pattern.img);
                                        CLIENTS[ext.connector]({
                                            title, desc, price, url, img
                                        }, ext.attrs, function(found) {
                                            let extprice = found.price;
                                            if(found.currency != src.currency) {
                                                if(!CURRENCY[src.currency][found.currency]) {
                                                    currencyExc(found.currency, src.currency, (rate) => {
                                                        CURRENCY[src.currency] = CURRENCY[src.currency] || {};
                                                        CURRENCY[src.currency][found.currency] = rate;
                                                        compare({ title, desc, price, url, img, src: src.name}, found, extprice * rate);
                                                    })
                                                } else {
                                                    compare({ title, desc, price, url, img, src: src.name}, found, extprice * CURRENCY[src.currency][found.currency]);
                                                }
                                            } else {
                                                compare({ title, desc, price, url, img, src: src.name}, found);
                                            }
                                        })
                                    })
                                }).catch(function() {
                                    console.log(arguments)
                                })
                            }
                        });
                    }
                };

                app.models.scan.count({ name: src.name, date: today}, function(err, cd) {
                    if(!err) {
                        if(cd > 0) {
                            app.models.product.find({ where: { updated: today, source: src.name }}, (err, data) => {
                                parse(data, true)
                            })
                        } else {
                            loader(src.attrs.opt, src.attrs.parser, parse);
                            app.models.scan.upsert({
                                name: src.name,
                                date: today,
                                info: {}
                            }, (sc, el) => {

                            })
                        }
                    }
                }); res.json({ message: 'success'});
            } else {
                res.json({ message: 'not found'});
            }
        })
    });
}