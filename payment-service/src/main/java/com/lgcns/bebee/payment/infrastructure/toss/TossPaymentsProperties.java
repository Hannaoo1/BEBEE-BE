package com.lgcns.bebee.payment.infrastructure.toss;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "toss.payments")
@Getter
@Setter
public class TossPaymentsProperties {
    private String baseUrl = "https://api.tosspayments.com";
    private String secretKey;
    private String clientKey;
    private int timeout = 10000;
    private String apiVersion = "2022-11-16";
}
