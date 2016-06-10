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

        LinearLayout holder = (LinearLayout) findViewById(R.id.feedLayout);
        LayoutInflater inflater = LayoutInflater.from(Tabs.this);
        holder.removeAllViews();
        Log.d("Dashboard", "layout inflated");

        tabs = getUsers();
        Log.d("Tabs", "pulled all users from database");
        for (String user : tabs) {
            View child = inflater.inflate(R.layout.history_post, null, false);
            TextView tv = (TextView) child.findViewById(R.id.postName1);
            tv.setText(user);
            tv = (TextView) child.findViewById(R.id.postReason);
            tv.setText("Last Transaction: 06/09/2016");
            tv = (TextView) child.findViewById(R.id.transAmount);
            tv.setText("$56.65");
            tv = (TextView) child.findViewById(R.id.postDate);
            tv.setText("Open");
            holder.addView(child);
        }
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
