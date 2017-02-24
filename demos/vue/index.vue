<template>
  <div class="container">
    <div class="bg">
      <image class="map-bg" src="http://img1.vued.vanthink.cn/vued606f6ce9ac598cf0c23e0653f4cd7d54.png" ></image>
    </div>
    <div class="map">
      <image class="logo" src="http://img1.vued.vanthink.cn/vuedf325648b3361db3cf5fdfc55510167f3.png"></image>
      <text class="title">WEEX-AMAP DEMOS</text>
    </div>
    <example-list :items="items"></example-list>
  </div>
</template>

<style>
  .container{
    position: relative;
    flex:1;
    background-color: #b5d6ea;
  }
  .bg{
    position:absolute;
    left:0;
    right:0;
    top:0;
    background-color: #b4d6eb;
  }
  .map-bg{
    flex: 1;
    height: 1130px;
  }
  .title {
    color: #1995f9;
    font-size: 38px;
    padding: 20px;
    align-items: center;
  }
  .logo-wrap{
    display: flex;
    align-items: center;
  }
  .logo{
    width: 160px;
    height:160px;
    margin-top: 40px;
  }
  .map{
    height: 300px;
    align-items: center;
  }
</style>

<script>
  import exampleList from './include/example-list.vue';
  module.exports = {
    components: {
      'example-list': exampleList
    },
    data() {
      return {
        root: '初始化',
        items: [{
          name: 'pages/init', 
          title: '初始化地图', 
          imgsrc: 'http://img1.vued.vanthink.cn/vueda6fc9eae90e26f386d08e7cf8efe90d6.png',
          keyword: 'Amap',
          desc: '如何创建一个地图'
        },
        {
          name: 'pages/center', 
          title: '设置中心', 
          imgsrc: 'http://img1.vued.vanthink.cn/vueda8593decf0b04433bbafeaa563f13f63.png',
          keyword: 'Center',
          desc: '设置或者修改地图的中心'
        },
        {
          name: 'pages/zoom', 
          title: '设置缩放', 
          imgsrc: 'http://img1.vued.vanthink.cn/vued620b795c054a92dbe6de5e52705a113b.png',
          keyword: 'Zoom',
          desc: '设置地图的缩放级别以及事件回调'
        },
        {
          name: 'pages/marker', 
          title: '设置标记点', 
          imgsrc: 'http://img1.vued.vanthink.cn/vued25f8ab41ad07d65b5cb729248a29c057.png',
          keyword: 'Marker',
          desc: '给地图添加标记点，提示用户相关地理信息'
        },
        {
          name: 'pages/geolocation', 
          title: '进行定位', 
          imgsrc: 'http://img1.vued.vanthink.cn/vued6ce9441a88f274be473e7a0fa2101512.png',
          keyword: 'Geolocation',
          desc: '使用地图进行用户的定位'
        },
        {
          name: 'pages/search', 
          title: '进行搜索', 
          imgsrc: 'http://img1.vued.vanthink.cn/vued32adfd07696fdc72d8fdecb46f106ba9.png',
          keyword: 'Search',
          desc: '使用地图搜索关键城市和服务'
        }]  
      };
    }
  }  
    

</script>
