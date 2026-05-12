<template>
  <div>
       <order-list-tl v-if="shopType===911"></order-list-tl>
       <order-list :shopType="shopType" :shopId="shopId" :merchantId="merchantId" v-else></order-list>
  </div>
</template>

<script>
import OrderListTl from "@/views/thirdSystem/tanglang/order_index.vue";
import OrderList from "@/views/shop/order/order_list.vue";

export default {
  name: "ShopOrderIndex",
  components:{
    OrderList,OrderListTl
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
