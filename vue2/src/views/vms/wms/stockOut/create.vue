<template>
  <div class="app-container">
    <el-form ref="form" :model="form" :rules="rules" label-width="108px">
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
        <el-form-item label="出库单号" prop="outNum">
          <el-col :span="24">
            <el-input v-model="form.outNum" style="width:220px" placeholder="请输入出库单号" />
            <el-button type="" size="mini" @click="genOrderNum">生成单号</el-button>
          </el-col>
        </el-form-item>
        <el-form-item label="出库类型" prop="type">
          <el-select v-model="form.type" filterable r placeholder="出库类型" >
          <el-option  label="订单发货出库" value="1"></el-option>
          <el-option  label="采购退货出库" value="2"></el-option>
          <el-option  label="盘亏出库" value="3"></el-option>
          <el-option  label="报损出库" value="4"></el-option>
        </el-select>
        </el-form-item>

        <el-form-item label="源单号" prop="sourceNo">
          <el-input v-model="form.sourceNo" style="width: 220px;" placeholder="请输入源单号" />
        </el-form-item>

        <el-form-item label="出库商品">
          <el-row :gutter="10" class="mb8" >
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddGoods">添加</el-button>
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
          <el-table-column label="仓库商品ID" prop="id" width="150"></el-table-column>
          <el-table-column label="ERP商品ID" prop="erpGoodsSkuId" width="150"></el-table-column>
          <el-table-column label="商家商品编码" prop="erpGoodsNo" width="150"></el-table-column>
          <el-table-column label="商品图片" prop="goodsImg" width="150">
            <template slot-scope="scope">
              <image-preview :src="scope.row.imageUrl" :width="55" :height="55"/>
            </template>
          </el-table-column>


          <el-table-column label="商品名" prop="goodsName" width="150">
          </el-table-column>
          <el-table-column label="规格" prop="standard" width="150">
          </el-table-column>

          <el-table-column label="当前库存" prop="currentQty" width="100" />

          <el-table-column label="出库数量" prop="quantity" width="110">
            <template slot-scope="scope">
              <el-input v-model.number="scope.row.quantity" placeholder="出库数量" />
            </template>
          </el-table-column>
        </el-table>

<!--      <el-form-item label="操作人" prop="operator">-->
<!--        <el-input v-model="form.operator" style="width: 220px;" placeholder="请输入操作人" />-->
<!--      </el-form-item>-->

        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" style="width: 400px;" placeholder="备注" />
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer" style="margin-left: 108px;">
        <el-button type="primary" @click="submitForm">创建出库单</el-button>
        <!-- <el-button @click="cancel">取 消</el-button> -->
      </div>

    <PopupWarehouseGoodsItems @data-from-select="handleDataFromPopup" :merchantId="form.merchantId" :btn="1" ref="popup"></PopupWarehouseGoodsItems>

  </div>
</template>

<script>
import { getGoodsStockQty, searchGoodsStockBatch } from '@/api/vms/wms/goods_inventory'
import {stockOutCreate} from "@/api/vms/wms/stockOut";
import PopupWarehouseGoodsItems from '@/views/vms/goods/PopupWarehouseGoodsItems.vue'
import { listMerchant } from '@/api/vms/merchant'


export default {
  name: "StockInCreate",
  components: { PopupWarehouseGoodsItems },
  data() {
    return {
      // 表单参数
      form: {
        merchantId:'0',
        outNum:null,
        type:null,
        sourceNo:null,
        itemList:[],
        operator:null
      },
      // 表单校验
      rules: {
        merchantId: [{ required: true, message: '请选择商户' }],
        outNum: [{ required: true, message: '单号不能为空' }],
        type: [{ required: true, message: '请选择入库类型' }],
        shopGroupId: [{ required: true, message: '不能为空' }],
        // operator: [{ required: true, message: '请填写操作人' }],
      },
      skuListLoading: false,
      skuList: [],
      shopList: [],
      shopGroupList: [],
      merchantList: [],
      // 子表选中数据
      checkedSShopOrderItem: []
    };
  },
  created() {
    listMerchant().then(resp=>{
      this.merchantList = resp.rows
      this.genOrderNum()
    })
  },
  methods: {
    genOrderNum(){
      const timestamp = Date.now();
      // 可以使用随机数增加订单号的唯一性
      const randomNumber = Math.floor(Math.random() * 1000);
      const orderNum = `${timestamp}${randomNumber}`;
      this.form.outNum = orderNum;
      console.log("======生成单号=======",orderNum)
    },
    handleAddGoods(){
      if(!this.form.merchantId) {
        this.$modal.msgError("请选择商户");
        return;
      }
      this.$refs.popup.openDialog();
    },
    // 接收子组件传来的数据
    handleDataFromPopup(data) {
      console.log('Received data from popup:', data);
      if(data) {
        data.forEach((item) => {
          const find = this.form.itemList.find(x => x.id === item.id)
          if (!find) {
            // 查询商品库存
            getGoodsStockQty({goodsId:item.id}).then(resp=>{
              item.currentQty = resp.data
              this.form.itemList.push(item)
            })


          }
        })
      }
      console.log('====添加后=====',this.form.itemList)

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
              if(!this.form.itemList[i].quantity || this.form.itemList[i].quantity<=0){
                this.$modal.msgError("请填写出库数量");
                return
              }else if(this.form.itemList[i].quantity > this.form.itemList[i].currentQty){
                this.$modal.msgError("出库数量不能大于当前库存");
                return
              }
            }
            // this.form.itemList.forEach(x=>{
            //   if(!x.goodsId || !x.quantity){
            //     this.$modal.msgError("请完善商品信息");
            //     return
            //   }
            // })

            console.log('======创建出库单=====',this.form)
            stockOutCreate(this.form).then(response => {
              this.$modal.msgSuccess("创建成功");
              // 调用全局挂载的方法,关闭当前标签页
              this.$store.dispatch("tagsView/delView", this.$route);
              this.$router.push('/stock_out/stock_out');
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
