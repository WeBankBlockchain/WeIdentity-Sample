## spring-boot服务体验

## 整体介绍

    使用 spring boot 方式，weid-sample 程序将作为一个后台进程运行，您可以使用 http 方式体验交互流程。


#### 1. 下载 weid-sample 源码：

```shell
git clone https://github.com/WeBankFinTech/weid-sample.git

```

#### 2. 配置证书及properties文件

* 安装部署weid-java-sdk

 weid-sample 需要依赖weid-java-sdk，您需要参考[WeIdentity JAVA SDK安装部署](https://weidentity.readthedocs.io/zh_CN/latest/docs/weidentity-installation.html)完成 weid-java-sdk 的安装部署，并参照[Java应用集成章节](https://weidentity.readthedocs.io/zh_CN/latest/docs/weidentity-build-with-deploy.html#weid-java-sdk)完成 weid-sample 的配置。


* 配置weid-java-sdk部署合约的私钥

 您需要将您在[部署合约阶段](https://weidentity.readthedocs.io/zh_CN/latest/docs/weidentity-build-with-deploy.html#id7)生成的私钥文件拷贝至 `keys/priv/` 目录中，此私钥后续将用于注册 Authority Issuer。


#### 3. 编译 weid-sample

```shell
cd weid-sample
chmod +x *.sh
./build.sh
```

#### 4. 启/停 weid-sample 服务:

1. 启动 weid-sample 服务
```shell
./start.sh
```

若启动成功，则会打印以下信息：

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

#### 5. 体验weid-sample服务

weid-sample 提供 http 和 https 两种协议的 POST 请求服务，以下为 http 请求为例

1. 创建 weidentity DID

命令：
```shell
curl -l -H "Content-type: application/json" -X POST   http://IP:20191/createWeId
```

若运行成功，则会打印以下信息：

```text
{"result":{"weId":"did:weid:101:0xd613fbc0249f2ce5088ed484fa6b7b51ecb95e24","userWeIdPublicKey":{"publicKey":"3170902924087212850995053706205512080445198963430287429721846825598988998466716040533782467342119206581749393570668868631792331397183368695050591746049552"},"userWeIdPrivateKey":null},"errorCode":0,"errorMessage":"success","transactionInfo":{"blockNumber":60643,"transactionHash":"0xc73b7ba6af39614761423dc8fcbbbc7e5f24c82e8187bc467cf0398b4ce4330b","transactionIndex":0}}
```


2. 注册 Authority Issuer

issuer 参数请使用 createWeId 接口创建出来的 weId

命令：
```shell
curl -l -H "Content-type: application/json" -X POST -d '{"issuer":"did:weid:101:0xd613fbc0249f2ce5088ed484fa6b7b51ecb95e24","authorityName":"webank"}'  http://IP:20191/registerAuthorityIssuer
```

若运行成功，则会打印以下信息：

```text
{"result":true,"errorCode":0,"errorMessage":"success","transactionInfo":{"blockNumber":60668,"transactionHash":"0xa0b84473705da2679cfec9119e2cdef03175df0f1af676e0579d5809e4e8d6cd","transactionIndex":0}}
```

3. 注册 CPT

publisher 参数请使用 createWeId 接口创建出来的 weId

命令：
```shell
curl -l -H "Content-type: application/json" -X POST -d '{"publisher": "did:weid:101:0xd613fbc0249f2ce5088ed484fa6b7b51ecb95e24","claim": {"properties": {"id":{"type":"string","description":"user weid"},"name":{"type":"string","description":"user name"},"gender":{"type":"string","description":"user gender"}}}}' http://IP:20191/registCpt
```

若运行成功，则会打印以下信息：

```text
{"result":{"cptId":1189,"cptVersion":1},"errorCode":0,"errorMessage":"success","transactionInfo":{"blockNumber":60676,"transactionHash":"0x72d55eb1d020acd09b115177a46e230ffdb0177ab5dd74e16765d79338522093","transactionIndex":0}}
```

4. 颁发 credential

cptId 参数请使用 registCpt 接口注册出来的 cptId

issuer 参数请使用 createWeId 接口创建出来的 weId

命令：
```shell
curl -l -H "Content-type: application/json" -X POST -d '{"cptId": "1189","issuer": "did:weid:101:0xd613fbc0249f2ce5088ed484fa6b7b51ecb95e24","claimData": {"id":"did:weid:101:0xf36fb2308d36bb94c579f568bdf670743d949deb","name":"zhangsan","gender":"F"}}' http://IP:20191/createCredential
```

若运行成功，则会打印以下信息：

```text
{"result":{"credential":{"context":"https://github.com/WeBankFinTech/WeIdentity/blob/master/context/v1","id":"e4f4accd-6026-4fd0-9392-1379ddd4f778","cptId":1189,"issuer":"did:weid:101:0xd613fbc0249f2ce5088ed484fa6b7b51ecb95e24","issuanceDate":1564371227764,"expirationDate":1595475227763,"claim":{"gender":"F","name":"zhangsan","id":"did:weid:101:0xf36fb2308d36bb94c579f568bdf670743d949deb"},"proof":{"creator":"did:weid:101:0xd613fbc0249f2ce5088ed484fa6b7b51ecb95e24","signature":"G2kD4u4jrnmYbq/oVl9idmTEQzP3a0KEomHGJaVpWzhITIE+dDYSRMyF9TDy+jPANpYRJGg7pGnANM+QeJ9Ba00=","created":"1564371227764","type":"EcdsaSignature"},"signature":"G2kD4u4jrnmYbq/oVl9idmTEQzP3a0KEomHGJaVpWzhITIE+dDYSRMyF9TDy+jPANpYRJGg7pGnANM+QeJ9Ba00=","proofType":"EcdsaSignature"},"disclosure":{"name":1,"id":1,"gender":1}},"errorCode":0,"errorMessage":"success","transactionInfo":null}
```

5. 验证 credential

JSON参数为 createCredential 接口创建出来的 credential 内容

命令：
```shell
curl -l -H "Content-type: application/json" -X POST -d '{"context":"https://github.com/WeBankFinTech/WeIdentity/blob/master/context/v1","id":"e4f4accd-6026-4fd0-9392-1379ddd4f778","cptId":1189,"issuer":"did:weid:101:0xd613fbc0249f2ce5088ed484fa6b7b51ecb95e24","issuanceDate":1564371227764,"expirationDate":1595475227763,"claim":{"gender":"F","name":"zhangsan","id":"did:weid:101:0xf36fb2308d36bb94c579f568bdf670743d949deb"},"proof":{"creator":"did:weid:101:0xd613fbc0249f2ce5088ed484fa6b7b51ecb95e24","signature":"G2kD4u4jrnmYbq/oVl9idmTEQzP3a0KEomHGJaVpWzhITIE+dDYSRMyF9TDy+jPANpYRJGg7pGnANM+QeJ9Ba00=","created":"1564371227764","type":"EcdsaSignature"},"signature":"G2kD4u4jrnmYbq/oVl9idmTEQzP3a0KEomHGJaVpWzhITIE+dDYSRMyF9TDy+jPANpYRJGg7pGnANM+QeJ9Ba00=","proofType":"EcdsaSignature"},"disclosure":{"name":1,"id":1,"gender":1}'  http://IP:20191/verifyCredential
```

若运行成功，则会打印以下信息：

```text
{"result":true,"errorCode":0,"errorMessage":"success","transactionInfo":null}
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