//
//  WXMapCustomInfoWindow.m
//  Pods
//
//  Created by yangshengtao on 17/3/7.
//
//

#import "WXMapCustomInfoWindow.h"

@implementation WXMapCustomInfoWindow

- (id)initWithAnnotation:(id<MAAnnotation>)annotation reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithAnnotation:annotation reuseIdentifier:reuseIdentifier];
    
    if (self)
    {
        self.bounds = CGRectMake(0.f, 0.f, 150, 120);
        
        self.backgroundColor = [UIColor grayColor];
    }
    
    return self;
}

- (void)addCustomView:(UIView *)view;
{
    [self addSubview:view];
}

@end
