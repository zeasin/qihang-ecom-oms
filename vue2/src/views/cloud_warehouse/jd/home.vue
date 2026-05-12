
<template>
  <div class="app-container">
    <el-row style="background-color: #ffffff;margin: 10px 10px 10px 10px;padding: 10px 20px 10px 20px" >
      <el-col :span="6" style="color: #ffffff">&nbsp;HOME</el-col>
      <el-col :span="12">
        <el-steps :active="6"   finish-status="success">
          <el-step title="JD云仓参数配置" >
          </el-step>
          <el-step title="API对接" >
          </el-step>
          <el-step title="开启自动任务" >
          </el-step>

          <el-step title="云仓推送" >
          </el-step>
        </el-steps>
      </el-col>

    </el-row>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          :loading="pullLoading"
          type="success"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handlePull()"
        >更新JD云仓基本信息</el-button>
      </el-col>
    </el-row>
<!--    <el-row :gutter="40" class="panel-group">-->
<!--      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">-->
<!--        <div class="card-panel" >-->
<!--          <div class="card-panel-icon-wrapper icon-money">-->
<!--            <svg-icon icon-class="shopping" class-name="card-panel-icon" />-->
<!--          </div>-->
<!--          <div class="card-panel-description">-->
<!--            <div class="card-panel-text">-->
<!--              推单成功-->
<!--            </div>-->
<!--            <count-to :start-val="0" :end-val="chartData.salesVolume" :duration="3200" class="card-panel-num" />-->
<!--          </div>-->
<!--        </div>-->
<!--      </el-col>-->
<!--      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">-->
<!--        <div class="card-panel" >-->
<!--          <div class="card-panel-icon-wrapper icon-message">-->
<!--            <svg-icon icon-class="stock" class-name="card-panel-icon" />-->
<!--          </div>-->
<!--          <div class="card-panel-description">-->
<!--            <div class="card-panel-text">-->
<!--              发货成功-->
<!--            </div>-->
<!--            <count-to :start-val="0" :end-val="chartData.waitShip" :duration="3000" class="card-panel-num" />-->
<!--          </div>-->
<!--        </div>-->
<!--      </el-col>-->
<!--      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">-->
<!--        <div class="card-panel" >-->
<!--          <div class="card-panel-icon-wrapper icon-shopping">-->
<!--            <svg-icon icon-class="chart" class-name="card-panel-icon" />-->
<!--          </div>-->
<!--          <div class="card-panel-description">-->
<!--            <div class="card-panel-text">-->
<!--              推送接口请求总量-->
<!--            </div>-->
<!--            <count-to :start-val="0" :end-val="chartData.orderCount" :duration="3600" class="card-panel-num" />-->
<!--          </div>-->
<!--        </div>-->
<!--      </el-col>-->


<!--      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">-->
<!--        <div class="card-panel" >-->
<!--          <div class="card-panel-icon-wrapper icon-people">-->
<!--            <svg-icon icon-class="star" class-name="card-panel-icon" />-->
<!--          </div>-->
<!--          <div class="card-panel-description">-->
<!--            <div class="card-panel-text">-->
<!--              回传接口请求总量-->
<!--            </div>-->
<!--            <count-to :start-val="0" :end-val="chartData.shopCount" :duration="2600" class="card-panel-num" />-->
<!--          </div>-->
<!--        </div>-->
<!--      </el-col>-->
<!--    </el-row>-->
    <el-row >
      <el-tabs>
        <el-tab-pane label="仓库信息" >
          <el-row :gutter="10" class="mb8">
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getCloudWarehouseList"></right-toolbar>
          </el-row>
          <el-table :data="cloudWarehouseList" style="width: 100%">
            <el-table-column prop="id" label="ID" width="88"></el-table-column>
            <el-table-column prop="warehouseNo" label="云仓编码" width="180"></el-table-column>
            <el-table-column prop="warehouseName" label="云仓名称" width="260"></el-table-column>
            <el-table-column prop="province" label="省" width="80"></el-table-column>
            <el-table-column prop="city" label="市" width="80"></el-table-column>
            <el-table-column prop="county" label="区" width="80"></el-table-column>
            <el-table-column prop="address" label="详细地址"></el-table-column>
            <el-table-column prop="contacts" label="联系人" width="80"></el-table-column>
            <el-table-column prop="phone" label="联系电话" width="120"></el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="云仓店铺" >
          <el-row :gutter="10" class="mb8">
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getCloudWarehouseShopList"></right-toolbar>
          </el-row>
          <el-table :data="cloudWarehouseShopList" style="width: 100%">
            <el-table-column prop="id" label="ID" width="88"></el-table-column>
            <el-table-column prop="shopNo" label="店铺编号" width="180"></el-table-column>
            <el-table-column prop="shopName" label="店铺名称" width="260"></el-table-column>
            <el-table-column prop="salesPlatformSourceName" label="店铺类型" width="220"></el-table-column>
            <el-table-column prop="salesPlatformShopNo" label="销售平台店铺编号" width="180"></el-table-column>
            <el-table-column prop="ownerNo" label="客户编码" width="220"></el-table-column>

            <el-table-column prop="shopContacts" label="联系人" width="80"></el-table-column>
            <el-table-column prop="shopPhone" label="联系电话" width="120"></el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="承运商" >
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button
                :loading="pullLoading"
                type="primary"
                plain
                icon="el-icon-plus"
                size="mini"
                @click="handleAddShipper"
              >手动添加承运商</el-button>
            </el-col>

            <right-toolbar :showSearch.sync="showSearch" @queryTable="getCloudWarehouseShipperList"></right-toolbar>
          </el-row>
          <el-table :data="cloudWarehouseShipperList" style="width: 100%">
            <el-table-column prop="shipperNo" label="承运商编号" width="180"></el-table-column>
            <el-table-column prop="shipperName" label="承运商名称" width="260"></el-table-column>

            <el-table-column prop="ownerNo" label="客户编码" width="220"></el-table-column>

            <el-table-column prop="contacts" label="联系人" width="80"></el-table-column>
            <el-table-column prop="phone" label="联系电话" width="120"></el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="回调URL" >
          <el-table :data="cloudWarehouseCallbackList" style="width: 100%">
            <el-table-column prop="name" label="名称" width="180"></el-table-column>
            <el-table-column prop="url" label="URL" width="700"></el-table-column>
            <el-table-column prop="remark" label="说明" ></el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>

      <!-- 添加或修改店铺对话框 -->
      <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body :close-on-click-modal="false">
        <el-form ref="form" :model="form" :rules="rules" label-width="100px">

          <el-form-item label="承运商名称" prop="shipperName">
            <el-input v-model="form.shipperName" placeholder="请输入承运商名称" />
          </el-form-item>
          <el-form-item label="承运商编号" prop="shipperNo">
            <el-input v-model="form.shipperNo" placeholder="请输入承运商编号" />
          </el-form-item>
          <el-form-item label="联系人" prop="contacts" >
            <el-input v-model="form.contacts" placeholder="请输入联系人" />
          </el-form-item>
          <el-form-item label="联系电话" prop="phone" >
            <el-input v-model="form.phone" placeholder="请输入联系电话" />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="form.status" placeholder="状态">
              <el-option label="启用" value="1"></el-option>
              <el-option label="禁用" value="0"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </el-dialog>
    </el-row>
  </div>
</template>
<script>
import CountTo from 'vue-count-to'
import {getJdCloudWarehouseList} from "@/api/cloud_warehouse/jd";
import {listCloudWarehouseShop,listCloudWarehouseShipper,addWarehouseShipper} from "@/api/cloud_warehouse/index";
import {updateJdlBaseInfo} from "@/api/cloud_warehouse/jdl_isc";

export default {
  name: "ThirdSystemHome",
  components: {
    CountTo
  },
  data() {
    return {
      chartData:{
        waitShip:0,
        salesVolume:0,
        orderCount:0,
        shopCount:0
      },
      showSearch:true,
      pullLoading:false,
      open:false,
      title:'',
      queryParams:{
        warehouseId:null
      },
      cloudWarehouseShopList: [],
      cloudWarehouseShipperList: [],
      cloudWarehouseCallbackList: [{
        name:'运单号推送回传',
        url:'https://erp.benshutech.com/prod-api/open/cloudWarehouse/feedback/jd/waybillFeedbackMsg',
        remark:'配置在京东物流开放平台应用-出库单订单运单号推送 (clpsSOWaybillFeedbackMsg)'
      },{
        name:'运单号物流轨迹回传',
        url:'https://erp.benshutech.com/prod-api/open/cloudWarehouse/feedback/jd/trajectoryFeedbackMsg',
        remark:'配置在京东物流开放平台应用-京配物流轨迹信息回传 (TrajectoryTAFeedbackMsg)'
      }],
      // 京东云仓list
      cloudWarehouseList: [],
      form:{
        warehouseId:null,
        shipperName:null,
        shipperNo:null,
        contacts:null,
        phone:null,
        status:null,
      },
      // 表单校验
      rules: {
        warehouseId: [{ required: true, message: "不能为空", trigger: "blur" }],
        shipperName: [{ required: true, message: "不能为空", trigger: "blur" }],
        shipperNo: [{ required: true, message: "不能为空", trigger: "change" }],
        status: [{ required: true, message: "不能为空", trigger: "change" }],
      },
    };
  },
  created() {

  },
  mounted() {
    if (this.$route.query.warehouseId) {
      this.queryParams.warehouseId = this.$route.query.warehouseId
    } else {
      this.queryParams.warehouseId = null;
    }
    this.getCloudWarehouseList()
    this.getCloudWarehouseShopList()
    this.getCloudWarehouseShipperList()
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    },
    getCloudWarehouseList(){
      getJdCloudWarehouseList({id:this.queryParams.warehouseId}).then(resp=>{
        this.cloudWarehouseList = resp.data
      })
    },
    getCloudWarehouseShopList(){
      listCloudWarehouseShop({warehouseId:this.queryParams.warehouseId}).then(resp=>{
        this.cloudWarehouseShopList = resp.data
      })
    },
    getCloudWarehouseShipperList(){
      listCloudWarehouseShipper({warehouseId:this.queryParams.warehouseId}).then(resp=>{
        this.cloudWarehouseShipperList = resp.data
      })
    },
    handlePull(){
      updateJdlBaseInfo({warehouseId:this.queryParams.warehouseId}).then(resp=>{
        if(resp.code===200){
          this.$modal.msgSuccess("更新成功");
          this.getCloudWarehouseList()
          this.getCloudWarehouseShopList()
          this.getCloudWarehouseShipperList()
        }else
          this.$modal.msgError(resp.msg)
      })
    },
    cancel(){
      this.open = false
    },
    /** 新增按钮操作 */
    handleAddShipper() {
      this.form.status='1'
      this.form.warehouseId = this.queryParams.warehouseId
      this.form.shipperName=null
      this.form.shipperNo=null
      this.form.contacts=null
      this.form.phone=null
      this.open = true;
      this.title = "添加承运商";
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          addWarehouseShipper(this.form).then(response => {
            this.$modal.msgSuccess("新增成功");
            this.open = false;
            this.getCloudWarehouseShipperList();
          });
        }
      });
    },
  }
};
</script>

<style lang="scss" scoped>
.panel-group {
  margin-top: 18px;

  .card-panel-col {
    margin-bottom: 32px;
  }

  .card-panel {
    height: 108px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);

    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }

      .icon-people {
        background: #40c9c6;
      }

      .icon-message {
        background: #36a3f7;
      }

      .icon-money {
        background: #f4516c;
      }

      .icon-shopping {
        background: #34bfa3
      }
    }

    .icon-people {
      color: #40c9c6;
    }

    .icon-message {
      color: #36a3f7;
    }

    .icon-money {
      color: #f4516c;
    }

    .icon-shopping {
      color: #34bfa3
    }

    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }

    .card-panel-icon {
      float: left;
      font-size: 48px;
    }

    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px;
      margin-left: 0px;

      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }

      .card-panel-num {
        font-size: 20px;
      }
    }
  }
}

@media (max-width:550px) {
  .card-panel-description {
    display: none;
  }

  .card-panel-icon-wrapper {
    float: none !important;
    width: 100%;
    height: 100%;
    margin: 0 !important;

    .svg-icon {
      display: block;
      margin: 14px auto !important;
      float: none !important;
    }
  }
}
</style>
