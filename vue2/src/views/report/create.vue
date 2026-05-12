<template>
  <div class="app-container">
    <el-form ref="form" :model="form" :rules="rules" label-width="108px">
      <el-form-item label="日期" prop="date">
        <el-date-picker clearable v-model="form.date" value-format="yyyy-MM-dd" type="date">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="店铺" prop="shopId">
        <el-select v-model="form.shopId" filterable r placeholder="搜索店铺" >
          <el-option v-for="item in shopList" :key="item.id" :label="item.name" :value="item.id">
            <span style="float: left">{{ item.name }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  >
              {{ platformList.find(x=>x.id === item.type) ? platformList.find(x=>x.id === item.type).name : '' }}
            </span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="总订单数" prop="orderTotal">
        <el-input v-model.number="form.orderTotal" style="width: 220px;" placeholder="请输入总订单数" />
      </el-form-item>
      <el-form-item label="总销售额" prop="orderAmount">
        <el-input type="number" v-model.number="form.orderAmount" style="width: 220px;" placeholder="请输入总销售额" />
      </el-form-item>
      <el-form-item label="刷单数量" prop="falseOrderTotal">
        <el-input  v-model.number="form.falseOrderTotal" style="width: 220px;" placeholder="请输入刷单数量" />
      </el-form-item>
      <el-form-item label="刷单金额" prop="falseOrderAmount">
        <el-input type="number" v-model.number="form.falseOrderAmount" style="width: 220px;" placeholder="请输入刷单金额" />
      </el-form-item>
      <el-form-item label="广告支出" prop="adFee">
        <el-input type="number" v-model.number="form.adFee" style="width: 220px;" placeholder="请输入广告支出金额" />
      </el-form-item>
      <el-form-item label="广告点击次数" prop="adClick">
        <el-input v-model.number="form.adClick" style="width: 220px;" placeholder="请输入广告点击次数" />
      </el-form-item>
      <el-form-item label="提款" prop="withdrawalAmount">
        <el-input v-model.number="form.withdrawalAmount" style="width: 220px;" placeholder="请输入提款金额" />
      </el-form-item>

        <el-form-item label="商品明细">
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

        <el-table style="margin-left: 108px;" :data="form.itemList" :row-class-name="rowSShopOrderItemIndex" @selection-change="handleSShopOrderItemSelectionChange" ref="sShopOrderItem">
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
              <el-select v-model="scope.row.skuId" filterable remote reserve-keyword placeholder="搜索商品SKU" style="width: 330px;"
                :remote-method="searchSku" :loading="skuListLoading" @change="skuChanage(scope.row)">
                <el-option v-for="item in skuList" :key="item.skuId"
                  :label="item.goodsName + ' ' + item.skuName +' - ' + item.skuCode"
                  :value="item.skuId">
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="商品图片" prop="goodsImg" width="80">
            <template slot-scope="scope">
              <!-- <el-input v-model="scope.row.goodsImg" placeholder="请输入商品图片" /> -->
              <div>
                <el-image style="width: 70px; height: 70px" :src="scope.row.goodsImg"></el-image>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="商品规格" prop="skuName" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.skuName" disabled placeholder="请输入商品规格" />
            </template>
          </el-table-column>
          <el-table-column label="Sku编码" prop="skuCode" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.skuCode" disabled placeholder="请输入商品规格编码" />
            </template>
          </el-table-column>
          <el-table-column label="订单数量" prop="orderTotal" width="100">
            <template slot-scope="scope">
              <el-input v-model.number="scope.row.orderTotal" placeholder="订单数量" />
            </template>
          </el-table-column>
          <el-table-column label="订单金额" prop="orderAmount" width="150">
            <template slot-scope="scope">
              <el-input type="number" v-model.number="scope.row.orderAmount"  placeholder="订单金额" />
            </template>
          </el-table-column>
          <el-table-column label="刷单数量" prop="falseOrderTotal" width="100">
            <template slot-scope="scope">
              <el-input v-model.number="scope.row.falseOrderTotal" placeholder="刷单数量" />
            </template>
          </el-table-column>
          <el-table-column label="刷单费用（人民币）" prop="falseOrderAmount" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.falseOrderAmount" placeholder="刷单费用（人民币" />
            </template>
          </el-table-column>
          <el-table-column label="广告费用" prop="adFee" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.adFee" placeholder="广告费用" />
            </template>
          </el-table-column>
          <el-table-column label="广告点击" prop="adClick" width="100">
            <template slot-scope="scope">
              <el-input v-model="scope.row.adClick" placeholder="广告点击" />
            </template>
          </el-table-column>
<!--          <el-table-column label="是否赠品0否1是" prop="isGift" width="150">-->
<!--            <template slot-scope="scope">-->
<!--              &lt;!&ndash; <el-input v-model="scope.row.isGift" placeholder="请输入是否赠品0否1是" /> &ndash;&gt;-->
<!--              <el-select v-model="scope.row.isGift" placeholder="是否赠品">-->
<!--                <el-option label="否" value="0" />-->
<!--                <el-option label="是" value="1" />-->
<!--              </el-select>-->
<!--            </template>-->
<!--          </el-table-column>-->
        </el-table>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" style="margin-left: 108px;">
        <el-button type="primary" @click="submitForm">保存数据</el-button>
        <!-- <el-button @click="cancel">取 消</el-button> -->
      </div>
  </div>
</template>

<script>

import { searchSku } from "@/api/goods/goods";
import { addDaily } from "@/api/shop/daily";
import { listShop,listPlatform } from "@/api/shop/shop";

export default {
  name: "OrderCreate",
  data() {
    return {
      // 表单参数
      form: {
        date:null,
        adFee:0.0,
        withdrawalAmount:0.0,
        shopId:null,
        adClick:0,
        orderTotal:0,
        orderAmount:0,
        falseOrderTotal:0,
        falseOrderAmount:0.0,
        remark:null,
        itemList:[],
      },
      // 表单校验
      rules: {
        date: [{ required: true, message: '不能为空' }],
        shopId: [{ required: true, message: '请选择店铺' }],
        orderTotal: [{ required: true, message: '不能为空' }],
        orderAmount: [{ required: true, message: '不能为空' }],
        falseOrderTotal: [{ required: true, message: '不能为空' }],
        falseOrderAmount: [{ required: true, message: '不能为空' }],
        adFee: [{ required: true, message: '不能为空' }],
        adClick: [{ required: true, message: '不能为空' }],
        withdrawalAmount: [{ required: true, message: '不能为空' }],
      },
      shopLoading: false,
      shopList: [],
      platformList: [],
      skuListLoading: false,
      skuList: [],

      // 子表选中数据
      checkedSShopOrderItem: []
    };
  },
  created() {
    this.form.date = this.getDate()
    listPlatform().then(response => {
      this.platformList = response.rows;
      listShop({verifyUser:1}).then(resp => {
        this.shopList = resp.rows;
      });
    });

  },
  methods: {
    getDate() {
      // var now = new Date();
      // var year = now.getFullYear(); //得到年份
      // var month = now.getMonth(); //得到月份
      // var date = now.getDate() - 1; //得到日期
      // var hour = " 00:00:00"; //默认时分秒 如果传给后台的格式为年月日时分秒，就需要加这个，如若不需要，此行可忽略
      // month = month + 1;
      // month = month.toString().padStart(2, "0");
      // date = date.toString().padStart(2, "0");
      // var defaultDate = `${year}-${month}-${date}`;//
      // 获取当前日期
      let currentDate = new Date();

// 获取前一天的日期
      let previousDate = new Date(currentDate);
      previousDate.setDate(currentDate.getDate() - 1);
      let formattedDate = previousDate.getFullYear() + '-' + (previousDate.getMonth() + 1) + '-' + previousDate.getDate();
// 输出前一天的日期
      console.log(formattedDate); // 输出格式为：星期几 月 日 年
      return formattedDate;
    },
    // 搜索SKU
    searchSku(query) {
      this.shopLoading = true;
      const qw = {
        keyword: query
      }
      searchSku(qw).then(res => {
        this.skuList = res.rows;
        this.skuListLoading = false;
      })
    },
    skuChanage(row) {
      // console.log('=====0000====',row)
      const spec = this.skuList.find(x => x.skuId === row.skuId);
      if (spec) {
        console.log('=======11111==', spec)
        row.skuId = spec.skuId
        row.goodsName = spec.goodsName
        row.skuName = spec.skuName
        row.goodsImg = spec.colorImage
        row.skuCode = spec.skuCode
        row.falseOrderTotal = 0
        row.orderTotal = 0
        row.orderAmount = 0
        row.falseOrderAmount = 0.0
        row.adFee = 0.0
        row.adClick = 0
      }
    },
    /** ${subTable.functionName}添加按钮操作 */
    handleAddSShopOrderItem() {
      let obj = {};
      obj.skuId = "";
      obj.skuCode = "";
      obj.goodsName = "";
      obj.goodsImg = "";
      obj.skuName = "";
      obj.orderTotal = null;
      obj.orderAmount = null;
      obj.falseOrderTotal = 0;
      obj.falseOrderAmount = 0;
      obj.adFee = 0;
      obj.adClick = 0;

      this.form.itemList.push(obj);
    },
    /** ${subTable.functionName}删除按钮操作 */
    handleDeleteSShopOrderItem() {
      if (this.checkedSShopOrderItem.length == 0) {
        this.$modal.msgError("请先选择要删除的商品数据");
      } else {
        const sShopOrderItemList = this.form.itemList;
        const checkedSShopOrderItem = this.checkedSShopOrderItem;
        this.form.itemList = sShopOrderItemList.filter(function (item) {
          return checkedSShopOrderItem.indexOf(item.index) == -1
        });
      }
    },
    /** 复选框选中数据 */
    handleSShopOrderItemSelectionChange(selection) {
      this.checkedSShopOrderItem = selection.map(item => item.index)
    },
    /** ${subTable.functionName}序号 */
    rowSShopOrderItemIndex({row, rowIndex}) {
      row.index = rowIndex + 1;
    },
    /** 提交按钮 */
    submitForm() {
      console.log('=======提交数据======', this.form)
      this.$refs["form"].validate(valid => {
        if (valid) {

          if (this.form.itemList && this.form.itemList.length > 0) {
            for (var i = 0; i < this.form.itemList.length; i++) {
              if (!this.form.itemList[i].skuId) {
                this.$modal.msgError("请完善商品信息");
                return
              }
            }
          }
            console.log('======保存数据=====', this.form)
            addDaily(this.form).then(response => {
              this.$modal.msgSuccess("保存成功");
              // 调用全局挂载的方法,关闭当前标签页
              this.$store.dispatch("tagsView/delView", this.$route);
              this.$router.push('/report/dailyList');
            });

          }
        // else {
        //     this.$modal.msgError("请添加商品");
        //   }


      });
    }
  }
};

</script>
