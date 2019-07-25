spring-boot服务体验
-------------------

整体介绍
--------

::

    使用 spring boot 方式，weid-sample 程序将作为一个后台进程运行，您可以使用 http 方式体验交互流程。

1. 下载 weid-sample 源码：
^^^^^^^^^^^^^^^^^^^^^^^^^^

.. code:: shell

    git clone https://github.com/WeBankFinTech/weid-sample.git

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

3. 编译 weid-sample
^^^^^^^^^^^^^^^^^^^

.. code:: shell

    cd weid-sample
    chmod +x *.sh
    ./build.sh

4. 启/停 weid-sample 服务:
^^^^^^^^^^^^^^^^^^^^^^^^^^

1. 启动 weid-sample 服务

   .. code:: shell

       ./start.sh

若启动成功，则会打印以下信息：

.. code:: text

    [main] INFO  AnnotationMBeanExporter() - Registering beans for JMX exposure on startup
    [main] INFO  Http11NioProtocol() - Initializing ProtocolHandler ["https-jsse-nio-20190"]
    [main] INFO  Http11NioProtocol() - Starting ProtocolHandler ["https-jsse-nio-20190"]
    [main] INFO  NioSelectorPool() - Using a shared selector for servlet write/read
    [main] INFO  Http11NioProtocol() - Initializing ProtocolHandler ["http-nio-20191"]
    [main] INFO  NioSelectorPool() - Using a shared selector for servlet write/read
    [main] INFO  Http11NioProtocol() - Starting ProtocolHandler ["http-nio-20191"]
    [main] INFO  TomcatEmbeddedServletContainer() - Tomcat started on port(s): 20190 (https) 20191 (http)
    [main] INFO  SampleApp() - Started SampleApp in 3.588 seconds (JVM running for 4.294)

2. 体验weid-sample服务（待补充）

--------------

附：

-  如何更改 weid-sample 启动的 https 服务的证书

weid-sample中提供了自签证书. ``tomcat.keystore`` 和 ``server.cer``
文件存放于 ``src/main/resources``
目录中。客户端浏览器安装server.cer证书，导入为受信任的根证书颁发机构即可。

自签证书所需的配置文件:

.. code:: shell

    ls src/main/resources/tomcat.keystore
    ls src/main/resources/server.cer

