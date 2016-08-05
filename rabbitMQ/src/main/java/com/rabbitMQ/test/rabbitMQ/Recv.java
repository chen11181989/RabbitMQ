package com.rabbitMQ.test.rabbitMQ;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class Recv {

	
	public final static String QUEUE_NAME="hello test";
	public static void main(String[] args) throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
		//创建连接
		 ConnectionFactory factory = new ConnectionFactory();
		 //设置rabbitMQ 所在的ip　或者主机名
		 factory.setHost("localhost");
		 //指定用户名，密码
		  factory.setUsername("admin123");
		  factory.setPassword("admin123");
		  //指定端口号
		   factory.setPort(AMQP.PROTOCOL.PORT);
		 //创建一个链接
		 Connection connection =  factory.newConnection();
		 //创建一个频道
		 Channel channel =  connection.createChannel();
		
		 //声明队列， 主要为了防止消息接收者先运行此程序，队列还不存在时候创建队列
		  channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		  System.out.println("waiting for message to exit press");
		  //创建消费者
		   QueueingConsumer consumer = new QueueingConsumer(channel);
		   //指定消费队列
		    channel.basicConsume(QUEUE_NAME, true, consumer);
		    while (true) {
				// nextDelivery   是一个阻塞方法（ 内部实现其实是阻塞队列的take 方法）
		    	QueueingConsumer.Delivery delivery = consumer.nextDelivery();
		    	String message = new String(delivery.getBody());
		    	System.out.println(" received------" +message);
				
			}
	}
}
