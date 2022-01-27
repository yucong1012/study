package com.pinen.activemq.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

@Service
public class SpringMQ_Produce {
   @Autowired
    private JmsTemplate jmsTemplate;


   public static void main(String[] args){

       ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
       SpringMQ_Produce produce = (SpringMQ_Produce) ctx.getBean("springMQ_Produce");

       produce.jmsTemplate.send(new MessageCreator() {
           @Override
           public Message createMessage(Session session) throws JMSException {
               return session.createTextMessage("*****spring和ActiveMQ整合***");
           }
       });

//       produce.jmsTemplate.send((session)->{
//           return session.createTextMessage("*****spring和ActiveMQ整合***");
//       });

       System.out.println("**********send tast over");
   }
}
