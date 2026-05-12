<template>
  <div class="app-container">
    <el-row style="background-color: #ffffff;margin: 10px 10px 10px 10px;padding: 10px 20px 10px 20px" >
      <el-col :span="6" style="color: #ffffff">&nbsp;HOME</el-col>
      <el-col :span="12">
        <el-steps :active="6"   finish-status="success">
          <el-step title="电子面单设置" >
          </el-step>
          <el-step title="安装平台打印组件" >
          </el-step>
          <el-step title="打单发货" >
          </el-step>
          <el-step title="面单补打" >
          </el-step>
        </el-steps>
      </el-col>
    </el-row>
    <el-tabs v-model="activeName" @tab-click="handleClick" type="border-card">
      <el-tab-pane label="待发货订单" name="ShippingListJd" >
       <shipping-list :shop-type="shopType"></shipping-list>
      </el-tab-pane>
      <el-tab-pane label="已发货订单" name="ShippedListJd" lazy>
        <shipped-list :shop-type="shopType"></shipped-list>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>
import ShippingList from "@/views/shipping/ewaybillPrint/jd/shipping_list.vue";
import ShippedList from "@/views/shipping/ewaybillPrint/jd/shipped_list.vue";

export default {
  name: "PrintShipmentJd",
  components:{
    ShippingList,
    ShippedList
  },
  props: {
    shopType: {
      type: Number,  // 根据你的数据类型选择合适的类型
      required: true,
    },
  },
  data() {
    return {
      activeName: 'ShippingListJd',
      typeList: [],
    };
  },
  created() {

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
