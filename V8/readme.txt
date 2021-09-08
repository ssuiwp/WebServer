重构代码;

进行功能拆分。将ClientHandler第三部发送响应的细节工作拆分到响应对象上,


实现:
1:在com.webserver.http包下新建一个类: HttpResponse 响应对象

    该类的每一个实力用于表示服务端给客户端发送的一个http响应内容、
    每个相应内容由三部分组成,:状态行,响应头,响应正文。

2:在响应对象上定义方法flush,用于发送响应
  将ClientHandler第三部的工作拆分迁移到这里


3: ClientHandler改为调用响应对象来完成发送工作。