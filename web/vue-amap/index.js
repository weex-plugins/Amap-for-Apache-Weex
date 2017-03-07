// polyfills
import './polyfills';
// 组建导入
import AMap from './components/amap.vue';
import AMapMarker from './components/amap-marker.vue';
import AMapPolygon from './components/amap-polygon.vue';
import AMapPolyline from './components/amap-polyline.vue';
import AMapCircle from './components/amap-circle.vue';
import upperCamelCase from './utils/uppercamelcase';

require('./module/amap');

const components = [
  AMap,
  AMapMarker,
  AMapPolygon,
  AMapPolyline,
  AMapCircle
];
const VueAmap = {};
VueAmap.install = function (Vue) {
  components.map((_component) => {
    VueAmap[upperCamelCase(_component.name).replace(/^WEEX/, '')] = _component;
    return Vue.component('weex-' + _component.name, _component);
  });
};
export default VueAmap;
