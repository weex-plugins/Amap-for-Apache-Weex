## 三期需求api设计参考

### 折线组件 weex-amap-polyline

在地图上绘制折线

| 属性        | 类型         | Demo  | 描述  |
| ------------- |:-------------:| -----:|----------:|
| path     | array | [[116.487, 40.00003],[113.487, 40.0002]...]| 折线的节点坐标数组 |
| stroke-color | string     |    #000 | 线条颜色 |
| stroke-weight | number   |   2 | 线条宽度 |
| stroke-opacity | number  | 0.5  | 线条透明度[0-1]
| stroke-style   | string  | solid | 线条的样式 实线:solid，虚线:dashed

#### code example

``` bash
<weex-amap-polyline path="path" stroke-color="#000" stroke-weight="2"></weex-amap-polyline>

<script>
module.exports = {
  data: {
    path: [  
      [116.368904, 39.913423],
      [116.382122, 39.901176],
      [116.387271, 39.912501],
      [116.398258, 39.904600]
    ],
  
  }
}
</script>
```


### 多边形组件 weex-amap-polygon

在地图上绘制多边形


| 属性        | 类型         | Demo  | 描述  |
| ------------- |:-------------:| -----:|----------:|
| path     | array | [[116.487, 40.00003],[113.487, 40.0002]...]| 多边形轮廓线的节点坐标数组 |
| fill-color | string     |    #000 | 多边形填充颜色 |
| fill-opacity | string     |    #000 | 多边形填充透明度 |
| stroke-color | string     |    #000 | 线条颜色 |
| stroke-weight | number   |   2 | 线条宽度 |
| stroke-opacity | number  | 0.5  | 线条透明度[0-1]
| stroke-style   | string  | solid | 线条的样式 实线:solid，虚线:dashed


### 圆形组件 weex-amap-circle

在地图上绘制圆形


| 属性        | 类型         | Demo  | 描述  |
| ------------- |:-------------:| -----:|----------:|
| center     | array | [116.346, 40.234234]| 圆形位置 |
| radius | number     |    50 | 圆的半径 |
| fill-color | string     |    #000 | 圆的填充颜色 |
| fill-opacity | string     |    #000 | 圆的填充透明度 |
| fill-opacity | string     |    #000 | 圆的填充透明度 |
| stroke-color | string     |    #000 | 圆的轮廓线条颜色 |
| stroke-weight | number   |   2 | 圆的轮廓线条宽度 |
| stroke-opacity | number  | 0.5  | 圆的轮廓线条透明度[0-1]
| stroke-style   | string  | solid | 圆的轮廓线条的样式 实线:solid，虚线:dashed


### Amap方法  getLineDistance

计算两个标记点的距离

该方法接收三个参数 进行计算比如:

@param coor1 坐标1

@param coor2 坐标2

@param callback 计算完成后的回调 会返回一个计算出的具体距离，单位 米

``` js
//...
amap.getLineDistance(this.marker1.position, this.marker2.position, (res) => {
  if(res.result == 'success') {
    this.distance = '两点相距' + res.data + '米';
    console.log(res.data.distance + '米');
  } else {
    console.log('计算失败');
  }
})   
```

### Amap 方法  polygonContainsMarker

判断几何形是否包含某个点 

该方法接收两个参数，返回一个boolean值

@param coor 点的坐标

@param 多边形的ref

``` 
amap.polygonContainsMarker([114.23423, 43.2222], this.$ref('polygon2017'))

```

















