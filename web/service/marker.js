// a lib to manage all marker
const markers = {};
module.exports = {
  changeMarker(arr, map) {
    for (let i = 0; i < arr.length; i++) {
      const data = arr[i];
      const marker = this.findMarker(data);
      if (!marker) {
        this.addMarker(data, map);
      } else {
        this.removeMarker(data);
      }
    }
  },
  addMarker(data, map) {
    let icon = null;
    if (data.icon) {
      icon = new AMap.Icon({
        image: data.icon,
        size: new AMap.Size(64, 64)
      });
    }
    const marker = new AMap.Marker({
      position: data.position,
      title: data.title,
      icon: icon,
      map: map,
    });
    markers[this.__getMid(data)] = marker;
  },
  registerEvents(events) {
    if (typeof events === 'object') {
          
    }
  },
  removeMarker(data) {
    let marker = this.findMarker(data);
    if (marker) {
      marker.visible = true;
      marker = null;
    }
  },
  findMarker(data) {
    const mid = this.__getMid(data);
    return markers[mid];
  },
  __getMid(data) {
    return 'mid-' + data.position.join('-');
  },
  __isMaker(obj) {
    return typeof obj === 'object' && obj.CLASS_NAME === 'AMap.Marker';
  }
};
