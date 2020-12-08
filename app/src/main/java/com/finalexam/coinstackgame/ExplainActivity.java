package com.finalexam.coinstackgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class ExplainActivity extends AppCompatActivity {

    ViewPager2 viewPager2;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explain);
        viewPager2 = findViewById(R.id.viewpager);

        ArrayList<DataPage> list = new ArrayList<>();
        list.add(new DataPage(R.drawable.mexplain));
        list.add(new DataPage(R.drawable.nexplain));
        list.add(new DataPage(R.drawable.iexplain));
        list.add(new DataPage(R.drawable.iex2));
        list.add(new DataPage(R.drawable.infback3));
        list.add(new DataPage(R.drawable.infback4));
        list.add(new DataPage(R.drawable.infback5));

        viewPager2.setAdapter(new ViewPagerAdapter(list));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}