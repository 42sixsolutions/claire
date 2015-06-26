module.exports = function(config){
  config.set({

    basePath : './src/main/webapp/',

    frameworks: ['jasmine'],

    files : [
      'bower_components/angular/angular.js',
      'bower_components/angular-route/angular-route.js',
      'bower_components/angular-resource/angular-resource.js',
      'bower_components/angular-mocks/angular-mocks.js',
      'bower_components/jquery/jquery.js',
      'bower_components/jquery-ui/jquery-ui.js',
      'bower_components/d3/d3.js',
      'bower_components/trianglify/dist/trianglify.min.js',
      'bower_components/chosen/chosen.jquery.min.js',
      'lib/radial-progress/radialProgress.js',
      'lib/radial-progress/radialProgressSml.js',
      'lib/flot/jquery.flot.js',
      'lib/flot/jquery.flot.categories.js',
      'lib/flot/jquery.flot.crosshair.js',
      'lib/flot/jquery.flot.symbol.js',
      'lib/flot/jquery.flot.stack.js',
      'lib/flot/jquery.flot.time.js',
      'lib/flot/jquery.flot.resize.js',
      'lib/curvedLines.js',
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
