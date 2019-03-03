/********* RCSUtil Implementation *******/
#import "RCSUtil.h"

@implementation RCSUtil

+ (NSDictionary*)szPurchaserInfo:(RCPurchaserInfo*)pi {
  return [NSDictionary dictionaryWithObjectsAndKeys: 
    [pi.activeEntitlements allObjects], @"activeEntitlements",
    [pi.activeSubscriptions allObjects], @"activeSubscriptions", 
    [pi.allPurchasedProductIdentifiers allObjects], @"allPurchasedProductIds", 
    [RCSUtil dateToMillis:pi.latestExpirationDate], @"latestExpirationDate", 
    nil
  ];
}

+ (NSMutableDictionary*)szEntitlements:(RCEntitlements*)entitlements {
  NSMutableDictionary *entsDict = [[NSMutableDictionary alloc]init];

  for (id entitlementId in entitlements) {
    NSMutableDictionary *offsDict = [[NSMutableDictionary alloc]init];
    RCEntitlement* entitlement = [entitlements objectForKey:entitlementId];

    for (id offeringId in entitlement.offerings) {
      RCOffering* offering = [entitlement.offerings objectForKey:offeringId];
      NSDictionary* product = [RCSUtil productToDict:offering.activeProduct];
      [offsDict setObject:[NSDictionary dictionaryWithObjectsAndKeys:product, @"product", nil] forKey:offeringId];
    }

    [entsDict setObject:[NSDictionary dictionaryWithObjectsAndKeys:offsDict, @"offerings", nil] forKey:entitlementId];
  }

  return entsDict;
}

+ (NSDictionary*)productToDict:(SKProduct*)p {
  return [NSDictionary dictionaryWithObjectsAndKeys: 
    p.productIdentifier, @"productId",
    [RCSUtil localizedPrice:p], @"price", 
    p.localizedTitle, @"title", 
    p.localizedDescription, @"description", 
    nil
  ];
}

+ (NSString*)dateToMillis:(NSDate*)date {
  NSTimeInterval millis = [date timeIntervalSince1970];
  return [NSString stringWithFormat:@"%ld", lroundf(millis)];
}

+ (NSString*)localizedPrice:(SKProduct*)p {
  NSNumberFormatter *numberFormatter = [[NSNumberFormatter alloc] init];
  [numberFormatter setFormatterBehavior:NSNumberFormatterBehavior10_4];
  [numberFormatter setNumberStyle:NSNumberFormatterCurrencyStyle];
  [numberFormatter setLocale:p.priceLocale];
  NSString *formattedString = [numberFormatter stringFromNumber:p.price];

  return formattedString;
}

@end
