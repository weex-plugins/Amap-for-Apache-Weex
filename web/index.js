import markerManage  from './service/marker';
import mapLoader from './service/map-loader';
import vendor from './service/vendor';
import amapModuleRegist from './module/amap';

const defaultAttr = {
  zoom: 13,
  resizeEnable: true,
};
const loadingGif = 'http://img1.vued.vanthink.cn/vued2113eaa062abbaee0441d16a7848d23e.gif';
let params = {
  center: undefined,
  zoom:11,
  toolbar: true,
  scale: false,
  geolocation: false,
  resizeEnable: true,
};
// prototype methods.
const proto = {
  
  create () {
    this.mapwrap = document.createElement('div');
    this.mapwrap.id = vendor.gengerateRandomId('map');
    this.mapwrap.append(this.renderLoadingSpinner());
    mapLoader.load({},this.mapwrap,() => this.ready());   
    return this.mapwrap;
  },
  
  renderLoadingSpinner() {
    let el = document.createElement('div');
    el.style.height = 60;
    el.style.margin = '20 auto';
    el.style.textAlign = 'center';
    let image = document.createElement('img');
    image.src = loadingGif;
    el.appendChild(image);
    let text = document.createElement('div');
    text.appendChild(document.createTextNode('高德地图加载中....'));
    el.appendChild(text);
    return el;
  },
  
  ready () {
    let self = this;
      if(window.AMap) {
        console.log(this.mapwrap);
        this.map = new AMap.Map(this.mapwrap.id,params);
        AMap.plugin(['AMap.ToolBar','AMap.Geolocation'],() => {
          if(params.scale) {
            self.map.addControl(new AMap.ToolBar());  
          }
          if(params.geolocation) {
            self.map.addControl(new AMap.Geolocation()); 
          }
        });
        markerManage.changeMarker(markers,this.map);
        this.mapInstance = this.map;
      }   
  }
  
};

let markers = [];

const attr = {
  center (val) {
    if(Array.isArray(val) && val.length==2){
      params.center = val;   
    }
    if(window.AMap) {
      this.map.setCenter(params.center);
    }
  },
  zoom(val) {
    if(/^[0-9]+$/.test(val)) {
      params.zoom = val;   
    }
    if(window.AMap) {
      this.map.setZoom(params.zoom);
    }
  },
  marker(val) {
    if(Array.isArray(val)) { 
      markers = val;
      if(window.AMap) {
        markerManage.changeMarker(markers,this.map);
      }
    }
  },
  scale(val) {
     params.scale = val; 
  },
  geolocation(val) {
     params.geolocation = val; 
  }
  
};

function init (Weex) {
  const Component = Weex.Component;
  const extend = Weex.utils.extend;

  function Amap (data) {
    Component.call(this, data);
  }
  
  Amap.prototype = Object.create(Component.prototype);
  extend(Amap.prototype, proto);
  extend(Amap.prototype, { attr });
  extend(Amap.prototype, {
    style: extend(Object.create(Component.prototype.style), {})
  });
  extend(Amap.prototype, { event });

  Weex.registerComponent('weex-amap', Amap);
  amapModuleRegist();
}

export default { init };