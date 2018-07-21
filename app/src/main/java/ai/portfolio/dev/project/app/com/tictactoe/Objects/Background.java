package ai.portfolio.dev.project.app.com.tictactoe.Objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.SurfaceView;

public class Background {
    private Bitmap image;
    private int WIDTH,HEIGHT;
    private int x,y,dx;
    private boolean isRunning;

    public Background(Bitmap image){
        this.image = image;
        this.WIDTH = image.getWidth();
        this.HEIGHT = image.getHeight();
        this.isRunning =false;
    }

    private void update(){
        while(isRunning) {
            x += dx;
            if (x < -WIDTH) x = 0;
        }
    }
    public void startAnimation(){
        this.isRunning=true;
        update();
    }
    public void stopAnimation(){
        this.isRunning=false;
    }
    private void draw(Canvas canvas){
        canvas.drawBitmap(image,x,y,null);
        if(x<0)canvas.drawBitmap(image,x+WIDTH,y,null);
    }

    public void setVector(int dx){
        this.dx=dx;
    }

    public void overrideCanvas(SurfaceView surfaceView, Canvas canvas) {
            final float scaleFactorX = surfaceView.getWidth()/WIDTH;
            final float scaleFactorY = surfaceView.getHeight()/HEIGHT;

            if(canvas!=null){
                final  int savadState = canvas.save();
                canvas.scale(scaleFactorX,scaleFactorY);
                this.draw(canvas);
                canvas.restoreToCount(savadState);
            }

    }
}
