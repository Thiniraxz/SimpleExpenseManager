package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class SQLiteAccountDAO extends DBHandler implements AccountDAO {
    private ArrayList<String> accountNumbers;
    private ArrayList<Account> accounts;

    public SQLiteAccountDAO(@Nullable Context context) {
        super(context);
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumbers=new ArrayList<>();

        String sql = "select accountNo from accounts";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);

        if (cursor.moveToFirst()){
            do{
                accountNumbers.add(cursor.getString(0));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return accountNumbers;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accounts=new ArrayList<>();

        String sql = "select * from accounts";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);

        if (cursor.moveToFirst()){
            do{
                String accountNo = cursor.getString(0);
                String bankName = cursor.getString(1);
                String accountHolderName = cursor.getString(2);
                double balance = cursor.getInt(3);

                Account account = new Account(accountNo,bankName,accountHolderName,balance);
                accounts.add(account);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) {

        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from accounts where accountNo = '"+ accountNo + "' ;";

        Cursor cursor = db.rawQuery(sql,null);

        Account account = null;

        String bankName = cursor.getString(1);
        String accountHolderName = cursor.getString(2);
        double balance = cursor.getInt(3);

        account = new Account(accountNo,bankName,accountHolderName,balance);

        cursor.close();
        db.close();
        return account;

    }

    @Override
    public void addAccount(Account account) {
        List<String> accountNumbers=getAccountNumbersList();

        if(!accountNumbers.contains(account.getAccountNo())){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();

            cv.put("accountNo", account.getAccountNo());
            cv.put("bankName", account.getBankName());
            cv.put("accountHolderName", account.getAccountHolderName());
            cv.put("balance", account.getBalance());

            db.insert("accounts", null, cv);

        }
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db=this.getWritableDatabase();
        String remove_account="DELETE FROM "+accounts+" WHERE accountNo = "+accountNo+";";
        db.execSQL(remove_account);
        db.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        List<String> accountNumbers= getAccountNumbersList();
        if(!accountNumbers.contains(accountNo)){
            String msg="Account"+accountNo+"is not valid";
            throw new InvalidAccountException(msg);
        }
        SQLiteDatabase db=this.getWritableDatabase();
        String check_balance = "select "+balance+" from accounts where accountNo = '"+ accountNo + "' ;";
        Cursor cursor = db.rawQuery(check_balance,null);
        cursor.moveToFirst();
        double balance = cursor.getInt(0);
        switch (expenseType) {
            case EXPENSE:
                balance-=amount;
                break;
            case INCOME:
                balance+=amount;
                break;
        }

        String update_balance="UPDATE accounts SET balance = " +balance+" WHERE accountNo = '"+accountNo+"';";
        db.execSQL(update_balance);
        cursor.close();
        db.close();

    }
}