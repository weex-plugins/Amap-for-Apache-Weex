<template>
  <div class="navbar">
    <div class="btn-back" @click="goHome">
      <image class="btn-arrow-left" src="http://img1.vued.vanthink.cn/vued9c8e7e738ff94abee23eb69d7f1401e9.png"></image>
      <text class="btn-text">返回</text>
    </div>
    <text class="title">{{title}}</text>
  </div>
</template>
<style>
  .navbar{
    position: relative;
    display:flex;
    flex-direction: row;
    height: 88px;
    background-color: #fff;
    align-items: center;
    border-bottom-width: 2px;
    border-bottom-color: #ddd;
  }
  .btn-back{
    z-index:10;
    flex-direction: row;
    align-items: center;
    width: 180px;
  }
  .btn-arrow-left{
    width: 60px;
    height: 60px;
    margin-left: 10px;
  }
  .btn-text{
    color: #0f89f5;
  }
  .title{
    position: absolute;
    left:0;
    right:0;
    top:0;
    height: 88px;
    line-height: 88px;
    font-size: 36px;
    text-align: center;
    
  }
</style>
<script>
  const navigator = weex.requireModule('navigator');
  module.exports = {
    props: {
      title: {
        default: 'Title',
        type: String
      }
    },
    methods: {
      goHome() {
        navigator.pop();
      }
    }   
  }
</script>