//
//  WXConvert+AMapKit.h
//  Pods
//
//  Created by yangshengtao on 17/3/3.
//
//

#import <WeexSDK/WeexSDK.h>
#import <CoreLocation/CoreLocation.h>

@interface WXConvert (AMapKit)

+ (CLLocationCoordinate2D)CLLocationCoordinate2D:(id)json;
+ (BOOL)isLineDash:(id)json;

@end
