package com.nowcoder.community.event;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.community.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventProducer {  //只要主动调用生产者

    @Autowired
    private KafkaTemplate kafkaTemplate;

    // 处理事件
    public void fireEvent(Event event) {   //调用方才知道是什么事件，传进来，然后将事件所包含的信息发出去
        // 将事件发布到指定的主题
        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));
    }

}
