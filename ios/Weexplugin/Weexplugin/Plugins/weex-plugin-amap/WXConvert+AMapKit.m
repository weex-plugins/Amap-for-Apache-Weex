//
//  WXConvert+AMapKit.m
//  Pods
//
//  Created by yangshengtao on 17/3/3.
//
//

#import "WXConvert+AMapKit.h"
#import "NSArray+WXMap.h"

@implementation WXConvert (AMapKit)

#define WX_JSON_CONVERTER(type)           \
+ (type *)type:(id)json { return json; }

WX_JSON_CONVERTER(NSArray)
WX_JSON_CONVERTER(NSDictionary)
WX_JSON_CONVERTER(NSString)

+ (CLLocationCoordinate2D)CLLocationCoordinate2D:(id)json
{
    json = [self NSArray:json];
    return (CLLocationCoordinate2D){
        [[json wxmap_safeObjectForKey:1] doubleValue],
        [[json wxmap_safeObjectForKey:0] doubleValue]
    };
}

+ (BOOL)isLineDash:(id)json
{
    json = [self NSString:json];
    if ([json isEqualToString:@"dashed"]) {
        return YES;
    }
    return NO;
}

@end
