# weex-plugin-amap

<img width="320" src="https://img.alicdn.com/tps/TB1m1l.PXXXXXczXFXXXXXXXXXX-800-600.png" />


一款高德地图weex插件，当前版本支持定位，缩放等地图常用操作，如下API。

### API

#### weex-amap 属性

| 属性        | 类型         | Demo  | 描述  |
| ------------- |:-------------:| -----:|----------:|
| center     | array | [116.487, 40.00003] | 传入地理位置坐标[x,y] 默认为当前定位位置 |
| zoom      | number     |  11 | 缩放级别 |
| zoomEnable | boolean  | true | 是否允许缩放
| marker |  array | [`{position:[116,12]}]` |  点标记物的属性
| geolocation  | boolean | true | 添加定位控件
| sdkKey   | object | {ios:'xxx',android: 'xxx',h5: 'xxx'} | 指定开发者的 SDK 密匙 

**建议你前往[高德开发者社区](http://lbs.amap.com/)申明你对应产品的Key，保证地图正常工作**

#### 点标记marker的对象属性 

| 属性        | 类型         | Demo  | 描述  |
| ------------- |:-------------:| -----:|----------:|
| position     | array | [116.487, 40.00003] | 传入地理位置坐标[x,y] 默认为当前定位位置 |
| icon | string     |    some_icon_url | 图标的url地址 |
| title | string   |   'this is a marker' | 坐标点的名称 |


#### Amap 模块

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




### 快速开始

编辑你的weex文件

``` we
<template>
  <div class="container">
      <weex-amap class="map" id="map2017" scale="true" geolocation="true" center={{pos}} points={{pointArr}} ></weex-amap>
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
      pointArr: [[100,31],[101,31],[102,31]]
    },
    
    created () {

    },
    
  }
</script>

```




### Demo

####H5 demo 
直接点击[Demo](https://weex-plugins.github.io/weex-plugin-amap/)可以演示当前版本支持的功能

####用weexpack运行demo(Android／iOS／H5)

参考weexpack命令([网址](https://github.com/weexteam/weex-pack))来测试地图组件demo:

1.安装weexpack

npm install -g weexpack

2.创建工程，如aaa

weexpack create aaa

3.创建运行平台

cd aaa & weexpack platform add ios (/android)

4.添加地图插件

weexpack plugin add /users/abcd/Code/weex-plugins/weex-plugin-amap (这后面是地图插件的本地目录)

5.编译和运行

对H5用如下命令：weexpack build web & weexpack run web

对安卓和iOS用命令：weexpack run ios (/android) 可在模拟器或者device上运行
ios demo 如下所示

<img src="http://img.alicdn.com/tps/TB1wSRvPpXXXXcOapXXXXXXXXXX-429-536.gif">










