实现动态数据 处理

以显示用户列表的需求为例,在首页上点击超链接, 希望看到的页面上可以体现所有的注册用户信息

问题:该页面不是事先能准备好的页面,因为该页面上的数据时会随着用户的操作不断变化,我们不可能实施修改
因此,我们需要事先当用户请求这个页面时,将现有的所有的用户信息生成一个html页面响应给用户

静态资源:事先准备好的,数据内容(资源的2进制数据)不会改变的资源
        例如index。html  logo.png或视频 gig等静态资源

动态资源:每次查看该资源时都由程序临时生成的资源
    例如:动态页面(此版本要实现的,)验证码图片等

思路: 在首页点击超链接 之后dispatcherServlet拦截该请求并调用UserController生存动态页面的方法:

该方法我们将所有的用户信息读取出来,并拼接一个Html代码。,拼接过程将用户数据都拼接过去
然后将拼接好的页面显示给浏览器即可。
