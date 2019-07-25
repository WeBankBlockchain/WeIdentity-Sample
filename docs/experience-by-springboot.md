## spring-boot服务体验

#### 1. 下载 weid-sample 源码：

```shell
git clone https://github.com/WeBankFinTech/weid-sample.git
cd weid-sample
```

#### 2. 配置证书及properties文件

1. 将weid-java-sdk部署合约的私钥文件复制到 `keys/priv/` 目录中

* 如果是安装部署为[源码方式](https://weidentity.readthedocs.io/zh_CN/latest/docs/weidentity-installation-by-sourcecode.html)，weid-java-sdk部署合约的私钥文件路径 `weid-java-sdk/ecdsa_key` 。

* 如果是安装部署为[工具方式](https://weidentity.readthedocs.io/zh_CN/latest/docs/weidentity-build-with-deploy.html)，weid-java-sdk部署合约的私钥文件路径 `weid-build-tools/output/admin/ecdsa_key` 。

2. 配置证书和properties文件

* 如果 FISCO-BCOS 为 1.3.x 的版本，将<a href="../README.md#install-weid-java-sdk">安装部署weid-java-sdk</a>得到的配置文件: `fisco.properties` ，`weidentity.properties` ， `ca.crt` ， `client.keystore` 文件拷贝到 `src/main/resources/` 目录下。

* 如果 FISCO-BCOS 为 2.x 的版本，将<a href="../README.md#install-weid-java-sdk">安装部署weid-java-sdk</a>得到的配置文件: `fisco.properties`， `weidentity.properties` ， `ca.crt` ， `node.crt` ， `node.key` 文件拷贝到 `src/main/resources/` 目录下。

3. 关键配置文件及说明，[详见](configuration-instructions.md)

#### 3. 编译 weid-sample

```shell
chmod +x *.sh
./build.sh
```

#### 4. 启/停 weid-sample 服务:

1. 启动 weid-sample 服务
```shell
./start.sh
```

输出如下日志，则表示服务启动成功

```text
[main] INFO  AnnotationMBeanExporter() - Registering beans for JMX exposure on startup
[main] INFO  Http11NioProtocol() - Initializing ProtocolHandler ["https-jsse-nio-20190"]
[main] INFO  Http11NioProtocol() - Starting ProtocolHandler ["https-jsse-nio-20190"]
[main] INFO  NioSelectorPool() - Using a shared selector for servlet write/read
[main] INFO  Http11NioProtocol() - Initializing ProtocolHandler ["http-nio-20191"]
[main] INFO  NioSelectorPool() - Using a shared selector for servlet write/read
[main] INFO  Http11NioProtocol() - Starting ProtocolHandler ["http-nio-20191"]
[main] INFO  TomcatEmbeddedServletContainer() - Tomcat started on port(s): 20190 (https) 20191 (http)
[main] INFO  SampleApp() - Started SampleApp in 3.588 seconds (JVM running for 4.294)
```

2. 停止 weid-sample 服务
```shell
./stop.sh
```

输出如下日志，则表示服务停止成功

```text
the server stop success.
```

---

附：

* 如何更改 weid-sample 启动的 https 服务的证书

weid-sample中提供了自签证书. ```tomcat.keystore``` 和 ```server.cer``` 文件存放于 ```src/main/resources``` 目录中。客户端浏览器安装server.cer证书，导入为受信任的根证书颁发机构即可。

自签证书所需的配置文件:

```shell
ls src/main/resources/tomcat.keystore
ls src/main/resources/server.cer
```