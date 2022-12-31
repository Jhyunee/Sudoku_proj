package com.example.sudoku;

import static android.view.View.VISIBLE;
import static android.view.View.inflate;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    CustomButton[][] buttons = new CustomButton[9][9];

    CustomButton clickedCustomButton;
    CustomButton longClickedCustomButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BoardGenerator board = new BoardGenerator();

        Integer[][] random = new Integer[9][9];

        ToggleButton[] toggle = new ToggleButton[9];

        View dialogView = inflate(this, R.layout.dialog_memo, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Memo")
                .setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.framelayout);
                        frameLayout.setVisibility(View.INVISIBLE);

                        TableLayout table = (TableLayout) longClickedCustomButton.findViewById(R.id.memoTableLayout);

                        // If toggle is not checked, set memo invisible
                        for (int c = 0; c < 9; c++) {
                            if (!toggle[c].isChecked())
                                longClickedCustomButton.memos[c].setVisibility(View.INVISIBLE);
                            else
                                longClickedCustomButton.memos[c].setVisibility(View.VISIBLE);
                        }
                        table.setVisibility(View.VISIBLE);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.framelayout);
                        frameLayout.setVisibility(View.INVISIBLE);
                    }
                })
                .setNeutralButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.framelayout);
                        frameLayout.setVisibility(View.INVISIBLE);
                        for (int c = 0; c < 9; c++)
                            longClickedCustomButton.memos[c].setVisibility(View.INVISIBLE);
                    }
                });
        AlertDialog dialog = builder.create();

        for (int i = 0; i < 9; i++) {

            TableLayout table;
            table = (TableLayout) findViewById(R.id.tableLayout);
            TableRow tableRow = new TableRow(this);
            table.addView(tableRow);

            for (int j = 0; j < 9; j++) {
                // Create 1~100 random numbers
                random[i][j] = (int) (Math.random() * 100) + 1; // + 47 for quick test
                buttons[i][j] = new CustomButton(this, i, j);

                TableRow.LayoutParams layoutParams =
                        new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT,
                                0.1f
                        );

                // Modify button size
                buttons[i][j].setPadding(0, 6, 0, 6);

                // Modify gap between buttons
                layoutParams.setMargins(6, 6, 6, 6);

                if (j%3 ==0){
                    layoutParams.setMargins(30, 6, 6, 6);
                }
                if (i%3 == 0) {
                    layoutParams.setMargins(6, 30, 6, 6);
                    if (j%3 == 0) {
                        layoutParams.setMargins(30, 30, 6, 6);
                    }
                    if (j == 8) {
                        layoutParams.setMargins(6, 30, 30, 6);
                    }
                }
                else {
                    if (j == 8)
                        layoutParams.setMargins(6, 6, 30, 6);
                }

                buttons[i][j].setLayoutParams(layoutParams);
                tableRow.addView(buttons[i][j]);

                int number = board.get(i, j);

                // Some numbers must not be entered ; select random num 1~49
                int least = (int) (Math.random() * 49) + 1;
                int leastI = (int) (Math.random() * 9);
                int leastJ = (int) (Math.random() * 9);
                random[leastI][leastJ] = least;

                // Set number if random >= 50 ; random[leastI][leastJ] naturally gets empty
                if (random[i][j] >= 50) {
                    buttons[i][j].set(number);
                }
                else {
                    buttons[i][j].set(0);
                    buttons[i][j].TextView.setTextColor(Color.BLUE);
                    buttons[i][j].setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            FrameLayout frameLayout = (FrameLayout) findViewById(R.id.framelayout);
                            frameLayout.setVisibility(View.VISIBLE);
                            TableLayout tableLayout = (TableLayout) findViewById(R.id.numPad);
                            tableLayout.setVisibility(View.VISIBLE);

                            // Parameter view ; clicked customButton
                            clickedCustomButton = (CustomButton) view;

                            // Hide memo when number is selected
                            TableLayout table = (TableLayout) clickedCustomButton.findViewById(R.id.memoTableLayout);
                            table.setVisibility(View.INVISIBLE);
                            for (int c = 0; c < 9; c++)
                                clickedCustomButton.memos[c].setVisibility(View.INVISIBLE);
                        }
                    });
                    buttons[i][j].setOnLongClickListener(new View.OnLongClickListener(){
                        @Override
                        public boolean onLongClick(View view) {
                            FrameLayout frameLayout = (FrameLayout) findViewById(R.id.framelayout);
                            frameLayout.setVisibility(View.VISIBLE);

                            longClickedCustomButton = (CustomButton) view;

                            // Get ToggleButtons in dialog_memo
                            toggle[0] = (ToggleButton) dialogView.findViewById(R.id.toggleButton1);
                            toggle[1] = (ToggleButton) dialogView.findViewById(R.id.toggleButton2);
                            toggle[2] = (ToggleButton) dialogView.findViewById(R.id.toggleButton3);
                            toggle[3] = (ToggleButton) dialogView.findViewById(R.id.toggleButton4);
                            toggle[4] = (ToggleButton) dialogView.findViewById(R.id.toggleButton5);
                            toggle[5] = (ToggleButton) dialogView.findViewById(R.id.toggleButton6);
                            toggle[6] = (ToggleButton) dialogView.findViewById(R.id.toggleButton7);
                            toggle[7] = (ToggleButton) dialogView.findViewById(R.id.toggleButton8);
                            toggle[8] = (ToggleButton) dialogView.findViewById(R.id.toggleButton9);

                            // If memos TextView is Visible, set toggle checked
                            for (int i = 0; i < 9; i++) {
                                toggle[i].setChecked(longClickedCustomButton.memos[i].getVisibility() == VISIBLE);
                            }
                            dialog.show();
                            return true;
                        }
                    });
                }
            }
        }
    }

    // Check if game is finished
    public void checkWin(View v) {
        int countFull = 0;
        int countConflict = countConflict(clickedCustomButton);
        Toast toast = Toast.makeText(this, "Win! Game Finished.", Toast.LENGTH_SHORT);

        // Check if numbers are all filled in
        for (int i=0; i<9; i++) {
            for (int j=0; j<9; j++) {
                if ((buttons[i][j].value != 0))
                    ++countFull;
            }
        }
        // Winning Message & Fix numbers
        if ((countFull == 81) && (countConflict == 0)) {
            toast.show();
            for (int i=0; i<9; i++) {
                for (int j=0; j<9; j++)
                    buttons[i][j].setOnClickListener(null);
            }
        }

    }

    // Reset total game & board
    public void resetClick(View v) {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    // Restart game with existing board
    public void restartClick(View v) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (buttons[i][j].TextView.getCurrentTextColor() == 0xFF0000FF)
                    buttons[i][j].set(0);
                buttons[i][j].setBackgroundColor(Color.WHITE);

                for (int k = 0; k < 9; k++)
                    buttons[i][j].memos[k].setVisibility(View.INVISIBLE);
            }
        }

    }

    // Set Number on clickedCustomButton
    public void onClickNum1(View v) {
        clickedCustomButton.set(1);
        TableLayout tableLayout = (TableLayout) findViewById(R.id.numPad);
        tableLayout.setVisibility(View.INVISIBLE);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.framelayout);
        frameLayout.setVisibility(View.INVISIBLE);
        setConflict(v);
        unsetConflict(v);
        checkWin(v);
    }

    public void onClickNum2(View v) {
        clickedCustomButton.set(2);
        TableLayout tableLayout = (TableLayout) findViewById(R.id.numPad);
        tableLayout.setVisibility(View.INVISIBLE);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.framelayout);
        frameLayout.setVisibility(View.INVISIBLE);
        setConflict(v);
        unsetConflict(v);
        checkWin(v);
    }

    public void onClickNum3(View v) {
        clickedCustomButton.set(3);
        TableLayout tableLayout = (TableLayout) findViewById(R.id.numPad);
        tableLayout.setVisibility(View.INVISIBLE);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.framelayout);
        frameLayout.setVisibility(View.INVISIBLE);
        setConflict(v);
        unsetConflict(v);
        checkWin(v);
    }

    public void onClickNum4(View v) {
        clickedCustomButton.set(4);
        TableLayout tableLayout = (TableLayout) findViewById(R.id.numPad);
        tableLayout.setVisibility(View.INVISIBLE);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.framelayout);
        frameLayout.setVisibility(View.INVISIBLE);
        setConflict(v);
        unsetConflict(v);
        checkWin(v);
    }

    public void onClickNum5(View v) {
        clickedCustomButton.set(5);
        TableLayout tableLayout = (TableLayout) findViewById(R.id.numPad);
        tableLayout.setVisibility(View.INVISIBLE);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.framelayout);
        frameLayout.setVisibility(View.INVISIBLE);
        setConflict(v);
        unsetConflict(v);
        checkWin(v);
    }

    public void onClickNum6(View v) {
        clickedCustomButton.set(6);
        TableLayout tableLayout = (TableLayout) findViewById(R.id.numPad);
        tableLayout.setVisibility(View.INVISIBLE);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.framelayout);
        frameLayout.setVisibility(View.INVISIBLE);
        setConflict(v);
        unsetConflict(v);
        checkWin(v);
    }

    public void onClickNum7(View v) {
        clickedCustomButton.set(7);
        TableLayout tableLayout = (TableLayout) findViewById(R.id.numPad);
        tableLayout.setVisibility(View.INVISIBLE);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.framelayout);
        frameLayout.setVisibility(View.INVISIBLE);
        setConflict(v);
        unsetConflict(v);
        checkWin(v);
    }

    public void onClickNum8(View v) {
        clickedCustomButton.set(8);
        TableLayout tableLayout = (TableLayout) findViewById(R.id.numPad);
        tableLayout.setVisibility(View.INVISIBLE);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.framelayout);
        frameLayout.setVisibility(View.INVISIBLE);
        setConflict(v);
        unsetConflict(v);
        checkWin(v);
    }

    public void onClickNum9(View v) {
        clickedCustomButton.set(9);
        TableLayout tableLayout = (TableLayout) findViewById(R.id.numPad);
        tableLayout.setVisibility(View.INVISIBLE);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.framelayout);
        frameLayout.setVisibility(View.INVISIBLE);
        setConflict(v);
        unsetConflict(v);
        checkWin(v);
    }

    public void onClickCancel(View v) {
        TableLayout tableLayout = (TableLayout) findViewById(R.id.numPad);
        tableLayout.setVisibility(View.INVISIBLE);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.framelayout);
        frameLayout.setVisibility(View.INVISIBLE);
        // Resume memos when number is not selected & empty
        if (clickedCustomButton.value == 0) {
            TableLayout table = (TableLayout) clickedCustomButton.findViewById(R.id.memoTableLayout);
            table.setVisibility(View.VISIBLE);
        }
    }

    public void onClickDel(View v) {
        clickedCustomButton.set(0);
        TableLayout tableLayout = (TableLayout) findViewById(R.id.numPad);
        tableLayout.setVisibility(View.INVISIBLE);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.framelayout);
        frameLayout.setVisibility(View.INVISIBLE);
        unsetConflict(clickedCustomButton);
        // Remove all memos
        for (int c = 0; c < 9; c++)
            longClickedCustomButton.memos[c].setVisibility(View.INVISIBLE);

    }

    // Set conflict
    public void setConflict(View v) {
        // Check conflicts on all numbers (9x9)*(9x9)
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {

                int smallI = i / 3;  // for conflict in small (3x3)
                int smallJ = j / 3;
                int check = buttons[i][j].value;

                for (int k = 0; k < 9; k++) {
                    for (int l = 0; l < 9; l++) {
                        if ((check == buttons[k][l].value) && (check != 0)) {
                            if (((i == k) || (j == l)) && !((i == k) && (j == l))) // if same row & col
                                buttons[k][l].setBackgroundColor(Color.RED);
                            if ((smallI == k / 3) && (smallJ == l / 3) && !((i == k) && (j == l))) // if same (3x3)
                                buttons[k][l].setBackgroundColor(Color.RED);
                        }
                    }
                }
            }
        }

    }

    // Unset Conflict
    public void unsetConflict(View v) {
        // Initialize all conflicts
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++)
                buttons[i][j].setBackgroundColor(Color.WHITE);
        }
        // Set conflict again
        setConflict(v);

    }

    // Count conflict
    public int countConflict (View v) {
        // For winning check
        int count = 0;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int row = i / 3;
                int col = j / 3;
                int check = buttons[i][j].value;

                for (int k = 0; k < 9; k++) {
                    for (int l = 0; l < 9; l++) {
                        if ((check == buttons[k][l].value) && (check != 0)) {
                            if (((i == k) || (j == l)) && !((i == k) && (j == l)))
                                count++;
                            if ((row == k / 3) && (col == l / 3) && !((i == k) && (j == l)))
                                count++;
                        }
                    }
                }
            }
        }

        return count;
    }

}
