<template>
  <div class="app-container">
    <el-form ref="form" :model="form" :rules="rules" label-width="108px">
        <el-form-item label="入库单号" prop="stockInNum">
          <el-col :span="24">
            <el-input v-model="form.stockInNum" style="width:220px" placeholder="请输入入库单号" />
            <el-button type="" size="mini" @click="genOrderNum">生成单号</el-button>
          </el-col>
        </el-form-item>
        <el-form-item label="入库类型" prop="stockInType">
          <el-select v-model="form.stockInType" filterable r placeholder="入库类型" >
          <el-option  label="采购入库" value="1"></el-option>
          <el-option  label="退货入库" value="2"></el-option>
          <el-option  label="盘盈入库" value="3"></el-option>
        </el-select>
        </el-form-item>
      <el-form-item label="商户" prop="merchantId">
        <el-select v-model="form.merchantId" placeholder="请选择商户" clearable>
          <el-option label="总部" value="0"></el-option>
          <el-option
            v-for="item in merchantList"
            :key="item.merchantId"
            :label="item.name"
            :value="item.merchantId">
          </el-option>
        </el-select>
      </el-form-item>
        <el-form-item label="源单号" prop="sourceNo">
          <el-input v-model="form.sourceNo" style="width: 220px;" placeholder="请输入源单号" />
        </el-form-item>

        <el-form-item label="入库商品">
          <el-row :gutter="10" class="mb8" >
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddSShopOrderItem">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteSShopOrderItem">删除</el-button>
          </el-col>
        </el-row>
        </el-form-item>
        <!-- <el-divider content-position="center" style="margin-left: 98px;">商品信息</el-divider> -->

        <el-table style="margin-left: 108px;margin-bottom: 20px;" :data="form.itemList" :row-class-name="rowSShopOrderItemIndex" @selection-change="handleSShopOrderItemSelectionChange" ref="sShopOrderItem">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <!-- <el-table-column label="erp系统商品id" prop="goodsId" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.goodsId" placeholder="请输入erp系统商品id" />
            </template>
          </el-table-column>
          <el-table-column label="erp系统商品规格id" prop="specId" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.specId" placeholder="请输入erp系统商品规格id" />
            </template>
          </el-table-column> -->
          <el-table-column label="商品" prop="skuId" width="350">
            <template slot-scope="scope">
              <!-- <el-input v-model="scope.row.goodsTitle" placeholder="请输入商品标题" /> -->
              <el-select v-model="scope.row.id" filterable remote reserve-keyword placeholder="搜索商品SKU" style="width: 330px;"
                :remote-method="searchSku" :loading="skuListLoading" @change="skuChanage(scope.row)">
                <el-option v-for="item in skuList" :key="item.id"
                  :label="item.goodsName + ' ' + item.standard +' - ' + item.erpGoodsNo"
                  :value="item.id">
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="商品图片" prop="goodsImg" width="150">
            <template slot-scope="scope">
              <!-- <el-input v-model="scope.row.goodsImg" placeholder="请输入商品图片" /> -->
              <el-image style="width: 70px; height: 70px" :src="scope.row.imageUrl"></el-image>
            </template>
          </el-table-column>
          <el-table-column label="商品规格" prop="skuName" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.standard" disabled placeholder="请输入商品规格" />
            </template>
          </el-table-column>
          <el-table-column label="Sku编码" prop="erpGoodsNo" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.erpGoodsNo" disabled placeholder="请输入商品规格编码" />
            </template>
          </el-table-column>
          <el-table-column label="数量" prop="quantity" width="150">
            <template slot-scope="scope">
              <el-input v-model.number="scope.row.quantity" placeholder="请输入商品数量" />
            </template>
          </el-table-column>

        </el-table>

      <el-form-item label="操作人" prop="stockInOperator">
        <el-input v-model="form.stockInOperator" style="width: 220px;" placeholder="请输入操作人" />
      </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" style="width: 400px;" placeholder="备注" />
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer" style="margin-left: 108px;">
        <el-button type="primary" @click="submitForm">创建入库单</el-button>
        <!-- <el-button @click="cancel">取 消</el-button> -->
      </div>
  </div>
</template>

<script>
import { searchSku } from "@/api/vms/goods/goods";
import {stockInCreate} from "@/api/vms/wms/stockIn";
import {listMerchant} from "@/api/vms/merchant";
export default {
  name: "StockInCreate",
  data() {
    return {
      // 表单参数
      form: {
        stockInNum:null,
        stockInType:null,
        sourceNo:null,
        merchantId:null,
        itemList:[]
      },
      // 表单校验
      rules: {
        stockInNum: [{ required: true, message: '单号不能为空' }],
        stockInType: [{ required: true, message: '不能为空' }],
        merchantId: [{ required: true, message: '请选择' }],
        // sourceNo: [{ required: true, message: '不能为空' }],
        // stockInOperator: [{ required: true, message: '请填写操作人' }],
      },
      skuListLoading: false,
      skuList: [],
      merchantList: [],

      // 子表选中数据
      checkedSShopOrderItem: []
    };
  },
  created() {
    listMerchant().then(resp=>{
      this.merchantList = resp.rows
    })
    this.genOrderNum()
  },
  methods: {
    genOrderNum(){
      const timestamp = Date.now();
      // 可以使用随机数增加订单号的唯一性
      const randomNumber = Math.floor(Math.random() * 1000);
      const orderNum = `${timestamp}${randomNumber}`;
      this.form.stockInNum = orderNum;
      console.log("======生成单号=======",orderNum)
    },
    // getDate() {
    //   var now = new Date();
    //   var year = now.getFullYear(); //得到年份
    //   var month = now.getMonth(); //得到月份
    //   var date = now.getDate(); //得到日期
    //   var hour = " 00:00:00"; //默认时分秒 如果传给后台的格式为年月日时分秒，就需要加这个，如若不需要，此行可忽略
    //   month = month + 1;
    //   month = month.toString().padStart(2, "0");
    //   date = date.toString().padStart(2, "0");
    //   var defaultDate = `${year}-${month}-${date}`;//
    //   return defaultDate;
    // },
    // 搜索SKU
    searchSku(query) {
      if(!this.form.merchantId) {
        this.$modal.msgError("请选择商户")
        return
      }else{
        this.shopLoading = true;
        const qw = {
          keyword: query,
          merchantId:this.form.merchantId
        }
        searchSku(qw).then(res => {
          this.skuList = res.rows;
          this.skuListLoading = false;
        })
      }

    },
    skuChanage(row) {
      console.log('=====0000====',row)
      const spec = this.skuList.find(x => x.id === row.id);
      if (spec) {
        console.log('=======11111==', spec)
        row.quantity = 1
        row.id = spec.id
        row.goodsName = spec.goodsName
        row.standard = spec.standard
        row.goodsNo = spec.goodsNo
        row.erpGoodsNo = spec.erpGoodsNo
        row.imageUrl = spec.imageUrl

      }
    },
    /** ${subTable.functionName}添加按钮操作 */
    handleAddSShopOrderItem() {
      let obj = {};
      obj.id = "";
      obj.goodsNo = "";
      obj.erpGoodsNo = "";
      obj.goodsName = "";
      obj.imageUrl = "";
      obj.standard = "";
      obj.quantity = "";
      this.form.itemList.push(obj);
    },
    /** ${subTable.functionName}删除按钮操作 */
    handleDeleteSShopOrderItem() {
      if (this.checkedSShopOrderItem.length == 0) {
        this.$modal.msgError("请先选择要删除的商品数据");
      } else {
        const sShopOrderItemList = this.form.itemList;
        const checkedSShopOrderItem = this.checkedSShopOrderItem;
        this.form.itemList = sShopOrderItemList.filter(function(item) {
          return checkedSShopOrderItem.indexOf(item.index) == -1
        });
      }
    },
    /** 复选框选中数据 */
    handleSShopOrderItemSelectionChange(selection) {
      this.checkedSShopOrderItem = selection.map(item => item.index)
    },
    /** ${subTable.functionName}序号 */
    rowSShopOrderItemIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if(this.form.itemList && this.form.itemList.length >0){
            for(var i=0;i<this.form.itemList.length;i++){
              if(!this.form.itemList[i].id || !this.form.itemList[i].quantity){
                this.$modal.msgError("请完善商品信息");
                return
              }
            }
            // this.form.itemList.forEach(x=>{
            //   if(!x.goodsId || !x.quantity){
            //     this.$modal.msgError("请完善商品信息");
            //     return
            //   }
            // })

            console.log('======创建入库单=====',this.form)
            stockInCreate(this.form).then(response => {
              this.$modal.msgSuccess("创建成功");
              // 调用全局挂载的方法,关闭当前标签页
              this.$store.dispatch("tagsView/delView", this.$route);
              this.$router.push('/stock_in/stock_in');
            });

        }else{
          this.$modal.msgError("请添加商品");
        }

        }
      });
    }
  }
};

</script>
