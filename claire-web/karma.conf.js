module.exports = function(config){
  config.set({

    basePath : './src/main/webapp/',

    frameworks: ['jasmine'],

    files : [
      'bower_components/angular/angular.js',
      'bower_components/angular-route/angular-route.js',
      'bower_components/angular-resource/angular-resource.js',
      'bower_components/angular-mocks/angular-mocks.js',
      'scripts/*.js',
      'scripts/**/*.js',
      'test/**/*.js'
    ],

    autoWatch : false,

    frameworks: ['jasmine'],

    browsers : ['Firefox'],

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
