package com.example.SolanaPOC.utils.bip32.wallet;

import com.example.SolanaPOC.utils.bip32.wallet.DerivableType;
import com.example.SolanaPOC.utils.bip32.wallet.HdAddress;
import com.example.SolanaPOC.utils.bip32.wallet.HdKeyGenerator;
import com.example.SolanaPOC.utils.bip32.wallet.SolanaCoin;

/**
 * Utility class for Solana BIP-44 paths
 */
public class SolanaBip44 {
    private final HdKeyGenerator hdKeyGenerator = new HdKeyGenerator();

    private final SolanaCoin solanaCoin;
    private final long PURPOSE;
    private final long TYPE;
    private final long ACCOUNT;
    private final int CHANGE;

    public SolanaBip44(){
        this.solanaCoin = new SolanaCoin();
        this.PURPOSE = solanaCoin.getPurpose();
        this.TYPE = solanaCoin.getCoinType();
        this.ACCOUNT = 0;
        this.CHANGE = 0;
    }


    /**
     * Get a root account address for a given seed using bip44 to match sollet implementation
     *
     * @param seed seed
     * @param derivableType bip44 derivableType
     * @return PrivateKey
     */
    public byte[] getPrivateKeyFromSeed(byte[] seed, DerivableType derivableType) {
        switch (derivableType){
            case BIP44:
                return getPrivateKeyFromBip44Seed(seed);
            case BIP44CHANGE:
                return getPrivateKeyFromBip44SeedWithChange(seed);
            default:
                throw new RuntimeException("DerivableType not supported");
        }
    }

    private byte[] getPrivateKeyFromBip44SeedWithChange(byte[] seed) {
        com.example.SolanaPOC.utils.bip32.wallet.HdAddress masterAddress = hdKeyGenerator.getAddressFromSeed(seed, solanaCoin);
        com.example.SolanaPOC.utils.bip32.wallet.HdAddress purposeAddress = hdKeyGenerator.getAddress(masterAddress, PURPOSE, solanaCoin.getAlwaysHardened()); // 55H
        com.example.SolanaPOC.utils.bip32.wallet.HdAddress coinTypeAddress = hdKeyGenerator.getAddress(purposeAddress, TYPE, solanaCoin.getAlwaysHardened()); // 501H
        com.example.SolanaPOC.utils.bip32.wallet.HdAddress accountAddress = hdKeyGenerator.getAddress(coinTypeAddress, ACCOUNT, solanaCoin.getAlwaysHardened()); //0H
        com.example.SolanaPOC.utils.bip32.wallet.HdAddress changeAddress = hdKeyGenerator.getAddress(accountAddress, CHANGE, solanaCoin.getAlwaysHardened()); //0H
        return changeAddress.getPrivateKey().getPrivateKey();
    }

    private byte[] getPrivateKeyFromBip44Seed(byte[] seed) {
        com.example.SolanaPOC.utils.bip32.wallet.HdAddress masterAddress = hdKeyGenerator.getAddressFromSeed(seed, solanaCoin);
        com.example.SolanaPOC.utils.bip32.wallet.HdAddress purposeAddress = hdKeyGenerator.getAddress(masterAddress, PURPOSE, solanaCoin.getAlwaysHardened()); // 55H
        com.example.SolanaPOC.utils.bip32.wallet.HdAddress coinTypeAddress = hdKeyGenerator.getAddress(purposeAddress, TYPE, solanaCoin.getAlwaysHardened()); // 501H
        HdAddress accountAddress = hdKeyGenerator.getAddress(coinTypeAddress, ACCOUNT, solanaCoin.getAlwaysHardened()); //0H
        return accountAddress.getPrivateKey().getPrivateKey();
    }
}