/*
 * Copyright (C) 2015 tyrantgit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.mertsaygi.heartprogress;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;


public class HeartView extends ImageView {

    private static final Paint sPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    private int mHeartBorderResId = R.drawable.heart_border;
    private static Bitmap sHeartBorder;
    private static final Canvas sCanvas = new Canvas();
    private int progress = 0;
    private Paint paint;
    private Path path;;
    private float firstRoundWidth = 15;
    private int backColor = Color.RED;
    private float secondRoundWidth = 10;
    private int progressColor = Color.YELLOW;

    public HeartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HeartView(Context context) {
        super(context);
    }

    public void createHeart() {
        if (sHeartBorder == null) {
            sHeartBorder = BitmapFactory.decodeResource(getResources(), mHeartBorderResId);
        }
        Bitmap heartBorder = sHeartBorder;
        Bitmap bm = createBitmapSafely(heartBorder.getWidth(), heartBorder.getHeight());
        if (bm == null) {
            return;
        }
        Canvas canvas = sCanvas;
        canvas.setBitmap(bm);
        Paint p = sPaint;
        canvas.drawBitmap(heartBorder, 0, 0, p);
        p.setColorFilter(new PorterDuffColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_ATOP));
        canvas.setBitmap(null);
        setImageDrawable(new BitmapDrawable(getResources(), bm));
    }


    private Path firstPart(Canvas canvas){
        path = new Path();

        float x = 135;
        float y = 300;

        canvas.rotate(45, x, y);

        float l1 = y/2;
        float l2 = y/2;

        path.moveTo(x - (l1), y);
        path.lineTo(x - (l1), y);

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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(firstPart(canvas), firstPaint());

        FloatPoint[] pointArray = getPoints();

        for (int i = 0 ;i < progress; i++) {
            canvas.drawCircle(pointArray[i].getX(), pointArray[i].getY(), (int)secondRoundWidth/2 , secondPaint());
            invalidate();
        }

    }

    private Paint secondPaint(){
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeJoin(Paint.Join.MITER);
        paint.setColor(progressColor);
        paint.setStrokeWidth(secondRoundWidth);
        paint.setStyle(Paint.Style.STROKE);

        return paint;
    }

    private static Bitmap createBitmapSafely(int width, int height) {
        try {
            return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }
        return null;
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

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }

}
