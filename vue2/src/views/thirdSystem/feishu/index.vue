<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="商品库数据保存到飞书多维表格" name="bitableSave" >
        <WikiTableSave></WikiTableSave>
      </el-tab-pane>
      <el-tab-pane label="飞书多维表格数据读取" name="bitable" lazy>
        <WikiTablePage></WikiTablePage>
      </el-tab-pane>
      <el-tab-pane label="飞书参数配置" name="feishuConfig" lazy>
        <FeiShuConfig></FeiShuConfig>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>
import FeiShuConfig from "@/views/thirdSystem/feishu/config.vue";
import WikiTablePage from "@/views/thirdSystem/feishu/wiki_table.vue";
import WikiTableSave from "@/views/thirdSystem/feishu/data_save_bitable.vue";



export default {
  name: "FeiShuApi",
  components:{ FeiShuConfig,WikiTablePage,WikiTableSave},
  data() {
    return {
      activeName: 'bitableSave',
      typeList: [],
    };
  },
  created() {

  },
  mounted() {
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    }
  }
};
</script>
