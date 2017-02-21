// AMap module
const mapManager = require('../service/map-manager');

const amap = {
  /** get user loaction by browser and IP
  * @param {function} callback
  * @param {function} errorCallback
  **/
  getUserLocation(mapRef, callback) {
    const self = this;
    const geo = new AMap.Geolocation({
      enableHighAccuracy: true,
      timeout: 10000
    });
    geo.getCurrentPosition((status, res) => {
      if (status !== 'error') {
        self.sender.performCallback(callback, {
          data: {
            position: [res.position.getLng(), res.position.getLat()]
          },
          result: 'success'
        });
      } else {
        console.warn(res.message);
      }
    });
  },
  /**
  *  search place
  * @param {string} mapref
  * @param {function} callback
  **/
  search(q, callback, mapRef) {
    const map = mapManager.getMap();
    map.placeSearch.search(q, (status, res) => {
      this.sender.performCallback(callback, {
        data: res,
        result: status === 'complete' ? 'success' : 'error'
      });
    });
  },
  /**
  * search nearby
  * @param {string} q query keyword
  * @param {array} location example:[123.123, 12.123123]
  * @param {number} distance example: 1000
  **/
  searchNearBy(q, location, distance, callback, mapRef) {
    const map = mapManager.getMap();
    map.placeSearch.search(q, location, distance, (status, res) => {
      this.sender.performCallback(callback, {
        data: res,
        result: status === 'complete' ? 'success' : 'error'
      });
    });
  }
};

const meta = {
  amap: [{
    name: 'getUserLocation',
    args: ['string', 'function']
  }, {
    name: 'searchNearBy',
    args: ['string', 'function', 'string']
  }, {
    name: 'search',
    args: ['string', 'array', 'number', 'function', 'string']
  }]
};

module.exports = function (Weex) {
  Weex.registerApiModule('amap', amap, meta);
};
