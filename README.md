# SAI-Forum-backend
SAI-Forum是一个前后端分离的论坛博客系统，拥有完整的系统设计，包括文章发布/搜索/评论/统计流程。为本院师生提供了一个良好的技术交流平台，希望构建一个和谐的社区系统。

前台社区系统包括首页、文章推荐、文章搜索、文章发布、文章详情、个人中心等模块

后台管理系统包括文章管理、分类管理、统计图表、设置等模块

## 技术选型

|技术选型|说明|
|-|-|
|SpringBoot|脚手架|
|Mybatis-Plus|ORM框架|
|MySQL|关系型数据库|
|Redis|缓存|
|Caffeine|本地缓存|
|RabbitMQ|消息队列|
|SLF4J|日志框架|
|Nginx|反向代理|
|Hutool|Java工具包类库|

## 项目架构

```Bash
SAI-Forum
├── sai-api  #  通用的枚举、实体类定义
├── sai-core # 核心组件模块，如搜索、缓存、推荐等
├── sai-service # 业务相关的主要逻辑
└── sai-web  # 项目启动入口，Web模块
```

## 项目展示图

![image](https://github.com/qing-wq/SAI-Forum-backend/assets/93132738/7da18f8e-54ad-453b-b774-2ccd0d4800e5)
![image](https://github.com/qing-wq/SAI-Forum-backend/assets/93132738/417c4389-3784-4525-8595-b54c482e2881)
![image](https://github.com/qing-wq/SAI-Forum-backend/assets/93132738/f70e0c48-4150-4bea-9528-811cb7ffeeef)
![image](https://github.com/qing-wq/SAI-Forum-backend/assets/93132738/7e276258-25bd-4038-ba3b-d3ee370ac524)
![image](https://github.com/qing-wq/SAI-Forum-backend/assets/93132738/f0329490-16fe-488f-aaed-27b92d8985cb)
![image](https://github.com/qing-wq/SAI-Forum-backend/assets/93132738/c304a794-ca8c-4454-ab73-6a358cf1c13b)
