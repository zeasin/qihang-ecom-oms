<template>
  <div class="app-container">
    <el-form ref="form" :model="form" :rules="rules" label-width="108px">

        <el-form-item label="店铺" prop="shopId" >
          <el-select v-model="form.shopId" filterable placeholder="搜索店铺" @change="handleShopChange" >
            <el-option v-for="item in shopList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
      <el-form-item label="商品名称" prop="goodsName">
        <el-input v-model="form.goodsName" placeholder="请输入商品名称" style="width:680px"/>
      </el-form-item>
      <el-form-item label="商品图片" prop="goodsImg">
        <image-upload v-model="form.goodsImg" :limit="1"/>
        <el-input v-model="form.goodsImg" placeholder="请输入商品图片" style="width:680px"/>
      </el-form-item>
      <el-form-item label="商品编号" prop="goodsNum" >
        <el-input v-model="form.goodsNum" placeholder="请输入商品编号" style="width:220px"/>
      </el-form-item>
      <el-form-item label="平台ID" prop="productId" >
        <el-input v-model="form.productId" placeholder="平台ID" style="width:220px"/>
      </el-form-item>
      <el-form-item label="售价" prop="minPrice">
        <el-input type="number" v-model.number="form.minPrice" placeholder="请输入售价" style="width:220px"/>
      </el-form-item>
<!--      <el-form-item label="库存数量" prop="retailPrice">-->
<!--        <el-input v-model.number="form.retailPrice" placeholder="请输入建议零售价" style="width:220px"/>-->
<!--      </el-form-item>-->


        <el-form-item label="商品规格">
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

        <el-table style="margin-left: 108px;margin-bottom: 20px;" :data="form.skuList" :row-class-name="rowSShopOrderItemIndex" @selection-change="handleSShopOrderItemSelectionChange" ref="sShopOrderItem">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="规格" prop="skuName" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.skuName" placeholder="请输入规格" />
            </template>
          </el-table-column>

          <el-table-column label="平台规格ID" prop="skuId" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.skuId" placeholder="平台规格ID" />
            </template>
          </el-table-column>
          <el-table-column label="SKU编码" prop="skuCode" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.skuCode" placeholder="请输入SKU编码" />
            </template>
          </el-table-column>


          <el-table-column label="库存数量" prop="stockNum" width="150">
            <template slot-scope="scope">
              <el-input v-model.number="scope.row.stockNum" placeholder="请输入库存数量" />
            </template>
          </el-table-column>
          <el-table-column label="售价" prop="price" width="150">
            <template slot-scope="scope">
              <el-input type="number" v-model.number="scope.row.price" placeholder="售价" />
            </template>
          </el-table-column>

        </el-table>


      </el-form>
      <div slot="footer" class="dialog-footer" style="margin-left: 108px;">
        <el-button type="primary" @click="submitForm">添加店铺商品</el-button>
        <!-- <el-button @click="cancel">取 消</el-button> -->
      </div>
  </div>
</template>

<script>

import { listShop } from '@/api/shop/shop'
import { addShopGoods } from '@/api/shop/goods'

export default {
  name: "StockInCreate",
  data() {
    return {
      // 表单参数
      form: {
        shopId:null,
        shopType:null,
        merchantId:null,
        minPrice:null,
        productId:null,
        goodsName:null,
        goodsNum:null,
        goodsImg:null,
        skuList:[]
      },
      // 表单校验
      rules: {
        shopId: [{ required: true, message: '请选择店铺' }],
        goodsName: [{ required: true, message: '商品名称不能为空' }],
        productId: [{ required: true, message: '不能为空' }],
        minPrice: [{ required: true, message: '不能为空' }],

      },
      skuListLoading: false,
      skuList: [],
      warehouseList: [],
      shopList: [],
      // 子表选中数据
      checkedSShopOrderItem: []
    };
  },
  created() {
    if(this.$route.query.shopType){
      this.shopType = this.$route.query.shopType
    }
    listShop({type:this.shopType}).then(response => {
      this.shopList = response.rows;

    });
  },
  methods: {
    /** 店铺选择变化 */
    handleShopChange(shopId) {
      const shop = this.shopList.find(item => item.id === shopId);
      if (shop) {
        this.form.shopType = shop.type;
        this.form.merchantId = shop.merchantId;
      }
    },
    /** ${subTable.functionName}添加按钮操作 */
    handleAddSShopOrderItem() {
      let obj = {};
      obj.skuId = "";
      obj.skuName = "";
      obj.skuCode = "";
      obj.stockNum = null;
      obj.price = null;
      this.form.skuList.push(obj);
    },
    /** ${subTable.functionName}删除按钮操作 */
    handleDeleteSShopOrderItem() {
      if (this.checkedSShopOrderItem.length == 0) {
        this.$modal.msgError("请先选择要删除的商品数据");
      } else {
        const sShopOrderItemList = this.form.skuList;
        const checkedSShopOrderItem = this.checkedSShopOrderItem;
        this.form.skuList = sShopOrderItemList.filter(function(item) {
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
          if(this.form.skuList && this.form.skuList.length >0){
            for(var i=0;i<this.form.skuList.length;i++){
              if(!this.form.skuList[i].skuId || !this.form.skuList[i].skuName|| !this.form.skuList[i].skuCode|| !this.form.skuList[i].price){
                this.$modal.msgError("请完善商品规格信息");
                return
              }
            }
            // this.form.itemList.forEach(x=>{
            //   if(!x.goodsId || !x.quantity){
            //     this.$modal.msgError("请完善商品信息");
            //     return
            //   }
            // })

            console.log('======手动添加店铺商品=====',this.form)
            addShopGoods(this.form).then(response => {
              if(response.code == 200){
                this.$modal.msgSuccess("创建成功");
                // 调用全局挂载的方法,关闭当前标签页
                this.$store.dispatch("tagsView/delView", this.$route);
                // 带上shopType、merchantId、shopId参数跳转，用于刷新列表
                this.$router.push({
                  path: '/goods/shop_goods',
                  query: {
                    shopType: this.form.shopType || '',
                    merchantId: this.form.merchantId || '',
                    shopId: this.form.shopId || '',
                    refresh: '1'
                  }
                });
              }else this.$modal.msgError(response.msg);

            });

        }else{
          this.$modal.msgError("请添加商品规格");
        }

        }
      });
    }
  }
};

</script>
