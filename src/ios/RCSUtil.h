#import "Purchases.h"

@interface RCSUtil : NSObject

+ (NSDictionary*)szPurchaserInfo:(RCPurchaserInfo*)purchaserInfo;
+ (NSMutableDictionary*)szEntitlements:(RCEntitlements*)entitlements;
+ (NSDictionary*)productToDict:(SKProduct*)product;
+ (NSString*)dateToMillis:(NSDate*)date;
+ (NSString *)localizedPrice:(SKProduct*)product;

@end
