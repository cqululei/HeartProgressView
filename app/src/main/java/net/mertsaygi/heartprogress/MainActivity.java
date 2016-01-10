package net.mertsaygi.heartprogress;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private HeartProgressBar heartProgressBar;
    private HeartView mHeartLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        heartProgressBar = (HeartProgressBar) findViewById(R.id.heart_progress_bar);
        heartProgressBar.setBackColor(Color.GRAY);
        heartProgressBar.setProgressColor(Color.GREEN);

        mHeartLayout = (HeartView) findViewById(R.id.heart_progress_bar3);
        mHeartLayout.createHeart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<=100;i++) {
                    final int d = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            heartProgressBar.setProgress(d);
                            mHeartLayout.setProgress(d);
                        }
                    });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        th.start();
    }
}
