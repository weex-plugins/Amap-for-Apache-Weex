<template>
  <div class="container">
    <navbar title="地图搜索"></navbar>
    <weex-amap class="map" id="map2017" :search="search" :sdk-key="keys" :zoom=zoom :center="pos">
      <weex-amap-marker :position="marker.position" :title="marker.title" v-for="marker in markers"></weex-amap-marker>
    </weex-amap>
    <div class="map-search">
      <input class="input" @change="change" placeholder="Search Places..."/>
      <div class="btn-search" @click="searchEvent">
        <image style="width:60;height:60;" src="http://img1.vued.vanthink.cn/vuedea56601586fafc6cb665126938506b35.png"></image>
      </div>
    </div>
    <div class="map-bttom-bar">
      <div class="btn-sm" @click="changeType('电影')">
        <text v-if="type!='电影'" class="btn-sm-text">电影</text>
        <text v-if="type =='电影'" class="btn-sm-text active">电影</text>
      </div>
      <div class="btn-sm" onclick="changeType('ATM')">
        <text v-if="type!='ATM'" class="btn-sm-text">ATM</text>
        <text v-if="type =='ATM'" class="btn-sm-text active">ATM</text>
      </div>
      <div class="btn-sm" onclick="changeType('快餐')">
        <text v-if="type!='快餐'" class="btn-sm-text">快餐</text>
        <text v-if="type =='快餐'" class="btn-sm-text active">快餐</text>
      </div>
      <div class="btn-sm" onclick="changeType('酒店')">
        <text v-if="type!='酒店'" class="btn-sm-text">酒店</text>
        <text v-if="type =='酒店'" class="btn-sm-text active">酒店</text>
      </div>
    </div>
  </div>
</template>

<style>
  .container{
    position: relative;
    flex:1;
    flex-direction: column;
    background-color: #fff;
  }
  .map-search{
    flex-direction: row;
    align-items: center;
    position: absolute;
    top: 108;
    left:20;
    right: 20;
    height: 88;
    padding-left: 10;
    background-color: #fff;
    border-radius: 5;
    border-bottom-width: 2;
    border-right-width: 1;
    border-color: rgba(0,0,0,.2);
  }
  
  .input{
    flex: 1;
    background-color: #fff;
    height: 70;
    margin-top: 9;
    font-size: 30;
  }
  .btn-search{
    justify-content: center;
    align-self: flex-end;
    width: 88;
    height: 88;
  }
  .map{
    flex: 1;
    position: relative;
    background-color: #fff;
    min-height: 400;
    border-bottom-width: 10;
    border-bottom-color: #fff;
  }
  .map-bttom-bar{
    flex-direction: row;
    align-items: center;
    z-index: 200;
    position:absolute;
    bottom: 0;
    left:0;
    right:0;
    height: 88;
    line-height: 88;
    background-color: #fff;
    border-top-width: 1;
    border-top-color: rgba(0,0,0,.15);
  }
  .btn-sm{
    flex: 1;
    justify-content: center;
    align-items: center;
  }
  .btn-sm-text{
    color: #777;
    font-size: 28;
  }
  .active{
    color: #1ba1e2;
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
  var modal = weex.requireModule('amap');
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
        pos: [104.093731,30.675117],
        zoom: 13,
        search: {
          city: '成都'
        },
        place: '',
        markers: [],
        type: ''
      };
    },
    methods: {
      change(event) {
        this.place = event.value;
      },
      searchEvent() {
        if(!this.place) {
          modal.alert({
            message: 'empty word'
          });
          return;
        }
        Amap.search(this.place, (res) => {
          if(res.result == 'success') {
            let newarr = [];
            if(res.data.info === 'OK' ) {
              const arr = res.data.poiList.pois;
              
              arr.forEach((item) => {
                newarr.push({
                  title: item.name,
                  position: [item.location.L, item.location.I]  
                });
              });
            }
            //this.markers = newarr;
          } else {
            modal.alert({
              message: res.data
            });
          }
        });
      },
      changeType(q) {
        this.type = q;
        Amap.searchNearBy(this.type, this.pos, 1000, (res) => {
          if(res.result == 'success') {
            let newarr = [];
            if(res.data.info === 'OK' ) {
              const arr = res.data.poiList.pois;
              
              arr.forEach((item) => {
                newarr.push({
                  title: item.name,
                  position: [item.location.L, item.location.I]  
                });
              });
            }
            //this.markers = newarr;
          } else {
            modal.alert({
              message: res.data
            });
          }
        });
      }
    }
  }
</script>
