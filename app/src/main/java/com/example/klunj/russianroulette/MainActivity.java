package com.example.klunj.russianroulette;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialise vibrator.
        final Vibrator vibrate = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        //Empty chamber sound clip.
        final MediaPlayer emptyChamber = MediaPlayer.create(this, R.raw.emptychamber);

        //Gun shot sound clip.
        final MediaPlayer gunShot = MediaPlayer.create(this, R.raw.gunshot);

        //Number of chambers to be selected by user.
        final int[] chambersNum = {0};

        //Spinner allows user to select number of chambers. 1 to 12 inclusive.
        Spinner chamberSpinner = (Spinner) findViewById(R.id.chamberSpinner) ;
        Integer[] chamberValues = new Integer[] {1,2,3,4,5,6,7,8,9,10,11,12};
        ArrayAdapter<Integer> chamberAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, chamberValues);
        chamberSpinner.setAdapter(chamberAdapter);

        chamberSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener(){
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View v, int pos, long id){
                        chambersNum[0] = (int) parent.getItemAtPosition(pos);
                        System.out.println("Number of chambers: " + chambersNum[0]);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                }
        );

        //Initialise array capable of holding a maximum of 12 chambers.
        final boolean[] chambers = new boolean[12];

        //Number of bullets in gun.
        final int bullets = 1;

        TextView chamberTextView = (TextView) findViewById(R.id.chamberTextView);
        chamberTextView.setText("Chambers: ");

        TextView bulletTextView = (TextView) findViewById(R.id.bulletTextView);
        bulletTextView.setText("Bullets: ");

        //This button is used to start or reset the game. It is also used to pull
        //the trigger during game play. Button text is updated to reflect the players actions.
        final Button multiButton = (Button) findViewById(R.id.multiPurposeButton);
        multiButton.setText("Play");

        //True when game is in play, false when game is not in play. It's a flag used to
        //determine what the multi purpose button should do.
        final boolean[] gamePlay = {false};

        //This number represents the chamber being fired.
        final int[] i = {0};

        //Game play.
        multiButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (gamePlay[0]){
                    //If chamber is empty it is incremented and text is updated accordingly.
                    if (!chambers[i[0]]) {
                        multiButton.setText("Pull Trigger\nChambers Remaining: " +
                                ((chambersNum[0] - i[0]) -1));
                        System.out.println("Empty. " + chambers[i[0]] + " " + i[0]);
                        emptyChamber.start();
                        vibrate.vibrate(100);
                        i[0]++;
                    //If chamber is loaded the player is informed of their death
                    // and the game is reset.
                    }else{
                        multiButton.setText("You Died\nPlay Again?");
                        gunShot.start();
                        vibrate.vibrate(300);
                        i[0] = 0;
                        gamePlay[0] = false;
                        for (int i = 0; i < chambers.length; i++){
                            chambers[i] = false;
                        }
                    }
                //Puts the game into 'play' mode.
                }else{
                    gamePlay[0] = true;
                    multiButton.setText("Pull Trigger\nChambers Remaining: " + chambersNum[0]);
                    generate(chambers, chambersNum[0], bullets);
                }
            }
        });
    }

    //Assigns bullet to random chamber.
    public void generate(boolean[] chambers, int chambersNum, int bullets){

        Random random = new Random();

        //Only a chamber within the specified limit is loaded with a bullet.
        final int n = random.nextInt(chambersNum);

        chambers[n] = true;

        //To cheat with. This prints out the chambers while showing which one is loaded.
        for (int i = 0; i < chambersNum; i++) {
            System.out.println("Chamber " + i + " : " + chambers[i]);
        }
    }
}