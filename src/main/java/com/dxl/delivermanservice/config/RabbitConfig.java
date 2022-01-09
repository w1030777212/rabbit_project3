package com.dxl.delivermanservice.config;


import com.dxl.delivermanservice.service.OrderMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @author : dxl
 * @version: 2022/1/7  18:04
 */
@Configuration
public class RabbitConfig {
    @Autowired
    OrderMessageService orderMessageService;

    @Autowired
    public void startListenMessage() throws InterruptedException {
        orderMessageService.handleMessage();
    }
}
