此版本实现根据浏览器地址栏输入的URL地址来响应其请求的具体页面

思路如下：
在地址栏输入下列路径：
http://localhost:8088/myweb/index.html
http://localhost:8088/myweb/classTable.html

那么我们在解析请求是，在请求行的抽象路径部分分别得到
/myweb/index.html
/myweb/classTable.html

ClientHandler在上一个版本实现了将index.html页面响应的工作
写法是先实例化一个File对象表示index.html。具体代码：
File file = new File("./myweb/myweb/index.html")

如果我们想实现根据浏览器地址请求来响应不同的界面，只需要将抽象路径部分
替换到实例化File中的路径即可，因此我们在ClientHandler第二部处理请求时完成两句代码
String path = request.getUri();//先获取浏览器地址栏抽象路径

File file = new File("./webapps"+path)；//根据抽象路径定为文件
这样我们就实现了
