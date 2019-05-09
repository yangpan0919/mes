package cn.tzauto.mes.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Map;

/**
 * @description 队列消息生产者，发送消息到队列
 */
@Component("queueSender")
public class QueueSender {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * 发送一条文本消息到指定的队列（目标）
     * 以持久化方式
     * @param queueName 队列名称
     * @param message   消息内容
     */
    public void send(String queueName, final String message) {
        logger.info(message);
        //发的是Queue,支持持久化
        jmsTemplate.convertAndSend(queueName,message);
    }

    public void  sendMap(String queueName, final Map message) {
        //发的是Queue,支持持久化
//        jmsTemplate.convertAndSend(queueName,message);
        Message message1 = jmsTemplate.sendAndReceive(queueName, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {

                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setStringProperty("msgName","mes.trackIn");
                mapMessage.setStringProperty("RFID","1");
                return mapMessage;
            }
        });
        System.out.println(message1.toString());
    }

}
