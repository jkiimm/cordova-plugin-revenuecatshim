#import "RCSUtil.h"
#import <Cordova/CDV.h>

@interface RCSPurchase : CDVPlugin {
  // Member variables go here.
}

- (void)configure:(CDVInvokedUrlCommand*)command;
- (void)getEntitlements:(CDVInvokedUrlCommand*)command;
- (void)makePurchase:(CDVInvokedUrlCommand*)command;
- (void)getPurchaserInfo:(CDVInvokedUrlCommand*)command;
- (void)identify:(CDVInvokedUrlCommand*)command;
- (void)reset:(CDVInvokedUrlCommand*)command;
- (void)getAppUserId:(CDVInvokedUrlCommand*)command;
@end

