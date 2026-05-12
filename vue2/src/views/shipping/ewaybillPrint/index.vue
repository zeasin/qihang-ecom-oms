<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick" >
      <el-tab-pane label="首页" name="printHome" >
<!--        <print-home></print-home>-->
        <div class="dashboard-editor-container">
        <el-row style="background-color: #ffffff;margin: 10px 10px 10px 10px;padding: 10px 20px 10px 20px" >
          <el-col :span="6" style="color: #ffffff">&nbsp;HOME</el-col>
          <el-col :span="12">
            <el-steps :active="6"   finish-status="success">
              <el-step title="电子面单设置" >
                <!--            <template slot="description">-->
                <!--              <el-button size="mini" @click="$router.push('/shop/list')">设置对接参数</el-button>-->
                <!--            </template>-->
              </el-step>
              <el-step title="安装平台打印组件" >
                <!--            <template slot="description">-->
                <!--              <el-button size="mini" @click="$router.push('/shop/list')">店铺授权</el-button>-->
                <!--            </template>-->
              </el-step>
              <el-step title="打单发货" >
                <!--            <template slot="description">-->
                <!--              <el-button size="mini" @click="$router.push('/shop/shop_goods/list')">拉取店铺商品</el-button>-->
                <!--            </template>-->
              </el-step>


            </el-steps>
          </el-col>

        </el-row>
          <el-tabs >
          <el-tab-pane label="待发货订单" >
        <el-row :gutter="40" class="panel-group">
          <el-col :span="6" class="card-panel-col" v-if="getShow(100)">
            <div class="card-panel" @click="handleClick(null,null,'TMALL')">
              <div class="card-panel-icon-wrapper icon-message">
                <svg-icon icon-class="tao" class-name="card-panel-icon" />
              </div>
              <div class="card-panel-description">
                <div class="card-panel-text">
                  淘宝待发货
                </div>
                <count-to :start-val="0" :end-val="reportData.tao" :duration="3000" class="card-panel-num" />
              </div>
            </div>
          </el-col>
          <el-col :span="6" class="card-panel-col" v-if="getShow(200)">
            <div class="card-panel" @click="handleClick(null,null,'JD-POP')">
              <div class="card-panel-icon-wrapper icon-shopping">
                <svg-icon icon-class="jd" class-name="card-panel-icon" />
              </div>
              <div class="card-panel-description">
                <div class="card-panel-text">
                  京东待发货
                </div>
                <count-to :start-val="0" :end-val="reportData.jd" :duration="3600" class="card-panel-num" />
              </div>
            </div>
          </el-col>
          <el-col :span="6" class="card-panel-col" v-if="getShow(300)">
            <div class="card-panel" @click="handleClick(null,null,'PDD')">
              <div class="card-panel-icon-wrapper icon-people">
                <svg-icon icon-class="pdd" class-name="card-panel-icon" />
              </div>
              <div class="card-panel-description">
                <div class="card-panel-text">
                  拼多多待发货
                </div>
                <count-to :start-val="0" :end-val="reportData.pdd" :duration="2600" class="card-panel-num" />
              </div>
            </div>
          </el-col>
          <el-col :span="6" class="card-panel-col" v-if="getShow(400)">
            <div class="card-panel" @click="handleClick(null,null,'DOUDIAN')">
              <div class="card-panel-icon-wrapper icon-people">
                <svg-icon icon-class="dou" class-name="card-panel-icon" />
              </div>
              <div class="card-panel-description">
                <div class="card-panel-text">
                  抖店待发货
                </div>
                <count-to :start-val="0" :end-val="reportData.dou" :duration="2600" class="card-panel-num" />
              </div>
            </div>
          </el-col>
          <el-col :span="6" class="card-panel-col" v-if="getShow(500)">
            <div class="card-panel" @click="handleClick(null,null,'WEISHOP')">
              <div class="card-panel-icon-wrapper icon-people">
                <svg-icon icon-class="wei" class-name="card-panel-icon" />
              </div>
              <div class="card-panel-description">
                <div class="card-panel-text">
                  视频号待发货
                </div>
                <count-to :start-val="0" :end-val="reportData.wei" :duration="2600" class="card-panel-num" />
              </div>
            </div>
          </el-col>
          <el-col :span="6" class="card-panel-col" v-if="getShow(600)">
            <div class="card-panel" @click="handleClick(null,null,'KUAISHOU')">
              <div class="card-panel-icon-wrapper icon-people">
                <svg-icon icon-class="kuai" class-name="card-panel-icon" />
              </div>
              <div class="card-panel-description">
                <div class="card-panel-text">
                  快手待发货
                </div>
                <count-to :start-val="0" :end-val="reportData.kuai" :duration="2600" class="card-panel-num" />
              </div>
            </div>
          </el-col>
          <el-col :span="6" class="card-panel-col" v-if="getShow(700)">
            <div class="card-panel" @click="handleClick(null,null,'XHS')">
              <div class="card-panel-icon-wrapper icon-people">
                <svg-icon icon-class="xhs" class-name="card-panel-icon" />
              </div>
              <div class="card-panel-description">
                <div class="card-panel-text">
                  小红书待发货
                </div>
                <count-to :start-val="0" :end-val="reportData.kuai" :duration="2600" class="card-panel-num" />
              </div>
            </div>
          </el-col>
        </el-row>
          </el-tab-pane>
          </el-tabs>
          <el-tabs >
            <el-tab-pane label="发货设置" >
              <el-row :gutter="40" class="panel-group">
              <el-col :span="3" class="card-panel-col" >
              <div class="card-panel" @click="handleLink('/ship/ewaybill_account')">
                <div class="card-panel-icon-wrapper icon-people">
                  <svg-icon icon-class="dzmdsz" class-name="card-panel-icon" />
                </div>
                <div class="card-panel-description">
                  <div class="card-panel-text">
                    电子面单设置
                  </div>
                </div>
              </div>
              </el-col>
                <el-col :span="3" class="card-panel-col" >
                  <div class="card-panel" @click="handleLink('/ship/ship_logistics_company')">
                    <div class="card-panel-icon-wrapper icon-people">
                      <svg-icon icon-class="kdsz" class-name="card-panel-icon" />
                    </div>
                    <div class="card-panel-description">
                      <div class="card-panel-text">
                        发货快递设置
                      </div>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </el-tab-pane>
          </el-tabs>
        </div>
      </el-tab-pane>
<!--      <el-tab-pane label="销售订单" name="saleOrder" lazy>-->
<!--        <print-list-offline :shop-type="0"></print-list-offline>-->
<!--      </el-tab-pane>-->
      <el-tab-pane v-for="item in typeList" :label="item.name" :key="item.id" :name="item.code" lazy>
        <print-tao v-if="item.id === 100"></print-tao>
<!--        <print-jd v-if="item.id === 200"></print-jd>-->
<!--        <print-jdvc v-if="item.id === 280"></print-jdvc>-->
        <print-jd :shopType="200" v-if="item.id === 200"></print-jd>
        <print-jd :shopType="280" v-if="item.id === 280"></print-jd>
        <print-pdd v-if="item.id === 300"></print-pdd>
        <print-dou v-if="item.id === 400"></print-dou>
        <print-wei v-if="item.id === 500"></print-wei>
        <print-ks v-if="item.id === 600"></print-ks>
        <print-xhs v-if="item.id === 700"></print-xhs>
        <print-list v-if="item.id === 999"></print-list>
      </el-tab-pane>

    </el-tabs>

  </div>
</template>

<script>
import printList from "@/views/shipping/ewaybillPrint/print_list.vue";
import printListOffline from "@/views/shipping/ewaybillPrint/print_list_offline.vue";
import printTao from "@/views/shipping/ewaybillPrint/tao/index.vue";
import printPdd from "@/views/shipping/ewaybillPrint/pdd/index.vue";
import printDou from "@/views/shipping/ewaybillPrint/dou/index.vue";
import printJd from "@/views/shipping/ewaybillPrint/jd/index.vue";
import printJdvc from "@/views/shipping/ewaybillPrint/jd/vc-order.vue";
import printWei from "@/views/shipping/ewaybillPrint/wei/index.vue";
import CountTo from 'vue-count-to'
import {listPlatform} from "@/api/shop/shop";
import {waitOrderReport} from "@/api/order/order";
import {todayWaybillList} from "@/api/order/ship_order";
import printXhs from "@/views/shipping/ewaybillPrint/xhs/index.vue";
import printKs from "@/views/shipping/ewaybillPrint/ks/index.vue";
export default {
  name: "print",
  components:{
    printKs, printXhs, CountTo,
    printTao,printPdd,printDou,printJd,printWei,printJdvc,printList,printListOffline},
  data() {
    return {
      activeName: 'printHome',
      typeList: [],
      reportData:{
        waitShip:50,
        salesVolume:5989.98,
        orderCount:302,
        shopCount:8
      },
    };
  },
  created() {

  },
  mounted() {
    listPlatform({status:0}).then(res => {
      this.typeList = res.rows.filter(x=>x.id<900);
      // this.activeName = this.typeList[0].code
      this.getWaitOrderReport()
      this.getTodayWaybillList()
    })
  },
  methods: {
    getShow(typeId){
      try{
        const  a = this.typeList.filter(item => item.id===typeId)
        return !!(a && a.length > 0);
      }catch(err){
        return false
      }
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
    handleClick(tab, event,tagName) {
      console.log(tab, event);
      if(tagName){
        this.activeName = tagName;
      }
    },
    handleLink(url){
      this.$router.push(url)
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
