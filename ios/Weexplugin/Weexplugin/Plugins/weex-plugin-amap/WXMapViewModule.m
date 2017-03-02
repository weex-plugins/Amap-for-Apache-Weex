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
#import <MJExtension/MJExtension.h>

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
WX_EXPORT_METHOD(@selector(searchNearBy:position:radius:callback:))

- (void)getUserLocation:(NSString *)elemRef callback:(WXModuleCallback)callback
{
    [self performBlockWithMapView:elemRef block:^(WXMapViewComponent *mapView) {
        callback([mapView getUserLocation] ? : nil);
    }];
}

- (void)search:(NSString *)keyWord callback:(WXModuleCallback)callback ref:(NSString *)elemRef
{
    [self setupSearchInstance];
    _searchCallback = [callback copy];
    [self searchPoiByKeyword:keyWord];
}
                 
- (void)searchNearBy:(NSString *)type position:(NSArray *)pos radius:(NSInteger)radius callback:(WXModuleCallback)callback
{
    [self setupSearchInstance];
    _searchCallback = [callback copy];
    if (pos.count < 2) {
        return;
    }
    [self searchPoiByCenterCoordinate:pos keyword:type radius:radius];
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

- (void)setupSearchInstance
{
    if (!_search) {
        _search = [[AMapSearchAPI alloc] init];
        _search.delegate = self;
    }
    _searchCallback = nil;
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
    request.requireSubPOIs      = YES;
    
    [_search AMapPOIKeywordsSearch:request];
}

/* 根据中心点坐标来搜周边的POI. */
- (void)searchPoiByCenterCoordinate:(NSArray *)center keyword:(NSString *)keyword radius:(NSInteger)radius
{
    AMapPOIAroundSearchRequest *request = [[AMapPOIAroundSearchRequest alloc] init];
    
    request.location            = [AMapGeoPoint locationWithLatitude:[center[1] doubleValue] longitude:[center[0] doubleValue]];
    request.keywords            = keyword;
    request.radius = radius;
    /* 按照距离排序. */
    request.sortrule            = 0;
    request.requireExtension    = YES;
    
    [_search AMapPOIAroundSearch:request];
}

/* POI 搜索回调. */
- (void)onPOISearchDone:(AMapPOISearchBaseRequest *)request response:(AMapPOISearchResponse *)response
{
    NSDictionary *userDic;
    NSDictionary *data = response.mj_keyValues;
    if (!data) {
        userDic = @{@"result":@"false",@"data":[NSDictionary dictionary]};
    }else{
        userDic = @{@"result":@"success",@"data":data};
    }
    _searchCallback(userDic ? : nil);
}


@end
