<template>
  <div class="container">
    <navbar title="设置地图中心"></navbar>
    <weex-amap class="map" id="map2017" :sdk-key="keys" :zoom="zoom" :center="pos">
    </weex-amap>
    <div class="map-control">
      <text class="name">Props: center</text> 
      <text class="tips">center属性表示地图中心点坐标值，你可以设置它来改变地图的位置，比如洁面显示的[116.487, 40.00003]为中心的地图</text>
      <div @click="changeCenter" class="btnbox"><text class="btn" >Set Center</text></div>
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
</style>

<script>
  import navbar from '../include/navbar.vue';
  const Amap = weex.requireModule('amap');
  Amap.setSDKKey({
    h5:'f4b99dcd51752142ec0f1bdcb9a8ec02',
    ios: '',
    android: 'db6a973159cb0c2639ad02c617a786ae'
  });
  module.exports = {
    components: {
      navbar
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
      }
    },
    
    methods: {
      changeCenter() {
        this.pos = [116.4 + Math.random()*0.5,40.25 + Math.random()*0.25];
      }
    }
  }
</script>
