package com.example.SolanaPOC.domain;

public record SolanaTransferRequest(String feePayerPrivateKey, String recipientPublicKey, long amountLamports) {}
