package alvin.example.com.tabtracker;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Dashboard extends AppCompatActivity {

    Context ctx = this;
    private double owed = 0.00;
    private double owe = 0.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        loadTotals();
        loadDash();
    }

    // new transaction button is clicked
    public void newTransaction (View view) {
        Intent intent = new Intent(this, NewTransaction.class);
        startActivity(intent);
    }

    // all tabs button is clicked
    public void allTabs (View view) {
        Intent intent = new Intent(this, Tabs.class);
        startActivity(intent);
    }

    // loads totals
    public void loadTotals() {
        TextView owedBox = (TextView) findViewById(R.id.owedBox);
        TextView oweBox = (TextView) findViewById(R.id.oweBox);

        DatabaseOperations db = new DatabaseOperations(ctx);
        Cursor cr = db.getTotal(db);
        if (cr != null && cr.moveToFirst()) {
            do {
                owed = cr.getDouble(0);
                owe = cr.getDouble(1);
            } while (cr.moveToNext());
            owedBox.setText("$" + owed);
            oweBox.setText("$" + owe);
        }
        cr.close();
        db.close();
    }


    // loads news feed
    public void loadDash () {
        LinearLayout holder = (LinearLayout) findViewById(R.id.feedLayout);
        LayoutInflater inflater = LayoutInflater.from(Dashboard.this);
        holder.removeAllViews();
        Log.d("Dashboard", "layout inflated");

        DatabaseOperations db = new DatabaseOperations(ctx);
        Cursor cr = db.getTab(db);
        Log.d("Dashboard", "database set up");
        if (cr != null && cr.moveToFirst()) {
            do {
                // post to feed
                String name = cr.getString(0);
                String date = cr.getString(1);
                String amount = cr.getString(2);
                String reason = cr.getString(3);
                String type = cr.getString(5);
                String loan = cr.getString(6);

                View child = inflater.inflate(R.layout.history_post, null, false);
                TextView tv = (TextView) child.findViewById(R.id.postReason);
                tv.setText(reason);
                tv = (TextView) child.findViewById(R.id.postDate);
                tv.setText(date);
                tv = (TextView) child.findViewById(R.id.transAmount);
                tv.setText("$" + amount);

                tv = (TextView) child.findViewById(R.id.postStatus);
                if (type.equals("trans")) {
                    tv.setText("charged ");
                } else if (type.equals("set")) {
                    tv.setText("paid ");
                }

                tv = (TextView) child.findViewById(R.id.postName1);
                if (loan.equals("yes")) {
                    tv.setText("You ");
                    tv = (TextView) child.findViewById(R.id.postName2);
                    tv.setText(name);
                } else{ //(loan.equals("no")) {
                    tv.setText(name + " ");
                    tv = (TextView) child.findViewById(R.id.postName2);
                    tv.setText("You");
                }
                holder.addView(child);
                Log.d("Dashboard", "posted 1 transaction to feed");
            } while (cr.moveToNext());
        }
        cr.close();
        db.close();
    }

//    public void loadDash () {
//
//        try {
//            AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
//                @Override
//                protected String doInBackground(String... params) {
//                    return null;
//                }
//                @Override
//                protected void onPostExecute(String result) {
//                    try {
//                        LinearLayout holder = (LinearLayout) findViewById(R.id.feedLayout);
//                        holder.removeAllViews();
//                        DatabaseOperations db = new DatabaseOperations(ctx);
//                        Cursor cr = db.getTab(db);
//                        if (cr != null && cr.moveToFirst()) {
//                            do {
//                                // post to feed
//                                String name = cr.getString(0);
//                                String date = cr.getString(1);
//                                String amount = cr.getString(2);
//                                String reason = cr.getString(3);
//                                String type = cr.getString(4);
//                                String loan = cr.getString(5);
//
//                                View child = getLayoutInflater().inflate(R.layout.history_post, null);
//                                TextView tv = (TextView) child.findViewById(R.id.postReason);
//                                tv.setText(reason);
//                                tv = (TextView) child.findViewById(R.id.postDate);
//                                tv.setText(date);
//                                tv = (TextView) child.findViewById(R.id.transAmount);
//                                tv.setText(amount);
//
//                                tv = (TextView) child.findViewById(R.id.postStatus);
//                                if (type.equals("trans")) {
//                                    tv.setText("charged");
//                                } else if (type.equals("set")) {
//                                    tv.setText("paid");
//                                }
//
//                                tv = (TextView) child.findViewById(R.id.postName1);
//                                if (loan.equals("yes")) {
//                                    tv.setText("You");
//                                    tv = (TextView) child.findViewById(R.id.postName2);
//                                    tv.setText(name);
//                                } else if (loan.equals("no")) {
//                                    tv.setText(name);
//                                    tv = (TextView) child.findViewById(R.id.postName2);
//                                    tv.setText("You");
//                                }
//                                holder.addView(child);
//                                Log.d("Dashboard", "posted 1 transaction to feed");
//                            } while (cr.moveToFirst());
//                        }
//                        cr.close();
//                        db.close();
//                    } catch (NumberFormatException e) {
//
//                    }
//                }
//            };
//            task.execute("params");
//        } finally {
//            Log.d("Dashboard", "async task failed");
//        }
//        try {
//            LinearLayout holder = (LinearLayout) findViewById(R.id.feedLayout);
////            holder.removeAllViews();
//            DatabaseOperations db = new DatabaseOperations(ctx);
//            Cursor cr = db.getTab(db);
//            if (cr != null && cr.moveToFirst()) {
//                do {
//                    // post to feed
//                    String name = cr.getString(0);
//                    String date = cr.getString(1);
//                    String amount = cr.getString(2);
//                    String reason = cr.getString(3);
//                    String type = cr.getString(4);
//                    String loan = cr.getString(5);
//
//                    View child = getLayoutInflater().inflate(R.layout.history_post, null);
//                    TextView tv = (TextView) child.findViewById(R.id.postReason);
//                    tv.setText(reason);
//                    tv = (TextView) child.findViewById(R.id.postDate);
//                    tv.setText(date);
//                    tv = (TextView) child.findViewById(R.id.transAmount);
//                    tv.setText(amount);
//
//                    tv = (TextView) child.findViewById(R.id.postStatus);
//                    if (type.equals("trans")) {
//                        tv.setText("charged");
//                    } else if (type.equals("set")) {
//                        tv.setText("paid");
//                    }
//
//                    tv = (TextView) child.findViewById(R.id.postName1);
//                    if (loan.equals("yes")) {
//                        tv.setText("You");
//                        tv = (TextView) child.findViewById(R.id.postName2);
//                        tv.setText(name);
//                    } else if (loan.equals("no")) {
//                        tv.setText(name);
//                        tv = (TextView) child.findViewById(R.id.postName2);
//                        tv.setText("You");
//                    }
//                    holder.addView(child);
//                    Log.d("Dashboard", "posted 1 transaction to feed");
//                } while (cr.moveToFirst());
//            }
//            cr.close();
//            db.close();
//        } catch (NullPointerException e) {
//
//        }
//    }
}
