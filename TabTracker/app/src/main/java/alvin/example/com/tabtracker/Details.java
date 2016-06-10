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

public class Details extends AppCompatActivity {

    Context ctx = this;
    String type = "set";
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Bundle extras = getIntent().getExtras();
        username = extras.getString("name");

        loadFeed(username);

    }

    // load feed
    public void loadFeed(String user) {
        TextView nameText = (TextView) findViewById(R.id.usernameText);
        TextView balanceText = (TextView) findViewById(R.id.amountText);

        LinearLayout holder = (LinearLayout) findViewById(R.id.feedLayout);
        LayoutInflater inflater = LayoutInflater.from(Details.this);
        holder.removeAllViews();
        Log.d("Dashboard", "layout inflated");

        DatabaseOperations db = new DatabaseOperations(ctx);
        Cursor cr = db.getSpecTab(db, user);
        Log.d("Details", "database set up");
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
                Log.d("Dashboard", "posted 1 transaction to details feed");
            } while (cr.moveToNext());
        }
        String total = "";
        cr = db.getUserTab(db, user);
        if (cr != null && cr.moveToFirst()) {
            do {
                total = cr.getString(1);
            } while (cr.moveToNext());
        }

        nameText.setText(user);
        balanceText.setText("$" + total);
    }

    // settle button is clicked
    public void settle (View view) {
        Intent intent = new Intent(view.getContext(), Settle.class);
        intent.putExtra("name",username);
        view.getContext().startActivity(intent);
    }

}
