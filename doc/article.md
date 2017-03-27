### weex-amap 使用指南


<img width="320" src="https://img.alicdn.com/tps/TB19sYlPFXXXXaRaXXXXXXXXXXX-600-450.png" />

[weex-amap](https://github.com/weex-plugins/weex-amap) 一款高德地图weex插件，它具备了地图的基本使用功能，包括地图展示，添加坐标点，控制地图缩放，在地图上添加折线或者圆形等图形，同时也具备地图的一些基本计算和判断等功能。而且如同 Weex 的能力，它是三端都支持运行，这样你可以通过一套代码实现三端的地图功能。 

使用效果预览(iOS版)：

<img src="https://img.alicdn.com/tps/TB1c5BKQXXXXXckXpXXXXXXXXXX-367-633.gif" />

### 快速开始

使用插件，我们需要初始化一个工程项目，然后将插件添加进去。

``` bash
weex create mapapp
cd mapapp
# 你可以自行添加ios 或者 android
weex platform add ios 
# 添加地图插件
weex plugin add weex-amap
```
*请确保你安装了最新的 [weex-toolkit](https://github.com/weexteam/weex-toolkit)*

这样你的项目里就具备了高德地图的插件功能。

先来一段基本的地图展示，编辑你的weex文件

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

编辑完后，你可以连接你的手机，然后通过

``` bash
weex run android
```
实现手机上的预览。

### weex-amap基本功能介绍


####地图组件  weex-amap 

我们要在地图上显示一个基本的地图，可以直接使用:

``` weex
<weex-amap props...></weex-amap>
```
通过设置不同的属性来实现地图控制。

常见的属性列表如下：

| 属性        | 类型         | Demo  | 描述  |
| ------------- |:-------------:| -----:|----------:|
| center     | array | [116.487, 40.00003] | 传入地理位置坐标[x,y] 默认为当前定位位置 |
| zoom      | number     |  11 | 缩放级别 |
| zoomEnable | boolean  | true | 是否允许缩放
| marker |  array | [`{position:[116,12]}]` |  点标记物的属性
| geolocation  | boolean | true | 添加定位控件
| sdkKey   | object | {ios:'xxx',android: 'xxx',h5: 'xxx'} | 指定开发者的 SDK 密匙 
*建议你前往[高德开发者社区](http://lbs.amap.com/)申明你对应产品的Key，保证地图正常工作*

它支持绑定的事件:

|事件    |     描述   |
| ------------- |----------:|
|zoomchange | 用户缩放改变  | 
|dragend | 用户拖拽完成  | 



#### 点标记 weex-amap-marker  

我们可以在地图上添加一系列点标记来展示特定地理位置的一些信息：

``` bash
<template>
  <div class="container">
    <weex-amap class="map" id="map2017"  map-instance={{map}} zoom={{zoom}} center={{pos}}>
      <weex-amap-marker position="{{point.position}}" title="{{point.title}}" icon="{{point.icon}}" repeat="point in pointArr"  onclick="{{markerClick}}"></weex-amap-marker>
    </weex-amap>
  </div>
</template>

<style>
  .container{
    position: relative;
    flex:1;
    background-color: #fff;
  }
  .map{
    flex: 1;
    position: relative;
    background-color: #fff;
    min-height: 800;
    border-bottom-width: 10;
    border-bottom-color: #fff;
  }
</style>

<script>
 
  module.exports = {
    data: {
      pos: [116.487, 40.00003],
      zoom: 9,
      pointArr: [
        {
          position:[116.3944071,39.9278548],
          title: '北京市',
          events: {
            click: function() {
              modal.alert('北京市区');
            }  
          }
        },
        {
          position: [116.4848977,40.0004496],
          title: '火箭',
          icon: 'http://img1.vued.vanthink.cn/vued6db28fd538a0446a3ab2fa2694d1ba57.gif'
        }
      ],
    },
  }
</script>
```
它可以通过下面的属性来控制:

| 属性        | 类型         | Demo  | 描述  |
| ------------- |:-------------:| -----:|----------:|
| position     | array | [116.487, 40.00003] | 传入地理位置坐标[x,y] 默认为当前定位位置 |
| icon | string     |    some_icon_url | 图标的url地址 |
| title | string   |   'this is a marker' | 坐标点的名称 |

它支持的基本的绑定事件：

|事件    |     描述   |
| ------------- |----------:|
|click | 用户点击标记物 | 


### 折线组件 weex-amap-polyline


在地图上绘制一段折线：

``` bash
<weex-amap>
	<weex-amap-polyline path="{{path}}" stroke-color="#000" stroke-wdith="2"></weex-amap-polyline>
</weex-amap>
```

<img src="https://gw.alicdn.com/tfs/TB1HtXvQpXXXXawXXXXXXXXXXXX-682-806.png" />

你可以控制它的折线路径以及颜色宽度等。
| 属性        | 类型         | Demo  | 描述  |
| ------------- |:-------------:| -----:|----------:|
| path     | array | [[116.487, 40.00003],[113.487, 40.0002]...]| 折线的节点坐标数组 |
| stroke-color | string     |    #000 | 线条颜色 |
| stroke-width | number   |   2 | 线条宽度 |
| stroke-opacity | number  | 0.5  | 线条透明度[0-1]
| stroke-style   | string  | solid | 线条的样式 实线:solid，虚线:dashed



#### 多边形组件 weex-amap-polygon

在地图上绘制多边形:
``` bash
<weex-amap class="map" id="map2017" sdk-key={{keys}} zoom={{zoom}} center={{pos}}>
      <weex-amap-polygon path="{{path}}" fill-opacity="0.5" fill-color="#2ecc71" fill-width="4"></weex-amap-polygon>
    </weex-amap>
```
<img src="https://gw.alicdn.com/tfs/TB1q53IQXXXXXXzaFXXXXXXXXXX-668-800.png" />

你可以自定义它的形状，填充的颜色，以及描边样式：

| 属性        | 类型         | Demo  | 描述  |
| ------------- |:-------------:| -----:|----------:|
| path     | array | [[116.487, 40.00003],[113.487, 40.0002]...]| 多边形轮廓线的节点坐标数组 |
| fill-color | string     |    #000 | 多边形填充颜色 |
| fill-opacity | string     |    #000 | 多边形填充透明度 |
| stroke-color | string     |    #000 | 线条颜色 |
| stroke-width | number   |   2 | 线条宽度 |
| stroke-opacity | number  | 0.5  | 线条透明度[0-1]
| stroke-style   | string  | solid | 线条的样式 实线:solid，虚线:dashed


### 圆形组件 weex-amap-circle

在地图上绘制圆形

``` html
<weex-amap class="map" id="map2017" sdk-key={{keys}} zoom={{zoom}} center={{pos}}>
    <weex-amap-circle center="{{pos}}" radius="7000" fill-color="#2ecc71" fill-width="4"></weex-amap-circle>
 </weex-amap>
```
<img src="https://gw.alicdn.com/tfs/TB1kjw5QXXXXXaAXVXXXXXXXXXX-656-724.png" />

你可以控制它的大小，圆心和颜色:

| 属性        | 类型         | Demo  | 描述  |
| ------------- |:-------------:| -----:|----------:|
| center     | array | [116.346, 40.234234]| 圆形位置 |
| radius | number     |    50 | 圆的半径 |
| fill-color | string     |    #000 | 圆的填充颜色 |
| fill-opacity | string     |    #000 | 圆的填充透明度 |
| stroke-color | string     |    #000 | 圆的轮廓线条颜色 |
| stroke-width | number   |   2 | 圆的轮廓线条宽度 |
| stroke-opacity | number  | 0.5  | 圆的轮廓线条透明度[0-1]
| stroke-style   | string  | solid | 圆的轮廓线条的样式 实线:solid，虚线:dashed


#### 自定义信息窗体 weex-amap-info-window

``` bash 
<weex-amap class="map" id="map2017" sdk-key={{keys}} zoom={{zoom}} center={{pos}}>
      <weex-amap-marker style="z-index:1000" hide-callout="true" position="{{marker.position}}" icon="{{marker.icon}}" title="{{marker.title}}"></weex-amap-marker>
      <weex-amap-marker style="z-index:1000" hide-callout="true" position="{{marker2.position}}" icon="{{marker.icon}}" title="{{marker2.title}}"></weex-amap-marker>
      <weex-amap-info-window class="info-win" offset="{{palaceMuseum.offset}}"  open="{{palaceMuseum.open}}" position="{{palaceMuseum.position}}">
        <info-window src="http://img1.vued.vanthink.cn/vued6dfd998fc0738f7e88d4b66bafc547ce.jpeg" title="The Palace Museum" location="4 Jingshan Front St, Dongcheng Qu, Beijing"></info-window>
      </weex-amap-info-window>
      <weex-amap-info-window class="info-win" offset="{{southStation.offset}}" open="{{southStation.open}}" position="{{southStation.position}}">
        <info-window  src="http://img1.vued.vanthink.cn/vued2de302ef72ae921313a1fa1bbbbd9455.jpeg" title="Beijing South Railway Station" location="Fengtai, Beijing"></info-window>
      </weex-amap-info-window>
    </weex-amap>
```
<img src="https://gw.alicdn.com/tfs/TB1JugBQXXXXXczapXXXXXXXXXX-692-840.png" />

你可以在地图上自定义显示的信息窗体的样式，通过写子组件的形式。

*⚠️ SDK限制，一个地图上只能显示一个信息窗体，需要自己实现逻辑控制*

| 属性        | 类型         | Demo  | 描述  |
| ------------- |:-------------:| -----:|----------:|
| position     | array | [[116.487, 40.00003]| 在地图上的位置 |
| open | boolean     |    true | 是否在地图上打开 |
| offset | array     |    偏移 | 相对定位点坐标偏移 |
| children | weex comonnet     |    <text>This is a info window</text> | 窗体的内容 |
||



#### Amap 地图模块

有的时候我们需要实现一些定位和计算功能。这个时候我们可以引入`amap` 模块。


``` html 
<template>
  <weex-amap class="map" id="map2017" center="{{pos}}" ></weex-amap>
  <div class="btn-wrap">
    <div onclick="setUserLocation" class="btnbox"><text class="btn" >set location </text></div>
    <text class="tips">进行当前定位</text>
  </div>
</template>

<script>
  // 使用 amap 模块
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
它具备的一些基本方法;

#####  getUserLocation(completeFunc,errorFunc)

+ completeFunc 定位成功后的回调函数，返回的数据:
```
{ 
  data:{
    position: []
  },
  result: 'success' 
}
```

*⚠️ 使用定位  `weex-amap` 组件 必须设置 `geolocation="true"` 使用定位插件*
##### getLineDistance

计算两个标记点的距离

该方法接收三个参数 进行计算比如:

+  `coor1` 坐标1

+ `coor2` 坐标2

+ `callback `计算完成后的回调 会返回一个计算出的具体距离，单位 米

``` js
//...
amap.getLineDistance(this.marker1.position, this.marker2.position, (res) => {
  if (res.result == 'success') {
    this.distance = '两点相距' + res.data + '米';
    console.log(res.data.distance + '米');
  } else {
    console.log('计算失败');
  }
})   
```

##### polygonContainsMarker

判断几何形是否包含某个点 

该方法接收两个参数，返回一个boolean值

+ coor 点的坐标

+ polygonRef 多边形的ref

+  callbcak 计算完成后的回调 会返回一个运算的结果,其中data字段是个boolean，表示是否包含

``` 
amap.polygonContainsMarker([114.23423, 43.2222], this.$ref('polygon2017'), (res) => {
  if (res.result == 'success') {
    console.log(res.data ? '存在' : '不存在' );
  }
})
```

### 参考 Demo

使用项目 Demo ，插件 Github 项目里包含了基本的功能演示 :

[Github Demos](https://github.com/weex-plugins/weex-amap/tree/master/demos)

你只需要将 Demo 里的文件复制粘贴到你的 工程项目的src下即可。然后运行 run android or ios 即可.

直接点击[ H5 Demo](https://weex-plugins.github.io/weex-amap/)可以演示当前版本支持的功能

### 集成插件

如何将地图插件集成到自己的项目呢，请参考[weexpack文档说明](https://github.com/weexteam/weex-pack/tree/dev#5现有应用集成插件--组件容器)