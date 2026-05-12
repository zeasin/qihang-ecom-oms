'use strict'
const path = require('path')
const webpack = require('webpack')
const NodePolyfillPlugin = require('node-polyfill-webpack-plugin') // 新增

function resolve(dir) {
  return path.join(__dirname, dir)
}

const CompressionPlugin = require('compression-webpack-plugin')

const name = process.env.VUE_APP_TITLE || '启航智云电商ERP系统'

const port = process.env.port || process.env.npm_config_port || 88

module.exports = {
  publicPath: process.env.NODE_ENV === "production" ? "/" : "/",
  outputDir: 'dist',
  assetsDir: 'static',
  lintOnSave: process.env.NODE_ENV === 'development',
  productionSourceMap: false,
  transpileDependencies: ['vant', '@babel/runtime'],
  devServer: {
    host: '0.0.0.0',
    port: port,
    open: true,
    client: {
      overlay: false
    },
    proxy: {
      [process.env.VUE_APP_BASE_API]: {
        target: `http://localhost:8086`,
        changeOrigin: true,
        pathRewrite: {
          ['^' + process.env.VUE_APP_BASE_API]: ''
        }
      }
    },
    historyApiFallback: true,
    allowedHosts: 'all'
  },
  css: {
    loaderOptions: {
      sass: {
        sassOptions: { outputStyle: "expanded" }
      }
    }
  },
  configureWebpack: {
    name: name,
    resolve: {
      alias: {
        '@': resolve('src')
      },
      fallback: {
        "path": require.resolve("path-browserify"),
        "util": require.resolve("util/"),
        "buffer": require.resolve("buffer/"),
        "assert": require.resolve("assert/")
        // process 由 NodePolyfillPlugin 提供，无需再配置
      }
    },
    plugins: [
      new NodePolyfillPlugin(), // 新增：自动注入 process, Buffer 等 polyfill
      new CompressionPlugin({
        test: /\.(js|css|html)$/i,
        filename: '[path][base].gz',
        algorithm: 'gzip',
        minRatio: 0.8
      }),
      new webpack.NormalModuleReplacementPlugin(
          /@babel\/runtime\/helpers\/esm\//,
          resource => {
            resource.request = resource.request.replace(/@babel\/runtime\/helpers\/esm\//, '@babel/runtime/helpers/')
          }
      ),
      // 修正：只定义 process.env，保留 process 的其他方法（如 cwd）
      new webpack.DefinePlugin({
        'process.env': JSON.stringify({
          NODE_ENV: process.env.NODE_ENV,
          VUE_APP_BASE_API: process.env.VUE_APP_BASE_API,
          VUE_APP_TITLE: process.env.VUE_APP_TITLE,
          VUE_APP_SSE_URL: process.env.VUE_APP_SSE_URL
        })
      })
    ],
    module: {
      rules: [
        {
          test: /\.m?js$/,
          resolve: {
            fullySpecified: false
          }
        }
      ]
    }
  },
  chainWebpack(config) {
    config.plugins.delete('preload')
    config.plugins.delete('prefetch')

    config.module
        .rule('vant')
        .test(/vant\/es\//)
        .use('babel-loader')
        .loader('babel-loader')
        .options({
          presets: [
            ['@babel/preset-env', {
              modules: 'commonjs'
            }]
          ]
        })
        .end()

    config.module
        .rule('svg')
        .exclude.add(resolve('src/assets/icons'))
        .end()
    config.module
        .rule('icons')
        .test(/\.svg$/)
        .include.add(resolve('src/assets/icons'))
        .end()
        .use('svg-sprite-loader')
        .loader('svg-sprite-loader')
        .options({
          symbolId: 'icon-[name]'
        })
        .end()

    config.when(process.env.NODE_ENV !== 'development', config => {
      config.optimization.splitChunks({
        chunks: 'all',
        cacheGroups: {
          libs: {
            name: 'chunk-libs',
            test: /[\\/]node_modules[\\/]/,
            priority: 10,
            chunks: 'initial'
          },
          elementUI: {
            name: 'chunk-elementUI',
            test: /[\\/]node_modules[\\/]_?element-ui(.*)/,
            priority: 20
          },
          commons: {
            name: 'chunk-commons',
            test: resolve('src/components'),
            minChunks: 3,
            priority: 5,
            reuseExistingChunk: true
          }
        }
      })

      config.optimization.runtimeChunk('single')
    })
  }
}