package com.finalexam.coinstackgame;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderPage extends RecyclerView.ViewHolder {

    private ImageView iv;
    private RelativeLayout rl_layout;

    DataPage data;

    ViewHolderPage(View itemView) {
        super(itemView);
        iv = itemView.findViewById(R.id.iv_explain);
        rl_layout = itemView.findViewById(R.id.rl_layout);
    }

    public void onBind(DataPage data){
        this.data = data;

        iv.setImageResource(data.id);
    }
}
