### RabbitMQ急速入门

------

急速入门，在这里我们使用RabbitMQ 3.6.5 版本进行操作：

- 环境搭建：

- 官网地址：http://www.rabbitmq.com/

- 环境描述：Linux（centos7 Redhat7）

  ```shell
## 1 配置好主机名称
/etc/hosts /etc/hostname

## 2. 下载RabbitMQ所需软件包（本神在这里使用的是 RabbitMQ3.6.5 稳定版本）
wget -O- https://dl.bintray.com/rabbitmq/Keys/rabbitmq-release-signing-key.asc | sudo apt-key add -
wget -O- https://www.rabbitmq.com/rabbitmq-release-signing-key.asc | sudo apt-key add -

## 3. 安装服务命令
sudo apt update
sudo apt -y install rabbitmq-server

## 4. 修改用户登录与连接心跳检测，注意修改
vim /usr/lib/rabbitmq/lib/rabbitmq_server-3.6.5/ebin/rabbit.app
修改点1：loopback_users 中的 <<"guest">>,只保留guest （用于用户登录）
修改点2：heartbeat 为10（用于心跳连接）

## 5. 安装管理插件

## 5.1 首先启动服务(后面 | 包含了停止、查看状态以及重启的命令)
sudo /etc/init.d/rabbitmq-server start | stop | status | restart

## 5.2 查看服务有没有启动： lsof -i:5672 （5672是Rabbit的默认端口）
sudo rabbitmq-plugins enable rabbitmq_management

## 5.3 可查看管理端口有没有启动： 
netstat -tnlp | grep 15672

## 6. 一切OK 我们访问地址，输入用户名密码均为 guest ：
## http://你的ip地址:15672/

## 7. 如果一切顺利，那么到此为止，我们的环境已经安装完啦
  ```

- 1
