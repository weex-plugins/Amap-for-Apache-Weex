//
//  WXMapViewModule.m
//  Pods
//
//  Created by yangshengtao on 17/1/23.
//
//

#import "WXMapViewModule.h"
#import "WXMapViewComponent.h"
#import <AMapSearchKit/AMapSearchKit.h>

@interface WXMapViewModule () <AMapSearchDelegate>

@end

@implementation WXMapViewModule
{
    AMapSearchAPI *_search;
    WXModuleCallback _searchCallback;
}

@synthesize weexInstance;

WX_EXPORT_METHOD(@selector(getUserLocation:callback:))
WX_EXPORT_METHOD(@selector(search:callback:ref:))

- (void)getUserLocation:(NSString *)elemRef callback:(WXModuleCallback)callback
{
    [self performBlockWithMapView:elemRef block:^(WXMapViewComponent *mapView) {
        callback([mapView getUserLocation] ? : nil);
    }];
}

- (void)search:(NSString *)keyWord callback:(WXModuleCallback)callback ref:(NSString *)elemRef
{
    if (!_search) {
        _search = [[AMapSearchAPI alloc] init];
        _search.delegate = self;
    }
    _searchCallback = nil;
    _searchCallback = [callback copy];
    [self searchPoiByKeyword:keyWord];
}

- (void)performBlockWithMapView:(NSString *)elemRef block:(void (^)(WXMapViewComponent *))block {
    if (!elemRef) {
        return;
    }
    __weak typeof(self) weakSelf = self;
    
    WXPerformBlockOnComponentThread(^{
        WXMapViewComponent *mapView = (WXMapViewComponent *)[weakSelf.weexInstance componentForRef:elemRef];
        if (!mapView) {
            return;
        }
        
        [weakSelf performSelectorOnMainThread:@selector(doBlock:) withObject:^() {
            block(mapView);
        } waitUntilDone:NO];
    });
}

- (void)doBlock:(void (^)())block {
    block();
}

#pragma mark -
#pragma mark - search
/* 根据关键字来搜索POI. */
- (void)searchPoiByKeyword:(NSString *)keyword
{
    AMapPOIKeywordsSearchRequest *request = [[AMapPOIKeywordsSearchRequest alloc] init];
    request.keywords = keyword;
    //    request.keywords            = @"北京大学";
    //    request.city                = @"北京";
    //    request.types               = @"高等院校";
    //    request.requireExtension    = YES;
    //
    //    /*  搜索SDK 3.2.0 中新增加的功能，只搜索本城市的POI。*/
    //    request.cityLimit           = YES;
    request.requireSubPOIs      = YES;
    
    [_search AMapPOIKeywordsSearch:request];
}

/* POI 搜索回调. */
- (void)onPOISearchDone:(AMapPOISearchBaseRequest *)request response:(AMapPOISearchResponse *)response
{
    NSDictionary *userDic;
    NSDictionary *data = [self parseAmapSearchResponse:response];
    if (!data) {
        userDic = @{@"result":@"false",@"data":[NSDictionary dictionary]};
    }else{
        userDic = @{@"result":@"success",@"data":data};
    }
    _searchCallback(userDic ? : nil);
}

- (NSDictionary *)parseAmapSearchResponse:(AMapPOISearchResponse *)res
{
    if (res.count <= 0) {
        return nil;
    }
    return @{
             @"count":@(res.count),
             @"suggestion":@{@"keywords":res.suggestion.keywords,
             @"cities":res.suggestion.cities},
             @"pois":res.pois
            };
}

@end
