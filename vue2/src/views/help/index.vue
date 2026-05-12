<template>
  <div class="help-container">
    <div class="help-sidebar">
      <el-menu
        :default-active="activeMenu"
        class="help-menu"
        @select="handleMenuSelect"
      >
        <el-menu-item index="introduction">
          <i class="el-icon-info"></i>
          <span>系统介绍</span>
        </el-menu-item>

        <el-submenu index="operations">
          <template slot="title">
            <i class="el-icon-menu"></i>
            <span>操作指南</span>
          </template>
          <el-menu-item index="create">创建数据</el-menu-item>
          <el-menu-item index="edit">编辑数据</el-menu-item>
          <el-menu-item index="delete">删除数据</el-menu-item>
        </el-submenu>

        <el-menu-item index="faq">
          <i class="el-icon-question"></i>
          <span>常见问题</span>
        </el-menu-item>

        <el-menu-item index="contact">
          <i class="el-icon-phone"></i>
          <span>联系我们</span>
        </el-menu-item>
      </el-menu>
    </div>

    <div class="help-content">
      <div v-if="activeContent === 'introduction'">
        <div class="html-content" v-html="content"></div>
<!--        <h2>系统介绍</h2>-->
<!--        <p>欢迎使用本系统，这是一个功能强大的后台管理系统，旨在帮助您高效地完成日常工作。</p>-->
<!--        <p>主要功能包括：</p>-->
<!--        <ul>-->
<!--          <li>数据管理</li>-->
<!--          <li>用户管理</li>-->
<!--          <li>报表统计</li>-->
<!--          <li>系统设置</li>-->
<!--        </ul>-->
      </div>

      <div v-if="activeContent === 'create'">
        <h2>创建数据</h2>
        <p>1. 点击页面右上角的"新增"按钮</p>
        <p>2. 在弹出的表单中填写相关信息</p>
        <p>3. 点击"提交"按钮保存数据</p>
        <el-image src="" fit="contain" style="max-width: 600px;"></el-image>
      </div>

      <div v-if="activeContent === 'edit'">
        <h2>编辑数据</h2>
        <p>1. 在数据列表中找到需要编辑的记录</p>
        <p>2. 点击该行右侧的"编辑"按钮</p>
        <p>3. 修改表单中的信息</p>
        <p>4. 点击"保存"按钮更新数据</p>
      </div>

      <div v-if="activeContent === 'delete'">
        <h2>删除数据</h2>
        <p>1. 在数据列表中找到需要删除的记录</p>
        <p>2. 点击该行右侧的"删除"按钮</p>
        <p>3. 在弹出的确认对话框中点击"确定"</p>
        <el-alert title="警告" type="warning" description="删除操作不可逆，请谨慎操作！" show-icon></el-alert>
      </div>

      <div v-if="activeContent === 'faq'">
        <h2>常见问题</h2>
        <el-collapse v-model="activeFaq">
          <el-collapse-item title="如何重置密码？" name="reset-pwd">
            <div>请联系系统管理员，他们会为您重置初始密码。</div>
          </el-collapse-item>
          <el-collapse-item title="数据导出功能在哪里？" name="export">
            <div>在数据列表页面顶部工具栏中，点击"导出"按钮即可导出当前数据。</div>
          </el-collapse-item>
          <el-collapse-item title="系统支持哪些浏览器？" name="browser">
            <div>推荐使用Chrome、Firefox或Edge的最新版本。</div>
          </el-collapse-item>
        </el-collapse>
      </div>

      <div v-if="activeContent === 'contact'">
        <h2>联系我们</h2>
        <p>如果您有任何问题或建议，请联系我们的技术支持团队：</p>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="邮箱">280645618@qq.com</el-descriptions-item>
          <el-descriptions-item label="电话">15818590119</el-descriptions-item>
          <el-descriptions-item label="工作时间">周一至周五 9:00-18:00</el-descriptions-item>
        </el-descriptions>
      </div>
    </div>
  </div>
</template>

<script>
import {getHelpDetail} from "../../api/system/config";

export default {
  name: 'HelpPage',
  data() {
    return {
      activeMenu: 'introduction',
      activeContent: 'introduction',
      activeFaq: ['reset-pwd'],
      content:''
    }
  },
  mounted() {
    console.log(this.$route.query.name)
    if(this.$route.query.name){
      this.activeMenu = this.$route.query.name
      this.activeContent = this.$route.query.name
    }else{
      this.activeMenu = 'introduction'
      this.activeContent = 'introduction'
    }
    this.getDetail(this.activeContent)
  },
  watch: {
    '$route'(to, from) {
      // 每次 URL 改变时，都会执行这个函数
      console.log('URL changed', to, from);
      console.log('Current query parameters: ', to.query);
      if(to.query.name){
        this.activeMenu = to.query.name
        this.activeContent = to.query.name
        this.getDetail(this.activeContent)
      }

    }
  },
  methods: {
    handleMenuSelect(index) {
      console.log('===========',index)
      this.$router.push({path:'/help',query:{name:index}})
      // this.activeContent = index;
      // this.getDetail()
    },
    getDetail(){
      getHelpDetail( this.activeContent).then(resp=>{
        if(resp.code==200){
          this.content = resp.data.content
        }

      })
    }
  }
}
</script>

<style scoped>
.help-container {
  display: flex;
  height: 100%;
  min-height: calc(100vh - 60px); /* 假设顶部导航栏高度为60px */
}

.help-sidebar {
  width: 220px;
  background-color: #fff;
  border-right: 1px solid #e6e6e6;
}

.help-menu {
  border-right: none;
}

.help-content {
  flex: 1;
  padding: 20px 30px;
  overflow-y: auto;
  background-color: #fff;
}

.help-content h2 {
  color: #333;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.help-content p {
  margin: 10px 0;
  line-height: 1.6;
  color: #666;
}

.help-content ul {
  padding-left: 20px;
  color: #666;
}

.el-collapse {
  margin-top: 15px;
}
</style>
