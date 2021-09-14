上一个版本 实现了显示所有的用户信息动态页面

问题:
性能可以再优化,之前的做法是把所有拼接的html代码写入一个文件userList.html文件,等到发送想要的时候
再将文件数据全部读取到正文你发送给浏览器
这里的IO操作降低了性能
参考:当前版本的图1.png


解决:
我们直接将拼接后的html内容作为响应正文内容设置到HttpResponse中,使其直接发送,
这需要我们对响应正文多一种存放方式,。不能只是单一的文件形式


首先:
1:在httpresponse中添加一个新的属性,byte【】 contentData;
    这个属性用来保存正文内容,通常保存的就是动态数据(程序生成的)。

2:提供对应的get,set方法:

3:在HttpResponse中在定义一个属性:ByteArrayOutputStream baOut;

4:提供方法:getWriter()
    内部使用流连接,直接提供一个pw给外界。这样一来。,
    外界就可以直接使用这个printWriter将拼接好的Html代码逐行写入到这个baOut内部的直接数组中了

5:将UserController的showAllUser方法中拼接页面的方法使用上述的pw进行

