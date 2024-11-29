package com.example.SolanaPOC.controller;

import com.example.SolanaPOC.core.Account;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @GetMapping("/create")
    public Map<String, String> createWallet() {
        Account account = new Account();
        Map<String, String> wallet = new HashMap<>();
        wallet.put("publicKey", account.getPublicKeyBase58());
        wallet.put("privateKey", account.getPrivateKeyBase58());
        return wallet;
    }

    @PostMapping("/import/bip44")
    public Map<String, String> importFromBip44(@RequestBody Map<String, String> body) {
        List<String> words = Arrays.asList(body.get("mnemonic").split(" "));
        String passphrase = body.getOrDefault("passphrase", "");

        Account account = Account.fromBip44Mnemonic(words, passphrase);

        Map<String, String> wallet = new HashMap<>();
        wallet.put("publicKey", account.getPublicKeyBase58());
        wallet.put("privateKey", account.getPrivateKeyBase58());

        return wallet;
    }

    @PostMapping("/import/bip44-change")
    public Map<String, String> importFromBip44Change(@RequestBody Map<String, String> body) {
        List<String> words = Arrays.asList(body.get("mnemonic").split(" "));
        String passphrase = body.getOrDefault("passphrase", "");

        Account account = Account.fromBip44MnemonicWithChange(words, passphrase);

        Map<String, String> wallet = new HashMap<>();
        wallet.put("publicKey", account.getPublicKeyBase58());
        wallet.put("privateKey", account.getPrivateKeyBase58());

        return wallet;
    }

    @PostMapping("/import/privateKey")
    public Map<String, String> importFromPrivateKey(@RequestBody Map<String, String> body) {
        String privateKey = body.get("privateKey");

        Account account = Account.fromBase58PrivateKey(privateKey);

        Map<String, String> wallet = new HashMap<>();
        wallet.put("publicKey", account.getPublicKeyBase58());
        wallet.put("privateKey", account.getPrivateKeyBase58());

        return wallet;
    }



}

