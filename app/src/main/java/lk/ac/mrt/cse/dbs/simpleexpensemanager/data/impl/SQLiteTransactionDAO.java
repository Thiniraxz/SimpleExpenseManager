package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class SQLiteTransactionDAO extends DBHandler implements TransactionDAO {

    private List<Transaction> transactionList;

    public SQLiteTransactionDAO(@Nullable Context context) {
        super(context);
        this.transactionList=new ArrayList<Transaction>();
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        String transDate=new SimpleDateFormat("dd-mm-yyyy").format(date);
        cv.put("date",transDate);
        cv.put("accountNo",accountNo);
        cv.put("type",String.valueOf(expenseType));
        cv.put("amount",amount);

        sqLiteDatabase.insert(transactions,null,cv);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        String selectQuery = "select * from transactions";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()){

            do{
                String startdate = cursor.getString(1);
                Date strdate= null;
                try {
                    strdate = new SimpleDateFormat("dd-mm-yyyy").parse(startdate);
                    String accountNo = cursor.getString(2);

                    String type = cursor.getString(3);
                    ExpenseType expenseType = ExpenseType.valueOf(type.toUpperCase());

                    double amount = cursor.getDouble(4);

                    Transaction transaction = new Transaction(strdate,accountNo,expenseType,amount);
                    transactionList.add(transaction);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return transactionList;


    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {

        transactionList = getAllTransactionLogs();
        int size = transactionList.size();

        if (size <= limit) {
            return transactionList;
        }
        return transactionList.subList(size - limit, size);

    }
}