<template>
  <div class="item" @click="redirect">
    <image class="item-bannar" :src="imgsrc"></image>
    <div class="item-desc">
      <div class="inner">
        <text class="keyword">{{keyword}}</text>
        <text class="details">{{desc}}</text>
      </div>
      <div class="btn-wrap">
        <text class="btn">查看详情</text>
      </div>
    </div>
  </div>
</template>

<style>
  .item{
    height: 460px;
    margin: 40px;
    margin-top: 20px;
    background-color: #fff;
    border-width:1;
    border-bottom-width: 2px;
    border-color: rgba(0,0,0,.2);
  }
  .item-bannar{
    height: 300px;
    border-width: 10px;
    border-color: #fff;
  }
  .item-desc {
    flex: 1;
    flex-direction: row;
    padding-top: 15px;
    font-size: 36px;
    color: #555;
    text-align: center;
  }
  .inner{
    flex:0.7;
    flex-direction: column;
    padding-left: 10px;
    align-items: flex-start;
  }
  .keyword{
    font-size: 40px;
    color: #1995f9;
  }
  .details{
    margin-top: ;
    font-size: 24px;
    color: #333;
  }
  .btn-wrap{
    flex:0.4;
    justify-content: center;
    align-self: right;
  }
  .btn{
    align-self: center;
    margin-left: 10px;
    display: inline-block;
    background-color: #1995f9;
    color: #fff;
    padding: 20px;
    padding-top: 10px;
    padding-bottom: 10px;
    border-radius: 30px; 
    font-size: 24px;
  }
</style>

<script>
  const navigator = weex.requireModule('navigator');
  module.exports = {
    props: {
      imgsrc: {
        type: String
      },
      keyword: {
        type: String
      },
      desc: {
        type: String
      },
      url: {
        type: String
      }
      
    },
    methods: {
      redirect: function() {
        navigator.push({
          url: this.url
        });
      }
    }
  }
</script>