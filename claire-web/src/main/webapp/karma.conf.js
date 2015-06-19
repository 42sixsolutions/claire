module.exports = function(config){
  config.set({

    basePath : './',

    files : [
      'webappapp/bower_components/angular/angular.js',
      'webappapp/bower_components/angular-route/angular-route.js',
      'webappapp/bower_components/angular-mocks/angular-mocks.js',
      'webappapp/view*/**/*.js'
    ],

    autoWatch : false,

    frameworks: ['jasmine'],

    browsers : ['Chrome'],

    plugins : [
            'karma-chrome-launcher',
            'karma-firefox-launcher',
            'karma-jasmine',
            'karma-junit-reporter'
            ],

    junitReporter : {
      outputFile: 'test_out/unit.xml',
      suite: 'unit'
    }

  });
};
