package com.example.klunj.russianroulette;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MediaPlayer emptyChamber = MediaPlayer.create
                (this, R.raw.emptychamber);//<-Empty chamber sound clip.
        final MediaPlayer gunShot = MediaPlayer.create(this, R.raw.gunshot);//<-Gun shot sound clip.

        //Spinner for selecting number of chambers.
        Spinner chamberSpinner = (Spinner) findViewById(R.id.chamberSpinner) ;
        Integer[] chamberValues = new Integer[] {1,2,3,4,5,6,7,8,9,10,11,12};
        ArrayAdapter<Integer> chamberAdapter = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_spinner_dropdown_item, chamberValues);
        chamberSpinner.setAdapter(chamberAdapter);

        int chambersNum = (int) chamberSpinner.getSelectedItem();

        final boolean[] chambers = new boolean[6];//<-Number of chambers in gun.
        final int bullets = 1;//<-Number of bullets in gun.

        TextView chamberTextView = (TextView) findViewById(R.id.chamberTextView);
        chamberTextView.setText("Chambers: " + chambers.length);//<-Displays number of
        // chambers to player.

        TextView bulletTextView = (TextView) findViewById(R.id.bulletTextView);
        bulletTextView.setText("Bullets: " + bullets);//<-Displays number of bullets to player.

        final Button multiButton = (Button) findViewById(R.id.multiPurposeButton);//<-This button
        // is used to start game, reset game or pull trigger.
        multiButton.setText("Play");//<-Button text updates depending on what stage of
        // the game the player is in.

        final boolean[] gamePlay = {false};//<-True when game is in play, false when
        // game is not in play.

        final int[] i = {0};

        //Game play.
        multiButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (gamePlay[0]){
                    //If chamber is empty it is incremented and text is updated accordingly.
                    if (!chambers[i[0]]) {
                        multiButton.setText("Pull Trigger\nChambers Remaining: " +
                                ((chambers.length - i[0]) -1));
                        System.out.println("Empty. " + chambers[i[0]] + " " + i[0]);
                        emptyChamber.start();
                        i[0]++;
                    //If chamber is loaded the player is informed of their death
                    // and the game is reset.
                    }else{
                        multiButton.setText("You Died\nPlay Again?");
                        gunShot.start();
                        i[0] = 0;
                        gamePlay[0] = false;
                        for (int i = 0; i < chambers.length; i++){
                            chambers[i] = false;
                        }
                    }
                //Puts the game into 'play' mode.
                }else{
                    gamePlay[0] = true;
                    multiButton.setText("Pull Trigger\nChambers Remaining: 6");
                    generate(chambers, bullets);
                }
            }
        });
    }

    //Assigns bullet to random chamber.
    public void generate(boolean[] chambers, int bullets){
        Random random = new Random();
        final int n = random.nextInt(chambers.length);

        chambers[n] = true;
        System.out.println("n: " + n);
        for (int i = 0; i < chambers.length; i++) {
            System.out.println("Chamber " + i + " : " + chambers[i]);
        }
    }
}