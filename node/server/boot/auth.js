'use strict';

module.exports = function enableAuthentication(app) {
  // enable authentication
//   app.enableAuth();

  var ensureLoggedIn = require('connect-ensure-login').ensureLoggedIn;

  app.get('/auth/account', ensureLoggedIn('/login'), function(req, res, next) {
    res.render('pages/loginProfiles', {
      user: req.user,
      url: req.url,
    });
  });

  console.log('enableAuth');
//   console.log(app);
//   console.log(app.models.application);

  // app.models.Email.send({
  //   to: 'leventgny@gmail.com',
  //   from: 'ankarajava@gmail.com',
  //   subject: 'Hey there',
  //   text: 'my text',
  //   html: 'my <em>html</em>'
  // }, function(err, mail) {
  //   console.log('email sent!', err, mail);
  // });

  app.post('/signup', function(req, res, next) {
    var User = app.models.user;

    var newUser = {};
    newUser.email = req.body.email.toLowerCase();
    newUser.username = req.body.username.trim();
    newUser.password = req.body.password;

    User.create(newUser, function(err, user) {
      if (err) {
        req.flash('error', err.message);
        return res.redirect('back');
      } else {
        // Passport exposes a login() function on req (also aliased as logIn())
        // that can be used to establish a login session. This function is
        // primarily used when users sign up, during which req.login() can
        // be invoked to log in the newly registered user.
        req.login(user, function(err) {
          if (err) {
            req.flash('error', err.message);
            return res.redirect('back');
          }
          return res.redirect('/auth/account');
        });
      }
    });
  });

  app.get('/login', function(req, res, next) {
    res.render('pages/login', {
      user: req.user,
      url: req.url,
    });
  });

  app.get('/auth/logout', function(req, res, next) {
    req.logout();
    res.redirect('/');
  });
};
