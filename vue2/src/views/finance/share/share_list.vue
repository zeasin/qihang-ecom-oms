<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="订单编号" prop="orderNum">
        <el-input
          v-model="queryParams.orderNum"
          placeholder="请输入订单编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="分账方" prop="sharePartyId">
        <el-select v-model="queryParams.sharePartyId" placeholder="请选择分账方" clearable>
          <el-option
            v-for="item in partyList"
            :key="item.id"
            :label="item.partyName"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="店铺类型" prop="shopType">
        <el-select v-model="queryParams.shopType" placeholder="请选择店铺类型" clearable>
          <el-option label="抖音" :value="10" />
          <el-option label="快手" :value="20" />
          <el-option label="淘宝" :value="30" />
          <el-option label="拼多多" :value="40" />
          <el-option label="微信" :value="50" />
          <el-option label="小红书" :value="60" />
          <el-option label="京东" :value="70" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="待处理" :value="0" />
          <el-option label="成功" :value="1" />
          <el-option label="失败" :value="2" />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
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
          @click="handleProcessShare"
        >手动分账</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="recordList">
      <el-table-column label="ID" align="center" prop="id" width="80" />
      <el-table-column label="订单编号" align="left" prop="orderNum" min-width="150" />
      <el-table-column label="商品名称" align="left" prop="goodsName" min-width="200" show-overflow-tooltip />
      <el-table-column label="SKU规格" align="left" prop="skuName" min-width="150" show-overflow-tooltip />
      <el-table-column label="店铺类型" align="center" prop="shopType" width="100">
        <template slot-scope="scope">
          {{ getShopTypeName(scope.row.shopType) }}
        </template>
      </el-table-column>
      <el-table-column label="分账方" align="left" prop="sharePartyId" width="150">
        <template slot-scope="scope">
          {{ getPartyName(scope.row.sharePartyId) }}
        </template>
      </el-table-column>
      <el-table-column label="分账方式" align="center" prop="shareWay" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.shareWay === 1" type="success">比例分账</el-tag>
          <el-tag v-else type="warning">固定金额</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="分账比例" align="center" prop="shareRatio" width="100">
        <template slot-scope="scope">
          <span v-if="scope.row.shareRatio">{{ scope.row.shareRatio }}%</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="基准单价" align="right" prop="basePrice" width="100">
        <template slot-scope="scope">
          ￥{{ scope.row.basePrice }}
        </template>
      </el-table-column>
      <el-table-column label="基准金额" align="right" prop="baseAmount" width="100">
        <template slot-scope="scope">
          ￥{{ scope.row.baseAmount }}
        </template>
      </el-table-column>
      <el-table-column label="分账金额" align="right" prop="shareAmount" width="100">
        <template slot-scope="scope">
          <span style="color: #E6A23C; font-weight: bold;">￥{{ scope.row.shareAmount }}</span>
        </template>
      </el-table-column>
      <el-table-column label="数量" align="center" prop="quantity" width="80" />
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 0" type="info">待处理</el-tag>
          <el-tag v-else-if="scope.row.status === 1" type="success">成功</el-tag>
          <el-tag v-else type="danger">失败</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="失败原因" align="left" prop="failReason" min-width="150" show-overflow-tooltip />
      <el-table-column label="处理时间" align="center" prop="processTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.processTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
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

    <el-dialog title="手动分账" :visible.sync="shareDialogVisible" width="400px" append-to-body>
      <el-form ref="shareForm" :model="this" :rules="shareRules" label-width="100px">
        <el-form-item label="选择订单" prop="selectedOrderNum">
          <el-input v-model="selectedOrderNum" placeholder="请选择订单" readonly @click.native="openOrderPopup">
            <el-button slot="append" icon="el-icon-search"></el-button>
          </el-input>
        </el-form-item>
        <el-form-item label="订单信息" v-if="selectedOrder && selectedOrder.id">
          <div>订单号：{{ selectedOrder.orderNum }}</div>
          <div>店铺：{{ getShopTypeName(selectedOrder.shopType) }}</div>
          <div>金额：￥{{ (selectedOrder.amount).toFixed(2) }}</div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitShare">确 定</el-button>
        <el-button @click="shareDialogVisible = false">取 消</el-button>
      </div>
    </el-dialog>

    <PopupOrderList @data-from-select="handleOrderSelect" ref="orderPopup" :order-status="3"></PopupOrderList>
  </div>
</template>

<script>
import { listShareParty, listShareRecord, manualShare } from "@/api/finance/orderShare";
import PopupOrderList from "@/views/order/PopupOrderList.vue";

export default {
  name: "ShareRecord",
  components: { PopupOrderList },
  data() {
    return {
      loading: true,
      showSearch: true,
      total: 0,
      recordList: [],
      partyList: [],
      selectedOrder: {},
      selectedOrderNum: '',
      dateRange: [],
      shareDialogVisible: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNum: null,
        sharePartyId: null,
        shopType: null,
        status: null,
        startTime: null,
        endTime: null
      },
      shareForm: {
        orderId: null
      },
      shareRules: {
        selectedOrderNum: [{ required: true, message: "订单ID不能为空", trigger: "change" }]
      }
    };
  },
  created() {
    this.getPartyList();
    this.getList();
  },
  methods: {
    getPartyList() {
      listShareParty({ pageNum: 1, pageSize: 100 }).then(response => {
        this.partyList = response.rows;
      });
    },
    getPartyName(partyId) {
      const party = this.partyList.find(item => item.id === partyId);
      return party ? party.partyName : '';
    },
    getShopTypeName(shopType) {
      const shopTypeMap = {
        10: '抖音',
        20: '快手',
        30: '淘宝',
        40: '拼多多',
        50: '微信',
        60: '小红书',
        70: '京东'
      };
      return shopTypeMap[shopType] || '全部';
    },
    getList() {
      this.loading = true;
      if (this.dateRange && this.dateRange.length === 2) {
        this.queryParams.startTime = this.dateRange[0];
        this.queryParams.endTime = this.dateRange[1];
      } else {
        this.queryParams.startTime = null;
        this.queryParams.endTime = null;
      }
      listShareRecord(this.queryParams).then(response => {
        this.recordList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
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
    handleProcessShare() {
      this.shareForm = { orderId: null };
      this.selectedOrder = {};
      this.selectedOrderNum = '';
      this.shareDialogVisible = true;
    },
    submitShare() {
      this.$refs["shareForm"].validate(valid => {
        if (valid) {
          if (!this.selectedOrder.id) {
            this.$modal.msgError("请选择订单");
            return;
          }
          manualShare(this.selectedOrder.id).then(response => {
            if (response.code === 200) {
              this.$modal.msgSuccess("分账成功");
              this.shareDialogVisible = false;
              this.getList();
            } else {
              this.$modal.msgError(response.msg);
            }
          });
        }
      });
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
