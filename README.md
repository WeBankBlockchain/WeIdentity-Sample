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
<a name="start_use"></a>
#### 1. 参照[如何集成weidentity-java-sdk](https://weidentity.readthedocs.io/projects/javasdk/zh_CN/latest/docs/weidentity-installation.html)，部署 WeIdentity 智能合约的部署并完成配置。

完成后会生成配置文件 `applicationContext.xml`。

#### 2. 下载weidentity-sample源码：

```shell
git clone https://github.com/WeBankFinTech/weidentity-sample.git
```

#### 3. 配置 https 和 http 端口
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

#### 4. 配置文件

将第1步得到的三个文件: `applicationContext.xml` 文件，以及 `ca.crt`，`client.keystore` 拷贝到 `weidentity-sample/src/main/resources/` 目录下。


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


#### 7. 启动weidentity-sample服务:

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

## Linux上体验

#### 1. 参照<a href="#start_use">开始使用</a>完成1，2步骤

此项体验过程中，将为您演示AMOP服务，所以在联盟链中至少需要有两个区块链节点，如：Node1、Node2。

#### 2. 配置文件

将第1步得到的三个文件: `applicationContext.xml` 文件，以及 `ca.crt`，`client.keystore` 拷贝到 `weidentity-sample/src/main/resources/` 目录下。

#### 3. 修改配置项

1. 修改`weidentity-sample/src/main/resources/` 目录下的文件 `weidentity.properties`

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
  <td>blockchain.orgId</td>
  <td>机构编号，同一个联盟链中机构编号唯一 </td>
 </tr>
 <tr>
  <td>jdbc.url</td>
  <td>MySql数据库配置URL</td>
 </tr>
 <tr>
  <td>jdbc.username</td>
  <td>MySql数据库配置数据库用户名 </td>
 </tr>
 <tr>
  <td>jdbc.password</td>
  <td>MySql数据库配置用户名对应密码 </td>
 </tr>
</table>

配置示例如下：

```properties
blockchain.orgId=1002
jdbc.url=jdbc:mysql://0.0.0.0:3306/mysql?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true
jdbc.username=username
jdbc.password=password
```

注： Sample中为了演示AMOP服务，启动AMOP服务时的 `blockchain.orgId` 的配置值必须是1002，正式环境正常配置即可。

2. 修改`weidentity-sample/src/main/resources/` 目录下的文件 `applicationContext.xml`

```shell
vim src/main/resources/applicationContext.xml
```

确保连接的节点是您两个节点中的一个，如：连接Node1。

配置示例如下：

```xml
<property name="connectionsStr">
  <list>
    <value>WeIdentity@IP:PORT</value>
  </list>
</property>
```

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

1. 修改`weidentity-sample/dist/conf/` 目录下的文件 `weidentity.properties`

```shell
vim dist/conf/weidentity.properties
```

修改 `blockchain.orgId` 为1001

配置示例如下：

```properties
blockchain.orgId=1001
```

2. 修改`weidentity-sample/dist/conf/` 目录下的文件 `applicationContext.xml`

```shell
vim dist/conf/applicationContext.xml
```

确保连接的节点跟AMOP的配置节点不一致，如：Node2。

配置示例如下：

```xml
<property name="connectionsStr">
  <list>
    <value>WeIdentity@IP:PORT</value>
  </list>
</property>
```

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