package cn.tzauto.mes.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;

@Configuration
public class ActiveConfig {

//    @Bean
//    public ConnectionFactory connectionFactory() {
//        return new ActiveMQConnectionFactory();    默认的连接方式，默认的配置
//    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setPubSubDomain(false);
        jmsTemplate.setDeliveryMode(1);
        jmsTemplate.setExplicitQosEnabled(true);
        jmsTemplate.setTimeToLive(3600000);
        jmsTemplate.setReceiveTimeout(30000);
        return jmsTemplate;
    }
//
//    @Bean(value="jmsMessagingTemplate")
//    public JmsMessagingTemplate jmsMessagingTemplate(JmsTemplate jmsTemplate) {
//        JmsMessagingTemplate messagingTemplate = new JmsMessagingTemplate(jmsTemplate);
//        return messagingTemplate;
//    }
}
