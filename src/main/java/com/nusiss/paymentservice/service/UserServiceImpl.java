package com.nusiss.paymentservice.service;

import com.nusiss.commonservice.config.ApiResponse;
import com.nusiss.commonservice.entity.User;
import com.nusiss.commonservice.feign.UserFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserFeignClient userFeignClient;

    @Override
    public ResponseEntity<ApiResponse<User>> getUserById(Integer id) {

        return userFeignClient.getUserById(id);
    }
}
