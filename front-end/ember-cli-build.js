'use strict';

const EmberApp = require('ember-cli/lib/broccoli/ember-app');

module.exports = function(defaults) {
  var app = new EmberApp({
	  
  });

  app.import('bower_components/boostrap/dist/css/bootstrap.css'); //Import bootstrap
  app.import('bower_components/boostrap/dist/js/bootstrap.js');
  return app.toTree();
};
