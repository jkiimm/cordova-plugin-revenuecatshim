var exec = require('cordova/exec')

exports.configure = function (apiKey, appUserId) {
  log('configure')
  appUserId = appUserId || ''

  return new Promise(function (resolve, reject) {
    try { 
      exec(resolve, reject, 'RevenueCatShim', 'configure', [apiKey, appUserId])
    } catch (e) {
      reject(e)
    }
  })
}

exports.getEntitlements = function () {
  log('getEntitlements')
  return new Promise(function (resolve, reject) {
    try {
      exec(resolve, reject, 'RevenueCatShim', 'getEntitlements', [])
    } catch (e) {
      reject(e)
    }
  })
}

exports.makePurchase = function (productId) {
  log('makePurchase')
  return new Promise(function (resolve, reject) {
    try {
      exec(resolve, reject, 'RevenueCatShim', 'makePurchase', [productId])
    } catch (e) {
      reject(e)
    }
  })
}

exports.getPurchaserInfo = function () {
  log('getPurchaserInfo')
  return new Promise(function (resolve, reject) {
    try {
      exec(resolve, reject, 'RevenueCatShim', 'getPurchaserInfo', [])
    } catch (e) {
      reject(e)
    }
  })
}

exports.identify = function (userId) {
  log('identify')
  return new Promise(function (resolve, reject) {
    try {
      exec(resolve, reject, 'RevenueCatShim', 'identify', [userId])
    } catch (e) {
      reject(e)
    }
  })
}

exports.reset = function () {
  log('reset')
  return new Promise(function (resolve, reject) {
    try {
      exec(resolve, reject, 'RevenueCatShim', 'reset', [])
    } catch (e) {
      reject(e)
    }
  })
}

exports.getAppUserId = function () {
  log('getAppUserId')
  return new Promise(function (resolve, reject) {
    try {
      exec(resolve, reject, 'RevenueCatShim', 'getAppUserId', [])
    } catch (e) {
      reject(e)
    }
  })
}

function log (text) {
  console.log('[RCS.js] ' + text)
}
