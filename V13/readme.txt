通过解析tomcat提供的web.xml文件,将所有的资源后缀与对应的Content-Type得到
来初始化HttpContext中的静态属性mimeMapping这个map,这样一来我们也可以支持所有的资源类型了

实现
1:将tomcat安装目录下的config/web.xml文件拷贝到WebServer项目目录下
2:重构HttpContext中初始化mimeMapping的方法:initMimeMapping
    将原有的固定向mimeMapping中put六个元素的操作删除,改为解析XMl

    具体过程:
    将web.xml跟标签下所有名为<mime-mapping>的子标签获取到,
    并将该标签中的子标签:
    <extension>中的文本作为key
    <mime-type>中的文本作为value
    put到mimeMapping中

    初始化后mimeMapping中应当有1011个元素