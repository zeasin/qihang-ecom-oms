<template>
    <div class="app-container">

        <el-form :model="form" ref="form" size="small" style="width: 1300px" :rules="rules" :inline="true" label-width="128px">
          <el-col :span="8">
            <el-form-item label="采购单Id" >
              <el-input v-model="form.id" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="采购单号" >
            <el-input v-model="form.orderNum" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="8">
<!--            <el-form-item label="下单日期" prop="orderDate">-->
<!--              <el-input v-model="form.orderDate" disabled />-->
<!--            </el-form-item>-->
            <el-form-item label="创建时间">
              <el-date-picker clearable v-model="form.createTime" disabled type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="创建人" >
              <el-input v-model="form.createBy" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="总金额" >
              <el-input v-model="form.orderAmount" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="供应商" prop="supplier">
                <el-input v-model="form.supplier" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="审核人" >
            <el-input v-model="form.auditUser" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="审核时间" >
              <el-input v-model="form.auditTime" disabled />
            </el-form-item>
          </el-col>
<!--            <el-form-item label="采购单确认日期">-->
<!--                <el-date-picker clearable v-model="form.supplierConfirmTime" disabled type="date" value-format="yyyy-MM-dd" placeholder="请选择订单日期">-->
<!--                </el-date-picker>-->
<!--            </el-form-item>-->
          <el-col :span="8">
            <el-form-item label="供应商发货日期">
                <el-date-picker clearable v-model="form.supplierDeliveryTime" disabled type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="">
                </el-date-picker>
            </el-form-item>
          </el-col>

            <el-row>
            <el-form-item label="商品明细：" prop="goodsList">
            <el-col :span="24">

            </el-col>
            </el-form-item>

            <el-col :span="24" style="margin-left: 98px;">

            <el-table :data="itemList" style="width: 100%">
                <el-table-column type="index" label="序号">
                </el-table-column>
                <el-table-column prop="colorImage" label="商品图片">
                <template slot-scope="scope">
                    <el-image style="width: 70px; height: 70px" :src="scope.row.colorImage"></el-image>
                </template>
                </el-table-column>
                <el-table-column prop="goodsName" label="商品名称" width="300px"></el-table-column>
                <el-table-column prop="specNum" label="SKU">
                </el-table-column>
                <el-table-column prop="colorValue" label="颜色">
                </el-table-column>
                <el-table-column prop="sizeValue" label="尺码">
                </el-table-column>
                <el-table-column prop="styleValue" label="款式">
                </el-table-column>

                <el-table-column prop="price" label="采购价" :formatter="amountFormatter">
                </el-table-column>
              <el-table-column prop="quantity" label="采购数量">
              </el-table-column>
            </el-table>

            </el-col>

        </el-row>
        <el-row style="margin-top: 20px;">
<!--        <el-form-item label="总金额" prop="amount">-->
<!--            <el-input type="number" v-model.number="form.orderAmount" disabled placeholder="请输入总金额" />-->
<!--        </el-form-item>-->
        </el-row>
        </el-form>
    </div>
</template>
<script>
import {getPurchaseOrder} from "@/api/purchase/purchaseOrder";
import { getSupplier } from '@/api/goods/supplier'
import {amountFormatter, parseTime} from "@/utils/zhijian";

export default {
  name: "PurchaseOrderDetail",
  data() {
    return {
      form: {
        id: null
      },
      itemList: [],
      rules: {}
    }
  },
  mounted() {
    this.form.id = this.$route.query.id
    this.getDetail()
  },
  created() {

  },
  methods: {
    amountFormatter,
    parseTime,
    getDetail() {
      getPurchaseOrder(this.form.id).then((response) => {

        getSupplier(response.data.supplierId).then((resp) => {
          this.form = response.data;
          this.form.supplier = resp.data.name;
          this.form.auditTime = parseTime(response.data.auditTime * 1000)
          // this.form.auditTime = this.dateToString(response.data.auditTime * 1000)
          this.itemList = response.data.itemList
          console.log('采购单详情', this.form)
        })

      });
      // listPurchaseOrderItem({orderId:this.form.id}).then(res =>{
      //     this.itemList = res.rows
      //     console.log('采购单明细',res)
      // })
    }
  }
}
</script>
