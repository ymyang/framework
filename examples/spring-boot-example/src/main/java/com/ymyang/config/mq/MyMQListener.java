package com.ymyang.config.mq;

import com.ymyang.entity.wx.WxMemberEntity;
import com.ymyang.framework.web.utils.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class MyMQListener implements MessageListenerConcurrently {

    @Autowired
    private MQMsgHandler mqMsgHandler;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        try {
            log.info("MyMQConsumer.consumeMessage, msgs = " + msgs.size());
            for (MessageExt msg : msgs) {
                String body = new String(msg.getBody());
                log.info("MyMQConsumer.consumeMessage, topic = " + msg.getTopic() + ", tag = " + msg.getTags() + ", body = " + body);

                if (MqMsgTag.WX_HEAD_IMG.equals(msg.getTags())) {
                    WxMemberEntity entity = JsonParser.fromJson(body, WxMemberEntity.class);
//                    mqMsgHandler.uploadWxHeadImgUrl(entity);
                } else if (MqMsgTag.COOPERATION_PROJECT_MAIL.equals(msg.getTags())) {

                }

            }
        } catch (Exception ex) {
            log.error("MyMQConsumer.consumeMessage err", ex);
//            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
