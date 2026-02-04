<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
    <el-tab-pane v-for="item in typeList" :label="item.name"  :name="item.code" lazy>
      <goods-tao v-if="item.id === 100"></goods-tao>
      <goods-jd v-if="item.id === 200"></goods-jd>
      <goods-pdd v-if="item.id === 300"></goods-pdd>
      <goods-dou v-if="item.id === 400"></goods-dou>
      <goods-wei v-if="item.id === 500"></goods-wei>
    </el-tab-pane>
    </el-tabs>
<!--    <el-tabs v-model="activeName" @tab-click="handleClick">-->
<!--      <el-tab-pane label="天猫" name="taoGoods">-->
<!--        <goods-tao></goods-tao>-->
<!--      </el-tab-pane>-->
<!--      <el-tab-pane label="京东POP" name="jdGoods" lazy>-->
<!--        <goods-jd></goods-jd>-->
<!--      </el-tab-pane>-->
<!--      <el-tab-pane label="京东自营" name="jdvcGoods" lazy>-->
<!--        <goods-jdvc></goods-jdvc>-->
<!--      </el-tab-pane>-->
<!--      <el-tab-pane label="抖店" name="douOrder" lazy>-->
<!--        <goods-dou></goods-dou>-->
<!--      </el-tab-pane>-->
<!--      <el-tab-pane label="拼多多" name="pddOrder" lazy>-->
<!--        <goods-pdd></goods-pdd>-->
<!--      </el-tab-pane>-->
<!--    </el-tabs>-->

  </div>
</template>

<script>
import GoodsTao  from "@/views/tao/goods/index";
import GoodsJd  from "@/views/jd/goods/index";
import GoodsDou  from "@/views/dou/goods/index";
import GoodsPdd  from "@/views/pdd/goods/index";
import GoodsWei  from "@/views/wei/goods/index";
import {listPlatform} from "@/api/shop/shop";

export default {
  name: "Goods",
  components:{
    GoodsTao,GoodsJd,GoodsDou,GoodsPdd,GoodsWei },
  data() {
    return {
      activeName: '',
      typeList: [],
    };
  },
  created() {

  },
  mounted() {
    listPlatform({status:0}).then(res => {
      this.typeList = res.rows;
      this.activeName = this.typeList[0].code
    })
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    }
  }
};
</script>
