## weid-sample配置说明

#### 1. application.properties基本配置说明

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
  <td>注册成为委员会成员所需要的合约部署私钥。weid-sample默认将SDK私钥存放在 ./keys/priv/ecdsa_key 文件里面。 </td>
 </tr>
 <tr>
  <td>weid.keys.dir</td>
  <td>weid-sample中演示创建weId时，动态创建的私钥的存放路径。默认为 ./keys/ 。注意此配置为weid-sample中的使用，在你的工程代码里面请自行妥善保管好自己的私钥，谨防泄露。 </td>
 </tr>
</table>

配置示例如下：

```properties
#https port
server.port=20190
#http port
http.port=20191

#server-side certificate library
server.ssl.key-store=classpath:tomcat.keystore
#the password for certificates
server.ssl.key-store-password=123456
server.ssl.key-store-type=JKS
#alias for certificates
server.ssl.key-alias=tomcat
server.ssl.ciphers=TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256,TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384,TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,TLS_RSA_WITH_AES_128_CBC_SHA256,TLS_RSA_WITH_AES_128_CBC_SHA,TLS_RSA_WITH_AES_256_CBC_SHA256,TLS_RSA_WITH_AES_256_CBC_SHA,SSL_RSA_WITH_RC4_128_SHA

#the private key storage path of SDK
admin.privKeyPath=./keys/priv/ecdsa_key
#the private key storage path of weId
weid.keys.dir=./keys/
```

**注：server.ssl.xxx为https请求的证书默认配置，如果您有自己的证书请自行修改此配置**


#### 2. weidentity.properties配置说明

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

**注：xxx 为数据源名称，具体参考配置示例**

**注：domain的作用为数据的定向存储，可以将不同的数据存储到不同的库实例或表实例，配置方式为： 数据源名称:表名称，同时如果您有需要还可以支持不同数据的domain配置。**
   
配置示例如下：

```properties
# The organization ID for AMOP communication.
blockchain.orgid=organizationA

# Persistence Layer configurations. Do NOT change this if you are not using Persistence Layer features!
# MySQL connection config
# Support multiple data source configurations with comma-separated multiple data sources.
datasource.name=datasource1

# The configuration of each data source is prefixed by the name of the data source.
datasource1.jdbc.url=jdbc:mysql://0.0.0.0:3306/mysql?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useSSL=false
datasource1.jdbc.username=user
datasource1.jdbc.password=password
datasource1.jdbc.maxActive=50
datasource1.jdbc.minIdle=5
datasource1.jdbc.maxIdle=5
datasource1.jdbc.maxWait=10000
datasource1.jdbc.timeBetweenEvictionRunsMillis=600000
datasource1.jdbc.numTestsPerEvictionRun=5
datasource1.jdbc.minEvictableIdleTimeMillis=1800000

# Domain configuration, which divides colons into two segments, the first segment is the name of the data source, 
# the second segment is the name of the table, and if not, the default is the first data source and the default table `sdk_all_data`,
# Multiple domains can be configured at the same time.
# example:
# credential.domain=datasource1:credential_data
# weidDocument.domain=datasource1:weid_document_data

default.domain=datasource1:sdk_all_data

# Salt length for Proof creation.
salt.length=5

# AMOP Config
# Timeout for amop request, default: 5000ms
amop.request.timeout=5000

# Blockchain node info.
nodes=NODE_IP
```

**注：节点配置目前针对 FISCO-BCOS 为 1.3.x 和  FISCO-BCOS 为 2.x 有如下差异**

如果 FISCO-BCOS 为 1.3.x 的版本，配置如下:

```properties
nodes=WeIdentity@IP:PORT 
```

如果 FISCO-BCOS 为 2.x 的版本，配置如下:

```properties
nodes=IP:PORT
```