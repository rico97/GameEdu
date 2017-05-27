package com.myfirstgame.rico.gameedu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;


public class ResultDisplay extends View {
    private Paint paint;
    private String finalScore;
    public ResultDisplay(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //Setting up the initial display
        finalScore = "0";
        paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        setBackground(ContextCompat.getDrawable(this.getContext(), R.drawable.bg_main));
    }

    private PointF getCenter() {
        return new PointF(getWidth()/2f,getHeight()/2f);
    }

    public Position getPosition(float x, float y) {
        //To set up the position for the share and exit button
        if (Math.pow((x - getCenter().x), 2) + Math.pow((y - getCenter().y-100)*4, 2) < 160000) {
            return Position.SHARE_BUTTON;
        } else if (Math.pow((x - getCenter().x), 2) + Math.pow((y - getCenter().y - 400)*4, 2) < 160000) {
            return Position.EXIT_BUTTON;
        } else {
            return Position.NONE;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //To draw the canvas accordingly
        PointF center = getCenter();
        paint.setColor(Color.CYAN);
        canvas.drawOval(center.x-400,center.y,center.x+400,center.y+200,paint);
        canvas.drawOval(center.x-400,center.y+300,center.x+400,center.y+500,paint);
        paint.setColor(Color.BLACK);
        paint.setTextSize(100);
        canvas.drawText("SHARE",center.x,center.y+130,paint);
        canvas.drawText("EXIT",center.x,center.y+430,paint);
        canvas.drawText("Score:",center.x,200,paint);

        paint.setTextSize(300);
        canvas.drawText(finalScore,center.x,600,paint);

    }

    public void setFinalScore(int finalScore){
        //Method used to set the final score to be displayed
        this.finalScore = Integer.toString(finalScore);
    }

}
