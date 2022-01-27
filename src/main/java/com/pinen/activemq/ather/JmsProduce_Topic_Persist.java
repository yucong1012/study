package com.pinen.activemq.ather;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsProduce_Topic_Persist {

    public static final String ACTIVEMQ_URL = "tcp://127.0.0.1:61616";
    public static final String  ADMIN_NAME="admin";
    public static final String  ADMIN_PASSWORD="admin";
    public static final String TOPIC_NAME = "Topic-Persist";

    public static void main(String[] args)throws JMSException{
        ActiveMQConnectionFactory activeMQConnectionFactory=new ActiveMQConnectionFactory(ADMIN_NAME,ADMIN_PASSWORD,ACTIVEMQ_URL);
        Connection connection = activeMQConnectionFactory.createConnection();
//        connection.start();

        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        Topic topic =session.createTopic(TOPIC_NAME);
        MessageProducer messageProducer = session.createProducer(topic);
        messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);

        connection.start();
        for(int i = 1;i<=3;i++){
            TextMessage textMessage = (TextMessage) session.createTextMessage("msg-persist"+i);
            messageProducer.send(textMessage);
        }
        messageProducer.close();
        session.close();
        connection.close();
        System.out.println("************持久化Topic消息发送完毕");
    }

}
