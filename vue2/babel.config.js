module.exports = {
  presets: [
    ['@vue/cli-plugin-babel/preset', {
      useBuiltIns: 'usage',
      corejs: 3,
      modules: 'commonjs'
    }]
  ],
  plugins: [
    'add-module-exports',
    ['@babel/plugin-transform-runtime', {
      corejs: false,
      helpers: true,
      regenerator: true,
      useESModules: false
    }]
  ],
  'env': {
    'development': {
      'plugins': ['dynamic-import-node']
    }
  }
}