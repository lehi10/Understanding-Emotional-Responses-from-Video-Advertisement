package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Helper.GraphicOverlay;
import com.example.myapplication.Helper.PointOverlay;
import com.example.myapplication.Helper.PointsOverlay;
import com.example.myapplication.Helper.RectOverlay;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionPoint;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceContour;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark;

import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import org.w3c.dom.Text;

import java.util.List;



import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity  {

    CameraView cameraView;
    GraphicOverlay graphicOverlay;
    Button btnDetect;
    Button btnStartAds;

    AlertDialog waitingDialog;


    ImageButton btnImg_1,btnImg_2,btnImg_3,btnImg_4,btnImg_5,btnImg_6; //Botones de anuncios

    @Override
    protected void onResume(){
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.stop();
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraView =(CameraView) findViewById(R.id.camera_view);

        cameraView.setFacing(CameraKit.Constants.FACING_FRONT); // Camara delantera

        graphicOverlay = (GraphicOverlay)findViewById(R.id.graphics_overlay);

        btnDetect = (Button) findViewById(R.id.btn_detect);

        // Botones de Anuncios
        btnImg_1 = (ImageButton)findViewById(R.id.advertisement_1);
        btnImg_2 = (ImageButton)findViewById(R.id.advertisement_2);
        btnImg_3 = (ImageButton)findViewById(R.id.advertisement_3);
        btnImg_4 = (ImageButton)findViewById(R.id.advertisement_4);
        btnImg_5 = (ImageButton)findViewById(R.id.advertisement_5);
        btnImg_6 = (ImageButton)findViewById(R.id.advertisement_6);

        btnImg_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idVideo = "B8_QMv9Im7Q";
                Intent mainToplayAdsIntent;
                mainToplayAdsIntent = new Intent(MainActivity.this, VideoPlayerActivity.class);
                mainToplayAdsIntent.putExtra("idVideo",idVideo);
                startActivity(mainToplayAdsIntent);
            }
        });

        btnImg_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idVideo = "-PahM0ymGdI";
                Intent mainToplayAdsIntent;
                mainToplayAdsIntent = new Intent(MainActivity.this, VideoPlayerActivity.class);
                mainToplayAdsIntent.putExtra("idVideo",idVideo);
                startActivity(mainToplayAdsIntent);
            }
        });

        btnImg_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainToplayAdsIntent;
                String idVideo = "RENDRigVHWU";
                mainToplayAdsIntent = new Intent(MainActivity.this, VideoPlayerActivity.class);
                mainToplayAdsIntent.putExtra("idVideo",idVideo);
                startActivity(mainToplayAdsIntent);
            }
        });

        btnImg_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainToplayAdsIntent;
                String idVideo = "L0CvEPRc-ao";
                mainToplayAdsIntent = new Intent(MainActivity.this, VideoPlayerActivity.class);
                mainToplayAdsIntent.putExtra("idVideo",idVideo);
                startActivity(mainToplayAdsIntent);
            }
        });

        btnImg_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainToplayAdsIntent;
                String idVideo = "ay2-3vO_B60";
                mainToplayAdsIntent = new Intent(MainActivity.this, VideoPlayerActivity.class);
                mainToplayAdsIntent.putExtra("idVideo",idVideo);
                startActivity(mainToplayAdsIntent);
            }
        });

        btnImg_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainToplayAdsIntent;
                String idVideo = "WNzEHZ_wr_E";
                mainToplayAdsIntent = new Intent(MainActivity.this, VideoPlayerActivity.class);
                mainToplayAdsIntent.putExtra("idVideo",idVideo);
                startActivity(mainToplayAdsIntent);
            }
        });

        waitingDialog = new SpotsDialog.Builder().setContext(this)
                    .setMessage("Please Wait ")
                    .setCancelable(false)
                    .build();

        //event

        btnDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.start();

                cameraView.captureImage();
                graphicOverlay.clear();
            }
        });


        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                waitingDialog.show();

                Bitmap bitmap=cameraKitImage.getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap,cameraView.getWidth(),cameraView.getHeight(),false);
                cameraView.stop();

                runFaceDetector(bitmap);
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });
    }

    private void runFaceDetector(Bitmap bitmap) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);


        FirebaseVisionFaceDetectorOptions options = new FirebaseVisionFaceDetectorOptions.Builder()
                //.setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                .setContourMode(FirebaseVisionFaceDetectorOptions.ALL_CONTOURS)
                .build();

        FirebaseVisionFaceDetectorOptions options_for_classifications = new FirebaseVisionFaceDetectorOptions.Builder()
                .setPerformanceMode(FirebaseVisionFaceDetectorOptions.FAST)
                .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS).build();


        FirebaseVisionFaceDetector detector = FirebaseVision.getInstance().getVisionFaceDetector(options);
        FirebaseVisionFaceDetector detector_for_classification = FirebaseVision.getInstance().getVisionFaceDetector(options_for_classifications);

        //Detector de landmarks and contourns
        detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
            @Override
            public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces) {
                //processFaceResultLandMarks(firebaseVisionFaces);
                processFaceResultContourns(firebaseVisionFaces);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        //Classificador de imagenes

        detector_for_classification.detectInImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
            @Override
            public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces) {
                processFaceResultClassification(firebaseVisionFaces);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void processFaceResultClassification(List<FirebaseVisionFace> firebaseVisionFaces) {

        int count=0;
        for(FirebaseVisionFace face : firebaseVisionFaces)
        {


            float smileProbability=         -1.0f;
            float leftEyeOpenProbability=   -1.0f;
            float rightEyeOpenProbability=  -1.0f;

            if(face.getSmilingProbability() != FirebaseVisionFace.UNCOMPUTED_PROBABILITY)
            {
                smileProbability=face.getSmilingProbability();
            }

            if(face.getLeftEyeOpenProbability() != FirebaseVisionFace.UNCOMPUTED_PROBABILITY)
            {
                leftEyeOpenProbability=face.getSmilingProbability();
            }

            if(face.getRightEyeOpenProbability() != FirebaseVisionFace.UNCOMPUTED_PROBABILITY)
            {
                rightEyeOpenProbability=face.getSmilingProbability();
            }


            Log.i("Smile Probability", "processFaceResultClassification:  "+Float.toString(smileProbability));
            Log.i("LeftEyeOpenProbability", "processFaceResultClassification:  "+Float.toString(leftEyeOpenProbability));
            Log.i("RightEyeOpenProbability", "processFaceResultClassification:  "+Float.toString(rightEyeOpenProbability));

            if(smileProbability > 0.6f)
            {
                TextView textView_emotionalResp = (TextView) findViewById(R.id.textView_emotionalResp);
                textView_emotionalResp.setText("Positive Response");
            }
            if(smileProbability < 0.6f)
            {
                TextView textView_emotionalResp = (TextView) findViewById(R.id.textView_emotionalResp);
                textView_emotionalResp.setText("Neutral or Negative Response");
            }


            count++;
        }
        waitingDialog.dismiss();
        Toast.makeText(this,String.format("Detected %d  faces in image",count), Toast.LENGTH_SHORT).show();

    }

    private void processFaceResultContourns(List<FirebaseVisionFace> firebaseVisionFaces) {
        int count=0;
        for(FirebaseVisionFace face : firebaseVisionFaces)
        {

            //Rect bounds = face.getBoundingBox();
            //RectOverlay rect = new RectOverlay(graphicOverlay,bounds);
            //graphicOverlay.add(rect);

            List<FirebaseVisionPoint> left_contour = face.getContour(FirebaseVisionFaceContour.LEFT_EYE).getPoints();
            List<FirebaseVisionPoint> right_contour = face.getContour(FirebaseVisionFaceContour.RIGHT_EYE).getPoints();
            List<FirebaseVisionPoint> face_contour = face.getContour(FirebaseVisionFaceContour.FACE).getPoints();
            List<FirebaseVisionPoint> nose_bridge_contour = face.getContour(FirebaseVisionFaceContour.NOSE_BRIDGE).getPoints();
            List<FirebaseVisionPoint> upper_lip_bottom_contour = face.getContour(FirebaseVisionFaceContour.LOWER_LIP_TOP).getPoints();
            List<FirebaseVisionPoint> upper_lip_top_contour = face.getContour(FirebaseVisionFaceContour.UPPER_LIP_BOTTOM).getPoints();
            List<FirebaseVisionPoint> right_eyebrown_top_contour = face.getContour(FirebaseVisionFaceContour.RIGHT_EYEBROW_TOP).getPoints();
            List<FirebaseVisionPoint> left_eyebrown_top_contour = face.getContour(FirebaseVisionFaceContour.LEFT_EYEBROW_TOP).getPoints();



            PointsOverlay points_left_contour = new PointsOverlay(graphicOverlay,left_contour);
            PointsOverlay points_right_contour = new PointsOverlay(graphicOverlay,right_contour);
            PointsOverlay points_face_contour = new PointsOverlay(graphicOverlay,face_contour);
            PointsOverlay points_bridge_contour = new PointsOverlay(graphicOverlay,nose_bridge_contour);
            PointsOverlay points_lip_bottom_contour = new PointsOverlay(graphicOverlay,upper_lip_bottom_contour);
            PointsOverlay points_lip_top_contour = new PointsOverlay(graphicOverlay,upper_lip_top_contour);
            PointsOverlay points_right_eyebrown_top_contour = new PointsOverlay(graphicOverlay,right_eyebrown_top_contour);
            PointsOverlay points_left_eyebrown_top_contour = new PointsOverlay(graphicOverlay,left_eyebrown_top_contour);


            graphicOverlay.add(points_left_contour);
            graphicOverlay.add(points_right_contour);
            graphicOverlay.add(points_face_contour);
            graphicOverlay.add(points_bridge_contour);
            graphicOverlay.add(points_lip_bottom_contour);
            graphicOverlay.add(points_lip_top_contour);
            graphicOverlay.add(points_right_eyebrown_top_contour);
            graphicOverlay.add(points_left_eyebrown_top_contour);

            count++;
        }
        waitingDialog.dismiss();
        Toast.makeText(this,String.format("Detected %d  faces in image",count), Toast.LENGTH_SHORT).show();

    }

    private void processFaceResultLandMarks(List<FirebaseVisionFace> firebaseVisionFaces) {
        int count=0;
        for(FirebaseVisionFace face : firebaseVisionFaces)
        {

            Rect bounds = face.getBoundingBox();

            FirebaseVisionFaceLandmark eyeLeft      = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EYE);
            FirebaseVisionFaceLandmark eyeRight     = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EYE);
            FirebaseVisionFaceLandmark mouthLeft    = face.getLandmark(FirebaseVisionFaceLandmark.MOUTH_LEFT);
            FirebaseVisionFaceLandmark mouthRight   = face.getLandmark(FirebaseVisionFaceLandmark.MOUTH_RIGHT);
            FirebaseVisionFaceLandmark mouthBotoom  = face.getLandmark(FirebaseVisionFaceLandmark.MOUTH_BOTTOM);
            FirebaseVisionFaceLandmark leftCheek    = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_CHEEK);
            FirebaseVisionFaceLandmark rightCheek   = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_CHEEK);
            FirebaseVisionFaceLandmark noseBase     = face.getLandmark(FirebaseVisionFaceLandmark.NOSE_BASE);

            PointOverlay point_eyeLeft = new PointOverlay(graphicOverlay,eyeLeft.getPosition());
            PointOverlay point_eyeRight = new PointOverlay(graphicOverlay,eyeRight.getPosition());
            PointOverlay point_mouthLeft = new PointOverlay(graphicOverlay,mouthLeft.getPosition());
            PointOverlay point_mouthRight = new PointOverlay(graphicOverlay,mouthRight.getPosition());
            PointOverlay point_mouthBotoom  = new PointOverlay(graphicOverlay,mouthBotoom.getPosition());
            PointOverlay point_leftCheek = new PointOverlay(graphicOverlay,leftCheek.getPosition());
            PointOverlay point_rightCheek = new PointOverlay(graphicOverlay,rightCheek.getPosition());
            PointOverlay point_noseBase = new PointOverlay(graphicOverlay,noseBase.getPosition());

            graphicOverlay.add(point_eyeLeft);
            graphicOverlay.add(point_eyeRight);
            graphicOverlay.add(point_mouthLeft);
            graphicOverlay.add(point_mouthRight);
            graphicOverlay.add(point_mouthBotoom);
            graphicOverlay.add(point_leftCheek);
            graphicOverlay.add(point_rightCheek);
            graphicOverlay.add(point_noseBase);


            RectOverlay rect = new RectOverlay(graphicOverlay,bounds);
            graphicOverlay.add(rect);
            count++;
        }
        waitingDialog.dismiss();
        Toast.makeText(this,String.format("Detected %d  faces in image",count), Toast.LENGTH_SHORT).show();
    }


}
