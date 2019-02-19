## 环境准备

<table style="width:100%;border-collapse:collapse">
 <tr>
  <th width="100">要求</th>
  <th>说明</th>
 </tr>
 <tr>
  <td>操作系统 </td>
  <td>CentOS （7.2 64位）或Ubuntu（16.04 64位） </td>
 </tr>
 <tr>
  <td>JDK</td>
  <td>要求[<a href='https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html'>JDK1.8+</a>]。推荐使用jdk8u141</td>
 </tr>
 <tr>
  <td>gradle</td>
  <td>WeIdentity-sample使用[<a href='https://gradle.org/'>gradle</a>]进行构建，您需要提前安装好gradle，版本要求不低于4.3 </td>
 </tr>
</table>

## 开始使用

#### 1. 将weidentity-sample.zip上传到Linux服务器，并解压代码包：

```shell
unzip weidentity-sample.zip
```

#### 2. 配置 https 和 http 端口
注： （默认配置的端口是20190和20191，如果不需要更改，可以跳过这一步）。

weidentity-sample 启动后，会以 https 和 http 的方式对外提供服务：

```shell
vim src/main/resources/application.properties
```

配置说明:

<table style="width:100%;border-collapse:collapse">
 <tr>
  <th width="100">配置项</th>
  <th>说明</th>
 </tr>
 <tr>
  <td>server.port</td>
  <td>weidentity-sample启动的 https 端口.默认为20190 </td>
 </tr>
 <tr>
  <td>http.port</td>
  <td>weidentity-sample启动的 http 端口.默认为20191 </td>
 </tr>
 <tr>
  <td>admin.privKeyPath</td>
  <td>权威机构注册所需要的SDK私钥。weidentity-sample默认将SDK私钥存放在 ./keys/priv/sdkkey 文件里面。POC项目可以直接使用默认提供的私钥。 </td>
 </tr>
 <tr>
  <td>weid.keys.dir</td>
  <td>weidentity-sample中演示创建weId时，动态创建的私钥的存放路径。默认为 ./keys/ 。注意此配置为weidentity-sample中的使用，在你的工程代码里面请自行妥善保管好自己的私钥，谨防泄露。 </td>
 </tr>
</table>

配置示例如下：

```properties
server.port=20190
http.port=20191
admin.privKeyPath=./keys/priv/sdkkey
weid.keys.dir=./keys/
```


#### 3. 编译

执行：

```shell
gradle clean
gradle build
```

注：需要安装gradle。

#### 4. 添加执行权限

给```command.sh```，```start.sh```，```stop.sh``` 脚本添加执行权限:

```shell
chmod +x *.sh
```


#### 5. 启动weidentity-sample服务:

```shell
./start.sh
```

---

附：

* 如何更改 weidentity-sample 启动的https服务的证书

weidentity-sample中提供了自签证书.```tomcat.keystore```和```server.cer```文件存放于```src/main/resources```目录中。客户端浏览器安装server.cer证书，导入为受信任的根证书颁发机构即可。

自签证书所需的配置文件:

```shell
ls src/main/resources/tomcat.keystore
ls src/main/resources/server.cer
```

* 如何停止服务

项目根目录执行：
```shell
./stop.sh
```

---