package com.sharklabs.ams.wallet;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletRequestRepository extends JpaRepository<WalletRequest,Long> {
WalletRequest findByRequestUUID(String RequestUUID);
List<WalletRequest> findBySenderWalletUUIDAndApproveFlagAndIgnoreFlag(String SenderWalletUUID,Boolean Approve,Boolean Ignore );
List<WalletRequest> findByWalletTypeAndOrgUUID(String walletType,String orgUUID);
List<WalletRequest> findBySenderWalletUUIDOrReceiverWalletUUIDAndWalletTypeAndOrgUUID(String WalletUUID,String WalletUUID1,String walletType,String orgUUID);
List<WalletRequest> findBySenderWalletUUIDOrReceiverWalletUUIDAndWalletTypeAndOrgUUIDAndApproveFlagAndIgnoreFlagAndWithDrawFlag(String WalletUUID,String WalletUUID1,String walletType,String orgUUID,Boolean approve,Boolean ignore,Boolean withdrawn);
List<WalletRequest> findByApproveFlagAndIgnoreFlagAndWithDrawFlagAndSenderWalletUUID(Boolean approve,Boolean ignore,Boolean withdrawn,String WalletUUID);
List<WalletRequest> findByApproveFlagAndIgnoreFlagAndWithDrawFlagAndReceiverWalletUUID(Boolean approve,Boolean ignore,Boolean withdrawn,String WalletUUID);

}
