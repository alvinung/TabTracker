package alvin.example.com.tabtracker;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Tabs extends AppCompatActivity {

    Context ctx = this;
    ArrayList<String> tabs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        String username = "";
        LinearLayout holder = (LinearLayout) findViewById(R.id.feedLayout);
        LayoutInflater inflater = LayoutInflater.from(Tabs.this);
        holder.removeAllViews();
        Log.d("Dashboard", "layout inflated");

        tabs = getUsers();
        Log.d("Tabs", "pulled all users from database");
        for (String user : tabs) {
            View child = inflater.inflate(R.layout.tabs_post, null, false);
            String balance = "";
            String date = "";

            DatabaseOperations db = new DatabaseOperations(ctx);
            Cursor cr = db.getUserTab(db, user);
            if (cr != null && cr.moveToFirst()) {
                do {
                    balance = cr.getString(1);
                    date = cr.getString(2);
                } while (cr.moveToNext());
            }

            TextView tv = (TextView) child.findViewById(R.id.postName1);
            tv.setText(user);
            tv = (TextView) child.findViewById(R.id.postReason);
            tv.setText("Last Transaction: " + date);
            tv = (TextView) child.findViewById(R.id.transAmount);
            if (Double.parseDouble(balance) > 0) {
                tv.setTextColor(0xFF07BF32);
                tv.setText(balance);
            }
            else if (Double.parseDouble(balance) < 0) {
                tv.setTextColor(0xFFD30012);
                tv.setText(balance);
            }
            else {
                tv.setText("0.00");
            }
            tv = (TextView) child.findViewById(R.id.postDate);
            if (Double.parseDouble(balance) == 0) {
                tv.setText("Closed");
            }
            else {
                tv.setText("Open");
            }
            tv = (TextView) child.findViewById(R.id.postName2);
            tv.setText("");
            tv = (TextView) child.findViewById(R.id.postStatus);
            tv.setText("");
            holder.addView(child);
        }
    }

    // post is clicked
    public void getDetails (View view) {
        TextView user = (TextView) view.findViewById(R.id.postName1);
        String username = (String) user.getText();

        Intent intent = new Intent(view.getContext(), Details.class);
        intent.putExtra("name",username);
        view.getContext().startActivity(intent);
    }

    // new transaction button is clicked
    public void newTransaction (View view) {
        Intent intent = new Intent(this, NewTransaction.class);
        startActivity(intent);
    }

    // load feed
    public ArrayList<String> getUsers () {
        ArrayList<String> users = new ArrayList<>();

        DatabaseOperations db = new DatabaseOperations(ctx);
        Cursor cr = db.getUsers(db);
        if (cr != null && cr.moveToFirst()) {
            do {
                users.add(cr.getString(0));
            } while (cr.moveToNext());
        }
        cr.close();
        db.close();
        return users;
    }

}
