package com.pinen.activemq.ather;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class JmsConsumer_TX {

    public static final String ACTIVEMQ_URL = "tcp://127.0.0.1:61616";
    public static final String  ADMIN_NAME="admin";
    public static final String  ADMIN_PASSWORD="admin";
    public static final String QUEUE_NAME = "queue01";

    public static void main(String[] args) throws JMSException, IOException {
        System.out.println("*****我是1号消费者");

        //1创建链接工厂,按照给定的URL地址，采用默认的用户名和密码
        ActiveMQConnectionFactory activeMQConnectionFactory=new ActiveMQConnectionFactory(ADMIN_NAME,ADMIN_PASSWORD,ACTIVEMQ_URL);
        //2通过链接工厂，获得链接connection 并启动访问
        Connection connection =activeMQConnectionFactory.createConnection();
        connection.start();

        // 3创建会话。session
        //2个参数   第一个叫 事务  第二个叫签收
        Session session =connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
        //4创建目的地(队列/主题)
        Queue queue = session.createQueue(QUEUE_NAME);
        //5 创建消费者
        MessageConsumer messageConsumer = session.createConsumer(queue);

        /*
        同步阻塞方式（RECEIVE（））
        订阅者或者接收者调用MessageConsumer的receive（）方法来接收消息。
        receive方法能够接收到消息之前（或者超时之前）将一直阻塞。
        while (true){
            TextMessage textMessage  = (TextMessage) messageConsumer.receive(4000L);
            if(textMessage!=null)
            {
                System.out.println("**********消费者接收到的消息："+textMessage.getText());
            }else break;
        }
        messageConsumer.close();
        session.close();
        connection.close();
         */
        //通过监听的方式来消费消息。
        /*
        异步非阻塞方式（监听器）
        订阅者或者接收者通过 MessageConsumer的setMessageListener  注册一个消息监听器
        当消息到达之后，系统自动调用监听器  onMessage方法
         */
        messageConsumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if(message!=null  && message instanceof TextMessage){
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        System.out.println("*************消费者接收到消息："+textMessage.getText());
                        textMessage.acknowledge();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
//                if(message!=null  && message instanceof MapMessage){
//                    MapMessage mapMessage = (MapMessage) message;
//                    try {
//                        System.out.println("*************消费Map者接收到消息："+mapMessage.getString("nihaoma"));
//                    } catch (JMSException e) {
//                        e.printStackTrace();
//                    }
//                }
            }
        });
        System.in.read();
        messageConsumer.close();


        session.commit();


        session.close();
        connection.close();
        /*
        1、先生产   只启动1号消费者  问题：21号消费者能消费消息吗？
        Y 能
        2、先生产   启动1号消费者，再启动2号消费者  问题：2号消费者还能再消费消息吗？
        2.1 1号消费者能消费
        2.2 2号消费者不能消费
        3、先启动2个消费者，再生产6条消息
          3.1 2个消费者都有6条
          3.2 先到先得  6条全部给1个
          3.3  1人一半         Y
        4、MQ挂了，那么消息的持久化和丢失情况分别如何
         */

    }
}
