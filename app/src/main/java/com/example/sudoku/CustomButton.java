package com.example.sudoku;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

public class CustomButton extends FrameLayout {

    int row;
    int col;
    int value;
    public android.widget.TextView TextView;

    TextView[] memos = new TextView[9];

    public CustomButton(Context context, int row, int col) {
        super(context);

        this.row = row;
        this.col = col;

        // CustomButton
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTypeface(null, Typeface.BOLD);
        this.TextView = textView;
        addView(textView);

        setClickable(true);
        setBackgroundResource(R.drawable.button_selector);

        // Memo
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TableLayout memo = (TableLayout) layoutInflater.inflate(R.layout.layout_memo, null);

        int k = 0;
        for (int i = 0; i < 3; i++) {
            TableRow tableRow = (TableRow) memo.getChildAt(i);
            for (int j = 0; j < 3; j++, k++) {
                memos[k] = (TextView) tableRow.getChildAt(j);
                memos[k].setVisibility(INVISIBLE);
            }
        }
        addView(memo);

    }

    public void set(int a) {
        this.value = a; // Set value as parameter a

        // Get CustomButton TextView and set number
        TextView textView = this.TextView;
        if (a == 0) {
            textView.setText(null);
        }
        else {
            textView.setText(String.valueOf(a));
        }

    }

}
