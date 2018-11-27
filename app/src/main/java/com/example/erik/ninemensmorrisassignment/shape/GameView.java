package com.example.erik.ninemensmorrisassignment.shape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.erik.ninemensmorrisassignment.R;
import com.example.erik.ninemensmorrisassignment.model.NineMensMorrisGame;

import java.util.ArrayList;
import java.util.List;

public class GameView extends View {

    private static final String TAG = "GameView";

    private List<Drawable> gamePieces;
    private Drawable currentPiece;
    private Drawable background;
    private NineMensMorrisGame model;
    private Drawable blueCircle1;

    private int width;
    private int height;

    private static final int CIRCLE_DIAMETER = 50;

    public GameView(Context context) {
        super(context);

    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        model = NineMensMorrisGame.getInstance();
        gamePieces = new ArrayList<>();
        blueCircle1 = getResources().getDrawable(R.drawable.blue_circle);
        background = getResources().getDrawable(R.drawable.morrisplayfield);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
        blueCircle1.setBounds(
                w / 2 - CIRCLE_DIAMETER / 2,
                h / 2 - CIRCLE_DIAMETER / 2,
                w / 2 + CIRCLE_DIAMETER / 2,
                h / 2 + CIRCLE_DIAMETER / 2
        );
    }

    @Override
    protected void onDraw(Canvas canvas){
        background.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        Log.d(TAG, "canvas width: " + canvas.getWidth() + ", canvas height: " + canvas.getHeight());
        background.draw(canvas);
        blueCircle1.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch(event.getAction()){
            case MotionEvent.ACTION_MOVE:{
                int x = (int)event.getX();
                int y = (int)event.getY();
                Log.d(TAG, "x: " + x + ", y: " + y);
                blueCircle1.setBounds(
                        x - CIRCLE_DIAMETER / 2,
                        y - CIRCLE_DIAMETER / 2,
                        x + CIRCLE_DIAMETER / 2,
                        y + CIRCLE_DIAMETER / 2
                );
                break;
            }
            case MotionEvent.ACTION_DOWN:{
                int x = (int)event.getX();
                int y = (int)event.getY();
                Log.d(TAG, "x: " + x + ", y: " + y);
                break;
            }
            case MotionEvent.ACTION_UP:{

                break;
            }
        }
        invalidate();
        return true;
    }
}
