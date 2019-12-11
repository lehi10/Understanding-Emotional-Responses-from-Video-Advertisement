package com.example.myapplication;

//import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myapplication.Helper.GraphicOverlay;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class VideoPlayerActivity extends YouTubeBaseActivity {

    List<Float> smileProbabilityList;
    List<Float> eyesOpenProbabilityList;


    CameraView cameraView;

    GraphicOverlay graphicOverlay;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);


        smileProbabilityList = new ArrayList<Float>();
        eyesOpenProbabilityList = new ArrayList<Float>();



        Button buttonPlay = findViewById(R.id.buttonPlay);
        final YouTubePlayerView youTubePlayerView= findViewById(R.id.youtubePlayerView);


        Intent intent = getIntent();

        String videoId = intent.getStringExtra("idVideo").toString();
        playVideo(videoId,youTubePlayerView);


        cameraView =(CameraView) findViewById(R.id.camera_view);
        cameraView.setFacing(CameraKit.Constants.FACING_FRONT); // Camara delantera
        graphicOverlay = (GraphicOverlay)findViewById(R.id.graphics_overlay);


        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {

                Bitmap bitmap=cameraKitImage.getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap,cameraView.getWidth(),cameraView.getHeight(),false);
                runFaceDetector(bitmap);
                
                
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });

    }

    private void runFaceDetector(Bitmap bitmap) {


        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);


        FirebaseVisionFaceDetectorOptions options_for_classifications = new FirebaseVisionFaceDetectorOptions.Builder()
                .setPerformanceMode(FirebaseVisionFaceDetectorOptions.FAST)
                .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS).build();


        FirebaseVisionFaceDetector detector_for_classification = FirebaseVision.getInstance().getVisionFaceDetector(options_for_classifications);

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


            //  Saving reaction probability
            smileProbabilityList.add(smileProbability);
            eyesOpenProbabilityList.add((leftEyeOpenProbability+rightEyeOpenProbability)/2.0f);


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

    }

    public void playVideo(final String videoId, YouTubePlayerView youTubePlayerView)
    {
        youTubePlayerView.initialize("AIzaSyA6otESAkD6MnmATepVdjxnV0gUYc2bz7I", new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                //youTubePlayer.cueVideo(videoId);
                //

                youTubePlayer.loadVideo(videoId);

                cameraView.start();
                youTubePlayer.play();


                new CountDownTimer(50000,1000){
                    @Override
                    public void onTick(long millisUntilFinished) {
                        Log.i("message VIDEO countdown", "onTick: helllo asdasdasd");
                        cameraView.captureImage();
                    }
                    @Override
                    public void onFinish() {
                        Log.i("message VIDEO countdown", "Finish");
                        //Toast.makeText(VideoPlayerActivity.this,"Video Finished",Toast.LENGTH_SHORT).show();

                        Float avg = average(smileProbabilityList);
                        Float stdDeviation = sdtDeviation(smileProbabilityList);

                        TextView stdDeviationRes=(TextView)findViewById(R.id.stdDeviationRes);
                        stdDeviationRes.setText(Float.toString(stdDeviation));

                        TextView averageRes =(TextView)findViewById(R.id.averageRes);
                        averageRes.setText(Float.toString(avg));

                        cameraView.stop();

                    }
                }.start();
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });

    }

    public float average(List<Float> listProbability)
    {
        float avg = 0.0f;
        for(int i=0;i<listProbability.size();i++)
        {
            avg+=listProbability.get(i);
        }
        return avg / (float)listProbability.size();
    }

    public float sdtDeviation(List<Float> listProbability)
    {
        float sum = 0.0f;
        float avg = average(listProbability);

        for(int i=0;i<listProbability.size();i++)
        {
            sum+=Math.pow(listProbability.get(i)-avg,2);
        }
        return (float) Math.sqrt(sum/(float)listProbability.size());
    }

}
