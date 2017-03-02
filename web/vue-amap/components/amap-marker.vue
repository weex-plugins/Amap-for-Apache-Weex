<template></template>
<script>
import registerMixin from '../mixins/register-component';
import CONST from '../utils/constant';
export default {
  name: 'amap-marker',
  mixins: [registerMixin],
  props: [
    'vid',
    'position',
    'offset',
    'icon',
    'content',
    'topWhenClick',
    'bubble',
    'draggable',
    'raiseOnDrag',
    'cursor',
    'visible',
    'zIndex',
    'angle',
    'autoRotation',
    'animation',
    'shadow',
    'title',
    'clickable',
    'shape',
    'extData',
    'label',
    'onceEvents'
  ],
  data() {
    return {
      converters: {
        shape(options) {
          return new AMap.MarkerShape(options);
        },
        shadow(options) {
          return new AMap.Icon(options);
        }
      },
      handlers: {
        zIndex(index) {
          this.setzIndex(index);
        },
        visible(flag) {
          // flag === false ? this.hide() : this.show();
        }
      }
    };
  },
  methods: {
    initComponent(options) {
      if (!options.events) {
        this.$options.propsData.events = {};
      }
      const self = this;
      CONST.MAEKER_EVENTS.forEach((ev) => {
        this.$options.propsData.events[ev] = function() {
          self.$emit(ev, {result: 'success'});
        };
      });
      console.log(options);
      this.$amapComponent = new AMap.Marker(options);
    }
  }
};
</script>
