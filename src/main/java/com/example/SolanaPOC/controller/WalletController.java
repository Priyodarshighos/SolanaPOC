package com.example.SolanaPOC.controller;

import com.example.SolanaPOC.domain.Bip44Request;
import com.example.SolanaPOC.domain.PrivateKeyRequest;
import com.example.SolanaPOC.domain.WalletResponse;
import com.example.SolanaPOC.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/create")
    public WalletResponse createWallet() {
        return walletService.createWallet();
    }

    @PostMapping("/import/bip44")
    public WalletResponse importFromBip44(@RequestBody Bip44Request request) {
        return walletService.importFromBip44(request.mnemonic(), request.passphrase());
    }

    @PostMapping("/import/bip44-change")
    public WalletResponse importFromBip44Change(@RequestBody Bip44Request request) {
        return walletService.importFromBip44Change(request.mnemonic(), request.passphrase());
    }

    @PostMapping("/import/privateKey")
    public WalletResponse importFromPrivateKey(@RequestBody PrivateKeyRequest request) {
        return walletService.importFromPrivateKey(request.privateKey());
    }
}
