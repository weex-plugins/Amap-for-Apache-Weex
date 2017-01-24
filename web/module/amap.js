// AMap module
const methods = {
  /** get user loaction by browser and IP
  * @param {function} callback 
  * @param {function} errorCallback
  **/
  getUserLocation(mapRef,callback,errorCallback) {
    var geo = AMap.Geolocation();
    Amap.event.addEventListener(geo,'complete',callback);
    Amap.event.addEventListener(geo,'error',callback);
  }
  
  
};

module.exports = function() {
  weex.registerModule('Amap',methods);
};