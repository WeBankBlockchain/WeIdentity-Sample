## 环境准备

<table style="width:100%;border-collapse:collapse">
 <tr>
  <th width="200">要求</th>
  <th>说明</th>
 </tr>
 <tr>
  <td  align="center">操作系统 </td>
  <td>CentOS （7.2 64位）或 Ubuntu（16.04 64位） </td>
 </tr>
 <tr>
  <td  align="center">FISCO-BCOS区块链环境 </td>
  <td>您需要有一套可以运行的FISCO-BCOS区块链环境，如果没有，可以参考[<a href='https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/installation.html'>FISCO-BCOS 2.0节点安装方法</a>]或[<a href='https://fisco-bcos-documentation.readthedocs.io/zh_CN/release-1.3/docs/tools/index.html'>FISCO-BCOS 1.3节点安装方法</a>]来搭建一套区块链环境。 </td>
 </tr>
 <tr>
  <td  align="center">JDK</td>
  <td>要求[<a href='https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html'>JDK1.8+</a>]。推荐使用jdk8u141</td>
 </tr>
 <tr>
  <td  align="center">gradle</td>
  <td>weid-sample使用[<a href='https://gradle.org/'>gradle</a>]进行构建，您需要提前安装好gradle，版本要求不低于4.3 </td>
 </tr>
</table>

<a name="install-weid-java-sdk"></a>
## 安装部署weid-java-sdk

完成[weid-java-sdk安装部署](https://weidentity.readthedocs.io/projects/javasdk/zh_CN/latest/docs/weidentity-installation.html)后会生成配置文件 `fisco.properties`，`weidentity.properties` 以及相关证书文件。

* 如果是安装部署为[源码方式](https://weidentity.readthedocs.io/zh_CN/latest/docs/weidentity-installation-by-sourcecode.html)，生成的配置文件路径 `weid-java-sdk/dist/conf/` 。

* 如果是安装部署为[工具方式](https://weidentity.readthedocs.io/zh_CN/latest/docs/weidentity-build-with-deploy.html) ，生成的配置文件路径 `weid-build-tools/resources/` 。

## 快速体验

* [spring-boot服务体验](./docs/experience-by-springboot.md)

* [shell命令体验](./docs/experience-by-command.md)
