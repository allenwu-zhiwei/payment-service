package com.nusiss.paymentservice.service;


import com.nusiss.commonservice.config.ApiResponse;
import com.nusiss.commonservice.entity.User;
import org.springframework.http.ResponseEntity;

public interface UserService {

    public ResponseEntity<ApiResponse<User>> getUserById(Integer id);
}
