package net.mertsaygi.heartprogress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
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
    private float firstRoundWidth = 15;
    private float secondRoundWidth = 10;
    private int backColor = Color.RED;
    private int progressColor = Color.YELLOW;
    private int w;
    private int h;
    private Rect rectf;

    public HeartProgressBar(Context context) {
        super(context);
    }

    public HeartProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeartProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Path firstPart(Canvas canvas,float x,float y){
        path = new Path();

        canvas.rotate(45, x, y);

        float l1 = y/2;
        float l2 = y/2;
        path.moveTo(x - l1, y);
        path.lineTo(x - l1, y);

        float left =  x - l1 - (l1 / 2);
        float top =  y - l1;
        float right = x - (l1 / 2);
        float bottom= y;

        float left2= x - l2;
        float top2= y - l2 - (l2 / 2);
        float right2= x;
        float bottom2= y - (l2 / 2);

        path.arcTo(new RectF(left, top, right, bottom), 90, 180);
        path.arcTo(new RectF(left2,top2 , right2,bottom2 ), 180, 180);
        path.lineTo(x, y);
        path.close();

        return path;
    }

    private Paint firstPaint(){
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeJoin(Paint.Join.MITER);
        paint.setColor(backColor);
        paint.setStrokeWidth(firstRoundWidth);
        paint.setStyle(Paint.Style.STROKE);

        return paint;
    }

    private Paint secondPaint(){
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeJoin(Paint.Join.MITER);
        paint.setColor(progressColor);
        paint.setStrokeWidth(secondRoundWidth);
        paint.setStyle(Paint.Style.STROKE);

        return paint;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float mDensity = getResources().getDisplayMetrics().density;
        float centerX = (float) w/2;
        float centerY = (float) h/2;

        rectf = new Rect();
        getLocalVisibleRect(rectf);

        canvas.drawPath(firstPart(canvas, centerX, centerY), firstPaint());

        FloatPoint[] pointArray = getPoints();

        for (int i = 0 ;i < progress; i++) {
            canvas.drawCircle(pointArray[i].getX(), pointArray[i].getY(), (int)secondRoundWidth*mDensity , secondPaint());
            invalidate();
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int desiredWidth = 250;
        int desiredHeight = 250;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            w = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            w = Math.min(desiredWidth, widthSize);
        } else {
            w = desiredWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            h = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            h = Math.min(desiredHeight, heightSize);
        } else {
            h = desiredHeight;
        }
        setMeasuredDimension(w, h);
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

    public void setBackColor(int backColor) {
        this.backColor = backColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }
}
