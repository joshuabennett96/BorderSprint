package jb.bordersprint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{
    private MainThread thread;

    private Player player;
    private Point playerPoint;



    public GamePanel (Context context) {
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(),this);

        player = new Player(new Rect(100,100,200,200), Color.rgb(255,0,0));
        playerPoint = new Point (150,150);

        setFocusable(true);


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        thread = new MainThread(getHolder(), this);

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        while(true) {
            try{
                thread.setRunning(false);
                thread.join();
            } catch(Exception e) {e.printStackTrace();}
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_BUTTON_PRESS:
                System.out.println(playerPoint.x + " " + playerPoint.y);
                //Anywhere on the North Edge
                if (playerPoint.y == 150 && playerPoint.x <500){
                    playerPoint.set(playerPoint.x + 2, playerPoint.y);
                }
                //If on the NE Point
                //else if (playerPoint.y==150 && playerPoint.x == 500){
                //    playerPoint.set(playerPoint.x,playerPoint.y+2);
               // }
                //If on the Eastern Edge
                else if (playerPoint.x == 500 && playerPoint.y < 500){
                    playerPoint.set(playerPoint.x,playerPoint.y + 2);
                }
                //If on SE Point
                else if (playerPoint.x > 150 && playerPoint.y == 500){
                    playerPoint.set(playerPoint.x -2,playerPoint.y);
                }
                //If on SW Point
                else if (playerPoint.x == 150 && playerPoint.y > 150){
                    playerPoint.set(playerPoint.x,playerPoint.y -2);
                }
        }

        return true;
        //return super.onTouchEvent(event);
    }

    public void update() {
        player.update(playerPoint);
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);

        canvas.drawColor(Color.WHITE);

        player.draw(canvas);
    }
}
