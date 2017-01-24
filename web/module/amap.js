// AMap module
const Amap = {
  /** get user loaction by browser and IP
  * @param {function} callback 
  * @param {function} errorCallback
  **/
  getUserLocation(mapRef,callback) {
    var self = this;
    var geo = new AMap.Geolocation({
      enableHighAccuracy: true,
      timeout: 10000     
    });
    geo.getCurrentPosition(function(status, res) {
      if(status !== 'error') {
         self.sender.performCallback(callback, {
          data: {
            position: [res.position.getLng(),res.position.getLat()]
          },
          result: 'success'
        });
      } else {
        console.warn(res.message);
      }
     
    });
  }
  
};

const meta = {
  Amap: [{
    name: 'getUserLocation',
    args: ['string','function']
  }]
};

module.exports = function (Weex) {
  Weex.registerApiModule('Amap', Amap, meta);
};