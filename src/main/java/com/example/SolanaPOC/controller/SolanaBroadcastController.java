package com.example.SolanaPOC.controller;

import com.example.SolanaPOC.domain.SolanaTransactionResponse;
import com.example.SolanaPOC.domain.SolanaTransferRequest;
import com.example.SolanaPOC.service.SolanaBroadcastService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SolanaBroadcastController {

    private final SolanaBroadcastService broadcastService;

    @Autowired
    public SolanaBroadcastController(SolanaBroadcastService broadcastService) {
        this.broadcastService = broadcastService;
    }

    @PostMapping("/broadcast")
    public SolanaTransactionResponse broadcastTransaction(@RequestBody SolanaTransferRequest request) {
        try {
            return broadcastService.sendTransaction(request);
        } catch (Exception e) {
            return new SolanaTransactionResponse(null, "Transaction failed: " + e);
        }
    }
}

