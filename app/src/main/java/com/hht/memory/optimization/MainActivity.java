package com.hht.memory.optimization;

import android.os.Bundle;
import android.view.View;

import com.hht.memory.optimization.utils.CleanAppUtils;
import com.hht.memory.optimization.widget.WaveView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private WaveView waveView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        waveView = findViewById(R.id.waveview);
        waveView.setOnClickListener(this);
        waveView.setProgressWithAnim(CleanAppUtils.getUsedMemoryPercent(this));
    }

    @Override
    public void onClick(View view) {
        CleanAppUtils.killAll(this);
        waveView.setProgressWithAnim(CleanAppUtils.getUsedMemoryPercent(this),2000);
    }
}
