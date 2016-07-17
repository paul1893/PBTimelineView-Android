package com.thefrenchtouch.timelineview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.thefrenchtouch.pbtimelineview.object.PBItem;
import com.thefrenchtouch.pbtimelineview.widget.PBTimelineView;

public class MainActivity extends AppCompatActivity implements PBTimelineView.onItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PBTimelineView mTimelineView = (PBTimelineView) findViewById(R.id.pbtimelineview);
        mTimelineView.setOnItemClickListener(this);
        mTimelineView.setTextItems(new String[][]{
                {"Boxe", "Dance", "Boxe", "Dance", "Boxe", "Dance"},
                {"MMA", "Step", "Muscu"},
                {},
                {"Course", "Aviron", "Boxe", "Dance"},
                {"Volley-ball", "Soccer", "Boxe", "Dance", "Boxe", "Dance"},
                {"BasketBall"},
                {"Rally", "Cuisine", "F1"}
        });
    }

    @Override
    public void onItemClick(int section, int num, String text, PBItem item) {
        Toast.makeText(this, "Section: " + section + " Item: " + num, Toast.LENGTH_SHORT).show();
        Log.i("Item Click", "You click on item " + num + " in section " + section + " : " + text);
    }
}
