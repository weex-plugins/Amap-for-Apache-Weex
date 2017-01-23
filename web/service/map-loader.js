const DEFAULT_CONFIG = {
  key: '',
  v: '1.3',
  url: 'http://webapi.amap.com/maps'
};
 
const gengerateScriptUrl = function(obj) {
  let paramArr = [];
  for(let key in obj) {
    if(key !== 'url') {
      paramArr.push(encodeURI(key + '=' + obj[key]));  
    }
  }
  return obj.url += '?' + paramArr.join('&');
};

module.exports = {
  
  load(config,container,callback) {
    let newConfig = Object.assign({},DEFAULT_CONFIG,config);
    let lib = document.createElement('script');
    lib.src = gengerateScriptUrl(newConfig);
    console.log(lib.src);
    let self = this;
    lib.addEventListener('load',function() {
      window.maploaded = true;
      callback();
    });
    container.append(lib);  
  }

};