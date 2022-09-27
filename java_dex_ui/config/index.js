// see http://vuejs-templates.github.io/webpack for documentation.
var path = require('path')

module.exports = {
  build: {
    env: require('./prod.env'),
    index: path.resolve(__dirname, '../manage/index.html'),
    assetsRoot: path.resolve(__dirname, '../manage'),
    assetsSubDirectory: 'static',
    assetsPublicPath: '/manage/',
    productionSourceMap: false,
    // Gzip off by default as many popular static hosts such as
    // Surge or Netlify already gzip all static assets for you.
    // Before setting to `true`, make sure to:
    // npm install --save-dev compression-webpack-plugin
    productionGzip: false,
    productionGzipExtensions: ['js', 'css'],
    // Run the build command with an extra argument to
    // View the bundle analyzer report after build finishes:
    // `npm run build --report`
    // Set to `true` or `false` to always turn it on or off
    bundleAnalyzerReport: process.env.npm_config_report
  },
  dev: {
    env: require('./dev.env'),
    port: 8002,
    autoOpenBrowser: true,
    assetsSubDirectory: 'static',
    assetsPublicPath: '/',
      proxyTable: {
          '/dex': { // '/api'是代理标识，用于告诉node，url前面是/api的就是使用代理的
              target: "http://127.0.0.1:8080/dex", //目标地址，一般是指后台服务器地址
              changeOrigin: true, //是否跨域
              pathRewrite: { // pathRewrite 的作用是把实际Request Url中的'/api'用""代替
                  '^/dex': ""
              }
          }
      },
    context: [
        '/dex',
    ],
    // CSS Sourcemaps off by default because relative paths are "buggy"
    // with this option, according to the CSS-Loader README
    // (https://github.com/webpack/css-loader#sourcemaps)
    // In our experience, they generally work as expected,
    // just be aware of this issue when enabling this option.
    cssSourceMap: false
  }
}
