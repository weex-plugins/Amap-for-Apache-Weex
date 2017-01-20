//
//  WXMapViewComponent.m
//  WeexDemo
//
//  Created by guibao on 2016/12/16.
//  Copyright © 2016年 taobao. All rights reserved.
//

#import "WXMapViewComponent.h"

@interface WXMapViewComponent()

@property (nonatomic, strong) MAMapView *mapView;

@end

@implementation WXMapViewComponent
{
    CLLocationCoordinate2D _centerCoordinate;
    NSMutableArray *_annotations;
    CGFloat _zoomLevel;
    BOOL _showScale;
    BOOL _showGeolocation;
}


- (instancetype)initWithRef:(NSString *)ref
                       type:(NSString*)type
                     styles:(nullable NSDictionary *)styles
                 attributes:(nullable NSDictionary *)attributes
                     events:(nullable NSArray *)events
               weexInstance:(WXSDKInstance *)weexInstance
{
    self = [super initWithRef:ref type:type styles:styles attributes:attributes events:events weexInstance:weexInstance];
    if (self) {
        _centerCoordinate.latitude = [attributes[@"center"][1] doubleValue];
        _centerCoordinate.longitude = [attributes[@"center"][0] doubleValue];
        _zoomLevel = [attributes[@"zoom"] floatValue];
        _showScale = [attributes[@"scale"] boolValue];
        _showGeolocation = [attributes[@"geolocation"] boolValue];
        
    }
    
    return self;
}

- (UIView *) loadView
{
    UIWindow *window = [UIApplication sharedApplication].keyWindow;
    CGSize windowSize = window.rootViewController.view.frame.size;
    self.mapView = [[MAMapView alloc] initWithFrame:CGRectMake(0, 0, windowSize.width, windowSize.height)];
    self.mapView.showsUserLocation = YES;
    self.mapView.delegate = self;
    
    return self.mapView;
}

- (void)viewDidLoad
{
    self.mapView.showsScale = _showScale;
    [self.mapView setCenterCoordinate:_centerCoordinate];
    [self.mapView setZoomLevel:_zoomLevel];
}

- (void)layoutDidFinish
{
    
}

- (void)viewWillUnload
{
    
}

- (void)dealloc
{
    
}

- (void)updateAttributes:(NSDictionary *)attributes
{
    if (attributes[@"center"] ) {
        CLLocationCoordinate2D centerCoordinate;
        centerCoordinate.latitude = [attributes[@"center"][1] doubleValue];
        centerCoordinate.longitude = [attributes[@"center"][0] doubleValue];
        [self.mapView setCenterCoordinate:centerCoordinate];
    }
    
    if (attributes[@"points"]) {
        NSArray *points = attributes[@"points"];
        if (!_annotations) {
            _annotations = [NSMutableArray arrayWithCapacity:5];
        }
        for (int i = 0; i < points.count; ++i)
        {
            NSDictionary *annotations = points[i];
            NSArray *coordinates = annotations[@"pos"];
            MAPointAnnotation *a1 = [[MAPointAnnotation alloc] init];
            CLLocationCoordinate2D coordinate;
            coordinate.latitude = [coordinates[1] doubleValue];
            coordinate.longitude = [coordinates[0] doubleValue];
            a1.coordinate = coordinate;
            a1.title      = [NSString stringWithFormat:@"%@", annotations[@"title"]];
            [_annotations addObject:a1];
            [self.mapView addAnnotations:_annotations];
            [self.mapView showAnnotations:_annotations edgePadding:UIEdgeInsetsMake(20, 20, 20, 80) animated:YES];
        }
    }
}

- (void)addEvent:(NSString *)eventName
{
    
}

- (void)removeEvent:(NSString *)eventName
{
    
}


#pragma mark - 
/*!
 @brief 根据anntation生成对应的View
 @param mapView 地图View
 @param annotation 指定的标注
 @return 生成的标注View
 */
- (MAAnnotationView*)mapView:(MAMapView *)mapView viewForAnnotation:(id <MAAnnotation>)annotation {
    if ([annotation isKindOfClass:[MAPointAnnotation class]])
    {
        static NSString *pointReuseIndetifier = @"pointReuseIndetifier";
        MAPinAnnotationView *annotationView = (MAPinAnnotationView*)[mapView dequeueReusableAnnotationViewWithIdentifier:pointReuseIndetifier];
        if (annotationView == nil)
        {
            annotationView = [[MAPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:pointReuseIndetifier];
        }
        
        annotationView.canShowCallout               = YES;
        annotationView.animatesDrop                 = YES;
        annotationView.draggable                    = YES;
        annotationView.rightCalloutAccessoryView    = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
        annotationView.pinColor                     = [_annotations indexOfObject:annotation] % 3;
        
        return annotationView;
    }
    
    return nil;
}

@end
