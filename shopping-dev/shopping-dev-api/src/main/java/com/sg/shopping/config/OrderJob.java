package com.sg.shopping.config;

import com.sg.shopping.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderJob {

    @Autowired
    private OrderService orderService;

    @Scheduled(cron = "0/3 0 0/1 * * ?")
    public void autoCloseOrder() {
        orderService.closeOrder();
    }
}
