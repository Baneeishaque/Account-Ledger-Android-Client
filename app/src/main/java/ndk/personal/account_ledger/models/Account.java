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

    public Account(String accountType, String accountId, String notes, String parentAccountId, String ownerId, String name, String commodityType, String commodityValue) {
        this.accountType = accountType;
        this.accountId = accountId;
        this.notes = notes;
        this.parentAccountId = parentAccountId;
        this.ownerId = ownerId;
        this.name = name;
        this.commodityType = commodityType;
        this.commodityValue = commodityValue;
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
        return
                "Account{" +
                        "account_type = '" + accountType + '\'' +
                        ",account_id = '" + accountId + '\'' +
                        ",notes = '" + notes + '\'' +
                        ",parent_account_id = '" + parentAccountId + '\'' +
                        ",owner_id = '" + ownerId + '\'' +
                        ",name = '" + name + '\'' +
                        ",commodity_type = '" + commodityType + '\'' +
                        ",commodity_value = '" + commodityValue + '\'' +
                        "}";
    }
}
