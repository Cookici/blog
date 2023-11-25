# Blog

Blog系统后端，一个简单的博客系统后端。有文章管理，单聊功能，群聊功能以及评论功能

[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]


<!-- PROJECT LOGO -->
<br />

<p align="center">
  <a href="https://github.com/Cookici/blog/">
    <img src="https://github.com/Cookici/blog/tree/main/img/logo.png" alt="Logo" width="80" height="80">
  </a>

<h3 align="center">Blog</h3>
  <p align="center">
    Blog后端系统
    <br />
    <a href="https://github.com/Cookici/blog"><strong>Blog后端项目文档 »</strong></a>
    <br />
    <br />
    <a href="https://github.com/Cookici/blog">查看Demo</a>
    ·
    <a href="https://github.com/Cookici/blog/issues">报告Bug</a>
    ·
    <a href="https://github.com/Cookici/blog/issues">提出新特性</a>
  </p>
</p>

本篇README.md面向开发者


<br /><br />

## 目录

- [上手指南](#上手指南)
    - [开发前的配置要求](#开发前的配置要求)
    - [部署步骤](#部署步骤)
- [文件目录说明](#文件目录说明)
- [开发的架构](#开发的架构)
- [部署](#部署)
- [使用到的框架](#使用到的框架)
- [贡献者](#贡献者)
    - [如何参与开源项目](#如何参与开源项目)
- [版本控制](#版本控制)
- [作者](#作者)
- [项目参考以及鸣谢](#项目参考以及鸣谢)
- [项目已知问题](#项目已知问题)
- [Blog-Vue3前端项目](#Blog-Vue3前端项目)
- [项目展示](#项目展示)


<br /><br />

### 上手指南
    需要一定的硬件配置以及编程基础



###### 开发前的环境配置
1. JAVA JDK8
2. IDEA
3. Maven
4. Nginx(主要配置到nacos集群上其他要搭建集群同理)
5. Docker(可选择搭建在本地)



###### **搭建步骤**
虚拟机环境Centos7（Docker：nacos、redis、mysql、elasticsearch）以及Centos8（RabbitMQ）
1. 搭建nacos
2. 搭建redis
3. 搭建mysql
4. 搭建elasticsearch
5. 搭建RabbitMQ


<br /><br />

### 文件目录说明

```
Blog
├── blog-article
├── blog-chat-handler
├── blog-chat
├── blog-common
├── blog-identify
├── blog-oss
├── blog-search
├── sql
├── yaml
├── README.md
└── pom.xml
```

1. blog-article文章相关功能
2. blog-chat-handler使用Redis处理聊天消息
3. blog-chat搭建Netty服务器以及RabbitMQ消息解耦
4. blog-common各种实体类和工具
5. blog-identify集成SpringSecurity和SpringCloudGateWay的鉴权和网关功能（SpringWebflux）
6. blog-oss阿里云OSS实现图片上传
7. blog-search通过Elasticsearch实现文章的扩展功能
8. sql包含项目的所有表结构
9. yaml包含nacos配置中心的所有yaml配置文件


<br /><br />

### 开发的架构
项目主要使用SpringCloud微服务架构，每个微服务使用MVC架构

<br /><br />


### 使用到的框架
- SpringBoot 2.6.3
- SpringCloud 2021.0.1
- SpringCloudAlibaba 2021.0.1.0  具体可见:<a href="https://github.com/alibaba/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E#%E7%BB%84%E4%BB%B6%E7%89%88%E6%9C%AC%E5%85%B3%E7%B3%BB">版本对应</a>
- MySQL 8.0.30
- Redis 7.0.0
- RabbitMQ 
- Elasticsearch 7.15.2
- MyBatis-Plus 3.4.0

<br /><br />

### 贡献者
当然只有我一个人咯😭😭😭


<br /><br />

#### 如何参与开源项目
贡献使开源社区成为一个学习、激励和创造的绝佳场所。你所作的任何贡献都是**非常感谢**的。


<br /><br />

### 版本控制
该项目使用Git进行版本管理。您可以在repository参看当前可用版本。

<br /><br />


### 作者
✉️632832232@qq.com
🐧632832232


<br /><br />

### 项目参考以及鸣谢
- [Netty服务器搭建](https://github.com/194295git/yan)
- [数据库表设计](https://zhangjia.io/852.html)
- 本项目中参考开源社区的各位前辈的解决方案以及代码实现

<br /><br />


### 项目已知问题
1. 在鉴权方面（自己的东西不能别人删除增加修改）有更好的
   实现方法，例如AOP以及全局过滤器。但本项目由于个人原
   因（期末），使用JWT中的封装用户名以及请求。参数中的
   用户名是 否相等来做判断（增删改等敏感操作）。
2. 群聊表的设计是有缺陷的（设计缺陷），没有创建者。所以
   申请加入群聊和退出群聊是无法实现的。但是Redis中针对
   群聊消息的已读和未读维护性（用户的加入和离开）是很高
   的，可以修改表结构和代码来实现。
3. 由于时间原因评论没有删除功能和每个文章每人评论条数的
   限制，但实现是很简单的。
4. BlogArticles类设计可考虑装饰者模式
5. 用户在线状态未实现，但实现原理简单，可通过Netty中的
   channel的退出和建立来控制用户（好友）的显示状态
6. 单聊和群聊未支持图片发送


<br /><br />

### Blog-Vue3前端项目
<a href="https://github.com/Cookici/blog-vue/tree/main">Blog-Vue3</a>


<br /><br />

### 项目展示
![image](https://github.com/Cookici/blog/tree/main/img/1.png)
![image](https://github.com/Cookici/blog/tree/main/img/2.png)
![image](https://github.com/Cookici/blog/tree/main/img/3.png)
![image](https://github.com/Cookici/blog/tree/main/img/4.png)
![image](https://github.com/Cookici/blog/tree/main/img/5.png)
![image](https://github.com/Cookici/blog/tree/main/img/6.png)
![image](https://github.com/Cookici/blog/tree/main/img/7.png)
![image](https://github.com/Cookici/blog/tree/main/img/8.png)
![image](https://github.com/Cookici/blog/tree/main/img/9.png)
![image](https://github.com/Cookici/blog/tree/main/img/10.png)
![image](https://github.com/Cookici/blog/tree/main/img/11.png)

<!-- links -->

[your-project-path]: https://github.com/Cookici/blog/tree/main

[contributors-shield]: https://img.shields.io/github/contributors/Cookici/blog.svg?style=flat-square

[contributors-url]: https://github.com/Cookici/blog/graphs/contributors

[forks-shield]: https://img.shields.io/github/forks/Cookici/blog.svg?style=flat-square

[forks-url]: https://github.com/Cookici/blog/network/members

[stars-shield]: https://img.shields.io/github/stars/Cookici/blog.svg?style=flat-square

[stars-url]: https://github.com/Cookici/blog/stargazers

[issues-shield]: https://img.shields.io/github/issues/Cookici/blog.svg?style=flat-square

[issues-url]: https://img.shields.io/github/issues/Cookici/blog.svg

[license-shield]: https://img.shields.io/github/license/Cookici/blog.svg?style=flat-square
