<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="仓库" prop="warehouseId">
        <el-select v-model="queryParams.warehouseId"  clearable filterable  placeholder="请选择入库仓库"  @change="handleQuery">
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
      <el-form-item label="入库单号" prop="stockInNum">
        <el-input
          v-model="queryParams.stockInNum"
          placeholder="请输入入库单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="来源单号" prop="sourceNo">
        <el-input
          v-model="queryParams.sourceNo"
          placeholder="请输入来源单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="入库类型" prop="stockInType">
        <el-select v-model="form.stockInType" filterable r placeholder="入库类型" >
          <el-option  label="采购入库" value="1"></el-option>
          <el-option  label="退货入库" value="2"></el-option>
          <el-option  label="盘盈入库" value="3"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="入库时间" prop="stockInTime">
        <el-date-picker clearable
          v-model="queryParams.stockInTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择入库时间">
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
      >新建商品入库单</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['wms:WmsStockInEntry:export']"-->
<!--        >导出</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="WmsStockInEntryList" @selection-change="handleSelectionChange">
<!--      <el-table-column type="selection" width="55" align="center" />-->
      <el-table-column label="主键ID" align="center" prop="id" width="66"/>
      <el-table-column label="单号" align="center" prop="stockInNum" width="160"/>
      <el-table-column label="来源单号" align="center" prop="sourceNo" width="160"/>
      <el-table-column label="仓库" align="left" prop="warehouseId" >
        <template slot-scope="scope">
          {{ warehouseList.find(x=>x.id === scope.row.warehouseId) ? warehouseList.find(x=>x.id === scope.row.warehouseId).warehouseName : '未知' }}
<!--          <el-tag type="info" v-if="scope.row.warehouseType==='LOCAL'">本地仓</el-tag>-->
<!--          <el-tag type="info" v-if="scope.row.warehouseType==='JDYC'">京东云仓</el-tag>-->
<!--          <el-tag type="info" v-if="scope.row.warehouseType==='CLOUD'">系统云仓</el-tag>-->
<!--          <el-tag type="info" v-if="scope.row.warehouseType==='Other'">其他</el-tag>-->

        </template>
      </el-table-column>
      <el-table-column label="仓库类型" align="left" prop="warehouseId" >
        <template slot-scope="scope">
          <el-tag type="info" v-if="scope.row.warehouseType==='LOCAL'">本地仓</el-tag>
          <el-tag type="info" v-if="scope.row.warehouseType==='JDYC'">京东云仓</el-tag>
          <el-tag type="info" v-if="scope.row.warehouseType==='CLOUD'">系统云仓</el-tag>
          <el-tag type="info" v-if="scope.row.warehouseType==='Other'">其他</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="入库类型" align="center" prop="stockInType" >
        <template slot-scope="scope">
        <el-tag size="small" v-if="scope.row.stockInType ===1 ">采购入库</el-tag>
        <el-tag size="small" v-if="scope.row.stockInType ===2 ">退货入库</el-tag>
        <el-tag size="small" v-if="scope.row.stockInType ===3 ">盘盈入库</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="商品数" align="center" prop="sourceGoodsUnit" />
      <el-table-column label="商品规格数" align="center" prop="sourceSpecUnit" />
      <el-table-column label="总件数" align="center" prop="sourceSpecUnitTotal" />
      <el-table-column label="入库业务单号" align="center" prop="entryOrderCode" />
      <el-table-column label="备注" align="center" prop="remark" />
<!--      <el-table-column label="操作入库人id" align="center" prop="stockInOperatorId" />-->
<!--      <el-table-column label="操作入库人" align="center" prop="stockInOperator" />-->
<!--      <el-table-column label="最后入库时间" align="center" prop="stockInTime" width="180">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ parseTime(scope.row.stockInTime) }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
          <div v-if="scope.row.warehouseType ==='LOCAL'">
         <el-tag size="small" v-if="scope.row.status === 0">待入库</el-tag>
         <el-tag size="small" v-if="scope.row.status === 1">部分入库</el-tag>
         <el-tag size="small" v-if="scope.row.status === 2">完全入库</el-tag>
          </div>
          <div v-else>
            <el-tag size="small" v-if="scope.row.status === 0">等待云仓审核</el-tag>
            <el-tag size="small" v-if="scope.row.status === 1">云仓已审核</el-tag>
            <el-tag size="small" v-if="scope.row.status === 2">已推送到云仓</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="stockInTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="(scope.row.status === 0 || scope.row.status === 1) && scope.row.warehouseType ==='LOCAL' "
            size="mini" style="padding-right: 6px;padding-left: 6px"
            type="primary" plain
            icon="el-icon-edit"
            @click="handleStockIn(scope.row,true)"
          >入库</el-button>
<!--          <el-button-->
<!--            v-if="(scope.row.status === 0 || scope.row.status === 1) && scope.row.warehouseType ==='CLOUD' "-->
<!--            size="mini"-->
<!--            type="primary"-->
<!--            icon="el-icon-edit"-->
<!--            @click="handleStockInCloud(scope.row,true)"-->
<!--          >推送到系统云仓</el-button>-->
          <el-button style="padding-right: 10px;padding-left: 10px;" plain
            v-if="scope.row.pushStatus === 0 && (scope.row.warehouseType ==='JDYC' || scope.row.warehouseType ==='JKYYC') "
            size="mini"
            type="primary"
            icon="el-icon-edit"
            @click="handleStockInJDYC(scope.row,true)"
          >推送到三方云仓</el-button>
          <el-button style="padding-right: 10px;padding-left: 10px;" plain
            v-if="scope.row.pushStatus ===1 && (scope.row.warehouseType ==='JDYC' || scope.row.warehouseType ==='JKYYC')"
            size="mini"
            type="success"
            icon="el-icon-check"
            @click="handleManualConfirmStockIn(scope.row)"
          >手动确认入库</el-button>
          <el-button style="padding-right: 10px;padding-left: 10px;" plain
            v-if="scope.row.status ===2 && scope.row.warehouseType ==='LOCAL'"
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleStockIn(scope.row,false)"
          >入库详情</el-button>

<!--          <el-button-->
<!--            v-if="scope.row.status ===1 "-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-delete"-->
<!--            @click="handleStockInComplete(scope.row)"-->
<!--          >入库完成</el-button>-->
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

    <!-- 添加或修改入库单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px" inline>
<!--        <el-form-item label="入库仓库" prop="warehouseId"  v-if="isEdit">-->
<!--          <el-select v-model="form.warehouseId" filterable r placeholder="入库仓库" disabled>-->
<!--            <el-option v-for="item in warehouseList" :key="item.id" :label="item.warehouseName" :value="item.id">-->
<!--            </el-option>-->
<!--          </el-select>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="入库人" prop="stockInOperator" v-if="isEdit">-->
<!--          <el-input v-model="form.stockInOperator" placeholder="请输入操作入库人" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="入库时间" prop="stockInTime">-->
<!--          <el-date-picker clearable-->
<!--                          v-model="form.stockInTime"-->
<!--                          type="datetime"-->
<!--                          value-format="yyyy-MM-dd HH:mm:ss"-->
<!--                          placeholder="请选择入库时间">-->
<!--          </el-date-picker>-->
<!--        </el-form-item>-->

        <el-divider content-position="center">入库明细</el-divider>

        <el-table style="margin-bottom: 10px;" :data="itemList" :row-class-name="rowWmsStockInEntryItemIndex" ref="wmsStockInEntryItem">
<!--          <el-table-column type="selection" width="50" align="center" />-->
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="ID" align="center" prop="id" width="70"/>
          <el-table-column label="图片" width="50">
            <template slot-scope="scope">
              <el-image style="width: 40px; height: 40px" :src="scope.row.goodsImage"></el-image>
            </template>
          </el-table-column>
          <el-table-column label="名称" prop="goodsName" width="200"></el-table-column>
          <el-table-column label="规格"  width="120" prop="skuName"></el-table-column>
          <el-table-column label="sku编码" prop="skuCode" width="120"></el-table-column>
          <el-table-column label="数量" prop="quantity"></el-table-column>
          <el-table-column label="已入库" prop="inQuantity"></el-table-column>
          <el-table-column label="入库数量" prop="intoQuantity" width="110"  v-if="isEdit">
            <template slot-scope="scope">
              <el-input v-model.number="scope.row.intoQuantity" placeholder="入库数量" v-if="scope.row.status === 0 || scope.row.status === 1"/>
            </template>
          </el-table-column>
          <el-table-column label="入库仓位编码" prop="positionNum" width="150"  v-if="!isEdit" />
          <el-table-column label="入库仓位" prop="positionId" width="150"  v-if="isEdit">
            <template slot-scope="scope">
              <el-select v-model="scope.row.positionId" filterable remote reserve-keyword placeholder="搜索仓位编码"  v-if="scope.row.status === 0 || scope.row.status === 1"
                         :remote-method="searchLocation" :loading="locationLoading" @change="locationChanage(scope.row)">
                <el-option v-for="item in locationList" :key="item.id"
                           :label="item.name"
                           :value="item.id">
                  <span style="float: left">{{ item.name }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px">{{ item.number }}</span>
                </el-option>
              </el-select>
<!--              <el-input v-model="scope.row.locationNum" placeholder="请输入入库仓位编码" />-->
            </template>
          </el-table-column>
          <el-table-column label="入库" prop=""  v-if="isEdit">
            <template slot-scope="scope">
              <el-button v-if="scope.row.status !== 2 "  size="mini" plain type="success" @click="submitItemForm(scope.row)">入库</el-button>
            </template>
          </el-table-column>
<!--          <el-table-column label="入库" prop=""  v-if="isEdit">-->
<!--            <template slot-scope="scope">-->
<!--            <el-button v-if="scope.row.status !== 2 " size="mini" plain type="primary" @click="submitItemForm2(scope.row)">入库</el-button>-->
<!--            </template>-->
<!--          </el-table-column>-->
<!--          <el-table-column label="总入库数量" prop="totalQuantity"></el-table-column>-->
        </el-table>

<!--        <el-form-item label="操作入库人id" prop="stockInOperatorId">-->
<!--          <el-input v-model="form.stockInOperatorId" placeholder="请输入操作入库人id" />-->
<!--        </el-form-item>-->

      </el-form>
<!--      <div slot="footer" class="dialog-footer">-->
<!--        <el-button type="primary" @click="submitForm">确 定</el-button>-->
<!--        <el-button @click="cancel">取 消</el-button>-->
<!--      </div>-->
    </el-dialog>

    <el-dialog :title="title" :visible.sync="jdycOpen" width="1200px" append-to-body :close-on-click-modal="false"  @close="handleClose">
      <el-form ref="jdycForm" :model="jdycForm" :rules="rules" label-width="110px" >
        <el-form-item label="入库仓库" prop="warehouseId"  >
          <el-select v-model="jdycForm.warehouseId" filterable r placeholder="入库仓库" disabled style="width: 380px">
            <el-option v-for="item in warehouseList" :key="item.id" :label="item.warehouseName" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="发件人姓名" prop="senderName">
          <el-input v-model="jdycForm.senderName" placeholder="请输入发件人姓名" style="width: 380px" />
        </el-form-item>
        <el-form-item label="发件人手机号" prop="senderMobile">
          <el-input v-model="jdycForm.senderMobile" placeholder="请输入发件人手机号" style="width: 380px" />
        </el-form-item>
        <el-form-item label="发件地址" prop="senderAddress">
          <el-input v-model="jdycForm.senderAddress" placeholder="请输入发件地址" style="width: 580px" />
        </el-form-item>
        <el-divider content-position="center">入库明细</el-divider>

        <el-table style="margin-bottom: 10px;" :data="itemList" :row-class-name="rowWmsStockInEntryItemIndex" ref="wmsStockInEntryItem">
          <!--          <el-table-column type="selection" width="50" align="center" />-->
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="SkuId" prop="skuId" width="66"></el-table-column>
          <el-table-column label="图片" width="60">
            <template slot-scope="scope">
              <el-image style="width: 40px; height: 40px" :src="scope.row.goodsImage"></el-image>
            </template>
          </el-table-column>
          <el-table-column label="商品" prop="goodsName" ></el-table-column>
          <el-table-column label="规格"  width="150" prop="skuName">
          </el-table-column>
          <el-table-column label="sku编码" prop="skuCode"></el-table-column>
          <el-table-column label="数量" prop="quantity"></el-table-column>

        </el-table>

        <!--        <el-form-item label="操作入库人id" prop="stockInOperatorId">-->
        <!--          <el-input v-model="form.stockInOperatorId" placeholder="请输入操作入库人id" />-->
        <!--        </el-form-item>-->

      </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button type="primary" @click="submitForm">推送到三方云仓</el-button>
              <el-button @click="cancel">取 消</el-button>
            </div>
    </el-dialog>


<!--    <el-dialog :title="title" :visible.sync="cloudOpen" width="1200px" append-to-body :close-on-click-modal="false">-->
<!--      <el-form ref="cloudForm" :model="cloudForm" :rules="rules" label-width="110px" >-->
<!--        <el-form-item label="入库仓库" prop="warehouseId"  >-->
<!--          <el-select v-model="cloudForm.warehouseId" filterable r placeholder="入库仓库" disabled style="width: 280px">-->
<!--            <el-option v-for="item in warehouseList" :key="item.id" :label="item.warehouseName" :value="item.id">-->
<!--            </el-option>-->
<!--          </el-select>-->
<!--        </el-form-item>-->

<!--        <el-divider content-position="center">入库明细</el-divider>-->

<!--        <el-table style="margin-bottom: 10px;" :data="itemList" :row-class-name="rowWmsStockInEntryItemIndex" ref="wmsStockInEntryItem">-->
<!--          &lt;!&ndash;          <el-table-column type="selection" width="50" align="center" />&ndash;&gt;-->
<!--          <el-table-column label="序号" align="center" prop="index" width="50"/>-->
<!--          <el-table-column label="商品图片" width="80">-->
<!--            <template slot-scope="scope">-->
<!--              <el-image style="width: 70px; height: 70px" :src="scope.row.goodsImage"></el-image>-->
<!--            </template>-->
<!--          </el-table-column>-->
<!--          <el-table-column label="商品标题" prop="goodsName" ></el-table-column>-->
<!--          <el-table-column label="规格"  width="150" prop="skuName">-->
<!--          </el-table-column>-->
<!--          <el-table-column label="sku编码" prop="skuCode"></el-table-column>-->
<!--          <el-table-column label="数量" prop="quantity"></el-table-column>-->

<!--        </el-table>-->

<!--        &lt;!&ndash;        <el-form-item label="操作入库人id" prop="stockInOperatorId">&ndash;&gt;-->
<!--        &lt;!&ndash;          <el-input v-model="form.stockInOperatorId" placeholder="请输入操作入库人id" />&ndash;&gt;-->
<!--        &lt;!&ndash;        </el-form-item>&ndash;&gt;-->

<!--      </el-form>-->
<!--      <div slot="footer" class="dialog-footer">-->
<!--        <el-button type="primary" @click="submitCloudForm">推送到系统云仓</el-button>-->
<!--        <el-button @click="cancel">取 消</el-button>-->
<!--      </div>-->
<!--    </el-dialog>-->

    <el-dialog :title="title" :visible.sync="stockInOpen" width="800px" append-to-body :close-on-click-modal="false"  @close="handleClose">
      <el-form ref="stockInForm" :model="stockInForm" :rules="stockInRules" label-width="100px" inline>
        <el-col :span="24">
          <image-preview style="width: 70px; height: 70px" :src="stockInForm.goodsImage" />
        </el-col>

        <el-col :span="24">
        <el-form-item label="商品名称" prop="" >
          <el-col>{{stockInForm.goodsName}}</el-col>
        </el-form-item>
          </el-col>
          <el-col :span="12">
        <el-form-item label="规格" prop="" >
          <el-col>{{stockInForm.skuName}}</el-col>
        </el-form-item>
            </el-col>
        <el-col :span="12">
        <el-form-item label="SKU编码" prop="" >

          <el-col>{{stockInForm.skuCode}}</el-col>
        </el-form-item>
            </el-col>
        <el-col :span="12">
        <el-form-item label="总数量" prop="" >
          <el-col>{{stockInForm.quantity}}</el-col>
        </el-form-item>
          </el-col>
        <el-col :span="12">
        <el-form-item label="已入库数量" prop="" >
          <el-col>{{stockInForm.inQuantity}}</el-col>
        </el-form-item>
        </el-col>
        <el-col :span="12">
        <el-form-item label="入库仓库" prop="warehouseId" >
          <el-select v-model="stockInForm.warehouseId" filterable r placeholder="入库仓库" disabled style="width: 220px" >
            <el-option v-for="item in warehouseList" :key="item.id" :label="item.warehouseName" :value="item.id">

            </el-option>
          </el-select>
        </el-form-item>
        </el-col>
        <el-col :span="12">
        <el-form-item label="货架仓位" prop="positionId">
          <el-select v-model="stockInForm.positionId" filterable remote reserve-keyword placeholder="搜索仓位编码" style="width: 220px"
                     :remote-method="searchLocation" :loading="locationLoading" @change="locationChanage(scope.row)">
            <el-option v-for="item in locationList" :key="item.id"
                       :label="item.name"
                       :value="item.id">
              <span style="float: left">{{ item.name }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">{{ item.number }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        </el-col>
        <el-col :span="12">
        <el-form-item label="生产日期" prop="productionDate">
          <el-date-picker clearable
                          v-model="stockInForm.productionDate" value-format="yyyy-MM-dd"
                          type="date" style="width: 220px"
          >
          </el-date-picker>
        </el-form-item>
        </el-col>
        <el-col :span="12">
        <el-form-item label="有效期(天)" prop="period">
          <el-input type="number" v-model.number="stockInForm.period" style="width: 220px" placeholder="有效期(天)" />
        </el-form-item>
        </el-col>
        <el-col :span="24">
        <el-form-item label="入库数量" prop="intoQuantity">
          <el-input type="number" v-model.number="stockInForm.intoQuantity" style="width: 220px" placeholder="入库数量" />
        </el-form-item>
        </el-col>
        <el-col :span="24">
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="form.remark" style="width: 220px" placeholder="请输入备注" />
        </el-form-item>
        </el-col>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitStockInForm">确 定</el-button>
        <el-button @click="cancel1">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 手动确认入库对话框 -->
    <el-dialog :title="'手动确认入库'" :visible.sync="manualConfirmOpen" width="1200px" append-to-body :close-on-click-modal="false">
      <el-form ref="manualConfirmForm" :model="manualConfirmForm" :rules="manualConfirmRules" label-width="100px" inline>
        <el-form-item label="入库单号" prop="stockInNum"  >
          <el-input v-model="manualConfirmForm.stockInNum" disabled placeholder="请输入入库单号" />
        </el-form-item>
        <el-form-item label="仓库" prop="warehouseId"  >
          <el-input v-model="manualConfirmForm.warehouseName" disabled placeholder="请输入仓库" />
        </el-form-item>

        <el-divider content-position="center">商品明细</el-divider>

        <el-table style="margin-bottom: 10px;" :data="manualConfirmForm.itemList" ref="manualConfirmItem">
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="商品图片" width="80">
            <template slot-scope="scope">
              <el-image style="width: 70px; height: 70px" :src="scope.row.goodsImage"></el-image>
            </template>
          </el-table-column>
          <el-table-column label="商品标题" prop="goodsName" ></el-table-column>
          <el-table-column label="规格"  width="150" prop="skuName">
          </el-table-column>
          <el-table-column label="sku编码" prop="skuCode"></el-table-column>
          <el-table-column label="申请数量" prop="quantity" width="100"></el-table-column>
          <el-table-column label="实际入库数量" prop="actualQuantity" width="120">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.actualQuantity" :min="1" :max="scope.row.quantity" size="small" />
            </template>
          </el-table-column>
          <el-table-column label="采购价格" prop="purPrice" width="100">
            <template slot-scope="scope">
              <el-input v-model="scope.row.purPrice" type="number" size="small" placeholder="请输入采购价格" />
            </template>
          </el-table-column>
        </el-table>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitManualConfirm">确 定</el-button>
        <el-button @click="cancelManualConfirm">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listStockIn, getWmsStockInEntry} from "@/api/wms/stockIn";
import { listWarehouse,searchPosition } from "@/api/wms/warehouse";
import { getUserProfile } from '@/api/system/user'
import { pushJdCloudWarehousePurchase } from '@/api/cloud_warehouse/jd'

import {listMyAllWarehouse} from "@/api/store/warehouse";
import {pushCloudWarehousePurchase} from "@/api/cloud_warehouse";



export default {
  name: "WmsStockInEntry",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedWmsStockInEntryItem: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      isEdit: true,
      // 总条数
      total: 0,
      // 入库单表格数据
      WmsStockInEntryList: [],
      // 入库单明细表格数据
      itemList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      stockInOpen: false,
      jdycOpen: false,
      cloudOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        stockInNum: null,
        stockInType: null,
        sourceNo: null,
        sourceId: null,
        stockInTime: null,
      },
      // 表单参数
      form: {
        stockInId:null,
        stockInItemId:null,
        warehouseId:null,
        positionId:null,
        intoQuantity:null,
      },
      stockInItem:{},
      stockInForm:{
        stockInItemId:null,
        stockInId:null,
        warehouseId:null,
        positionId:null,
        goodsName:null,
        goodsImage:null,
        skuName:null,
        skuCode:null,
        quantity:null,
        inQuantity:null,
        intoQuantity:null,
        productionDate:null,

      },
      cloudForm: {
        id: null,
      },
      jdycForm:{
        id:null,
        senderName:null,
        senderMobile:null,
        senderAddress:null,
      },
      // 仓库列表
      warehouseList:[],
      // 仓位列表
      locationList:[],
      locationLoading:false,
      // 表单校验
      rules: {
        warehouseId: [{ required: true, message: "不能为空", trigger: "change" }],
        stockInOperator: [{ required: true, message: "不能为空", trigger: "change" }],
        senderName: [{ required: true, message: "不能为空", trigger: "change" }],
        senderMobile: [{ required: true, message: "不能为空", trigger: "change" }],
        senderAddress: [{ required: true, message: "不能为空", trigger: "change" }],
      },
      stockInRules:{
        warehouseId: [{ required: true, message: "不能为空", trigger: "change" }],
        productionDate: [{ required: true, message: "不能为空", trigger: "change" }],
        period: [{ required: true, message: "不能为空", trigger: "change" }],
        intoQuantity: [{ required: true, message: "不能为空", trigger: "change" }],
      },
      // 手动确认入库弹出层
      manualConfirmOpen: false,
      // 手动确认入库表单
      manualConfirmForm: {
        stockInId: null,
        stockInNum: null,
        warehouseId: null,
        warehouseName: null,
        itemList: []
      },
      // 手动确认入库表单校验
      manualConfirmRules: {
        itemList: [
          {
            required: true,
            validator: (rule, value, callback) => {
              if (value.length === 0) {
                callback(new Error('请添加商品明细'));
              } else {
                let isValid = true;
                value.forEach(item => {
                  if (!item.actualQuantity || item.actualQuantity <= 0) {
                    isValid = false;
                  }
                  if (!item.purPrice || item.purPrice <= 0) {
                    isValid = false;
                  }
                });
                if (!isValid) {
                  callback(new Error('请填写完整的入库信息'));
                } else {
                  callback();
                }
              }
            },
            trigger: 'blur'
          }
        ]
      }
    };
  },
  created() {
    getUserProfile().then(res=> {
      this.loading = false;
      if (res.data.userType == 0) {
        // 总部
        console.log('===总部');
        listWarehouse(this.queryParams).then(response => {
          this.warehouseList = response.rows;
          this.getList();
        });
      } else if (res.data.userType == 20) {
        // 商户
        listMyAllWarehouse().then(response => {
          this.warehouseList = response.data;
          this.getList();
        })
      } else if (res.data.userType == 40) {
        // 店铺
        listMyAllWarehouse().then(response => {
          this.warehouseList = response.data;
          this.getList();
        })
      }
    })
    // listWarehouse({ pageSize:50}).then(response => {
    //   this.warehouseList = response.rows;
    // });
    // this.getList();
  },
  methods: {
    handleClose(){
      // this.$modal.msgError("关闭")
      this.getList()
    },
    handleAdd(){
      this.$router.push({path:"stock_in/create"})
    },
    searchLocation(query){
      if(!this.form.warehouseId){
        this.$modal.msgError("请选择仓库")
      }else{
        this.locationLoading = true;
        const qw = {
          warehouseId:this.form.warehouseId,
          number: query
        }
        searchPosition(qw).then(res => {
          this.locationList = res.rows;
          this.locationLoading = false;
        })
      }

    },
    locationChanage(row){
      console.log(row)
      const selection = this.locationList.find(x => x.id === row.positionId);
      if (selection) {
        row.positionNum = selection.number
      }
    },
    // qtyChange(row) {
    //   console.log('======值变化=====', row)
    //   if(row.intoQuantity){
    //     row.totalQuantity = parseInt(row.inQuantity) + parseInt(row.intoQuantity)
    //   }else {
    //     row.totalQuantity = row.inQuantity
    //   }
    //
    // },
    /** 查询入库单列表 */
    getList() {
      this.loading = true;
      listStockIn(this.queryParams).then(response => {
        this.WmsStockInEntryList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.cloudOpen = false;
      this.jdycOpen = false;
      this.reset();
    },
    cancel1(){
      this.open = true;
      this.stockInOpen = false
    },
    // 表单重置
    reset() {
      this.form = {
        stockInOperator: null,
        stockInTime: null,
      };
      this.itemList = [];
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    handleStockInComplete(row){
      this.$modal.confirm('确认完成之后就不能再入库了！您确定吗？').then(function() {
        return complete(row.id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("状态修改成功");
      }).catch(() => {});
    },
    /** 入库按钮操作 */
    handleStockIn(row,isEdit) {
      this.$router.push({path : "stock_in", query: { id: row.id }})
      // this.reset();
      // const id = row.id || this.ids
      // this.isEdit = isEdit
      // // listWarehouse({status:1}).then(resp=>{
      // //   this.warehouseList = resp.rows;
      // // })
      // getWmsStockInEntry(id).then(response => {
      //   // this.form = response.data;
      //   this.form.stockInId = response.data.id
      //   this.form.warehouseId = response.data.warehouseId
      //   const qw = {
      //     warehouseId:this.form.warehouseId,
      //     number: ''
      //   }
      //   searchPosition(qw).then(res => {
      //     this.locationList = res.rows;
      //   })
      //   this.itemList = response.data.itemList;
      //
      //   this.itemList.forEach(x=>{
      //     x.intoQuantity = x.quantity - x.inQuantity
      //   })
      //   getUserProfile().then(resp=>{
      //     this.form.stockInOperator = resp.data.nickName
      //   })
      //   this.title = "入库操作";
      //   this.open = true;
      //
      // });
    },
    submitItemForm(row) {
      console.log("======入库操作=====",row)
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.stockInItemId = row.id
          this.form.positionId = row.positionId
          this.form.intoQuantity = row.intoQuantity
          if(!row.intoQuantity || row.intoQuantity<=0){
            this.$modal.msgError("请填写正确的入库数量");
          }else {
            // if(!row.positionId){
            //   this.$modal.msgError("请填写入库仓位");
            // }else{
            stockIn(this.form).then(response => {
              this.$modal.msgSuccess("入库操作成功");
              this.open = false;
              this.getList();
            });
            // }
          }
        }
      });
    },
    // 入库二次弹窗
    submitItemForm2(row) {
      console.log("======入库操作=====",row)
      this.stockInForm.warehouseId = this.form.warehouseId;
      this.stockInForm.stockInId = this.form.stockInId;
      this.stockInForm.stockInItemId = row.id;
      this.stockInForm.goodsName = row.goodsName;
      this.stockInForm.skuName = row.skuName;
      this.stockInForm.skuCode = row.skuCode;
      this.stockInForm.goodsImage = row.goodsImage;
      this.stockInForm.quantity = row.quantity;
      this.stockInForm.inQuantity = row.inQuantity;
      this.stockInItem = row

      this.open = false;
      this.stockInOpen = true
    },
    submitStockInForm() {
      this.$refs["stockInForm"].validate(valid => {
        if (valid) {
          console.log('=======验证通过了========',this.stockInForm)

          stockIn(this.stockInForm).then(response => {
            if(response.code === 200){
              this.$modal.msgSuccess("入库操作成功");
              this.stockInItem.inQuantity = this.stockInItem.inQuantity + this.stockInForm.intoQuantity;
              if(this.stockInItem.inQuantity > this.stockInItem.quantity){
                this.stockInItem.status = 2;
              }
              this.stockInForm={}
              this.stockInOpen = false;
              this.open = true;
              this.getList();
            }else{
              this.$modal.msgError(response.message)
            }


          });
        }
      });
    },
    /** 推送到系统云仓 */
    // handleStockInCloud(row){
    //   this.itemList = []
    //   getWmsStockInEntry(row.id).then(response => {
    //     this.title ="推送到系统云仓"
    //     this.cloudOpen = true
    //     this.itemList = response.data.itemList;
    //     this.cloudForm.id = response.data.id;
    //     this.cloudForm.warehouseId = response.data.warehouseId;
    //   })
    // },
    // submitCloudForm(){
    //   this.$refs["cloudForm"].validate(valid => {
    //     if (valid) {
    //       console.log('=======验证通过了========',this.cloudForm)
    //       pushCloudWarehousePurchase(this.cloudForm).then(response => {
    //         this.$modal.msgSuccess("入库操作成功");
    //         this.open = false;
    //         this.jdycOpen = false;
    //         this.getList();
    //       })
    //     }
    //   });
    // },
    /** 推送到京东云仓 */
    handleStockInJDYC(row){
      this.itemList = []
      getWmsStockInEntry(row.id).then(response => {
        this.title ="推送到三方云仓"
        this.jdycOpen = true
        this.itemList = response.data.itemList;
        this.jdycForm.id = response.data.id;
        this.jdycForm.warehouseId = response.data.warehouseId;
        this.jdycForm.senderName = response.data.senderName;
        this.jdycForm.senderMobile = response.data.senderMobile;
        this.jdycForm.senderAddress = response.data.senderAddress;
        console.log(response)
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["jdycForm"].validate(valid => {
        if (valid) {
            console.log('=======验证通过了========',this.jdycForm)
          pushCloudWarehousePurchase(this.jdycForm).then(response => {
            if(response.code===200){
              this.$modal.msgSuccess("三方云仓采购入库推送成功");
              this.open = false;
              this.jdycOpen = false;
              this.getList();
            }else{
              this.$modal.msgError(response.msg)
            }
          })

          // pushJdCloudWarehousePurchase(this.jdycForm).then(response => {
          //     if(response.code===200){
          //       this.$modal.msgSuccess("入库操作成功");
          //       this.open = false;
          //       this.jdycOpen = false;
          //       this.getList();
          //     }else{
          //       this.$modal.msgError(response.msg)
          //     }
          // })
        }
      });
    },
    // 打开手动确认入库对话框
    handleManualConfirmStockIn(row){
      getWmsStockInEntry(row.id).then(resp=>{
        this.manualConfirmForm = {
          stockInId: row.id,
          stockInNum: row.stockInNum,
          warehouseId: row.warehouseId,
          warehouseName: this.warehouseList.find(x=>x.id === row.warehouseId) ? this.warehouseList.find(x=>x.id === row.warehouseId).warehouseName : '未知',
          itemList: resp.data.itemList.map(item => ({
            ...item,
            skuId: item.skuId,
            goodsId: item.goodsId,
            actualQuantity: item.quantity,
            purPrice: item.purPrice || 0,
            inventoryMode: item.inventoryMode || 0
          }))
        };
        this.manualConfirmOpen = true;
      });
    },

    // 提交手动确认入库
    submitManualConfirm(){
      this.$refs.manualConfirmForm.validate(valid => {
        if (valid) {
          // 调用后端API进行手动确认入库
          import('@/api/wms/thirdPartyCloudWarehouse').then(({ manualConfirmStockIn }) => {
            const requestData = {
              stockInId: this.manualConfirmForm.stockInId,
              warehouseId: this.manualConfirmForm.warehouseId,
              items: this.manualConfirmForm.itemList.map(item => ({
                skuId: item.skuId,
                goodsId: item.goodsId,
                quantity: item.actualQuantity,
                purPrice: item.purPrice,
                inventoryMode: item.inventoryMode || 0
              }))
            };
            manualConfirmStockIn(requestData).then(resp => {
              this.$modal.msgSuccess('手动确认入库成功');
              this.manualConfirmOpen = false;
              this.getList();
            }).catch(err => {
              this.$modal.msgError('手动确认入库失败：' + (err.msg || err.message));
            });
          });
        }
      });
    },

    // 取消手动确认入库
    cancelManualConfirm(){
      this.manualConfirmOpen = false;
    },
	/** 入库单明细序号 */
    rowWmsStockInEntryItemIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
  }
};
</script>
