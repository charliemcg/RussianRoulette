package com.example.klunj.russianroulette;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MediaPlayer emptyChamber = MediaPlayer.create(this, R.raw.emptychamber);
        final MediaPlayer gunShot = MediaPlayer.create(this, R.raw.gunshot);

        final boolean[] chambers = new boolean[6];
        int bullets = 1;

        TextView chamberTextView = (TextView) findViewById(R.id.chamberTextView);
        chamberTextView.setText("Chambers: " + chambers.length);

        TextView bulletTextView = (TextView) findViewById(R.id.bulletTextView);
        bulletTextView.setText("Bullets: " + bullets);

        Random random = new Random();
        final int n = random.nextInt(chambers.length);

        chambers[n] = true;

        final Button multiButton = (Button) findViewById(R.id.multiPurposeButton);
        multiButton.setText("Play");

        final boolean[] gamePlay = {false};

        final int[] i = {0};
        multiButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (gamePlay[0]){
                    if (!chambers[i[0]]) {
                        multiButton.setText("Pull Trigger\nChambers Remaining: " + ((chambers.length - i[0]) -1));
                        System.out.println("Empty. " + chambers[i[0]] + " " + i[0]);
                        emptyChamber.start();
                        i[0]++;
                    }else{
                        multiButton.setText("You Died\nPlay Again?");
                        gunShot.start();
                        gamePlay[0] = false;
                    }
                }else{
                    System.out.println("n: " + n);
                    for (int i = 0; i < chambers.length; i++) {
                        System.out.println("Chamber " + i + " : " + chambers[i]);
                    }
                    gamePlay[0] = true;
                    multiButton.setText("Pull Trigger\nChambers Remaining: 6");
                }
            }
        });
    }
}