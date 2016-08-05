package com.rabbitMQ.rabbitMQ.topic;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class ReceiveLogsTopicForKernel {

	private final static String EXCHANGE_NAME = "topic_logs";

	public static void main(String[] args) throws IOException,
			ShutdownSignalException, ConsumerCancelledException,
			InterruptedException {
		// 创建连接
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		factory.setUsername("admin123");
		factory.setPassword("admin123");
		factory.setPort(AMQP.PROTOCOL.PORT);

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, "topic");
		// 随机生成一个唯一且自动删除的队列
		String queueName = channel.queueDeclare().getQueue();
		
		
		// 绑定所有与kernel 相关的消息
		channel.queueBind(queueName, EXCHANGE_NAME, "kernel.*");
		System.out.println(" [*] Waiting for messages about kernel. To exit press CTRL+C");
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(queueName, true, consumer);

		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
			System.out.println("message :" + message);
		}

		
		
		/**
		 * 总结，1：在发送消息的时候要设置转发器类型 
		 * 2：channel.basicPublish(EXCHANGE_NAME, serverity, null, message.getBytes());
		 * 在发布的时候，第二个参数 为绑定键 routing key , fanout 为空  其他的为非空，要设置
		 * 
		 * 3: 在接收消息的时候，同样也要设置转发器类型
		 * 4：还要随机生成一个唯一且自动删除的队列 String queueName = channel.queueDeclare().getQueue();
		 * 
		 * 5：绑定与之对应的相关消息
		 * channel.queueBind(queueName, EXCHANGE_NAME, "kernel.*");
		 * fanOut 类型的为空， direct 类型的为，发送消息时候绑定的其中一种类型
		 * topic  的是按某种规则去匹配的，总而言之，就是发送时候绑定的key
		 */
	}
}
