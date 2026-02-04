import request from '@/utils/request'

// 更新token
export function updateDouToken(data) {
  return request({
    url: '/dou/oauth/update_token',
    method: 'post',
    data: data
  })
}



