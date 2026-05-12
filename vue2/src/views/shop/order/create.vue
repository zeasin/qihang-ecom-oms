<template>
  <div class="app-container">
    <el-form ref="form" :model="form" :rules="rules" label-width="108px" inline>
      <el-col :span="24">
        <el-form-item label="订单号" prop="orderNum">
            <el-input v-model="form.orderNum" style="width:220px" placeholder="请输入订单编号" />
            <el-button size="mini" style="margin-left: 20px;" @click="genOrderNum">生成订单号</el-button>
        </el-form-item>
      </el-col>
      <el-col :span="24" v-if="!form.shopId">
        <el-form-item label="店铺" prop="shopId" >
          <el-select v-model="form.shopId" filterable placeholder="搜索店铺" >
          <el-option v-for="item in shopList" :key="item.id" :label="item.name" :value="item.id">
             <span style="float: left">{{ item.name }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.merchantId === 0">总部店铺</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  v-else>{{item.merchantName}}</span>
          </el-option>
        </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="商品明细" prop="itemList">
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
      <el-table style="margin-left: 108px;margin-bottom: 16px;" :data="form.itemList" :row-class-name="rowSShopOrderItemIndex" @selection-change="handleSShopOrderItemSelectionChange" ref="sShopOrderItem">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column label="序号" align="center" prop="index" width="50"/>
        <el-table-column label="规格ID" prop="id" width="60"> </el-table-column>
        <el-table-column label="商品" prop="productTitle" width="200">
        </el-table-column>
        <el-table-column label="" prop="img" width="70">
          <template slot-scope="scope">
            <image-preview :src="scope.row.img" :width="40" :height="40"/>
          </template>
        </el-table-column>
        <el-table-column label="规格" prop="skuName" width="150"></el-table-column>
        <el-table-column label="Sku编码" prop="skuCode" width="150"></el-table-column>
        <el-table-column label="商品单价" prop="price" width="150">
          <template slot-scope="scope">
            <!--              <el-input type="number" v-model.number="scope.row.price"  placeholder="请输入商品单价" @input="qtyChange(scope.row)"/>-->
            {{amountFormatter(null,null,scope.row.price/100)}}
          </template>
        </el-table-column>
        <el-table-column label="商品数量" prop="quantity" width="150">
          <template slot-scope="scope">
            <el-input v-model.number="scope.row.quantity" placeholder="请输入商品数量" @input="qtyChange(scope.row)"/>
          </template>
        </el-table-column>
        <el-table-column label="金额" prop="itemAmount" width="150">
          <template slot-scope="scope">
            <el-input v-model="scope.row.itemAmount" disabled placeholder="请输入子订单金额" />
          </template>
        </el-table-column>

        <el-table-column label="是否赠品" prop="isGift" width="100">
          <template slot-scope="scope">
            <!-- <el-input v-model="scope.row.isGift" placeholder="请输入是否赠品0否1是" /> -->
            <el-select v-model="scope.row.isGift" placeholder="是否赠品" @input="qtyChange(scope.row)">
              <el-option label="否" value="0" />
              <el-option label="是" value="1" />
            </el-select>
          </template>
        </el-table-column>
      </el-table>

      <el-col  :span="24">
        <el-form-item label="商品金额" prop="goodsAmount" style="margin-top: 10px;">
          <el-input v-model="form.goodsAmount" style="width: 220px;" disabled placeholder="商品金额，单位：元" />
        </el-form-item>
        <el-form-item label="运费" prop="postage" style="margin-top: 10px;">
          <el-input v-model="form.postage" style="width:220px" placeholder="邮费，单位：元" @input="handlePostageInput" />
        </el-form-item>
      </el-col>

      <el-col :span="24">
        <el-form-item label="手动优惠金额" prop="changePrice">
          <el-input v-model="form.changePrice" style="width: 220px;" placeholder="手动优惠金额，单位：元" @input="handleChangePriceInput" />
        </el-form-item>
        <el-form-item label="折扣优惠" prop="discountAmount">
          <el-input v-model="form.discountAmount" disabled style="width: 220px;" placeholder="折扣金额，单位：元" />
          <el-button size="mini" style="margin-left: 20px;" @click="selectDiscountDialog">选择优惠折扣</el-button>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="抵扣金额" prop="deductionPrice">
          <el-input v-model="form.deductionPrice" style="width: 220px;" placeholder="抵扣金额，单位：元" @input="handleDeductionPriceInput" />
<!--          <el-button size="mini" style="margin-left: 20px;" @click="addDeduction">添加回收抵扣</el-button>-->
        </el-form-item>
      </el-col>

      <el-col :span="24" style="display: none">
      <el-form-item label="智能识别" prop="addressAll">
        <el-input type="textarea" v-model="form.addressAll" placeholder="" style="width: 800px" /><br/>
        <el-button size="mini" @click="parseAddress">识别</el-button>
      </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="收件人">
          <el-row :gutter="10" class="mb8" >
            <el-col :span="1.5">
              <el-button size="small" @click="selectMemberDialog"> 选择会员</el-button>
            </el-col>
          </el-row>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="姓名" prop="receiverName">
          <el-input v-model="form.receiverName" style="width: 150px;" placeholder="请输入收件人姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="receiverPhone">
          <el-input v-model="form.receiverPhone"  style="width: 155px;" placeholder="请输入收件人手机号" />
        </el-form-item>
        <el-form-item label="省市区" prop="provinces">
          <el-cascader style="width:220px"
                       size="large"
                       :options="pcaTextArr"
                       v-model="form.provinces">
          </el-cascader>
        </el-form-item>
      </el-col>
<!--        <el-col :span="24">-->
<!--        &lt;!&ndash; <el-form-item label="省市区" prop="town">-->
<!--            <el-input v-model="form.province" style="width: 120px;" placeholder="请输入省" />-->
<!--            <el-input v-model="form.city" placeholder="请输入市" style="width: 120px;margin-left: 10px;" />-->
<!--            <el-input v-model="form.town" placeholder="请输入区"  style="width: 120px;margin-left: 10px;"/>-->
<!--        </el-form-item> &ndash;&gt;-->
<!--          <el-form-item label="省市区" prop="provinces">-->
<!--            <el-cascader style="width:220px"-->
<!--                         size="large"-->
<!--                         :options="pcaTextArr"-->
<!--                         v-model="form.provinces">-->
<!--            </el-cascader>-->
<!--          </el-form-item>-->
<!--        </el-col>-->
          <el-col :span="24">
        <el-form-item label="详细地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入详细地址" style="width: 760px;" />
        </el-form-item>
        </el-col>


      <el-col :span="24">
        <el-form-item label="支付方式" prop="payMethod">
          <!--支付方式：XYHF先用后付，WEIXIN微信支持，ALIPAY支付宝，reward奖品订单， points积分兑换-->
          <el-select v-model="form.payMethod" placeholder="支付方式" >
            <el-option label="微信支持" value="WEIXIN"></el-option>
            <el-option label="支付宝" value="ALIPAY"></el-option>
          </el-select>
        </el-form-item>
      </el-col>
<!--      <el-col :span="24">-->
<!--        <el-form-item label="是否锁库存" prop="isLock">-->
<!--          <el-select v-model="form.isLock" placeholder="支付方式" >-->
<!--            <el-option label="不锁库存" value="0"></el-option>-->
<!--            <el-option label="锁库存" value="1"></el-option>-->
<!--          </el-select>-->
<!--        </el-form-item>-->
<!--      </el-col>-->
      <el-col :span="24">
        <el-form-item label="销售员" prop="finderId" >
          <el-select v-model="form.finderId" placeholder="选择销售员" >
            <el-option v-for="item in salePeopleList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
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

    <!-- 添加 对话框 -->
    <el-dialog :title="title" :visible.sync="deductionOpen" width="800px" append-to-body>
      <el-form ref="deductionForm" :model="deductionForm" :rules="deductionRules" label-width="120px">
        <el-form-item label="金重(g)" prop="goldWeight">
          <el-input v-model="deductionForm.goldWeight" placeholder="请输入金重(g)" style="width: 320px" @input="handleGoldWeightInput" />
        </el-form-item>
        <el-form-item label="回收金价" prop="goldPrice">
          <el-input v-model="deductionForm.goldPrice" placeholder="请输入回收金价" style="width: 320px" @input="handleGoldPriceInput" />
        </el-form-item>
        <el-form-item label="银重(g)" prop="silverWeight">
          <el-input v-model="deductionForm.silverWeight" placeholder="请输入银重(g)" style="width: 320px" @input="handleSilverWeightInput"/>
        </el-form-item>
        <el-form-item label="回收银价" prop="silverPrice">
          <el-input v-model="deductionForm.silverPrice" placeholder="请输入回收银价" style="width: 320px" @input="handleSilverPriceInput"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="deductionForm.remark" placeholder="请输入备注" style="width: 320px" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitDeductionForm">确 定</el-button>
        <el-button @click="deductionCancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加品对话框 -->
    <PopupShopGoodsSkuList @data-from-select="handleDataFromPopup" :btn="1" :shopId="form.shopId" ref="popup"></PopupShopGoodsSkuList>
    <!-- 选择会员 -->
    <PopupShopMemberList @data-from-select="handleDataFromPopupMember" ref="popupMember"></PopupShopMemberList>
    <!-- 选择优惠 -->
    <PopupDiscountList @data-from-select="handleDataFromPopupDiscount" ref="popupDiscount"></PopupDiscountList>
  </div>
</template>

<script>

import PopupShopGoodsSkuList from '@/views/shop/goods/PopupShopGoodsSkuList.vue'
import PopupShopMemberList from '@/views/member/PopupShopMemberList.vue'
import PopupDiscountList from '@/views/marketing/discount/PopupDiscountList.vue'
import { listShop } from "@/api/shop/shop";
import {
  provinceAndCityData,
  pcTextArr,
  regionData,
  pcaTextArr,
  codeToText,
} from "element-china-area-data";
import { createShopOrder } from '@/api/shop/order'
import { amountFormatter } from '@/utils/zhijian'
import {listSalesPeople} from "@/api/order/salespeople";
import {addRecovery} from "@/api/afterSale/recovery";
import { limitDecimalLength, stringToNumber } from "@/utils/numberInput";
import tab from "@/plugins/tab";


export default {
  name: "ShopOrderCreate",
  components: {
    PopupShopGoodsSkuList,PopupShopMemberList,PopupDiscountList
  },
  data() {
    return {
      deductionOpen:false,
      shopLoading:true,
      merchantLoading:true,
      shopType:null,
      title:null,
      deductionForm:{
        originalOrderNo:null,
        customerId:null,
        customerName:null,
        customerPhone:null,
        customerAddress:null,
        goldWeight:null,
        goldPrice:null,
        silverWeight:null,
        silverPrice:null,
        settlementType:null,
      },
      // 表单参数
      form: {
        orderNum:null,
        shopId:null,
        shopMemberId:null,
        goodsAmount:0.0,
        postage:0.0,
        sellerDiscount:0.0,
        discountId: 0,
        discountAmount:0.0,
        deductionPrice:0.0,
        changePrice:0.0,
        isRecoveryDeduction:0,
        receiverPhone:'',
        receiverName:'',
        address:'',
        buyerMemo:null,
        payMethod:null,
        isLock:null,
        finderId:null,
        itemList:[],
        provinces: []
      },
      // 添加商品弹窗form
      goodsForm:{},
      pcaTextArr,
      // 表单校验
      rules: {
        orderNum: [{ required: true, message: '订单号不能为空', trigger: "blur" }],
        shopId: [{ required: true, message: '请选择店铺', trigger: "blur" }],
        itemList: [{ required: true, message: '请选择商品', trigger: "change" }],
        customerType: [{ required: true, message: '请选择客户类型', trigger: "blur" }],
        customerId: [{ required: true, message: '不能为空', trigger: "blur" }],
        receiverName: [{ required: true, message: '请填写收件人信息', trigger: "blur" }],
        receiverPhone: [{ required: true, message: '请填写收件人信息' , trigger: "blur"}],
        provinces: [{ required: true, message: '请填写收货信息', trigger: "blur" }],
        city: [{ required: true, message: '请填写收货信息', trigger: "blur" }],
        town: [{ required: true, message: '请填写收货信息' , trigger: "blur"}],
        address: [{ required: true, message: '请填写收货信息', trigger: "blur" }],
        goodsAmount: [{ required: true, message: '请填写商品价格' , trigger: "blur"}],
        postage: [{ required: true, message: '请填写运费' , trigger: "blur"}],
        changePrice: [{ required: true, message: '请填写手动优惠金额' , trigger: "blur"}],
        discountAmount: [{ required: true, message: '请填写折扣金额' , trigger: "blur"}],
        deductionPrice: [{ required: true, message: '请填写抵扣金额', trigger: "blur" }],
        // payMethod: [{ required: true, message: '请选择支付方式', trigger: "blur" }],
        // isLock: [{ required: true, message: '是否锁定库存', trigger: "blur" }]
      },
      deductionRules:{
        goldWeight: [{ required: true, message: "订单号不能为空", trigger: "blur" }],
        goldPrice: [{ required: true, message: "不能为空", trigger: "blur" }],
        silverWeight: [{ required: true, message: "不能为空", trigger: "change" }],
        silverPrice: [{ required: true, message: "不能为空", trigger: "change" }],
      },
      shopList: [],
      salePeopleList: [],
      merchantList: [],
      merchantShopList: [],
      skuListLoading: false,
      skuList: [],

      // 子表选中数据
      checkedSShopOrderItem: []
    };
  },
  created() {
    if (this.$route.query.shopType) {
      this.shopType = this.$route.query.shopType
    }

    this.form.orderDate = this.getDate()
    if(this.$route.query.shopId) {
      this.form.shopId = parseInt(this.$route.query.shopId)
    }else{
      listShop({type: this.shopType}).then(response => {
        this.shopList = response.rows;
        if (this.$route.query.shopId) {
          this.form.shopId = parseInt(this.$route.query.shopId)
        }
        this.shopLoading = false
      });
    }

    listSalesPeople({pageIndex:1,pageSize:100}).then(response => {
      this.salePeopleList = response.rows;
    });
    this.genOrderNum()
  },
  methods: {
    amountFormatter,
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
    // 选择会用
    selectMemberDialog(){
      this.$refs.popupMember.openDialog();
    },
    // 添加商品
    addGoodsDialog() {
       if(!this.form.shopId){
         this.$modal.msgError("请选择店铺")
         return
       }
      this.$refs.popup.openDialog();
    },
    // 添加抵扣(回收抵扣)
    addDeduction(){
      this.deductionForm = {
        originalOrderNo:null,
        customerId:null,
        customerName:null,
        customerPhone:null,
        customerAddress:null,
        goldWeight:null,
        goldPrice:null,
        silverWeight:null,
        silverPrice:null,
        settlementType:null,
      };
      this.title = '添加回收抵扣'
      this.deductionOpen = true;
    },
    // 取消按钮
    deductionCancel() {
      this.deductionForm = {
        originalOrderNo:null,
        customerId:null,
        customerName:null,
        customerPhone:null,
        customerAddress:null,
        goldWeight:null,
        goldPrice:null,
        silverWeight:null,
        silverPrice:null,
        settlementType:null,
      };
      this.deductionOpen = false;
      this.form.isRecoveryDeduction = 0;
      this.form.deductionPrice = 0.0;
    },
    // 回收抵扣表单提交
    submitDeductionForm(){
      this.$refs["deductionForm"].validate(valid => {
        if (valid) {

          this.form.deductionRemark = this.deductionForm.remark;
          this.form.isRecoveryDeduction = 1;
          // 将价格字段从字符串转换回数字类型
          this.deductionForm.goldWeight = stringToNumber(this.deductionForm.goldWeight);
          this.deductionForm.goldPrice = stringToNumber(this.deductionForm.goldPrice);
          this.deductionForm.silverWeight = stringToNumber(this.deductionForm.silverWeight);
          this.deductionForm.silverPrice = stringToNumber(this.deductionForm.silverPrice);
          const goldPrice = this.deductionForm.goldWeight * this.deductionForm.goldPrice;
          const silverPrice = this.deductionForm.silverWeight * this.deductionForm.silverPrice;

          this.form.deductionPrice = goldPrice+silverPrice;
          console.log('抵扣金额',this.form.deductionPrice);
          console.log('抵扣后',this.form);
          this.deductionOpen = false;
        }
      })
    },
    // 选择优惠
    selectDiscountDialog(){
      if(!this.form.itemList || this.form.itemList.length===0){
        this.$modal.msgError('请选择商品')
      }else if(this.form.goodsAmount<=0){
        this.$modal.msgError('商品金额为0')
      }else
        this.$refs.popupDiscount.openDialog();
    },
    // 接收优惠折扣
    handleDataFromPopupDiscount(data){
      console.log('========选择的折扣：',data);
      if(data) {
        console.log('=====开始计算折扣')
        // 判断订单金额是否满足
        if(data.minOrderAmount>0){
          if(this.form.goodsAmount<data.minOrderAmount){
            this.$modal.msgError('订单金额不满足折扣要求')
          }
        }
        this.form.discountAmount = 0.0
        this.form.discountId = data.id
        // 计算折扣后的金额
        if(data.discountType==1){
          // 百分比
          this.form.discountAmount = this.form.goodsAmount * data.discountValue /100;
        }else{
          // 固定金额
          this.form.discountAmount = data.discountValue;
        }
      }
    },
    //接收会员信息
    handleDataFromPopupMember(data){
      console.log('========选择的会员：',data);
      if(data){
        console.log('=====接收到会员')
        this.form.shopMemberId=data.id;
        this.form.receiverName=data.name;
        this.form.receiverPhone=data.phone;
        this.form.address=data.address;
        this.form.province=data.province;
        this.form.city=data.city;
        this.form.town=data.county;
        this.form.provinces = []
        this.form.provinces.push(this.form.province);
        this.form.provinces.push(this.form.city);
        this.form.provinces.push(this.form.town);
      }
    },
    // 接收子组件传来的数据
    handleDataFromPopup(data) {
      console.log('Received data from popup:', data);
      if(data){
        data.forEach((item) => {
          const find = this.form.itemList.find(x => x.id === item.id)
          if (!find) {
            this.form.itemList.push(item)
          }else {
            this.$modal.msgError("已经添加过了")
          }
        })
      }
      let goodsAmount = 0;
      this.form.itemList.forEach(item => {
        if(!item.quantity) item.quantity= 1
        item.itemAmount = item.quantity*item.price/100
        goodsAmount+= item.itemAmount
        if(!item.isGift) item.isGift = '0'
      })
      console.log('====添加后=====',this.form.itemList)
      this.form.goodsAmount = goodsAmount
      this.form.orderAmount = goodsAmount
    },
    qtyChange(row) {
      console.log('======值变化=====', row)
      row.itemAmount = parseFloat(row.price || 0) * parseFloat(row.quantity || 0) / 100
      // 计算总金额
      let goodsAmountNew =0.0
      this.form.itemList.forEach(x=>{
        if(x.isGift==0) {
          goodsAmountNew += parseFloat(x.itemAmount || 0)
        }
      })
      this.form.goodsAmount = goodsAmountNew
      this.form.orderAmount = goodsAmountNew
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
      listShop({merchantId:this.form.customerId}).then(response => {
        this.merchantShopList = response.rows;

        this.shopLoading = false
      });
    },
    searchShop(query){
      if (query !== '') {
        this.shopLoading = true;
        listShop({merchantId:this.form.customerId,name:query}).then(response => {
          this.merchantShopList = response.rows;
          this.shopLoading = false
        })
      }
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {

          this.form.province = this.form.provinces[0]
          this.form.city = this.form.provinces[1]
          this.form.town = this.form.provinces[2]
          // 将价格字段从字符串转换回数字类型
          this.form.postage = stringToNumber(this.form.postage);
          this.form.changePrice = stringToNumber(this.form.changePrice);
          this.form.deductionPrice = stringToNumber(this.form.deductionPrice);
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

            console.log('======创建订单=====',this.form)
            createShopOrder(this.form).then(response => {
              if(response.code===200){
                this.$modal.msgSuccess("订单创建成功");
                // 调用全局挂载的方法,关闭当前标签页
                this.$store.dispatch("tagsView/delView", this.$route);
                // 获取选中店铺的merchantId
                const selectedShop = this.shopList.find(shop => shop.id === this.form.shopId);
                const merchantId = selectedShop ? selectedShop.merchantId : '';
                // 构建跳转参数
                let tabParam = '';
                if(this.shopType==999){
                  tabParam = 'OFFLINE';
                } else if(this.shopType==911) {
                  tabParam = 'TANGLANG';
                }
                // 带上shopType、merchantId、shopId参数跳转，用于刷新列表
                this.$router.push({
                  path: '/shop/shop_order_list',
                  query: {
                    tab: tabParam,
                    shopType: this.shopType || '',
                    merchantId: merchantId || '',
                    shopId: this.form.shopId || '',
                    refresh: '1'
                  }
                });
              }else{
                this.$modal.msgError(response.msg)
              }

            });

        }else{
          this.$modal.msgError("请添加商品");
        }

        }
      });
    },
    handlePostageInput() {
      this.form.postage = limitDecimalLength(this.form.postage);
    },
    handleChangePriceInput() {
      this.form.changePrice = limitDecimalLength(this.form.changePrice);
    },
    handleDeductionPriceInput() {
      this.form.deductionPrice = limitDecimalLength(this.form.deductionPrice);
    },
    handleGoldWeightInput() {
      this.deductionForm.goldWeight = limitDecimalLength(this.deductionForm.goldWeight);
    },
    handleGoldPriceInput() {
      this.deductionForm.goldPrice = limitDecimalLength(this.deductionForm.goldPrice);
    },
    handleSilverWeightInput() {
      this.deductionForm.silverWeight = limitDecimalLength(this.deductionForm.silverWeight);
    },
    handleSilverPriceInput() {
      this.deductionForm.silverPrice = limitDecimalLength(this.deductionForm.silverPrice);
    }
  }
};

</script>
