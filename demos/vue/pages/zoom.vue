<template>
  <div class="container">
    <navbar title="设置地图缩放"></navbar>
    <weex-amap class="map" id="map2017" @zoomchange="zoomHandle" :zoom="zoom" :center="pos">
    </weex-amap>
    <div class="map-control">
      <text class="name">Props: zoom</text> 
      <text class="tips">zoom 表示地图显示的缩放级别；zoomchange可以绑定缩放完后的事件；zoom-enable表示地图是否允许缩放</text>
      <div class="btn-group">
        <div @click="increseZoom" v-if="this.zoom<13" class="btnbox"><text class="btn" >放大</text></div>
        <div @click="decreseZoom" v-if="this.zoom>3" class="btnbox"><text class="btn btn-reverse" >缩小</text></div>
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
      increseZoom() {
        if(this.zoom < 13) {
          this.zoom ++;
        }
      },
      decreseZoom() {
        if(this.zoom > 3) {
          this.zoom --;
        }
      },
      zoomHandle() {
        modal.alert({
          message: 'zoom changed!'
        });
      },
    }
  }
</script>
