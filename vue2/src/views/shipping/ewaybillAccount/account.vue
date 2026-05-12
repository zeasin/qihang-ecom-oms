<template>
  <div class="app-container">
    <el-row>
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
        <el-form-item label="快递公司" prop="outerLogisticsId">
          <el-select v-model="queryParams.outerLogisticsId" placeholder="请选择快递公司" @change="handleQuery" clearable>
          <el-option
            v-for="item in logisticsList"
            :key="item.id"
            :label="item.name"
            :value="item.logisticsId">
          </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="商户" prop="merchantId" v-if="!isMerchant">
          <el-select v-model="queryParams.merchantId"  placeholder="请选择商户"  @change="merchantChange">
            <el-option
              v-for="item in merchantList"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="店铺" prop="shopId">
          <el-select v-model="queryParams.shopId" placeholder="请选择店铺" @change="handleQuery">
            <el-option
              v-for="item in shopList"
              :key="item.id"
              :label="item.name"
              :value="item.id">
              <span style="float: left">{{ item.name }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px"  >
             <el-tag>{{ typeList.find(x=>x.id === item.type) ? typeList.find(x=>x.id === item.type).name : '未知平台' }}</el-tag>
           </span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>

      </el-form>

    </el-row>


    <el-row :gutter="10" class="mb8">

      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-time"
          size="mini"
          @click="updateWaybillAccount"
        >更新电子面单账户信息</el-button>
      </el-col>

<!--      <el-col :span="1.5">-->

<!--        <el-button-->
<!--          type="success"-->
<!--          plain-->
<!--          icon="el-icon-printer"-->
<!--          size="mini"-->
<!--          @click="handleAdd"-->
<!--        >手动添加</el-button>-->
<!--      </el-col>-->

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="deliverList" @selection-change="handleSelectionChange">
<!--       <el-table-column type="selection" width="55" align="center" />-->
<!--      <el-table-column label="ID" align="center" prop="id" />-->
<!--      <el-table-column label="分配的供应商" align="left" width="300">-->
<!--        <template slot-scope="scope">-->
<!--          {{ scope.row.supplierIds ? supplierList.filter(item =>-->
<!--          scope.row.supplierIds.replace(/^,|,$/g, '').split(',').map(Number).includes(item.id)-->
<!--        ).map(item => item.name).join(', ') : '' }}-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="店铺" align="left" prop="shopId" width="150px">
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.shopType===0">销售订单</el-tag>
          <span v-else>
          <el-tag size="small" style="padding-bottom: 10px;margin-bottom: 10px;" type="success" v-if="!isMerchant">{{merchantList.find(x=>x.id == scope.row.merchantId).name}}</el-tag>
            <br/>
            <el-tag size="small">{{shopList.find(x=>x.id == scope.row.shopId)?shopList.find(x=>x.id == scope.row.shopId).name:''}}</el-tag>
            </span>
        </template>
      </el-table-column>
      <el-table-column label="网点" align="left" prop="branchName" width="200px">
      </el-table-column>
      <el-table-column label="网点编号" align="center" prop="branchCode" width="100px" />
      <el-table-column label="快递公司编码" align="center" prop="providerCode" width="100px" />
      <el-table-column label="快递公司" align="center" prop="providerName" width="100px" />
<!--      <el-table-column label="客户编码" align="center" prop="sellerId" width="100px" />-->
<!--      <el-table-column label="店铺" align="left" prop="netsiteName" width="200px">-->
<!--        <template slot-scope="scope">-->
<!--          {{ shopList.find(x=>x.id === scope.row.shopId) ? shopList.find(x=>x.id === scope.row.shopId).name : '' }}-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="已取单号" align="center" prop="allocatedQuantity" width="100px" />-->
<!--      <el-table-column label="已取消单号" align="center" prop="cancelledQuantity" width="100px" />-->
<!--      <el-table-column label="已回收单号" align="center" prop="recycledQuantity" width="100px" />-->
      <el-table-column label="余额" align="center" prop="amount" width="100px" />
      <el-table-column label="发货地址" align="left" prop="provinceName" >
        <template slot-scope="scope">
          {{scope.row.province}} {{scope.row.city}} {{scope.row.district}} {{scope.row.town}}<br/>
          {{scope.row.detail}}
        </template>
      </el-table-column>
      <el-table-column label="发货人" align="left" prop="deliverName" width="100">
        <template slot-scope="scope">
          {{scope.row.deliverName}}
        </template>
      </el-table-column>

      <el-table-column label="发货人电话" align="center" prop="deliverMobile">
        <template slot-scope="scope">
          {{scope.row.deliverMobile}}{{scope.row.deliverPhone}}
        </template>
      </el-table-column>
<!--      <el-table-column label="市" align="center" prop="cityName"/>-->
<!--      <el-table-column label="区" align="center" prop="districtName"/>-->
<!--      <el-table-column label="发货人" align="center" prop="name"/>-->
<!--      <el-table-column label="发货手机号" align="center" prop="mobile"/>-->


      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
            >发货人&快递模版设置</el-button><br/>
          <el-button
            size="mini" style="padding-left: 6px;padding-right: 6px"
            icon="el-icon-share"
            @click="handleShareSupplier(scope.row)"
          >共享给发货人</el-button>
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

    <!-- 手动添加 -->
    <el-dialog title="发货人&快递模版设置" :visible.sync="updateOpen" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="网点名" prop="branchName">
          <el-input v-model="form.branchName" placeholder="请输入网点名" />
        </el-form-item>
        <el-form-item label="网点编码" prop="branchCode">
          <el-input v-model="form.branchCode" disabled placeholder="请输入网编码" />
        </el-form-item>
<!--        <el-form-item label="卖家Id" prop="sellerShopId">-->
<!--          <el-input v-model.number="form.sellerShopId" placeholder="请输入卖家Id名" />-->
<!--        </el-form-item>-->
        <el-form-item label="快递模块" prop="templateId">
          <el-select v-model="form.templateId" placeholder="请选择快递模版" clearable @change="templateUrlChange">
            <el-option
              v-for="item in templateList"
              :key="item.id"
              :label="(item.logisticsCode+' '+item.desc1)"
              :value="item.templateId">
            </el-option>
          </el-select>
          <el-button
            size="mini"
            type="text"
            @click="handlePullTemplate(form.deliveryId)"
          >更新快递模版</el-button>
        </el-form-item>
        <el-form-item label="发货人" prop="deliverName" >
          <el-input v-model="form.deliverName" placeholder="请输入发货人" />
        </el-form-item>
        <el-form-item label="发货手机号" prop="deliverMobile" >
          <el-input v-model="form.deliverMobile" placeholder="请输入发货手机号" />
        </el-form-item>
        <el-form-item label="发货电话" prop="deliverPhone" >
          <el-input v-model="form.deliverPhone" placeholder="请输入发货电话" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="updateSubmit">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="共享电子面单给发货人" :visible.sync="shareOpen" width="600px" append-to-body>
      <el-table :data="shareList"  style="margin-bottom: 10px;">
        <el-table-column label="序号" align="center" type="index" width="50"/>

        <el-table-column label="网点" prop="siteName" ></el-table-column>

        <el-table-column label="发货人类型" prop="num">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.shipperType==10">系统云仓</el-tag>
            <el-tag v-if="scope.row.shipperType==30">供应商</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="发货人" prop="shipperName"></el-table-column>
      </el-table>
      <el-form ref="form" :model="form2" :rules="rules2" label-width="120px">
        <el-form-item label="发货人类型" prop="shipperType">
          <el-select v-model="form2.shipperType" placeholder="请选择供应商" @change="shipperTypeChange">
            <el-option label="系统云仓" value="10"></el-option>
            <el-option label="供应商" value="30"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="发货人" prop="shipperId">
          <el-select v-model="form2.shipperId" placeholder="请选择发货人" clearable >
            <el-option
              v-for="item in shipperList"
              :key="item.id"
              :label="item.name || item.warehouseName"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
<!--        <el-form-item label="供应商" prop="supplierIds">-->
<!--          <el-select v-model="form.supplierIds" placeholder="请选择供应商" clearable multiple >-->
<!--            <el-option-->
<!--              v-for="item in supplierList"-->
<!--              :key="item.id"-->
<!--              :label="item.name"-->
<!--              :value="item.id">-->
<!--            </el-option>-->
<!--          </el-select>-->
<!--        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="shareSupplierSubmit">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import {listAllSupplier, listShipSupplier} from "@/api/goods/supplier";
import {listSupplier} from "@/api/goods/supplier";
import {listPlatform, listShop} from "@/api/shop/shop";
import { listMerchant } from '@/api/shop/merchant'
import {
  getWaybillAccountList,
  getWaybillTemplateList,
  getShareShipperList,
  shareShipper,
  updateAccount
} from "@/api/shop/ewaybill";


import {
  pullWaybillAccount,
  pullLogisticsTemplateListJd
} from "@/api/jd/ewaybill";
import {listLogistics, listLogisticsStatus} from "@/api/shipping/logistics_library";
import {getShopListData} from "@/utils/shopUtils";
import {pullWaybillAccountTao} from "@/api/tao/ewaybill";
import {pullWaybillAccountWei} from "@/api/wei/ewaybill";
import {listWarehouse} from "@/api/wms/warehouse";
import {pullWaybillAccountDou} from "@/api/dou/ewaybill";
import {pullWaybillAccountPdd} from "@/api/pdd/ewaybill";
import {pullLogisticsTemplateListXhs, pullWaybillAccountXhs} from "@/api/xhs/ewaybill";
import {pullLogisticsTemplateListPdd} from "@/api/pdd/shopApi";
import {pullLogisticsTemplateListDou} from "@/api/dou/shop_api";
import {pullLogisticsTemplateListWei} from "@/api/wei/shop_api";


export default {
  name: "waybillAccountList",
  props: {
    shopType: {
      type: Number,  // 根据你的数据类型选择合适的类型
      required: true,
    },
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      isMerchant: false,
      // 选中数组
      ids: [],
      shopList: [],
      typeList:[],
      logisticsList: [],
      deliverList: [],
      supplierList: [],
      shopListAll:[],
      merchantList: [],
      templateList: [],
      shareList: [],
      shipperList: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 弹出层标题
      title: "",
      open:false,
      shareOpen:false,
      updateOpen:false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        shopType:null,
        shopId: null,
        outerLogisticsId: null,
      },
      // 打印参数
      printParams: {
        deliver: null,
        printer: null
      },
      // 表单参数
      form: {
        id: null,
        sellerShopId:null,
        branchName:null,
        branchCode:null,
        templateUrl:null,
        company:null,
        name:null,
        mobile:null,
        shipperType: '10',
        shipperId: null,
        templateId: null,
        supplierIds: null
      },
      form2: {
        id: null,
        shipperType: '10',
        shipperId: null,
      },
      // 表单校验
      rules: {
        branchName: [{ required: true, message: '不能为空', trigger: "blur" }],
        // branchCode: [{ required: true, message: '不能为空', trigger: "blur" }],
        templateId: [{ required: true, message: '不能为空', trigger: "blur" }],
        deliverName: [{ required: true, message: '不能为空', trigger: "blur" }],
        // deliverMobile: [{ required: true, message: '不能为空', trigger: "blur" }],
        // deliverPhone: [{ required: true, message: '不能为空', trigger: "blur" }],
      },
      rules2: {
        shipperType: [{ required: true, message: '不能为空', trigger: "blur" }],
        shipperId: [{ required: true, message: '不能为空', trigger: "blur" }],
      }
    };
  },
  created() {
    this.queryParams.shopType = this.shopType
    listPlatform({ status: 0 }).then(res => {
      this.typeList = res.rows;
    })
    listLogisticsStatus({shopType:this.shopType,status:1}).then(response => {
      this.logisticsList = response.rows;
    });

    listMerchant({ pageNum: 1, pageSize: 1000 }).then(resp => {
      this.merchantList = resp.rows
      if (this.merchantList.length > 0) {
        this.queryParams.merchantId = this.merchantList[0].id
      }
      if(resp.rows.length === 1&&resp.rows[0].id>0) {
        this.isMerchant = true;
      }
      listShop({ merchantId: this.queryParams.merchantId,type:this.shopType}).then(response => {
        this.shopList = response.rows;
        this.shopLoading = false
        if (this.shopList && this.shopList.length > 0) {
          this.queryParams.shopId = this.shopList[0].id
        }
        this.getList()
      });

    })
    // listShop({type: this.queryParams.platformId}).then(response => {
    //   this.shopList = response.rows;
    //   if (this.shopList && this.shopList.length > 0) {
    //     this.queryParams.shopId = this.shopList[0].id
    //   }
    //   this.getList();
    // });
    listSupplier({pageNum: 1, pageSize: 100}).then(resp=>{
      this.supplierList = resp.rows
    })
  },
  async mounted() {
    this.shopListAll = await getShopListData(); // 获取并设置 shopList
  },
  methods: {
    merchantChange(nv){
      console.log('==============',nv);
      this.shopList = []
      this.queryParams.shopId = null
      listShop({merchantId:nv,type:this.shopType}).then(response => {
        this.shopList = response.rows;
        this.shopLoading = false
        if (this.shopList && this.shopList.length > 0) {
          this.queryParams.shopId = this.shopList[0].id
        }
        this.handleQuery()
      });

    },
    /** 查询商品管理列表 */
    getList() {
      this.loading = true;
      getWaybillAccountList(this.queryParams).then(response => {
        this.deliverList = response.rows;
        this.total = response.total
        this.loading = false;
      });

    },
    // 取消按钮
    cancel() {
      this.open = false
      this.updateOpen = false
      this.shareOpen = false
      // this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        erpSkuId: null
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
      this.ids = selection.map(item => item.orderNum)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    // 更新电子面单信息
    updateWaybillAccount() {
      if(this.shopType===200){
        console.log("=========jd=======")
        pullWaybillAccount({shopId: this.queryParams.shopId,logisticsId:this.queryParams.outerLogisticsId}).then(response => {
          if(response.code === 200){
            this.$modal.msgSuccess("拉取成功")
            this.getList()
          }else{
            this.$modal.msgError(response.msg)
          }
        });
      }else if(this.shopType===100) {
        console.log("=========tao=======")
        pullWaybillAccountTao({shopId: this.queryParams.shopId}).then(response => {
          if(response.code === 200){
            this.$modal.msgSuccess("拉取成功")
            this.getList()
          }else{
            this.$modal.msgError(response.msg)
          }

        });
      }else if(this.shopType ===300){
        pullWaybillAccountPdd({shopId: this.queryParams.shopId}).then(response => {
          if(response.code===200){
            this.$modal.msgSuccess("更新成功")
            this.getList()
          }else{
            this.$modal.msgError(response.msg)
          }

        });
      }else if(this.shopType ===400){
        pullWaybillAccountDou({shopId: this.queryParams.shopId}).then(response => {
          if(response.code===200){
            this.$modal.msgSuccess("更新成功")
            this.getList()
          }else{
            this.$modal.msgError(response.msg)
          }
        });
      }else if(this.shopType===500) {
        console.log("=========wei=======")
        pullWaybillAccountWei({shopId: this.queryParams.shopId}).then(response => {
          if(response.code === 200){
            this.$modal.msgSuccess("拉取成功")
            this.getList()
          }else{
            this.$modal.msgError(response.msg)
          }
        });
      }else if(this.shopType===700) {
        console.log("=========xhs=======")
        pullWaybillAccountXhs({shopId: this.queryParams.shopId}).then(response => {
          if(response.code === 200){
            this.$modal.msgSuccess("拉取成功")
            this.getList()
          }else{
            this.$modal.msgError(response.msg)
          }
        });
      }

      else {
        this.$modal.msgError("还不支持")
      }

    },
    templateUrlChange(nv){
      this.form.templateId = nv;
      const temp = this.templateList.filter(x=>x.templateId === nv);
      console.log('===选择的模板',temp,nv)
      if(temp && temp.length>0){
        this.form.templateUrl = temp[0].templateUrl
      }

    },
    handlePullTemplate(){
      if(this.shopType===200) {
        this.pullTemplateLoading = true
        pullLogisticsTemplateListJd({}).then(response => {
          if (response.code === 200) {
            getWaybillTemplateList({shopType: this.shopType, cpCode: this.form.providerCode}).then(resp => {
              this.$modal.msgSuccess("同步成功")
              this.templateList = resp.data
              this.pullTemplateLoading = false
            })
          }

        })
      }else if(this.shopType===300){
        this.pullTemplateLoading = true
        pullLogisticsTemplateListPdd({}).then(response => {
          if(response.code==200){
            getWaybillTemplateList({shopType: this.shopType,cpCode:this.form.providerCode}).then(resp=>{
              this.templateList = resp.data
              this.pullTemplateLoading = false
            })
          }
        })
      }else if(this.shopType === 400){
        this.pullTemplateLoading =true
        pullLogisticsTemplateListDou({}).then(response => {
          if(response.code==200){
            this.$modal.msgSuccess("同步成功")
            getWaybillTemplateList({shopType: this.shopType,cpCode:this.form.providerCode}).then(resp=>{
              this.templateList = resp.data
              this.pullTemplateLoading = false
            })
          }

        })
      }else if(this.shopType === 500){
        this.pullTemplateLoading =true
        pullLogisticsTemplateListWei({}).then(response => {
          if(response.code==200) {
            this.$modal.msgSuccess("同步成功")
            getWaybillTemplateList({shopType: this.shopType,cpCode:this.form.providerCode}).then(resp=>{
              this.templateList = resp.data
              this.pullTemplateLoading = false
            })
          }
        })
      }else if(this.shopType === 700){
        this.pullTemplateLoading =true
        pullLogisticsTemplateListXhs({}).then(response => {
          if(response.code==200) {
            this.$modal.msgSuccess("同步成功")
            getWaybillTemplateList({shopType: this.shopType,cpCode:this.form.providerCode}).then(resp=>{
              this.templateList = resp.data
              this.pullTemplateLoading = false
            })
          }
        })
      }
      else {
        this.$modal.msgError("还不支持")
      }
    },
    handleUpdate(row){
      this.form = row
      getWaybillTemplateList({shopType:this.shopType,cpCode:row.providerCode}).then(resp=>{
        this.templateList = resp.data
        this.form.templateUrl = row.templateUrl
        this.updateOpen = true
      })
    },
    updateSubmit(){
      this.$refs["form"].validate(valid => {
        if (valid) {
          updateAccount(this.form).then(resp=>{
            this.$modal.msgSuccess("保存成功")
            this.updateOpen =false
            this.getList()
          })
        }
      })

    },
    shipperTypeChange(nv){
      console.log('====发货人类型：',nv)
      this.form2.shipperId = null
      this.shipperList = []
      if(nv==10){
        listWarehouse({warehouseType:'CLOUD'}).then(response => {
          this.shipperList = response.rows;
        });
      }else if(nv==30){
        listAllSupplier({isShip:1}).then(response => {
          this.shipperList = response.rows;
        });
      }

    },
    handleShareSupplier(row) {
      this.form2.id = row.id
      this.form2.shipperId = null
      // console.log('=====11111===',row.supplierIds)
      // if (row.supplierIds) {
      //   console.log('=======22222222222=====')
      //   let numArray = row.supplierIds.replace(/^,|,$/g, '').split(',').map(Number);
      //   this.form.supplierIds = numArray
      // } else {
      //   console.log('=========333333333==========')
      //   this.form.supplierIds = null
      // }

      getShareShipperList({id:row.id}).then(resp=>{
        console.log("====共享列表",resp.rows)
        this.shareList = resp.rows
        this.form2.shipperType = '10'
        this.shipperTypeChange('10')
        this.shareOpen = true
        // listWarehouse({warehouseType:'CLOUD'}).then(response => {
        //   this.shipperList = response.rows;
        //   this.shareOpen = true
        // });
      })

    },
    shareSupplierSubmit(){
      console.log("=======共享给发货人=====",this.form2)
      this.$refs["form"].validate(valid => {
        if (valid) {
          shareShipper(this.form2).then(resp => {
            this.$modal.msgSuccess("保存成功")
            this.shareOpen = false
            this.getList()
          })
        }
      })
    }
  }
};
</script>
