/********* RevenueCatShim.m Cordova Plugin Implementation *******/

#import "Purchases.h"
#import <Cordova/CDV.h>

@interface RevenueCatShim : CDVPlugin {
  // Member variables go here.
}

- (void)configure:(CDVInvokedUrlCommand*)command;
- (void)getEntitlements:(CDVInvokedUrlCommand*)command;
- (void)makePurchase:(CDVInvokedUrlCommand*)command;
- (void)getPurchaserInfo:(CDVInvokedUrlCommand*)command;
- (void)identify:(CDVInvokedUrlCommand*)command;
- (void)reset:(CDVInvokedUrlCommand*)command;
@end

@implementation RevenueCatShim

- (void)configure:(CDVInvokedUrlCommand*)command {
  RCPurchases.debugLogsEnabled = YES;
  [RCPurchases configureWithAPIKey:command.arguments[0] appUserID:command.arguments[1]];
  CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
  [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)getEntitlements:(CDVInvokedUrlCommand*)command {
  [[RCPurchases sharedPurchases] entitlementsWithCompletionBlock:^(RCEntitlements *entitlements, NSError *error) {
    CDVPluginResult* pluginResult = nil;

    if (error) {
      pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"failed to get entitlements"];
    } else {
      pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsMultipart:entitlements];
    }

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
  }];
}

- (void)makePurchase:(CDVInvokedUrlCommand*)command {
  /*
  NSObject* product = command.arguments[0];

  [[RCPurchases sharedPurchases] makePurchase:product withCompletionBlock:^(SKPaymentTransaction *transaction, RCPurchaserInfo *purchaserInfo, NSError *error) {
    CDVPluginResult* pluginResult = nil;
    NSString* entitlementId = command.arguments[0];

    if (error) {
      pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"failed to make a purchase"];
    } else if ([purchaserInfo.activeEntitlements containsObject:entitlementId]) {
      NSArray *dataArray = @[transaction, purchaserInfo];
      pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsMultipart:dataArray];
    } else {
      pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"entitlement not exist"];
    }

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
  }];
  */
  CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
  [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)getPurchaserInfo:(CDVInvokedUrlCommand*)command {
  [[RCPurchases sharedPurchases] purchaserInfoWithCompletionBlock:^(RCPurchaserInfo * purchaserInfo, NSError * error) {
    CDVPluginResult* pluginResult = nil;
    NSString* entitlementId = command.arguments[0];

    if (error) {
      pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"failed to get purchaser info"];
    } else if ([purchaserInfo.activeEntitlements containsObject:entitlementId]) {
      pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:purchaserInfo];
    } else {
      pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"entitlement not exist"];
    }

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
  }];
}

- (void)identify:(CDVInvokedUrlCommand*)command {
  NSString* appUserId = command.arguments[0];

  [[RCPurchases sharedPurchases] identify:appUserId completionBlock:^(RCPurchaserInfo *purchaserInfo, NSError *error) {
    CDVPluginResult* pluginResult = nil;
    
    if (error) {
      pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"failed to identify"];
    } else {
      pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:purchaserInfo];
    }

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
  }];
}

- (void)reset:(CDVInvokedUrlCommand*)command {
  [[RCPurchases sharedPurchases] resetWithCompletionBlock:^(RCPurchaserInfo *purchaserInfo, NSError *error) {
    CDVPluginResult* pluginResult = nil;

    if (error) {
      pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"failed to reset"];
    } else {
      pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:purchaserInfo];
    }

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
  }];
}

@end
