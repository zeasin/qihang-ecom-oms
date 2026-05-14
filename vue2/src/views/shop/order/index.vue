<template>
  <div>
       <order-list :shopType="shopType" :shopId="shopId" :merchantId="merchantId"></order-list>
  </div>
</template>

<script>

import OrderList from "@/views/shop/order/order_list.vue";

export default {
  name: "ShopOrderIndex",
  components:{
    OrderList
  },
  props: {
    shopType: {
      type: Number,
      required: false,
    },
    shopId: {
      type: Number,
      required: false,
    },
    merchantId: {
      type: Number,
      required: false,
    },
  },
  data() {
    return {

    };
  },
  created() {
    console.log('店铺类型：'+this.shopType);
    console.log('商户ID：'+this.merchantId);
    console.log('店铺ID：'+this.shopId);
  },
  mounted() {

  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    }
  }
};
</script>
