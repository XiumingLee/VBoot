// vue配置文件，看Vue Cli 配置参考 https://cli.vuejs.org/zh/config/#publicpath
module.exports = {
    // 打包输出路径,默认就是当前目录的dist文件夹
    outputDir:"dist",
    devServer: {  // 请求地址代理转发
        proxy: 'http://localhost:8081'
    },
    lintOnSave: false
}
