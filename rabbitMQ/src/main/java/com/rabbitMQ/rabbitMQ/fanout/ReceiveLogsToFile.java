package com.rabbitMQ.rabbitMQ.fanout;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class ReceiveLogsToFile {

	private final static String EXCHANGE_NAME = "ex_log";

	public static void main(String[] args) throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
		// 创建连接
		ConnectionFactory factory = new ConnectionFactory();
		// 设置rabbitMQ 服务所在的ip
		factory.setHost("localhost");
		// 设置账户，密码
		factory.setUsername("admin123");
		factory.setPassword("admin123");
		// 设置端口号
		factory.setPort(AMQP.PROTOCOL.PORT);
		// 创建连接
		Connection connection = factory.newConnection();
		// 创建频道
		Channel channel = connection.createChannel();
		//声明转发器类型
		 channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		 //创建一个非持久的，唯一的，且自动删除的队列 ，队列名称由服务器随机参数，一般情况下这个名称与amq.gen-JzTY20BRgKO-HjmUJj0wLg 类似
		 String queueName = channel.queueDeclare().getQueue();
		 
		 //为转发器指定队列，设置binding
		  channel.queueBind(queueName, EXCHANGE_NAME, "");
		  System.out.println("[*] waiting for messages . to exit press ");	  		
		//创建消费者
		 QueueingConsumer consumer = new QueueingConsumer(channel);
		 // 指定接收者，第二个参数为自动应答，无需手动应答
		  channel.basicConsume(queueName, true,consumer);
		 while (true) {
			QueueingConsumer.Delivery delivery =  consumer.nextDelivery();
			String message = new String(delivery.getBody());
			System.out.println(" 我在写入日志，没有实现写入日志的方法");
			System.out.println(message);
		}
	}
}
