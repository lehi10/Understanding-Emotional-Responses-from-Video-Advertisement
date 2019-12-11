package com.example.myapplication.Helper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.google.firebase.ml.vision.common.FirebaseVisionPoint;

import java.util.List;

public class PointOverlay extends  GraphicOverlay.Graphic{

    private int RECT_COLOR = Color.RED;
    private float STROKE_WIDTH = 4.0f;
    private Paint pointsPaint;

    private GraphicOverlay graphicOverlay;
    private FirebaseVisionPoint point;

    public PointOverlay(GraphicOverlay graphicOverlay, FirebaseVisionPoint point)
    {
        super(graphicOverlay);

        pointsPaint = new Paint();
        pointsPaint.setColor(RECT_COLOR);
        pointsPaint.setStyle(Paint.Style.STROKE);
        pointsPaint.setStrokeWidth(STROKE_WIDTH);

        this.graphicOverlay = graphicOverlay;
        this.point = point;
        postInvalidate();

    }

    public PointOverlay(GraphicOverlay overlay) {
        super(overlay);
    }


    @Override
    public void draw(Canvas canvas) {

        String textPoints = Float.toString(point.getX()) + " " +Float.toString(point.getY());

        canvas.drawCircle(point.getX(), point.getY(), 1, pointsPaint);

    }
}
