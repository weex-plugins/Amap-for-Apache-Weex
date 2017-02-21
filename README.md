# weex-plugin-amap

<img width="320" src="https://img.alicdn.com/tps/TB1m1l.PXXXXXczXFXXXXXXXXXX-800-600.png" />


一款高德地图weex插件，当前版本支持定位，缩放等地图常用操作。

### 快速开始

编辑你的weex文件

``` we
<template>
  <div class="container">
      <weex-amap class="map" id="map2017" scale="true" geolocation="true" center="{{pos}}" >
        <weex-amap-marker position="{{point.position}}" title="{{point.title}}"></weex-amap-marker>
      </weex-amap>
  </div>
</template>

<style>
  .container{
    position: relative;
    height: 100%;
    
  }
  .map{
    width:100%;
    height: 600;
    background-color: #000;
  }
</style>

<script>

  module.exports = {
    data: {
      pos:[116.487, 40.00003],
      point: {
        position: [112,36],
        title: 'this is a marker'
      }
    },
    
    created () {

    },
    
  }
</script>

```


### API

#### weex-amap 属性

| 属性        | 类型         | Demo  | 描述  |
| ------------- |:-------------:| -----:|----------:|
| center     | array | [116.487, 40.00003] | 传入地理位置坐标[x,y] 默认为当前定位位置 |
| zoom      | number     |  11 | 缩放级别 |
| zoomEnable | boolean  | true | 是否允许缩放
| marker |  array | [`{position:[116,12]}]` |  点标记物的属性
| geolocation  | boolean | true | 添加定位控件
| search   | object | {city:'北京',type: '餐饮'} | 初始化搜索的参数，比如城市,类型
| sdkKey   | object | {ios:'xxx',android: 'xxx',h5: 'xxx'} | 指定开发者的 SDK 密匙 


**建议你前往[高德开发者社区](http://lbs.amap.com/)申明你对应产品的Key，保证地图正常工作**

#### weex-amap 事件
|事件    |     描述   |
| ------------- |----------:|
|zoomchange | 用户缩放改变  | 
|dragend | 用户拖拽完成  | 



#### weex-amap-marker 属性 

| 属性        | 类型         | Demo  | 描述  |
| ------------- |:-------------:| -----:|----------:|
| position     | array | [116.487, 40.00003] | 传入地理位置坐标[x,y] 默认为当前定位位置 |
| icon | string     |    some_icon_url | 图标的url地址 |
| title | string   |   'this is a marker' | 坐标点的名称 |

#### weex-amap-marker 事件
|事件    |     描述   |
| ------------- |----------:|
|click | 用户点击标记物 | 


#### Amap 模块

#####  getUserLocation(mapref,completeFunc)
+ mapref 当前地图容器的ref值
+ completeFunc 定位成功后的回调函数，返回的数据:
```
{ 
  data:{
    position: []
  },
  result: 'success' 
}
```
#### search(place, completeFunc, mapRef)

+ place 表示搜索的关键词

+ completeFunc 搜索执行后的回调

+ mapRef 当前地图容器的ref值


#### searhNearBy(place, position, distance, completeFunc, mapRef)

+ place 表示搜索的关键词

+ position  搜索中心经纬度 [123.234, 36.45]

+ distance 搜索距离 比如: 5000

+ completeFunc 搜索执行后的回调

+ mapRef 当前地图容器的ref值



##### 使用Amap模块

``` html 
<template>
  <weex-amap class="map" id="map2017" center="{{pos}}" ></weex-amap>
  <div class="btn-wrap">
    <div onclick="setUserLocation" class="btnbox"><text class="btn" >set location </text></div>
    <text class="tips">进行当前定位</text>
  </div>
</template>

<script>
  const Amap = require('@weex-module/amap');
  module.exports = {
    data: {
      pos:[116.487, 40.00003]
    },
    
    methods: {
      setUserLocation() {
        const self = this;
        Amap.getUserLocation(this.$el('map2017').ref, function (data) {
          if(data.result == 'success') {
            self.pos = data.data.position;
          }
        });  
    }
  };
  
</script>
```



### Demo

####H5 demo 
直接点击[Demo](https://weex-plugins.github.io/weex-plugin-amap/)可以演示当前版本支持的功能

####用weexpack运行demo(Android／iOS／H5)

参考weexpack命令([网址](https://github.com/weexteam/weex-pack))来测试地图组件demo:

1.安装weexpack

npm install -g weexpack

2.创建工程，如MyApp

weexpack create MyApp

3.创建运行平台

cd MyApp & weexpack platform add ios (/android)

4.添加地图插件

weexpack plugin add /users/abcd/Code/weex-plugins/weex-plugin-amap (这后面是地图插件clone到本地的目录)

5.编译和运行demo

把demo文件（所有在目录plugins/weex-plugin-amap/demos/下的文件）拷贝到项目工程MyApp/src下，然后：

对H5用如下命令：weexpack build web & weexpack run web

对安卓和iOS用命令：weexpack run ios (/android) 可在模拟器或者device上运行

ios demo 如下所示

<img src="https://img.alicdn.com/tps/TB1lJ51PpXXXXX5aFXXXXXXXXXX-429-687.gif">







