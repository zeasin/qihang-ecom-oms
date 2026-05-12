<template>
  <div class="app-container">
    <el-card class="form-card">
      <div slot="header">
        <span>采购单</span>
      </div>
      <el-form :model="form" ref="form" size="small" :rules="rules" :inline="true" label-width="128px">
      <el-col :span="24">
      <el-form-item label="采购单号">
        <el-input v-model="form.orderNum" disabled style="width: 220px"/>
      </el-form-item>

        <el-form-item label="供应商" prop="supplierId">
          <el-select v-model="form.supplierId" disabled filterable  placeholder="请选择供应商名称" style="width: 220px">
            <el-option v-for="item in supplierList" :key="'sup'+item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
      </el-col>
        <el-col :span="24">
        <el-form-item label="商品总数量"><el-input v-model="ship.orderSpecUnitTotal" disabled style="width: 220px"/> </el-form-item>
        <el-form-item label="总金额">
          <el-input v-model="form.orderAmount" disabled style="width: 220px"/>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="采购日期" prop="orderDate" >
          <el-date-picker
            clearable
            v-model="form.orderDate"
            disabled
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择订单日期"
            style="width: 220px"
          >
          </el-date-picker>
        </el-form-item>

        <el-form-item label="发货日期">
          <el-date-picker
            clearable
            v-model="form.supplierDeliveryTime"
            disabled
            type="date"
            value-format="yyyy-MM-dd"
            placeholder=""
            style="width: 220px"
          >
          </el-date-picker>
        </el-form-item>
      </el-col>
      <el-col :span="24">
      <el-form-item label="创建人">
        <el-input v-model="form.createBy" disabled style="width: 220px"/>
      </el-form-item>
      <el-form-item label="审核人">
        <el-input v-model="form.auditUser" disabled style="width: 220px"/>
      </el-form-item>
      </el-col>
      <el-col :span="24">
      <el-form-item label="发货物流公司"><el-input v-model="ship.shipCompany" disabled style="width: 220px" /> </el-form-item>
      <el-form-item label="发货物流单号"><el-input v-model="ship.shipNum" disabled style="width: 220px" /> </el-form-item>
      </el-col>

      </el-form>
    </el-card>
    <el-card class="items-card" style="margin-top: 20px;">
      <div slot="header">
        <span>商品明细</span>
      </div>
      <el-row>
        <el-col :span="24">
          <el-table :data="itemList" style="width: 1200px">
            <el-table-column type="index" label="序号"> </el-table-column>
            <el-table-column prop="colorImage" label="商品图片">
              <template slot-scope="scope">
                <el-image
                  style="width: 70px; height: 70px"
                  :src="scope.row.colorImage"
                ></el-image>
              </template>
            </el-table-column>
            <el-table-column
              prop="goodsName"
              label="商品名称"
            ></el-table-column>
            <el-table-column prop="specNum" label="SKU"> </el-table-column>
            <el-table-column prop="colorValue" label="规格1"> </el-table-column>
            <el-table-column prop="sizeValue" label="规格2"> </el-table-column>
            <el-table-column prop="styleValue" label="规格3"> </el-table-column>
            <el-table-column prop="price" label="单价"> </el-table-column>
            <el-table-column prop="quantity" label="数量">
            </el-table-column>
<!--            <el-table-column prop="inQty" label="需入库数量">-->
<!--                <template slot-scope="scope">-->
<!--                    <el-input v-model.number="scope.row.inQty" placeholder="填写需入库的数量" />-->
<!--                </template>-->
<!--            </el-table-column>-->
            <el-table-column prop="amount" label="总金额"> </el-table-column>
            <el-table-column label="库存模式" width="100">
              <template slot-scope="scope">
                <el-tag size="mini" :type="scope.row.inventoryMode === 1 ? 'success' : 'info'">
                  {{ scope.row.inventoryMode === 1 ? '一物一码' : '普通' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-col>

      </el-row>
    </el-card>
    <el-card class="items-card" style="margin-top: 20px;">
      <div slot="header">
        <span>生成入库单</span>
      </div>
      <el-form :model="form1" ref="form1" size="small" :rules="rules" :inline="true" label-width="128px">
        <el-row style="margin-top: 20px">
          <el-form-item label="收货日期" >
            <el-date-picker
              clearable
              v-model="form1.receiptTime"
              type="date"
              value-format="yyyy-MM-dd"
              placeholder=""
              style="width: 220px"
            >
            </el-date-picker>
          </el-form-item>

        </el-row>
        <el-row>
          <el-form-item label="备注"><el-input type="textarea" v-model="form1.remark" style="width: 220px" /> </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="入库仓库" prop="warehouseId">
            <el-select v-model="form1.warehouseId"  filterable  placeholder="请选择入库仓库" style="width: 220px">
              <el-option v-for="item in warehouseList" :key="item.id" :label="item.warehouseName" :value="item.id">
                <span style="float: left">{{ item.warehouseName }}</span>
                <span style="float: right; color: #8492a6; font-size: 13px" v-if="item.warehouseType=='LOCAL'">本地仓</span>
                <span style="float: right; color: #8492a6; font-size: 13px" v-else-if="item.warehouseType=='JDYC' && item.jdlApiType==0">京东云仓-仓配一体</span>
                <span style="float: right; color: #8492a6; font-size: 13px" v-else-if="item.warehouseType=='JDYC' && item.jdlApiType==1">京东云仓-ERP</span>
                <span style="float: right; color: #8492a6; font-size: 13px" v-else-if="item.warehouseType=='JDYC' ">京东云仓</span>
                <span style="float: right; color: #8492a6; font-size: 13px" v-else-if="item.warehouseType=='JKYYC'">吉客云云仓</span>
                <span style="float: right; color: #8492a6; font-size: 13px" v-else-if="item.warehouseType=='CLOUD'">系统云仓</span>
                <span style="float: right; color: #8492a6; font-size: 13px" v-else>未知仓</span>
              </el-option>
            </el-select>
          </el-form-item>
        </el-row>
        <el-row>
          <!--        <el-form-item>-->
          <!--          <el-button type="primary" style="margin-left: 128px;" @click="submitForm">生成采购入库单</el-button>-->
          <!--        </el-form-item>-->
        </el-row>
      </el-form>
    </el-card>
    <div class="submit-bar">
      <el-button type="primary" style="margin-left: 128px;" @click="submitForm" :loading="submitting">生成采购入库单</el-button>
<!--      <el-button @click="saveDraft">暂存</el-button>-->
    </div>

  </div>
</template>
<script>
import { getPurchaseOrderShip,createStockInEntry } from "@/api/purchase/PurchaseOrderShip";
import { getPurchaseOrder } from "@/api/purchase/purchaseOrder";
import {listAllSupplier, listSupplier} from '@/api/goods/supplier'
import { parseTime } from '@/utils/zhijian'
import {listWarehouse, myAvailableList} from '@/api/wms/warehouse'
import { listMerchantCloudWarehouse } from '@/api/shop/merchant'
import {getUserProfile} from "@/api/system/user";
import {listMyAllWarehouse} from "@/api/store/warehouse";
// import { listPurchaseOrderItem } from "@/api/purchase/purchaseOrderItem";

export default {
  name: "PurchaseOrderShipCreateEntry",
  data() {
    return {
      queryParams:{
        pageNum: 1,
        pageSize:50,
      },
      form1: {
        id: null,
        receiptTime: null,
        warehouseId:null
      },
      form: {
        id: null,
        warehouseId:null
      },
      submitting: false,
      ship:{},
      itemList:[],
      supplierList: [],
      warehouseList: [],
      rules: {
        warehouseId:[{ required: true, trigger: "blur", message: '请选择入库的仓库' }],
      },
    };
  },
  mounted() {
    listAllSupplier({pageIndex:1,pageSize:1000}).then(response => {
      this.supplierList = response.rows;
    });
    getUserProfile().then(res=> {
      this.loading = false;
      if (res.data.userType == 0) {
        // 总部
        console.log('===总部');
        myAvailableList().then(response => {
          this.warehouseList = response.data;
        });
      } else if (res.data.userType == 20) {
        // 商户
        listMyAllWarehouse().then(response => {
          this.warehouseList = response.data;
        })
      } else if (res.data.userType == 40) {
        // 店铺
        listMyAllWarehouse().then(response => {
          this.warehouseList = response.data;
        })
      }
    })
    // listWarehouse(this.queryParams).then(response => {
    //   this.warehouseList = response.rows;
      // listMerchantCloudWarehouse({}).then(response => {
      //   if(response.code===200){
      //     if(response.data){
      //       response.data.forEach(item => {
      //         this.warehouseList.push({
      //           id:item.warehouseId,
      //           warehouseName:item.warehouseName,
      //           warehouseType:item.warehouseType,
      //         })
      //       })
      //     }
      //   }
      //
      //
      // });
    // });
    this.form1.id = this.$route.query.id;
    this.getDetail();
    this.form1.receiptTime = new Date()
  },
  created() {},
  methods: {
    parseTime,
    getDetail() {
      getPurchaseOrder(this.form1.id).then((res) => {
        this.form = res.data;
        this.itemList = res.data.itemList
        console.log("采购单详情", res.data);
      });

      getPurchaseOrderShip(this.form1.id).then((response) => {
        this.ship = response.data;
        console.log("物流详情", this.ship);
      });
      // listPurchaseOrderItem({orderId:this.form.id}).then(res =>{
      //   if(res.rows){
      //       res.rows.forEach(x=>{
      //           const g = {
      //               ...x
      //           }
      //           x.inQty = x.quantity
      //
      //       })
      //   }
      //       this.itemList = res.rows
      //       console.log('采购单明细',res)
      //   })
    },
    submitForm() {
      this.$refs["form1"].validate((valid) => {
        if (valid) {
          this.submitting = true;
            this.form1.goodsList = this.itemList
            console.log("======创建采购入库单=====", this.form1);
            createStockInEntry(this.form1).then(res=>{
                console.log("====创建入库单api结果====",res)
                if(res.code === 200){
                    // 调用全局挂载的方法,关闭当前标签页
                    this.$store.dispatch("tagsView/delView", this.$route);
                    this.$router.push('purchase_stock_in_list');
                }
            })
        }
      });
    },
  },
};
</script>
<style scoped>
.submit-bar {
  margin-top: 20px;
  text-align: center;
}
</style>
