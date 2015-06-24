
angular.module('claire.mockREST', ['ngMockE2E']).run(function($injector) {
    var $httpBackend = $injector.get('$httpBackend');

    $httpBackend.whenGET(/partials/).passThrough();

    /**
     * Sample JSON
     */
    var crestor =  {
        brandName: "Crestor",
        genericName: "ROSUVASTATIN CALCIUM",
        details: "Rosuvastatin (marketed by AstraZeneca as Crestor, In India marketed by (Cipla)as \"rosulip\" is a member of the drug class of statins, used in combination with exercise, diet, and weight-loss to treat high cholesterol and related conditions, and to prevent cardiovascular disease. It was developed by Shionogi. Crestor is the fourth-highest selling drug in the United States, accounting for approx. $5.2 billion in sales in 2013.",
        additionalDetails: "The primary use of rosuvastatin is for the treatment of dyslipidemia.[3] It is recommended to be used only after other measures such as diet, exercise, and weight reduction have not improved cholesterol levels "
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

    $httpBackend.whenGET(/api\/drug\/rankings/).respond( {
        "positiveTweets": [
            { brandName: "Crestor", ranking: 1, currentDrug: true },
            { brandName: "Viagra", ranking: 100 }
        ],
        "negativeTweets": [
            { brandName: "Crestor", ranking: 1, currentDrug: true },
            { brandName: "Viagra", ranking: 100 }
        ], 
        "neutralTweets": [
            { brandName: "Crestor", ranking: 1, currentDrug: true },
            { brandName: "Viagra", ranking: 100 }
        ], 
        "adverseEvents": [
            { brandName: "Crestor", ranking: 1, currentDrug: true },
            { brandName: "Viagra", ranking: 100 }
        ], 
        "recalls": [
            { brandName: "Crestor", ranking: 1, currentDrug: true },
            { brandName: "Viagra", ranking: 100 }
        ]
    });

    $httpBackend.whenGET(/api\/drug\/chart/).respond( {
        "positiveTweets": [['2015-01-01', 25],['2015-01-02', 15],['2015-01-03', 20],['2015-01-04', 15]],
        "negativeTweets": [['2015-01-01', 50],['2015-01-02', 70],['2015-01-03', 48],['2015-01-04', 15]],
        "unknownTweets": [['2015-01-01', 25],['2015-01-02', 15],['2015-01-03', 32],['2015-01-04', 70]],
        "adverseEvents": [['2015-01-01', 0],['2015-01-02', 0],['2015-01-03', 0],['2015-01-04', 0]],
        "recalls": [['2015-01-02', 100]]
    } );

    $httpBackend.whenGET(/api\/drug\/list/).respond(
        ["enbrel","humira","aspirin","hydrochloride","tysabri","methotrexate","prednisone","metformin","avonex","lipitor","lisinopril","acetaminophen","nexium","omeprazole","avandia","simvastatin","folic","metoprolol","lyrica","mirena","synthroid","hydrochlorothiazide","dianeal","levothyroxine","remicade","lasix","furosemide","crestor","chantix","amlodipine","atenolol","seroquel","plavix","spiriva","revlimid","lantus","advair","dexamethasone","albuterol","ibuprofen","celebrex","coumadin","byetta","warfarin","insulin","prednisolone","diovan","vioxx","tylenol","cymbalta","norvasc","oxycodone","xanax","forteo","premarin","aleve","neurontin","chloride","tramadol","zoloft","fentanyl","hydrocodone","gabapentin","morphine","tartrate","paxil","fosamax","prilosec","allopurinol","cyclophosphamide","niaspan","letairis","wellbutrin","ribavirin","acetylsalicylic","effexor","zocor","lorazepa"]
    );


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