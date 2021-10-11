/* Wriiten By Kumail Khan*/
package com.sharklabs.ams.wallet;

import com.amazonaws.services.waf.model.WafAction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletRespository extends JpaRepository<Wallet,Long> {
    Wallet findByUserUUID(String userUUID);
    List<Wallet> findByUserUUIDAndOrgUUID(String userUUID, String orgUUID);
    List<Wallet>findByUserUUIDAndOrgUUIDAndWalletType(String userUUID,String orgUUID,String walletType);
    Wallet findByUserUUIDAndWalletType(String userUUID,String walletType);
    Wallet findByOrgUUIDAndWalletType(String orgUUID,String walletType);
    Wallet findByWalletUUID(String walletUUID);
    List<Wallet> findByWalletTypeAndOrgUUID(String walletType,String orgUUID);
    List<Wallet> findByWalletTypeAndOrgUUIDAndProductType(String walletType,String orgUUID,String productType);


}
