/**
 * 限制小数位数
 * @param {number|string} value - 输入值
 * @param {number} decimalPlaces - 小数位数，默认2位
 * @returns {string} - 限制后的字符串值
 */
export function limitDecimalLength(value, decimalPlaces = 2) {
  if (value === null || value === undefined || value === '') {
    return '';
  }
  
  // 转换为字符串
  let strValue = String(value);
  
  // 处理空字符串
  if (strValue === '') {
    return '';
  }
  
  // 处理只包含小数点的情况
  if (strValue === '.') {
    return '0.';
  }
  
  // 移除多余的小数点
  const parts = strValue.split('.');
  if (parts.length > 2) {
    strValue = parts[0] + '.' + parts.slice(1).join('');
  }
  
  // 限制小数位数
  if (parts.length === 2 && parts[1].length > decimalPlaces) {
    strValue = parseFloat(strValue).toFixed(decimalPlaces);
  }
  
  return strValue;
}

/**
 * 将数字转换为字符串，保留指定小数位数
 * @param {number} value - 数字值
 * @param {number} decimalPlaces - 小数位数，默认2位
 * @returns {string} - 转换后的字符串
 */
export function numberToString(value, decimalPlaces = 2) {
  if (value === null || value === undefined) {
    return '';
  }
  return parseFloat(value).toFixed(decimalPlaces);
}

/**
 * 将字符串转换为数字
 * @param {string} value - 字符串值
 * @returns {number} - 转换后的数字
 */
export function stringToNumber(value) {
  if (value === null || value === undefined || value === '') {
    return 0;
  }
  return parseFloat(value) || 0;
}
