<template>
  <div class="dashboard-editor-container">

    <panel-group :chart-data="report" @handleSetLineChartData="handleSetLineChartData" />

    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;" v-if="report.userType!=='10'">
      <line-chart :chart-data="lineChartData" />
    </el-row>
<!--    <el-row>-->
<!--      <el-table-->
<!--        :data="tableData"-->
<!--        style="width: 100%">-->
<!--        <el-table-column-->
<!--          prop="date"-->
<!--          label="日期"-->
<!--          width="180">-->
<!--        </el-table-column>-->
<!--        <el-table-column-->
<!--          prop="name"-->
<!--          label="姓名"-->
<!--          width="180">-->
<!--        </el-table-column>-->
<!--        <el-table-column-->
<!--          prop="address"-->
<!--          label="地址">-->
<!--        </el-table-column>-->
<!--      </el-table>-->
<!--    </el-row>-->
    <el-row :gutter="32">
<!--      <el-col :xs="24" :sm="24" :lg="8">-->
<!--        <div class="chart-wrapper">-->
<!--          <raddar-chart />-->
<!--        </div>-->
<!--      </el-col>-->
<!--      <el-col :xs="24" :sm="24" :lg="24">-->
<!--        <div class="chart-wrapper">-->
<!--          <pie-chart :chart-data="skuTopData" />-->
<!--        </div>-->
<!--      </el-col>-->
<!--      <el-col :xs="24" :sm="24" :lg="12">-->
<!--        <div class="chart-wrapper">-->
<!--          <bar-chart />-->
<!--        </div>-->
<!--      </el-col>-->
    </el-row>

    <!-- 浮动智能分析按钮 -->
    <div class="floating-ai-menu">
      <el-button type="primary" circle @click="goToAiAnalysis">
        <i class="el-icon-star-on"></i>
      </el-button>
      <div class="floating-menu-tooltip">智能分析</div>
    </div>

  </div>
</template>

<script>
import PanelGroup from './dashboard/PanelGroup'
import LineChart from './dashboard/LineChart'
import RaddarChart from './dashboard/RaddarChart'
import PieChart from './dashboard/PieChart'
import BarChart from './dashboard/BarChart'
import { todayDaily,salesDaily } from "@/api/report/report";
const lineChartData = {
  // newVisitis: {
  //   expectedData: [100, 120, 161, 134, 105, 160, 165],
  //   actualData: [120, 82, 91, 154, 162, 140, 145]
  // },
  // messages: {
  //   expectedData: [200, 192, 120, 144, 160, 130, 140],
  //   actualData: [180, 160, 151, 106, 145, 150, 130]
  // },
  // purchases: {
  //   expectedData: [80, 100, 121, 104, 105, 90, 100],
  //   actualData: [120, 90, 100, 138, 142, 130, 130]
  // },
  // shoppings: {
  //   expectedData: [130, 140, 141, 142, 145, 150, 160],
  //   actualData: [120, 82, 91, 154, 162, 140, 130]
  // }
  salesDaily: {
    date:['09-15','09-16','09-17','09-18','09-19','09-20','09-21'],
    salesVolume: [130, 140, 141, 142, 145, 150, 160],
    salesOrder: [120, 82, 91, 154, 162, 140, 130]
  }
}

export default {
  name: 'Index',
  components: {
    PanelGroup,
    LineChart,
    RaddarChart,
    PieChart,
    BarChart
  },
  data() {
    return {
      lineChartData: {
        date:[],
        salesVolume:[],
        salesOrder:[]
      },
      skuTopData:{
        data:[
          // { value: 320, name: 'AAA' },
          // { value: 240, name: 'BBB' },
          // { value: 149, name: 'CC' },
          // { value: 100, name: 'DD' },
          // { value: 59, name: 'DEEE' }
        ]
      },
      report:{
        userType:0,
        waitShip:50,
        salesVolume:5989.98,
        orderCount:302,
        shopCount:8
      }
    }
  },
  mounted() {
    // 加载统计
    todayDaily().then(resp=>{

      this.report = resp.data
    })
    salesDaily().then(resp=>{
      this.lineChartData.date=[]
      this.lineChartData.salesVolume =[]
      this.lineChartData.salesOrder =[]
      if(resp.data.list){
        resp.data.list.forEach(x=>{
          this.lineChartData.date.push(x.date)
          this.lineChartData.salesVolume.push(x.amount)
          this.lineChartData.salesOrder.push(x.count)
        })
      }

    })
  },
  methods: {
    handleSetLineChartData(type) {
      // this.lineChartData = lineChartData[type]
    },
    goToAiAnalysis() {
      this.$router.push('/ai/analysis')
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

@media (max-width:1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}

.floating-ai-menu {
  position: fixed;
  right: 30px;
  top: 50%;
  transform: translateY(-50%);
  z-index: 1000;
  display: flex;
  flex-direction: column;
  align-items: center;

  .floating-menu-tooltip {
    margin-top: 8px;
    background: rgba(0, 0, 0, 0.7);
    color: white;
    padding: 4px 8px;
    border-radius: 4px;
    font-size: 12px;
    opacity: 0;
    transition: opacity 0.3s;
    white-space: nowrap;
  }

  &:hover .floating-menu-tooltip {
    opacity: 1;
  }
}
</style>
