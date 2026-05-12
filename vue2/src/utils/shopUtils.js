// shopUtils.js

import axios from 'axios'
import { listShop } from '@/api/shop/shop'

export async function getShopListData() {
  let shopList = JSON.parse(sessionStorage.getItem('shopList'));

  if (!shopList) {
    try {
      const res = await fetchShopListFromServer(); // 这里是你请求服务器的逻辑

      shopList = res; // 假设你从服务器获得的是 { data: [shopList] }
      sessionStorage.setItem('shopList', JSON.stringify(shopList)); // 存入缓存
    } catch (error) {
      console.error('Error fetching shop list:', error);
      shopList = []; // 如果请求失败，返回空数组或其他默认值
    }
  }

  return shopList;
}

async function fetchShopListFromServer() {
  // 这里实现你从服务器获取 shopList 的逻辑
  // 比如：使用 axios 或 fetch 请求数据
  const response = await listShop();
  return response.rows;
}
