<template>
  <div class="app-container">
    <el-form ref="form" :model="form" :rules="rules" label-width="108px" inline>
      <el-col :span="24">
        <el-form-item label="订单号" prop="orderNum">
            <el-input v-model="form.orderNum" style="width:220px" placeholder="请输入订单编号" />
            <el-button type="" size="mini" @click="genOrderNum">生成订单号</el-button>
        </el-form-item>
      </el-col>
<!--      <el-col :span="24">-->
<!--        <el-form-item label="销售店铺" prop="shopId" >-->
<!--          <el-select v-model="form.shopId" filterable placeholder="搜索店铺" >-->
<!--          <el-option v-for="item in shopList" :key="item.id" :label="item.name" :value="item.id">-->
<!--             <span style="float: left">{{ item.name }}</span>-->
<!--            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.merchantId === 0">自营店铺</span>-->
<!--            <span style="float: right; color: #8492a6; font-size: 13px"  v-else>{{item.merchantName}}</span>-->
<!--          </el-option>-->
<!--        </el-select>-->
<!--        </el-form-item>-->
<!--        </el-col>-->
        <el-col :span="24">
          <el-form-item label="订单类型" prop="orderType">
            <el-select v-model="form.orderType"  placeholder="订单类型" >
              <el-option label="销售订单" value="0"></el-option>
              <el-option label="代发订单" value="1"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="客户" prop="customerType">
            <el-select v-model="form.customerType"  placeholder="客户类型" >
              <el-option label="集团商户" value="20"></el-option>
              <el-option label="外部2B客户" value="100"></el-option>
              <el-option label="消费者" value="0"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      <el-col :span="24">
        <el-form-item label="商户" prop="merchantId">
          <el-select :loading="merchantLoading" v-model="form.merchantId" filterable clearable placeholder="选择商户" @change="merchantChange">
            <el-option v-for="item in merchantList" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="商户店铺" prop="shopId">
          <el-select :loading="shopLoading" v-model="form.shopId" remote reserve-keyword :remote-method="searchShop" filterable clearable placeholder="搜索商户店铺" @change="shopChange">
            <el-option v-for="item in merchantShopList" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="24">
      <el-form-item label="智能识别" prop="addressAll">
        <el-input type="textarea" v-model="form.addressAll" placeholder="" style="width: 800px" /><br/>
        <el-button size="mini" @click="parseAddress">识别</el-button>
      </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="收件人姓名" prop="receiverName">
          <el-input v-model="form.receiverName" style="width: 220px;" placeholder="请输入收件人姓名" />
        </el-form-item>
        <el-form-item label="收件人手机号" prop="receiverPhone">
          <el-input v-model="form.receiverPhone"  style="width: 220px;" placeholder="请输入收件人手机号" />
        </el-form-item>

      </el-col>
        <el-col :span="24">
        <!-- <el-form-item label="省市区" prop="town">
            <el-input v-model="form.province" style="width: 120px;" placeholder="请输入省" />
            <el-input v-model="form.city" placeholder="请输入市" style="width: 120px;margin-left: 10px;" />
            <el-input v-model="form.town" placeholder="请输入区"  style="width: 120px;margin-left: 10px;"/>
        </el-form-item> -->
          <el-form-item label="省市区" prop="provinces">
            <el-cascader style="width:220px"
                         size="large"
                         :options="pcaTextArr"
                         v-model="form.provinces">
            </el-cascader>
          </el-form-item>
        </el-col>
          <el-col :span="24">
        <el-form-item label="详细地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入详细地址" style="width: 520px;" />
        </el-form-item>
        </el-col>
      <el-col :span="24">
        <el-form-item label="商品明细">
          <el-row :gutter="10" class="mb8" >
          <el-col :span="1.5">
            <el-button size="small" @click="addGoodsDialog"> 添加商品</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteSShopOrderItem">删除</el-button>
          </el-col>
        </el-row>
        </el-form-item>
        <!-- <el-divider content-position="center" style="margin-left: 98px;">商品信息</el-divider> -->
      </el-col>
        <el-table style="margin-left: 108px;width: 1300px" :data="form.itemList" height="500px" :row-class-name="rowSShopOrderItemIndex" @selection-change="handleSShopOrderItemSelectionChange" ref="sShopOrderItem">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="" prop="goodsImg" width="60">
            <template slot-scope="scope">
              <image-preview :src="scope.row.colorImage" :width="40" :height="40"/>
            </template>
          </el-table-column>
          <el-table-column label="SkuID" prop="id" width="60"> </el-table-column>
          <el-table-column label="商品" prop="goodsName" width="250">
          </el-table-column>

          <el-table-column label="商品规格" prop="skuName" width="200">
            <template slot-scope="scope">
                <el-tag size="small" v-if="scope.row.colorValue">{{ scope.row.colorValue }}</el-tag>
                <el-tag size="small" v-if="scope.row.sizeValue">{{ scope.row.sizeValue }}</el-tag>
                <el-tag size="small" v-if="scope.row.styleValue">{{ scope.row.styleValue }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="Sku编码" prop="skuCode" width="200"></el-table-column>
          <el-table-column label="商品单价" prop="salePrice" width="120">
            <template slot-scope="scope">
              <el-input v-model="scope.row.retailPrice"  placeholder="商品单价" @input="handleRetailPriceInput(scope.row)"/>
            </template>
          </el-table-column>
          <el-table-column label="商品数量" prop="quantity" width="120">
            <template slot-scope="scope">
              <el-input v-model.number="scope.row.quantity" placeholder="商品数量" @input="qtyChange(scope.row)"/>
            </template>
          </el-table-column>
          <el-table-column label="金额" prop="itemAmount" width="150"  v-if="false">
            <template slot-scope="scope">
              <el-input v-model="scope.row.itemAmount" disabled placeholder="请输入子订单金额" />
            </template>
          </el-table-column>

          <el-table-column label="是否赠品" prop="isGift" width="100">
            <template slot-scope="scope">
              <!-- <el-input v-model="scope.row.isGift" placeholder="请输入是否赠品0否1是" /> -->
              <el-select v-model="scope.row.isGift" placeholder="是否赠品" @change="qtyChange(scope.row)">
                <el-option label="否" value="0" />
                <el-option label="是" value="1" />
              </el-select>
            </template>
          </el-table-column>
        </el-table>
      <el-row style="padding-top: 20px">
      <el-col  :span="24">
        <el-form-item label="商品金额" prop="goodsAmount" style="margin-top: 10px;">
          <el-input v-model="form.goodsAmount" style="width: 220px;" placeholder="商品金额，单位：元" />
        </el-form-item>

      </el-col>
      </el-row>
      <el-col :span="24">
        <el-form-item label="运费" prop="postFee">
          <el-input v-model="form.postFee" style="width:220px" placeholder="邮费，单位：元" @input="handlePostFeeInput" />
        </el-form-item>
        <el-form-item label="折扣金额" prop="sellerDiscount">
          <el-input v-model="form.sellerDiscount" style="width: 220px;" placeholder="折扣金额，单位：元" @input="handleSellerDiscountInput" />
        </el-form-item>
      </el-col>
<!--        <el-form-item label="支付金额" prop="payAmount">-->
<!--          <el-input v-model="form.payAmount" style="width: 220px;" placeholder="请输入支付金额，单位：元，支付金额=商品金额-折扣金额+邮费" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="支付时间" prop="payTime">-->
<!--          <el-date-picker clearable v-model="form.payTime" type="date" style="width: 220px;" value-format="yyyy-MM-dd" placeholder="请选择支付日期">-->
<!--        </el-date-picker>-->
<!--          &lt;!&ndash; <el-input v-model="form.payTime" style="width: 220px;" placeholder="请输入支付时间" /> &ndash;&gt;-->
<!--        </el-form-item>-->
      <el-col :span="24">
      <el-form-item label="买家留言" prop="buyerMemo">
        <el-input v-model="form.buyerMemo" type="textarea" placeholder="请输入买家留言信息"  style="width:520px"/>
      </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="订单备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"  style="width:520px"/>
        </el-form-item>
      </el-col>
<!--        <el-form-item label="标签" prop="tag">-->
<!--          <el-input v-model="form.tag" placeholder="请输入标签" style="width: 220px;" />-->
<!--        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer" style="margin-left: 108px;">
        <el-button type="primary" @click="submitForm">创建订单</el-button>
        <!-- <el-button @click="cancel">取 消</el-button> -->
      </div>

    <!-- 添加品对话框 -->
    <PopupSkuList @data-from-select="handleDataFromPopup" :btn="1" ref="popup"></PopupSkuList>
  </div>
</template>

<script>

import PopupSkuList from '@/views/goods/PopupSkuList.vue'
import { addOrder } from "@/api/order/sales_order";
import {getShop, listShop} from "@/api/shop/shop";
import {
  provinceAndCityData,
  pcTextArr,
  regionData,
  pcaTextArr,
  codeToText,
} from "element-china-area-data";
import { listAllMerchant } from '@/api/shop/merchant'
import { limitDecimalLength, stringToNumber } from "@/utils/numberInput";


export default {
  name: "OrderCreate",
  components: {
    PopupSkuList
  },
  data() {
    return {
      shopLoading:true,
      merchantLoading:true,
      // 表单参数
      form: {
        customerType:'20',
        orderNum:null,
        merchantId:null,
        shopId:null,
        goodsAmount:0.0,
        postFee:0.0,
        sellerDiscount:0.0,
        receiverPhone:'',
        receiverName:'',
        address:'',
        buyerMemo:null,
        orderType:'0',
        itemList:[],
        provinces: []
      },
      // 添加商品弹窗form
      goodsForm:{},
      pcaTextArr,
      // 表单校验
      rules: {
        orderNum: [{ required: true, message: '订单号不能为空' }],
        orderType: [{ required: true, message: '请选择' }],
        customerType: [{ required: true, message: '请选择客户类型' }],
        merchantId: [{ required: true, message: '不能为空' }],
        receiverName: [{ required: true, message: '请填写收件人信息' }],
        receiverPhone: [{ required: true, message: '请填写收件人信息' }],
        provinces: [{ required: true, message: '请填写收货信息' }],
        city: [{ required: true, message: '请填写收货信息' }],
        town: [{ required: true, message: '请填写收货信息' }],
        address: [{ required: true, message: '请填写收货信息' }],
        goodsAmount: [{ required: true, message: '请填写商品价格' }],
        postFee: [{ required: true, message: '请填写运费' }],
        discountAmount: [{ required: true, message: '请填写折扣金额' }]
      },
      shopList: [],
      merchantList: [],
      merchantShopList: [],
      skuListLoading: false,
      skuList: [],

      // 子表选中数据
      checkedSShopOrderItem: []
    };
  },
  created() {
    this.form.orderDate = this.getDate()
    if(this.$route.query.merchantId) {
      this.form.merchantId = this.$route.query.merchantId;
    }
    if(this.form.customerType==='20'){
      listAllMerchant().then(response => {
        this.merchantList = response.data;
        this.merchantLoading = false;
      })
    }

    // listShop({merchantId:0}).then(response => {
    //     this.shopList = response.rows;
    //     if(this.shopList&&this.shopList.length >0 ){
    //       this.form.shopId=this.shopList[0].id
    //     }
    //     this.shopLoading = false
    //   });
    this.genOrderNum()
  },
  methods: {
     parseAddress() {
       if (this.form.addressAll) {
         const nameRegex =/(?<![\u4e00-\u9fa5])[\u4e00-\u9fa5]{2,3}(?![\u4e00-\u9fa5])/gs
         const nameMatch = this.form.addressAll.match(nameRegex);
         const name = nameMatch ? nameMatch[0] : '';
         this.form.receiverName = name
         // console.log(nameMatch,name)

         let addressWithoutName = this.form.addressAll.replace(this.form.receiverName, '').trim();
         console.log('====end0000=====',addressWithoutName)
         // const phoneRegex = /(\b\d{11}(-\d{4})?\b)/;
         // const phoneRegex = /\d{11}\-\d{4}/;
         const phoneRegex = /(\b\d{11}(-\d{4})?\b)/gs;

         const phoneMatch = addressWithoutName.match(phoneRegex);
         let phone = phoneMatch ? phoneMatch[1] : '';
         if(!phone){
           phone = phoneMatch[0]
         }
         // console.log(phoneMatch,phone)
         this.form.receiverPhone = phone
         addressWithoutName = addressWithoutName.replace(this.form.receiverPhone, '').trim();
         addressWithoutName = addressWithoutName.replace(/ /g,'')
         console.log('====end1111=====',addressWithoutName)
         // let addressWithoutPhone = addressWithoutName.replace(phoneRegex, '').trim();
         const areaRegex = /(?<province>[\u4e00-\u9fa5]{1,5}[省/自治区/港/门])(?<city>[\u4e00-\u9fa5]+市)?(?<district>[\u4e00-\u9fa5]+[县/区])?(?<address>.*)/
         // const areaRegex = /(?<province>[\u4e00-\u9fa5]{1,5}(省|自治区|特别行政区|市))(?<city>[\u4e00-\u9fa5]+市)?(?<district>[\u4e00-\u9fa5]+(区|县))?(?<address>.*)/

         const addr = addressWithoutName.match(areaRegex);
         console.log('==============addr=======',addr)
         const areaMatch = addressWithoutName.match(areaRegex);
         const province = areaMatch ? areaMatch[1] : '';
         const city = areaMatch ? areaMatch[2] : '';
         const district = areaMatch ? areaMatch[3] : '';
         const address = areaMatch ? areaMatch[4] : '';
         this.form.provinces = [];
         this.form.provinces.push(province)
         this.form.provinces.push(city)
         this.form.provinces.push(district)
         this.form.address = address
         console.log('==============省市区=======',province,city,district)

       }
     },
    genOrderNum(){
       const orderDate = this.getDate().substring(2,).replace(/-/g,'')
      const timestamp =  Math.floor(Date.now() / 1000);;
      // 可以使用随机数增加订单号的唯一性
      const randomNumber = Math.floor(Math.random() * 1000);
      const orderNum = `${orderDate}-${timestamp}`;
      this.form.orderNum = orderNum;
      console.log("======生成订单号=======",orderNum)
    },
    getDate() {
      var now = new Date();
      var year = now.getFullYear(); //得到年份
      var month = now.getMonth(); //得到月份
      var date = now.getDate(); //得到日期
      var hour = " 00:00:00"; //默认时分秒 如果传给后台的格式为年月日时分秒，就需要加这个，如若不需要，此行可忽略
      month = month + 1;
      month = month.toString().padStart(2, "0");
      date = date.toString().padStart(2, "0");
      var defaultDate = `${year}-${month}-${date}`;//
      return defaultDate;
    },
    addGoodsDialog() {
      // this.goodsForm.id = null
      // this.goodsForm.goodsId = null
      // this.goodsForm.name = null
      // this.goodsForm.colorImage = null
      // this.goodsForm.colorValue = null
      // this.goodsForm.number = null
      // this.goodsForm.purPrice = null
      // this.goodsForm.sizeValue = null
      // this.goodsForm.specNum = null
      // this.goodsForm.styleValue = null
      // this.goodsForm.quantity = null
      // this.goodsForm.amount = null
      // this.goodsFormOpen = true
      this.$refs.popup.openDialog();
    },
    // 接收子组件传来的数据
    handleDataFromPopup(data) {
      console.log('Received data from popup:', data);
      if(data){
        data.forEach((item) => {
          const find = this.form.itemList.find(x => x.id === item.id)
          if (!find) {
            this.form.itemList.push(item)
          }
        })
      }
      let goodsAmount = 0;
      this.form.itemList.forEach(item => {
        if(!item.quantity) item.quantity= 1
        item.itemAmount = item.quantity*item.retailPrice
        goodsAmount+= item.itemAmount
        if(!item.isGift) item.isGift = '0'
      })
      console.log('====添加后=====',this.form.itemList)
      this.form.goodsAmount = goodsAmount
      this.form.orderAmount = goodsAmount
    },
    qtyChange(row) {
      console.log('======值变化=====', row)
      // row.itemAmount = row.retailPrice * row.quantity
      // // 计算总金额
      // let goodsAmountNew =0.0
      // this.form.itemList.forEach(x=>{
      //   goodsAmountNew+= row.itemAmount
      // })
      // this.form.goodsAmount = goodsAmountNew
      this.form.goodsAmount = 0
      if (this.form.itemList && this.form.itemList.length > 0) {
        this.form.itemList.forEach(item => {
          if(item.isGift==0){
            this.form.goodsAmount+= item.quantity*parseFloat(item.retailPrice || 0)
          }

        })
      }
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
    merchantChange(){
      this.form.shopId=null
      listShop({merchantId:this.form.merchantId}).then(response => {
        this.merchantShopList = response.rows;

        this.shopLoading = false
      });
    },
    searchShop(query){
      if (query !== '') {
        this.shopLoading = true;
        this.form.shopId=null
        listShop({merchantId:this.form.merchantId,name:query}).then(response => {
          this.merchantShopList = response.rows;
          this.shopLoading = false
        })
      }
    },
    shopChange(){
      getShop(this.form.shopId).then(resp=>{
        console.log('=======选择的店铺====',resp.data)
        if(resp.data){
          this.form.provinces = [];
          this.form.provinces.push(resp.data.province)
          this.form.provinces.push(resp.data.city)
          this.form.provinces.push(resp.data.district)
          this.form.receiverName=resp.data.contact
          this.form.receiverPhone=resp.data.phone
          this.form.address=resp.data.address
        }
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {

          this.form.province = this.form.provinces[0]
          this.form.city = this.form.provinces[1]
          this.form.town = this.form.provinces[2]
          // 将价格字段从字符串转换回数字类型
          this.form.postFee = stringToNumber(this.form.postFee);
          this.form.sellerDiscount = stringToNumber(this.form.sellerDiscount);
          if(this.form.itemList && this.form.itemList.length >0){
            for(var i=0;i<this.form.itemList.length;i++){
              if(!this.form.itemList[i].id || !this.form.itemList[i].quantity){
                this.$modal.msgError("请完善商品信息");
                return
              }
              this.form.itemList[i].retailPrice = stringToNumber(this.form.itemList[i].retailPrice);
            }
            // this.form.itemList.forEach(x=>{
            //   if(!x.goodsId || !x.quantity){
            //     this.$modal.msgError("请完善商品信息");
            //     return
            //   }
            // })

            console.log('======创建订单=====',this.form)
            addOrder(this.form).then(response => {
              this.$modal.msgSuccess("订单创建成功");
              // 调用全局挂载的方法,关闭当前标签页
              this.$store.dispatch("tagsView/delView", this.$route);
              this.$router.push('sale_order_list');
            });

        }else{
          this.$modal.msgError("请添加商品");
        }

        }
      });
    },
    handleRetailPriceInput(row) {
      row.retailPrice = limitDecimalLength(row.retailPrice);
      this.qtyChange(row);
    },
    handlePostFeeInput() {
      this.form.postFee = limitDecimalLength(this.form.postFee);
    },
    handleSellerDiscountInput() {
      this.form.sellerDiscount = limitDecimalLength(this.form.sellerDiscount);
    }
  }
};

</script>
