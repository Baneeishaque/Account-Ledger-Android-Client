package ndk.utils.models.sortable_tableView.pass_book;

import java.util.Date;

public class Pass_Book_Entry_v2 {

    private Date insertion_date;
    private String particulars;
    private String second_account_name;
    private double credit_amount;
    private double debit_amount;
    private double balance;
    private int from_account_id;
    private int to_account_id;
    private int id;
    private String from_account_full_name;
    private String to_account_full_name;

    public Pass_Book_Entry_v2(Date insertion_date, String particulars, String second_account_name, double credit_amount, double debit_amount, double balance, int from_account_id, int to_account_id, int id, String from_account_full_name, String to_account_full_name) {
        this.insertion_date = insertion_date;
        this.particulars = particulars;
        this.second_account_name = second_account_name;
        this.credit_amount = credit_amount;
        this.debit_amount = debit_amount;
        this.balance = balance;
        this.from_account_id = from_account_id;
        this.to_account_id = to_account_id;
        this.id = id;
        this.from_account_full_name = from_account_full_name;
        this.to_account_full_name = to_account_full_name;
    }

    public String getSecond_account_name() {
        return second_account_name;
    }

    public void setSecond_account_name(String second_account_name) {
        this.second_account_name = second_account_name;
    }

    public String getFrom_account_full_name() {
        return from_account_full_name;
    }

    public void setFrom_account_full_name(String from_account_full_name) {
        this.from_account_full_name = from_account_full_name;
    }

    public String getTo_account_full_name() {
        return to_account_full_name;
    }

    public void setTo_account_full_name(String to_account_full_name) {
        this.to_account_full_name = to_account_full_name;
    }

    public int getFrom_account_id() {
        return from_account_id;
    }

    public void setFrom_account_id(int from_account_id) {
        this.from_account_id = from_account_id;
    }

    public int getTo_account_id() {
        return to_account_id;
    }

    public void setTo_account_id(int to_account_id) {
        this.to_account_id = to_account_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getDebit_amount() {
        return debit_amount;
    }

    public void setDebit_amount(double debit_amount) {
        this.debit_amount = debit_amount;
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

    @Override
    public String toString() {
        return "Pass_Book_Entry_v2{" +
                "insertion_date=" + insertion_date +
                ", particulars='" + particulars + '\'' +
                ", second_account_name='" + second_account_name + '\'' +
                ", credit_amount=" + credit_amount +
                ", debit_amount=" + debit_amount +
                ", balance=" + balance +
                ", from_account_id=" + from_account_id +
                ", to_account_id=" + to_account_id +
                ", id=" + id +
                ", from_account_full_name='" + from_account_full_name + '\'' +
                ", to_account_full_name='" + to_account_full_name + '\'' +
                '}';
    }
}
