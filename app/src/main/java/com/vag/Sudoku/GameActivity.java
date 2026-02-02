package com.vag.Sudoku;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private int[][] board;
    private boolean[][] isFixed;
    private TextView[][] cellViews;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private static final int BOARD_SIZE = 9;
    private GridLayout gridLayout;

    // Custom Toast variables
    private CardView customToastContainer;
    private TextView customToastText;
    private Handler toastHandler = new Handler(Looper.getMainLooper());
    private Runnable hideToastRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Initialize logic arrays
        board = new int[BOARD_SIZE][BOARD_SIZE];
        isFixed = new boolean[BOARD_SIZE][BOARD_SIZE];
        cellViews = new TextView[BOARD_SIZE][BOARD_SIZE];

        gridLayout = findViewById(R.id.sudoku_grid);

        // Initialize custom toast views
        customToastContainer = findViewById(R.id.custom_toast_container);
        customToastText = findViewById(R.id.tv_custom_toast_msg);
        customToastText.setTypeface(ResourcesCompat.getFont(this, R.font.ztnature_medium));

        // Setup UI components
        setupDifficultySpinner();
        setupKeypad();

        // Button listeners
        findViewById(R.id.btn_check).setOnClickListener(v -> checkSolution());
        findViewById(R.id.btn_solve).setOnClickListener(v -> solveGame());
    }

    // Show custom toast message with animation
    private void showCustomMessage(String message, boolean isSuccess) {
        if (hideToastRunnable != null) {
            toastHandler.removeCallbacks(hideToastRunnable);
        }

        customToastText.setText(message);

        // Set text color based on success state
        if (isSuccess) {
            customToastText.setTextColor(Color.parseColor("#E88E2E"));
        } else {
            customToastText.setTextColor(Color.RED);
        }

        // Fade in animation
        customToastContainer.setAlpha(0f);
        customToastContainer.setVisibility(View.VISIBLE);
        customToastContainer.animate()
                .alpha(1f)
                .setDuration(300)
                .setListener(null);

        // Hide after 2 seconds
        hideToastRunnable = () -> {
            customToastContainer.animate()
                    .alpha(0f)
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            customToastContainer.setVisibility(View.GONE);
                        }
                    });
        };
        toastHandler.postDelayed(hideToastRunnable, 2000);
    }

    // Setup difficulty dropdown menu
    private void setupDifficultySpinner() {
        Spinner spinner = findViewById(R.id.spinner_difficulty);
        String[] difficulties = {"Easy", "Medium", "Hard"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, difficulties);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getChildAt(0) != null) {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#E88E2E"));
                    ((TextView) parent.getChildAt(0)).setTextSize(20);
                    ((TextView) parent.getChildAt(0)).setTypeface(ResourcesCompat.getFont(GameActivity.this, R.font.ztnature_medium));
                }
                int holes = 35;
                switch (position) {
                    case 0: holes = 20; break;
                    case 1: holes = 35; break;
                    case 2: holes = 50; break;
                }
                startNewGame(holes);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    // Start a new game with specified empty cells
    private void startNewGame(int holes) {
        selectedRow = -1;
        selectedCol = -1;
        generateNewGame(holes);
        buildBoard(gridLayout);
    }

    // Build the Sudoku grid UI dynamically
    private void buildBoard(GridLayout gridLayout) {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        float density = getResources().getDisplayMetrics().density;
        int totalPadding = (int) (80 * density);
        int cellSize = (screenWidth - totalPadding) / 9;

        gridLayout.removeAllViews();

        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                TextView cell = new TextView(this);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = cellSize;
                params.height = cellSize;
                params.setMargins(2, 2, 2, 2);
                cell.setLayoutParams(params);

                cell.setGravity(Gravity.CENTER);
                cell.setTextSize(cellSize / 4.5f);
                cell.setTypeface(ResourcesCompat.getFont(this, R.font.ztnature_medium));
                cell.setTextColor(Color.BLACK);

                if (board[r][c] != 0) {
                    cell.setText(String.valueOf(board[r][c]));
                    cell.setBackgroundResource(R.drawable.circle_orange);
                    isFixed[r][c] = true;
                } else {
                    cell.setText("");
                    cell.setBackgroundResource(R.drawable.circle_grey);
                    isFixed[r][c] = false;
                }

                final int row = r;
                final int col = c;
                cell.setOnClickListener(v -> onCellClicked(row, col));

                cellViews[r][c] = cell;
                gridLayout.addView(cell);
            }
        }
    }

    // Handle cell selection
    private void onCellClicked(int r, int c) {
        if (isFixed[r][c]) return;
        if (selectedRow != -1 && selectedCol != -1) {
            cellViews[selectedRow][selectedCol].setBackgroundResource(R.drawable.circle_grey);
        }
        selectedRow = r;
        selectedCol = c;
        cellViews[r][c].setBackgroundResource(R.drawable.cell_selected_border);
    }

    // Setup number keypad listeners
    private void setupKeypad() {
        int[] buttonIds = {
                R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4, R.id.btn_5,
                R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9
        };
        for (int i = 0; i < buttonIds.length; i++) {
            int number = i + 1;
            findViewById(buttonIds[i]).setOnClickListener(v -> inputNumber(number));
        }
        findViewById(R.id.btn_delete).setOnClickListener(v -> inputNumber(0));
    }

    // Input number into selected cell
    private void inputNumber(int number) {
        if (selectedRow == -1 || selectedCol == -1) return;
        board[selectedRow][selectedCol] = number;

        if (number == 0) cellViews[selectedRow][selectedCol].setText("");
        else cellViews[selectedRow][selectedCol].setText(String.valueOf(number));

        cellViews[selectedRow][selectedCol].setTextColor(Color.BLACK);
    }

    // Check user solution
    private void checkSolution() {
        if (isValidBoard(board)) {
            boolean isFull = true;
            for(int i=0; i<9; i++)
                for(int j=0; j<9; j++)
                    if(board[i][j] == 0) isFull = false;

            if(isFull) {
                showCustomMessage("Congratulations! You solved it!", true);
            } else {
                showCustomMessage("So far so good! Keep going!", true);
            }
        } else {
            showCustomMessage("Something is wrong! Check again.", false);
        }
    }

    // Solve the game automatically
    private void solveGame() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (!isFixed[i][j]) board[i][j] = 0;
            }
        }

        if (solve(board)) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    cellViews[i][j].setText(String.valueOf(board[i][j]));
                    cellViews[i][j].setTextColor(Color.BLACK);
                }
            }
            if (selectedRow != -1 && selectedCol != -1) {
                cellViews[selectedRow][selectedCol].setBackgroundResource(R.drawable.circle_grey);
                selectedRow = -1;
                selectedCol = -1;
            }
            showCustomMessage("Puzzle solved!", true);
        } else {
            showCustomMessage("No solution found.", false);
        }
    }

    // Generate a new Sudoku board
    private void generateNewGame(int holes) {
        board = generateBoardWithHoles(holes);
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                isFixed[i][j] = (board[i][j] != 0);
            }
        }
    }

    // Generate valid board and remove numbers
    public static int[][] generateBoardWithHoles(int holes) {
        int SIZE = 9;
        int[][] newBoard = new int[SIZE][SIZE];
        Random rand = new Random();
        fillDiagonalBoxes(newBoard);
        solve(newBoard);
        int count = holes;
        while (count != 0) {
            int cellId = rand.nextInt(SIZE*SIZE);
            int i = cellId / SIZE;
            int j = cellId % SIZE;
            if (newBoard[i][j] != 0) {
                newBoard[i][j] = 0;
                count--;
            }
        }
        return newBoard;
    }

    // Helper: Fill diagonal 3x3 boxes
    private static void fillDiagonalBoxes(int[][] board) {
        for (int i = 0; i < 9; i = i + 3) fillBox(board, i, i);
    }

    // Helper: Fill a specific 3x3 box
    private static void fillBox(int[][] board, int row, int col) {
        int num;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                do { num = new Random().nextInt(9) + 1; } while (!isSafeInBox(board, row, col, num));
                board[row + i][col + j] = num;
            }
        }
    }

    // Helper: Check if number is safe in 3x3 box
    private static boolean isSafeInBox(int[][] board, int rowStart, int colStart, int num) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[rowStart + i][colStart + j] == num) return false;
            }
        }
        return true;
    }

    // Backtracking solver
    public static boolean solve(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if (board[row][column] == 0) {
                    for (int k = 1; k <= 9; k++) {
                        board[row][column] = k;
                        if (isValid(board, row, column) && solve(board)) return true;
                        board[row][column] = 0;
                    }
                    return false;
                }
            }
        }
        return true;
    }

    // Validation checks
    private static boolean isValid(int[][] board, int row, int column) {
        return rowConstraint(board, row) && columnConstraint(board, column) && subsectionConstraint(board, row, column);
    }
    private static boolean rowConstraint(int[][] board, int row) {
        boolean[] constraint = new boolean[9];
        for (int column = 0; column < 9; column++) {
            if (board[row][column] != 0) {
                if (constraint[board[row][column] - 1]) return false;
                constraint[board[row][column] - 1] = true;
            }
        }
        return true;
    }
    private static boolean columnConstraint(int[][] board, int column) {
        boolean[] constraint = new boolean[9];
        for (int row = 0; row < 9; row++) {
            if (board[row][column] != 0) {
                if (constraint[board[row][column] - 1]) return false;
                constraint[board[row][column] - 1] = true;
            }
        }
        return true;
    }
    private static boolean subsectionConstraint(int[][] board, int row, int column) {
        boolean[] constraint = new boolean[9];
        int subsectionRowStart = (row / 3) * 3;
        int subsectionColumnStart = (column / 3) * 3;
        for (int r = subsectionRowStart; r < subsectionRowStart + 3; r++) {
            for (int c = subsectionColumnStart; c < subsectionColumnStart + 3; c++) {
                if (board[r][c] != 0) {
                    if (constraint[board[r][c] - 1]) return false;
                    constraint[board[r][c] - 1] = true;
                }
            }
        }
        return true;
    }

    // Check if whole board is valid
    public boolean isValidBoard(int[][] brd) {
        for (int i = 0; i < 9; i++) {
            boolean[] present = new boolean[10];
            for (int j = 0; j < 9; j++) {
                int val = brd[i][j];
                if (val != 0) {
                    if (present[val]) return false;
                    present[val] = true;
                }
            }
        }
        for (int j = 0; j < 9; j++) {
            boolean[] present = new boolean[10];
            for (int i = 0; i < 9; i++) {
                int val = brd[i][j];
                if (val != 0) {
                    if (present[val]) return false;
                    present[val] = true;
                }
            }
        }
        for (int i = 0; i < 9; i += 3) {
            for (int j = 0; j < 9; j += 3) {
                boolean[] present = new boolean[10];
                for (int r = 0; r < 3; r++) {
                    for (int c = 0; c < 3; c++) {
                        int val = brd[i + r][j + c];
                        if (val != 0) {
                            if (present[val]) return false;
                            present[val] = true;
                        }
                    }
                }
            }
        }
        return true;
    }
}