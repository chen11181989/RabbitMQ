package com.rabbitMQ.test.rabbitMQ;


import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Send {

	//队列名称
	private final static String QUEUEZ_NAME	="hello test";
	
	public static void main(String[] args) throws IOException {
		//创建连接，连接到rabbitMQ;
		 ConnectionFactory factory = new ConnectionFactory();
		 
		 //设置rabbitMQ 所在主机IP 或者主机名
		  factory.setHost("localhost");
		  //指定用户名，密码
		   factory.setUsername("admin123");
		   factory.setPassword("admin123");
		   //指定端口
		    factory.setPort(AMQP.PROTOCOL.PORT);
		    
		  //创建一个链接
		 Connection connection=   factory.newConnection();
		 
		 //创建一个频道
		 Channel channel = connection.createChannel();
		//指定一个队列
		  channel.queueDeclare(QUEUEZ_NAME, false, false, false, null);
		  
		  //发送的消息
		  String message ="hello rabbitMQ";
		  //往队列中发送一条消息
		   channel.basicPublish("", QUEUEZ_NAME, null, message.getBytes());
		   
		  //关闭频道和链接
		   channel.close();
		   connection.close();
	}
	
}
