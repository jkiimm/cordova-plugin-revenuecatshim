var exec = require('cordova/exec');

exports.configure = function (arg0, success, error) {
  exec(success, error, 'RevenueCatShim', 'configure', [arg0]);
};

exports.getEntitlements = function (arg0, success, error) {
  exec(success, error, 'RevenueCatShim', 'getEntitlements', [arg0]);
};

exports.makePurchase = function (arg0, success, error) {
  exec(success, error, 'RevenueCatShim', 'makePurchase', [arg0]);
};

exports.getPurchaserInfo = function (arg0, success, error) {
  exec(success, error, 'RevenueCatShim', 'getPurchaserInfo', [arg0]);
};

exports.identify = function (arg0, success, error) {
  exec(success, error, 'RevenueCatShim', 'identify', [arg0]);
};

exports.reset = function (arg0, success, error) {
  exec(success, error, 'RevenueCatShim', 'reset', [arg0]);
};
