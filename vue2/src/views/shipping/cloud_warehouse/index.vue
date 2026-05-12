<template>
  <div class="app-container">
    <el-row style="background-color: #ffffff;margin: 10px 10px 10px 10px;padding: 10px 20px 10px 20px" >
      <el-col :span="6" style="color: #ffffff">&nbsp;HOME</el-col>
      <el-col :span="12">
        <el-steps :active="6"   finish-status="success">
          <el-step title="配置云仓接口" >
          </el-step>
          <el-step title="推送订单数据到云仓" >
          </el-step>
          <el-step title="云仓发货" >
          </el-step>
          <el-step title="发货物流信息回传" >
          </el-step>

        </el-steps>
      </el-col>

    </el-row>
    <el-tabs v-model="activeName" @tab-click="handleClick">
<!--      <el-tab-pane label="首页" name="VendorShipHome" >-->
<!--        <CloudWarehouseShipHome></CloudWarehouseShipHome>-->
<!--      </el-tab-pane>-->
      <el-tab-pane label="订单发货推送" name="CompleteShipment" >
        <wait_ship_order_list></wait_ship_order_list>
      </el-tab-pane>
      <el-tab-pane label="拆单发货推送" name="SplitShipment" lazy>
        <wait_ship_order_item_list></wait_ship_order_item_list>
      </el-tab-pane>
      <el-tab-pane label="推送记录" name="pushList" lazy>
        <CloudWarehouseShipList></CloudWarehouseShipList>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>

import CloudWarehouseShipHome from "@/views/shipping/cloud_warehouse/home.vue";
import wait_ship_order_list from "@/views/shipping/cloud_warehouse/wait_ship_order_list.vue";
import wait_ship_order_item_list from "@/views/shipping/cloud_warehouse/wait_ship_order_item_list.vue";
import CloudWarehouseShipList from "@/views/shipping/cloud_warehouse/push_list.vue";


export default {
  name: "print",
  components:{wait_ship_order_list,wait_ship_order_item_list,CloudWarehouseShipHome,CloudWarehouseShipList},
  data() {
    return {
      activeName: 'CompleteShipment',
      typeList: [],
    };
  },
  created() {

  },
  mounted() {
    // listPlatform({status:0}).then(res => {
    //   this.typeList = res.rows.filter(x=>x.id!=999);
    //   // this.activeName = this.typeList[0].code
    // })
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    }
  }
};
</script>
