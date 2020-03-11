package ndk.personal.account_ledger.models;

public class Account {
    private String accountType;
    private String accountId;
    private String notes;
    private String parentAccountId;
    private String ownerId;
    private String name;
    private String commodityType;
    private String commodityValue;
    private String full_name;

    public Account(String accountType, String accountId, String notes, String parentAccountId, String ownerId, String name, String commodityType, String commodityValue, String full_name) {

        this.accountType = accountType;
        this.accountId = accountId;
        this.notes = notes;
        this.parentAccountId = parentAccountId;
        this.ownerId = ownerId;
        this.name = name;
        this.commodityType = commodityType;
        this.commodityValue = commodityValue;
        this.full_name = full_name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getParentAccountId() {
        return parentAccountId;
    }

    public void setParentAccountId(String parentAccountId) {
        this.parentAccountId = parentAccountId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(String commodityType) {
        this.commodityType = commodityType;
    }

    public String getCommodityValue() {
        return commodityValue;
    }

    public void setCommodityValue(String commodityValue) {
        this.commodityValue = commodityValue;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountType='" + accountType + '\'' +
                ", accountId='" + accountId + '\'' +
                ", notes='" + notes + '\'' +
                ", parentAccountId='" + parentAccountId + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", name='" + name + '\'' +
                ", commodityType='" + commodityType + '\'' +
                ", commodityValue='" + commodityValue + '\'' +
                ", full_name='" + full_name + '\'' +
                '}';
    }
}
