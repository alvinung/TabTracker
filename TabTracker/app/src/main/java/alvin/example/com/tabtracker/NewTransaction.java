package alvin.example.com.tabtracker;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.opengl.EGLDisplay;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class NewTransaction extends AppCompatActivity {

    EditText NAME, AMOUNT, REASON;
    CheckBox exY, exN, loY, loN;
    int ex = 0;
    int lo = 0;
    String name, amount, reason, date, loan;
    String type = "trans";
    double owed = 0.00;
    double owe = 0.00;
    Boolean exist = false;
    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transaction);

        getTotals();

        exY = (CheckBox) findViewById(R.id.yesBox1);
        exN = (CheckBox) findViewById(R.id.noBox1);
        loY = (CheckBox) findViewById(R.id.yesBox2);
        loN = (CheckBox) findViewById(R.id.noBox2);

        exY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    ex = 1;
                }
                else {
                    ex = 2;
                }
            }
        });

//        exN.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (((CheckBox) v).isChecked()) {
//                    ex = 2;
//                }
//            }
//        });

        loY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    lo = 1;
                }
                else {
                    lo = 2;
                }
            }
        });

//        loN.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (((CheckBox) v).isChecked()) {
//                    lo = 2;
//                }
//            }
//        });
    }

    // submits transaction: if success, add to database and go to dashboard
    public void submit (View view) {

        NAME = (EditText) findViewById(R.id.nameInput);
        AMOUNT = (EditText) findViewById(R.id.amountInput1);
        REASON = (EditText) findViewById(R.id.reasonInput);
        name = NAME.getText().toString();
        amount = AMOUNT.getText().toString();
        reason = REASON.getText().toString();

        DatabaseOperations db = new DatabaseOperations(ctx);
        exist = checkExist(name, db);

    //    DatabaseOperations dbWrite = new DatabaseOperations(ctx);
        date = DatabaseOperations.getDate();

        // checking loan status
        if (lo == 1) {
            loan = "yes";
        }
        else { //(lo == 2) {
            loan = "no";
        }

        Log.d("******Exist******", Integer.toString(ex));
        // checking for existing tabs
        if (ex != 1 && exist) {
            // already exists, try again
            Toast.makeText(getBaseContext(), "Tab already exists, try a different name", Toast.LENGTH_SHORT).show();
        }
        else {
            // get imbalance
            Double total = Double.parseDouble(getImbalance(name));
            Log.d("******NT*****", loan);

            if (loan.equals("yes")) {
                owed += Double.parseDouble(amount);
                total -= Double.parseDouble(amount);
            }
            else {
                owe += Double.parseDouble(amount);
                total += Double.parseDouble(amount);
            }

            // add to database
            db.addTab(db, name, date, amount, reason, type, loan, total.toString());
//            db.close();
            db.addTotal(db, owed, owe);

            // go to dashboard
            Intent intent = new Intent(this, Dashboard.class);
            startActivity(intent);
            finish();
        }

    }


    public boolean checkExist (String tabName, DatabaseOperations db) {
//        DatabaseOperations db = new DatabaseOperations(ctx);
        Cursor cr = db.getTab(db);
        if (cr != null && cr.moveToFirst()) {
            do {
                if (tabName.equals(cr.getString(0))) {
                    return true;
                }
            } while (cr.moveToNext());
        }
        cr.close();
//        db.close();
        return false;
    }

    // get user imbalance
    public String getImbalance (String name) {
        String balance = "0.00";
        DatabaseOperations db = new DatabaseOperations(ctx);
        Cursor cr = db.getUserTab(db, name);
        if (cr != null && cr.moveToFirst()) {
            do {
                balance = cr.getString(1);
            } while (cr.moveToNext());
        }
        cr.close();
        db.close();
        return balance;
    }

    // get current total data
    public void getTotals () {
        DatabaseOperations db = new DatabaseOperations(ctx);
        Cursor cr = db.getTotal(db);
        if (cr != null && cr.moveToFirst()) {
            do {
                owed = cr.getDouble(0);
                owe = cr.getDouble(1);
            } while (cr.moveToNext());
        }
        cr.close();
        db.close();
    }

    // all submit button is clicked
    public void goDash (View view) {
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
    }
}
