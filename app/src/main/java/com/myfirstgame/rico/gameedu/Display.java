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


public class Display extends View {
    private Position selection;
    private String message;
    private String answer1;
    private String answer2;
    private String answer3;
    private Paint paint;
    public Display(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //Creating the base display for game screen
        message = "Display";
        answer1="1";
        answer2="2";
        answer3="3";
        selection = Position.NONE;
        paint = new Paint();
        paint.setTextSize(100);
        paint.setTextAlign(Paint.Align.CENTER);
        setBackground(ContextCompat.getDrawable(this.getContext(), R.drawable.bg_game));
    }

    private PointF getCenter() {
        return new PointF(getWidth()/2f,getHeight()/2f);
    }

    public Position getPosition(float x, float y) {
        //Setting up the position for the answer button
        if(Math.pow((x-getCenter().x),2)+Math.pow((y-getCenter().y),2)<10000){
            return Position.MIDDLE_BUTTON;
        } else if(Math.pow((x-getCenter().x),2)+Math.pow((y-getCenter().y-300),2)<10000){
            return Position.BOTTOM_BUTTON;
        } else if(Math.pow((x-getCenter().x),2)+Math.pow((y-getCenter().y+300),2)<10000){
            return Position.TOP_BUTTON;
        } else {
            return Position.NONE;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //To draw the blank canvas accordingly
        PointF center = getCenter();
        canvas.drawText(message,center.x,300,paint);
        if(selection == Position.NONE){
            paint.setColor(Color.CYAN);
            canvas.drawCircle(center.x,(center.y-300),100,paint);
            canvas.drawCircle(center.x,(center.y),100,paint);
            canvas.drawCircle(center.x,(center.y+300),100,paint);

            paint.setColor(Color.BLACK);
            canvas.drawText(answer1,center.x,(center.y-270),paint);
            canvas.drawText(answer2,center.x,(center.y+30),paint);
            canvas.drawText(answer3,center.x,(center.y+330),paint);
        } else if (selection == Position.TOP_BUTTON){
            paint.setColor(Color.MAGENTA);
            canvas.drawCircle(center.x,(center.y-300),100,paint);
            paint.setColor(Color.CYAN);
            canvas.drawCircle(center.x,(center.y),100,paint);
            canvas.drawCircle(center.x,(center.y+300),100,paint);

            paint.setColor(Color.BLACK);
            canvas.drawText(answer1,center.x,(center.y-270),paint);
            canvas.drawText(answer2,center.x,(center.y+30),paint);
            canvas.drawText(answer3,center.x,(center.y+330),paint);
        }else if (selection == Position.MIDDLE_BUTTON){
            paint.setColor(Color.CYAN);
            canvas.drawCircle(center.x,(center.y-300),100,paint);
            paint.setColor(Color.MAGENTA);
            canvas.drawCircle(center.x,(center.y),100,paint);
            paint.setColor(Color.CYAN);
            canvas.drawCircle(center.x,(center.y+300),100,paint);

            paint.setColor(Color.BLACK);
            canvas.drawText(answer1,center.x,(center.y-270),paint);
            canvas.drawText(answer2,center.x,(center.y+30),paint);
            canvas.drawText(answer3,center.x,(center.y+330),paint);
        }else if (selection == Position.BOTTOM_BUTTON){
            paint.setColor(Color.CYAN);
            canvas.drawCircle(center.x,(center.y-300),100,paint);
            canvas.drawCircle(center.x,(center.y),100,paint);
            paint.setColor(Color.MAGENTA);
            canvas.drawCircle(center.x,(center.y+300),100,paint);

            paint.setColor(Color.BLACK);
            canvas.drawText(answer1,center.x,(center.y-270),paint);
            canvas.drawText(answer2,center.x,(center.y+30),paint);
            canvas.drawText(answer3,center.x,(center.y+330),paint);
        }
    }

    public void setSelection(Position position){
        //Set the selection for our answer
        selection = position;
    }

    public void setMessage(String message){
        //Setting the question message
        this.message = message;
    }

    public void setAllAnswer(String answer1, String answer2, String answer3){
        //Set all the answer to be displayed
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
    }

    public String getChosenAnswer(){
        //To return the result of chosen answer
        if(selection == Position.TOP_BUTTON){
            return answer1;
        } else if (selection == Position.MIDDLE_BUTTON){
            return answer2;
        } else if(selection == Position.BOTTOM_BUTTON){
            return answer3;
        } else {
            return "NO ANSWER";
        }
    }
}
