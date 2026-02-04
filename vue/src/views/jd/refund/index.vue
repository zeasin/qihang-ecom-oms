<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="店铺" prop="shopId">
        <el-select v-model="queryParams.shopId" placeholder="请选择店铺" clearable @change="handleQuery">
          <el-option
            v-for="item in shopList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="服务单号" prop="serviceId">
        <el-input
          v-model="queryParams.serviceId"
          placeholder="请输入退服务单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="订单号" prop="orderId">
        <el-input
          v-model="queryParams.orderId"
          placeholder="请输入订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="类型" prop="customerExpect">
        <el-select v-model="queryParams.customerExpect" placeholder="请选择类型" clearable @change="handleQuery">
          <el-option label="取消订单" value="1" ></el-option>
<!--          <el-option label="仅退款" value="11" ></el-option>-->
          <el-option label="退货" value="10"></el-option>
          <el-option label="换货" value="20"> </el-option>
          <el-option label="维修" value="30"> </el-option>
<!--          <el-option label="补发商品" value="80"> </el-option>-->
        </el-select>
      </el-form-item>
      <el-form-item label="申请时间" prop="createTime">
        <el-date-picker clearable  @change="handleQuery"
                        v-model="queryParams.createTime" value-format="yyyy-MM-dd"
                        type="date" placeholder="售后申请时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          :loading="pullLoading"
          type="success"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handlePull"
        >API拉取新售后</el-button>
      </el-col>

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="taoRefundList" @selection-change="handleSelectionChange">
       <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="${comment}" align="center" prop="id" /> -->
      <el-table-column label="服务单号" align="center" prop="serviceId" />
      <el-table-column label="类型" align="center" prop="customerExpect" >
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.customerExpect === 1"> 取消订单</el-tag>
          <el-tag size="small" v-if="scope.row.customerExpect === 10"> 退货</el-tag>
          <el-tag size="small" v-if="scope.row.customerExpect === 20"> 换货</el-tag>
          <el-tag size="small" v-if="scope.row.customerExpect === 30"> 维修</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="店铺" align="center" prop="shopId" >
        <template slot-scope="scope">
<!--          <el-tag size="small"></el-tag>-->
            {{shopList.find(x=>x.id === scope.row.shopId).name}}
        </template>
      </el-table-column>
      <el-table-column label="订单号" align="center" prop="orderId" />
      <el-table-column label="商品" prop="wareName" ></el-table-column>
      <el-table-column label="退款金额" prop="applyRefundSum" >
        <template slot-scope="scope">
          <el-col v-if="scope.row.applyRefundSum">{{scope.row.applyRefundSum / 100}}</el-col>
        </template>
      </el-table-column>
      <el-table-column label="退款说明" prop="refundCheckRemark" ></el-table-column>
      <el-table-column label="退款原因" prop="refundReason" ></el-table-column>


      <el-table-column label="状态" align="center" prop="serviceStatusName" >
        <template slot-scope="scope">
<!--          客户期望(1-售前退款 10-退货 20-换货 30-维修 40-大家电安装 50-大家电移机 60-大家电增值服务 70-上门维修 90-优鲜赔 80-补发商品 100-试用收回 11-仅退款)-->
          <el-tag size="small" v-if="scope.row.customerExpect===10"> {{ scope.row.serviceStatusName }}</el-tag>
          <el-tag size="small" v-if="scope.row.customerExpect===20"> {{ scope.row.serviceStatusName }}</el-tag>

          <el-tag size="small" v-if="scope.row.customerExpect===30 || scope.row.customerExpect===40 ||scope.row.customerExpect===50 ||scope.row.customerExpect===60 ||scope.row.customerExpect===70 "> {{ scope.row.serviceStatusName }}</el-tag>
          <el-tag size="small" v-if="(scope.row.customerExpect===1 || scope.row.customerExpect===11)&& (scope.row.refundStatus===11 || scope.row.refundStatus===3)"> 退款成功</el-tag>
          <el-tag size="small" v-else-if="(scope.row.customerExpect===1 || scope.row.customerExpect===11)&& scope.row.refundStatus!==11&&scope.row.refundStatus!==3"> 退款中</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="申请时间" align="center" prop="applyTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.applyTime) }}</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button-->
<!--          v-if="scope.row.auditStatus === 0 && scope.row.afterSalesType === 1 "-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-edit"-->
<!--            @click="handleConfirm(scope.row)"-->
<!--            v-hasPermi="['tao:taoRefund:edit']"-->
<!--          >退货确认</el-button>-->
<!--          &lt;!&ndash; <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-delete"-->
<!--            @click="handleDelete(scope.row)"-->
<!--            v-hasPermi="['tao:taoRefund:remove']"-->
<!--          >删除</el-button> &ndash;&gt;-->
<!--        </template>-->
<!--      </el-table-column>-->
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改淘宝退款订单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="退款ID" prop="refundId">
          <el-input v-model="form.refundId" placeholder="请输入退款id" disabled/>
        </el-form-item>
        <el-form-item label="订单单号" prop="tid">
          <el-input v-model="form.tid" placeholder="请输入淘宝交易单号" disabled/>
        </el-form-item>
        <el-form-item label="退还金额" prop="refundFee">
          <el-input v-model="form.refundFee" placeholder="请输入退还金额(退还给买家的金额)。精确到2位小数;单位:元。如:200.07，表示:200元7分" disabled/>
        </el-form-item>
        <el-form-item label="sku编号" prop="specNumber">
          <el-input v-model="form.specNumber" placeholder="请输入sku编号" />
        </el-form-item>
        <el-form-item label="退货数量" prop="num">
          <el-input v-model="form.num" placeholder="请输入退货数量" disabled/>
        </el-form-item>
        <el-form-item label="物流公司" prop="logisticsCompany">
          <el-input v-model="form.logisticsCompany" placeholder="请输入物流公司" />
        </el-form-item>
        <el-form-item label="物流单号" prop="logisticsCode">
          <el-input v-model="form.logisticsCode" placeholder="请输入物流单号" />
        </el-form-item>
        <el-form-item label="发货时间" prop="sendTime">
          <el-date-picker clearable
            v-model="form.sendTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择发货时间">
          </el-date-picker>
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>



      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRefund, getRefund,pullRefund,pushOms } from "@/api/jd/refund";
import { listShop } from "@/api/shop/shop";
import {MessageBox} from "element-ui";
import {isRelogin} from "@/utils/request";
export default {
  name: "RefundJd",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 淘宝退款订单表格数据
      taoRefundList: [],
      shopList:[],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      pullLoading: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        refundId: null,
        afterSalesType: null,
        customerExpect: null,
        createTime: null,
        orderStatus: null,
        status: null,
        goodStatus: null,
        hasGoodReturn: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        num: [
          { required: true, message: "退货数量不能为空", trigger: "blur" }
        ],
        logisticsCompany: [
          { required: true, message: "不能为空", trigger: "change" }
        ],
        logisticsCode: [
          { required: true, message: "不能为空", trigger: "blur" }
        ],
        sendTime: [
          { required: true, message: "不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    listShop({type: 200}).then(response => {
      this.shopList = response.rows;
      if (this.shopList && this.shopList.length > 0) {
        this.queryParams.shopId = this.shopList[0].id
      }
      this.getList();
    });
    // this.getList();
  },
  methods: {
    /** 查询淘宝退款订单列表 */
    getList() {
      this.loading = true;
      listRefund(this.queryParams).then(response => {
        this.taoRefundList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        refundId: null,
        afterSalesType: null,
        tid: null,
        oid: null,
        refundPhase: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.serviceId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },

    handlePull() {
      if (!this.queryParams.shopId) {
        this.$modal.msgError("请先选择店铺");
        return
      }
      if (!this.queryParams.createTime) {
        this.$modal.msgError("请选择售后申请时间")
        return
      }
      this.pullLoading = true
      pullRefund({
        shopId: this.queryParams.shopId,
        updType: 0,
        createTime: this.queryParams.createTime
      }).then(response => {
        console.log('拉取jd订单接口返回=====', response)
        if (response.code === 200) {
          this.$modal.msgSuccess(JSON.stringify(response));
          this.pullLoading = false
          this.getList()
        } else if (response.code === 1401) {
          MessageBox.confirm('Token已过期，需要重新授权！请前往店铺列表重新获取授权！', '系统提示', {
            confirmButtonText: '前往授权',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.$router.push({path: "/shop/shop_list", query: {type: 2}})
            // isRelogin.show = false;
            // store.dispatch('LogOut').then(() => {
            // location.href = response.data.tokenRequestUrl+'?shopId='+this.queryParams.shopId
            // })
          }).catch(() => {
            isRelogin.show = false;
          });

          // return Promise.reject('无效的会话，或者会话已过期，请重新登录。')
        } else {
          this.$modal.msgError(JSON.stringify(response));
          this.pullLoading = false
        }

      })


      // this.$modal.msgSuccess("请先配置API");
    },
    submitForm(){

    }
  }
};
</script>
