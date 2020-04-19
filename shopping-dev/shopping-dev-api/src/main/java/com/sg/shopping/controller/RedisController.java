package com.sg.shopping.controller;

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

    @GetMapping("/set")
    public Object set(@RequestParam String key, @RequestParam String value) {
        redisTemplate.opsForValue().set(key, value);
        return "OK";
    }

    @GetMapping("/get")
    public String get(@RequestParam String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    @GetMapping("/delete")
    public String delete(@RequestParam String key) {
        redisTemplate.delete(key);
        return "OK";
    }

}
