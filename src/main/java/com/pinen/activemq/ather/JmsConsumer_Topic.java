package com.pinen.activemq.ather;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class JmsConsumer_Topic {
    public static final String ACTIVEMQ_URL = "tcp://127.0.0.1:61616";
    public static final String  ADMIN_NAME="admin";
    public static final String  ADMIN_PASSWORD="admin";
    public static final String TOPIC_NAME = "Topic01";

    public static void main(String[] args) throws JMSException, IOException {
        System.out.println("*****我是3号消费者");

        //1创建链接工厂,按照给定的URL地址，采用默认的用户名和密码
        ActiveMQConnectionFactory activeMQConnectionFactory=new ActiveMQConnectionFactory(ADMIN_NAME,ADMIN_PASSWORD,ACTIVEMQ_URL);
        //2通过链接工厂，获得链接connection 并启动访问
        Connection connection =activeMQConnectionFactory.createConnection();
        connection.start();

        // 3创建会话。session
        //2个参数   第一个叫 事务  第二个叫签收
        Session session =connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4创建目的地(队列/主题)
        Topic topic = (Topic) session.createTopic(TOPIC_NAME);
        //5 创建消费者
        MessageConsumer messageConsumer = session.createConsumer(topic);
        //通过监听的方式来消费消息。

        messageConsumer.setMessageListener((Message message) -> {
            if(message!=null  && message instanceof TextMessage){
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        System.out.println("*************3消费者接收到消息text："+textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
//
//            if(message!=null  && message instanceof MapMessage){
//                MapMessage textMessage = (MapMessage) message;
//                try {
//                    System.out.println("*************3消费者接收到消息MAP："+textMessage.getMapNames());
//                } catch (JMSException e) {
//                    e.printStackTrace();
//                }
//            }

//            if(message!=null  && message instanceof BytesMessage){
//                BytesMessage textMessage = (BytesMessage) message;
//                try {
//                    System.out.println("*************3消费者接收到消息BYTE："+textMessage.readByte()+textMessage.getJMSPriority());
//                } catch (JMSException e) {
//                    e.printStackTrace();
//                }
//            }


        });

        System.in.read();

        messageConsumer.close();
        session.close();
        connection.close();


    }
}
