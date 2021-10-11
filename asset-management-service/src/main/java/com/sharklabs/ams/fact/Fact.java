
/* Wriiten By Kumail Khan*/
package com.sharklabs.ams.fact;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
@Entity(name ="t_fact")
public class Fact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String factUUID;
    private Double total;
    private String walletUUID;
    private Date dateTime;
    private Double currentAverage;
    private Double volume;
    private Double rate;
    private String rateBasisUnit; //Unit
    private String rateCurrency;
    private String quantityUnit;
    private Double quantity;
    private String userUUID;
    private String description;
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getUserUUID() {
        return userUUID;
    }
    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }
    public String getQuantityUnit() {
        return quantityUnit;
    }
    public void setQuantityUnit(String quantityUnit) {
        this.quantityUnit = quantityUnit;
    }
    public Double getQuantity() {
        return quantity;
    }
    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
    public Double getRate() {
        return rate;
    }
    public void setRate(Double rate) {
        this.rate = rate;
    }
    public String getRateBasisUnit() {
        return rateBasisUnit;
    }
    public void setRateBasisUnit(String rateBasisUnit) {
        this.rateBasisUnit = rateBasisUnit;
    }
    public String getRateCurrency() {
        return rateCurrency;
    }
    public void setRateCurrency(String rateCurrency) {
        this.rateCurrency = rateCurrency;
    }
    public Double getVolume() {
        return volume;
    }
    public void setVolume(Double volume) {
        this.volume = volume;
    }
    public Date getDateTime() {
        return dateTime;
    }
    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
    public String getTransactiontype() {
        return transactiontype;
    }
    public void setTransactiontype(String transactiontype) {
        this.transactiontype = transactiontype;
    }
    private String transactiontype;
    public Double getCurrentAverage() {
        return currentAverage;
    }
    public void setCurrentAverage(Double currentAverage) {
        this.currentAverage = currentAverage;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFactUUID() {
        return factUUID;
    }
    public void setFactUUID(String factUUID) {
        this.factUUID = factUUID;
    }
    public Double getTotal() {
        return total;
    }
    public void setTotal(Double total) {
        this.total = total;
    }
    public String getWalletUUID() {
        return walletUUID;
    }
    public void setWalletUUID(String walletUUID) {
        this.walletUUID = walletUUID;
    }
}
