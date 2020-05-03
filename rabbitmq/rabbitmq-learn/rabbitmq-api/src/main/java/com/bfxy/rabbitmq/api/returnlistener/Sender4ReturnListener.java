package com.bfxy.rabbitmq.api.returnlistener;

import com.bfxy.rabbitmq.api.Config;
import com.rabbitmq.client.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class Sender4ReturnListener {

  public static void main(String[] args) throws Exception {

    // 1 创建ConnectionFactory
    ConnectionFactory connectionFactory = new ConnectionFactory();
    connectionFactory.setHost(Config.RABBIT_MQ);
    connectionFactory.setPort(5672);
    connectionFactory.setVirtualHost("/");

    // 2 创建Connection
    Connection connection = connectionFactory.newConnection();
    // 3 创建Channel
    Channel channel = connection.createChannel();

    // 4 声明
    String exchangeName = "test_returnlistener_exchange";
    String routingKey1 = "abcd.save";
    String routingKey2 = "return.save";
    String routingKey3 = "return.delete.abc";

    // 5 监听
    channel.addReturnListener(
        new ReturnListener() {
          public void handleReturn(
                  int replyCode,
                  String replyText,
                  String exchange,
                  String routingKey,
                  AMQP.BasicProperties properties,
                  @NotNull byte[] body)
              throws IOException {
            System.out.println("**************handleReturn**********");
            System.out.println("replyCode: " + replyCode);
            System.out.println("replyText: " + replyText);
            System.out.println("exchange: " + exchange);
            System.out.println("routingKey: " + routingKey);
            System.out.println("body: " + new String(body));
          }
        });

    // 6 发送
    String msg = "Hello World RabbitMQ 4 Return Listener Message ...";

    boolean mandatory = true;
    channel.basicPublish(exchangeName, routingKey1, mandatory, null, msg.getBytes());
    //		channel.basicPublish(exchangeName, routingKey2 , null , msg.getBytes());
    ///		channel.basicPublish(exchangeName, routingKey3 , null , msg.getBytes());

  }
}
