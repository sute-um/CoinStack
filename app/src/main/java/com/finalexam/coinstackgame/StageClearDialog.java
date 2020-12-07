package com.finalexam.coinstackgame;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class StageClearDialog extends Dialog {
    private  Context context;
    private  CustumDialogClickListener custumDialogClickListener;
    TextView goMain, next, clearTimeText;


    public StageClearDialog(@NonNull Context context, CustumDialogClickListener custumDialogClickListener) {
        super(context);
        this.context = context;
        this.custumDialogClickListener = custumDialogClickListener;
    }

    public StageClearDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialig_stage_clear);

        goMain = findViewById(R.id.stagemain);
        next = findViewById(R.id.nextstage);
        clearTimeText = findViewById(R.id.stagecleartime);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custumDialogClickListener.onPositiveClick();
                dismiss();
            }
        });

        goMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custumDialogClickListener.onNegativeClick();
                dismiss();
            }
        });
    }
}
