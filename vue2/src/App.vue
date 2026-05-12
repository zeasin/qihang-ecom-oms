<template>
  <div id="app">
    <router-view />
    <theme-picker />
  </div>
</template>

<script>
import ThemePicker from "@/components/ThemePicker";
import { v4 as uuidv4 } from 'uuid';
export default {
  name: "App",
  components: { ThemePicker },
  metaInfo() {
        return {
            title: this.$store.state.settings.dynamicTitle && this.$store.state.settings.title,
            titleTemplate: title => {
                return title ? `${title} - ${process.env.VUE_APP_TITLE}` : process.env.VUE_APP_TITLE
            }
        }
  },
  data() {
    return {
      // SSE 相关数据
      eventSource: null,
      sseUrl: process.env.VUE_APP_SSE_URL || process.env.VUE_APP_BASE_API+'/api/erp-api/sse/notify_msg', // 优先使用环境变量
      heartbeatTimeout: null,   // 存储心跳超时定时器 ID
      // 心跳间隔 (毫秒) - 应略大于后端实际发送心跳的间隔
      // 例如，后端每 30 秒发一次，前端可设为 40-45 秒
      heartbeatInterval: 45000,
      reconnectTimeout: null,   // 存储重连定时器 ID
      reconnectDelay: 5000,     // 重连延迟 (毫秒) 5 秒
      reconnectAttempts: 0,     // 当前重连尝试次数
      maxReconnectAttempts: 5,  // 最大重连尝试次数
    };
  },
  methods: {
    generateClientId() {
      // uuidv4() 会返回一个类似 '1b9d6bcd-bbfd-4b2d-9b5d-ab8dfbbd4bed' 的字符串
      const id = `client_${uuidv4()}`; // 可以加个前缀方便识别
      console.log('[App SSE] Generated UUID Client ID:', id);
      return id;
    },
    // 3. 修改或保留的方法：获取或生成客户端 ID
    generateOrGetClientId() {
      // 尝试从 localStorage 获取已存在的 ID
      let id = localStorage.getItem('app_sse_client_id');

      if (!id) {
        // 如果没有，则生成一个新的 UUID
        id = this.generateClientId();
        // 将新生成的 ID 保存到 localStorage 以便下次复用
        localStorage.setItem('app_sse_client_id', id);
        console.log('[App SSE] New UUID generated and saved:', id);
      } else {
        console.log('[App SSE] Retrieved existing UUID from storage:', id);
      }
      return id;
    },
    // 初始化 SSE 连接
    initSSE() {
      // 确保有 Client ID
      if (!this.clientId) {
        this.clientId = this.generateOrGetClientId();
      }
      // 防止重复连接
      if (this.eventSource) {
        console.warn('[App SSE] SSE already initialized.');
        return;
      }

      if (!this.sseUrl) {
        console.error('[App SSE] No SSE URL configured.');
        return;
      }

      // 构建带 clientId 查询参数的完整 URL
      const fullSseUrl = `${this.sseUrl}?clientId=${encodeURIComponent(this.clientId)}`;
      console.log(`[App SSE] Connecting to ${fullSseUrl}...`);

      this.eventSource = new EventSource(fullSseUrl);

      this.eventSource.onopen = () => {
        console.log('[App SSE] Connected successfully.');
        // 可选：重置重连尝试计数器
        // this.reconnectAttempts = 0;
        this.reconnectAttempts = 0; // 连接成功，重置重试次数
        this.startHeartbeatTimeout(); // 连接成功后启动心跳超时检测
      };

      // *** 修改部分 ***
      this.eventSource.onerror = (err) => {
        console.error('[App SSE] Connection error occurred. Browser may attempt auto-reconnect.', err);
        // 移除这行：this.closeSSE(); // 不要主动关闭，让浏览器自动重连

        // 如果你需要在错误时执行额外逻辑（例如，触发 UI 更新提示用户，或者使用自定义的重连策略而不是 scheduleReconnect）
        // 可以放在这里，但通常不需要立即 close()

        // 如果你有自定义的重连调度逻辑（例如，带指数退避），并且想在浏览器默认重连失败后介入，
        // 你可能需要更复杂的逻辑，但这对于基本用法来说可能不是必需的。
        // this.scheduleReconnect(); // 通常不需要立即调度，除非你想覆盖浏览器默认行为或添加特殊逻辑
         // 停止当前的心跳检测，因为我们不确定连接状态
        this.stopHeartbeatTimeout();

        // 安排重连。注意：浏览器会自动重连，但我们也可以添加自己的逻辑
        // 这里的逻辑会在每次 onerror 时都尝试安排一次重连
        // 可能导致多次重连尝试，但会被 reconnectAttempts 限制
        this.scheduleReconnect();
      };

      // 确保后端会定期发送名为 'heartbeat' 的事件
      this.eventSource.addEventListener('heartbeat', (event) => {
        // 可以根据需要处理心跳数据，例如 event.data 可能包含服务器时间戳
        // console.debug('[App SSE] Received heartbeat data:', event.data);
        console.debug('[App SSE] Received heartbeat.');
        this.resetHeartbeatTimeout(); // 收到心跳，重置心跳超时
        // 通常心跳消息不需要特殊处理，或者可以用来更新 UI 状态指示器
      });
      // 监听后端发送的 'NewOrder' 事件
      this.eventSource.addEventListener('NewOrder', (event) => {
        console.log('[App SSE] Received NewOrder event:', event.data);
        // 收到消息后，立即弹出 Element UI 通知
        this.showNotificationPopup(event.data);
      });

      // 如果有其他事件类型，可以继续添加监听器
      // this.eventSource.addEventListener('systemAlert', (event) => { ... });
    },
    // --- 新增：启动心跳超时定时器 ---
    startHeartbeatTimeout() {
      this.stopHeartbeatTimeout(); // 确保定时器唯一
      this.heartbeatTimeout = setTimeout(() => {
        console.warn(`[App SSE] No message received within ${this.heartbeatInterval / 1000} seconds. Assuming connection lost.`);
        this.handleHeartbeatTimeout(); // 处理超时
      }, this.heartbeatInterval);
    },

    // --- 新增：重置心跳超时定时器 ---
    resetHeartbeatTimeout() {
      // 收到任何消息（心跳或业务消息）时调用此方法
      this.startHeartbeatTimeout(); // 清除并重新设置
    },

    // --- 新增：停止心跳超时定时器 ---
    stopHeartbeatTimeout() {
      if (this.heartbeatTimeout) {
        clearTimeout(this.heartbeatTimeout);
        this.heartbeatTimeout = null;
      }
    },

    // --- 新增：处理心跳超时 ---
    handleHeartbeatTimeout() {
      console.log('[App SSE] Heartbeat timeout detected. Closing connection and scheduling reconnect.');
      this.closeSSE(); // 主动关闭当前可能僵死的连接
      this.scheduleReconnect(); // 安排重连
    },

    // --- 修改或确认：安排重连 ---
    scheduleReconnect() {
      // 避免累积多个重连定时器
      this.cancelScheduledReconnect();

      if (this.reconnectAttempts >= this.maxReconnectAttempts) {
         console.error(`[App SSE] Maximum reconnection attempts (${this.maxReconnectAttempts}) reached. Giving up.`);
         // 可以在这里触发 UI 提示，告知用户连接失败
         return;
      }

      this.reconnectAttempts++;
      const delay = this.reconnectDelay; // 可以加入指数退避逻辑
      console.log(`[App SSE] Scheduling reconnect attempt #${this.reconnectAttempts} in ${delay / 1000} seconds...`);

      this.reconnectTimeout = setTimeout(() => {
        // 在 Vue 2 中，组件销毁后 this 可能仍然存在引用一段时间
        // 最稳妥的方式是在 beforeDestroy 中清理资源
        // 这里再简单检查一下 eventSource 是否还存在，作为一种辅助手段
        if (this._isDestroyed || !this.$el) { // Vue 2 特有的属性 _isDestroyed 和 $el
           console.log("[App SSE] Component seems destroyed, not attempting reconnect.");
           return;
        }
        this.initSSE(); // 尝试重新初始化连接

      }, delay);
    },

    // --- 新增：取消已安排的重连 ---
    cancelScheduledReconnect() {
       if (this.reconnectTimeout) {
          clearTimeout(this.reconnectTimeout);
          this.reconnectTimeout = null;
       }
    },

    // --- 修改或确认：关闭 SSE 连接 ---
    closeSSE() {
      if (this.eventSource) {
        this.stopHeartbeatTimeout(); // 关闭时停止心跳检测
        this.cancelScheduledReconnect(); // 关闭时取消可能的重连计划
        this.eventSource.close();
        this.eventSource = null;
        console.log('[App SSE] Connection closed.');
      }
    },
    // 显示 Element UI 通知
    showNotificationPopup(message) {
      try {
        const parsedData = JSON.parse(message);
        // 使用 Element UI 的 $notify 方法
        this.$notify({
          title: '来新订单了！', // 通知标题
          message: parsedData.shopType+'订单：'+parsedData.orderNum,               // 通知内容
          type: 'success',                // 通知类型 (success/info/warning/error)
          duration: 5000,                 // 显示时长 (毫秒)，0 表示不自动关闭
          position: 'top-right'        // 显示位置
        });
      } catch (e) {
        console.error('[App SSE] Failed to parse NewOrder event data:', e);

        // 使用 Element UI 的 $notify 方法
        this.$notify({
          title: 'New Order Notification', // 通知标题
          message: message,               // 通知内容
          type: 'success',                // 通知类型 (success/info/warning/error)
          duration: 5000,                 // 显示时长 (毫秒)，0 表示不自动关闭
          position: 'top-right'        // 显示位置
      });
      }

    }
  },
  mounted() {
    // 组件挂载完成后初始化 SSE 连接
    this.initSSE();
  },
  beforeDestroy() {
    // 组件销毁前关闭 SSE 连接，防止内存泄漏
    this.closeSSE();
  }

};
</script>
<style scoped>
#app .theme-picker {
  display: none;
}
</style>
