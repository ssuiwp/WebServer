重构

1:DispatcherServlet中处理请求是我们临时创建了一个map保存所有的资源后缀与对应的Content—type值,但是这样一来意味这每一次请求都要创建一个这个mao
这是没有必要的
把他定义成全局一份即可,每次用时从中获取值即可。

实现:
一
1:在com.webserver.http包下新建类HttpContext
这个类用于定义所有的和HTTP协议有关的数据,以便当前程序所有类共同使用

2:在HttpContext中定义一个静态属性,static map MimeMapping
3:提供对应的getMineType方法,可以根据资源后缀提取对应的Context-Type的值

二
对于Httpresponse的设置工作,每当我们添加一个响应正文跟就应当包含两个说明正文的响应头:
Content-Type和Content-Length,因此我们可以将添加这两个响应头的工作移动到HttpResponse提供的添加响应正文方法setEntity中
这样。每当我们添加正文就自动添加这两个头

实现:
将DispatcherServlet中通过资源后缀分析类型并添加Content-Length和Content-Type的代码全部移动到HttpResponse的添加正文方法中。


xml文件存取数据,