<template>
  <el-dialog
    title="自动审核规则配置"
    :visible.sync="dialogVisible"
    width="90%"
    top="5vh"
    :close-on-click-modal="false"
    @open="handleDialogOpen"
    @close="handleDialogClose"
    custom-class="auto-review-rules-dialog"
  >
    <!-- 主内容区域 -->
    <div class="dialog-content" v-loading="loading">
      <!-- 标签页导航 -->
      <el-tabs v-model="activeTab" type="card" class="rules-tabs">
        <!-- 规则列表标签页 -->
        <el-tab-pane label="规则列表" name="ruleList">
          <div class="rule-list-content">
            <!-- 规则筛选工具栏 -->
            <div class="filter-toolbar">
              <el-form :inline="true" :model="filterForm" class="filter-form">
                <el-form-item label="规则名称">
                  <el-input
                    v-model="filterForm.ruleName"
                    placeholder="输入规则名称"
                    clearable
                    style="width: 200px"
                  ></el-input>
                </el-form-item>

                <el-form-item label="规则状态">
                  <el-select
                    v-model="filterForm.status"
                    placeholder="全部状态"
                    clearable
                    style="width: 120px"
                  >
                    <el-option label="启用" value="active"></el-option>
                    <el-option label="禁用" value="inactive"></el-option>
                    <el-option label="草稿" value="draft"></el-option>
                  </el-select>
                </el-form-item>

                <el-form-item label="规则类型">
                  <el-select
                    v-model="filterForm.ruleType"
                    placeholder="全部类型"
                    clearable
                    style="width: 150px"
                  >
                    <el-option label="价格规则" value="price"></el-option>
                    <el-option label="供应商规则" value="supplier"></el-option>
                    <el-option label="综合规则" value="comprehensive"></el-option>
                    <el-option label="自定义规则" value="custom"></el-option>
                  </el-select>
                </el-form-item>

                <el-form-item>
                  <el-button type="primary" icon="el-icon-search" @click="handleSearch">
                    搜索
                  </el-button>
                  <el-button @click="resetFilter">重置</el-button>
                </el-form-item>
              </el-form>

              <div class="toolbar-actions">
                <el-button
                  type="primary"
                  icon="el-icon-plus"
                  @click="handleCreateRule"
                >
                  新建规则
                </el-button>
                <el-button
                  icon="el-icon-refresh"
                  @click="refreshRules"
                >
                  刷新
                </el-button>
              </div>
            </div>

            <!-- 规则统计卡片 -->
            <div class="stats-cards" v-if="rules.length > 0">
              <el-row :gutter="20">
                <el-col :span="6">
                  <el-card class="stat-card" shadow="hover">
                    <div class="stat-content">
                      <div class="stat-icon" style="color: #67C23A;">
                        <i class="el-icon-success"></i>
                      </div>
                      <div class="stat-info">
                        <div class="stat-value">{{ stats.activeCount }}</div>
                        <div class="stat-label">启用中</div>
                      </div>
                    </div>
                  </el-card>
                </el-col>

                <el-col :span="6">
                  <el-card class="stat-card" shadow="hover">
                    <div class="stat-content">
                      <div class="stat-icon" style="color: #909399;">
                        <i class="el-icon-switch-button"></i>
                      </div>
                      <div class="stat-info">
                        <div class="stat-value">{{ stats.inactiveCount }}</div>
                        <div class="stat-label">已禁用</div>
                      </div>
                    </div>
                  </el-card>
                </el-col>

                <el-col :span="6">
                  <el-card class="stat-card" shadow="hover">
                    <div class="stat-content">
                      <div class="stat-icon" style="color: #E6A23C;">
                        <i class="el-icon-document"></i>
                      </div>
                      <div class="stat-info">
                        <div class="stat-value">{{ stats.draftCount }}</div>
                        <div class="stat-label">草稿</div>
                      </div>
                    </div>
                  </el-card>
                </el-col>

                <el-col :span="6">
                  <el-card class="stat-card" shadow="hover">
                    <div class="stat-content">
                      <div class="stat-icon" style="color: #409EFF;">
                        <i class="el-icon-timer"></i>
                      </div>
                      <div class="stat-info">
                        <div class="stat-value">{{ stats.totalExecutions }}</div>
                        <div class="stat-label">总执行次数</div>
                      </div>
                    </div>
                  </el-card>
                </el-col>
              </el-row>
            </div>

            <!-- 规则列表表格 -->
            <el-card class="rules-table-card" shadow="never">
              <el-table
                :data="filteredRules"
                v-loading="tableLoading"
                style="width: 100%"
                @sort-change="handleSortChange"
                :default-sort="{ prop: 'priority', order: 'ascending' }"
              >
                <el-table-column prop="name" label="规则名称" width="200">
                  <template slot-scope="{ row }">
                    <div class="rule-name-cell">
                      <div class="rule-name">{{ row.name }}</div>
                      <div class="rule-code">{{ row.code }}</div>
                    </div>
                  </template>
                </el-table-column>

                <el-table-column prop="type" label="规则类型" width="120">
                  <template slot-scope="{ row }">
                    <el-tag :type="getRuleTypeTagType(row.type)" size="small">
                      {{ formatRuleType(row.type) }}
                    </el-tag>
                  </template>
                </el-table-column>

                <el-table-column prop="description" label="规则描述" min-width="200">
                  <template slot-scope="{ row }">
                    <div class="rule-description" :title="row.description">
                      {{ row.description || '暂无描述' }}
                    </div>
                  </template>
                </el-table-column>

                <el-table-column prop="priority" label="优先级" width="100" sortable>
                  <template slot-scope="{ row }">
                    <el-tag
                      :type="getPriorityTagType(row.priority)"
                      size="small"
                      effect="plain"
                    >
                      {{ row.priority }}
                    </el-tag>
                  </template>
                </el-table-column>

                <el-table-column prop="status" label="状态" width="100">
                  <template slot-scope="{ row }">
                    <el-switch
                      v-model="row.status"
                      :active-value="'active'"
                      :inactive-value="'inactive'"
                      active-text="启用"
                      inactive-text="禁用"
                      @change="handleToggleStatus(row)"
                    ></el-switch>
                  </template>
                </el-table-column>

                <el-table-column prop="executionCount" label="执行次数" width="100" sortable>
                  <template slot-scope="{ row }">
                    <span class="execution-count">{{ row.executionCount || 0 }}</span>
                  </template>
                </el-table-column>

                <el-table-column prop="successRate" label="成功率" width="120" sortable>
                  <template slot-scope="{ row }">
                    <div class="success-rate-cell">
                      <el-progress
                        :percentage="row.successRate || 0"
                        :show-text="false"
                        :stroke-width="6"
                        :color="getSuccessRateColor(row.successRate)"
                      ></el-progress>
                      <span class="rate-text">{{ (row.successRate || 0).toFixed(1) }}%</span>
                    </div>
                  </template>
                </el-table-column>

                <el-table-column prop="updatedAt" label="更新时间" width="150" sortable>
                  <template slot-scope="{ row }">
                    <div class="time-cell">
                      {{ formatDateTime(row.updatedAt) }}
                    </div>
                  </template>
                </el-table-column>

                <el-table-column label="操作" width="180" fixed="right">
                  <template slot-scope="{ row, $index }">
                    <div class="action-buttons">
                      <el-button
                        type="primary"
                        icon="el-icon-edit"
                        size="mini"
                        circle
                        @click="handleEditRule(row)"
                        title="编辑规则"
                      ></el-button>

                      <el-button
                        type="success"
                        icon="el-icon-view"
                        size="mini"
                        circle
                        @click="handleViewRule(row)"
                        title="查看详情"
                      ></el-button>

                      <el-button
                        type="warning"
                        icon="el-icon-copy-document"
                        size="mini"
                        circle
                        @click="handleDuplicateRule(row)"
                        title="复制规则"
                      ></el-button>

                      <el-popconfirm
                        title="确定要删除这条规则吗？"
                        @confirm="handleDeleteRule(row, $index)"
                        confirm-button-text="确定"
                        cancel-button-text="取消"
                      >
                        <el-button
                          slot="reference"
                          type="danger"
                          icon="el-icon-delete"
                          size="mini"
                          circle
                          title="删除规则"
                        ></el-button>
                      </el-popconfirm>
                    </div>
                  </template>
                </el-table-column>
              </el-table>

              <!-- 分页 -->
              <div class="pagination-container" v-if="pagination.total > 0">
                <el-pagination
                  @size-change="handlePageSizeChange"
                  @current-change="handlePageChange"
                  :current-page="pagination.current"
                  :page-sizes="[10, 20, 50, 100]"
                  :page-size="pagination.size"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="pagination.total"
                ></el-pagination>
              </div>

              <!-- 空状态 -->
              <div class="empty-state" v-if="!tableLoading && filteredRules.length === 0">
                <el-empty description="暂无自动审核规则">
                  <el-button type="primary" @click="handleCreateRule">
                    创建第一条规则
                  </el-button>
                </el-empty>
              </div>
            </el-card>
          </div>
        </el-tab-pane>

        <!-- 规则详情标签页 -->
        <el-tab-pane :label="detailTabLabel" name="ruleDetail" v-if="currentRule">
          <div class="rule-detail-content">
            <!-- 规则基本信息 -->
            <el-card class="rule-basic-info" shadow="never">
              <div slot="header" class="card-header">
                <span>规则基本信息</span>
                <el-button
                  v-if="isEditMode"
                  type="primary"
                  size="small"
                  @click="handleSaveRule"
                  :loading="saving"
                >
                  保存规则
                </el-button>
                <el-button
                  v-else
                  type="primary"
                  size="small"
                  @click="enableEditMode"
                >
                  编辑规则
                </el-button>
              </div>

              <div class="card-body">
                <el-form
                  ref="ruleForm"
                  :model="ruleForm"
                  :rules="ruleRules"
                  label-width="120px"
                  :disabled="!isEditMode"
                >
                  <el-row :gutter="20">
                    <el-col :span="12">
                      <el-form-item label="规则名称" prop="name">
                        <el-input
                          v-model="ruleForm.name"
                          placeholder="请输入规则名称"
                        ></el-input>
                      </el-form-item>

                      <el-form-item label="规则编码" prop="code">
                        <el-input
                          v-model="ruleForm.code"
                          placeholder="自动生成或手动输入"
                        >
                          <el-button
                            slot="append"
                            icon="el-icon-refresh"
                            @click="generateRuleCode"
                            :disabled="!isEditMode"
                          ></el-button>
                        </el-input>
                      </el-form-item>

                      <el-form-item label="规则类型" prop="type">
                        <el-select
                          v-model="ruleForm.type"
                          placeholder="请选择规则类型"
                          style="width: 100%"
                        >
                          <el-option label="价格规则" value="price"></el-option>
                          <el-option label="供应商规则" value="supplier"></el-option>
                          <el-option label="交货周期规则" value="delivery"></el-option>
                          <el-option label="综合评估规则" value="comprehensive"></el-option>
                          <el-option label="自定义规则" value="custom"></el-option>
                        </el-select>
                      </el-form-item>

                      <el-form-item label="优先级" prop="priority">
                        <el-input-number
                          v-model="ruleForm.priority"
                          :min="1"
                          :max="100"
                          controls-position="right"
                          style="width: 100%"
                        ></el-input-number>
                        <div class="form-tip">数值越小优先级越高</div>
                      </el-form-item>
                    </el-col>

                    <el-col :span="12">
                      <el-form-item label="规则状态" prop="status">
                        <el-radio-group v-model="ruleForm.status">
                          <el-radio label="active">启用</el-radio>
                          <el-radio label="inactive">禁用</el-radio>
                          <el-radio label="draft">草稿</el-radio>
                        </el-radio-group>
                      </el-form-item>

                      <el-form-item label="适用范围" prop="scope">
                        <el-select
                          v-model="ruleForm.scope"
                          placeholder="请选择适用范围"
                          style="width: 100%"
                          multiple
                          collapse-tags
                        >
                          <el-option label="全部商品" value="all"></el-option>
                          <el-option label="特定类目" value="category"></el-option>
                          <el-option label="特定供应商" value="supplier"></el-option>
                          <el-option label="特定商品" value="product"></el-option>
                        </el-select>
                      </el-form-item>

                      <el-form-item label="执行动作" prop="action">
                        <el-select
                          v-model="ruleForm.action"
                          placeholder="请选择执行动作"
                          style="width: 100%"
                        >
                          <el-option label="自动通过" value="auto_approve"></el-option>
                          <el-option label="自动拒绝" value="auto_reject"></el-option>
                          <el-option label="标记为待审核" value="mark_pending"></el-option>
                          <el-option label="发送预警通知" value="send_alert"></el-option>
                        </el-select>
                      </el-form-item>

                      <el-form-item label="生效时间" prop="effectiveTime">
                        <el-date-picker
                          v-model="ruleForm.effectiveTime"
                          type="datetimerange"
                          range-separator="至"
                          start-placeholder="开始日期"
                          end-placeholder="结束日期"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          style="width: 100%"
                        ></el-date-picker>
                      </el-form-item>
                    </el-col>
                  </el-row>

                  <el-form-item label="规则描述" prop="description">
                    <el-input
                      v-model="ruleForm.description"
                      type="textarea"
                      :rows="3"
                      placeholder="请输入规则描述"
                      maxlength="500"
                      show-word-limit
                    ></el-input>
                  </el-form-item>
                </el-form>
              </div>
            </el-card>

            <!-- 条件配置 -->
            <el-card class="rule-conditions" shadow="never">
              <div slot="header" class="card-header">
                <span>条件配置</span>
                <el-button
                  type="primary"
                  size="small"
                  icon="el-icon-plus"
                  @click="addCondition"
                  :disabled="!isEditMode"
                >
                  添加条件
                </el-button>
              </div>

              <div class="card-body">
                <div v-if="ruleForm.conditions && ruleForm.conditions.length > 0">
                  <el-table
                    :data="ruleForm.conditions"
                    border
                    size="small"
                    style="width: 100%"
                  >
                    <el-table-column prop="field" label="条件字段" width="180">
                      <template slot-scope="{ row, $index }">
                        <el-select
                          v-model="row.field"
                          placeholder="选择字段"
                          size="small"
                          style="width: 100%"
                          :disabled="!isEditMode"
                          @change="handleFieldChange(row, $index)"
                        >
                          <el-option-group label="价格相关">
                            <el-option label="单价" value="unit_price"></el-option>
                            <el-option label="含税单价" value="tax_included_price"></el-option>
                            <el-option label="历史价格对比" value="historical_price_ratio"></el-option>
                            <el-option label="市场均价对比" value="market_price_ratio"></el-option>
                          </el-option-group>
                          <el-option-group label="供应商相关">
                            <el-option label="供应商等级" value="supplier_rating"></el-option>
                            <el-option label="历史交货准时率" value="delivery_on_time_rate"></el-option>
                            <el-option label="历史质量合格率" value="quality_pass_rate"></el-option>
                            <el-option label="合作年限" value="cooperation_years"></el-option>
                          </el-option-group>
                          <el-option-group label="交货相关">
                            <el-option label="交货周期" value="delivery_days"></el-option>
                            <el-option label="最小起订量" value="moq"></el-option>
                          </el-option-group>
                        </el-select>
                      </template>
                    </el-table-column>

                    <el-table-column prop="operator" label="比较运算符" width="120">
                      <template slot-scope="{ row, $index }">
                        <el-select
                          v-model="row.operator"
                          placeholder="选择运算符"
                          size="small"
                          style="width: 100%"
                          :disabled="!isEditMode"
                        >
                          <el-option label="等于" value="eq"></el-option>
                          <el-option label="不等于" value="neq"></el-option>
                          <el-option label="大于" value="gt"></el-option>
                          <el-option label="大于等于" value="gte"></el-option>
                          <el-option label="小于" value="lt"></el-option>
                          <el-option label="小于等于" value="lte"></el-option>
                          <el-option label="包含" value="contains"></el-option>
                          <el-option label="不包含" value="not_contains"></el-option>
                          <el-option label="在范围内" value="in_range"></el-option>
                          <el-option label="不在范围内" value="not_in_range"></el-option>
                        </el-select>
                      </template>
                    </el-table-column>

                    <el-table-column prop="value" label="条件值">
                      <template slot-scope="{ row, $index }">
                        <div v-if="row.field === 'supplier_rating'">
                          <el-rate
                            v-model="row.value"
                            :max="5"
                            :disabled="!isEditMode"
                          ></el-rate>
                        </div>
                        <div v-else-if="row.operator === 'in_range' || row.operator === 'not_in_range'">
                          <el-input-number
                            v-model="row.minValue"
                            placeholder="最小值"
                            size="small"
                            style="width: 45%"
                            :disabled="!isEditMode"
                          ></el-input-number>
                          <span style="margin: 0 5px">至</span>
                          <el-input-number
                            v-model="row.maxValue"
                            placeholder="最大值"
                            size="small"
                            style="width: 45%"
                            :disabled="!isEditMode"
                          ></el-input-number>
                        </div>
                        <div v-else>
                          <el-input
                            v-model="row.value"
                            placeholder="输入条件值"
                            size="small"
                            :disabled="!isEditMode"
                          ></el-input>
                        </div>
                      </template>
                    </el-table-column>

                    <el-table-column prop="logic" label="逻辑关系" width="120">
                      <template slot-scope="{ row, $index }">
                        <el-select
                          v-model="row.logic"
                          placeholder="选择关系"
                          size="small"
                          style="width: 100%"
                          :disabled="!isEditMode || $index === ruleForm.conditions.length - 1"
                        >
                          <el-option label="并且(AND)" value="and"></el-option>
                          <el-option label="或者(OR)" value="or"></el-option>
                        </el-select>
                      </template>
                    </el-table-column>

                    <el-table-column label="操作" width="80" fixed="right">
                      <template slot-scope="{ row, $index }">
                        <el-button
                          type="danger"
                          icon="el-icon-delete"
                          size="mini"
                          circle
                          @click="removeCondition($index)"
                          :disabled="!isEditMode"
                        ></el-button>
                      </template>
                    </el-table-column>
                  </el-table>

                  <div class="conditions-logic">
                    <span class="logic-label">整体逻辑关系：</span>
                    <el-radio-group v-model="ruleForm.conditionsLogic" :disabled="!isEditMode">
                      <el-radio label="and">所有条件都需要满足 (AND)</el-radio>
                      <el-radio label="or">任一条件满足即可 (OR)</el-radio>
                    </el-radio-group>
                  </div>
                </div>
                <div v-else class="empty-conditions">
                  <el-empty description="暂无条件配置">
                    <el-button
                      type="primary"
                      icon="el-icon-plus"
                      @click="addCondition"
                      :disabled="!isEditMode"
                    >
                      添加第一个条件
                    </el-button>
                  </el-empty>
                </div>
              </div>
            </el-card>

            <!-- 执行统计 -->
            <el-card class="rule-statistics" shadow="never" v-if="!isEditMode">
              <div slot="header" class="card-header">
                <span>执行统计</span>
                <el-button
                  type="text"
                  @click="refreshStatistics"
                  :loading="refreshingStats"
                >
                  <i class="el-icon-refresh"></i> 刷新统计
                </el-button>
              </div>

              <div class="card-body">
                <el-row :gutter="20">
                  <el-col :span="8">
                    <div class="stat-item">
                      <div class="stat-label">总执行次数</div>
                      <div class="stat-value">{{ currentRule.executionCount || 0 }}</div>
                      <div class="stat-trend" v-if="currentRule.lastMonthCount">
                        上月: {{ currentRule.lastMonthCount }}
                      </div>
                    </div>
                  </el-col>

                  <el-col :span="8">
                    <div class="stat-item">
                      <div class="stat-label">执行成功率</div>
                      <div class="stat-value">
                        {{ (currentRule.successRate || 0).toFixed(1) }}%
                      </div>
                      <div class="stat-progress">
                        <el-progress
                          :percentage="currentRule.successRate || 0"
                          :show-text="false"
                          :stroke-width="8"
                          :color="getSuccessRateColor(currentRule.successRate)"
                        ></el-progress>
                      </div>
                    </div>
                  </el-col>

                  <el-col :span="8">
                    <div class="stat-item">
                      <div class="stat-label">最近执行时间</div>
                      <div class="stat-value time-value">
                        {{ formatDateTime(currentRule.lastExecutedAt) || '暂无' }}
                      </div>
                    </div>
                  </el-col>
                </el-row>

                <!-- 执行趋势图表 -->
                <div class="execution-trend" v-if="executionTrend.length > 0">
                  <el-divider content-position="left">执行趋势</el-divider>
                  <div class="trend-chart">
                    <!-- 这里可以集成图表组件 -->
                    <div class="chart-placeholder">
                      <i class="el-icon-data-analysis" style="font-size: 48px; color: #409EFF;"></i>
                      <div>执行趋势图表</div>
                      <div class="chart-tips">可展示近30天规则执行次数趋势</div>
                    </div>
                  </div>
                </div>
              </div>
            </el-card>
          </div>
        </el-tab-pane>

        <!-- 规则测试标签页 -->
        <el-tab-pane label="规则测试" name="ruleTest">
          <div class="rule-test-content">
            <el-card class="test-config" shadow="never">
              <div slot="header" class="card-header">
                <span>测试配置</span>
              </div>

              <div class="card-body">
                <el-form :model="testForm" label-width="120px">
                  <el-row :gutter="20">
                    <el-col :span="12">
                      <el-form-item label="测试规则">
                        <el-select
                          v-model="testForm.ruleId"
                          placeholder="选择要测试的规则"
                          style="width: 100%"
                          @change="handleTestRuleChange"
                        >
                          <el-option
                            v-for="rule in rules"
                            :key="rule.id"
                            :label="rule.name"
                            :value="rule.id"
                          ></el-option>
                        </el-select>
                      </el-form-item>

                      <el-form-item label="测试数据">
                        <el-radio-group v-model="testForm.dataSource">
                          <el-radio label="manual">手动输入</el-radio>
                          <el-radio label="sample">使用样本数据</el-radio>
                          <el-radio label="history">历史报价数据</el-radio>
                        </el-radio-group>
                      </el-form-item>
                    </el-col>

                    <el-col :span="12">
                      <el-form-item label="样本数据">
                        <el-select
                          v-model="testForm.sampleId"
                          placeholder="选择样本数据"
                          style="width: 100%"
                          :disabled="testForm.dataSource !== 'sample'"
                        >
                          <el-option label="样本1: 优质供应商报价" value="sample1"></el-option>
                          <el-option label="样本2: 价格过高报价" value="sample2"></el-option>
                          <el-option label="样本3: 交货周期过长" value="sample3"></el-option>
                          <el-option label="样本4: 新供应商报价" value="sample4"></el-option>
                        </el-select>
                      </el-form-item>

                      <el-form-item label="历史报价">
                        <el-select
                          v-model="testForm.historyQuoteId"
                          placeholder="选择历史报价"
                          style="width: 100%"
                          :disabled="testForm.dataSource !== 'history'"
                          filterable
                          remote
                          :remote-method="searchHistoryQuotes"
                          loading-text="加载中..."
                        >
                          <el-option
                            v-for="quote in historyQuotes"
                            :key="quote.id"
                            :label="`${quote.quoteNumber} - ${quote.productName}`"
                            :value="quote.id"
                          ></el-option>
                        </el-select>
                      </el-form-item>
                    </el-col>
                  </el-row>

                  <!-- 手动输入区域 -->
                  <div v-if="testForm.dataSource === 'manual'" class="manual-input-area">
                    <el-divider content-position="left">手动输入测试数据</el-divider>
                    <el-form-item label="单价">
                      <el-input-number
                        v-model="testForm.manualData.unitPrice"
                        placeholder="输入单价"
                        :precision="2"
                        :min="0"
                        style="width: 200px"
                      ></el-input-number>
                    </el-form-item>

                    <el-form-item label="供应商等级">
                      <el-rate
                        v-model="testForm.manualData.supplierRating"
                        :max="5"
                      ></el-rate>
                    </el-form-item>

                    <el-form-item label="交货周期(天)">
                      <el-input-number
                        v-model="testForm.manualData.deliveryDays"
                        placeholder="交货天数"
                        :min="1"
                        style="width: 200px"
                      ></el-input-number>
                    </el-form-item>

                    <el-form-item label="最小起订量">
                      <el-input-number
                        v-model="testForm.manualData.moq"
                        placeholder="最小起订量"
                        :min="1"
                        style="width: 200px"
                      ></el-input-number>
                    </el-form-item>
                  </div>

                  <el-form-item>
                    <el-button
                      type="primary"
                      icon="el-icon-video-play"
                      @click="runTest"
                      :loading="testing"
                    >
                      执行测试
                    </el-button>
                    <el-button @click="resetTest">重置</el-button>
                  </el-form-item>
                </el-form>
              </div>
            </el-card>

            <!-- 测试结果 -->
            <el-card class="test-result" shadow="never" v-if="testResult">
              <div slot="header" class="card-header">
                <span>测试结果</span>
                <el-tag :type="getTestResultTagType(testResult.result)" size="medium">
                  {{ formatTestResult(testResult.result) }}
                </el-tag>
              </div>

              <div class="card-body">
                <el-alert
                  :title="testResult.message"
                  :type="getTestResultAlertType(testResult.result)"
                  :closable="false"
                  show-icon
                ></el-alert>

                <div class="result-details" v-if="testResult.details">
                  <el-divider content-position="left">详细分析</el-divider>
                  <el-table
                    :data="testResult.details.conditions"
                    border
                    size="small"
                    style="width: 100%"
                  >
                    <el-table-column prop="condition" label="条件" width="300"></el-table-column>
                    <el-table-column prop="actualValue" label="实际值" width="150"></el-table-column>
                    <el-table-column prop="expectedValue" label="期望值" width="150"></el-table-column>
                    <el-table-column label="是否满足" width="100">
                      <template slot-scope="{ row }">
                        <el-tag
                          :type="row.met ? 'success' : 'danger'"
                          size="small"
                        >
                          {{ row.met ? '满足' : '不满足' }}
                        </el-tag>
                      </template>
                    </el-table-column>
                    <el-table-column prop="description" label="说明"></el-table-column>
                  </el-table>

                  <div class="final-action" v-if="testResult.details.action">
                    <el-divider content-position="left">执行动作</el-divider>
                    <el-alert
                      :title="`规则将执行: ${testResult.details.action}`"
                      :type="testResult.result === 'pass' ? 'success' : 'warning'"
                      :closable="false"
                    ></el-alert>
                  </div>
                </div>
              </div>
            </el-card>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 对话框底部 -->
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'AutoReviewRulesDialog',
  props: {
    // 对话框显示控制
    visible: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      // 对话框内部状态
      dialogVisible: false,
      loading: false,
      tableLoading: false,
      saving: false,
      testing: false,
      refreshingStats: false,

      // 标签页
      activeTab: 'ruleList',

      // 规则列表数据
      rules: [],
      filteredRules: [],

      // 筛选表单
      filterForm: {
        ruleName: '',
        status: '',
        ruleType: ''
      },

      // 分页
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },

      // 统计信息
      stats: {
        activeCount: 0,
        inactiveCount: 0,
        draftCount: 0,
        totalExecutions: 0
      },

      // 当前操作的规则
      currentRule: null,
      isEditMode: false,

      // 规则表单
      ruleForm: {
        id: null,
        name: '',
        code: '',
        type: 'price',
        description: '',
        priority: 10,
        status: 'draft',
        scope: ['all'],
        action: 'auto_approve',
        effectiveTime: [],
        conditions: [],
        conditionsLogic: 'and'
      },

      // 规则验证规则
      ruleRules: {
        name: [
          { required: true, message: '请输入规则名称', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        code: [
          { required: true, message: '请输入规则编码', trigger: 'blur' }
        ],
        type: [
          { required: true, message: '请选择规则类型', trigger: 'change' }
        ],
        priority: [
          { required: true, message: '请输入优先级', trigger: 'blur' }
        ]
      },

      // 测试相关
      testForm: {
        ruleId: '',
        dataSource: 'manual',
        sampleId: '',
        historyQuoteId: '',
        manualData: {
          unitPrice: 100,
          supplierRating: 3,
          deliveryDays: 7,
          moq: 100
        }
      },

      testResult: null,
      historyQuotes: [],

      // 执行趋势数据
      executionTrend: []
    }
  },

  computed: {
    // 详情标签页标题
    detailTabLabel() {
      if (!this.currentRule) return '规则详情'
      return this.isEditMode ? '编辑规则' : '规则详情'
    }
  },

  watch: {
    // 监听父组件传递的visible属性
    visible: {
      immediate: true,
      handler(newVal) {
        this.dialogVisible = newVal
      }
    },

    // 监听内部dialogVisible变化，通知父组件
    dialogVisible(newVal) {
      this.$emit('update:visible', newVal)
    }
  },

  methods: {
    // 对话框打开时
    handleDialogOpen() {
      this.activeTab = 'ruleList'
      this.loadRules()
    },

    // 对话框关闭时
    handleDialogClose() {
      this.resetData()
      this.$emit('close')
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
    },

    // 重置数据
    resetData() {
      this.rules = []
      this.filteredRules = []
      this.currentRule = null
      this.isEditMode = false
      this.testResult = null
      this.resetRuleForm()
      this.activeTab = 'ruleList'
    },

    // 加载规则列表
    async loadRules() {
      this.tableLoading = true
      try {
        // 模拟API调用
        await new Promise(resolve => setTimeout(resolve, 500))

        // 模拟数据
        this.rules = [
          {
            id: 1,
            code: 'RULE-2024-001',
            name: '优质供应商自动通过',
            type: 'supplier',
            description: '针对优质供应商的报价自动审核通过',
            priority: 1,
            status: 'active',
            executionCount: 125,
            successRate: 98.5,
            updatedAt: '2024-05-28 14:30:00',
            lastExecutedAt: '2024-05-28 10:15:00',
            lastMonthCount: 89
          },
          {
            id: 2,
            code: 'RULE-2024-002',
            name: '价格过高自动拒绝',
            type: 'price',
            description: '价格超过市场均价20%的报价自动拒绝',
            priority: 2,
            status: 'active',
            executionCount: 67,
            successRate: 92.3,
            updatedAt: '2024-05-27 09:45:00',
            lastExecutedAt: '2024-05-27 16:20:00',
            lastMonthCount: 45
          },
          {
            id: 3,
            code: 'RULE-2024-003',
            name: '紧急交期标记',
            type: 'delivery',
            description: '交货周期超过30天的报价标记为待审核',
            priority: 3,
            status: 'inactive',
            executionCount: 42,
            successRate: 85.7,
            updatedAt: '2024-05-26 11:20:00',
            lastExecutedAt: '2024-05-25 14:30:00',
            lastMonthCount: 32
          },
          {
            id: 4,
            code: 'RULE-2024-004',
            name: '新供应商预警',
            type: 'comprehensive',
            description: '新供应商首次报价发送预警通知',
            priority: 4,
            status: 'draft',
            executionCount: 0,
            successRate: 0,
            updatedAt: '2024-05-28 16:10:00',
            lastExecutedAt: null,
            lastMonthCount: 0
          }
        ]

        this.filteredRules = [...this.rules]
        this.calculateStats()

      } catch (error) {
        this.$message.error('加载规则列表失败：' + (error.message || '未知错误'))
      } finally {
        this.tableLoading = false
      }
    },

    // 计算统计信息
    calculateStats() {
      this.stats = {
        activeCount: this.rules.filter(r => r.status === 'active').length,
        inactiveCount: this.rules.filter(r => r.status === 'inactive').length,
        draftCount: this.rules.filter(r => r.status === 'draft').length,
        totalExecutions: this.rules.reduce((sum, r) => sum + (r.executionCount || 0), 0)
      }
    },

    // 处理搜索
    handleSearch() {
      this.filteredRules = this.rules.filter(rule => {
        // 规则名称筛选
        if (this.filterForm.ruleName &&
          !rule.name.toLowerCase().includes(this.filterForm.ruleName.toLowerCase())) {
          return false
        }

        // 状态筛选
        if (this.filterForm.status && rule.status !== this.filterForm.status) {
          return false
        }

        // 类型筛选
        if (this.filterForm.ruleType && rule.type !== this.filterForm.ruleType) {
          return false
        }

        return true
      })
    },

    // 重置筛选
    resetFilter() {
      this.filterForm = {
        ruleName: '',
        status: '',
        ruleType: ''
      }
      this.filteredRules = [...this.rules]
    },

    // 创建新规则
    handleCreateRule() {
      this.currentRule = null
      this.isEditMode = true
      this.resetRuleForm()
      this.activeTab = 'ruleDetail'
    },

    // 编辑规则
    handleEditRule(rule) {
      this.currentRule = { ...rule }
      this.isEditMode = true
      this.loadRuleFormData(rule)
      this.activeTab = 'ruleDetail'
    },

    // 查看规则详情
    handleViewRule(rule) {
      this.currentRule = { ...rule }
      this.isEditMode = false
      this.loadRuleFormData(rule)
      this.activeTab = 'ruleDetail'
    },

    // 复制规则
    handleDuplicateRule(rule) {
      this.currentRule = null
      this.isEditMode = true
      this.loadRuleFormData(rule)
      // 修改名称和编码
      this.ruleForm.name = `${rule.name} (复制)`
      this.ruleForm.code = ''
      this.ruleForm.status = 'draft'
      this.generateRuleCode()
      this.activeTab = 'ruleDetail'
    },

    // 加载规则表单数据
    loadRuleFormData(rule) {
      this.ruleForm = {
        id: rule.id,
        name: rule.name,
        code: rule.code,
        type: rule.type,
        description: rule.description,
        priority: rule.priority,
        status: rule.status,
        scope: ['all'],
        action: 'auto_approve',
        effectiveTime: [],
        conditions: [
          {
            field: 'unit_price',
            operator: 'lte',
            value: '1000',
            logic: 'and'
          },
          {
            field: 'supplier_rating',
            operator: 'gte',
            value: '4',
            logic: 'and'
          }
        ],
        conditionsLogic: 'and'
      }
    },

    // 重置规则表单
    resetRuleForm() {
      this.ruleForm = {
        id: null,
        name: '',
        code: '',
        type: 'price',
        description: '',
        priority: 10,
        status: 'draft',
        scope: ['all'],
        action: 'auto_approve',
        effectiveTime: [],
        conditions: [],
        conditionsLogic: 'and'
      }

      if (this.$refs.ruleForm) {
        this.$refs.ruleForm.clearValidate()
      }
    },

    // 生成规则编码
    generateRuleCode() {
      const timestamp = new Date().getTime()
      this.ruleForm.code = `RULE-${timestamp}`
    },

    // 启用编辑模式
    enableEditMode() {
      this.isEditMode = true
    },

    // 保存规则
    async handleSaveRule() {
      this.$refs.ruleForm.validate(async (valid) => {
        if (!valid) {
          this.$message.error('请填写完整的规则信息')
          return
        }

        this.saving = true
        try {
          // 模拟API调用
          await new Promise(resolve => setTimeout(resolve, 800))

          if (this.ruleForm.id) {
            // 更新现有规则
            const index = this.rules.findIndex(r => r.id === this.ruleForm.id)
            if (index !== -1) {
              this.rules[index] = {
                ...this.rules[index],
                ...this.ruleForm,
                updatedAt: new Date().toISOString().replace('T', ' ').substring(0, 19)
              }
            }
            this.$message.success('规则更新成功')
          } else {
            // 创建新规则
            const newRule = {
              id: this.rules.length + 1,
              ...this.ruleForm,
              executionCount: 0,
              successRate: 0,
              updatedAt: new Date().toISOString().replace('T', ' ').substring(0, 19),
              lastExecutedAt: null,
              lastMonthCount: 0
            }
            this.rules.unshift(newRule)
            this.$message.success('规则创建成功')
          }

          this.calculateStats()
          this.handleSearch()
          this.isEditMode = false

          if (this.currentRule) {
            this.currentRule = { ...this.ruleForm }
          }

        } catch (error) {
          this.$message.error('保存失败：' + (error.message || '未知错误'))
        } finally {
          this.saving = false
        }
      })
    },

    // 删除规则
    async handleDeleteRule(rule, index) {
      try {
        // 模拟API调用
        await new Promise(resolve => setTimeout(resolve, 300))

        this.rules.splice(index, 1)
        this.filteredRules = this.filteredRules.filter(r => r.id !== rule.id)
        this.calculateStats()

        this.$message.success('规则删除成功')

        // 如果删除的是当前查看的规则，返回规则列表
        if (this.currentRule && this.currentRule.id === rule.id) {
          this.activeTab = 'ruleList'
          this.currentRule = null
        }

      } catch (error) {
        this.$message.error('删除失败：' + error.message)
      }
    },

    // 切换规则状态
    async handleToggleStatus(rule) {
      try {
        // 模拟API调用
        await new Promise(resolve => setTimeout(resolve, 300))

        const index = this.rules.findIndex(r => r.id === rule.id)
        if (index !== -1) {
          this.rules[index].status = rule.status
          this.rules[index].updatedAt = new Date().toISOString().replace('T', ' ').substring(0, 19)
          this.calculateStats()

          this.$message.success(`规则已${rule.status === 'active' ? '启用' : '禁用'}`)
        }

      } catch (error) {
        // 恢复状态
        rule.status = rule.status === 'active' ? 'inactive' : 'active'
        this.$message.error('状态更新失败：' + error.message)
      }
    },

    // 添加条件
    addCondition() {
      this.ruleForm.conditions.push({
        field: '',
        operator: 'eq',
        value: '',
        logic: 'and'
      })
    },

    // 移除条件
    removeCondition(index) {
      this.ruleForm.conditions.splice(index, 1)
    },

    // 字段变更处理
    handleFieldChange(row, index) {
      // 根据字段类型设置默认值
      if (row.field === 'supplier_rating') {
        row.value = 3
      } else if (row.field.includes('price')) {
        row.value = '1000'
      } else if (row.field.includes('rate')) {
        row.value = '0.9'
      } else if (row.field.includes('days')) {
        row.value = '7'
      } else if (row.field.includes('years')) {
        row.value = '1'
      }
    },

    // 刷新规则
    refreshRules() {
      this.loadRules()
      this.$message.success('规则列表已刷新')
    },

    // 刷新统计
    async refreshStatistics() {
      this.refreshingStats = true
      try {
        await new Promise(resolve => setTimeout(resolve, 500))
        // 模拟更新统计
        if (this.currentRule) {
          const index = this.rules.findIndex(r => r.id === this.currentRule.id)
          if (index !== -1) {
            this.currentRule = { ...this.rules[index] }
          }
        }
        this.$message.success('统计信息已刷新')
      } catch (error) {
        this.$message.error('刷新失败：' + error.message)
      } finally {
        this.refreshingStats = false
      }
    },

    // 运行测试
    async runTest() {
      if (!this.testForm.ruleId) {
        this.$message.warning('请选择要测试的规则')
        return
      }

      this.testing = true
      try {
        // 模拟API调用
        await new Promise(resolve => setTimeout(resolve, 1000))

        // 模拟测试结果
        this.testResult = {
          result: Math.random() > 0.5 ? 'pass' : 'fail',
          message: Math.random() > 0.5
            ? '规则测试通过，报价符合自动通过条件'
            : '规则测试未通过，报价需要人工审核',
          details: {
            conditions: [
              {
                condition: '单价 ≤ 1000',
                actualValue: '¥850.00',
                expectedValue: '≤ ¥1000.00',
                met: true,
                description: '价格在合理范围内'
              },
              {
                condition: '供应商等级 ≥ 4',
                actualValue: '4.5 星',
                expectedValue: '≥ 4 星',
                met: true,
                description: '优质供应商'
              },
              {
                condition: '交货周期 ≤ 15天',
                actualValue: '10 天',
                expectedValue: '≤ 15 天',
                met: true,
                description: '交货周期合理'
              }
            ],
            action: '自动通过'
          }
        }

        this.$message.success('测试完成')

      } catch (error) {
        this.$message.error('测试失败：' + error.message)
      } finally {
        this.testing = false
      }
    },

    // 重置测试
    resetTest() {
      this.testForm = {
        ruleId: '',
        dataSource: 'manual',
        sampleId: '',
        historyQuoteId: '',
        manualData: {
          unitPrice: 100,
          supplierRating: 3,
          deliveryDays: 7,
          moq: 100
        }
      }
      this.testResult = null
    },

    // 测试规则变更
    handleTestRuleChange(ruleId) {
      // 加载规则详情
      const rule = this.rules.find(r => r.id === ruleId)
      if (rule) {
        // 可以加载规则详情用于测试
      }
    },

    // 搜索历史报价
    async searchHistoryQuotes(query) {
      if (query) {
        // 模拟搜索
        await new Promise(resolve => setTimeout(resolve, 300))
        this.historyQuotes = [
          { id: 1, quoteNumber: 'QUOTE-2024-001', productName: '无线鼠标' },
          { id: 2, quoteNumber: 'QUOTE-2024-002', productName: '机械键盘' },
          { id: 3, quoteNumber: 'QUOTE-2024-003', productName: '显示器' }
        ].filter(q =>
          q.quoteNumber.includes(query) ||
          q.productName.toLowerCase().includes(query.toLowerCase())
        )
      } else {
        this.historyQuotes = []
      }
    },

    // 分页处理
    handlePageSizeChange(size) {
      this.pagination.size = size
    },

    handlePageChange(page) {
      this.pagination.current = page
    },

    // 表格排序
    handleSortChange({ column, prop, order }) {
      if (!prop || !order) return

      this.filteredRules.sort((a, b) => {
        const aVal = a[prop]
        const bVal = b[prop]

        if (order === 'ascending') {
          return aVal > bVal ? 1 : -1
        } else {
          return aVal < bVal ? 1 : -1
        }
      })
    },

    // 工具方法
    formatRuleType(type) {
      const map = {
        price: '价格规则',
        supplier: '供应商规则',
        delivery: '交货规则',
        comprehensive: '综合规则',
        custom: '自定义规则'
      }
      return map[type] || type
    },

    getRuleTypeTagType(type) {
      const map = {
        price: 'warning',
        supplier: 'success',
        delivery: 'info',
        comprehensive: 'danger',
        custom: ''
      }
      return map[type] || ''
    },

    getPriorityTagType(priority) {
      if (priority <= 3) return 'danger'
      if (priority <= 10) return 'warning'
      return 'info'
    },

    getSuccessRateColor(rate) {
      if (rate >= 95) return '#67C23A'
      if (rate >= 85) return '#E6A23C'
      return '#F56C6C'
    },

    getTestResultTagType(result) {
      return result === 'pass' ? 'success' : 'warning'
    },

    getTestResultAlertType(result) {
      return result === 'pass' ? 'success' : 'warning'
    },

    formatTestResult(result) {
      return result === 'pass' ? '测试通过' : '测试未通过'
    },

    formatDateTime(dateTime) {
      if (!dateTime) return '--'
      return this.$moment ? this.$moment(dateTime).format('YYYY-MM-DD HH:mm') : dateTime
    }
  }
}
</script>

<style scoped>
.auto-review-rules-dialog {
  min-height: 600px;
}

.dialog-content {
  min-height: 500px;
  max-height: 70vh;
  overflow-y: auto;
}

/* 标签页样式 */
.rules-tabs {
  border: none;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

/* 规则列表样式 */
.rule-list-content {
  padding: 10px;
}

.filter-toolbar {
  margin-bottom: 20px;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 4px;
}

.filter-form {
  margin-bottom: 0;
}

.toolbar-actions {
  text-align: right;
  margin-top: 10px;
}

/* 统计卡片 */
.stats-cards {
  margin-bottom: 20px;
}

.stat-card {
  margin-bottom: 0;
}

.stat-content {
  display: flex;
  align-items: center;
  padding: 10px 0;
}

.stat-icon {
  font-size: 32px;
  margin-right: 15px;
  width: 50px;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 22px;
  font-weight: 600;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

/* 规则表格 */
.rules-table-card {
  margin-top: 20px;
}

.rule-name-cell {
  line-height: 1.4;
}

.rule-name {
  font-weight: 500;
  color: #303133;
  margin-bottom: 4px;
}

.rule-code {
  font-size: 12px;
  color: #909399;
  font-family: 'Courier New', monospace;
}

.rule-description {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  line-height: 1.4;
  max-height: 2.8em;
}

.execution-count {
  font-weight: 500;
  color: #409EFF;
}

.success-rate-cell {
  display: flex;
  align-items: center;
  gap: 10px;
}

.rate-text {
  min-width: 40px;
  text-align: right;
  font-weight: 500;
}

.time-cell {
  font-size: 13px;
  color: #606266;
}

.action-buttons {
  display: flex;
  gap: 5px;
  justify-content: center;
}

.pagination-container {
  margin-top: 20px;
  text-align: center;
}

.empty-state {
  padding: 40px 0;
}

/* 规则详情样式 */
.rule-detail-content {
  padding: 10px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-body {
  padding: 15px 0;
}

.form-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}

/* 条件配置 */
.conditions-logic {
  margin-top: 15px;
  padding: 10px;
  background: #f5f7fa;
  border-radius: 4px;
}

.logic-label {
  font-weight: 500;
  color: #303133;
  margin-right: 10px;
}

.empty-conditions {
  padding: 40px 0;
  text-align: center;
}

/* 统计信息 */
.stat-item {
  text-align: center;
  padding: 15px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
}

.time-value {
  font-size: 16px;
}

.stat-trend {
  font-size: 12px;
  color: #909399;
}

.stat-progress {
  margin-top: 8px;
}

.execution-trend {
  margin-top: 20px;
}

.trend-chart {
  height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
  border-radius: 4px;
  margin-top: 10px;
}

.chart-placeholder {
  text-align: center;
  color: #606266;
}

.chart-tips {
  font-size: 12px;
  color: #909399;
  margin-top: 8px;
}

/* 测试配置 */
.rule-test-content {
  padding: 10px;
}

.manual-input-area {
  padding: 15px;
  background: #f5f7fa;
  border-radius: 4px;
  margin-bottom: 15px;
}

/* 测试结果 */
.result-details {
  margin-top: 20px;
}

.final-action {
  margin-top: 20px;
}

/* 对话框底部 */
.dialog-footer {
  padding: 10px 0;
  border-top: 1px solid #ebeef5;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .auto-review-rules-dialog {
    width: 95% !important;
  }
}

@media (max-width: 768px) {
  .auto-review-rules-dialog {
    width: 98% !important;
    top: 2vh !important;
  }

  .filter-toolbar {
    padding: 10px;
  }

  .filter-form .el-form-item {
    margin-bottom: 10px;
  }

  .stats-cards .el-col {
    margin-bottom: 10px;
  }

  .card-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .card-header > * {
    margin-bottom: 10px;
  }
}
</style>
