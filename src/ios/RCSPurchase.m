/********* RCSPurchase.m Cordova Plugin Implementation *******/
#import "RCSPurchase.h"

@implementation RCSPurchase

- (void)configure:(CDVInvokedUrlCommand*)command {
  CDVPluginResult* pluginResult = nil;

  NSString* appUserId = command.arguments[1];
  if ([appUserId length] == 0) {
    appUserId = nil;
  }

  @try {
    RCPurchases.debugLogsEnabled = YES;
    [RCPurchases configureWithAPIKey:command.arguments[0] appUserID:appUserId];
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
  } @catch (NSException *exception) {
    NSLog(@"[RCS] %@", exception.reason);
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"error exception"];
  } @finally {
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
  }
}

- (void)getEntitlements:(CDVInvokedUrlCommand*)command {
  [[RCPurchases sharedPurchases] entitlementsWithCompletionBlock:^(RCEntitlements *entitlements, NSError *error) {
    CDVPluginResult* pluginResult = nil;

    @try {
      if (error) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"failed to get entitlements"];
      } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:[RCSUtil szEntitlements:entitlements]];
      }
    } @catch (NSException *exception) {
      NSLog(@"[RCS] %@", exception.reason);
      pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"error exception"];
    } @finally {
      [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }
  }];
}

- (void)makePurchase:(CDVInvokedUrlCommand*)command {
  NSArray* productIds = @[command.arguments[0]];
  RCPurchases* purchases = [RCPurchases sharedPurchases];

  [purchases productsWithIdentifiers:productIds completionBlock:^(NSArray<SKProduct *> * products) {
    [purchases makePurchase:products[0] withCompletionBlock:^(SKPaymentTransaction *transaction, RCPurchaserInfo *purchaserInfo, NSError *error) {
      CDVPluginResult* pluginResult = nil;

      if (error) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"failed to make a purchase"];
      } else {
        NSArray *dataArray = @[transaction.transactionIdentifier, [RCSUtil szPurchaserInfo:purchaserInfo]];
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsArray:dataArray];
      }

      [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
  }];
}

- (void)getPurchaserInfo:(CDVInvokedUrlCommand*)command {
  [[RCPurchases sharedPurchases] purchaserInfoWithCompletionBlock:^(RCPurchaserInfo * purchaserInfo, NSError * error) {
    CDVPluginResult* pluginResult = nil;

    if (error) {
      pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"failed to get purchaser info"];
    } else {
      pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:[RCSUtil szPurchaserInfo:purchaserInfo]];
    }

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
  }];
}

- (void)identify:(CDVInvokedUrlCommand*)command {
  NSString* appUserId = command.arguments[0];

  [[RCPurchases sharedPurchases] identify:appUserId completionBlock:^(RCPurchaserInfo *purchaserInfo, NSError *error) {
    CDVPluginResult* pluginResult = nil;
    
    @try {
      if (error) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"failed to identify"];
      } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:[RCSUtil szPurchaserInfo:purchaserInfo]];
      }
    } @catch (NSException *exception) {
      NSLog(@"[RCS] %@", exception.reason);
      pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"error exception"];
    } @finally {
      [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }
  }];
}

- (void)reset:(CDVInvokedUrlCommand*)command {
  [[RCPurchases sharedPurchases] resetWithCompletionBlock:^(RCPurchaserInfo *purchaserInfo, NSError *error) {
    CDVPluginResult* pluginResult = nil;

    if (error) {
      pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"failed to reset"];
    } else {
      pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:[RCSUtil szPurchaserInfo:purchaserInfo]];
    }

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
  }];
}

- (NSString*)getAppUserId:(CDVInvokedUrlCommand*)command {
  NSString* appUserId = [RCPurchases sharedPurchases].appUserID;
  CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:appUserId];
  [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end
