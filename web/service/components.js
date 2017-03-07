// manage components

import amapManager from './map-manager';
import vendor from './vendor';

const components = {
  registerComponent(componentName, options, id) {
    const opts = options || {};
    if (!this._components) {
      this._components = {};
    }
    amapManager.addReadyCallback((mapIns) => {
      opts.map = mapIns;
      // options.center = new AMap.LngLat(options.center[0],options.center[1]);
      const className = vendor.setFirstLetterToUppercase(componentName);
      this._components[id] = new AMap[className](options);
    });
  },
  getComponent(id) {
    if (!this._components) {
      return null;
    }
    console.log(this._components);
    if (!id) {
      return vendor.getObjectFirstVal(this._components);
    }
    return this._components[id];
  },
  getComponentMap() {
    return amapManager.getMap();
  }
};

module.exports = components;
