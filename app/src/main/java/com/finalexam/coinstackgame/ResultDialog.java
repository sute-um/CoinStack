package com.finalexam.coinstackgame;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ResultDialog extends Dialog {
    private Context context;
    private CustumDialogClickListener custumDialogClickListener;
    private TextView title, score, positive, negative;

    public ResultDialog(@NonNull Context context, CustumDialogClickListener custumDialogClickListener) {
        super(context);
        this.context = context;
        this.custumDialogClickListener = custumDialogClickListener;
    }

    public ResultDialog(@NonNull Context context) {

        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_result);

        title = findViewById(R.id.dialogTitleText);
        score = findViewById(R.id.scoreText);
        positive = findViewById(R.id.positive);
        negative = findViewById(R.id.negative);

        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custumDialogClickListener.onPositiveClick();
                dismiss();
            }
        });

        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custumDialogClickListener.onNegativeClick();
                dismiss();
            }
        });
    }
}
