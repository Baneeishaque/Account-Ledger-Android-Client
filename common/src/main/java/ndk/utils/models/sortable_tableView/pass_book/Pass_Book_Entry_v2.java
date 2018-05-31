package ndk.utils.models.sortable_tableView.pass_book;

import java.util.Date;

public class Pass_Book_Entry_v2 {

    private Date insertion_date;
    private String particulars;
    private String to_account_name;
    private double credit_amount;

    public double getDebit_amount() {
        return debit_amount;
    }

    public void setDebit_amount(double debit_amount) {
        this.debit_amount = debit_amount;
    }

    private double debit_amount;
    private double balance;

    public Pass_Book_Entry_v2(Date insertion_date, String particulars, String to_account_name, double credit_amount, double debit_amount, double balance) {
        this.insertion_date = insertion_date;
        this.particulars = particulars;
        this.to_account_name = to_account_name;
        this.credit_amount = credit_amount;
        this.debit_amount = debit_amount;
        this.balance = balance;
    }

    public Date getInsertion_date() {
        return insertion_date;
    }

    public void setInsertion_date(Date insertion_date) {
        this.insertion_date = insertion_date;
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public String getTo_account_name() {
        return to_account_name;
    }

    public void setTo_account_name(String to_account_name) {
        this.to_account_name = to_account_name;
    }

    public double getCredit_amount() {
        return credit_amount;
    }

    public void setCredit_amount(double credit_amount) {
        this.credit_amount = credit_amount;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
