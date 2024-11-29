package com.example.SolanaPOC.utils.bip32.wallet.key;

import com.example.SolanaPOC.utils.bip32.wallet.key.HdKey;

/**
 * Defines a key with a given public key
 */
public class HdPublicKey extends HdKey {
    private byte[] publicKey;

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }
}
