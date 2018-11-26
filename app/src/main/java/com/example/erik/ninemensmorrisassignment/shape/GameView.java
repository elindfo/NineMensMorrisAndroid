package com.example.erik.ninemensmorrisassignment.shape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

public class GameView extends View {

    public GameView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawColor(Color.TRANSPARENT);
        
    }
}
