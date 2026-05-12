<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">

      <el-form-item label="国家/地区" prop="regionId">
        <el-select v-model="queryParams.regionId" placeholder="请选择国家/地区" clearable>
          <el-option
            v-for="item in regionList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="平台" prop="platformId">
        <el-select v-model="queryParams.platformId" placeholder="请选择平台" clearable>
          <el-option
            v-for="item in platformList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="店铺" prop="shopId">
        <el-select v-model="queryParams.shopId" placeholder="请选择店铺" clearable @change="handleQuery">
          <el-option
            v-for="item in shopList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
            <span style="float: left">{{ item.name }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px"  >
              {{ platformList.find(x=>x.id === item.type) ? platformList.find(x=>x.id === item.type).name : '' }}
            </span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="店铺小组" prop="shopGroupId">
        <el-select v-model="queryParams.shopGroupId" placeholder="请选择店铺小组" clearable>
          <el-option
            v-for="item in groupList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="负责人" prop="manageUserId">
        <el-select v-model="queryParams.manageUserId" placeholder="负责人" clearable>
          <el-option
            v-for="item in userList"
            :key="item.userId"
            :label="item.nickName"
            :value="item.userId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="日期" prop="dates">
        <el-date-picker
                        v-model="queryParams.dates" value-format="yyyy-MM-dd"
                        type="daterange"
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期">
        </el-date-picker>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >手动录入数据</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dailyList" @selection-change="handleSelectionChange"
              show-summary :summary-method="customSummaryMethod" >
<!--      <el-table-column type="selection" width="55" align="center" />-->
<!--      <el-table-column label="ID" align="center" prop="id" />-->
      <!-- 汇总行 -->

      <el-table-column label="日期" align="center" prop="date" />
      <el-table-column label="店铺" align="center" prop="shop" >
        <template slot-scope="scope">
         {{shopList.find(x=>x.id === scope.row.shopId)?shopList.find(x=>x.id === scope.row.shopId).name:''}}
        </template>
      </el-table-column>
      <el-table-column label="平台" align="center" prop="platform" >
        <template slot-scope="scope">
          <el-tag >{{platformList.find(x=>x.id === scope.row.platformId)?platformList.find(x=>x.id === scope.row.platformId).name:''}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="店铺负责人" align="center" prop="manageUserId" >
        <template slot-scope="scope">
          <el-tag >{{
              shopList.find(x=>x.id === scope.row.shopId)?
                (userList.find(x=>x.userId === shopList.find(x=>x.id === scope.row.shopId).manageUserId)?userList.find(x=>x.userId === shopList.find(x=>x.id === scope.row.shopId).manageUserId).nickName:'')
                :''}}</el-tag>
        </template>
      </el-table-column>
       <el-table-column label="订单数" align="center" prop="orderTotal" :sortable="true" />
       <el-table-column label="总金额" align="center" prop="orderAmount" :formatter="amountFormatter" width="180px" :sort-method="sortOrderAmount"  :sortable="true">
         <template slot-scope="scope">
           {{scope.row.orderAmount}}/ {{scope.row.exchangeRate}} =
           {{amountFormatter(null,null,scope.row.orderAmount / scope.row.exchangeRate )}}
         </template>
       </el-table-column>
       <el-table-column label="刷单数" align="center" prop="falseOrderTotal" :sortable="true"/>
      <el-table-column label="刷单金额" align="center" prop="falseOrderAmount" :formatter="amountFormatter" :sort-method="sortFalseOrderAmount" :sortable="true">
        <template slot-scope="scope">
          {{scope.row.falseOrderAmount}}/ {{scope.row.exchangeRate}} =
          {{amountFormatter(null,null,scope.row.falseOrderAmount / scope.row.exchangeRate )}}
        </template>
      </el-table-column>
      <el-table-column label="真实订单" align="center" prop="trueOrderTotal" :sortable="true"/>
      <el-table-column label="真实销售额" align="center" prop="trueOrderAmount" :formatter="amountFormatter" :sort-method="sortTrueOrderAmount" :sortable="true">
        <template slot-scope="scope">
                    {{scope.row.trueOrderAmount}}/ {{scope.row.exchangeRate}} =
          {{amountFormatter(null,null,scope.row.trueOrderAmount / scope.row.exchangeRate )}}
        </template>
      </el-table-column>
      <el-table-column label="广告支出" align="center" prop="adFee" :sort-method="sortAdFee" :sortable="true">
        <template slot-scope="scope">
                    {{scope.row.adFee}}/ {{scope.row.exchangeRate}} =
          {{amountFormatter(null,null,scope.row.adFee / scope.row.exchangeRate )}}
        </template>
      </el-table-column>
      <el-table-column label="广告点击次数" align="center" prop="adClick" :sortable="true"/>
      <el-table-column label="广告点击成本" align="center" :sort-method="sortAdClickFee" :sortable="true" >
        <template slot-scope="scope">
        {{amountFormatter(null,null,(scope.row.adClickFee / scope.row.exchangeRate) )}}
        </template>
      </el-table-column>
      <el-table-column label="ROI" align="center" prop="adRoi" :sortable="true"/>
      <el-table-column label="平均客单价" align="center" prop="unitPrice" :sortable="true">
      </el-table-column>
      <el-table-column label="提款" align="center" prop="withdrawalAmount" :sort-method="sortWithdrawalAmount" :sortable="true">
        <template slot-scope="scope">
          <!--          {{scope.row.withdrawalAmount}}/ {{scope.row.exchangeRate}} =-->
          {{amountFormatter(null,null,scope.row.withdrawalAmount / scope.row.exchangeRate )}}
        </template>
      </el-table-column>
      <el-table-column label="汇率" align="left" prop="exchangeRate" />
      <el-table-column label="备注" align="center" >
        <template slot-scope="scope">
<!--          {{scope.row.remark}}-->
          <el-button v-if="scope.row.remark"
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleRemark(scope.row)"
          >有备注</el-button>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleDetail(scope.row)"
          >详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

<!--    <pagination-->
<!--      v-show="total>0"-->
<!--      :total="total"-->
<!--      :page.sync="queryParams.pageNum"-->
<!--      :limit.sync="queryParams.pageSize"-->
<!--      @pagination="getList"-->
<!--    />-->

    <!-- 详情对话框 -->
    <el-dialog title="日报详情" :visible.sync="detailOpen" width="1300px" append-to-body>

          <el-table :data="form.itemList"  style="margin-bottom: 10px;">
            <!-- <el-table-column type="selection" width="50" align="center" /> -->
            <el-table-column label="序号" align="center" type="index" width="50"/>
            <el-table-column label="商品标题" prop="goodsName" ></el-table-column>
            <el-table-column label="SKU" prop="skuName" width="150"></el-table-column>
            <el-table-column label="sku编码" prop="skuCode"></el-table-column>
            <el-table-column label="总订单数" align="center" prop="orderTotal" />
            <el-table-column label="总金额" align="center" prop="orderAmount" :formatter="amountFormatter" />
            <el-table-column label="刷单数" align="center" prop="falseOrderTotal" />
            <el-table-column label="刷单金额" align="center" prop="falseOrderAmount" :formatter="amountFormatter" />
<!--            <el-table-column label="真实订单数" align="center" prop="trueOrderTotal" />-->
<!--            <el-table-column label="真实销售金额" align="center" prop="trueOrderAmount" :formatter="amountFormatter" />-->
            <el-table-column label="广告支出" align="center" prop="adFee" />
            <el-table-column label="广告点击次数" align="center" prop="adClick" />
            <el-table-column label="广告点击成本" align="center" prop="adClickFee" />
            <el-table-column label="ROI" align="center" prop="adRoi" >
              <template slot-scope="scope">
                {{scope.row.adFee>0?(scope.row.orderAmount / scope.row.adFee).toFixed(2):0.00}}
              </template>
            </el-table-column>
<!--            <el-table-column label="平均客单价" align="center" prop="unitPrice" />-->
          </el-table>

    </el-dialog>
    <el-dialog title="备注" :visible.sync="remarkOpen" width="600px" append-to-body>
      {{this.form.remark}}
    </el-dialog>
    <el-dialog title="日报修改" :visible.sync="editOpen" width="1300px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="108px" inline class="demo-form-inline">
        <el-form-item label="日期" prop="date">
          <el-date-picker disabled v-model="form.date" value-format="yyyy-MM-dd" type="date">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="店铺" prop="shopId">
          <el-select v-model="form.shopId" filterable r placeholder="搜索店铺" disabled>
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
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      <el-table :data="form.itemList"  style="margin-bottom: 10px;">
        <!-- <el-table-column type="selection" width="50" align="center" /> -->
        <el-table-column label="序号" align="center" type="index" width="50"/>
        <el-table-column label="商品标题" prop="goodsName" width="222"></el-table-column>
        <el-table-column label="SKU" prop="skuName" width="110"></el-table-column>
        <el-table-column label="sku编码" prop="skuCode"></el-table-column>

        <el-table-column label="订单数量" prop="orderTotal" width="100">
          <template slot-scope="scope">
            <el-input v-model.number="scope.row.orderTotal" placeholder="订单数量" />
          </template>
        </el-table-column>
        <el-table-column label="订单金额" prop="orderAmount" width="120">
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
        <el-table-column label="广告费用" prop="adFee" width="100">
          <template slot-scope="scope">
            <el-input v-model="scope.row.adFee" placeholder="广告费用" />
          </template>
        </el-table-column>
        <el-table-column label="广告点击" prop="adClick" width="100">
          <template slot-scope="scope">
            <el-input v-model="scope.row.adClick" placeholder="广告点击" />
          </template>
        </el-table-column>
      </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer" style="margin-left: 108px;">
        <el-button type="primary" @click="submitForm">保存数据</el-button>
        <!-- <el-button @click="cancel">取 消</el-button> -->
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listShop,listPlatform} from "@/api/shop/shop";
import {listDaily,getDaily,updateDaily,delDaily} from "@/api/shop/daily";
import { listUser} from "@/api/system/user";
import {listRegion} from "@/api/shop/region";
import {listGroup} from "@/api/shop/group";
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
export default {
  name: "ShopDaily",
  components: {Treeselect},
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 店铺表格数据
      dailyList: [],
      regionList: [],
      platformList: [],
      userList: [],
      groupList: [],
      shopList: [],
      categoryTree: [],
      // 查询参数
      queryParams: {
        dates:[]
      },
      detailOpen:false,
      editOpen:false,
      remarkOpen:false,
      // 表单参数
      form: {
        itemList:[]
      },
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
    };
  },
  created() {
    this.queryParams.dates[0] = this.getYesterday()
    this.queryParams.dates[1] = this.getYesterday()
    listRegion().then(response => {
      this.regionList = response.rows;
      listGroup({}).then(response => {
        // this.deptList = this.handleTree(response.data, "deptId");
        this.groupList = response.rows;
        // this.categoryTree = this.buildTree(response.data, 0)
        // console.log('======tree======',this.categoryTree)
        listUser().then(response => {
          this.userList = response.rows.filter(item => item.userId >= 100);
          ;
        });
      })
    });
    listPlatform().then(res => {
      this.platformList = res.rows;
      listShop({verifyUser:1}).then(response => {
          this.shopList = response.rows
          this.getList();
        });
    })

  },
  methods: {
    sortWithdrawalAmount(a,b){
      return (a.withdrawalAmount / a.exchangeRate) - (b.withdrawalAmount / b.exchangeRate)
    },sortTrueOrderAmount(a,b){
      return (a.trueOrderAmount / a.exchangeRate) - (b.trueOrderAmount / b.exchangeRate)
    },sortFalseOrderAmount(a,b){
      return (a.falseOrderAmount / a.exchangeRate) - (b.falseOrderAmount / b.exchangeRate)
    },
    sortOrderAmount(a,b){
      return (a.orderAmount / a.exchangeRate) - (b.orderAmount / b.exchangeRate)
    },
    sortAdFee(a,b){
     return (a.adFee / a.exchangeRate) - (b.adFee / b.exchangeRate)
    },sortAdClickFee(a,b){
     return (a.adClickFee / a.exchangeRate) - (b.adClickFee / b.exchangeRate)
    },
      customSummaryMethod({columns, data}) {
        const totals = [];
        const totalAmount = data.reduce((sum, item) => sum + (item.orderAmount/item.exchangeRate), 0);
        const falseOrderAmount = data.reduce((sum, item) => sum + (item.falseOrderAmount/item.exchangeRate), 0);
        const trueOrderAmount = data.reduce((sum, item) => sum + (item.trueOrderAmount/item.exchangeRate), 0);
        const withdrawalAmount = data.reduce((sum, item) => sum + (item.withdrawalAmount/item.exchangeRate), 0);
        const orderTotal = data.reduce((sum, item) => sum + item.orderTotal, 0);
        const adFee = data.reduce((sum, item) => sum + (item.adFee/item.exchangeRate), 0);
        const adClick = data.reduce((sum, item) => sum + item.adClick, 0);
        // 遍历每一列
        columns.forEach((column, index) => {
          if (index === 0) {
            totals[index] = '汇总'; // 第一列显示为 "Total"
          } else if (index === 4 ||  index === 6|| index === 8|| index === 11) {
            // 对数量和价格列进行求和
            const sum = data.reduce((prev, current) => {
              return prev + current[column.property];
            }, 0);
            totals[index] = sum;
          }else if(index === 5){
            totals[index] = totalAmount.toFixed(2);
          }else if(index === 7){
            totals[index] = falseOrderAmount.toFixed(2);
          }else if(index === 9){
            totals[index] = trueOrderAmount.toFixed(2);
          }else if(index === 10){
            totals[index] = adFee.toFixed(2);
          }else if(index === 13){
            //ROI
            totals[index] = this.amountFormatter(null,null,totalAmount / adFee)
          }
          else if(index === 14){
            //客单价
            totals[index] = this.amountFormatter(null,null,totalAmount / orderTotal)
          }else if(index === 12) {
            totals[index] = this.amountFormatter(null, null, adFee / adClick)
          }
          else if(index === 15){
            //提款
            totals[15] = this.amountFormatter(null,null,withdrawalAmount )
          }

        });
        return totals;
      },
    getYesterday() {
      const yesterday = new Date();
      yesterday.setDate(yesterday.getDate() - 1);
      return this.formatDate(yesterday);
    },
    formatDate(date) {
      const year = date.getFullYear();
      const month = (date.getMonth() + 1).toString().padStart(2, '0');
      const day = date.getDate().toString().padStart(2, '0');
      return `${year}-${month}-${day}`;
    },
    amountFormatter(row, column, cellValue, index) {
      return parseFloat(cellValue).toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      listDaily(this.queryParams).then(response => {
        this.dailyList = response.rows;
        this.loading = false;
      });
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
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
    /** 新增按钮操作 */
    handleAdd() {
      this.$router.push('/report/dailyCreate');
    },
    handleDetail(row){
      getDaily(row.id).then(response => {
        this.form.itemList = response.rows;
        this.detailOpen = true;
      });
    },
    handleRemark(row){
      this.remarkOpen = true
      this.form.remark = row.remark
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      // const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除商品编号为"' + row.id + '"的数据项？').then(function() {
        return delDaily(row.id);
      }).then((resp) => {
        console.log("=======返回====",resp)
        // this.getList();
        this.$modal.msgSuccess("删除成功",resp);
      }).catch(() => {});
    },
    handleUpdate(row){
      getDaily(row.id).then(response => {
        this.form = row
        this.form.itemList = response.rows;
        this.editOpen = true;
      });
    },
    submitForm(){
      console.log('=======提交数据======', this.form)
      this.$refs["form"].validate(valid => {
        if (valid) {

          updateDaily(this.form).then(response => {
            this.$modal.msgSuccess("保存成功");
            this.getList()
          });
        }
      })
    }
  }
};
</script>
