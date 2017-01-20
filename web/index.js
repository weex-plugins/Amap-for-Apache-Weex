'use strict'

const defaultAttr = {
  zoom: 13,
  resizeEnable: true,
}

let params = {
  center: undefined,
  zoom:11,
  toolbar: true,
  scale: false,
  geolocation: false,
  resizeEnable: true,
};

let points = [];


function addMarker(map) {
  for(let i = 0; i < points.length; i ++){
    let point = Array.from(points[i]);
     let marker = new AMap.Marker({
        position: point.pos,
        map: map,
      });
  }
}
// prototype methods.
const proto = {
  create () {
    
    this.mapwrap = document.createElement('div');
    this.mapwrap.id = 'map' + (new Date()).getTime().toString().substring(9,3) + parseInt(Math.random() * 10000);
    this.mapwrap.append(document.createTextNode('高德地图加载中...'));
    let lib = document.createElement('script');
    lib.src = 'http://webapi.amap.com/maps?v=1.3';
    let self = this;
    lib.addEventListener('load',function() {
      window.maploaded = true;
      self.ready();
    })
    this.mapwrap.append(lib);    
    return this.mapwrap;
  },
  ready () {
    let self = this;
      if(window.AMap) {
        this.map = new AMap.Map(self.mapwrap.id,params);
        AMap.plugin(['AMap.ToolBar','AMap.Geolocation'],() => {
          if(params.scale) {
            self.map.addControl(new AMap.ToolBar());  
          }
          if(params.geolocation) {
            self.map.addControl(new AMap.Geolocation()); 
          }
        });
        addMarker(this.map);
      }   
  }
  
}




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
    if(/^[0-9]$/.test(val)) {
      params.zoom = val;   
    }
  },
  points(val) {
    if(Array.isArray(val)) {
      points = val;   
    }
    if(window.AMap) {
      addMarker(this.map);
    }
  },
  scale(val) {
     params.scale = val; 
  },
  geolocation(val) {
     params.geolocation = val; 
  },
  
}

// style setters
const style = {
  
}

// event config
const event = {
  
}

function init (Weex) {
  const Component = Weex.Component
  const extend = Weex.utils.extend

  function Amap (data) {
    Component.call(this, data)
  }

  Amap.prototype = Object.create(Component.prototype);
  extend(Amap.prototype, proto)
  extend(Amap.prototype, { attr })
  extend(Amap.prototype, {
    style: extend(Object.create(Component.prototype.style), style)
  })
  extend(Amap.prototype, { event });

  Weex.registerComponent('weex-amap', Amap);
}

export default { init }
