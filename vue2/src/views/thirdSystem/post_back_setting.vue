<template>
  <div class="app-container">
    <el-form ref="ruleForm" :model="form" :rules="rules" label-width="128px">
      <el-form-item label="服务器Url" prop="apiUrl">
        <el-input v-model="form.apiUrl" style="width: 300px" disabled></el-input>
      </el-form-item>
      <el-form-item label="回传内容" prop="type">
        <el-checkbox-group v-model="form.type">
          <el-checkbox label="订单发货回传" value="order" name="type" disabled></el-checkbox>
          <el-checkbox label="库存数据回传" value="refund" name="type" disabled></el-checkbox>
          <!--          <el-checkbox label="线下主题活动" name="type"></el-checkbox>-->
          <!--          <el-checkbox label="单纯品牌曝光" name="type"></el-checkbox>-->
        </el-checkbox-group>
      </el-form-item>
      <el-form-item label="订单发货回传路径" prop="orderShipPath">
        <el-input v-model="form.orderShipPath" style="width: 300px" disabled></el-input>
      </el-form-item>
      <el-form-item label="库存数据回传路径" prop="stockPath">
        <el-input v-model="form.stockPath" style="width: 300px" disabled></el-input>
      </el-form-item>
      <el-form-item label="AppKey" prop="appKey">
        <el-input v-model="form.appKey" style="width: 300px"></el-input>
      </el-form-item>
      <el-form-item label="AppSecret" prop="appSecret">
        <el-input v-model="form.appSecret" style="width: 300px"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm('ruleForm')">保存配置</el-button>
        <el-button @click="resetForm('ruleForm')">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row >
      <el-tabs>
        <el-tab-pane label="订单发货回传字段" >
          <el-table :row-class-name="rowSShopOrderItemIndex"
            :data="orderShipField"
            style="width: 100%">
            <el-table-column
              prop="index"
              label="序号"
              width="180">
            </el-table-column>
            <el-table-column
              prop="field"
              label="字段"
              width="180">
            </el-table-column>
            <el-table-column
              prop="remark"
              label="说明">
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="库存数据回传字段" >
          <el-table :row-class-name="rowSShopOrderItemIndex"
                    :data="goodsStockField"
                    style="width: 100%">
            <el-table-column
              prop="index"
              label="序号"
              width="180">
            </el-table-column>
            <el-table-column
              prop="field"
              label="字段"
              width="180">
            </el-table-column>
            <el-table-column
              prop="remark"
              label="说明">
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-row>




  </div>
</template>

<script>
import {getPostBackAppKey,savePostBackAppKey} from "@/api/third_system/open";

export default {
  name: "PostBackSetting",
  data() {
    return {
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 遮罩层
      loading: true,
      orderShipField:[
        {
          field:'orderId',
          remark:'订单号(订单编号)'
        },
        {
          field:'logisticsCompany',
          remark:'物流公司'
        },{
          field:'logisticsCompanyCode',
          remark:'物流公司编码'
        },{
          field:'logisticsCode',
          remark:'物流单号'
        },{
          field:'shipTime',
          remark:'发货时间（yyyy-MM-dd HH:mm:ss）'
        },{
          field:'shipMan',
          remark:'发货人'
        },{
          field:'shipFee',
          remark:'发货费用'
        },
      ],
      goodsStockField:[
        {
          field:'skuCode',
          remark:'SKU编码（唯一）'
        },
        {
          field:'quantity',
          remark:'库存数量'
        },{
          field:'lockedQty',
          remark:'锁定库存'
        },
      ],
      // 是否显示弹出层
      detailOpen:false,
      skuListLoading:false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        pullType: 'ORDER'
      },
      // 表单参数
      form: {
        name: '',
        apiUrl: 'http://ip:8088/api/open',
        appKey: '',
        appSecret: '',
        isOn: false,
        type: ["订单发货回传","库存数据回传"],
        stockPath: '/stock/updateStock',
        orderShipPath: '/ship/orderShip',
        accountToken: '',
        refreshToken: '',
        bizPin: '',
        bizId: '',
        remark: ''
      },
      rules: {
        id: [
          { required: true, message: "不能为空", trigger: "blur" }
        ],
        erpGoodsSkuId: [
          { required: true, message: "不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {

  },
  mounted() {
    getPostBackAppKey().then(response => {
      if(response.data){
        this.form.appKey = response.data.appKey;
        this.form.appSecret = response.data.appSecret;
      }

    });
  },
  methods: {
    /** ${subTable.functionName}序号 */
    rowSShopOrderItemIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
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
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          savePostBackAppKey(this.form).then(resp=>{
            this.$modal.msgSuccess("保存成功")
          })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },


  }
};
</script>
