## shell命令体验

### 整体介绍

命令行方式比较完整的模拟了[WeIdentity各个角色](https://weidentity.readthedocs.io/zh_CN/latest/docs/weidentity-spec.html#id9)的工作流程，可以帮您快速体验WeIdentity也业务流程和运行机制。

#### 1. 下载 weid-sample 源码：

```shell
git clone https://github.com/WeBankFinTech/weid-sample.git
cd weid-sample
```

#### 2. 配置证书及properties文件

* 安装部署weid-java-sdk

     weid-sample 需要依赖weid-java-sdk，您需要参考[WeIdentity JAVA SDK安装部署](https://weidentity.readthedocs.io/zh_CN/latest/docs/weidentity-installation.html)完成 weid-java-sdk 的安装部署，并参照[Java应用集成章节](https://weidentity.readthedocs.io/zh_CN/latest/docs/weidentity-build-with-deploy.html#weid-java-sdk)完成 weid-sample 的配置。


* 配置weid-java-sdk部署合约的私钥

     您需要将您在[部署合约阶段](https://weidentity.readthedocs.io/zh_CN/latest/docs/weidentity-build-with-deploy.html#id7)生成的私钥文件拷贝至 `keys/priv/` 目录中，此私钥后续将用于注册 Authority Issuer。


* 修改节点和机构配置

     多个角色之间会使用AMOP进行通信，根据AMOP协议，每个机构需要配置为连接不同的区块链节点。

```shell
vim src/main/resources/weidentity.properties
```

关键配置如下：

`blockchain.orgid` ： 机构名称。样例以organizationA为例，请修改为organizationA。   
`nodes` ： 区块链节点信息。你可以修改为您区块链网络中的任一节点即可。   

配置样例：
```properties
blockchain.orgid=organizationA
nodes=10.10.10.10:20200 
```

#### 3. 编译 weid-sample

```shell
chmod +x *.sh
./build.sh
```

#### 4. 启动 AMOP 服务

```shell
./command.sh daemon
```

运行成功，会启动AMOP服务，输出如下日志：

```text
the AMOP server start success.
```

#### 5. 修改user-agent配置

1. 修改 `weid-sample/dist/conf/` 目录下的文件 `weidentity.properties`


```shell
vim dist/conf/weidentity.properties
```

user-agent和verifier需要使用区块链的AMOP进行通信，因此机构名和节点名需要和前面的verifier不一样。

配置样例：

```properties
blockchain.orgid=organizationB
nodes=10.10.10.11:20200  
```

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