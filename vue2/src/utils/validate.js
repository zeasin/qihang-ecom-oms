/**
 * @param {string} path
 * @returns {Boolean}
 */
export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validUsername(str) {
  const valid_map = ['admin', 'editor']
  return valid_map.indexOf(str.trim()) >= 0
}

/**
 * @param {string} url
 * @returns {Boolean}
 */
export function validURL(url) {
  const reg = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/
  return reg.test(url)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validLowerCase(str) {
  const reg = /^[a-z]+$/
  return reg.test(str)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validUpperCase(str) {
  const reg = /^[A-Z]+$/
  return reg.test(str)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validAlphabets(str) {
  const reg = /^[A-Za-z]+$/
  return reg.test(str)
}

/**
 * @param {string} email
 * @returns {Boolean}
 */
export function validEmail(email) {
  const reg = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
  return reg.test(email)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function isString(str) {
  if (typeof str === 'string' || str instanceof String) {
    return true
  }
  return false
}

/**
 * @param {Array} arg
 * @returns {Boolean}
 */
export function isArray(arg) {
  if (typeof Array.isArray === 'undefined') {
    return Object.prototype.toString.call(arg) === '[object Array]'
  }
  return Array.isArray(arg)
}

// 密码验证方法
export function validatePassword(password,username) {
  console.log(password); // 查看传入的参数
  console.log(typeof password); // 查看类型
  const pwd = password+'';
  const uname = username+''
  // this.passwordError = ""; // 清除之前的错误信息
  // 1. 检查密码长度
  if (password.length < 8 || password.length > 32) {
    // this.passwordError = "密码长度必须在 8 到 32 个字符之间";
    // return false;
    return {
      result:false,
      msg:"密码长度必须在 8 到 32 个字符之间"
    }
  }

  // 2. 检查密码是否包含大小写字母、数字和特殊字符
  const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*(),.?":{}|<>]).+$/;
  if (!regex.test(password)) {
    // this.passwordError = "密码必须包含大小写字母、数字和特殊字符";
    // return false;
    return {
      result:false,
      msg:"密码必须包含大小写字母、数字和特殊字符"
    }
  }

  // 3. 禁止连续或重复字符
  if (hasSequentialOrRepeatedChars(password)) {
    // this.passwordError = "密码不能包含连续或重复字符";
    // return false;
    return {
      result:false,
      msg:"密码不能包含连续或重复字符"
    }
  }

  // 4. 密码不能与用户名相同

  if ( pwd.toLowerCase() === uname.toLowerCase()) {
    // this.passwordError = "密码不能与用户名相同";
    // return false;
    return {
      result:false,
      msg:"密码不能与用户名相同"
    }
  }

  // 密码符合要求
  // return true;
  return {
    result:true,
    msg:"SUCCESS"
  }
}

// 检查密码是否包含连续或重复字符
export function hasSequentialOrRepeatedChars(password) {
  // 检查连续数字或字母
  for (let i = 0; i < password.length - 2; i++) {
    const current = password.charAt(i);
    const next = password.charAt(i + 1);
    const nextNext = password.charAt(i + 2);
    if (next === current + 1 && nextNext === current + 2) {
      return true;
    }
  }

  // 检查重复字符
  for (let i = 0; i < password.length - 1; i++) {
    if (password.charAt(i) === password.charAt(i + 1)) {
      return true;
    }
  }

  return false;
}
