package com.example.myapplication.Helper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.google.firebase.ml.vision.common.FirebaseVisionPoint;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;

import java.util.List;

public class PointsOverlay extends  GraphicOverlay.Graphic{
    private int RECT_COLOR = Color.RED;
    private float STROKE_WIDTH = 4.0f;
    private Paint pointsPaint;

    private GraphicOverlay graphicOverlay;
    private List<FirebaseVisionPoint> points;

    public PointsOverlay(GraphicOverlay graphicOverlay, List<FirebaseVisionPoint> points)
    {
        super(graphicOverlay);

        pointsPaint = new Paint();
        pointsPaint.setColor(RECT_COLOR);
        pointsPaint.setStyle(Paint.Style.STROKE);
        pointsPaint.setStrokeWidth(STROKE_WIDTH);

        this.graphicOverlay = graphicOverlay;
        this.points = points;
        postInvalidate();

    }

    public PointsOverlay(GraphicOverlay overlay) {
        super(overlay);
    }


    @Override
    public void draw(Canvas canvas) {

        for( FirebaseVisionPoint point : points)
        {
            String textPoints = Float.toString(point.getX()) + " " +Float.toString(point.getY());
            Log.i("Point :", textPoints );
            canvas.drawCircle(point.getX(), point.getY(), 0.5f, pointsPaint);

        }
    }
}
