package com.sg.shopping.controller;

import com.sg.shopping.common.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@ApiIgnore
@RequestMapping("redis")
public class RedisController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisOperator redisOperator;

    @GetMapping("/set")
    public Object set(@RequestParam String key, @RequestParam String value) {
//        redisTemplate.opsForValue().set(key, value);
        redisOperator.set(key, value);
        return "OK";
    }

    @GetMapping("/get")
    public String get(@RequestParam String key) {
//        return (String) redisTemplate.opsForValue().get(key);
        return redisOperator.get(key);
    }

    @GetMapping("/delete")
    public String delete(@RequestParam String key) {
//        redisTemplate.delete(key);
        redisOperator.del(key);
        return "OK";
    }

}
