//
//  WXMapViewComponent.m
//  WeexDemo
//
//  Created by yangshengtao on 2017/1/20.
//  Copyright © 2016年 taobao. All rights reserved.
//

#import "WXMapViewComponent.h"
#import "WXImgLoaderImpl.h"
#import <objc/runtime.h>

@interface MAPointAnnotation(imageAnnotation)

@property(nonatomic, copy) NSString *iconImage;

@end

static const void *iconImageKey = &iconImageKey;

@implementation MAPointAnnotation (imageAnnotation)

@dynamic iconImage;

- (void)setIconImage:(NSString *)iconImage {
    objc_setAssociatedObject(self, iconImageKey, iconImage, OBJC_ASSOCIATION_COPY_NONATOMIC);
}

- (NSString *)iconImage {
    return objc_getAssociatedObject(self, iconImageKey);
}

@end


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
    BOOL _zoomChanged;
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
        if (attributes[@"sdkKey"]) {
            [self setAPIKey:[attributes[@"sdkKey"] objectForKey:@"ios"] ? : @""];
        }
        if ([events containsObject:@"zoomchange"]) {
            _zoomChanged = YES;
        }
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
    if (attributes[@"center"]) {
        [self setCenter:attributes[@"center"]];
    }
    
    if (attributes[@"zoom"]) {
        [self setZoomLevel:[attributes[@"zoom"] floatValue]];
    }
    
    if (attributes[@"marker"]) {
        [self setMarker:attributes[@"marker"]];
    }
    
}

- (void)addEvent:(NSString *)eventName
{
    
}

- (void)removeEvent:(NSString *)eventName
{
    
}

#pragma mark - component interface
- (void)setAPIKey:(NSString *)appKey {
    [AMapServices sharedServices].apiKey = appKey;
}

- (void)setCenter:(NSArray *)center {
    CLLocationCoordinate2D centerCoordinate;
    centerCoordinate.latitude = [center[1] doubleValue];
    centerCoordinate.longitude = [center[0] doubleValue];
    [self.mapView setCenterCoordinate:centerCoordinate];
}

- (void)setZoomLevel:(CGFloat)zoom {
    [self.mapView setZoomLevel:zoom animated:YES];
}

- (void)setMarker:(NSArray *)points {
    [self initPOIData];
    for (int i = 0; i < points.count; ++i)
    {
        NSDictionary *annotations = points[i];
        NSArray *coordinates = annotations[@"position"];
        MAPointAnnotation *a1 = [[MAPointAnnotation alloc] init];
        CLLocationCoordinate2D coordinate;
        coordinate.latitude = [coordinates[1] doubleValue];
        coordinate.longitude = [coordinates[0] doubleValue];
        a1.coordinate = coordinate;
        a1.title      = [NSString stringWithFormat:@"%@", annotations[@"title"]];
        a1.iconImage = annotations[@"icon"] ? : nil;
        [_annotations addObject:a1];
        [self.mapView addAnnotations:_annotations];
        [self.mapView showAnnotations:_annotations edgePadding:UIEdgeInsetsMake(20, 20, 20, 80) animated:NO];
    }
}

#pragma mark - publish method
- (NSDictionary *)getUserLocation {
    if(self.mapView.userLocation.updating && self.mapView.userLocation.location) {
        NSArray *coordinate = @[[NSNumber numberWithDouble:self.mapView.userLocation.location.coordinate.longitude],[NSNumber numberWithDouble:self.mapView.userLocation.location.coordinate.latitude]];
        NSDictionary *userDic = @{@"result":@"success",@"data":@{@"position":coordinate,@"title":@""}};
        return userDic;
    }
    return @{@"result":@"false",@"data":@""};
}

#pragma mark - private method
- (void)initPOIData {
    if (_annotations) {
        [_annotations removeAllObjects];
    }else {
        _annotations = [NSMutableArray arrayWithCapacity:5];
    }
}

- (void)clearPOIData {
    [_annotations removeAllObjects];
    _annotations = nil;
}

#pragma mark -
/*!
 @brief 根据anntation生成对应的View
 */
- (MAAnnotationView*)mapView:(MAMapView *)mapView viewForAnnotation:(id <MAAnnotation>)annotation {
    if ([annotation isKindOfClass:[MAPointAnnotation class]])
    {
        MAPointAnnotation *pointAnnotation = (MAPointAnnotation *)annotation;
        if (pointAnnotation.iconImage){
            static NSString *pointReuseIndetifier = @"customReuseIndetifier";
            MAAnnotationView *annotationView = (MAAnnotationView*)[mapView dequeueReusableAnnotationViewWithIdentifier:pointReuseIndetifier];
            if (annotationView == nil)
            {
                annotationView = [[MAAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:pointReuseIndetifier];
            }
            
            annotationView.canShowCallout               = YES;
            annotationView.draggable                    = YES;
            annotationView.rightCalloutAccessoryView    = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
            [[self imageLoader] downloadImageWithURL:pointAnnotation.iconImage imageFrame:CGRectMake(0, 0, 25, 25) userInfo:nil completed:^(UIImage *image, NSError *error, BOOL finished) {
                dispatch_async(dispatch_get_main_queue(), ^{
                    annotationView.image = image;
                });
            }];
            return annotationView;
        }else {
            static NSString *pointReuseIndetifier = @"pointReuseIndetifier";
            MAPinAnnotationView *annotationView = (MAPinAnnotationView*)[mapView dequeueReusableAnnotationViewWithIdentifier:pointReuseIndetifier];
            if (annotationView == nil)
            {
                annotationView = [[MAPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:pointReuseIndetifier];
            }
            
            annotationView.canShowCallout               = YES;
            annotationView.draggable                    = YES;
            annotationView.rightCalloutAccessoryView    = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
            return annotationView;
        }
    }
    
    return nil;
}

/**
 * @brief 地图将要发生缩放时调用此接口
 */
- (void)mapView:(MAMapView *)mapView mapWillZoomByUser:(BOOL)wasUserAction {
    
}

/**
 * @brief 地图缩放结束后调用此接口
 */
- (void)mapView:(MAMapView *)mapView mapDidZoomByUser:(BOOL)wasUserAction {
    if (_zoomChanged) {
        [self fireEvent:@"zoomchange" params:[NSDictionary dictionary]];
    }
}

@end
