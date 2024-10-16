package com.nusiss.paymentservice.service;

import com.nusiss.commonservice.feign.UserFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisCrudServiceImpl implements RedisCrudService {

    @Autowired
    private UserFeignClient userFeignClient;


    @Override
    public String save(String key, String value, Integer timeout) {
        return userFeignClient.save(key, value, timeout);
    }

    @Override
    public String get(String key) {
        String value =  (String) userFeignClient.get(key);
        if(!value.isBlank()){
            value = value.replaceAll("\\\\", "");
        }

        return value;
    }

    @Override
    public String delete(String key) {
        return userFeignClient.delete(key);
    }

    @Override
    public String exists(String key) {
        return userFeignClient.exists(key);
    }
}