# 使用官方 Nginx 镜像作为基础镜像
#FROM nginx:alpine
FROM nginx:1.24-alpine
COPY ./cert /etc/nginx/cert

# 删除默认的 Nginx 配置文件
RUN rm /etc/nginx/conf.d/default.conf

# 将自定义的 nginx.conf 配置文件复制到容器中的 Nginx 配置目录
COPY ./nginx.conf /etc/nginx/nginx.conf

# 将 Vue 项目的构建文件复制到 Nginx 的 html 目录
COPY ./dist /usr/share/nginx/html

# 暴露容器的端口（80 是 Nginx 默认的 HTTP 端口）
EXPOSE 88

# 启动 Nginx 服务
CMD ["nginx", "-g", "daemon off;"]
