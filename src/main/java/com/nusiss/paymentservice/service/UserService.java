package com.nusiss.paymentservice.service;


import com.nusiss.commonservice.config.ApiResponse;
import com.nusiss.commonservice.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface UserService {

    public ResponseEntity<ApiResponse<User>> getUserById(Integer id);

    public ResponseEntity<ApiResponse<User>> getCurrentUserInfo(String authToken);

}
