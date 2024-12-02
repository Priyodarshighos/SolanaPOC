package com.example.SolanaPOC.service;

import com.example.SolanaPOC.domain.SolanaTransactionResponse;
import com.example.SolanaPOC.domain.SolanaTransferRequest;
import org.p2p.solanaj.core.Account;
import org.p2p.solanaj.core.PublicKey;
import org.p2p.solanaj.core.Transaction;
import org.p2p.solanaj.core.TransactionInstruction;
import org.p2p.solanaj.programs.SystemProgram;
import org.p2p.solanaj.rpc.RpcClient;
import org.springframework.stereotype.Service;
import org.bitcoinj.core.Base58;

@Service
public class SolanaBroadcastService {

    private final RpcClient rpcClient;

    public SolanaBroadcastService() {
        this.rpcClient = new RpcClient("https://cool-convincing-owl.solana-testnet.quiknode.pro/09d86c9f96158f24d3c5e5dd016c569131a2e0be/", 20);
    }

    public SolanaTransactionResponse sendTransaction(SolanaTransferRequest request) throws Exception {

        Account feePayer = new Account(Base58.decode(request.feePayerPrivateKey()));
        PublicKey recipient = new PublicKey(request.recipientPublicKey());
        long amount = request.amountLamports();

        Transaction transaction = new Transaction();

        TransactionInstruction transferInstruction = SystemProgram.transfer(
                feePayer.getPublicKey(),
                recipient,
                amount
        );

        transaction.addInstruction(transferInstruction);

        String transactionSignature = rpcClient.getApi().sendTransaction(transaction, feePayer);

        return new SolanaTransactionResponse(transactionSignature, "Transaction successful!");
    }
}

