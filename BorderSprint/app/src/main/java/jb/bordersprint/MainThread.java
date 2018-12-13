package jb.bordersprint;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread{
    public static final int MAX_FPS = 30;
    private double avgFPS;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;
    private static Canvas canvas;

    public void setRunning(boolean running){
        this.running=running;
    }

    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel){
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run() {
        long startTime;
        long timeMil = 1000/MAX_FPS;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;
        long targetTime = 1000/MAX_FPS;

        //Setting running to false ends the game
        while(running){
            startTime = System.nanoTime();
            canvas = null;

            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    //Updates the values to current values
                    this.gamePanel.update();
                    //Displays this to the screen
                    this.gamePanel.draw(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if(canvas != null){
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }catch (Exception e){e.printStackTrace();}
                }
            }
            //Gets time in miliseconds
            timeMil = (System.nanoTime() - startTime)/1000000;
            waitTime = targetTime - timeMil;
            try {
                if (waitTime > 0)
                    //Will pause for that amount of wait time
                    this.sleep(waitTime);
            } catch(Exception e){e.printStackTrace();}

            totalTime +=System.nanoTime() - startTime;
            frameCount ++;
            if(frameCount == MAX_FPS){
                avgFPS = 1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0;
                System.out.println(avgFPS);
            }
        }
    }
}
