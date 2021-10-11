/*Written By Kumail Khan*/
package com.sharklabs.ams.fact;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.sharklabs.ams.wallet.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FactRepository  extends JpaRepository<Fact,Long> {
    long countByWalletUUID(String WalletUUID);
    long countByWalletUUIDAndTransactiontypeIsNotContaining(String WalletUUID,String Transactiontype);
    List<Fact> findAllByWalletUUID(String WalletUUID);
    List<Fact> findByWalletUUIDAndTransactiontypeIsNotContaining(String WalletUUID,String TransactionType);
    List<Fact> findAllByWalletUUIDAndTransactiontype(String WalletUUID,String TransactionType);
    Page<Fact> findByWalletUUIDOrderByDateTimeDesc(String WalletUUID,Pageable pageable);
    Page<Fact> findByWalletUUIDAndTransactiontypeNotContainsOrderByDateTimeDesc(String WalletUUID,String TransactionType,Pageable pageable);
    List<Fact> findByWalletUUIDAndTransactiontype(String WalletUUID,String TransactionType);
    Page<Fact> findByWalletUUIDAndTransactiontypeOrderByDateTimeDesc(String WalletUUID,String TransactionType,Pageable pageable);
    Page<Fact> findByWalletUUIDAndTransactiontypeIsNotContainingOrderByDateTimeDesc(String WalletUUID,String TransactionType,Pageable pageable);
}
