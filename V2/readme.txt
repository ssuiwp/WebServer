readme.txt
    此版本我们开始完成解析请求的工作

    HTTP协议要求客户端与服务端的交互
    采取一问一答的原则，因此服务端的处理流程
    我们分为三步完成：
    1：解析请求  2：处理请求  3：响应客户端


    为了使得服务端可以同时处理多客户端交互，因此还是采取多线程模式来完成处理客户端的工作。
    在com.webserver.core包中新建类：ClientHandler，用于处理客户端交互。
    当WebServer中主线程接受一个客户端连接后就启动一个线程来处理该客户端交互。