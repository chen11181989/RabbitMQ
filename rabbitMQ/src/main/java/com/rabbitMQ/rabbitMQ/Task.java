package com.rabbitMQ.rabbitMQ;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class Task {

	// 队列名称
	private final static String QUEUE_NAME = "workqueue-durable";

	public static void main(String[] args) throws IOException {
		// 创建连接
		ConnectionFactory factory = new ConnectionFactory();
		// 设置rabbitMQ 服务所在的IP
		factory.setHost("localhost");
		// 设置用户名，密码
		factory.setUsername("admin123");
		factory.setPassword("admin123");
		// 设置端口号
		factory.setPort(AMQP.PROTOCOL.PORT);
		// 创建连接
		Connection connection = factory.newConnection();
		
		//创建频道
		 Channel channel =connection.createChannel();
		 // 设置消息持久化 rabbitMQ  不允许使用不同的参数重新定义一个队列 ，所以已经存在的队列，无法修改属性
		 boolean durable = true;
		 //声明队列
		  channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
		  
		 //发送10条消息， 一次在消息后面附加1-10个点
		  for (int i = 500; i > 0; i--) {
			String dots ="";
			for (int j = 0; j <=i; j++) {
				dots+=".";
			}
			// 要发送的消息
			String message = "hello rabbitMQ" + dots+dots.length();
			//发送消息到队列
			 channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
		
		  }
		  //关闭频道，资源
		   channel.close();
		   connection.close();
	}
}
