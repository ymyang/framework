package com.ymyang.config.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.AccessChannel;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.remoting.RPCHook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RocketMQConfig {

    @Autowired
    private MQConfig mqConfig;

    @Autowired
    private MyMQListener mqListener;

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public DefaultMQProducer mqProducer() {
        DefaultMQProducer producer = null;
        if ("CLOUD".equalsIgnoreCase(mqConfig.getAccessChannel())) {
            producer = new DefaultMQProducer(this.rpcHook());
            producer.setAccessChannel(AccessChannel.CLOUD);
        } else {
            producer= new DefaultMQProducer();
        }

        producer.setNamesrvAddr(mqConfig.getNameSrvAddr());
        producer.setProducerGroup(mqConfig.getGroup());

        return producer;
    }

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public DefaultMQPushConsumer mqPushConsumer() {
        DefaultMQPushConsumer consumer = null;
        try {
            if ("CLOUD".equalsIgnoreCase(mqConfig.getAccessChannel())) {
                consumer = new DefaultMQPushConsumer(this.rpcHook());
                consumer.setAccessChannel(AccessChannel.CLOUD);
            } else {
                consumer= new DefaultMQPushConsumer();
            }

            consumer.setNamesrvAddr(mqConfig.getNameSrvAddr());
            consumer.setConsumerGroup(mqConfig.getGroup());
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

            consumer.registerMessageListener(mqListener);

            consumer.subscribe(mqConfig.getTopic(), "*");
        } catch (Exception ex) {
            log.error("mqPushConsumer init", ex);
        }
        return consumer;
    }

    private RPCHook rpcHook() {
        SessionCredentials credentials = new SessionCredentials();
        credentials.setAccessKey(mqConfig.getAccessKey());
        credentials.setSecretKey(mqConfig.getSecretKey());
        return new AclClientRPCHook(credentials);
    }


}
