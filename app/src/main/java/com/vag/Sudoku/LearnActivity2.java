package com.vag.Sudoku;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LearnActivity2 extends AppCompatActivity {

    private TextView[] cells = new TextView[9];
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable numberCycleRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn2);

        // Handle Play button click
        findViewById(R.id.btn_final_play).setOnClickListener(v -> {
            Intent intent = new Intent(LearnActivity2.this, GameActivity.class);
            startActivity(intent);
            // Slide transition
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            finish();
        });

        // Initialize grid cells
        cells[0] = findViewById(R.id.cell_1);
        cells[1] = findViewById(R.id.cell_2);
        cells[2] = findViewById(R.id.cell_3);
        cells[3] = findViewById(R.id.cell_4);
        cells[4] = findViewById(R.id.cell_5);
        cells[5] = findViewById(R.id.cell_6);
        cells[6] = findViewById(R.id.cell_7);
        cells[7] = findViewById(R.id.cell_8);
        cells[8] = findViewById(R.id.cell_9);

        // Start number animation
        startNumberCycle();
    }

    private void startNumberCycle() {
        numberCycleRunnable = new Runnable() {
            @Override
            public void run() {
                // Loop through all cells
                for (TextView cell : cells) {
                    try {
                        String text = cell.getText().toString();
                        int currentVal = Integer.parseInt(text);

                        int nextVal;

                        // Reset to 0 if 9, else increment
                        if (currentVal == 9) {
                            nextVal = 0;
                        } else {
                            nextVal = currentVal + 1;
                        }

                        // Update text
                        cell.setText(String.valueOf(nextVal));

                    } catch (NumberFormatException e) {
                        cell.setText("1");
                    }
                }

                // Repeat every 1 second
                handler.postDelayed(this, 1000);
            }
        };

        // Start immediately
        handler.post(numberCycleRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop animation to save battery
        handler.removeCallbacks(numberCycleRunnable);
    }
}