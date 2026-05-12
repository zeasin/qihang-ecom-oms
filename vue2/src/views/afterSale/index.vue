<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="68px">
      <el-form-item label="售后单号" prop="refundNum">
        <el-input
          v-model="queryParams.refundNum"
          placeholder="请输入售后单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="订单号" prop="orderNum">
        <el-input
          v-model="queryParams.orderNum"
          placeholder="请输入订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!--      <el-form-item label="sku编码" prop="skuCode">-->
      <!--        <el-input-->
      <!--          v-model="queryParams.skuCode"-->
      <!--          placeholder="请输入sku编码"-->
      <!--          clearable-->
      <!--          @keyup.enter.native="handleQuery"-->
      <!--        />-->
      <!--      </el-form-item>-->
      <!--      <el-form-item label="店铺" prop="shopId">-->
      <!--        <el-select v-model="queryParams.shopId" placeholder="请选择店铺" clearable @change="handleQuery">-->
      <!--          <el-option-->
      <!--            v-for="item in shopList"-->
      <!--            :key="item.id"-->
      <!--            :label="item.name"-->
      <!--            :value="item.id">-->
      <!--            <span style="float: left">{{ item.name }}</span>-->
      <!--            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 500">微信小店</span>-->
      <!--            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 200">京东POP</span>-->
      <!--            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 280">京东自营</span>-->
      <!--            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 100">淘宝天猫</span>-->
      <!--            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 300">拼多多</span>-->
      <!--            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 400">抖店</span>-->
      <!--            <span style="float: right; color: #8492a6; font-size: 13px"  v-if="item.type === 999">线下渠道</span>-->
      <!--          </el-option>-->
      <!--        </el-select>-->
      <!--      </el-form-item>-->

      <el-form-item label="类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择类型" clearable @change="handleQuery">
          <el-option label="退货" value="10" />
          <el-option label="换货" value="20" />
          <el-option label="维修" value="30" />
          <el-option label="补发" value="80" />
          <!--          <el-option label="订单拦截" value="99" ></el-option>-->
        </el-select>
      </el-form-item>
      <el-form-item label="处理状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择处理状态" clearable @change="handleQuery">
          <el-option label="待处理" value="0" />
          <el-option label="已发出" value="1" />
          <el-option label="已收货" value="2" />
          <el-option label="已完结" value="10" />

        </el-select>
      </el-form-item>

      <!--      <el-form-item label="物流单号" prop="logisticsCode">-->
      <!--        <el-input-->
      <!--          v-model="queryParams.logisticsCode"-->
      <!--          placeholder="请输入物流单号"-->
      <!--          clearable-->
      <!--          @keyup.enter.native="handleQuery"-->
      <!--        />-->
      <!--      </el-form-item>-->

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
        >手动添加</el-button>
      </el-col>
      <!--      <el-col :span="1.5">-->
      <!--        <el-button-->
      <!--          type="danger"-->
      <!--          plain-->
      <!--          icon="el-icon-download"-->
      <!--          size="mini"-->
      <!--          @click="handleShippingLog"-->
      <!--        >ERP售后处理推送记录</el-button>-->
      <!--      </el-col>-->
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <!--      <el-table-column label="ID" align="center" prop="id" />-->
      <el-table-column label="售后单号" align="left" prop="refundNum" width="240px">
        <template slot-scope="scope">
          {{ scope.row.refundNum }} <i class="el-icon-copy-document tag-copy" :data-clipboard-text="scope.row.refundNum" @click="copyActiveCode($event,scope.row.refundNum)" />
          <br>
          <el-tag v-if="scope.row.shopId===0" type="info">总部销售订单</el-tag>
          <el-tag v-else type="info">{{ shopListAll.find(x=>x.id === scope.row.shopId) ? shopListAll.find(x=>x.id === scope.row.shopId).name : '' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="订单号" align="left" prop="orderNum" width="200px">
        <template slot-scope="scope">
          {{ scope.row.orderNum }} <i class="el-icon-copy-document tag-copy" :data-clipboard-text="scope.row.orderNum" @click="copyActiveCode($event,scope.row.orderNum)" />
          <br>
        </template>
      </el-table-column>
      <!--      <el-table-column label="子订单号" align="left" prop="subOrderNum" width="200px"/>-->
      <el-table-column label="售后类型" align="center" prop="type">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.type === 0" size="small">无需处理</el-tag>
          <el-tag v-if="scope.row.type === 10" size="small">退货</el-tag>
          <el-tag v-if="scope.row.type === 20" size="small">换货</el-tag>
          <el-tag v-if="scope.row.type === 30" size="small">维修</el-tag>
          <el-tag v-if="scope.row.type === 80" size="small">补发</el-tag>
          <el-tag v-if="scope.row.type === 99" size="small">订单拦截</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="商品" align="left" prop="title" width="280px" />
      <el-table-column label="规格" align="left" prop="skuInfo" width="200px">
        <template slot-scope="scope">
          {{ getSkuValues(scope.row.skuInfo) }}
        </template>
      </el-table-column>
      <el-table-column label="Sku编码" align="center" prop="skuCode" />
      <el-table-column label="数量" align="center" prop="quantity" />
      <el-table-column label="是否发货" align="center" prop="hasGoodsSend">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.hasGoodsSend === 0" size="small"> 未发货</el-tag>
          <el-tag v-if="scope.row.hasGoodsSend === 1" size="small"> 已发货</el-tag>

        </template>
      </el-table-column>
      <!--      <el-table-column label="物流单号" align="center" prop="shipWaybillCode" />-->
      <!--      <el-table-column label="收货人" align="center" prop="receiverName" />-->
      <!--      <el-table-column label="手机号" align="center" prop="receiverTel" />-->
      <!--      <el-table-column label="收货地址" align="center" prop="receiverAddress" />-->
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="处理结果" align="center" prop="result" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 0" size="small"> 待处理</el-tag>
          <el-tag v-if="scope.row.status === 1" size="small"> 已发出</el-tag>
          <el-tag v-if="scope.row.status === 2" size="small"> 已收货</el-tag>
          <el-tag v-if="scope.row.status === 10" size="small"> 处理完成</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-row v-if="scope.row.type === 10">
            <el-button
              v-if="scope.row.status === 0"
              v-hasPermi="['api:returned:edit']"
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleReturned(scope.row)"
            >退货确认</el-button>
          </el-row>
          <el-row v-if="scope.row.type === 20">
            <el-button
              v-if="scope.row.status === 0"
              v-hasPermi="['api:returned:edit']"
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleExchange(scope.row)"
            >换货确认</el-button>
          </el-row>
          <el-row v-if="scope.row.type === 80">
            <el-button
              v-if="scope.row.status === 0"
              v-hasPermi="['api:returned:edit']"
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleShipAgain(scope.row)"
            >补发确认</el-button>
          </el-row>
          <!--          <el-button-->
          <!--           v-if="scope.row.status === 0"-->
          <!--            size="mini"-->
          <!--            type="text"-->
          <!--            icon="el-icon-edit"-->
          <!--            @click="handleUpdate(scope.row)"-->
          <!--            v-hasPermi="['api:returned:edit']"-->
          <!--          >确认完成</el-button>-->
          <!--          <el-button-->
          <!--            v-if="(scope.row.refundType === 10 || scope.row.refundType === 20) && scope.row.status === 10005"-->
          <!--            size="mini"-->
          <!--            type="text"-->
          <!--            icon="el-icon-edit"-->
          <!--            @click="handleUpdate(scope.row)"-->
          <!--            v-hasPermi="['api:returned:edit']"-->
          <!--          >确认收货</el-button>-->
          <!--          <el-button-->
          <!--            v-if="(scope.row.refundType === 20 || scope.row.refundType === 80)"-->
          <!--            size="mini"-->
          <!--            type="text"-->
          <!--            icon="el-icon-edit"-->
          <!--            @click="handleUpdate(scope.row)"-->
          <!--            v-hasPermi="['api:returned:edit']"-->
          <!--          >补发商品</el-button>-->
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
    <!-- 退货入库确认框 -->
    <el-dialog title="退货确认" :visible.sync="returnedOpen" width="500px" append-to-body>
      <el-form ref="returnedForm" :model="returnedForm" :rules="returnedRules" label-width="120px">
        <el-form-item label="商品名称" prop="title">{{ returnedForm.title }}</el-form-item>
        <el-form-item label="规格" prop="skuInfo">{{ getSkuValues(returnedForm.skuInfo) }}</el-form-item>
        <el-form-item label="数量" prop="quantity">{{ returnedForm.quantity }}</el-form-item>
        <!--        <el-form-item label="退回物流公司" prop="returnLogisticsCompany">-->
        <!--          <el-input v-model="returnedForm.returnLogisticsCompany" placeholder="请输入退回物流公司" style="width: 300px"/>-->
        <!--        </el-form-item>-->
        <el-form-item label="退回物流公司" prop="returnLogisticsCompany">
          <!--              <el-input v-model="form.shippingCompany" placeholder="请输入物流公司" style="width:300px" />-->
          <el-select v-model="returnedForm.returnLogisticsCompany" filterable r placeholder="选择退回物流公司" style="width:300px">
            <el-option v-for="item in logisticsList" :key="item.logisticsId" :label="item.logisticsName" :value="item.logisticsId">
              <span style="float: left">{{ item.logisticsName }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px;">{{ getPlatformName(item.platformId) }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="退回物流单号" prop="returnLogisticsCode">
          <el-input v-model="returnedForm.returnLogisticsCode" placeholder="请输入退回物流单号" style="width: 300px" />
        </el-form-item>
        <!--        <el-form-item label="退回类型" prop="returnWarehouseId" v-if="returnedForm.sendShipType!=300">-->
        <!--          <el-select v-model="returnedForm.returnWarehouseId" filterable r placeholder="退回仓库" style="width: 300px">-->
        <!--            <el-option v-for="item in warehouseList" :key="item.id" :label="item.warehouseName" :value="item.id">-->
        <!--              <span style="float: left">{{ item.warehouseName }}</span>-->
        <!--              <span style="float: right; color: #8492a6; font-size: 13px" v-if="item.warehouseType=='LOCAL'">本地仓</span>-->
        <!--              <span style="float: right; color: #8492a6; font-size: 13px" v-else-if="item.warehouseType=='CLOUD'">系统云仓</span>-->
        <!--              <span style="float: right; color: #8492a6; font-size: 13px" v-else-if="item.warehouseType=='JDYC'">京东云仓</span>-->
        <!--              <span style="float: right; color: #8492a6; font-size: 13px" v-else>未知仓</span>-->
        <!--            </el-option>-->
        <!--          </el-select>-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="退回仓库" prop="returnWarehouseId" v-if="returnedForm.sendShipType!=300">-->
        <!--          <el-select v-model="returnedForm.returnWarehouseId" filterable r placeholder="退回仓库" style="width: 300px">-->
        <!--            <el-option v-for="item in warehouseList" :key="item.id" :label="item.warehouseName" :value="item.id">-->
        <!--              <span style="float: left">{{ item.warehouseName }}</span>-->
        <!--              <span style="float: right; color: #8492a6; font-size: 13px" v-if="item.warehouseType=='LOCAL'">本地仓</span>-->
        <!--              <span style="float: right; color: #8492a6; font-size: 13px" v-else-if="item.warehouseType=='CLOUD'">系统云仓</span>-->
        <!--              <span style="float: right; color: #8492a6; font-size: 13px" v-else-if="item.warehouseType=='JDYC'">京东云仓</span>-->
        <!--              <span style="float: right; color: #8492a6; font-size: 13px" v-else>未知仓</span>-->
        <!--            </el-option>-->
        <!--          </el-select>-->
        <!--        </el-form-item>-->
        <el-form-item label="备注" prop="remark">
          <el-input v-model="returnedForm.remark" type="textarea" placeholder="请输入备注" style="width: 300px" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitReturnedForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 补发 对话框 -->
    <el-dialog :title="title" :visible.sync="shipAgainOpen" width="500px" append-to-body>
      <el-form ref="shipAgainForm" :model="shipAgainForm" :rules="shipAgainRules" label-width="120px">
        <el-form-item label="商品名称" prop="title">{{ shipAgainForm.title }}</el-form-item>
        <el-form-item label="规格" prop="skuInfo">{{ getSkuValues(shipAgainForm.skuInfo) }}</el-form-item>
        <el-form-item label="数量" prop="quantity">{{ shipAgainForm.quantity }}</el-form-item>

        <el-form-item label="收货人" prop="receiverName">
          <el-input v-model="shipAgainForm.receiverName" placeholder="请输入收货人" />
        </el-form-item>
        <el-form-item label="手机号" prop="receiverTel">
          <el-input v-model="shipAgainForm.receiverTel" placeholder="请输入收货人手机号" />
        </el-form-item>
        <el-form-item label="收货地址" prop="receiverAddress">
          <el-input v-model="shipAgainForm.receiverAddress" placeholder="请输入收货地址" />
        </el-form-item>
        <!--        <el-form-item label="补发仓库" prop="reissueWarehouseId" v-if="shipAgainForm.sendShipType!=300">-->
        <!--          <el-select v-model="shipAgainForm.reissueWarehouseId" filterable r placeholder="补发仓库" style="width: 300px">-->
        <!--            <el-option v-for="item in warehouseList" :key="item.id" :label="item.warehouseName" :value="item.id">-->
        <!--              <span style="float: left">{{ item.warehouseName }}</span>-->
        <!--              <span style="float: right; color: #8492a6; font-size: 13px" v-if="item.warehouseType=='LOCAL'">本地仓</span>-->
        <!--              <span style="float: right; color: #8492a6; font-size: 13px" v-else-if="item.warehouseType=='CLOUD'">系统云仓</span>-->
        <!--              <span style="float: right; color: #8492a6; font-size: 13px" v-else-if="item.warehouseType=='JDYC'">京东云仓</span>-->
        <!--              <span style="float: right; color: #8492a6; font-size: 13px" v-else>未知仓</span>-->
        <!--            </el-option>-->
        <!--          </el-select>-->
        <!--        </el-form-item>-->
        <el-form-item label="备注" prop="remark">
          <el-input v-model="exchangeForm.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitShipAgainForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 换货 对话框 -->
    <el-dialog :title="title" :visible.sync="exchangeOpen" width="500px" append-to-body>
      <el-form ref="exchangeForm" :model="exchangeForm" :rules="exchangeRules" label-width="120px">
        <el-form-item label="商品名称" prop="title">{{ exchangeForm.title }}</el-form-item>
        <el-form-item label="规格" prop="skuInfo">{{ getSkuValues(exchangeForm.skuInfo) }}</el-form-item>
        <el-form-item label="数量" prop="quantity">{{ exchangeForm.quantity }}</el-form-item>

        <el-form-item label="退回物流公司" prop="returnLogisticsCompany">
          <el-input v-model="exchangeForm.returnLogisticsCompany" placeholder="请输入退回物流公司" style="width: 300px" />
        </el-form-item>
        <el-form-item label="退回物流单号" prop="returnLogisticsCode">
          <el-input v-model="exchangeForm.returnLogisticsCode" placeholder="请输入退回物流单号" style="width: 300px" />
        </el-form-item>
        <el-form-item label="收货人" prop="receiverName">
          <el-input v-model="exchangeForm.receiverName" placeholder="请输入收货人" />
        </el-form-item>
        <el-form-item label="手机号" prop="receiverTel">
          <el-input v-model="exchangeForm.receiverTel" placeholder="请输入收货人手机号" />
        </el-form-item>
        <el-form-item label="收货地址" prop="receiverAddress">
          <el-input v-model="exchangeForm.receiverAddress" placeholder="请输入收货地址" />
        </el-form-item>
        <!--        <el-form-item label="补发仓库" prop="reissueWarehouseId" v-if="shipAgainForm.sendShipType!=300">-->
        <!--          <el-select v-model="shipAgainForm.reissueWarehouseId" filterable r placeholder="补发仓库" style="width: 300px">-->
        <!--            <el-option v-for="item in warehouseList" :key="item.id" :label="item.warehouseName" :value="item.id">-->
        <!--              <span style="float: left">{{ item.warehouseName }}</span>-->
        <!--              <span style="float: right; color: #8492a6; font-size: 13px" v-if="item.warehouseType=='LOCAL'">本地仓</span>-->
        <!--              <span style="float: right; color: #8492a6; font-size: 13px" v-else-if="item.warehouseType=='CLOUD'">系统云仓</span>-->
        <!--              <span style="float: right; color: #8492a6; font-size: 13px" v-else-if="item.warehouseType=='JDYC'">京东云仓</span>-->
        <!--              <span style="float: right; color: #8492a6; font-size: 13px" v-else>未知仓</span>-->
        <!--            </el-option>-->
        <!--          </el-select>-->
        <!--        </el-form-item>-->
        <el-form-item label="备注" prop="remark">
          <el-input v-model="exchangeForm.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitExchangeForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 添加或修改退换货对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择类型" clearable @change="handleQuery">
            <el-option label="退货" value="10" />
            <el-option label="换货" value="20" />
            <el-option label="补发" value="80" />
            <!--          <el-option label="订单拦截" value="99" ></el-option>-->
          </el-select>
        </el-form-item>
        <el-form-item label="售后单号" prop="refundNum">
          <el-input v-model="form.refundNum" placeholder="请输入售后单号" />
        </el-form-item>
        <el-form-item label="订单号" prop="orderNum">
          <el-input v-model="form.orderNum" placeholder="请输入订单号" />
        </el-form-item>
        <el-form-item label="子订单id" prop="subOrderId">
          <el-input v-model="form.subOrderId" placeholder="请输入订单id" />
        </el-form-item>
        <el-form-item label="平台商品id" prop="productId">
          <el-input v-model="form.productId" placeholder="请输入平台商品id" />
        </el-form-item>
        <el-form-item label="平台skuId" prop="skuId">
          <el-input v-model="form.skuId" placeholder="请输入平台skuId" />
        </el-form-item>
        <el-form-item label="Sku编码" prop="skuCode">
          <el-input v-model="form.skuCode" placeholder="请输入Sku编码" />
        </el-form-item>
        <el-form-item label="ERP商品id" prop="erpGoodsId">
          <el-input v-model="form.erpGoodsId" placeholder="请输入ERP商品id" />
        </el-form-item>
        <el-form-item label="ERP商品skuId" prop="erpSkuId">
          <el-input v-model="form.erpSkuId" placeholder="请输入ERP商品skuId" />
        </el-form-item>
        <el-form-item label="商品名称" prop="title">
          <el-input v-model="form.title" placeholder="请输入商品名称" />
        </el-form-item>
        <el-form-item label="商品SKU信息" prop="skuInfo">
          <el-input v-model="form.skuInfo" placeholder="请输入商品SKU信息" />
        </el-form-item>
        <el-form-item label="数量" prop="count">
          <el-input v-model="form.count" placeholder="请输入数量" />
        </el-form-item>
        <el-form-item label="发货物流公司" prop="shipCompany">
          <el-input v-model="form.shipCompany" placeholder="请输入发货物流公司" />
        </el-form-item>
        <el-form-item label="发货物流单号" prop="shipWaybillCode">
          <el-input v-model="form.shipWaybillCode" placeholder="请输入发货物流单号" />
        </el-form-item>
        <el-form-item label="收货人" prop="receiverName">
          <el-input v-model="form.receiverName" placeholder="请输入收货人" />
        </el-form-item>
        <el-form-item label="收货人手机号" prop="receiverTel">
          <el-input v-model="form.receiverTel" placeholder="请输入收货人手机号" />
        </el-form-item>
        <el-form-item label="收货地址" prop="receiverAddress">
          <el-input v-model="form.receiverAddress" placeholder="请输入收货地址" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 详情对话框 -->

  </div>
</template>

<script>
import { exchangeConfirm, list, returnedConfirmAndStockIn, shipAgainConfirm } from '@/api/afterSale/after_sale'
import { getShopListData } from '@/utils/shopUtils'
import { listWarehouse } from '@/api/wms/warehouse'
import { amountFormatter } from '../../utils/zhijian'
import { copyActiveCode, parseTime } from '@/utils/zhijian'
import { getFavoriteList } from '@/api/shipping/ship_logistics'

export default {
  name: 'AfterSale',
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
      // 表格数据
      dataList: [],
      shopList: [],
      shopListAll: [],
      warehouseList: [],
      logisticsList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      detailOpen: false,
      returnedOpen: false,
      shipAgainOpen: false,
      exchangeOpen: false,
      // 平台映射（参考 EnumShopType）
      platformMap: {
        0: 'ERP内销订单',
        100: '淘宝天猫',
        200: '京东POP',
        280: '京东自营',
        300: '拼多多',
        400: '抖店',
        500: '微信小店',
        600: '快手小店',
        700: '小红书',
        901: '微店',
        911: '螳螂系统',
        999: '线下渠道',
        10000: '店铺订单'
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        afterSaleOrderId: null,
        shopId: null,
        orderId: null,
        status: null
      },
      // 表单参数
      returnedForm: {
        id: null,
        title: null,
        skuInfo: null,
        quantity: null,
        reissueWarehouseId: null,
        returnLogisticsCompany: null,
        returnLogisticsCode: null,
        returnWarehouseId: null,
        sendShipType: null,
        remark: null
      },
      shipAgainForm: {},
      exchangeForm: {},
      form: {
        returnWarehouseId: null
      },
      // 表单校验
      exchangeRules: {
        returnLogisticsCompany: [{ required: true, message: '不能为空', trigger: 'blur' }],
        returnLogisticsCode: [{ required: true, message: '不能为空', trigger: 'blur' }],
        receiverName: [{ required: true, message: '不能为空', trigger: 'blur' }],
        receiverTel: [{ required: true, message: '不能为空', trigger: 'blur' }],
        receiverAddress: [{ required: true, message: '不能为空', trigger: 'blur' }]
      },
      returnedRules: {
        returnLogisticsCompany: [{ required: true, message: '不能为空', trigger: 'blur' }],
        returnLogisticsCode: [{ required: true, message: '不能为空', trigger: 'blur' }]
        // returnWarehouseId: [{ required: true, message: "不能为空", trigger: "blur" }],
      },
      shipAgainRules: {
        receiverName: [{ required: true, message: '不能为空', trigger: 'blur' }],
        receiverTel: [{ required: true, message: '不能为空', trigger: 'blur' }],
        receiverAddress: [{ required: true, message: '不能为空', trigger: 'blur' }]
        // reissueWarehouseId: [{ required: true, message: "不能为空", trigger: "change" }],
      },
      rules: {
        type: [{ required: true, message: '请选择类型', trigger: 'change' }],
        refundNum: [{ required: true, message: '不能为空', trigger: 'blur' }],
        orderNum: [{ required: true, message: '订单号不能为空', trigger: 'blur' }],
        erpGoodsId: [{ required: true, message: '不能为空', trigger: 'blur' }],
        erpSkuId: [{ required: true, message: '不能为空', trigger: 'change' }],
        skuInfo: [{ required: true, message: '不能为空', trigger: 'change' }],
        title: [{ required: true, message: '不能为空', trigger: 'change' }],
        count: [{ required: true, message: '不能为空', trigger: 'change' }],
        shipCompany: [{ required: true, message: '不能为空', trigger: 'change' }],
        shipWaybillCode: [{ required: true, message: '不能为空', trigger: 'change' }],
        receiverName: [{ required: true, message: '不能为空', trigger: 'change' }],
        receiverTel: [{ required: true, message: '不能为空', trigger: 'change' }],
        receiverAddress: [{ required: true, message: '不能为空', trigger: 'change' }]
      }
    }
  },
  created() {
    listWarehouse({}).then(response => {
      this.warehouseList = response.rows
      getFavoriteList({ shopType: 0, status: 1 }).then(resp => {
        this.logisticsList = resp.data || []
      })
      this.getList()
    })
  },
  async mounted() {
    this.shopListAll = await getShopListData() // 获取并设置 shopList
  },
  methods: {
    parseTime,
    copyActiveCode,
    amountFormatter,
    // 获取平台名称
    getPlatformName(platformId) {
      return this.platformMap[platformId] || '未知'
    },
    /** 查询退换货列表 */
    getList() {
      this.loading = true
      list(this.queryParams).then(response => {
        this.dataList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    getSkuValues(spec) {
      try {
        // 解析 JSON，返回一个数组
        const parsedSpec = JSON.parse(spec) || []

        // 使用 map 提取所有 value，使用 join() 用逗号连接
        return parsedSpec.map(item => item.attr_value || item.value).join(', ') || ''
      } catch (error) {
        return spec // 如果 JSON 解析出错，返回空字符串
      }
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.returnedOpen = false
      this.shipAgainOpen = false
      this.exchangeOpen = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {}
      this.returnedForm = {
        id: null,
        title: null,
        skuInfo: null,
        quantity: null,
        reissueWarehouseId: null,
        returnLogisticsCompany: null,
        returnLogisticsCode: null,
        returnWarehouseId: null,
        sendShipType: null,
        remark: null
      }
      this.exchangeForm = {
        id: null,
        title: null,
        skuInfo: null,
        quantity: null,
        reissueWarehouseId: null,
        returnLogisticsCompany: null,
        returnLogisticsCode: null,
        returnWarehouseId: null,
        sendShipType: null,
        remark: null,
        receiverName: null,
        receiverTel: null,
        receiverAddress: null
      }
      this.shipAgainForm = {}
      this.title = ''
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '手动添加售后记录'
    },

    /** 补发确认按钮操作 */
    handleShipAgain(row) {
      this.reset()
      this.shipAgainForm.id = row.id
      this.shipAgainForm.title = row.title
      this.shipAgainForm.skuInfo = row.skuInfo
      this.shipAgainForm.quantity = row.quantity
      this.shipAgainForm.remark = null
      this.title = '补发确认'
      this.shipAgainOpen = true
    },
    /** 补发确认按钮操作 */
    handleExchange(row) {
      this.reset()
      this.exchangeForm.id = row.id
      this.exchangeForm.title = row.title
      this.exchangeForm.skuInfo = row.skuInfo
      this.exchangeForm.quantity = row.quantity
      this.exchangeForm.returnLogisticsCompany = row.returnLogisticsCompany
      this.exchangeForm.returnLogisticsCode = row.returnLogisticsCode
      this.exchangeForm.remark = null
      this.exchangeForm.receiverName = null
      this.exchangeForm.receiverTel = null
      this.exchangeForm.receiverAddress = null
      this.title = '换货确认'
      this.exchangeOpen = true
    },
    /** 退货确认按钮操作 */
    handleReturned(row) {
      this.reset()
      this.returnedForm.id = row.id
      this.returnedForm.title = row.title
      this.returnedForm.skuInfo = row.skuInfo
      this.returnedForm.quantity = row.quantity
      this.returnedForm.returnLogisticsCompany = row.returnLogisticsCompany
      this.returnedForm.returnLogisticsCode = row.returnLogisticsCode
      this.returnedForm.remark = null
      if (row.sendWarehouseId && row.sendWarehouseId != 0) {
        this.returnedForm.returnWarehouseId = row.sendWarehouseId
      } else {
        this.returnedForm.returnWarehouseId = null
      }

      this.returnedForm.sendShipType = row.sendShipType
      this.returnedOpen = true
    },
    submitReturnedForm() {
      this.$refs['returnedForm'].validate(valid => {
        if (valid) {
          // if(this.returnedForm.sendShipType!=300 && !this.returnedForm.returnWarehouseId){
          //    this.$modal.msgError("请选择退回仓库")
          // }else{
          returnedConfirmAndStockIn(this.returnedForm).then(response => {
            console.log(response)
            if (response.code == 200) {
              this.$modal.msgSuccess('处理成功')
              this.returnedOpen = false
              this.reset()
              this.getList()
            } else {
              this.$modal.msgError(response.msg)
            }
          })
        }

        // }
      })
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      this.open = true
      this.title = '手动添加补发信息'
      // shipAgainComplete(id).then(response => {
      //   this.$modal.msgSuccess("确认完成");
      //   this.getList()
      // });
    },
    /** 提交按钮 */
    submitShipAgainForm() {
      this.$refs['shipAgainForm'].validate(valid => {
        if (valid) {
          shipAgainConfirm(this.shipAgainForm).then(response => {
            if (response.code == 200) {
              this.$modal.msgSuccess('处理成功')
              this.shipAgainOpen = false
              this.reset()
              this.getList()
            } else {
              this.$modal.msgError(response.msg)
            }
          })
        }
      })
    },
    submitExchangeForm() {
      this.$refs['exchangeForm'].validate(valid => {
        if (valid) {
          exchangeConfirm(this.exchangeForm).then(response => {
            if (response.code == 200) {
              this.$modal.msgSuccess('处理成功')
              this.exchangeOpen = false
              this.reset()
              this.getList()
            } else {
              this.$modal.msgError(response.msg)
            }
          })
        }
      })
    },
    submitForm() {

    }
  }
}
</script>
