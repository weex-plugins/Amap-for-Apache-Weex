//
//  WXMapViewModule.m
//  Pods
//
//  Created by yangshengtao on 17/1/23.
//
//

#import "WXMapViewModule.h"
#import "WXMapViewComponent.h"

@implementation WXMapViewModule

@synthesize weexInstance;

WX_EXPORT_METHOD(@selector(getUserLocation:callback:))

- (void)getUserLocation:(NSString *)elemRef callback:(WXModuleCallback)callback
{
    [self performBlockWithMapView:elemRef block:^(WXMapViewComponent *mapView) {
        callback([mapView getUserLocation] ? : nil);
    }];
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

@end
