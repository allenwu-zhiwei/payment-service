package com.nusiss.paymentservice.service;

public interface RedisCrudService {

    // Save a key-value pair with expiration time
    String save(String key, String value, Integer timeout);

    // Retrieve value by key
    String get(String key);

    // Delete a key
    String delete(String key);

    // Check if a key exists
    String exists(String key);
}
