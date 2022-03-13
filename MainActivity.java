package com.javalearning.catchme;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;


public class MainActivity extends AppCompatActivity {

//region UI
    ImageView imageView;
    TextView textView;
    TextView scoreView;


    int userScore;
    int dpHeight;
    int dpWidth;
//endregion
    Runnable runnable;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.timeText);
        imageView = findViewById(R.id.imageView);
        scoreView = findViewById(R.id.scoreText);
        handler = new Handler();
        userScore = 0;

        screenSizeCalculator();
        scoreView.setText("Score: " + userScore);


        new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textView.setText("Time: " + millisUntilFinished / 1000);
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        //Saniyede 1 kere animasyon kodu calısacak
                        animationHandler();
                        handler.postDelayed(runnable,1000);
                    }
                };
                handler.post(runnable);
            }

            @Override
            public void onFinish() {

                textView.setText("Time's Up!");
                handler.removeCallbacks(runnable);
                imageView.setEnabled(false);

                AlertDialog.Builder timeAlert = new AlertDialog.Builder(MainActivity.this);
                timeAlert.setTitle("Play again?");
                timeAlert.setMessage("Score: " + userScore);
                timeAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Sahneyi resetle
                        recreate();
                    }
                });
                timeAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //App cıkıs
                        finish();
                    }
                });
                timeAlert.show();
            }
        }.start();
    }
    public void animationHandler(){
        //Tek resim animator yardımı ile hareket edecek (Ekran boyutlarının sınırları icerisinde)
        Random rand = new Random();
        int randomX = rand.nextInt(dpWidth + dpWidth) - dpWidth;
        int randomY = rand.nextInt(dpHeight + dpHeight) - dpHeight;
        ObjectAnimator animation = ObjectAnimator.ofFloat(imageView, "translationX", randomX);
        ObjectAnimator animationY = ObjectAnimator.ofFloat(imageView, "translationY", randomY);
        animation.setDuration(1000);
        animationY.setDuration(1000);

        animationY.start();
        animation.start();
    }

    public void screenSizeCalculator(){
        //Ekranın boyutlarını dp cinsinden hesapla
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        dpHeight = (int) (displayMetrics.heightPixels / displayMetrics.density);
        dpWidth = (int) (displayMetrics.widthPixels / displayMetrics.density);
    }

    public void scoreHandler(View view){
        //score event
        userScore++;
        scoreView.setText("Score: " + userScore);
    }
}
