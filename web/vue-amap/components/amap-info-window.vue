<template>
  <div style="height:0;overflow:hidden;opacity:0;">
    <slot></slot>
  </div>
</template>
<script>
import { toLngLat } from '../utils/convert-helper';
import registerMixin from '../mixins/register-component';
export default {
  name: 'amap-info-window',
  mixins: [registerMixin],
  props: [
    'autoMove',
    'closeWhenClickMap',
    'content',
    'size',
    'offset',
    'position',
    'showShadow',
    'visible',
    'open',
    'events'
  ],

  data() {
    return {
      converters: {
      },
      handlers: {
        visible(flag) {
          flag === false ? this.close() : this.open(amap);
        },
        open(flag) {
          flag === false ? this.close() : this.open(this.G.map, [this.si.position.lng, this.si.position.lat]);
        }
      },
    };
  },
  destroyed() {
    this.$amapComponent.close();;
  },
  methods: {
    initComponent(options) {
      options.isCustom = true;
      this.$amapComponent = new AMap.InfoWindow(options);
      if (this['$el'].children[0].innerHTML) {
        this.$amapComponent.setContent(this['$el'].innerHTML);
      }
      if (this.visible !== false) {
        this.$amapComponent.open(this.$amap, toLngLat(this.position));
      }
    }
  }
};
</script>