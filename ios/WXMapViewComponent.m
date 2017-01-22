//
//  WXMapViewComponent.m
//  WeexDemo
//
//  Created by guibao on 2016/12/16.
//  Copyright © 2016年 taobao. All rights reserved.
//

#import "WXMapViewComponent.h"
#import "WXImgLoaderImpl.h"
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
    NSMutableArray *_iconAry;
}

- (id<WXImgLoaderProtocol>)imageLoader
{
    static id<WXImgLoaderProtocol> imageLoader;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        imageLoader = [WXImgLoaderImpl new];
    });
    return imageLoader; 
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
    [self clearPOIData];
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
        [self initPOIData];
        for (int i = 0; i < points.count; ++i)
        {
            NSDictionary *annotations = points[i];
            NSArray *coordinates = annotations[@"pos"];
            if (annotations[@"icon"]) {
                [_iconAry addObject:annotations[@"icon"]];
            }
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

#pragma mark - private method
- (void)initPOIData {
    if (_annotations) {
        [_annotations removeAllObjects];
    }else {
        _annotations = [NSMutableArray arrayWithCapacity:5];
    }
    if (_iconAry) {
        [_iconAry removeAllObjects];
    }else {
        _iconAry = [NSMutableArray arrayWithCapacity:5];
    }
}

- (void)clearPOIData {
    [_annotations removeAllObjects];
    _annotations = nil;
    [_iconAry removeAllObjects];
    _iconAry = nil;
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
        MAAnnotationView *annotationView = (MAAnnotationView*)[mapView dequeueReusableAnnotationViewWithIdentifier:pointReuseIndetifier];
        if (annotationView == nil)
        {
            annotationView = [[MAAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:pointReuseIndetifier];
        }
        
        annotationView.canShowCallout               = YES;
        annotationView.draggable                    = YES;
        annotationView.rightCalloutAccessoryView    = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
        NSInteger index = [_annotations indexOfObject:annotation];
        if (_iconAry && _iconAry.count >= index) {
            NSString *icon = [_iconAry objectAtIndex:index];
            [[self imageLoader] downloadImageWithURL:icon imageFrame:CGRectMake(0, 0, 25, 25) userInfo:nil completed:^(UIImage *image, NSError *error, BOOL finished) {
                dispatch_async(dispatch_get_main_queue(), ^{
                    annotationView.image = image;
                });
            }];
        }
        return annotationView;
    }
    
    return nil;
}

@end
