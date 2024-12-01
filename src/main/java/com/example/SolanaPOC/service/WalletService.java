package com.example.SolanaPOC.service;

import com.example.SolanaPOC.core.Account;
import com.example.SolanaPOC.domain.WalletResponse;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class WalletService {

    public WalletResponse createWallet() {
        Account account = new Account();
        return new WalletResponse(account.getPublicKeyBase58(), account.getPrivateKeyBase58());
    }

    public WalletResponse importFromBip44(String mnemonic, String passphrase) {
        List<String> words = Arrays.asList(mnemonic.split(" "));
        Account account = Account.fromBip44Mnemonic(words, passphrase);
        return new WalletResponse(account.getPublicKeyBase58(), account.getPrivateKeyBase58());
    }

    public WalletResponse importFromBip44Change(String mnemonic, String passphrase) {
        List<String> words = Arrays.asList(mnemonic.split(" "));
        Account account = Account.fromBip44MnemonicWithChange(words, passphrase);
        return new WalletResponse(account.getPublicKeyBase58(), account.getPrivateKeyBase58());
    }

    public WalletResponse importFromPrivateKey(String privateKey) {
        Account account = Account.fromBase58PrivateKey(privateKey);
        return new WalletResponse(account.getPublicKeyBase58(), account.getPrivateKeyBase58());
    }
}
