
<template>
  <div class="dashboard-editor-container">
    <el-row style="background-color: #ffffff;margin: 10px 10px 10px 10px;padding: 10px 20px 10px 20px" >
      <el-col span="6" style="color: #ffffff">&nbsp;HOME</el-col>
      <el-col span="12">
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
    <el-row :gutter="40" class="panel-group">
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" >
          <div class="card-panel-icon-wrapper icon-money">
            <svg-icon icon-class="waitShip" class-name="card-panel-icon" />
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">
              待发货
            </div>
            <count-to :start-val="0" :end-val="reportData.total" :duration="3200" class="card-panel-num" />
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" >
          <div class="card-panel-icon-wrapper icon-message">
            <svg-icon icon-class="cloudWarehouse" class-name="card-panel-icon" />
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">
              云仓待发货
            </div>
            <count-to :start-val="0" :end-val="reportData.tao" :duration="3000" class="card-panel-num" />
          </div>
        </div>
      </el-col>



    </el-row>
    <el-row >
      <el-tabs>
        <el-tab-pane label="推送日志" >
          <el-table :data="LogsList" style="width: 100%">
            <el-table-column prop="pushTime" label="推送时间" width="180">
              <template slot-scope="scope">
                {{ parseTime(scope.row.pushTime) }}
              </template>
            </el-table-column>
            <el-table-column prop="pushResult" label="推送结果" width="360"></el-table-column>
            <el-table-column prop="targetName" label="目标仓库" width="380"></el-table-column>
          </el-table>
          <pagination
            v-show="total>0"
            :total="total"
            :page.sync="queryParams.pageNum"
            :limit.sync="queryParams.pageSize"
            @pagination="getOrderPushLogsList"
          />
        </el-tab-pane>

      </el-tabs>
    </el-row>
  </div>
</template>
<script>
import CountTo from 'vue-count-to'
import {waitOrderReport} from "@/api/order/order";
import {todayWaybillList} from "@/api/order/ship_order";
import {getOrderPushList} from "../../../api/cloud_warehouse";
export default {
  name: "PrintHome",
  components: {
    CountTo
  },
  data() {
    return {
      reportData:{
        waitShip:50,
        salesVolume:5989.98,
        orderCount:302,
        shopCount:8
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      // 今日取号list
      shipWaybillList: [],
      LogsList: [],
      total: 0,
    };
  },
  created() {

  },
  mounted() {
    this.getWaitOrderReport()
    this.getTodayWaybillList()
    this.getOrderPushLogsList()
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    },
    getWaitOrderReport(){
      waitOrderReport().then(resp=>{
        this.reportData = resp.data
      })
    },
    getTodayWaybillList(){
      todayWaybillList().then(resp=>{
        this.shipWaybillList = resp.rows
      })
    },
    getOrderPushLogsList(){
      getOrderPushList(this.queryParams).then(resp=>{
        this.LogsList = resp.rows
        this.total = resp.total;
      })
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
