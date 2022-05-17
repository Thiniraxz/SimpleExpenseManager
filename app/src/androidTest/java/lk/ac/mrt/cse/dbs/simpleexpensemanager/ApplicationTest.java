
/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.PersistentExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest {

    public ExpenseManager expenseManager;

    @Before
    public void setupDatabase() {
        Context context = ApplicationProvider.getApplicationContext();
        try {
            expenseManager = new PersistentExpenseManager(context);
        } catch (ExpenseManagerException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddAccount(){
        expenseManager.addAccount("500","Commercial","Thinira",10000.0);
        List<String> accountNumberList=expenseManager.getAccountNumbersList();
        assertTrue(accountNumberList.contains("500"));
    }


    @Test
    public void testTransactionLog(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, 05, 17);
        Date transactionDate = calendar.getTime();
        expenseManager.getTransactionsDAO().logTransaction(transactionDate,"500",ExpenseType.INCOME,10000.0);

        List<Transaction> transactionList=expenseManager.getTransactionLogs();
        for (Transaction t:transactionList) {
            if(t.getAccountNo()=="500"){
                assertEquals(t.getAccountNo(),"500");
                assertEquals(t.getExpenseType(),ExpenseType.INCOME);
                assertEquals(t.getAmount(),10000.0,0.0);
            }
        }
    }
}