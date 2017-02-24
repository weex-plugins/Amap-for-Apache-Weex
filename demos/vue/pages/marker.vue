<template>
  <div class="container">
    <navbar title="设置地图标记点"></navbar>
    <weex-amap class="map" id="map2017" :sdk-key="keys" :zoom="zoom" :center="pos">
      <weex-amap-marker :position="point.position" :title="point.title" :icon="point.icon" v-for="point in pointArr"  @click="markerClick"></weex-amap-marker>
    </weex-amap>
    <div class="map-control">
      <text class="name">Element: weex-amap-marker </text> 
      <text class="tips">weex-amap-marker表示在地图上的标记点，你可以设置标记点的位置和点击事件</text>
      <div class="btn-group">
        <div @click="addPoints" class="btnbox"><text class="btn" >添加标记点</text></div>
        <div @click="removePoints" class="btnbox"><text class="btn btn-reverse" >移除标记点</text></div>
      </div>
    </div>
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
    min-height: 800px;
    border-bottom-width: 10px;
    border-bottom-color: #fff;
  }
  .map-control{
    padding-top: 20px;
    min-height: 600px;
  }
  .name{
    margin-left: 20px;
    padding-left: 20px;
    padding-top: 10px;
    padding-bottom: 10px;
    font-size: 36px;
    border-left-width: 6px;
    border-left-style: solid;
    border-left-color: #0f89f5;
    color: #222;
    text-align: left;
  }
  .tips{
    margin: 20px;
    padding: 10px;
    color:#666;
    font-size: 20px;
  }
  .btn-group{
    display: flex;
    flex-direction: row;
  }
  .btnbox{
     flex:1;
  }
  .btn{
    margin: 20px;
    padding: 20px;
    background-color: #1ba1e2;
    border-radius: 5px;
    color: #fff; 
    text-align:center;
    cursor: pointer;
    font-size: 28px;
  }
  .btn-reverse{
    background-color: #fff;
    color: #444;
    border-width: 2px;
    border-style: solid;
    border-color: #ccc;
  }
</style>

<script>
  
  import navbar from '../include/navbar.vue';
  const modal = weex.requireModule('modal');
  const Amap = weex.requireModule('amap');
  Amap.setSDKKey({
    h5:'f4b99dcd51752142ec0f1bdcb9a8ec02',
    ios: '',
    android: 'db6a973159cb0c2639ad02c617a786ae'
  });
  module.exports = {
    components: {
      'navbar': navbar
    },
    data() {
      return {
        keys: {
          h5:'f4b99dcd51752142ec0f1bdcb9a8ec02',
          ios: '',
          android: 'db6a973159cb0c2639ad02c617a786ae'
        },
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
        ]
      }
    },
    
    methods: {
      markerClick() {
        modal.alert({
          message: 'marker clicked!'
        });
      },
      
      addPoints() {
        const coor = [115.9 + Math.random()*1,39.5 + Math.random()*0.5];
        this.pointArr.push({
          position:coor,
          title:'坐标:' + coor.join(','),
          icon: 'http://img1.vued.vanthink.cn/vueda3685df0bda6b16805e380e852d99701.png'  
        });
        //this.pointArr = Array.from(this.pointArr);
      },
      
      removePoints() {
        this.pointArr.pop();
        this.pointArr = Array.from(this.pointArr);
        console.log(this.mapInstance);
      },
    }
  }
</script>
