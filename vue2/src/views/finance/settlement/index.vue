<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="结算单号" prop="settlementNo">
        <el-input
          v-model="queryParams.settlementNo"
          placeholder="请输入结算单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="订单编号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入订单编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="待确认" :value="0" />
          <el-option label="已确认" :value="1" />
        </el-select>
      </el-form-item>
      <el-form-item label="结算时间">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-refresh"
          size="mini"
          @click="handleProcessSettlement"
        >手动结算</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="settlementList">
      <el-table-column label="ID" align="center" prop="id" width="80" />
      <el-table-column label="结算单号" align="left" prop="settlementNo" min-width="150" />
      <el-table-column label="订单编号" align="left" prop="orderNo" min-width="150" />
      <el-table-column label="订单销售额" align="right" prop="salesAmount" width="120">
        <template slot-scope="scope">
          ￥{{ (scope.row.salesAmount).toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column label="商品采购成本" align="right" prop="purchaseCost" width="120">
        <template slot-scope="scope">
          ￥{{ (scope.row.purchaseCost).toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column label="发货费用" align="right" prop="shippingFee" width="100">
        <template slot-scope="scope">
          ￥{{ (scope.row.shippingFee).toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column label="平台扣点" align="right" prop="platformFee" width="100">
        <template slot-scope="scope">
          ￥{{ (scope.row.platformFee).toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column label="营销费用" align="right" prop="marketingFee" width="100">
        <template slot-scope="scope">
          ￥{{ (scope.row.marketingFee).toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column label="其他费用" align="right" prop="otherFee" width="100">
        <template slot-scope="scope">
          ￥{{ (scope.row.otherFee).toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column label="总成本" align="right" prop="totalCost" width="100">
        <template slot-scope="scope">
          ￥{{ (scope.row.totalCost).toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column label="订单利润" align="right" prop="profit" width="120">
        <template slot-scope="scope">
          <span :style="{ color: scope.row.profit >= 0 ? '#67C23A' : '#F56C6C', fontWeight: 'bold' }">
            ￥{{ (scope.row.profit).toFixed(2) }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="利润率" align="center" prop="profitRate" width="100">
        <template slot-scope="scope">
          {{ scope.row.profitRate ? scope.row.profitRate.toFixed(2) + '%' : '-' }}
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="90">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 0" type="info">待确认</el-tag>
          <el-tag v-else type="success">已确认</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="版本" align="center" prop="version" width="80" />
      <el-table-column label="备注" align="left" prop="remark" min-width="150" show-overflow-tooltip />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="100">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="handleDetail(scope.row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog title="手动结算" :visible.sync="settleDialogVisible" width="400px" append-to-body>
      <el-form ref="settleForm" :model="this" :rules="settleRules" label-width="100px">
        <el-form-item label="选择订单" prop="selectedOrderNum">
          <el-input v-model="selectedOrderNum" placeholder="请选择订单" readonly @click.native="openOrderPopup">
            <el-button slot="append" icon="el-icon-search"></el-button>
          </el-input>
        </el-form-item>
        <el-form-item label="订单信息" v-if="selectedOrder && selectedOrder.id">
          <div>订单号：{{ selectedOrder.orderNum }}</div>
          <div>订单金额：￥{{ (selectedOrder.amount).toFixed(2) }}</div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitSettle">确 定</el-button>
        <el-button @click="settleDialogVisible = false">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="结算详情" :visible.sync="detailDialogVisible" width="900px" append-to-body>
      <el-descriptions :column="2" border v-if="settlementDetail">
        <el-descriptions-item label="结算单号">{{ settlementDetail.settlementNo }}</el-descriptions-item>
        <el-descriptions-item label="订单编号">{{ settlementDetail.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="订单销售额" label-class-name="bg-blue">￥{{ (settlementDetail.salesAmount).toFixed(2) }}</el-descriptions-item>
        <el-descriptions-item label="订单利润" label-class-name="bg-green" :span="1">
          <span :style="{ color: settlementDetail.profit >= 0 ? '#67C23A' : '#F56C6C', fontWeight: 'bold' }">
            ￥{{ (settlementDetail.profit).toFixed(2) }}
          </span>
        </el-descriptions-item>
        <el-descriptions-item label="利润率" label-class-name="bg-yellow">
          {{ settlementDetail.profitRate ? settlementDetail.profitRate.toFixed(2) + '%' : '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag v-if="settlementDetail.status === 0" type="info">待确认</el-tag>
          <el-tag v-else type="success">已确认</el-tag>
        </el-descriptions-item>
      </el-descriptions>

      <el-divider content-position="left">成本明细</el-divider>
      <el-table :data="settlementItems" style="width: 100%">
        <el-table-column label="费用类型" prop="itemType" width="150">
          <template slot-scope="scope">
            <span>{{ getItemTypeName(scope.row.itemType) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="费用名称" prop="itemName" width="150" />
        <el-table-column label="金额" prop="amount" width="150">
          <template slot-scope="scope">
            ￥{{ (scope.row.amount).toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column label="备注" prop="remark" min-width="200" />
        <el-table-column label="创建时间" prop="createTime" width="180">
          <template slot-scope="scope">
            {{ parseTime(scope.row.createTime) }}
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <PopupOrderList @data-from-select="handleOrderSelect" ref="orderPopup" :order-status="3" :settlement-status="0"></PopupOrderList>
  </div>
</template>

<script>
import { listOrderSettlement, manualSettlement, getSettlementDetail } from "@/api/finance/orderSettlement";
import PopupOrderList from "@/views/order/PopupOrderList.vue";

export default {
  name: "OrderSettlement",
  components: { PopupOrderList },
  data() {
    return {
      loading: true,
      showSearch: true,
      total: 0,
      settlementList: [],
      selectedOrder: {},
      selectedOrderNum: '',
      dateRange: [],
      settleDialogVisible: false,
      detailDialogVisible: false,
      settlementDetail: null,
      settlementItems: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        settlementNo: null,
        orderNo: null,
        status: null,
        startTime: null,
        endTime: null
      },
      settleRules: {
        selectedOrderNum: [{ required: true, message: "订单号不能为空", trigger: "change" }]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      if (this.dateRange && this.dateRange.length === 2) {
        this.queryParams.startTime = this.dateRange[0];
        this.queryParams.endTime = this.dateRange[1];
      } else {
        this.queryParams.startTime = null;
        this.queryParams.endTime = null;
      }
      listOrderSettlement(this.queryParams).then(response => {
        this.settlementList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    getItemTypeName(itemType) {
      const typeMap = {
        1: '商品采购成本',
        2: '发货费用',
        4: '平台扣点',
        5: '营销费用',
        99: '其他费用'
      };
      return typeMap[itemType] || '其他';
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    handleProcessSettlement() {
      this.selectedOrder = {};
      this.selectedOrderNum = '';
      this.settleDialogVisible = true;
    },
    submitSettle() {
      this.$refs["settleForm"].validate(valid => {
        if (valid) {
          if (!this.selectedOrder.id) {
            this.$modal.msgError("请选择订单");
            return;
          }
          manualSettlement({ orderId: this.selectedOrder.id }).then(response => {
            if (response.code === 200) {
              this.$modal.msgSuccess("结算成功");
              this.settleDialogVisible = false;
              this.getList();
            } else {
              this.$modal.msgError(response.msg);
            }
          });
        }
      });
    },
    handleDetail(row) {
      this.settlementDetail = row;
      if (row.id) {
        getSettlementDetail(row.id).then(response => {
          this.settlementItems = response.data.items || [];
        });
      }
      this.detailDialogVisible = true;
    },
    openOrderPopup() {
      this.$refs.orderPopup.openDialog();
    },
    handleOrderSelect(row) {
      this.selectedOrder = row;
      this.selectedOrderNum = row.orderNum;
    }
  }
};
</script>
