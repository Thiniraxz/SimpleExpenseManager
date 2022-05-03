package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DBHandler extends SQLiteOpenHelper {

    public static final String accounts = "accounts";
    public static final String accountNo = "accountNo";
    public static final String bankName = "bankName";
    public static final String accountHolderName = "accountHolderName";
    public static final String balance = "balance";
    public static final String transactions = "transactions";
    public static final String transactionId = "transactionId";
    public static final String date = "date";
    public static final String type = "type";
    public static final String amount = "amount";

    public DBHandler(@Nullable Context context) {
        super(context, "Thinira_190658B.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createAccQuery = "CREATE TABLE " + accounts + "(" + accountNo + " TEXT PRIMARY KEY, " + bankName + " TEXT, " + accountHolderName + " TEXT, " + balance + " REAL);";
        db.execSQL(createAccQuery);

        String createTransactionQuery = "CREATE TABLE " + transactions + "(" + transactionId + " INTEGER PRIMARY KEY AUTOINCREMENT, " + date + " TEXT, " + accountNo + " TEXT, " + type + " TEXT, " + amount + " REAL);";
        db.execSQL(createTransactionQuery);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}