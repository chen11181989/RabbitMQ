package com.rabbitMQ.rabbitMQ;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class Work {
	private final static String QUEUE_NAME = "workqueue-durable";

	public static void main(String[] args) throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
	 // 区分不同工作进程的输出
		int hashCode = Work.class.hashCode();
		// 创建连接
		ConnectionFactory factory = new ConnectionFactory();
		// 设置rabbimtMQ 服务所在的ip
		factory.setHost("localhost");
		// 设置用户名，密码
		factory.setUsername("admin123");
		factory.setPassword("admin123");
		// 设置端口号
		factory.setPort(AMQP.PROTOCOL.PORT);
		// 创建连接
		Connection connection = factory.newConnection();
		// 创建频道
		Channel channel = connection.createChannel();
		boolean durable = true;
		// 声明队列
		channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
		// 创建消费者
		QueueingConsumer consumer = new QueueingConsumer(channel);

		boolean ask = false; // 打开应答机制
		// 指定消费队列
		channel.basicConsume(QUEUE_NAME, ask, consumer);
		// 公平转发， 设置最大服务转发消息数量， 只在消费者空闲的时候会发送下一条消息
		int prefetchCount = 1;
		channel.basicQos(prefetchCount);
		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
			System.out.println(hashCode+" received message:"+ message);
			 dowork(message);
			 System.out.println(hashCode+"Received Done");
			 // 在每处理完成一个消息后 ，手动发送应答
			  channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			
		}
		
	}
	
	private static void dowork(String task) throws InterruptedException{
		for (char ch:task.toCharArray()) {
			if (ch=='.') {
			  Thread.sleep(1000);
			}
		}			
	}

}
