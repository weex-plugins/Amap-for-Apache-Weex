//
//  WXMapViewComponent.m
//  WeexDemo
//
//  Created by yangshengtao on 2017/1/20.
//  Copyright © 2016年 taobao. All rights reserved.
//

#import "WXMapViewComponent.h"
#import "WXMapViewMarkerComponent.h"
#import "WXImgLoaderImpl.h"
#import <objc/runtime.h>

@interface MAPointAnnotation(imageAnnotation)

@property(nonatomic, copy) NSString *iconImage;
@property(nonatomic, copy) NSString *ref;

@end

static const void *iconImageKey = &iconImageKey;
static const void *refKey = &refKey;

@implementation MAPointAnnotation (imageAnnotation)

@dynamic iconImage;

- (void)setIconImage:(NSString *)iconImage {
    objc_setAssociatedObject(self, iconImageKey, iconImage, OBJC_ASSOCIATION_COPY_NONATOMIC);
}

- (NSString *)iconImage {
    return objc_getAssociatedObject(self, iconImageKey);
}

- (void)setRef:(NSString *)ref {
    objc_setAssociatedObject(self, refKey, ref, OBJC_ASSOCIATION_COPY_NONATOMIC);
}

- (NSString *)ref {
    return objc_getAssociatedObject(self, refKey);
}

@end


@interface WXMapViewComponent()

@property (nonatomic, strong) MAMapView *mapView;

@end

@implementation WXMapViewComponent
{
    CLLocationCoordinate2D _centerCoordinate;
    NSMutableDictionary *_annotations;
    CGFloat _zoomLevel;
    BOOL _showScale;
    BOOL _showGeolocation;
    BOOL _zoomChanged;
    BOOL _isDragend;
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
        if ([events containsObject:@"dragend"]) {
            _isDragend = YES;
        }
    }
    
    return self;
}

- (UIView *) loadView
{
    UIWindow *window = [UIApplication sharedApplication].keyWindow;
    CGSize windowSize = window.rootViewController.view.frame.size;
    self.mapView = [[MAMapView alloc] initWithFrame:CGRectMake(0, 0, windowSize.width, windowSize.height)];
    self.mapView.showsUserLocation = _showGeolocation;
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
    
}

- (void)addEvent:(NSString *)eventName
{
    
}

- (void)removeEvent:(NSString *)eventName
{
    
}

#pragma mark - component interface
- (void)addMarker:(WXMapViewMarkerComponent *)marker {
    [self initPOIData];
    MAPointAnnotation *a1 = [[MAPointAnnotation alloc] init];
    [self convertMarker:marker onAnnotation:a1];
    [_annotations setObject:a1 forKey:marker.ref];
    [self.mapView addAnnotation:a1];
}

- (void)convertMarker:(WXMapViewMarkerComponent *)marker onAnnotation:(MAPointAnnotation *)annotation {
    CLLocationCoordinate2D coordinate;
    coordinate.latitude = [marker.location[1] doubleValue];
    coordinate.longitude = [marker.location[0] doubleValue];
    annotation.coordinate = coordinate;
    annotation.title      = [NSString stringWithFormat:@"%@", marker.title];
    annotation.iconImage = marker.icon ? : nil;
    annotation.ref = marker.ref;
}

- (void)updateTitleMarker:(WXMapViewMarkerComponent *)marker {
    MAPointAnnotation *a1 = _annotations[marker.ref];
    a1.title = [NSString stringWithFormat:@"%@", marker.title];
    [self.mapView addAnnotation:a1];
}

- (void)updateIconMarker:(WXMapViewMarkerComponent *)marker {
    MAPointAnnotation *a1 = _annotations[marker.ref];
    a1.iconImage = marker.icon ? : nil;
    [self.mapView addAnnotation:a1];
}

- (void)updateLocationMarker:(WXMapViewMarkerComponent *)marker {
    MAPointAnnotation *a1 = _annotations[marker.ref];
    CLLocationCoordinate2D coordinate;
    coordinate.latitude = [marker.location[1] doubleValue];
    coordinate.longitude = [marker.location[0] doubleValue];
    a1.coordinate = coordinate;
    [self.mapView addAnnotation:a1];
}


- (void)removeMarker:(WXMapViewMarkerComponent *)marker {
    /*NSArray *tempAnnotations = [NSArray arrayWithArray:_annotations];
    [tempAnnotations enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        MAPointAnnotation *annotation = (MAPointAnnotation *)obj;
        if ([annotation.ref isEqualToString:marker.ref]) {
            [self.mapView removeAnnotation:annotation];
            *stop = YES;
            [_annotations removeObject:obj];
        }
    }];*/
    
    if (_annotations[marker.ref]) {
        [self.mapView removeAnnotation:_annotations[marker.ref]];
        [_annotations removeObjectForKey:marker.ref];
    }
}

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


#pragma mark - publish method
- (NSDictionary *)getUserLocation {
    if(self.mapView.userLocation.updating && self.mapView.userLocation.location) {
        NSArray *coordinate = @[[NSNumber numberWithDouble:self.mapView.userLocation.location.coordinate.longitude],[NSNumber numberWithDouble:self.mapView.userLocation.location.coordinate.latitude]];
        NSDictionary *userDic = @{@"result":@"success",@"data":@{@"position":coordinate,@"title":@""}};
        return userDic;
    }
    return @{@"resuldt":@"false",@"data":@""};
}

#pragma mark - private method
- (void)initPOIData {
    if (!_annotations) {
        _annotations = [NSMutableDictionary dictionaryWithCapacity:5];
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

/**
 * @brief 当选中一个annotation views时，调用此接口
 * @param mapView 地图View
 * @param view 选中的annotation views
 */
- (void)mapView:(MAMapView *)mapView didSelectAnnotationView:(MAAnnotationView *)view {
    MAPointAnnotation *annotation = view.annotation;
    for (WXComponent *component in self.subcomponents) {
        if ([component isKindOfClass:[WXMapViewMarkerComponent class]] &&
            [component.ref isEqualToString:annotation.ref]) {
            WXMapViewMarkerComponent *marker = (WXMapViewMarkerComponent *)component;
            if (marker.clickEvent) {
                [marker fireEvent:marker.clickEvent params:[NSDictionary dictionary]];
            }
        }
    }
}

/**
 * @brief 当取消选中一个annotation views时，调用此接口
 * @param mapView 地图View
 * @param view 取消选中的annotation views
 */
- (void)mapView:(MAMapView *)mapView didDeselectAnnotationView:(MAAnnotationView *)view {
    
}

/**
 * @brief 地图移动结束后调用此接口
 * @param mapView       地图view
 * @param wasUserAction 标识是否是用户动作
 */
- (void)mapView:(MAMapView *)mapView mapDidMoveByUser:(BOOL)wasUserAction {
    if (_isDragend) {
        [self fireEvent:@"dragend" params:[NSDictionary dictionary]];
    }
}

@end
