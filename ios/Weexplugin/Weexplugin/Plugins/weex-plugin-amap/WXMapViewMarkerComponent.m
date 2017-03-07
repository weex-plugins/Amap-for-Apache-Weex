//
//  WXMapViewMarkerComponent.m
//  Pods
//
//  Created by yangshengtao on 17/2/6.
//
//

#import "WXMapViewMarkerComponent.h"
#import "WXMapViewComponent.h"
#import "NSDictionary+WXMap.h"

@implementation WXMapViewMarkerComponent
{
    @private BOOL _viewLoaded;
}

@synthesize clickEvent = _clickEvent;
@synthesize icon = _icon;
@synthesize title = _title;
@synthesize location = _location;


- (instancetype)initWithRef:(NSString *)ref
                       type:(NSString*)type
                     styles:(nullable NSDictionary *)styles
                 attributes:(nullable NSDictionary *)attributes
                     events:(nullable NSArray *)events
               weexInstance:(WXSDKInstance *)weexInstance
{
    self = [super initWithRef:ref type:type styles:styles attributes:attributes events:events weexInstance:weexInstance];
    if (self) {
        if ([events containsObject:@"click"]) {
            _clickEvent = @"click";
        }
        _location = [attributes wxmap_safeObjectForKey:@"position"];
        _title = [attributes wxmap_safeObjectForKey:@"title"];
        _icon = [attributes wxmap_safeObjectForKey:@"icon"];
    }
    _viewLoaded = NO;
    return self;
}

/*
- (UIView *) loadView
{
    return nil;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    if (!_viewLoaded) {
        [(WXMapViewComponent *)self.supercomponent addMarker:self];
        _viewLoaded = YES;
    }
}
 */
- (void)updateAttributes:(NSDictionary *)attributes
{
    WXMapViewComponent *mapComponent = (WXMapViewComponent *)self.supercomponent;
    if (attributes[@"title"]) {
        _title = attributes[@"title"];
        [mapComponent updateTitleMarker:self];
    }
    
    if ([attributes wxmap_safeObjectForKey:@"icon"]) {
        _icon = attributes[@"icon"];
        [mapComponent updateIconMarker:self];
    }
    
    if ([attributes wxmap_safeObjectForKey:@"position"]) {
        _location = attributes[@"position"];
        [mapComponent updateLocationMarker:self];
        
    }
}

- (void)removeFromSuperview;
{
    [super removeFromSuperview];
    [(WXMapViewComponent *)self.supercomponent removeMarker:self];
}

- (void)dealloc
{
    
}

@end
