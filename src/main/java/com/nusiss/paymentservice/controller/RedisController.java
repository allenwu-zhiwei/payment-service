package com.nusiss.paymentservice.controller;

import com.nusiss.paymentservice.service.RedisCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/redis")
public class RedisController {

    @Autowired
    private RedisCrudService redisCrudService;



    // Save a key-value pair with an expiration time
    @PostMapping("/{key}/{value}/{timeout}")
    public String save(@PathVariable("key") String key, @PathVariable("value") String value, @PathVariable("timeout") Integer timeout){
        return redisCrudService.save(key, value, timeout);
    }

    // Get value by key
    @GetMapping("/{key}")
    public String get(@PathVariable String key) {
        return redisCrudService.get(key);
    }

    // Delete a key
    @DeleteMapping("/{key}")
    public String delete(@PathVariable String key) {
        return redisCrudService.delete(key);
    }

    // Check if key exists
    @GetMapping("/exists/{key}")
    public String exists(@PathVariable String key) {

        return redisCrudService.exists(key);
    }
}
