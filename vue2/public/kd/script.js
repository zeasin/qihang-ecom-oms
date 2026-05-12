// API配置
const API_CONFIG = {
    url: 'https://eolink.o.apispace.com/wlgj1/paidtobuy_api/trace_search',
    token: '3h8rhk2s4ruie4bh2tx2j5wrsxzhkyvl'
};

// 快递公司映射
const COMPANY_MAP = {
    'SF': '顺丰快递',
    'YTO': '圆通快递',
    'ZTO': '中通快递',
    'YUNDA': '韵达快递',
    'YDKY': '韵达快运',
    'STO': '申通快递',
    'TTKDEX': '天天快递',
    'JD': '京东快递',
    'EMS': '中国邮政快递'
};

// 物流状态映射
const STATUS_MAP = {
    'ACCEPT': '已揽收',
    'TRANSPORT': '运输中',
    'DELIVERING': '派送中',
    'SIGN': '已签收',
    'PROBLEM': '问题件',
    'REJECT': '拒收',
    'TIMEOUT': '超时'
};

// 状态描述映射
const STATUS_DESC_MAP = {
    '已揽收': 'ACCEPT',
    '运输中': 'TRANSPORT',
    '派送中': 'DELIVERING',
    '已签收': 'SIGN',
    '问题件': 'PROBLEM',
    '拒收': 'REJECT',
    '超时': 'TIMEOUT'
};

/**
 * 保存查询历史到本地存储
 */
function saveQueryHistory(cpCode, mailNo, tel, orderType) {
    const queryData = {
        cpCode: cpCode,
        mailNo: mailNo,
        tel: tel,
        orderType: orderType,
        timestamp: new Date().getTime()
    };
    localStorage.setItem('expressQueryHistory', JSON.stringify(queryData));
}

/**
 * 清除查询历史
 */
function clearQueryHistory() {
    try {
        localStorage.removeItem('expressQueryHistory');
        console.log('已清除查询历史');
        
        // 清空表单
        document.getElementById('cpCode').value = '';
        document.getElementById('mailNo').value = '';
        document.getElementById('tel').value = '';
        document.getElementById('orderType').value = 'asc';
        document.getElementById('mailNo').placeholder = '请输入快递单号';
        
        // 隐藏结果区域
        hideResult();
        hideError();
        
    } catch (error) {
        console.error('清除查询历史失败:', error);
    }
}

/**
 * 从本地存储恢复查询历史
 */
function restoreQueryHistory() {
    try {
        const stored = localStorage.getItem('expressQueryHistory');
        if (stored) {
            const queryData = JSON.parse(stored);
            
            // 设置恢复历史标志，防止清空单号
            window.isRestoringHistory = true;
            
            // 恢复表单数据
            if (queryData.cpCode) {
                document.getElementById('cpCode').value = queryData.cpCode;
            }
            if (queryData.mailNo) {
                document.getElementById('mailNo').value = queryData.mailNo;
            }
            if (queryData.tel) {
                document.getElementById('tel').value = queryData.tel;
            }
            if (queryData.orderType) {
                document.getElementById('orderType').value = queryData.orderType;
            }
            
            // 触发change事件以更新placeholder（在设置完所有值后）
            if (queryData.cpCode) {
                document.getElementById('cpCode').dispatchEvent(new Event('change'));
            }
            
            // 重置标志
            setTimeout(() => {
                window.isRestoringHistory = false;
            }, 100);
            
            console.log('已恢复查询历史:', queryData);
        }
    } catch (error) {
        console.error('恢复查询历史失败:', error);
    }
}

/**
 * 查询快递信息
 */
async function queryExpress() {
    // 获取表单数据
    const cpCode = document.getElementById('cpCode').value;
    const mailNo = document.getElementById('mailNo').value.trim();
    const tel = document.getElementById('tel').value.trim();
    const orderType = document.getElementById('orderType').value;

    // 验证必填字段
    if (!cpCode) {
        showError('请选择快递公司');
        return;
    }

    if (!mailNo) {
        showError('请输入快递单号');
        return;
    }

    // 验证快递单号格式（基本验证）
    if (mailNo.length < 8) {
        showError('快递单号格式不正确');
        return;
    }

    // 显示加载状态
    showLoading();
    hideError();
    hideResult();

    try {
        // 构建请求数据
        const requestData = {
            cpCode: cpCode,
            mailNo: mailNo,
            orderType: orderType
        };

        // 如果输入了手机号，添加到请求中
        if (tel) {
            requestData.tel = tel;
        }

        console.log('发送请求:', requestData);

        // 发送API请求
        const response = await fetch(API_CONFIG.url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-APISpace-Token': API_CONFIG.token
            },
            body: JSON.stringify(requestData)
        });

        console.log('响应状态:', response.status);

        if (!response.ok) {
            throw new Error(`请求失败: ${response.status} ${response.statusText}`);
        }

        const data = await response.json();
        console.log('响应数据:', data);

        // 隐藏加载状态
        hideLoading();

        if (data.success && data.logisticsTrace) {
            // 保存查询历史
            saveQueryHistory(cpCode, mailNo, tel, orderType);
            
            // 显示查询结果
            displayResult(data.logisticsTrace);
        } else {
            showError(data.message || '查询失败，请检查快递单号是否正确');
        }

    } catch (error) {
        console.error('查询错误:', error);
        hideLoading();
        
        if (error.name === 'TypeError' && error.message.includes('fetch')) {
            showError('网络连接失败，请检查网络连接后重试');
        } else if (error.message.includes('CORS')) {
            showError('跨域请求被阻止，请联系管理员配置服务器');
        } else {
            showError(`查询失败: ${error.message}`);
        }
    }
}

/**
 * 显示查询结果
 * @param {Object} logisticsTrace 物流信息
 */
function displayResult(logisticsTrace) {
    const resultSection = document.getElementById('resultSection');
    const companyName = document.getElementById('companyName');
    const expressNo = document.getElementById('expressNo');
    const expressStatus = document.getElementById('expressStatus');
    const lastUpdate = document.getElementById('lastUpdate');
    const timeline = document.getElementById('timeline');

    // 设置基本信息
    companyName.textContent = logisticsTrace.logisticsCompanyName || COMPANY_MAP[logisticsTrace.cpCode] || '未知快递公司';
    expressNo.textContent = `运单号: ${logisticsTrace.mailNo}`;
    
    // 设置状态
    const statusText = logisticsTrace.logisticsStatusDesc || STATUS_MAP[logisticsTrace.logisticsStatus] || '未知状态';
    expressStatus.textContent = statusText;
    expressStatus.className = `express-status ${STATUS_DESC_MAP[statusText] || logisticsTrace.logisticsStatus || 'UNKNOWN'}`;

    // 设置最后更新时间
    if (logisticsTrace.theLastTime) {
        lastUpdate.textContent = `最后更新: ${logisticsTrace.theLastTime}`;
    }

    // 生成时间轴
    generateTimeline(logisticsTrace.logisticsTraceDetailList || [], timeline);

    // 显示结果区域
    resultSection.style.display = 'block';
    
    // 平滑滚动到结果区域
    resultSection.scrollIntoView({ 
        behavior: 'smooth',
        block: 'start'
    });
}

/**
 * 生成时间轴
 * @param {Array} traceList 物流轨迹列表
 * @param {HTMLElement} container 容器元素
 */
function generateTimeline(traceList, container) {
    container.innerHTML = '';

    if (!traceList || traceList.length === 0) {
        container.innerHTML = '<div class="timeline-item"><div class="timeline-desc">暂无物流信息</div></div>';
        return;
    }

    traceList.forEach((trace, index) => {
        const timelineItem = document.createElement('div');
        timelineItem.className = 'timeline-item';
        
        // 标记最新的一条记录
        if (index === 0) {
            timelineItem.classList.add('latest');
        }

        // 格式化时间
        let timeStr = '';
        if (trace.time) {
            const date = new Date(trace.time);
            timeStr = formatDateTime(date);
        }

        // 构建HTML
        timelineItem.innerHTML = `
            ${timeStr ? `<div class="timeline-time">${timeStr}</div>` : ''}
            ${trace.areaName ? `<div class="timeline-location"><i class="fas fa-map-marker-alt"></i> ${trace.areaName}</div>` : ''}
            <div class="timeline-desc">${trace.desc || '无描述信息'}</div>
        `;

        container.appendChild(timelineItem);
    });
}

/**
 * 格式化日期时间
 * @param {Date} date 日期对象
 * @returns {string} 格式化后的日期时间字符串
 */
function formatDateTime(date) {
    if (!date || isNaN(date.getTime())) {
        return '';
    }

    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');

    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

/**
 * 显示加载状态
 */
function showLoading() {
    document.getElementById('loading').style.display = 'block';
}

/**
 * 隐藏加载状态
 */
function hideLoading() {
    document.getElementById('loading').style.display = 'none';
}

/**
 * 显示错误信息
 * @param {string} message 错误消息
 */
function showError(message) {
    const errorMessage = document.getElementById('errorMessage');
    const errorText = document.getElementById('errorText');
    errorText.textContent = message;
    errorMessage.style.display = 'flex';
    
    // 平滑滚动到错误信息
    errorMessage.scrollIntoView({ 
        behavior: 'smooth',
        block: 'center'
    });
}

/**
 * 隐藏错误信息
 */
function hideError() {
    document.getElementById('errorMessage').style.display = 'none';
}

/**
 * 隐藏查询结果
 */
function hideResult() {
    document.getElementById('resultSection').style.display = 'none';
}

/**
 * 表单验证和用户体验增强
 */
document.addEventListener('DOMContentLoaded', function() {
    const mailNoInput = document.getElementById('mailNo');
    const telInput = document.getElementById('tel');
    const cpCodeSelect = document.getElementById('cpCode');
    
    // 页面加载时恢复查询历史
    restoreQueryHistory();

    // 快递单号输入限制
    mailNoInput.addEventListener('input', function(e) {
        // 移除非数字和字母的字符
        let value = e.target.value.replace(/[^a-zA-Z0-9]/g, '');
        e.target.value = value.toUpperCase();
    });

    // 手机号输入限制
    telInput.addEventListener('input', function(e) {
        // 只允许数字
        let value = e.target.value.replace(/[^0-9]/g, '');
        e.target.value = value;
    });

    // 回车键查询
    document.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            queryExpress();
        }
    });

    // 快递公司选择时的自动建议和清空所有数据
    cpCodeSelect.addEventListener('change', function(e) {
        const selectedCode = e.target.value;
        
        // 只有在用户手动切换时才清空数据（不是在恢复历史记录时）
        if (!window.isRestoringHistory) {
            // 清空所有输入字段
            mailNoInput.value = '';
            telInput.value = '';
            
            // 隐藏之前的查询结果和错误信息
            hideResult();
            hideError();
            
            console.log('切换快递公司，已清空所有数据');
        }
        
        if (selectedCode) {
            // 根据选择的快递公司给出运单号格式提示
            const suggestions = {
                'SF': '请输入顺丰运单号（如：SF1234567890123）',
                'YTO': '请输入圆通运单号（如：YT1234567890123）',
                'ZTO': '请输入中通运单号（如：73570889383613）',
                'YUNDA': '请输入韵达运单号（如：4310123456789）',
                'YDKY': '请输入韵达快运单号（如：YDKY1234567890）',
                'STO': '请输入申通运单号（如：STO1234567890123）',
                'TTKDEX': '请输入天天快递运单号（如：TT1234567890）',
                'JD': '请输入京东快递运单号（如：JD1234567890123）',
                'EMS': '请输入邮政运单号（如：EA123456789CN）'
            };
            
            if (suggestions[selectedCode]) {
                mailNoInput.placeholder = suggestions[selectedCode];
            } else {
                mailNoInput.placeholder = '请输入快递单号';
            }
        } else {
            mailNoInput.placeholder = '请输入快递单号';
        }
    });
});

// 暴露全局函数
window.queryExpress = queryExpress;
window.clearQueryHistory = clearQueryHistory;
