//
//  WXMapViewMarkerComponent.m
//  Pods
//
//  Created by yangshengtao on 17/2/6.
//
//

#import "WXMapViewMarkerComponent.h"

@implementation WXMapViewMarkerComponent
@synthesize clickEvent = _clickEvent;

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
    }
    
    return self;
}

- (UIView *) loadView
{
    return nil;
}

@end
