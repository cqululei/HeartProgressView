package net.mertsaygi.heartprogress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by mertsaygi on 04/01/16.
 */
public class HeartProgressBar extends View {

    private int progress = 0;
    private Paint paint;
    private Path path;
    private int secondRoundWidth = 4;

    /*
    *
    * Renkleri firstPaint ve secondPaintten düzenliyorsun. Kullanırken FloatPoint ve HeartProgressBar
    * classlarını kendi projene aktarmayı atlama :)
    * secondRoundWidth ikinci turun yani progressin et kalınlığını belirler.
    * ilk halkanın et kalınlığını firstPaintteki setStrokeWidthden değiştirebilirsin
    *
    * */

    public HeartProgressBar(Context context) {
        super(context);
    }

    public HeartProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeartProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Path firstPart(Canvas canvas,float length,float x,float y){
        path = new Path();

        canvas.rotate(45, x, y);

        path.moveTo(x, y);
        path.lineTo(x - length, y);
        path.arcTo(new RectF(x - length - (length / 2), y - length, x - (length / 2), y), 90, 180);
        path.arcTo(new RectF(x - length, y - length - (length / 2), x, y - (length / 2)), 180, 180);
        path.lineTo(x, y);
        path.close();

        return path;
    }

    private Paint firstPaint(){
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeJoin(Paint.Join.MITER);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(15);
        paint.setStyle(Paint.Style.STROKE);

        return paint;
    }

    private Paint secondPaint(){
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeJoin(Paint.Join.MITER);
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(15);
        paint.setStyle(Paint.Style.STROKE);

        return paint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float length = canvas.getWidth()/2 - 40;
        float x = canvas.getWidth()/2;
        float y = canvas.getHeight()/2;

        canvas.drawPath(firstPart(canvas,length,x,y), firstPaint());

        FloatPoint[] pointArray = getPoints();

        for (int i = 0 ;i <progress ;i++){
            canvas.drawCircle(pointArray[i].getX(), pointArray[i].getY(), secondRoundWidth , secondPaint());
            invalidate();
        }

    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    private FloatPoint[] getPoints() {
        FloatPoint[] pointArray = new FloatPoint[100];
        PathMeasure pm = new PathMeasure(path, false);
        float length = pm.getLength();
        float distance = 0f;
        float speed = length / 100;
        int counter = 0;
        float[] aCoordinates = new float[2];
        while ((distance < length) && (counter < 100)) {
            // get point from the path
            pm.getPosTan(distance, aCoordinates, null);
            pointArray[counter] = new FloatPoint(aCoordinates[0],
                    aCoordinates[1]);
            counter++;
            distance = distance + speed;
        }

        return pointArray;
    }

}
