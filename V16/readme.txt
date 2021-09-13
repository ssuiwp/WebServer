此版本完成刚注册业务

实现:
1: 在DispatcherServlet处理请求环节,添加一个分支,根据请求路径判断是否为请求注册业务。
由于reg.html文件的form表单我孟指定的action为"action="./regUser"
因此浏览器提交给我们获取请求李静后专门判断这个字"./myweb/regUser",如果是,就是新注册业务
否则执行原有的逻辑,判断是否为请求webapps这个目录下的某个资源

2:新建一个包:com.webserver.controllers
这个包下保存所有处理业务的类(功能拆分的思想)

3:在controllers下新建用户处理相关操作,UserController
4:在UserController中新建方法:reg()
用于完成注册
注:将来处理用户其他操作,比如登录,修改密码等都可以在定义其他方法
5:在DispatcherServlet判断是注册是就实例化一个UserController并调用reg方法来完成注册操作即可