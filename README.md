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
  <td>weid-sample使用[<a href='https://gradle.org/'>gradle</a>]进行构建，您需要提前安装好gradle，版本要求不低于4.3 </td>
 </tr>
</table>

## 开始使用
<a name="start_use"></a>
#### 1. 参照[如何集成weid-java-sdk](https://weidentity.readthedocs.io/projects/javasdk/zh_CN/latest/docs/weidentity-installation.html)，部署 WeIdentity 智能合约的部署并完成配置。

完成后会生成配置文件 `fisco.properties`，`weidentity.properties` 以及相关证书文件。

#### 2. 下载weid-sample源码：

```shell
git clone https://github.com/WeBankFinTech/weid-sample.git
```
注：注意对应的版本

#### 3. 配置 https 和 http 端口
注： （默认配置的端口是20190和20191，如果不需要更改，可以跳过这一步）。

weid-sample 启动后，会以 https 和 http 的方式对外提供服务：

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
  <td>weid-sample启动的 https 端口.默认为20190 </td>
 </tr>
 <tr>
  <td>http.port</td>
  <td>weid-sample启动的 http 端口.默认为20191 </td>
 </tr>
 <tr>
  <td>admin.privKeyPath</td>
  <td>权威机构注册所需要的SDK私钥。weid-sample默认将SDK私钥存放在 ./keys/priv/ecdsa_key 文件里面。 </td>
 </tr>
 <tr>
  <td>weid.keys.dir</td>
  <td>weid-sample中演示创建weId时，动态创建的私钥的存放路径。默认为 ./keys/ 。注意此配置为weid-sample中的使用，在你的工程代码里面请自行妥善保管好自己的私钥，谨防泄露。 </td>
 </tr>
</table>

配置示例如下：

```properties
server.port=20190
http.port=20191
admin.privKeyPath=./keys/priv/ecdsa_key
weid.keys.dir=./keys/
```

#### 4. 配置文件

1. 将sdk私钥文件复制到 `weid-sample/keys/priv/` 中进行覆盖

* 如果是安装部署为源码方式，sdk私钥文件路径 `weid-java-sdk/ecdsa_key` 。

* 如果是安装部署为工具方式 ，sdk私钥文件路径 `weid-build-tools/output/admin/ecdsa_key` 。

2. 配置证书和properties文件

* 如果 FISCO-BCOS 为 1.3.x 的版本，将第1步得到的配置文件: `fisco.properties`，`weidentity.properties`，`ca.crt`，`client.keystore` 文件拷贝到 `weid-sample/src/main/resources/` 目录下。

* 如果 FISCO-BCOS 为 2.x 的版本，将第1步得到的配置文件: `fisco.properties`，`weidentity.properties`，`ca.crt`，`node.crt`，`node.key`文件拷贝到 `weid-sample/src/main/resources/` 目录下。

#### 5. 编译

执行：

```shell
gradle clean
gradle build
```

注：需要安装gradle。

#### 6. 添加执行权限

给```command.sh```，```start.sh```，```stop.sh``` 脚本添加执行权限:

```shell
chmod +x *.sh
```


#### 7. 启动weid-sample服务:

```shell
./start.sh
```

---

附：

* 如何更改 weid-sample 启动的https服务的证书

weid-sample中提供了自签证书.```tomcat.keystore```和```server.cer```文件存放于```src/main/resources```目录中。客户端浏览器安装server.cer证书，导入为受信任的根证书颁发机构即可。

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

## Linux上体验

#### 1. 参照<a href="#start_use">开始使用</a>完成1，2步骤

此项体验过程中，将为您演示AMOP服务，所以在联盟链中至少需要有两个区块链节点，如：Node1、Node2。

#### 2. 配置文件

1. 将sdk私钥文件复制到 `weid-sample/keys/priv/` 中进行覆盖

* 如果是安装部署为源码方式，sdk私钥文件路径 `weid-java-sdk/ecdsa_key` 。

* 如果是安装部署为工具方式 ，sdk私钥文件路径 `weid-build-tools/output/admin/ecdsa_key` 。

2. 配置证书和properties文件

* 如果 FISCO-BCOS 为 1.3.x 的版本，将第1步得到的配置文件: `fisco.properties`，`weidentity.properties`，`ca.crt`，`client.keystore` 文件拷贝到 `weid-sample/src/main/resources/` 目录下。

* 如果 FISCO-BCOS 为 2.x 的版本，将第1步得到的配置文件: `fisco.properties`，`weidentity.properties`，`ca.crt`，`node.crt`，`node.key`文件拷贝到 `weid-sample/src/main/resources/` 目录下。

#### 3. 修改配置项

1. 修改`weid-sample/src/main/resources/` 目录下的文件 `weidentity.properties`

```shell
vim src/main/resources/weidentity.properties
```

配置说明:

<table style="width:100%;border-collapse:collapse">
 <tr>
  <th width="100">配置项</th>
  <th>说明</th>
 </tr>
 <tr>
  <td>blockchain.orgid</td>
  <td>机构编号，同一个联盟链中机构编号唯一  </td>
 </tr>
 <tr>
  <td>datasource.name</td>
  <td>数据源名称配置，支持多个数据源名称配置，以英文逗号隔开  </td>
 </tr>
 <tr>
  <td>xxx.jdbc.url</td>
  <td>MySql数据库配置URL </td>
 </tr>
 <tr>
  <td>xxx.jdbc.username</td>
  <td>MySql数据库配置数据库用户名  </td>
 </tr>
 <tr>
  <td>xxx.jdbc.password</td>
  <td>MySql数据库配置用户名对应密码  </td>
 </tr>
 <tr>
  <td>xxx.jdbc.maxActive</td>
  <td>MySql数据库配置最大活跃连接  </td>
 </tr>
 <tr>
  <td>xxx.jdbc.minIdle</td>
  <td>MySql数据库配置最小空闲连接  </td>
 </tr>
 <tr>
  <td>xxx.jdbc.maxIdle</td>
  <td>MySql数据库配置最大空闲连接 </td>
 </tr>
 <tr>
  <td>xxx.jdbc.maxWait</td>
  <td>MySql数据库配置获取连接最大等待时间，单位毫秒 </td>
 </tr>
 <tr>
  <td>xxx.jdbc.timeBetweenEvictionRunsMillis</td>
  <td>MySql数据库配置间隔检查连接时间，单位毫秒 </td>
 </tr>
 <tr>
  <td>xxx.jdbc.numTestsPerEvictionRun</td>
  <td>MySql数据库配置单次检查连接个数  </td>
 </tr>
 <tr>
  <td>xxx.jdbc.minEvictableIdleTimeMillis</td>
  <td>MySql数据库配置连接保持空闲而不被驱逐的最长时间，单位毫秒  </td>
 </tr>
 <tr>
  <td>default.domain</td>
  <td>默认的存储域配置，默认配置为：xxx:sdk_all_data </td>
 </tr>
 <tr>
  <td>salt.length</td>
  <td>盐值长度 </td>
 </tr>
 <tr>
  <td>amop.request.timeout</td>
  <td>AMOP超时时间配置，单位毫秒 </td>
 </tr>
 <tr>
  <td>nodes</td>
  <td>节点IP和端口配置，多个节点用英文逗号隔开 </td>
 </tr>
</table>

注：xxx为数据源名称，具体参考配置示例

注：domain的作用为数据的定向存储，可以将不同的数据存储到不同的库实例或表实例，配置方式为： 数据源名称:表名称，同时如果您有需要还可以支持不同数据的domain配置。
   
配置示例如下：

```properties
blockchain.orgid=organizationA
#(数据源配置)
datasource.name=datasource1
datasource1.jdbc.url=jdbc:mysql://0.0.0.0:3306/mysql?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useSSL=false
datasource1.jdbc.username=username
datasource1.jdbc.password=password
datasource1.jdbc.maxActive=50
datasource1.jdbc.minIdle=5
datasource1.jdbc.maxIdle=5
datasource1.jdbc.maxWait=10000
datasource1.jdbc.timeBetweenEvictionRunsMillis=600000
datasource1.jdbc.numTestsPerEvictionRun=5
datasource1.jdbc.minEvictableIdleTimeMillis=1800000
#(domain配置)
default.domain=datasource1:sdk_all_data
#(不同数据的domain配置,调用persistence存取的时候自行通过PropertyUtils来读取配置使用即可)
#weid.domain=datasource1:sdk_weid_data
salt.length=5
amop.request.timeout=5000
#(如果 FISCO-BCOS 为 1.3.x 的版本，nodes配置如下)
nodes=WeIdentity@IP:PORT 
#(如果 FISCO-BCOS 为 2.x 的版本，nodes配置如下)
nodes=IP:PORT
```

注： Sample中为了演示AMOP服务，启动AMOP服务时的 `blockchain.orgid` 的配置值必须是organizationA，正式环境正常配置即可。

注：确保连接的节点是您两个节点中的一个，如：连接Node1。

#### 4. 添加执行权限

给```command.sh```，```build.sh```，```start.sh```，```stop.sh``` 脚本添加执行权限:

```shell
chmod +x *.sh
```

#### 5. 编译

```shell
./build.sh
```

注：需要安装gradle。

#### 6. 启动AMOP服务

```shell
./command.sh daemon
```

#### 7. 再次修改配置，完成issuer,user_agent,verifier三视觉的演示

1. 修改`weid-sample/dist/conf/` 目录下的文件 `weidentity.properties`

```shell
vim dist/conf/weidentity.properties
```

修改 `blockchain.orgId` 为organizationB
修改 `nodes` 为另外一个节点

配置示例如下：

```properties
blockchain.orgId=organizationB
#(如果 FISCO-BCOS 为 1.3.x 的版本，nodes配置如下)
nodes=WeIdentity@IP:PORT 
#(如果 FISCO-BCOS 为 2.x 的版本，nodes配置如下)
nodes=IP:PORT
```

注： 确保连接的节点跟AMOP服务端的配置节点不一致，如：Node2。

3. issuer操作流程演示

```shell
./command.sh issuer
```

4. user_agent操作流程演示

```shell
./command.sh user_agent
```

5. verifier操作流程演示

```shell
./command.sh verifier
```

6. 三视觉代码执行过程，详见

```text
src/main/java/com/webank/weid/demo/command/DemoCommand.java
```

---