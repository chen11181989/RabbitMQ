package com.rabbitMQ.rabbitMQ.topic;

import java.io.IOException;
import java.util.UUID;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SendLogTopic {

	private final static String EXCHANGE_NAME="topic_logs";
	
	public static void main(String[] args) throws IOException {
		//创建连接
		 ConnectionFactory factory = new ConnectionFactory();
		 //设置rabbitmq 服务所在的ip
		 factory.setHost("localhost");
		 //设置账户密码
		 factory.setUsername("admin123");
		 factory.setPassword("admin123");
		 //设置端口号
		 factory.setPort(AMQP.PROTOCOL.PORT);
		//创建连接
		  Connection connection =factory.newConnection();
		  // 创建频道
		   Channel channel = connection.createChannel();
		   //声明转发器类型
		    channel.exchangeDeclare(EXCHANGE_NAME, "topic");
		    //定义绑定键
		     String[]routing_keys = new String[]{"kernal.info", "cron.warning","auth.info", "kernel.critical"};
		   for (String routingKey : routing_keys) {
			//发送4条不同绑定键的消息
			    String msg = UUID.randomUUID().toString();
			    channel.basicPublish(EXCHANGE_NAME, routingKey, null, msg.getBytes());
			    System.out.println(" [x] Sent routingKey = "+routingKey+" ,msg = " + msg + ".");    
		}
		 
		   channel.close();
		   connection.close();
	}
	
}
