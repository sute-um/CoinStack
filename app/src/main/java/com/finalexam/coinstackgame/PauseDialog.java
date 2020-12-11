package com.finalexam.coinstackgame;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class PauseDialog extends Dialog {
    private Context context;
    private CustumDialogClickListener custumDialogClickListener;
    TextView text, resume;

    public PauseDialog(@NonNull Context context, CustumDialogClickListener custumDialogClickListener) {
        super(context);
        this.context = context;
        this.custumDialogClickListener = custumDialogClickListener;
    }

    public PauseDialog(@NonNull Context context) {

        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pause);

        text = findViewById(R.id.pauseText);
        resume = findViewById(R.id.resume);


        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custumDialogClickListener.onPositiveClick();
                dismiss();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getContext().startActivity(intent);
    }
}
