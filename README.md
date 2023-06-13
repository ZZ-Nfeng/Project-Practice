# delivery-takeout

#### 1、介绍
基于Springboot开发的外卖餐购项目（后台管理+消费者端）

#### 2、采用技术栈
前端：Element-ui、Vue
后端：SpringBoot、Mybatisplus、
Mysql8.0.23(注意和Mysql5的配置有所不同)、
Swagger、
Redis6.0+、

#### 3、主要的请求路径：

后端请求:
后端登录页:  http://localhost:8080/backend/page/login/login.html
后端主要展示页/主页: http://localhost:8080/backend/index.html
消费端请求:
消费端登录页: http://localhost:8080/front/page/login.html
消费端主页: http://localhost:8080/front/index.html


#### 4、项目说明：
在原本项目的基础上，我自行实现了如下功能：
1) 菜品（批量）启售和（批量）停售
   2）菜品的批量删除
   3）套餐的（批量）启售，停售
4) 套餐管理的修改
5) 后台按条件查看和展示客户订单
6) 手机端减少购物车中的菜品或者套餐数量
7) 用户查看自己订单
8) 移动端点击套餐图片查看套餐具体菜品

注意：启动项目前，需要启动Redis服务，因为项目前端验证码、前后端的菜品、套餐等模块都使用到了Redis。

