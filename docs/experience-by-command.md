## shell命令体验

此项体验过程中，因为涉及到 AMOP 服务，所以在联盟链中至少需要有两个区块链节点，如：Node1、 Node2 。

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

* 如果 FISCO-BCOS 为 2.x 的版本，将<a href="../README.md#install-weid-java-sdk">安装部署weid-java-sdk</a>得到的配置文件: `fisco.properties` ， `weidentity.properties` ， `ca.crt` ， `node.crt` ， `node.key` 文件拷贝到 `src/main/resources/` 目录下。

3. 关键配置文件及说明，[详见](configuration-instructions.md)

* 修改 `weid-sample/src/main/resources/weidentity.properties` 

```shell
vim src/main/resources/weidentity.properties
```

关键配置如下：

```properties
blockchain.orgid=organizationA
nodes=IP:PORT 
```
**注： 此处修改的节点配置为您两个节点中的节点1，如：Node1。**

#### 3. 编译 weid-sample

```shell
chmod +x *.sh
./build.sh
```

#### 4. 启动 AMOP 服务

```shell
./command.sh daemon
```

输出如下日志，则表示 AMOP 服务启动成功

```text
the AMOP server start success
```

#### 5. 再次修改配置，完成 issuer , user_agent , verifier 三视觉的演示

1. 修改 `weid-sample/dist/conf/` 目录下的文件 `weidentity.properties`

```shell
vim dist/conf/weidentity.properties
```

关键配置如下：

```properties
blockchain.orgid=organizationB
nodes=IP:PORT 
```

**注： 此处修改的节点配置为您两个节点中的节点2，如：Node2。**

---

2. issuer 操作流程演示

```shell
./command.sh issuer
```

输出如下日志，则表示运行成功

```text
issuer() finish...
```

---


3. user_agent 操作流程演示

```shell
./command.sh user_agent
```

输出如下日志，则表示运行成功

```text
userAgent() finish...
```


---


4. verifier 操作流程演示

```shell
./command.sh verifier
```

输出如下日志，则表示运行成功

```text
verifier() finish...
```


---


5. 三视觉代码执行过程，详见

```text
src/main/java/com/webank/weid/demo/command/DemoCommand.java
```