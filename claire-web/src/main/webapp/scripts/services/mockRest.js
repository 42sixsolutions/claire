
angular.module('claire.mockREST', ['ngMockE2E']).run(function($injector) {
    var $httpBackend = $injector.get('$httpBackend');

    $httpBackend.whenGET(/partials/).passThrough();

    /**
     * Sample JSON
     */
    var crestor =  {
        brandName: "Crestor",
        genericName: "ROSUVASTATIN CALCIUM",
        details : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus vitae pellentesque sem. Vivamus eu urna nec nibh sodales condimentum a eu lectus. Integer iaculis fermentum metus, eu tincidunt orci congue id. Curabitur varius, diam eu aliquet malesuada, ligula lacus blandit metus, in iaculis orci tellus sit amet felis. Cras sed sapien ac mi mollis sollicitudin. Morbi congue sem ac felis egestas, id ultricies metus tempus. Nulla in eleifend urna. Nam ipsum urna, pharetra varius cursus finibus, maximus a felis."
    };

    /**
     * Drug Info
     */
    $httpBackend.whenGET(/api\/drug\/detail/).respond( crestor );

    $httpBackend.whenGET(/api\/drug\/tweets/).respond( {
        totalTweets: 10,
        percentPositive: 25,
        percentNegative: 15,
        percentUnknown: 60
    } );

    $httpBackend.whenGET(/api\/drug\/fda/).respond( {
        totalRecalls: 7,
        totalAdverseEvents: 1500
    } );

    $httpBackend.whenGET(/api\/drug\/ranking/).respond( [
        {brandName: "Crestor", ranking: 1, currentDrug: true},
        {brandName: "Viagra", ranking: 2}
    ] );

    $httpBackend.whenGET(/api\/drug\/chart/).respond( {
        "positiveTweets": [['2015-01-01', 25],['2015-01-02', 15],['2015-01-03', 100],['2015-01-04', 5]],
        "negativeTweets": [['2015-01-01', 25],['2015-01-02', 15],['2015-01-03', 100],['2015-01-04', 5]],
        "unknownTweets": [['2015-01-01', 25],['2015-01-02', 15],['2015-01-03', 100],['2015-01-04', 5]],
        "adverseEvents": [['2015-01-01', 50],['2015-01-02', 100],['2015-01-03', 50],['2015-01-04', 25]],
        "recalls": [['2015-01-02', 100]]
    } );

    /**
     * Search
     */
    $httpBackend.whenGET(/api\/drugs\/test/).respond(['test1', 'test2']);


    /**
     * Trends
     */
    $httpBackend.whenGET(/api\/trends\/positive/).respond([
        {
            brandName: "Plavix",
            percentPositive: 99,
            percentNegative: 0
        },
        {
            brandName: "Zocor",
            percentPositive: 98,
            percentNegative: 2
        }]);

    $httpBackend.whenGET(/api\/trends\/negative/).respond([
        {
            brandName: "Lipitor",
            percentPositive: 4,
            percentNegative: 99
        },
        {
            brandName: "Nexium",
            percentPositive: 6,
            percentNegative: 90
        }]);

    $httpBackend.whenGET(/api\/trends\/adverse/).respond([
        {
            brandName: "Amoxil",
            incidents: 5676
        },
        {
            brandName: "Synthroid",
            percentPositive: 4565
        }]);

});