
<template>
  <div class="dashboard-editor-container">
    <el-row style="background-color: #ffffff;margin: 10px 10px 10px 10px;padding: 10px 20px 10px 20px" >
      <el-col span="6" style="color: #ffffff">&nbsp;HOME</el-col>
      <el-col span="12">
        <el-steps :active="6"   finish-status="success">
          <el-step title="配置对接参数" >
<!--            <template slot="description">-->
<!--              <el-button size="mini" @click="$router.push('/shop/list')">设置对接参数</el-button>-->
<!--            </template>-->
          </el-step>
          <el-step title="API对接" >
<!--            <template slot="description">-->
<!--              <el-button size="mini" @click="$router.push('/shop/list')">店铺授权</el-button>-->
<!--            </template>-->
          </el-step>
          <el-step title="开启自动任务" >
<!--            <template slot="description">-->
<!--              <el-button size="mini" @click="$router.push('/shop/shop_goods/list')">拉取店铺商品</el-button>-->
<!--            </template>-->
          </el-step>

          <el-step title="数据自动对接" >
<!--            <template slot="description">-->
<!--              <el-button size="mini" @click="$router.push('/ship/manual_shipment')">去发货</el-button>-->
<!--            </template>-->
          </el-step>
        </el-steps>
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
<!--    <el-row >-->
<!--      <el-tabs>-->
<!--        <el-tab-pane label="数据推送日志" >-->
<!--          <el-table-->
<!--            :data="tableData"-->
<!--            style="width: 100%">-->
<!--            <el-table-column-->
<!--              prop="date"-->
<!--              label="日期"-->
<!--              width="180">-->
<!--            </el-table-column>-->
<!--            <el-table-column-->
<!--              prop="name"-->
<!--              label="接口"-->
<!--              width="180">-->
<!--            </el-table-column>-->
<!--            <el-table-column-->
<!--              prop="address"-->
<!--              label="内容">-->
<!--            </el-table-column>-->
<!--            <el-table-column-->
<!--              prop="address"-->
<!--              label="状态">-->
<!--            </el-table-column>-->
<!--          </el-table>-->
<!--        </el-tab-pane>-->
<!--        <el-tab-pane label="商品推送" ></el-tab-pane>-->
<!--        <el-tab-pane label="订单推送" ></el-tab-pane>-->
<!--        <el-tab-pane label="售后推送" ></el-tab-pane>-->
<!--      </el-tabs>-->
<!--    </el-row>-->
  </div>
</template>
<script>
import CountTo from 'vue-count-to'
export default {
  name: "ThirdSystemHome",
  components: {
    CountTo
  },
  data() {
    return {
      chartData:{
        waitShip:50,
        salesVolume:5989.98,
        orderCount:302,
        shopCount:8
      },
      typeList: [],
      tableData: []
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
