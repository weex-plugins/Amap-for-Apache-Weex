// AMap module
import mapManager from '../managers/amap-manager';
import { initAMapApiLoader } from '../services/injected-amap-api-instance';

const amap = {
  /** set sdk jey and load sdk
  * @param {object} sdk key {h5:'xxxx'}
  **/
  setSDKKey(keys) {
    initAMapApiLoader({ key: keys.h5 });
  },
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
  search(q, callback) {
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
  searchNearBy(q, location, distance, callback) {
    const map = mapManager.getMap();
    map.placeSearch.search(q, location, distance, (status, res) => {
      this.sender.performCallback(callback, {
        data: res,
        result: status === 'complete' ? 'success' : 'error'
      });
    });
  },
  /** get distance between two position
  * @param coor1
  * @param corr2
  * @param callback
  **/
  getLineDistance(coor1, coor2, callback) {
    const lnglat = new AMap.LngLat(coor1[0], coor1[1]);
    const result = lnglat.distance(coor2);
    this.sender.performCallback(callback, {
      result: !result ? 'fail' : 'success',
      data: {
        distance: result
      }
    });
  },
  /** tell if the marker in a polygon
  * @param coor the marker position
  * @param polygonRef
  **/
  polygonContainsMarker(coor, polygonRef, callback) {
    const polygonCom = polygonRef['$amapComponent'];
    const result = polygonCom.contains(coor);
    this.sender.performCallback(callback, {
      result: !result ? 'fail' : 'success',
      data: result
    });
  }
};

if (window.weex) {
  window.weex.registerModule('amap', amap);
}
