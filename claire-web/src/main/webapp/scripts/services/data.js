'use strict';

angular.module('claire.services').factory('Data', [function() {
    var getLineData = function() {
        return [{
            "type": "val1",
            "date": "1/1/00",
            "Line 1": "11.361",
            "Line 2": "40.863",
            "Line 3": "106.118",
            "Line 4": "33.896"
          }, {
            "type": "val1",
            "date": "1/1/01",
            "Line 1": "9.552",
            "Line 2": "39.713",
            "Line 3": "123.521",
            "Line 4": "32.408"
          }, {
            "type": "val1",
            "date": "1/1/02",
            "Line 1": "8.576",
            "Line 2": "38.091",
            "Line 3": "137.61",
            "Line 4": "33.204"
          }, {
            "type": "val1",
            "date": "1/1/03",
            "Line 1": "7.549",
            "Line 2": "35.853",
            "Line 3": "140.896",
            "Line 4": "34.865"
          }, {
            "type": "val1",
            "date": "1/1/04",
            "Line 1": "6.716",
            "Line 2": "32.109",
            "Line 3": "156.805",
            "Line 4": "36.044"
          }, {
            "type": "val1",
            "date": "1/1/05",
            "Line 1": "6.312",
            "Line 2": "30.235",
            "Line 3": "164.498",
            "Line 4": "36.149"
          }, {
            "type": "val1",
            "date": "1/1/06",
            "Line 1": "5.76",
            "Line 2": "27.934",
            "Line 3": "161.806",
            "Line 4": "36.039"
          }, {
            "type": "val1",
            "date": "1/1/07",
            "Line 1": "5.181",
            "Line 2": "25.183",
            "Line 3": "164.546",
            "Line 4": "35.703"
          }, {
            "type": "val1",
            "date": "1/1/08",
            "Line 1": "4.922",
            "Line 2": "28.642",
            "Line 3": "180.783",
            "Line 4": "40.183"
          }, {
            "type": "val1",
            "date": "1/1/09",
            "Line 1": "8.195",
            "Line 2": "35.716",
            "Line 3": "183.53",
            "Line 4": "53.573"
          }, {
            "type": "val1",
            "date": "1/1/10",
            "Line 1": "10.966",
            "Line 2": "36.073",
            "Line 3": "183.53",
            "Line 4": "61.274"
        }];
    }

    var getTimeData = function() {
      return [
        {
          'value': 1380854103662
        }, {
          'value': 1363641921283
        }, {
          'value': 1373641921283
        }, {
          'value': 1369641921283
        }, {
          'value': 1362641921283
        }, {
          'value': 1373841921283
        }
      ];
    }

    return {
        getLineData: getLineData,
        getTimeData: getTimeData
    };
}]);
