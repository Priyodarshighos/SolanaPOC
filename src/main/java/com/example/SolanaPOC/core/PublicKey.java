package com.example.SolanaPOC.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.bitcoinj.core.Base58;
import org.bitcoinj.core.Sha256Hash;
import com.example.SolanaPOC.utils.ByteUtils;
import com.example.SolanaPOC.utils.PublicKeySerializer;
import com.example.SolanaPOC.utils.TweetNaclFast;

@JsonSerialize(using = PublicKeySerializer.class)
public class PublicKey {

    public static final int PUBLIC_KEY_LENGTH = 32;

    private final byte[] pubkey;

    public PublicKey(String pubkey) {
        if (pubkey.length() < PUBLIC_KEY_LENGTH) {
            throw new IllegalArgumentException("Invalid public key input: length must be at least " + PUBLIC_KEY_LENGTH);
        }
        this.pubkey = Base58.decode(pubkey);
    }

    public PublicKey(byte[] pubkey) {
        if (pubkey.length != PUBLIC_KEY_LENGTH) {
            throw new IllegalArgumentException("Invalid public key input: length must be exactly " + PUBLIC_KEY_LENGTH);
        }
        this.pubkey = Arrays.copyOf(pubkey, PUBLIC_KEY_LENGTH);
    }

    public static com.example.SolanaPOC.core.PublicKey readPubkey(byte[] bytes, int offset) {
        byte[] buf = ByteUtils.readBytes(bytes, offset, PUBLIC_KEY_LENGTH);
        return new com.example.SolanaPOC.core.PublicKey(buf);
    }

    public byte[] toByteArray() {
        return pubkey;
    }

    public String toBase58() {
        return Base58.encode(pubkey);
    }

    public boolean equals(com.example.SolanaPOC.core.PublicKey pubkey) {
        return Arrays.equals(this.pubkey, pubkey.toByteArray());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(pubkey);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof com.example.SolanaPOC.core.PublicKey)) return false;
        com.example.SolanaPOC.core.PublicKey other = (com.example.SolanaPOC.core.PublicKey) o;
        return Arrays.equals(this.pubkey, other.pubkey);
    }

    public String toString() {
        return toBase58();
    }

    public static com.example.SolanaPOC.core.PublicKey createProgramAddress(List<byte[]> seeds, com.example.SolanaPOC.core.PublicKey programId) {
        try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            for (byte[] seed : seeds) {
                if (seed.length > 32) {
                    throw new IllegalArgumentException("Max seed length exceeded: " + seed.length);
                }
                buffer.write(seed);
            }
            buffer.write(programId.toByteArray());
            buffer.write("ProgramDerivedAddress".getBytes());

            byte[] hash = Sha256Hash.hash(buffer.toByteArray());

            if (TweetNaclFast.is_on_curve(hash) != 0) {
                throw new IllegalStateException("Invalid seeds, address must fall off the curve");
            }

            return new com.example.SolanaPOC.core.PublicKey(hash);
        } catch (IOException e) {
            throw new RuntimeException("Error creating program address", e);
        }
    }

    public static class ProgramDerivedAddress {
        private com.example.SolanaPOC.core.PublicKey address;
        private int nonce;

        public ProgramDerivedAddress(com.example.SolanaPOC.core.PublicKey address, int nonce) {
            this.address = address;
            this.nonce = nonce;
        }

        public com.example.SolanaPOC.core.PublicKey getAddress() {
            return address;
        }

        public int getNonce() {
            return nonce;
        }

    }

    public static com.example.SolanaPOC.core.PublicKey.ProgramDerivedAddress findProgramAddress(List<byte[]> seeds, com.example.SolanaPOC.core.PublicKey programId) {
        for (int nonce = 255; nonce >= 0; nonce--) {
            try {
                List<byte[]> seedsWithNonce = new ArrayList<>(seeds);
                seedsWithNonce.add(new byte[] { (byte) nonce });
                com.example.SolanaPOC.core.PublicKey address = createProgramAddress(seedsWithNonce, programId);
                return new com.example.SolanaPOC.core.PublicKey.ProgramDerivedAddress(address, nonce);
            } catch (IllegalStateException e) {
                // Address was on the curve, try next nonce
            }
        }
        throw new IllegalStateException("Unable to find a viable program address nonce");
    }

    public static com.example.SolanaPOC.core.PublicKey valueOf(String publicKey) {
        return new com.example.SolanaPOC.core.PublicKey(publicKey);
    }

}
