package com.rabbitMQ.rabbitMQ.fanout;

import java.io.IOException;
import java.util.Date;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SendLog {

	private final static String EXCHANGE_NAME = "ex_log";

	public static void main(String[] args) throws IOException {
		// 创建连接
		ConnectionFactory factory = new ConnectionFactory();
		// 设置rabbitMQ 所在主机IP
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

		/**
		 * 声明转发器和类型，可用的转发器类型：direct topic headers fanout
		 * Direct Exchange --处理路由键，需要将一个队列绑定到交换机上，要求该消息与一个特定的路由键完全匹配
		 *fanout Exchange --不处理路由键，你只需要简单的将队列绑定到交换机上，。一个发送到交换机的消息都会被转发到与该交换机绑定的所有队列
		 *很像子网广播，每台子网内的主机都获得了一份复制的消息，fanout 交换机转发消息是最快的
		 *
		 *Top Exchange  - 将路由键和某模式进行匹配，此时队列需要绑定到一个模式上，符号" #"”匹配一个或多个词，符号“*”匹配不多不少一个词
		 *
		 */
		
		//将队列绑定到交换机上
		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		
		String message = new Date().toString()+": log something";
		//把消息转发到指定的转发器
		 channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
		 System.out.println("send:" +message);
		 
		 //关闭频道 和资源
		  channel.close();
		  connection.close();
	}
}
