<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="规则名称" prop="ruleName">
        <el-input
          v-model="queryParams.ruleName"
          placeholder="请输入规则名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="分账方" prop="sharePartyId">
        <el-select v-model="queryParams.sharePartyId" placeholder="请选择分账方" clearable>
          <el-option
            v-for="item in partyList"
            :key="item.id"
            :label="item.partyName"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="分账方式" prop="shareWay">
        <el-select v-model="queryParams.shareWay" placeholder="请选择分账方式" clearable>
          <el-option label="比例分账" :value="1" />
          <el-option label="固定金额" :value="2" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="禁用" :value="0" />
          <el-option label="启用" :value="1" />
        </el-select>
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
        >添加分账规则</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="ruleList">
      <el-table-column label="ID" align="center" prop="id" width="80" />
      <el-table-column label="分账方" align="left" prop="sharePartyId" width="150">
        <template slot-scope="scope">
          {{ getPartyName(scope.row.sharePartyId) }}
        </template>
      </el-table-column>
      <el-table-column label="分账方式" align="center" prop="shareWay" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.shareWay === 1" type="success">比例分账</el-tag>
          <el-tag v-else type="warning">固定金额</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="分账比例/金额" align="center" prop="shareRatio" width="120">
        <template slot-scope="scope">
          <span v-if="scope.row.shareWay === 1">{{ scope.row.shareRatio }}%</span>
          <span v-else>￥{{ scope.row.shareAmount }}</span>
        </template>
      </el-table-column>
      <el-table-column label="商品名称" align="left" prop="goodsName" min-width="350"/>
      <el-table-column label="规格名称" align="center" prop="skuName" width="150" />
      <el-table-column label="SKU编码" align="center" prop="skuCode" width="150" />
<!--      <el-table-column label="规则名称" align="left" prop="ruleName" min-width="150" />-->

<!--      <el-table-column label="优先级" align="center" prop="priority" width="80" />-->
      <el-table-column label="渠道" align="center" prop="shopType" width="100">
        <template slot-scope="scope">
          <el-tag>{{ getShopTypeName(scope.row.shopType) }}</el-tag>
        </template>
      </el-table-column>
<!--      <el-table-column label="商品ID" align="center" prop="goodsId" width="100" />-->
<!--      <el-table-column label="SKU ID" align="center" prop="goodsSkuId" width="100" />-->

<!--      <el-table-column label="分类ID" align="center" prop="categoryId" width="100" />-->
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            :active-value="1"
            :inactive-value="0"
            @change="handleStatusChange(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
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

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="规则名称" prop="ruleName">
          <el-input v-model="form.ruleName" placeholder="请输入规则名称" />
        </el-form-item>
        <el-form-item label="分账方" prop="sharePartyId">
          <el-select v-model="form.sharePartyId" placeholder="请选择分账方">
            <el-option
              v-for="item in partyList"
              :key="item.id"
              :label="item.partyName"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="分账方式" prop="shareWay">
          <el-radio-group v-model="form.shareWay" @change="shareWayChange">
            <el-radio :label="1">比例分账</el-radio>
            <el-radio :label="2">固定金额</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="分账比例(%)" prop="shareRatio" v-if="form.shareWay === 1">
          <el-input-number v-model="form.shareRatio" :min="0" :max="100" placeholder="请输入分账比例" />
        </el-form-item>
        <el-form-item label="固定金额(元)" prop="shareAmount" v-else>
          <el-input-number v-model="form.shareAmount" :min="0" :precision="2" placeholder="请输入固定金额" />
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-input-number v-model="form.priority" :min="0" placeholder="数值越小越优先" />
        </el-form-item>
        <el-form-item label="店铺类型" prop="shopType">
          <el-select v-model="form.shopType" placeholder="请选择店铺类型（不选则全部适用）" clearable>
            <el-option label="内销渠道" :value="0" />
            <el-option
              v-for="item in platformList"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="选择商品" prop="goodsSkuId">
          <el-input v-model="selectedGoodsName" placeholder="请选择商品" readonly @click.native="openGoodsPopup">
            <el-button slot="append" icon="el-icon-search" @click="openGoodsPopup"></el-button>
          </el-input>
        </el-form-item>
        <el-form-item label="分类ID" prop="categoryId" v-show="false">
          <el-input v-model="form.categoryId" placeholder="请输入分类ID" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="0">禁用</el-radio>
            <el-radio :label="1">启用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <PopupSkuList @data-from-select="handleDataFromPopup" :btn="1" ref="popup"></PopupSkuList>
  </div>
</template>

<script>
import { listShareParty } from "@/api/finance/orderShare";
import { listShareRule, addShareRule, updateShareRule, delShareRule } from "@/api/finance/orderShare";
import { listPlatform } from "@/api/shop/shop";
import PopupSkuList from '@/views/goods/PopupSkuList.vue';

export default {
  name: "ShareRule",
  components: { PopupSkuList },
  data() {
    return {
      loading: true,
      showSearch: true,
      total: 0,
      ruleList: [],
      partyList: [],
      platformList: [],
      selectedGoodsName: '',
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        ruleName: null,
        sharePartyId: null,
        shareWay: null,
        status: null
      },
      form: {
        id: null,
        ruleName: null,
        sharePartyId: null,
        shareWay: 1,
        shareRatio: null,
        shareAmount: null,
        priority: 100,
        shopType: null,
        goodsId: null,
        goodsSkuId: null,
        goodsName: null,
        skuName: null,
        skuCode: null,
        categoryId: null,
        status: 1
      },
      rules: {
        ruleName: [{ required: true, message: "规则名称不能为空", trigger: "blur" }],
        sharePartyId: [{ required: true, message: "请选择分账方", trigger: "change" }],
        shareWay: [{ required: true, message: "请选择分账方式", trigger: "change" }],
        status: [{ required: true, message: "请选择状态", trigger: "change" }]
      }
    };
  },
  created() {
    this.getPartyList();
    this.getList();
    listPlatform({ status: 0 }).then(res => {
      this.platformList = res.rows;
    });
  },
  methods: {
    getPartyList() {
      listShareParty({ pageNum: 1, pageSize: 100 }).then(response => {
        this.partyList = response.rows;
      });
    },
    getPartyName(partyId) {
      const party = this.partyList.find(item => item.id === partyId);
      return party ? party.partyName : '';
    },
    getShopTypeName(shopType) {
      const shopTypeMap = {
        0: '内销渠道',
        100: '淘宝天猫',
        200: '京东POD',
        280: '京东自营',
        300: '拼多多',
        400: '抖音',
        500: '微信',
        600: '快手',
        700: '小红书',
        801: '有赞',
        999: '线下门店'
      };
      return shopTypeMap[shopType] || '全部';
    },
    shareWayChange(val) {
      if (val === 1) {
        this.form.shareRatio = null;
      } else {
        this.form.shareAmount = null;
      }
    },
    openGoodsPopup() {
      this.$refs.popup.openDialog();
    },
    handleDataFromPopup(data) {
      console.log('Received data from popup:', data);
      if (data && data.length > 0) {
        this.form.goodsSkuId = data[0].id;
        this.form.goodsId = data[0].goodsId;
        this.form.goodsName = data[0].goodsName;
        this.form.skuName = data[0].skuName;
        this.form.skuCode = data[0].skuCode;
        this.selectedGoodsName = data[0].goodsName + ' - ' + data[0].skuName;
      } else {
        this.form.goodsSkuId = null;
        this.form.goodsId = null;
        this.form.goodsName = null;
        this.form.skuName = null;
        this.form.skuCode = null;
        this.selectedGoodsName = '';
      }
    },
    getList() {
      this.loading = true;
      listShareRule(this.queryParams).then(response => {
        this.ruleList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    reset() {
      this.form = {
        id: null,
        ruleName: null,
        sharePartyId: null,
        shareWay: 1,
        shareRatio: null,
        shareAmount: null,
        priority: 100,
        shopType: null,
        goodsId: null,
        goodsSkuId: null,
        goodsName: null,
        skuName: null,
        skuCode: null,
        categoryId: null,
        status: 1
      };
      this.selectedGoodsName = '';
      this.resetForm("form");
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加分账规则";
    },
    handleUpdate(row) {
      this.reset();
      this.form = Object.assign({}, row);
      this.open = true;
      this.title = "修改分账规则";
      if (row.goodsSkuId) {
        this.selectedGoodsName = '已选择商品(SKU ID: ' + row.goodsSkuId + ')';
      }
    },
    handleStatusChange(row) {
      updateShareRule(row).then(response => {
        if (response.code === 200) {
          this.$modal.msgSuccess("状态修改成功");
        } else {
          this.$modal.msgError(response.msg);
          this.getList();
        }
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          const data = {
            ...this.form,
            id: this.form.id ? Number(this.form.id) : null,
            sharePartyId: Number(this.form.sharePartyId),
            shareWay: Number(this.form.shareWay),
            shareRatio: this.form.shareRatio ? Number(this.form.shareRatio) : null,
            shareAmount: this.form.shareAmount ? Number(this.form.shareAmount) : null,
            priority: this.form.priority ? Number(this.form.priority) : null,
            shopType: this.form.shopType ? Number(this.form.shopType) : null,
            goodsId: this.form.goodsId ? Number(this.form.goodsId) : null,
            goodsSkuId: this.form.goodsSkuId ? Number(this.form.goodsSkuId) : null,
            categoryId: this.form.categoryId ? Number(this.form.categoryId) : null,
            status: Number(this.form.status)
          };
          if (data.id) {
            updateShareRule(data).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.$modal.msgError(response.msg);
              }
            });
          } else {
            addShareRule(data).then(response => {
              if (response.code === 200) {
                this.$modal.msgSuccess("添加成功");
                this.open = false;
                this.getList();
              } else {
                this.$modal.msgError(response.msg);
              }
            });
          }
        }
      });
    },
    handleDelete(row) {
      this.$modal.confirm('是否确认删除分账规则"' + row.ruleName + '"？')
        .then(() => {
          return delShareRule(row.id);
        })
        .then(response => {
          if (response.code === 200) {
            this.$modal.msgSuccess("删除成功");
            this.getList();
          } else {
            this.$modal.msgError(response.msg);
          }
        })
        .catch(() => {});
    }
  }
};
</script>
