package com.finalexam.coinstackgame;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class Data {
    Context context;

    ArrayList<String> nameArr;
    ArrayList<Integer> typeArr;
    ArrayList<Object> dataArr;

    static int IMAGE_RES = 0;

    int hitY = 20;
    float stagecnt = 1;

    public Data(Context c){
        context = c;
        nameArr = new ArrayList<String>();
        typeArr = new ArrayList<Integer>();
        dataArr = new ArrayList<Object>();
    }

    public void addImageResource(String name, int id){
        nameArr.add(name);
        typeArr.add(IMAGE_RES);
        dataArr.add((Drawable)context.getResources().getDrawable(id));
    }

    public void removeImageResource ( String name )
    {
        int idx = nameArr.indexOf( name );
        if( idx == -1 ) return;

        nameArr.remove( idx );
        typeArr.remove( idx );
        dataArr.remove( idx );
    }

    public Drawable getDrawable ( String name )
    {
        int idx = nameArr.indexOf( name );
        if( typeArr.get( idx ) != IMAGE_RES ) return null;
        return (Drawable)dataArr.get( idx );
    }

    public void setHitY(int num) {
        this.hitY -= num;
    }

}
