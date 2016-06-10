package alvin.example.com.tabtracker;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Settle extends AppCompatActivity {

    Context ctx = this;
    EditText MEMO, AMOUNT;
    String memo, amount, user, balance, date;
    String type = "set";
    double owed;
    double owe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settle);

        Bundle extras = getIntent().getExtras();
        user = extras.getString("name");

        getTotals();
        balance = getImbalance();

    }

    // submits transaction: if success, add to database and go to dashboard
    public void submit (View view) {

        MEMO = (EditText) findViewById(R.id.memoInput);
        AMOUNT = (EditText) findViewById(R.id.amountInput);
        memo = MEMO.getText().toString();
        amount = AMOUNT.getText().toString();
        double total = Double.parseDouble(balance);
        date = DatabaseOperations.getDate();

        DatabaseOperations db = new DatabaseOperations(ctx);

        if (total > 0) {
            total -= Double.parseDouble(amount);
            owe -= Double.parseDouble(amount);
        }
        else if (total < 0) {
            total += Double.parseDouble(amount);
            owed -= Double.parseDouble(amount);
        }
        else {
            Toast.makeText(getBaseContext(), "Balance is $0.00", Toast.LENGTH_SHORT).show();
        }
        db.addTab(db, user, date, amount, memo, type, "none", Double.toString(total));
        db.addTotal(db, owed, owe);

        db.close();

        // go to dashboard
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
        finish();
    }

    // find balance
    public String getImbalance() {
        DatabaseOperations db = new DatabaseOperations(ctx);
        Cursor cr = db.getUserTab(db, user);
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
}
