<template>
  <div class="app-container">
    <el-form ref="form" :model="goods" :rules="rules" label-width="108px" inline>
      <el-col :span="24">
      <el-form-item label="商品图片" prop="image">
        <el-image :src="goods.image" disabled style="width: 200px"/>
      </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="goods.name" disabled placeholder="请输入商品名称" style="width:800px"/>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="商品编号" prop="goodsNum" >
          <el-input v-model="goods.goodsNum" disabled placeholder="请输入商品编号" style="width:220px"/>
        </el-form-item>
      </el-col>
<!--        <el-col :span="6">-->
<!--          <el-form-item label="商品分类" prop="categoryId">-->
<!--            <treeselect :options="dataList" disabled placeholder="请选择上级菜单" v-model="form.categoryId" style="width:220px"  @select="categoryChange"/>-->
<!--          </el-form-item>-->
<!--        </el-col>-->
<!--      <el-col :span="6">-->
<!--        <el-form-item label="供应商" prop="supplierId">-->
<!--          &lt;!&ndash; <el-input v-model="form.supplierId" placeholder="请输入供应商id" /> &ndash;&gt;-->
<!--          <el-select v-model="goods.supplierId" disabled  placeholder="请选择供应商名称">-->
<!--            <el-option v-for="item in supplierList" :key="item.id" :label="item.name" :value="item.id">-->
<!--          </el-option>-->
<!--        </el-select>-->
<!--        </el-form-item>-->
<!--      </el-col>-->
<!--      <el-col :span="6">-->
<!--      <el-form-item label="发货方式" prop="shipType">-->
<!--        <el-select v-model="goods.shipType" disabled  placeholder="请选择发货方式">-->
<!--          <el-option label="自营发货" value="10"></el-option>-->
<!--          <el-option label="供应商发货" value="20"></el-option>-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      </el-col>-->



        <el-form-item label="商品规格">
          <el-row :gutter="10" class="mb8" >
            <el-col :span="1.5" style="width: 68px">规格：</el-col>
            <el-col :span="20">
              <el-select
                v-model="form.colorValues"
                multiple
                filterable
                allow-create
                default-first-option
                @input="onSpecChange"
                placeholder="自定义规格（可以上传图片）">
                <el-option
                  v-for="item in colorList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
<!--              <treeselect :options="colorList" placeholder="规格1" v-model="form.colorValues"  allow-create :normalizer="normalizer"  @input="onSpecChange" :multiple="true" />-->
            </el-col>
          </el-row>
          <el-row :gutter="10" class="mb8" >

            <el-col :span="24" style="margin-left: 60px;">
              <ul style=" display: flex;list-style: none;padding: 0;">
                <li v-for="color in form.colorValues" :key="color" style="margin-left: 20px;">
                  <el-upload
                    class="avatar-uploader"
                    :action="uploadImgUrl"
                    :show-file-list="false"
                    :headers="headers"
                    :on-success="(response, file, fileList) =>
                handleUploadSuccess(
                  response,
                  file,
                  fileList,
                  color
                )
            "
                    :before-upload="handleBeforeUpload">
                    <img v-if="form.colorImages[color]" :src="form.colorImages[color]" class="avatar">
                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                  </el-upload>
                  <span>{{form.colorNames[color]}}</span>
                </li>
              </ul>

            </el-col>
          </el-row>
          <el-row :gutter="10" class="mb8" >
            <el-col :span="1.5" style="width: 68px">规格2：</el-col>
            <el-col :span="20">
<!--              <treeselect :options="sizeList" placeholder="规格2" v-model="form.sizeValues" :normalizer="normalizer" @input="onSpecChange" :multiple="true" />-->
              <el-select
                v-model="form.sizeValues"
                multiple
                filterable
                allow-create
                default-first-option
                @input="onSpecChange"
                placeholder="自定义规格2（如：重量等）">
                                <el-option
                                  v-for="item in sizeList"
                                  :key="item.value"
                                  :label="item.label"
                                  :value="item.value">
                                </el-option>
              </el-select>
            </el-col>
          </el-row>
          <el-row :gutter="10" class="mb8" >
            <el-col :span="1.5" style="width: 68px">规格3：</el-col>
            <el-col :span="20">
<!--              <treeselect :options="styleList" placeholder="规格3" v-model="form.styleValues" :normalizer="normalizer" @input="onSpecChange" :multiple="true" />-->

              <el-select
                v-model="form.styleValues"
                multiple
                filterable
                allow-create
                default-first-option
                @input="onSpecChange"
                placeholder="自定义规格3（如套餐等）">
                <el-option
                  v-for="item in styleList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>

          </el-row>

        </el-form-item>
        <!-- <el-divider content-position="center" style="margin-left: 98px;">商品信息</el-divider> -->

        <el-table style="margin-left: 108px;" :data="form.specList" :row-class-name="rowSShopOrderItemIndex" ref="sShopOrderItem">
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="规格" prop="color" width="120">
            <template slot-scope="scope">
              <el-input v-model="scope.row.colorValue" disabled placeholder="颜色" />
            </template>
          </el-table-column>
          <el-table-column label="规格2" prop="size" width="120">
            <template slot-scope="scope">
              <el-input v-model="scope.row.sizeValue" disabled placeholder="尺码" />
            </template>
          </el-table-column>
          <el-table-column label="规格3" prop="style" width="120">
            <template slot-scope="scope">
              <el-input v-model="scope.row.styleValue" disabled placeholder="款式" />
            </template>
          </el-table-column>
          <el-table-column label="规格编码" prop="specNum" width="250">
            <template slot-scope="scope">
              <el-input v-model="scope.row.specNum" placeholder="规格编码" />
            </template>
          </el-table-column>
          <el-table-column label="条形码" prop="barCode" width="250">
            <template slot-scope="scope">
              <el-input v-model="scope.row.barCode" placeholder="条形码" />
            </template>
          </el-table-column>
          <el-table-column label="外部SkuId" prop="outerErpSkuId" width="220">
            <template slot-scope="scope">
              <el-input v-model="scope.row.outerErpSkuId" placeholder="外部ID" />
            </template>
          </el-table-column>
          <el-table-column label="单位" prop="unit" width="100" >
            <template slot-scope="scope">
              <el-input v-model="scope.row.unit" placeholder="单位" />
            </template>
          </el-table-column>
          <el-table-column label="采购价" prop="purPrice" width="120" v-if="form.priceType==0">
            <template slot-scope="scope">
              <el-input v-model="scope.row.purPrice" placeholder="采购价" @input="handlePurPriceInput(scope.row)" />
            </template>
          </el-table-column>
          <el-table-column label="零售价" prop="retailPrice" width="120" v-if="form.priceType==0">
            <template slot-scope="scope">
              <el-input v-model="scope.row.retailPrice" placeholder="零售价" @input="handleRetailPriceInput(scope.row)" />
            </template>
          </el-table-column>

<!--          <el-table-column label="金重(g)" prop="weight1" width="100" v-if="form.priceType==1">-->
<!--            <template slot-scope="scope">-->
<!--              <el-input v-model="scope.row.weight1" placeholder="金重(g)" />-->
<!--            </template>-->
<!--          </el-table-column>-->

<!--          <el-table-column label="银重(g)" prop="weight2" width="100" v-if="form.priceType==1">-->
<!--            <template slot-scope="scope">-->
<!--              <el-input v-model="scope.row.weight2" placeholder="银重(g)" />-->
<!--            </template>-->
<!--          </el-table-column>-->
<!--          <el-table-column label="工时" prop="weight3" width="100" v-if="form.priceType==1">-->
<!--            <template slot-scope="scope">-->
<!--              <el-input v-model="scope.row.weight3" placeholder="工时" />-->
<!--            </template>-->
<!--          </el-table-column>-->
<!--          <el-table-column label="外部ERP SkuId" prop="outerErpSkuId" width="200">-->
<!--            <template slot-scope="scope">-->
<!--              <el-input v-model.number="scope.row.outerErpSkuId" placeholder="外部ERP商品Sku Id" />-->
<!--            </template>-->
<!--          </el-table-column>-->
<!--          <el-table-column label="规格图片" prop="colorImage" width="150">-->
<!--            <template slot-scope="scope">-->
<!--&lt;!&ndash;              <image-upload v-model="scope.row.colorImage" :limit="1" style="width: 100px;height: 100px"/>&ndash;&gt;-->
<!--              <el-upload-->
<!--                class="avatar-uploader"-->
<!--                :action="uploadImgUrl"-->
<!--                :show-file-list="false"-->
<!--                :headers="headers"-->
<!--                :file-list="fileList"-->
<!--                :on-success="handleUploadSuccess"-->
<!--                :before-upload="handleBeforeUpload">-->
<!--                <img v-if="scope.row.colorImage" :src="scope.row.colorImage" class="avatar">-->
<!--                <i v-else class="el-icon-plus avatar-uploader-icon"></i>-->
<!--              </el-upload>-->
<!--            </template>-->

<!--          </el-table-column>-->
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer" style="margin-left: 108px;margin-top:20px;margin-bottom: 50px;">
        <el-button type="primary" @click="submitForm">添加商品SKU</el-button>
        <!-- <el-button @click="cancel">取 消</el-button> -->
      </div>
  </div>
</template>

<script>
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import { listCategory } from "@/api/goods/category";
import { listCategoryAttributeValue } from "@/api/goods/categoryAttributeValue";
import { addGoodsSku,getGoods } from "@/api/goods/goods";
import { getToken } from "@/utils/auth";
import {
  provinceAndCityData,
  pcTextArr,
  regionData,
  pcaTextArr,
  codeToText,
} from "element-china-area-data";
import {listSupplier} from "@/api/goods/supplier";
import {listCategoryAttribute} from "@/api/goods/categoryAttribute";
import { limitDecimalLength, stringToNumber } from "@/utils/numberInput";
export default {
  name: "OrderCreate",
  components: { Treeselect },
  data() {
    return {
      uploadImgUrl: process.env.VUE_APP_BASE_API + "/api/sys-api/images/upload",
      headers: {
        Authorization: "Bearer " + getToken(),
      },
      // 文件类型, 例如['png', 'jpg', 'jpeg']
      fileType: ["png", "jpg", "jpeg"],
      uploadList: [],
      fileList: [],
      // 表单参数
      goods:{},
      form: {
        province:undefined,
        city:undefined,
        town:undefined,
        colorValues:undefined,
        colorImages:{},
        colorNames:{},
        sizeValues:undefined,
        styleValues:undefined,
        outerErpGoodsId:undefined,
        number:'',
        categoryId:undefined,
        specList:[],
        provinces: [],
        shipType:null
      },
      supplierList: [],
      pcaTextArr,
      dataList:[{
          id: 'fruits',
          label: 'Fruits',
          children: [],
      }],
      // 表单校验
      rules: {
      },
      // 颜色
      colorList:[],
      // 尺码
      sizeList:[],
      //款式
      styleList:[],
      privateData:{}
    };
  },
  created() {

    this.getCategoryList()
    listSupplier({pageNum: 1, pageSize: 100}).then(resp=>{
      this.supplierList = resp.rows
    })
    // listCategoryAttributeValue({categoryAttributeId:114}).then(resp=>{
    //   this.colorList = resp.rows
    // })
    // listCategoryAttributeValue({categoryAttributeId:115}).then(resp=>{
    //   this.sizeList = resp.rows
    // })
    // listCategoryAttributeValue({categoryAttributeId:116}).then(resp=>{
    //   this.styleList = resp.rows
    // })
  },
  mounted() {
    getGoods(this.$route.query.id).then(response => {
      this.goods = response.data;
      this.goods.shipType = response.data.shipType+''
      this.form.id = response.data.id
      this.form.priceType = response.data.priceType

    });
  },
  methods: {
    categoryChange(node, instanceId){
      console.log("====分类边哈11111====",node,instanceId)
      console.log("====分类边哈====",this.form.categoryId)
      if(node){
        this.form.categoryId = node.id
        console.log("====分类边哈2====",this.form.categoryId)
        let topCategoryId = 0;
        if(node.parentId===0) topCategoryId=node.id;
        else topCategoryId = node.parentId
        console.log("====分类边哈22222====",topCategoryId)
        this.colorList = []
        this.sizeList = []
        this.styleList=[]
        listCategoryAttribute({categoryId:topCategoryId}).then(response => {
          this.categoryAttributeList = response.rows;
          if(response.rows){
            // 获取分类属性
            response.rows.forEach(x=>{
              listCategoryAttributeValue({categoryAttributeId:x.id}).then(resp=>{

                if(x.code==='color'){
                  this.colorList = resp.rows
                }else if(x.code==='size'){
                  this.sizeList = resp.rows
                }else if(x.code==='style'){
                  this.styleList = resp.rows
                }

              })
            })
          }
        });


        // 获取分类属性
        // listCategoryAttributeValue({categoryAttributeId:114}).then(resp=>{
        //   this.colorList = resp.rows
        // })
        // listCategoryAttributeValue({categoryAttributeId:115}).then(resp=>{
        //   this.sizeList = resp.rows
        // })
        // listCategoryAttributeValue({categoryAttributeId:116}).then(resp=>{
        //   this.styleList = resp.rows
        // })
      }
    },
    getRowDate(row){

    },
    // 上传前loading加载
    handleBeforeUpload(file) {
      let isImg = false;
      if (this.fileType.length) {
        let fileExtension = "";
        if (file.name.lastIndexOf(".") > -1) {
          fileExtension = file.name.slice(file.name.lastIndexOf(".") + 1);
        }
        isImg = this.fileType.some(type => {
          if (file.type.indexOf(type) > -1) return true;
          if (fileExtension && fileExtension.indexOf(type) > -1) return true;
          return false;
        });
      } else {
        isImg = file.type.indexOf("image") > -1;
      }

      if (!isImg) {
        this.$modal.msgError(`文件格式不正确, 请上传${this.fileType.join("/")}图片格式文件!`);
        return false;
      }
      if (this.fileSize) {
        const isLt = file.size / 1024 / 1024 < this.fileSize;
        if (!isLt) {
          this.$modal.msgError(`上传头像图片大小不能超过 ${this.fileSize} MB!`);
          return false;
        }
      }
      // this.$modal.loading("正在上传图片，请稍候...");
      // this.number++;
    },
    // 文件个数超出
    handleExceed() {
      this.$modal.msgError(`上传文件数量不能超过 ${this.limit} 个!`);
    },
    // 上传成功回调
    handleUploadSuccess(response, file,ty, color) {
      // console.log('====上传成功回调====',file,response)
      console.log('====上传成功回调====',color,response.url)
      this.$nextTick(()=>{
        this.form.colorImages[color] = response.url
        console.log('=====上传回调赋值=====',this.form.colorImages)
      })
      this.form = { ...this.form, colorImages: { ...this.form.colorImages, [color]: response.url } };
      // if (res.code ===   200) {
      //
      //   this.uploadList.push({ name: res.fileName, url: res.url });
      //   this.uploadedSuccessfully();
      // } else {
      //   this.number--;
      //   this.$modal.closeLoading();
      //   this.$modal.msgError(res.msg);
      //   this.$refs.imageUpload.handleRemove(file);
      //   this.uploadedSuccessfully();
      // }
    },
    // 删除图片
    handleDelete(file) {
      const findex = this.fileList.map(f => f.name).indexOf(file.name);
      if(findex > -1) {
        this.fileList.splice(findex, 1);
        this.$emit("input", this.listToString(this.fileList));
      }
    },
    // 上传失败
    handleUploadError() {
      this.$modal.msgError("上传图片失败，请重试");
      this.$modal.closeLoading();
    },
    // 上传结束处理
    uploadedSuccessfully() {
      if (this.number > 0 && this.uploadList.length === this.number) {
        this.fileList = this.fileList.concat(this.uploadList);
        this.uploadList = [];
        this.number = 0;
        this.$emit("input", this.listToString(this.fileList));
        this.$modal.closeLoading();
      }
    },
    normalizer(node) {
      return {
        id: node.id,
        label: node.value
      };
    },
    buildTree(list, parentId) {
      let tree = [];
      for (let i = 0; i < list.length; i++) {
        if (list[i].parentId === parentId) {
          let node = {
            id: list[i].id,
            parentId:list[i].parentId,
            label: list[i].name,
            children: this.buildTree(list, list[i].id)
          };
          tree.push(node);
        }
      }
      return tree;
    },
    /** 查询商品分类列表 */
    getCategoryList() {
      // this.loading = true;
      listCategory(this.queryParams).then(response => {
        this.dataList = this.buildTree(response.rows,0)
        // console.log("构建后的list",this.categoryList)
        // this.total = response.total;
        this.loading = false;
      });
    },
    onSpecChange(selected){
      // console.log('=====选择11111了=======',selected)
      // console.log('=======颜色111111：====',this.form.colorValues)
      // console.log('=======尺码11111：====',this.form.sizeValues)
      // console.log('=======款式11111：====',this.form.styleValues)
      //组合
      if(this.form.colorValues && this.form.colorValues.length>0){
        this.form.specList = []
        // this.form.specList.push()
        if(this.form.sizeValues && this.form.sizeValues.length>0 && this.form.styleValues && this.form.styleValues.length>0){
          // console.log('====颜色、尺码、款式===')
          this.form.colorValues.forEach(c=>{
              // const color = this.colorList.find(x=>x.id === c)
              this.form.sizeValues.forEach(s=>{
                // const size = this.sizeList.find(y=>y.id === s)
                this.form.styleValues.forEach(st=>{
                // const style = this.styleList.find(z=>z.id === st)
                 const spec = {
                  colorId:0,
                  colorValue:c,
                   sizeId:0,
                  sizeValue:s,
                  styleId:0,
                  styleValue:st,
                  specNum:this.goods.goodsNum+'-',//this.form.number+color.skuCode+size.skuCode+style.skuCode
                   outerErpSkuId:'',
                   barCode:'',
                   unit:'',
                   weight1:null,
                   weight2:null,
                   weight3:null,
                   // purPrice:this.form.purPrice,
                   // retailPrice:this.form.retailPrice,
                   purPrice:null,
                   retailPrice:null,
                }
                this.form.specList.push(spec)
                })
              })
            })

        }else {
          // 有一个没有选择
          if(this.form.sizeValues && this.form.sizeValues.length>0){
            // console.log('====颜色、尺码22222===')
            // console.log('=======颜色22222：====',this.form.colorValues)
            // console.log('=======尺码22222：====',this.form.sizeValues)
            // console.log('=======款式22222：====',this.form.styleValues)
            this.form.colorValues.forEach(c=>{
              // console.log('=======颜色22222：====',c)
              // const color = this.colorList.find(x=>x.id === c)
              this.form.sizeValues.forEach(s=>{
                // const size = this.sizeList.find(y=>y.id === s)
                 const spec = {
                  colorId:0,
                  colorValue:c,
                  sizeId:0,
                  sizeValue:s,
                  styleId:null,
                  styleValue:'',
                  specNum:this.goods.goodsNum+'-',//this.form.number+color.skuCode+size.skuCode
                   outerErpSkuId:'',
                   barCode:'',
                   unit:'',
                   weight1:null,
                   weight2:null,
                   weight3:null,
                   // purPrice:this.form.purPrice,
                   // retailPrice:this.form.retailPrice,
                   purPrice:null,
                   retailPrice:null,

                }
                this.form.specList.push(spec)
              })
            })
          }else if(this.form.styleValues && this.form.styleValues.length>0){
            // 选择了款式
            // console.log('====颜色、款式===')

          }else{
            // console.log('====颜色===')
            this.form.colorValues.forEach(x=>{
              // console.log('====颜色11111111111===',x)
              // const color = this.colorList.find(c=>c.id === x)
              const spec = {
                colorId:0,
                colorValue:x,
                sizeId:null,
                sizeValue:'',
                styleId:null,
                styleValue:'',
                specNum:this.goods.goodsNum+'-',
                outerErpSkuId:'',
                barCode:'',
                unit:'',
                weight1:null,
                weight2:null,
                weight3:null,
                // purPrice:this.form.purPrice,
                // retailPrice:this.form.retailPrice,
                purPrice:null,
                retailPrice:null,
              }
              this.form.specList.push(spec)
            })
          }
        }

        this.form.colorNames = {}
        this.form.colorValues.forEach(c=>{
          const color = this.colorList.find(x=>x.id === c)
          // this.form.colorNames[c] = color.value
          this.form.colorNames[c] = color
        })
      }
      // else{
      //   this.$modal.msgError("必须选择【颜色】")
      //   this.form.sizeValues = []
      //   this.form.styleValues = []
      // }



    },
    /** ${subTable.functionName}序号 */
    rowSShopOrderItemIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    /** 提交按钮 */
    submitForm() {
      console.log('=====添加商品===',this.form)
      this.$refs["form"].validate(valid => {
        if (valid) {

          // 判断规格 specList
          if(!this.form.specList || this.form.specList.length === 0){
            this.$modal.msgError("请添加商品规格")
            return
          }else{
            for(let i=0;i<this.form.specList.length;i++){
              const sp = this.form.specList[i]
              if(!sp.specNum){
                this.$modal.msgError("商品规格编码不能为空")
                return
              }
              if(this.form.priceType==0){
                // 判断价格
                if(sp.purPrice==null){
                  this.$modal.msgError("请填写采购价")
                  return
                }
                if(sp.retailPrice==null){
                  this.$modal.msgError("请填写零售价")
                  return
                }
                // 将价格字段从字符串转换回数字类型
                sp.purPrice = stringToNumber(sp.purPrice);
                sp.retailPrice = stringToNumber(sp.retailPrice);
              }
              // if(this.form.priceType==1){
              //   // 判断金重银重工时
              //   if(!sp.weight1){
              //     this.$modal.msgError("请填写金重(g)")
              //     return
              //   }
              //   if(!sp.weight2){
              //     this.$modal.msgError("请填写银重(g)")
              //     return
              //   }
              //   if(!sp.weight3){
              //     this.$modal.msgError("请填写工时")
              //     return
              //   }
              // }
            }

          }
          addGoodsSku(this.form).then(response => {
            if(response.code == 200) {
              this.$modal.msgSuccess("商品SKU添加成功");
              // 调用全局挂载的方法,关闭当前标签页
              this.$store.dispatch("tagsView/delView", this.$route);
              this.$router.push('/goods/goods_list');
            }else{
              this.$modal.msgError(response.msg)
            }
          });



        }
      });
    },
    handlePurPriceInput(row) {
      row.purPrice = limitDecimalLength(row.purPrice);
    },
    handleRetailPriceInput(row) {
      row.retailPrice = limitDecimalLength(row.retailPrice);
    }
  }
};

</script>
<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 78px;
  height: 78px;
  line-height: 78px;
  text-align: center;
}
.avatar {
  width: 78px;
  height: 78px;
  display: block;
}
</style>
