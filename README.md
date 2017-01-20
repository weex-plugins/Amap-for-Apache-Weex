# weex-plugin-amap

<img width="320" src="https://img.alicdn.com/tps/TB1m1l.PXXXXXczXFXXXXXXXXXX-800-600.png" />


一款高德地图weex插件，支持定位，缩放等地图常用操作

### 快速开始

``` bash
weexpack plugin add weex-plugin-amap
```

你也可以使用本地插件添加，你可以clone 这个项目，然后存放到你本地目录添加
``` bash
weexpack plugin add ./weex-plugins/weex-plugin-amap
```

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
### API

#### weex-amap 属性

| 属性        | 类型         | Demo  | 描述  |
| ------------- |:-------------:| -----:|----------:|
| center     | array | [116.487, 40.00003] | 传入地理位置坐标[x,y] 默认为当前定位位置 |
| zoom      | number      |  11 | 缩放级别 |
| zoomEnable | boolean     |    false | 是否允许缩放 |
| doubleClickZoom | boolean   |   false | 是否双击放大 |


#### weex-amap-marker

| 属性        | 类型         | Demo  | 描述  |
| ------------- |:-------------:| -----:|----------:|
| position     | array | [116.487, 40.00003] | 传入地理位置坐标[x,y] 默认为当前定位位置 |
| offset      | array      |  [1,2] | 位置偏移 |
| icon | string     |    some_icon_url | 是否允许缩放 |
| content | string   |   'this is a marker' | 坐标点点击文本 |










