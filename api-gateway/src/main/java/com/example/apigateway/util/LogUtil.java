package com.example.apigateway.util;

import com.example.apigateway.domain.ServiceLog;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogUtil {

    private final MongoTemplate mongoTemplate;

    public void saveLog(String requestId, String call, boolean isRequest, String statusCode) {
        ServiceLog newLog = new ServiceLog(requestId, call, isRequest, statusCode);
        mongoTemplate.insert(newLog);
    }
}
