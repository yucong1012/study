package com.pinen.activemq.ather;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsProduce_Topic {
    public static final String ACTIVEMQ_URL = "tcp://127.0.0.1:61616";
    public static final String  ADMIN_NAME="admin";
    public static final String  ADMIN_PASSWORD="admin";
    public static final String TOPIC_NAME = "Topic01";
    public static void main(String[] args) throws JMSException {

        //1创建链接工厂,按照给定的URL地址，采用默认的用户名和密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ADMIN_NAME, ADMIN_PASSWORD, ACTIVEMQ_URL);
        //2通过链接工厂，获得链接connection 并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        // 3创建会话。session
        //2个参数   第一个叫 事务  第二个叫签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4创建目的地(队列/主题)
        Topic topic = (Topic) session.createTopic(TOPIC_NAME);
        //Collection cllection =new ArrayList;
        //5创建消息的生产者
        MessageProducer producer = session.createProducer(topic);
        //6通过使用MessPro 生产3条消息发送到MQ队列
        for (int i = 1; i <= 3; i++) {
            //7创建消息
            TextMessage textMessage = session.createTextMessage("Topic_NAME-----" + i);//理解为字符串
            //8 通过MessProducer 发送给MQ
            producer.send(textMessage);
        }
        //9 关闭资源
        producer.close();
        session.close();
        connection.close();
        System.out.println("***********Topic消息发布完成");
    }
}
