package com.dxl.delivermanservice.service;

import com.dxl.delivermanservice.dao.DeliverymanMapper;
import com.dxl.delivermanservice.dto.OrderMessageDTO;
import com.dxl.delivermanservice.enumoperation.DeliverymanStatus;
import com.dxl.delivermanservice.po.DeliverymanPO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OrderMessageService {
    @Autowired
    DeliverymanMapper deliverymanMapper;

    @Async
    public void handleMessage(){
        final ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        try(Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel()){
            channel.queueDeclare(
              "queue.deliveryman",
              true,
              true,
              false,
              null
            );
            channel.basicConsume(
                    "queue.deliveryman",
                    true,
                    deliverCallback,
                    consumerTag -> {}
            );
            while (true){
                Thread.sleep(100000);
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }

    }

    DeliverCallback deliverCallback = ((consumerTag, message) -> {
        final byte[] body = message.getBody();
        final ObjectMapper objectMapper = new ObjectMapper();
        final OrderMessageDTO orderMessageDTO = objectMapper.readValue(body, OrderMessageDTO.class);
        List<DeliverymanPO> delieryList = deliverymanMapper.selectAvaliableDeliveryman(DeliverymanStatus.AVALIABIE);
        if(delieryList != null && delieryList.size()>0){
            orderMessageDTO.setDeliverymanId(delieryList.get(0).getId());
        }
        final String sendMessage = objectMapper.writeValueAsString(orderMessageDTO);
        final ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        try(Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel()){
            channel.basicPublish(
                    "exchange.order.deliveryman",
                    "key.order",
                    null,
                    sendMessage.getBytes()
            );
        }catch (Exception e){
            log.error(e.getMessage());
        }
    });
}
