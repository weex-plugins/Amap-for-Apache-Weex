//
//  WXMapInfoWindowComponent.m
//  Pods
//
//  Created by yangshengtao on 17/3/7.
//
//

#import "WXMapInfoWindowComponent.h"
#import "WXMapCustomInfoWindow.h"

@implementation WXMapInfoWindowComponent
@synthesize annotation = _annotation;
@synthesize identifier = _identifier;

- (instancetype)initWithRef:(NSString *)ref
                       type:(NSString*)type
                     styles:(nullable NSDictionary *)styles
                 attributes:(nullable NSDictionary *)attributes
                     events:(nullable NSArray *)events
               weexInstance:(WXSDKInstance *)weexInstance
{
    self = [super initWithRef:ref type:type styles:styles attributes:attributes events:events weexInstance:weexInstance];
    if (self) {
        
    }
    return self;
}

- (UIView *) loadView
{
    return [[WXMapCustomInfoWindow alloc] initWithAnnotation:_annotation reuseIdentifier:_identifier];
}

- (void)insertSubview:(WXComponent *)subcomponent atIndex:(NSInteger)index
{
    
}

@end
