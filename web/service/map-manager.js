/** map instance manager
* 20170204
**/
module.exports = {
  initMap(id, map) {
    if (!this.__maps) {
      this.__maps = {};
    }
    this.__maps.set(id, map);
  },
  getMap(id) {
    if (!id) {
      return Object.keys(this.__maps)[0];
    }
    return this.__maps[id];
  }
};
