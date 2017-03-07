//
//  WXMapRenderer.m
//  Pods
//
//  Created by yangshengtao on 17/3/3.
//
//

#import "WXMapRenderer.h"
#import "WXMapViewComponent.h"
#import "NSDictionary+WXMap.h"
#import "NSArray+WXMap.h"

static const void *shapeKey = &shapeKey;

@implementation WXComponent(WXMapShape)

@dynamic shape;

- (void)setShape:(MAShape *)shape {
    objc_setAssociatedObject(self, shapeKey, shape, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}

- (MAShape *)shape {
    return objc_getAssociatedObject(self, shapeKey);
}

@end

@implementation WXMapRenderer
{
    @private BOOL _viewLoaded;
}

@synthesize path = _path;
@synthesize strokeColor = _strokeColor;
@synthesize strokeWidth = _strokeWidth;
@synthesize strokeOpacity = _strokeOpacity;
@synthesize strokeStyle = _strokeStyle;

- (instancetype)initWithRef:(NSString *)ref
                       type:(NSString*)type
                     styles:(nullable NSDictionary *)styles
                 attributes:(nullable NSDictionary *)attributes
                     events:(nullable NSArray *)events
               weexInstance:(WXSDKInstance *)weexInstance
{
    self = [super initWithRef:ref type:type styles:styles attributes:attributes events:events weexInstance:weexInstance];
    if (self) {
        _path = [attributes wxmap_safeObjectForKey:@"path"];
        _strokeColor = [attributes wxmap_safeObjectForKey:@"strokeColor"];
        _strokeWidth = [[attributes wxmap_safeObjectForKey:@"strokeWidth"] doubleValue];
        _strokeOpacity = [[attributes wxmap_safeObjectForKey:@"strokeOpacity"] doubleValue];
        _strokeStyle = [attributes wxmap_safeObjectForKey:@"strokeStyle"];
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
        [(WXMapViewComponent *)self.supercomponent addOverlay:self];
        _viewLoaded = YES;
    }
}
*/
 
- (void)updateAttributes:(NSDictionary *)attributes
{
    WXMapViewComponent *mapComponent = (WXMapViewComponent *)self.supercomponent;
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
