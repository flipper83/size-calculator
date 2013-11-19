package com.flipper83.sizeCalculator_sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import com.flipper83.sizecalculator.SizeCalculator;
import com.flipper83.sizecalculator.SizeReadyListener;

public class MainActivity extends Activity implements SizeReadyListener {
    /**
     * Called when the activity is first created.
     */

    TextView example;
    SizeCalculator sizeCalculator = SizeCalculator.create();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        example = (TextView) findViewById(R.id.tv_example);
        example.setText(getSizeTitle(example.getWidth(), example.getHeight()));

        sizeCalculator.calculateSize(example,this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        sizeCalculator.calculateSize(example,this);
    }

    @Override
    public void onSizeReady(View view, int witdh, int height) {
        Log.d("borrar", "size ready " + view);
        if(view == example){
            example.setText(getSizeTitle(witdh, height));
        }
    }

    private String getSizeTitle(int witdh, int height) {
        StringBuilder sb = new StringBuilder();
        sb.append("Component Size: ");
        sb.append(witdh);
        sb.append(" ");
        sb.append(height);

        return sb.toString();

    }
}
