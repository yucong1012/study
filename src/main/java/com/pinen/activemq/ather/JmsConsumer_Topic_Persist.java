package com.pinen.activemq.ather;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.messaging.Message;

import javax.jms.*;
import java.io.IOException;

public class JmsConsumer_Topic_Persist {
    public static final String ACTIVEMQ_URL = "tcp://127.0.0.1:61616";
    public static final String  ADMIN_NAME="admin";
    public static final String  ADMIN_PASSWORD="admin";
    public static final String TOPIC_NAME = "Topic-Persist";

    public static void main(String[] args) throws JMSException, IOException {
        System.out.println("***z4");
        ActiveMQConnectionFactory activeMQConnectionFactory=new ActiveMQConnectionFactory(ADMIN_NAME,ADMIN_PASSWORD,ACTIVEMQ_URL);
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.setClientID("z4");
//        connection.start();
        Session session=connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        //4 创建
        Topic topic = session.createTopic(TOPIC_NAME);
        TopicSubscriber topicSubscriber =session.createDurableSubscriber(topic,"remark....");
        connection.start();

        javax.jms.Message message1 = topicSubscriber.receive();
        while (null!=message1){
            TextMessage textMessage =(TextMessage) message1;
            System.out.println("*****收到的持久化topic："+textMessage.getText());
            message1= (javax.jms.Message) topicSubscriber.receive(1000L);
        }
        session.close();
        connection.close();

//        1、一定要先运行一次消费者，等于向MQ注册，类似我订阅了这个主题。
//        2、然后再运行生产者发送信息，此时
//        3、无论消费者是否在线，都会接收到，不在线的话，下次链接的时候，会把没有收到过的消息都接收下来
    }
}
