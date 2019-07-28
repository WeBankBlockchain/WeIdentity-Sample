spring-boot服务方式使用
-------------------

整体介绍
--------

::

    使用 spring boot 方式，weid-sample 程序将作为一个后台进程运行，您可以使用 http 方式体验交互流程。

1. 下载 weid-sample 源码：
^^^^^^^^^^^^^^^^^^^^^^^^^^

.. code:: shell

    git clone https://github.com/WeBankFinTech/weid-sample.git

2. 配置与部署
^^^^^^^^^^^^^^^^^^^^^^^^^^

2.1 下载 weid-sample 源码：
''''''''''''''''''''''''''''''''''''

.. code:: shell

    git clone https://github.com/WeBankFinTech/weid-sample.git
    

2.2 部署 weid-java-sdk 与配置基本信息
''''''''''''''''''''''''''''''''''''''

-  安装部署 weid-java-sdk

   weid-sample 需要依赖 weid-java-sdk，您需要参考\ `WeIdentity JAVA
   SDK安装部署 <https://weidentity.readthedocs.io/zh_CN/latest/docs/weidentity-installation.html>`__\ 完成
   weid-java-sdk
   的安装部署，并参照\ `Java应用集成章节 <https://weidentity.readthedocs.io/zh_CN/latest/docs/weidentity-build-with-deploy.html#weid-java-sdk>`__\ 完成
   weid-sample 的配置。


-  配置weid-java-sdk部署合约的私钥

   将您在\ `部署WeIdentity智能合约阶段 <https://weidentity.readthedocs.io/zh_CN/latest/docs/weidentity-build-with-deploy.html#id7>`__\ 生成的私钥文件拷贝至
   ``weid-sample/keys/priv/`` 目录中，此私钥后续将用于注册 Authority Issuer，weid-sample 会自动加载。
.. note::
   此项配置并非必要，注册 Authority Issuer 需要委员会机构成员（ Committee Member ）权限，发布智能合约时生成的公私钥对会自动成为委员会机构成员，若您不是发布智能合约的机构，您无需关注此配置项。
   若您是智能合约发布的机构，您可以参考以下进行配置：


-  修改节点和机构配置

   多个角色之间会使用 \ `AMOP <https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/amop_protocol.html>`__\ 进行通信，根据 AMOP 协议，每个机构需要配置为连接不同的区块链节点。

.. code:: shell

    cd weid-sample
    vim src/main/resources/weidentity.properties

关键配置如下：

 | ``blockchain.orgid`` ：机构名称。样例以 organizationA 为例，请修改为 organizationA。
 | ``nodes`` ：区块链节点信息。你可以修改为您区块链网络中的任一节点即可。

配置样例：

.. code:: properties

    blockchain.orgid=organizationA
    nodes=10.10.10.10:20200 

2.3 基本流程的演示
''''''''''''''''''''''''
2.3.1 编译和运行
>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

- 编译 weid-sample

.. code:: shell

    cd weid-sample
    chmod +x *.sh
    ./build.sh

- 启动 weid-sample 服务:


   .. code:: shell

       ./start.sh

若启动成功，则会打印以下信息：

::

    [main] INFO  AnnotationMBeanExporter() - Registering beans for JMX exposure on startup
    [main] INFO  Http11NioProtocol() - Initializing ProtocolHandler ["https-jsse-nio-20190"]
    [main] INFO  Http11NioProtocol() - Starting ProtocolHandler ["https-jsse-nio-20190"]
    [main] INFO  NioSelectorPool() - Using a shared selector for servlet write/read
    [main] INFO  Http11NioProtocol() - Initializing ProtocolHandler ["http-nio-20191"]
    [main] INFO  NioSelectorPool() - Using a shared selector for servlet write/read
    [main] INFO  Http11NioProtocol() - Starting ProtocolHandler ["http-nio-20191"]
    [main] INFO  TomcatEmbeddedServletContainer() - Tomcat started on port(s): 20190 (https) 20191 (http)
    [main] INFO  SampleApp() - Started SampleApp in 3.588 seconds (JVM running for 4.294)

2.3.2 流程演示
>>>>>>>>>>>>>>>>>>>>>>>>

- issuer 创建 WeID、注册成为 Authority Issuer、注册 CPT 和创建 credential：
.. code:: shell

    curl xxx

若调用成功，则会打印以下信息：
::

    
    --------- start issuer ----------
    issuer() init...

    begin to createWeId...

    createWeId result:

    result:(com.webank.weid.protocol.response.CreateWeIdDataResult)
    weId: did:weid:1:0x7a276b294ecf0eb7b917765f308f024af2c99a38
    userWeIdPublicKey:(com.webank.weid.protocol.base.WeIdPublicKey)
        publicKey: 1443108387689714733821851716463554592846955595194902087319775398382966796515741745
        951182105547115313067791999154982272567881519406873966935891855085705784
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


- user_agent 操作流程演示

.. code:: shell

    curl xxx

运行成功，则会打印包括创建 WeID、 通过 AMOP 获取 verifier 发布的 presentation policy、创建 presentation 以及打包 presentation 成 QRcode 或者 Json 串的流程。
以下为截取的部分日志： 

::

    
    --------- start user_agent ----------
    userAgent() init...

    begin to create weId for useragent...

    createWeId result:

    result:(com.webank.weid.protocol.response.CreateWeIdDataResult)
    weId: did:weid:1:0x38198689923961e8ecd6d57d88d027b1a6d1daf2
    userWeIdPublicKey:(com.webank.weid.protocol.base.WeIdPublicKey)
        publicKey: 12409513077193959265896252693672990701614851618753940603742819290794422690048786166
        777486244492302423653282585338774488347536362368216536452956852123869456
    userWeIdPrivateKey:(com.webank.weid.protocol.base.WeIdPrivateKey)
        privateKey: 11700070604387246310492373601720779844791990854359896181912833510050901695117
    errorCode: 0
    errorMessage: success
    transactionInfo:(com.webank.weid.protocol.response.TransactionInfo)
    blockNumber: 2107
    transactionHash: 0x2474141b82c367d8d5770a7f4d124aeaf985e7fa3e3e2f7f98eeed3d38d862f5
    transactionIndex: 0


附录
^^^^^^^^^^^^^^^

-  如何更改 weid-sample 启动的 https 服务的证书

weid-sample 中提供了自签证书. ``tomcat.keystore`` 和 ``server.cer``
文件存放于 ``src/main/resources``
目录中。客户端浏览器安装 ``server.cer`` 证书，导入为受信任的根证书颁发机构即可。

自签证书所需的配置文件:

.. code:: shell

    ls src/main/resources/tomcat.keystore
    ls src/main/resources/server.cer

