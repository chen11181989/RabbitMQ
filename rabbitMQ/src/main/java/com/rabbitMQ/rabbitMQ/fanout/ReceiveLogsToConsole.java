package com.rabbitMQ.rabbitMQ.fanout;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class ReceiveLogsToConsole {

	private final static String EXCHANGE_NAME = "ex_log";

	public static void main(String[] args) throws IOException,
			ShutdownSignalException, ConsumerCancelledException,
			InterruptedException {
		// 创建连接
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		// 设置用户名密码
		factory.setUsername("admin123");
		factory.setPassword("admin123");
		// 设置端口号
		factory.setPort(AMQP.PROTOCOL.PORT);
		// 创建连接
		Connection connection = factory.newConnection();
		// 创建频道
		Channel channel = connection.createChannel();
		// 声明转发器类型
		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		// 创建一个非持久的，唯一的，且自动删除的队列
		String queueName = channel.queueDeclare().getQueue();
		// 为转发器指定队列
		channel.queueBind(queueName, EXCHANGE_NAME, "");

		// 创建消费者
		QueueingConsumer consumer = new QueueingConsumer(channel);
		// 指定接收者，第二个参数为自动应答， 无需手动
		channel.basicConsume(queueName, true, consumer);

		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
			System.out.println("  我是打在控制台上的内容");
			System.out.println(message);
			System.out.println("  打在控制台上的内容结束");
		}

	}
}
