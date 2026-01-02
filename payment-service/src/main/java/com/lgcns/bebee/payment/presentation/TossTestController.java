package com.lgcns.bebee.payment.presentation;

import com.lgcns.bebee.payment.application.client.TossPaymentsClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/toss")
@RequiredArgsConstructor
public class TossTestController {

    private final TossPaymentsClient tossPaymentsClient;

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Toss Client Ready");
    }
}
