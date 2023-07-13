package com.kash.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data
@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private String status;
    private String statusCode;
    private String message;
    private transient Object data;

    private transient Object errorMessage;
    private String successRule;
    @JsonProperty("response_message")
    private String responseMessage = "Request Processed Successfully";
    @JsonProperty("response_type")
    private String responseType;

    private transient Map<String, List<String>> headers;
    private transient InputStreamResource bodyData;

}
