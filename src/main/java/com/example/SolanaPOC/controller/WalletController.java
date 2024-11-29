package com.example.SolanaPOC.controller;

import com.example.SolanaPOC.core.Account;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @GetMapping("/create")
    public Map<String, String> createWallet() {
        // Generate a new Solana account (wallet)
        Account account = new Account();

        // Create a response map with public and private keys
        Map<String, String> wallet = new HashMap<>();
        wallet.put("publicKey", account.getPublicKeyBase58());
        wallet.put("privateKey", account.getPrivateKeyBase58());

        return wallet; // Return the wallet details as JSON
    }
}

