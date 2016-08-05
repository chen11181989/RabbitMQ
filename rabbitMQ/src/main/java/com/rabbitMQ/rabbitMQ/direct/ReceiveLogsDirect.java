package com.rabbitMQ.rabbitMQ.direct;

import java.io.IOException;
import java.util.Random;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

//接收端随机设置一个日志严重级别
public class ReceiveLogsDirect {

	private final static String EXCHANGE_NAME = "ex_logs_direct";
	private final static String[] severities = { "info", "warning", "error" };

	public static void main(String[] args) throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
		//创建连接
		 ConnectionFactory factory = new ConnectionFactory();
		 factory.setHost("localhost");
		 factory.setUsername("admin123");
		 factory.setPassword("admin123");
		 factory.setPort(AMQP.PROTOCOL.PORT);
		 Connection connection = factory.newConnection();
		 
		 Channel channel =connection.createChannel();
		 //设置转发器类型
		 channel.exchangeDeclare(EXCHANGE_NAME, "direct");
		 String queueName = channel.queueDeclare().getQueue();
		 String severity = getSeverity();
		 // 为转发器指定队列 接收队列binding_key 为severity  ,注意，接收者接收与binding_key与routing_key相同的消息。
		 // 这里，比如，发送的时候有info ,error warning 的日志， 这里的severity 是哪一个，则只会接收发送的对应的消息的种类
		 // 比如： severity 是info ,  则只会接收发送的info 类型的所有消息
		  channel.queueBind(queueName, EXCHANGE_NAME, severity);
		  System.out.println(" [*] Waiting for "+severity+" logs. To exit press CTRL+C");    
		 QueueingConsumer consumer = new QueueingConsumer(channel);
		 channel.basicConsume(queueName, true, consumer);
		 
		 while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
		String message = new String(delivery.getBody());
		System.out.println("接收消息"+ message);
		}
		 
	}

	private static String getSeverity() {

		Random random = new Random();
		int ranVal = random.nextInt(3);
		return severities[ranVal];

	}
}
