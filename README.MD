## 项目说明

----

本项目集合为个人SpringCloud学习时源代码，开发工具为Intellij Idea  
学习书籍为《SpringCloud微服务实战》，项目中按照书中学习到的知识和本人实际操作中遇到的问题做出的整理

更详细的记载目前是在[本人博客园](https://www.cnblogs.com/hellxz/category/1203216.html)中，如有兴趣欢迎一起讨论学习
如果本仓库对你有所帮助，请不吝star。博客园欢迎大家关注、评论、拍砖。

## 各模块端口占用表

| 项目名                | 端口号 |
| --------------------- | ------ |
| EurekaServer          | 1111   |
| EurekaServiceProvider | 8080   |
| RibbonCustomer        | 9000   |
| RibbonConsumHystrix   | 8088   |
| HystrixDashboard      | 2001   |
| Turbine               | 8089   |
| TurbineAmqp           | 8989   |
| FeignCustomer         | 9001   |
| ZuulGateway           | 5555   |
| DynamicRouteZuul      | 5556   |
| ConfigServer          | 7001   |
| ConfigClient          | 7002   |
----
2018-06-15
有点问题一直没有提醒大家，EurekaServer等几个项目用的host是本地写好的，在本系列文章的第一文中有写，但是还是有人不知道，
这里为大家提个醒
在hosts文件添加如下：
```
127.0.0.1 peer1
127.0.0.1 peer2
127.0.0.1 discovery
```  
如果不愿意使用别名可以直接使用localhost代替，这里只是为了做测试用

----
