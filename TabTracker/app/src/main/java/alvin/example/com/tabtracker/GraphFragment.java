package alvin.example.com.tabtracker;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

public class GraphFragment extends AppCompatActivity {

    Context ctx = this;
    String loan;
    private int lastX = 0;
    private LineGraphSeries<DataPoint> series;
    LinkedList<Double> data = new LinkedList<>();
    ListIterator<Double> iter;
    final int SIZE = 5;
    static double biggest = 0;
    private static final Random RANDOM = new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_fragment);

        Bundle extras = getIntent().getExtras();
        loan = extras.getString("loan");

        TextView titleText = (TextView) findViewById(R.id.graphTitle);
        if (loan.equals("yes")) {
            titleText.setText("How much you've lent");
        }
        else if (loan.equals("no")) {
            titleText.setText("How much you've borrowed");
        }

        DatabaseOperations db = new DatabaseOperations(ctx);
        Cursor cr = db.getDataPoints(db, loan);
        if (cr != null && cr.moveToFirst()) {
            do {
                String point = cr.getString(0);
                if (Double.parseDouble(point) > biggest) {
                    biggest = Double.parseDouble(point);
                }
                data.add(Double.parseDouble(point));
            } while (cr.moveToNext());
            iter = data.listIterator();

        }

        // graph view instance
        GraphView graph = (GraphView) findViewById(R.id.graph);

        series = new LineGraphSeries<>();
        graph.addSeries(series);

        // customize viewports
        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(biggest);
        viewport.setScrollable(true);


    }

    @Override
    protected void onResume() {
        super.onResume();
        // simulate real time data and append to graph
        new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < SIZE; i++) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            addEntry();
                        }
                    });

                    // slow down adding of entries for real time effect
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }).start();
    }

    private void addEntry() {
        // displaying max 10 points
        series.appendData(new DataPoint(lastX++, RANDOM.nextDouble() * biggest), true, 10);
    }
}
