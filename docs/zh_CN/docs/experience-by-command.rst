shell命令体验
-------------

整体介绍
~~~~~~~~

命令行方式比较完整的模拟了各个\ `WeIdentity角色 <https://weidentity.readthedocs.io/zh_CN/latest/docs/weidentity-spec.html#id9>`__\ 的工作流程，可以帮您快速体验WeIdentity也业务流程和运行机制。
各个角色的基本流程如下：

- Authority issuer
 | 创建WeID
 | 设置WeID公钥
 | 注册成为Authority issuer
 | 注册CPT
 | 创建Credential

- User Agent
 | 创建WeID
 | 设置WeID公钥
 | 通过AMOP获取verifier发布的presentation policy
 | 创建presentation
 | 打包presentation成QRcode或者Json串

 - Verifier
 | 收到User Agent的presentation，反序列化
 | 验证presentation


1. 下载 weid-sample 源码：
^^^^^^^^^^^^^^^^^^^^^^^^^^

.. code:: shell

    git clone https://github.com/WeBankFinTech/weid-sample.git
    cd weid-sample

2. 配置证书及properties文件
^^^^^^^^^^^^^^^^^^^^^^^^^^^

-  安装部署weid-java-sdk

   weid-sample 需要依赖weid-java-sdk，您需要参考\ `WeIdentity JAVA
   SDK安装部署 <https://weidentity.readthedocs.io/zh_CN/latest/docs/weidentity-installation.html>`__\ 完成
   weid-java-sdk
   的安装部署，并参照\ `Java应用集成章节 <https://weidentity.readthedocs.io/zh_CN/latest/docs/weidentity-build-with-deploy.html#weid-java-sdk>`__\ 完成
   weid-sample 的配置。

-  配置weid-java-sdk部署合约的私钥

   您需要将您在\ `部署合约阶段 <https://weidentity.readthedocs.io/zh_CN/latest/docs/weidentity-build-with-deploy.html#id7>`__\ 生成的私钥文件拷贝至
   ``keys/priv/`` 目录中，此私钥后续将用于注册 Authority Issuer。

-  修改节点和机构配置

   多个角色之间会使用AMOP进行通信，根据AMOP协议，每个机构需要配置为连接不同的区块链节点。

.. code:: shell

    vim src/main/resources/weidentity.properties

关键配置如下：

| ``blockchain.orgid`` ：
机构名称。样例以organizationA为例，请修改为organizationA。
| ``nodes`` ：
区块链节点信息。你可以修改为您区块链网络中的任一节点即可。

配置样例：

.. code:: properties

    blockchain.orgid=organizationA
    nodes=10.10.10.10:20200 

3. 编译 weid-sample
^^^^^^^^^^^^^^^^^^^

.. code:: shell

    chmod +x *.sh
    ./build.sh

4. 启动 AMOP 服务
^^^^^^^^^^^^^^^^^

.. code:: shell

    ./command.sh daemon

运行成功，会启动AMOP服务，输出如下日志：

.. code:: text

    the AMOP server start success.

5. 修改user-agent配置
^^^^^^^^^^^^^^^^^^^^^

user-agent和verifier需要使用区块链的AMOP进行通信，因此机构名和节点名需要和前面的verifier不一样。

.. code:: shell

    vim dist/conf/weidentity.properties

user-agent和verifier需要使用区块链的AMOP进行通信，因此机构名和节点名需要和前面的verifier不一样。

配置样例：

.. code:: properties

    blockchain.orgid=organizationB
    nodes=10.10.10.11:20200  



2. issuer 操作流程演示

.. code:: shell

    ./command.sh issuer

若运行成功，则会打印运行流程：

::

    
--------- start issuer ----------
issuer() init...

begin to createWeId...

createWeId result:

result:(com.webank.weid.protocol.response.CreateWeIdDataResult)
   weId: did:weid:1:0x7a276b294ecf0eb7b917765f308f024af2c99a38
   userWeIdPublicKey:(com.webank.weid.protocol.base.WeIdPublicKey)
      publicKey: 1443108387689714733821851716463554592846955595194902087319775398382966796515741745951182105547115313067791999154982272567881519406873966935891855085705784
   userWeIdPrivateKey:(com.webank.weid.protocol.base.WeIdPrivateKey)
      privateKey: 46686865859949148045125507514815998920467147178097685958028816903332430030079
errorCode: 0
errorMessage: success
transactionInfo:(com.webank.weid.protocol.response.TransactionInfo)
   blockNumber: 2098
   transactionHash: 0x20fc5c2730e4636248b121d31ffdbf7fa12e95185068fc1dea060d1afa9d554e
   transactionIndex: 0

begin to setPublicKey...

setPublicKey result:

result: true
errorCode: 0
errorMessage: success
transactionInfo:(com.webank.weid.protocol.response.TransactionInfo)
   blockNumber: 2099
   transactionHash: 0x498d2bfd2d8ffa297af699c788e80de1bd51c255a7365307624637ae5a42f3a1
   transactionIndex: 0


3. user\_agent 操作流程演示

.. code:: shell

    ./command.sh user_agent

输出如下日志，则表示运行成功

::

    
--------- start user_agent ----------
userAgent() init...


begin to create weId for useragent...

createWeId result:

result:(com.webank.weid.protocol.response.CreateWeIdDataResult)
   weId: did:weid:1:0x38198689923961e8ecd6d57d88d027b1a6d1daf2
   userWeIdPublicKey:(com.webank.weid.protocol.base.WeIdPublicKey)
      publicKey: 12409513077193959265896252693672990701614851618753940603742819290794422690048786166777486244492302423653282585338774488347536362368216536452956852123869456
   userWeIdPrivateKey:(com.webank.weid.protocol.base.WeIdPrivateKey)
      privateKey: 11700070604387246310492373601720779844791990854359896181912833510050901695117
errorCode: 0
errorMessage: success
transactionInfo:(com.webank.weid.protocol.response.TransactionInfo)
   blockNumber: 2107
   transactionHash: 0x2474141b82c367d8d5770a7f4d124aeaf985e7fa3e3e2f7f98eeed3d38d862f5
   transactionIndex: 0



4. verifier 操作流程演示

.. code:: shell

    ./command.sh verifier

输出如下日志，则表示运行成功

.. note::

    --------- start verifier ----------
    verifier() init...

    begin get the presentation json...



5. 三视觉代码执行过程，详见

.. code:: text

    src/main/java/com/webank/weid/demo/command/DemoCommand.java

