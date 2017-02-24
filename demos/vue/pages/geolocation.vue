<template>
  <div class="container">
    <navbar title="使用地图定位"></navbar>
    <weex-amap class="map" ref="map2017" :sdk-key="keys" :zoom="zoom" :center="pos">
    </weex-amap>
    <div class="map-control">
      <text class="name">Methods: getUserLocation</text> 
      <text class="tips">getUserLocation 你可以通过调用该方法来进行用户当前位置的定位</text>
      <div @click="setUserLocation" class="btnbox"><text class="btn" >定位到当前位置</text></div>
      <text class="tips">用户的当前坐标为: {{pos}}</text>
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
    border-left-color: #e74c3c;
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
  var Amap = null;
  try {
    Amap = weex.requireModule('amap');
  } catch(err) {
    console.log(err);
  }
  var modal = weex.requireModule('modal');
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
      }
    },
    methods: {
      setUserLocation() {
        const self = this;
        Amap.getUserLocation('map2017', function (data) {
          if(data.result == 'success') {
            self.pos = data.data.position;
          }
        });
      }
    }
  }
</script>
