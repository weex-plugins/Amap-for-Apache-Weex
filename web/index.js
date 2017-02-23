import amapModuleReg from './module/amap';
import Amap from './components/amap';
import AmapMarker from './components/amap-marker';
// import VueAmap from './vue-amap/index';
const components = [
  Amap,
  AmapMarker
];

function init(Weex) {
  components.forEach((comp) => {
    comp.init(Weex);
  });
  amapModuleReg(Weex);
}
module.exports = {
  init
};

