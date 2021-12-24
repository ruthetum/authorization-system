package com.example.apigateway.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Document(collation = "ServiceLog")
@Getter
@Setter
public class ServiceLog {

    public static final String ID = "id";
    public static final String USER_ID = "userId";
    public static final String CALL = "call";
    public static final String TIMESTAMP = "timestamp";

    @Id
    private String id;
    private String requestId;
    // Todo userId로 변경
    private String call;
    private boolean isRequest;
    private String statusCode;
    private String timestamp;

    public ServiceLog(String requestId, String call, boolean isRequest, String statusCode) {
        this.id = UUID.randomUUID().toString();
        this.requestId = requestId;
        this.call = call;
        this.isRequest = isRequest;
        this.statusCode = statusCode;
        this.timestamp = Timestamp.valueOf(LocalDateTime.now()).toString();
    }
}
