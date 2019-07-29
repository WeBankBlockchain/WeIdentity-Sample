整体介绍
~~~~~~~~

weid-sample是基于
`WeIdentity <https://weidentity.readthedocs.io/zh_CN/latest/README.html>`__\ 开发的Java应用样例程序，提供了一整套的流程演示，可以帮您快速理解WeIdentity的运行机制，您也可以参考该样例程序，开发您的WeIdentity应用。

环境准备
~~~~~~~~

.. list-table::
   :header-rows: 1

   * - 配置
     - 说明
   * - 操作系统
     - CentOS （7.2 64位）或Ubuntu（16.04 64位）。
   * - FISCO-BCOS区块链环境
     - 您需要有一套可以运行的FISCO-BCOS区块链环境，如果没有，可以参考\ `「FISCO-BCOS 2.0节点安装方法」 <https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/installation.html>`_\ 或\ `「FISCO-BCOS 1.3节点安装方法」 <https://fisco-bcos-documentation.readthedocs.io/zh_CN/release-1.3/docs/tools/index.html>`_\ 来搭建一套区块链环境。
   * - JDK
     - 要求\ `JDK1.8+ <https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html>`_\ ，推荐使用jdk8u141。
   * - gradle
     - WeIdentity JAVA SDK使用\ `gradle <https://gradle.org/>`_\ 进行构建，您需要提前安装好gradle，版本要求不低于4.3。
   * - 网络连通
     - 检查WeIdentity JAVA SDK部署环境是否能telnet通FISCO BCOS节点的channelPort端口，若telnet不通，需要检查网络连通性和安全策略。

快速体验
~~~~~~~~

我们提供了两种方式体验 weid-sample：

-  `命令行方式使用 <docs/experience-by-command.html>`__
   （推荐方式）

-  `spring-boot方式使用 <docs/experience-by-springboot.html>`__


