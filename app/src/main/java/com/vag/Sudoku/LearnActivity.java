package com.vag.Sudoku;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import androidx.appcompat.app.AppCompatActivity;

public class LearnActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        // Handle Next button click
        findViewById(R.id.btn_next_page).setOnClickListener(v -> {
            Intent intent = new Intent(LearnActivity.this, LearnActivity2.class);
            startActivity(intent);
            // Slide transition
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // Start animations
        setupRowAnimation();
        setupColumnAnimation();
    }

    // Setup infinite horizontal scrolling
    private void setupRowAnimation() {
        LinearLayout movingRow = findViewById(R.id.row_anim_content);
        HorizontalScrollView scrollView = findViewById(R.id.scroll_container);

        // Disable touch
        scrollView.setOnTouchListener((v, event) -> true);

        movingRow.post(() -> {
            float width = movingRow.getWidth();
            float halfWidth = width / 2.0f;

            // Animate horizontally
            ObjectAnimator animator = ObjectAnimator.ofFloat(movingRow, "translationX", 0f, -halfWidth);

            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.setRepeatMode(ValueAnimator.RESTART);
            animator.setInterpolator(new LinearInterpolator());
            animator.setDuration(20000); // 20 seconds duration

            animator.start();
        });
    }

    // Setup infinite vertical scrolling
    private void setupColumnAnimation() {
        LinearLayout movingCol = findViewById(R.id.col_anim_content);
        ScrollView scrollView = findViewById(R.id.vertical_scroll_container);

        // Disable touch
        scrollView.setOnTouchListener((v, event) -> true);

        movingCol.post(() -> {
            float height = movingCol.getHeight();
            float halfHeight = height / 2.0f;

            // Animate vertically
            ObjectAnimator animator = ObjectAnimator.ofFloat(movingCol, "translationY", 0f, -halfHeight);

            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.setRepeatMode(ValueAnimator.RESTART);
            animator.setInterpolator(new LinearInterpolator());
            animator.setDuration(20000); // 20 seconds duration

            animator.start();
        });
    }
}