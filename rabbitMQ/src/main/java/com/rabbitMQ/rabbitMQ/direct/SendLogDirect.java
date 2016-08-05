package com.rabbitMQ.rabbitMQ.direct;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

// 随机发送6条随机类型（routing key） 的日志给转发器
public class SendLogDirect {

	private  final static String EXCHANGE_NAME ="ex_logs_direct";
	
	private final static String[] severities={"info","warning" , "error"};
	
	public static void main(String[] args) throws IOException {
		//创建连接
		 ConnectionFactory factory = new ConnectionFactory();
		 factory.setHost("localhost");
		 factory.setUsername("admin123");
		 factory.setPassword("admin123");
		 factory.setPort(AMQP.PROTOCOL.PORT);
		 
		 Connection connection =factory.newConnection();
		 Channel channel = connection.createChannel();
		 //声明转发器类型
		 channel.exchangeDeclare(EXCHANGE_NAME, "direct");
		 //发送6条消息
		  for (int i = 0; i < 6; i++) {
			String serverity = getSeverity();
			String message = serverity+"_log：" +UUID.randomUUID().toString();
			//发送消息至转发器 ，指定 routingkey 这里为serverity 
			channel.basicPublish(EXCHANGE_NAME, serverity, null, message.getBytes());
			System.out.println("sent:" +message);
		}
		  channel.close();
		  connection.close();
	}
	
	private static String getSeverity(){
		
		Random random = new Random();
		int ranVal = random.nextInt(3);
		return severities[ranVal];
		
	}
}
