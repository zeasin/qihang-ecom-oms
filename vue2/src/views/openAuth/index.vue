
<template>
  <div class="app-container">
    <el-row style="background-color: #ffffff;margin: 10px 10px 10px 10px;padding: 10px 20px 10px 20px" >
      <el-col span="6" style="color: #ffffff">&nbsp;HOME</el-col>
      <el-col span="12">
        <el-steps :active="6"   finish-status="success">
          <el-step title="添加接口授权AppKey" >
          </el-step>
          <el-step title="API对接" >
          </el-step>
          <el-step title="调用接口" >
          </el-step>

        </el-steps>
      </el-col>

    </el-row>
    <el-row :gutter="40" class="panel-group" style="display: none">
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" >
          <div class="card-panel-icon-wrapper icon-money">
            <svg-icon icon-class="money" class-name="card-panel-icon" />
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">
              今日销售额
            </div>
            <count-to :start-val="0" :end-val="chartData.salesVolume" :duration="3200" class="card-panel-num" />
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" >
          <div class="card-panel-icon-wrapper icon-shopping">
            <svg-icon icon-class="shopping" class-name="card-panel-icon" />
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">
              今日订单量
            </div>
            <count-to :start-val="0" :end-val="chartData.orderCount" :duration="3600" class="card-panel-num" />
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" >
          <div class="card-panel-icon-wrapper icon-message">
            <svg-icon icon-class="stock" class-name="card-panel-icon" />
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">
              待发货
            </div>
            <count-to :start-val="0" :end-val="chartData.waitShip" :duration="3000" class="card-panel-num" />
          </div>
        </div>
      </el-col>

      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" >
          <div class="card-panel-icon-wrapper icon-people">
            <svg-icon icon-class="peoples" class-name="card-panel-icon" />
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">
              店铺
            </div>
            <count-to :start-val="0" :end-val="chartData.shopCount" :duration="2600" class="card-panel-num" />
          </div>
        </div>
      </el-col>
    </el-row>
    <el-row >
      <el-tabs>
        <el-tab-pane label="授权AppKey">
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button
                  type="primary"
                  plain
                  icon="el-icon-plus"
                  size="mini"
                  @click="handleAdd"
              >新增授权AppKey</el-button>
            </el-col>

            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
          </el-row>

          <el-table v-loading="loading" :data="shopList" >
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="ID" align="center" prop="id" width="68"/>
            <el-table-column label="类型" align="center" prop="status" >
              <template slot-scope="scope">
                <el-tag v-if="scope.row.type===0">其他</el-tag>
                <el-tag v-if="scope.row.type===99">其他</el-tag>
                <el-tag v-if="scope.row.type===10">回传专用</el-tag>
                <el-tag v-if="scope.row.type===20">开放接口</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="AppKey" align="center" prop="appKey" />
            <el-table-column label="AppSecret" align="center" prop="appSecret" />
<!--            <el-table-column label="请求次数" align="center" prop="requestCount" />-->
            <el-table-column label="描述" align="center" prop="remark" />
            <el-table-column label="白名单" align="center" prop="whiteList" />
            <el-table-column label="状态" align="center" prop="status" >
              <template slot-scope="scope">
                <el-tag v-if="scope.row.status===1">启用</el-tag>
                <el-tag v-if="scope.row.status===0">禁用</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createTime" >
              <template slot-scope="scope">
                {{ parseTime(scope.row.createTime) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-edit"
                    @click="handleUpdate(scope.row)"
                    v-hasPermi="['shop:shop:edit']"
                >修改</el-button>
                <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-delete"
                    @click="handleDelete(scope.row)"
                    v-hasPermi="['shop:shop:remove']"
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
        </el-tab-pane>
        <el-tab-pane label="添加商品API" >
          <el-table
            :data="tableData"
            style="width: 100%;margin-bottom: 20px;"
            row-key="id"
            default-expand-all
            :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
            <el-table-column prop="name" label="参数名称" width="180"></el-table-column>
            <el-table-column prop="type" label="参数类型" width="180"></el-table-column>
            <el-table-column prop="input" label="是否必须" width="180"></el-table-column>
            <el-table-column prop="remark" label="参数描述"></el-table-column>
          </el-table>
        </el-tab-pane>
<!--        <el-tab-pane label="添加订单API" ></el-tab-pane>-->
<!--        <el-tab-pane label="订单发货API" ></el-tab-pane>-->
<!--        <el-tab-pane label="售后数据API" ></el-tab-pane>-->
<!--        <el-tab-pane label="售后处理API" ></el-tab-pane>-->
<!--        <el-tab-pane label="商品数据API" ></el-tab-pane>-->
<!--        <el-tab-pane label="添加商品API" ></el-tab-pane>-->
<!--        <el-tab-pane label="店铺数据API" ></el-tab-pane>-->
<!--        <el-tab-pane label="添加店铺API" ></el-tab-pane>-->
      </el-tabs>
    </el-row>

    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="AppKey" prop="appKey">
          <el-input v-model="form.appKey" placeholder="请输入AppKey" />
        </el-form-item>
        <el-form-item label="AppSecret" prop="appSecret">
          <el-input v-model.number="form.appSecret" placeholder="请输入AppSecret" />
        </el-form-item>
        <el-form-item label="白名单" prop="whiteList">
          <el-input type="textarea" rows="5" v-model="form.whiteList" placeholder="白名单IP，一行一个" />
        </el-form-item>
        <el-form-item label="描述" prop="remark">
          <el-input type="textarea" v-model="form.remark" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="状态">
            <el-option label="启用" value="1"></el-option>
            <el-option label="禁用" value="0"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {listOpenAuth, getDetail, delShop, addOpenAuth, updateOpenAuth } from "@/api/system/openAuth";
import CountTo from 'vue-count-to'
export default {
  name: "OpenAuth",
  components: {
    CountTo
  },
  data() {
    return {
      loading:false,
      open:false,
      single:false,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        type: null
      },
      chartData:{
        waitShip:50,
        salesVolume:5989.98,
        orderCount:302,
        shopCount:8
      },
      title: '',
      shopList: [],
      tableData: [{
        id: 1,
        name: 'goodsNum',
        type: 'String',
        input: '是',
        remark: '商品编码（唯一）'
      }, {
        id: 2,
        name: 'goodsName',
        type: 'String',
        input: '是',
        remark: '商品名称'
      }, {
        id: 21,
        name: 'goodsImage',
        type: 'String',
        input: '否',
        remark: '商品主图'
      },{
        id: 22,
        name: 'outerErpGoodsId',
        type: 'String',
        input: '否',
        remark: '外部ERP商品编码'
      },{
        id: 23,
        name: 'purPrice',
        type: 'BigDecimal',
        input: '否',
        remark: '采购价'
      },{
        id: 24,
        name: 'retailPrice',
        type: 'BigDecimal',
        input: '否',
        remark: '零售价'
      },

        {
        id: 3,
          name: 'skus',
          type: 'List<ApiGoodsSkuAddRequest>',
          input: '是',
          remark: 'sku数组',
        children: [{
          id: 31,
          name: 'skuCode',
          type: 'String',
          input: '是',
          remark: '商品SKU编码（唯一）'
        }, {
          id: 32,
          name: 'outerErpSkuId',
          type: 'String',
          input: '否',
          remark: '外部ERP商品SKU编码'
        }, {
          id: 21,
          name: 'image',
          type: 'String',
          input: '否',
          remark: 'SKU图片'
        },{
          id: 231,
          name: 'purPrice',
          type: 'BigDecimal',
          input: '否',
          remark: '采购价'
        },{
          id: 241,
          name: 'retailPrice',
          type: 'BigDecimal',
          input: '否',
          remark: '零售价'
        }, {
          id: 33,
          name: 'color',
          type: 'String',
          input: '是',
          remark: '颜色'
        }, {
          id: 34,
          name: 'size',
          type: 'String',
          input: '否',
          remark: '尺码'
        }, {
          id: 35,
          name: 'style',
          type: 'String',
          input: '否',
          remark: '款式'
        }, {
          id: 36,
          name: 'volume',
          type: 'String',
          input: '否',
          remark: '体积'
        }, {
          id: 37,
          name: 'length',
          type: 'Integer',
          input: '否',
          remark: '长度（mm)'
        }, {
            id: 38,
            name: 'width',
            type: 'Integer',
            input: '否',
            remark: '宽度（mm)'
          }, {
            id: 39,
            name: 'height',
            type: 'Integer',
            input: '否',
            remark: '高度（mm)'
          }, {
            id: 40,
            name: 'weight',
            type: 'Double',
            input: '否',
            remark: '重量（g)'
          }
        ]
      }],
      form:{}
    };
  },
  created() {

  },
  mounted() {
    this.getList()
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
      };
      this.resetForm("form");
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.form.status='1'
      this.form.regionId=1
      this.open = true;
      this.title = "添加授权AppKey";
    },
    /** 查询店铺列表 */
    getList() {
      this.loading = true;
      listOpenAuth(this.queryParams).then(response => {
        this.shopList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getDetail(id).then(response => {
        this.form = response.data;
        this.form.status = response.data.status+''
        if(response.data.whiteList){
          this.form.whiteList = response.data.whiteList.replace(',',"\r\n")
        }

        // this.$nextTick(()=>{
        //   this.form.type = response.data.type;
        // })

        this.open = true;
        this.title = "修改";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateOpenAuth(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addOpenAuth(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除编号为"' + ids + '"的数据项？').then(function() {
        return delShop(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
  }
};
</script>

<style lang="scss" scoped>
.panel-group {
  margin-top: 18px;

  .card-panel-col {
    margin-bottom: 32px;
  }

  .card-panel {
    height: 108px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);

    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }

      .icon-people {
        background: #40c9c6;
      }

      .icon-message {
        background: #36a3f7;
      }

      .icon-money {
        background: #f4516c;
      }

      .icon-shopping {
        background: #34bfa3
      }
    }

    .icon-people {
      color: #40c9c6;
    }

    .icon-message {
      color: #36a3f7;
    }

    .icon-money {
      color: #f4516c;
    }

    .icon-shopping {
      color: #34bfa3
    }

    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }

    .card-panel-icon {
      float: left;
      font-size: 48px;
    }

    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px;
      margin-left: 0px;

      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }

      .card-panel-num {
        font-size: 20px;
      }
    }
  }
}

@media (max-width:550px) {
  .card-panel-description {
    display: none;
  }

  .card-panel-icon-wrapper {
    float: none !important;
    width: 100%;
    height: 100%;
    margin: 0 !important;

    .svg-icon {
      display: block;
      margin: 14px auto !important;
      float: none !important;
    }
  }
}
</style>
