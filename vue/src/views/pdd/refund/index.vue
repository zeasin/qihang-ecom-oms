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
      <el-form-item label="退款ID" prop="refundId">
        <el-input
          v-model="queryParams.refundId"
          placeholder="请输入退款ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="订单号" prop="orderSn">
        <el-input
          v-model="queryParams.orderSn"
          placeholder="请输入订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="类型" prop="afterSalesType">
        <el-select v-model="queryParams.afterSalesType" placeholder="请选择类型" clearable @change="handleQuery">

          <el-option label="仅退款" value="2" ></el-option>
          <el-option label="退货退款" value="3"></el-option>
          <el-option label="换货" value="4"> </el-option>
          <el-option label="维修" value="6"> </el-option>
          <el-option label="缺货补寄" value="5"> </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="更新时间" prop="updateTime">
        <el-date-picker clearable  @change="handleQuery"
                        v-model="queryParams.updateTime" value-format="yyyy-MM-dd"
                        type="date" placeholder="售后更新时间">
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
        >API拉取新退款</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="taoRefundList" @selection-change="handleSelectionChange">
<!--       <el-table-column type="selection" width="55" align="center" />-->
      <!-- <el-table-column label="${comment}" align="center" prop="id" /> -->
      <el-table-column label="退款ID" align="center" prop="id" width="180"/>
      <el-table-column label="类型" align="center" prop="afterSalesType" width="100">
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.afterSalesType === 2"> 仅退款</el-tag>
          <el-tag size="small" v-if="scope.row.afterSalesType === 3"> 退货退款</el-tag>
          <el-tag size="small" v-if="scope.row.afterSalesType === 4"> 换货</el-tag>
          <el-tag size="small" v-if="scope.row.afterSalesType === 6"> 维修</el-tag>
          <el-tag size="small" v-if="scope.row.afterSalesType === 5"> 缺货补寄</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="店铺" align="center" prop="shopId" width="160">
        <template slot-scope="scope">
<!--          <el-tag size="small"></el-tag>-->
            {{shopList.find(x=>x.id === scope.row.shopId).name}}
        </template>
      </el-table-column>
      <el-table-column label="订单号" align="center" prop="orderSn" width="220"/>
      <el-table-column label="商品" prop="goodsName" ></el-table-column>
      <el-table-column label="退款金额" prop="refundAmount" :formatter="amountFormatter" width="100"></el-table-column>
      <el-table-column label="订单金额" prop="orderAmount" :formatter="amountFormatter" width="100"></el-table-column>
      <el-table-column label="售后数量" prop="goodsNumber"  width="80"></el-table-column>

      <el-table-column label="售后原因" prop="afterSaleReason" ></el-table-column>


      <el-table-column label="状态" align="center" prop="afterSalesStatus" width="160">
        <template slot-scope="scope">
<!--          2：买家申请退款，待商家处理 3：退货退款，待商家处理 4：商家同意退款，退款中
5：平台同意退款，退款中 6：驳回退款，待买家处理 7：已同意退货退款,待用户发货 8：平台处理中 9：平台拒绝退款，退款关闭
10：退款成功 11：买家撤销 12：买家逾期未处理，退款失败 13：买家逾期，超过有效期 14：换货补寄待商家处理
15：换货补寄待用户处理 16：换货补寄成功 17：换货补寄失败 18：换货补寄待用户确认完成 21：待商家同意维修
22：待用户确认发货 24：维修关闭 25：维修成功 27：待用户确认收货 31：已同意拒收退款，待用户拒收 32：补寄待商家发货
33：待商家召回-->
          <el-tag size="small" > {{ scope.row.afterSalesStatus }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="申请时间" align="center" prop="createdTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="createdTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updatedTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="处理状态" align="center" prop="afterSalesStatus" width="120">
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.auditStatus===1"> 已处理</el-tag>
          <el-tag size="small" v-if="scope.row.auditStatus===0"> 待处理</el-tag>
          <el-tag size="small" v-if="scope.row.auditStatus===10"> 无需处理</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="100">
        <template slot-scope="scope">
          <el-button
          v-if="scope.row.auditStatus === 0 "
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleConfirm(scope.row)"
          >售后处理</el-button>
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
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="148px">
        <el-form-item label="售后单号" prop="refundNum">
          <el-input v-model="form.refundNum" placeholder="请输入售后单号" />
        </el-form-item>
        <el-form-item label="订单号" prop="orderNum">
          <el-input v-model="form.orderNum" placeholder="请输入订单号" />
        </el-form-item>
        <el-form-item label="订单是否发货" prop="hasGoodsSend">
          <el-select v-model="form.hasGoodsSend" placeholder="是否已发货" clearable>
            <el-option label="未发货" value="0"></el-option>
            <el-option label="已发货" value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="处理方式" prop="type">
          <el-select v-model="form.type" placeholder="售后处理方式" clearable @change="handleTypeChange">
            <el-option label="无需处理" value="0"></el-option>
            <el-option label="退货" value="10"></el-option>
            <el-option label="换货" value="20"></el-option>
            <el-option label="维修" value="30"></el-option>
            <el-option label="补发" value="80"></el-option>
            <el-option label="订单拦截" value="99"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="货物是否需要退回" prop="hasGoodsReturn">
          <el-select v-model="form.hasGoodsReturn" placeholder="买家是否需要退货" clearable>
            <el-option label="无需退回" value="0"></el-option>
            <el-option label="需要退回" value="1"></el-option>
          </el-select>
        </el-form-item>

<!--        <el-form-item label="发货单号" prop="sendLogisticsCode">-->
<!--          <el-input v-model="form.sendLogisticsCode" placeholder="发货单号" />-->
<!--        </el-form-item>-->
        <el-form-item label="退回物流单号" prop="returnLogisticsCode" v-if="form.hasGoodsReturn==1">
          <el-input v-model="form.returnLogisticsCode" placeholder="退回物流单号" />
        </el-form-item>

        <el-form-item label="联系人" prop="receiverName" v-if="form.type==20||form.type==80||form.type==30">
          <el-input v-model="form.receiverName" placeholder="收件人/联系人" />
        </el-form-item>
        <el-form-item label="联系电话" prop="receiverTel" v-if="form.type==20||form.type==80||form.type==30">
          <el-input v-model="form.receiverTel" placeholder="收件手机号/联系电话" />
        </el-form-item>
        <el-form-item label="地址" prop="receiverAddress" v-if="form.type==20||form.type==80||form.type==30">
          <el-input v-model="form.receiverAddress" placeholder="详细地址" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="form.remark" placeholder="请输入备注" />
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
import { listRefund, pullRefund, refundHandle } from '@/api/pdd/refund'
import { listShop } from "@/api/shop/shop";
import {MessageBox} from "element-ui";
import {isRelogin} from "@/utils/request";
import { amountFormatter } from '@/utils/zhijian'

export default {
  name: "RefundPdd",
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
      pullLoading: false,
      // 总条数
      total: 0,
      // 淘宝退款订单表格数据
      taoRefundList: [],
      shopList:[],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        refundId: null,
        afterSalesType: null,
        updateTime: null,
        oid: null,

      },
      // 表单参数
      form: {
        type:null,
        hasGoodsReturn:null,
      },
      // 表单校验
      rules: {
        refundNum: [{ required: true, message: "售后单号不能为空", trigger: "blur" }],
        orderNum: [{ required: true, message: "源订单号不能为空", trigger: "blur" }],
        hasGoodsSend: [{ required: true, message: "请选择是否发货", trigger: "blur" }],
        type: [{ required: true, message: "请选择处理方式", trigger: "blur" }],
        hasGoodsReturn: [{ required: true, message: "请选择货物是否需要退回", trigger: "blur" }],
      }
    };
  },
  created() {
    listShop({type: 300}).then(response => {
      this.shopList = response.rows;
      if (this.shopList && this.shopList.length > 0) {
        this.queryParams.shopId = this.shopList[0].id
      }
      this.getList();
    });
    // this.getList();
  },
  methods: {
    amountFormatter,
    /** 查询退款列表 */
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },

    handlePull() {
      if (!this.queryParams.shopId) {
        this.$modal.msgError("请先选择店铺");
        return
      }
      if (!this.queryParams.updateTime) {
        this.$modal.msgError("请选择售后更新时间")
        return
      }
        this.pullLoading = true
        pullRefund({shopId:this.queryParams.shopId,updType:0,updateTime:this.queryParams.updateTime}).then(response => {
          console.log('拉取淘宝订单接口返回=====',response)
          if(response.code === 1401) {
            MessageBox.confirm('Token已过期，需要重新授权！请前往店铺列表重新获取授权！', '系统提示', { confirmButtonText: '前往授权', cancelButtonText: '取消', type: 'warning' }).then(() => {
              this.$router.push({path:"/shop/shop_list",query:{type:4}})
              // isRelogin.show = false;
              // store.dispatch('LogOut').then(() => {
              // location.href = response.data.tokenRequestUrl+'?shopId='+this.queryParams.shopId
              // })
            }).catch(() => {
              isRelogin.show = false;
            });

            // return Promise.reject('无效的会话，或者会话已过期，请重新登录。')
          }else{
            this.$modal.msgSuccess(JSON.stringify(response));
           this.getList()
          }
          this.pullLoading = false
        })
    },
    /** 售后处理*/
    handleConfirm(row){
        this.open = true
        this.form.refundId = row.id
        this.form.refundNum = row.id
        this.form.orderNum = row.orderSn
    },
    submitForm(){
      this.$refs["form"].validate(valid => {
        if (valid) {
          refundHandle(this.form).then(response => {
            console.log('======返回====',response)
            this.$modal.msgSuccess("处理成功！");
            this.open = false
            this.getList()
          });
        }
      })
    },
  }
};
</script>
