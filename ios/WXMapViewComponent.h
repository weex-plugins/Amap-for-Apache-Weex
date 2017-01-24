//
//  WXMapViewComponent.h
//  WeexDemo
//
//  Created by yangshengtao on 2017/1/20.
//  Copyright © 2016年 taobao. All rights reserved.
//

#import <WeexSDK/WeexSDK.h>
#import <MAMapKit/MAMapKit.h>
#import <AMapFoundationKit/AMapFoundationKit.h>

@interface WXMapViewComponent : WXComponent<MAMapViewDelegate>

- (NSDictionary *)getUserLocation;

@end



