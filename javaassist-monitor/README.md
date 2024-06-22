## 使用 JavaAssist 计算运行方法的耗时

## guide
1. 引入依赖
    将本项目下的jar包引入到项目中即可。idea 加上 vm 参数 -javaagent:${jar包路径}
2. 创建配置文件。
   在 resource 文件夹中创建 config.properties 文件，配置示例如下：class.names=Main,Main2。Main 和 Main2 是需要监控的类名。类名一定需要是全路径类名
3. 直接运行方法即可

## 适用场景
不能用于大型项目，web项目。
适合用于计算显式带有 main 的小功能demo
