package cn.wolfcode.shop.seckill.mq;

import cn.wolfcode.commons.util.Result;
import cn.wolfcode.shop.seckill.controller.OrderInfoController;
import cn.wolfcode.shop.seckill.result.SeckillCodeMsg;
import cn.wolfcode.shop.seckill.service.IOrderInfoService;
import cn.wolfcode.shop.seckill.service.ISeckillGoodService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Component
public class OrderMQListener {
    @Autowired
    private IOrderInfoService orderInfoService;
    @Autowired
    private ISeckillGoodService seckillGoodService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queuesToDeclare = @Queue(name = MQConstants.ORDER_PEDDING_QUEUE))
    public void handlerOrderPeddingQueue(@Payload OrderMessage orderMessage, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("uuid", orderMessage.getUuid());
        map.put("seckillId", orderMessage.getSeckillId());
        try {
            String orderNo = orderInfoService.seckill(orderMessage.getUserId(), orderMessage.getSeckillId());
            map.put("orderNo", orderNo);
            map.put("code", Result.DEFAULT_SUCCESS_CODE);
            rabbitTemplate.convertAndSend(MQConstants.ORDER_RESULT_EXCHANGE, MQConstants.ORDER_RESULT_SUCCESS_KEY, map);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", SeckillCodeMsg.SECKILL_ERROR.getCode());
            map.put("msg", SeckillCodeMsg.SECKILL_ERROR.getMsg());
            rabbitTemplate.convertAndSend(MQConstants.ORDER_RESULT_EXCHANGE, MQConstants.ORDER_RESULT_FAIL_KEY, map);
        } finally {
            channel.basicAck(deliveryTag, false);
        }

    }


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = MQConstants.ORDER_RESULT_FAIL_QUEUE),
            exchange = @Exchange(name = MQConstants.ORDER_RESULT_EXCHANGE, type = "topic"),
            key = MQConstants.ORDER_RESULT_FAIL_KEY
    ))
    public void handlerOrderFaileQueue(@Payload Map<String, Object> map, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) throws IOException {
        try {
            Long seckillId = (Long) map.get("seckillId");
            seckillGoodService.sync(seckillId);
        } catch (Exception e) {
            e.printStackTrace();
            //把这个消息，把某个死信队列中放.
        } finally {
            channel.basicAck(deliveryTag, false);
        }

    }


    @Bean
    public org.springframework.amqp.core.Queue orderSuccessDelayQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", MQConstants.DELAY_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", MQConstants.ORDER_DELAY_KEY);
        arguments.put("x-message-ttl", 1000 * 60 * 5);
        return new org.springframework.amqp.core.Queue(MQConstants.ORDER_RESULT_SUCCESS_DELAY_QUEUE, true, false, false, arguments);
    }

    /**
     * 下单成功待支付的延迟队列
     * @param orderSuccessDelayQueue
     * @return
     */
    @Bean
    public Binding binding1a(org.springframework.amqp.core.Queue orderSuccessDelayQueue) {
        return BindingBuilder.bind(orderSuccessDelayQueue)
                .to(new TopicExchange(MQConstants.ORDER_RESULT_EXCHANGE))
                .with(MQConstants.ORDER_RESULT_SUCCESS_KEY);
    }


    /**
     * 处理超时订单
     * @param map
     * @param deliveryTag
     * @param channel
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = MQConstants.ORDER_TIMEOUT_QUEUE),
            exchange = @Exchange(name = MQConstants.DELAY_EXCHANGE, type = "topic"),
            key = MQConstants.ORDER_DELAY_KEY
    ))
    public void handlerOrderTimeOutQueue(@Payload Map<String, Object> map, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) throws IOException {
        try {
            Long seckillId = (Long) map.get("seckillId");
            String orderNo = (String) map.get("orderNo");
            orderInfoService.cancelOrder(seckillId, orderNo);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            channel.basicAck(deliveryTag, false);
        }
    }


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue,
            exchange = @Exchange(name = MQConstants.SECKILL_OVER_SIGN_PUBSUB_EX, type = "fanout")
    ))

    /**
     * 处理取消本地标识
     */
    public void handlerPubSubMessageQueue(@Payload Long seckillId, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) throws IOException {
        try {
            OrderInfoController.isOverSignMap.put(seckillId, false);
            System.out.println("取消本地标记........");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            channel.basicAck(deliveryTag, false);
        }
    }
}
