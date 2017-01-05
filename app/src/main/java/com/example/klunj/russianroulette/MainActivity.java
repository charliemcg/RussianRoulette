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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialise vibrator.
        final Vibrator vibrate = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        //Spinner allows user to select number of bullets. One to however many
        //available chambers inclusive.
        final Spinner bulletSpinner = (Spinner) findViewById(R.id.bulletSpinner);

        //Empty chamber sound clip.
        final MediaPlayer emptyChamber = MediaPlayer.create(this, R.raw.emptychamber);

        //Gun shot sound clip.
        final MediaPlayer gunShot = MediaPlayer.create(this, R.raw.gunshot);

        //Number of chambers to be selected by user.
        final int[] chambersNum = {0};

        //Number of bullets to be selected by user.
        final int[] bulletsNum = {0};

        //Spinner allows user to select number of chambers. 1 to 12 inclusive.
        final Spinner chamberSpinner = (Spinner) findViewById(R.id.chamberSpinner) ;
        Integer[] chamberValues = new Integer[] {1,2,3,4,5,6,7,8,9,10,11,12};
        ArrayAdapter<Integer> chamberAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, chamberValues);
        chamberSpinner.setAdapter(chamberAdapter);

        chamberSpinner.setSelection(5);

        chamberSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener(){
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View v, int pos, long id){
                        chambersNum[0] = (int) parent.getItemAtPosition(pos);
                        bulletSpinnerCreate(chambersNum, bulletsNum, bulletSpinner);
                        System.out.println("Number of chambers: " + chambersNum[0]);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                }
        );

        //Initialise array capable of holding a maximum of 12 chambers.
        final boolean[] chambers = new boolean[12];

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

        //This number is used to compare shots fired against number of available bullets.
        final int[] count = {0};

        //Game play.
        multiButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (gamePlay[0]){
                    //If chamber is empty it is incremented and text is updated accordingly.
                    if (!chambers[i[0]]) {
                        multiButton.setText("Pull Trigger\nChambers Remaining: " +
                                ((chambersNum[0] - i[0]) -1) + "\nBullets Remaining: "
                                + (bulletsNum[0] - count[0]));
                        System.out.println("Empty. " + chambers[i[0]] + " " + i[0]);
                        emptyChamber.start();
                        vibrate.vibrate(100);
                        i[0]++;
                    //If chamber is loaded the player is informed of their death. The game
                    //is only reset once all bullets have been fired.
                    }else{
                        //If there are still bullets left in the gun the player is simply
                        //informed of their death but can continue playing.
                        if(bulletsNum[0] != (count[0] + 1)){
                            count[0]++;
                            multiButton.setText("You Died\nChambers Remaining: " +
                            ((chambersNum[0] - i[0]) -1) + "\nBullets Remaining: "
                                    + (bulletsNum[0] - count[0]));
                            gunShot.start();
                            vibrate.vibrate(300);
                            i[0]++;
                            System.out.println("count: " + count[0]);
                        //If all bullets have been fired the player can reset the game.
                        }else{
                            multiButton.setText("You Died\nPlay Again?");
                            System.out.println("Game Over");
                            gunShot.start();
                            vibrate.vibrate(300);
                            i[0] = 0;
                            gamePlay[0] = false;
                            chamberSpinner.setEnabled(true);
                            bulletSpinner.setEnabled(true);
                            for (int i = 0; i < chambers.length; i++){
                                chambers[i] = false;
                            }
                            count[0] = 0;
                        }
                    }
                //Puts the game into 'play' mode.
                }else{
                    gamePlay[0] = true;
                    //Spinners are disabled during game play.
                    chamberSpinner.setEnabled(false);
                    bulletSpinner.setEnabled(false);
                    multiButton.setText("Pull Trigger\nChambers Remaining: " + chambersNum[0] +
                            "\nBullets Remaining: " + bulletsNum[0]);
                    generate(chambers, chambersNum[0], bulletsNum[0]);
                }
            }
        });
    }

    public void bulletSpinnerCreate(int[] chambersNum, final int[] bulletsNum,
                                    Spinner bulletSpinner) {

        final Integer[] bulletValues = new Integer[chambersNum[0]];

        for (int i = 0; i < bulletValues.length; i++){
            bulletValues[i] = i + 1;
        }
        ArrayAdapter<Integer> bulletAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, bulletValues);
        bulletSpinner.setAdapter(bulletAdapter);

        bulletSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener(){
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View v, int pos, long id){
                        bulletsNum[0] = (int) parent.getItemAtPosition(pos);
                        System.out.println("Number of bullets: " + bulletsNum[0]);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                }
        );
    }

    //Assign bullet to random chamber.
    public void generate(boolean[] chambers, int chambersNum, int bulletsNum){

        //An array list is created to the length of the number of available chambers.
        List<Integer> tempArray = new ArrayList<>();

        //The list is populated with integers 1 to max number of chambers inclusive.
        for(int i = 0; i < chambersNum; i++){
            tempArray.add(i);
        }

        //The list is shuffled.
        Collections.shuffle(tempArray);

        //The first x number of values are selected where x is the number of bullets. These
        //randomly chosen values represent the chambers being loaded with bullets.
        for(int i = 0; i < bulletsNum; i++){
            chambers[tempArray.get(i)] = true;
        }

        //To cheat with. This prints out the chambers while showing which one is loaded.
        for (int i = 0; i < chambersNum; i++) {
            System.out.println("Chamber " + i + " : " + chambers[i]);
        }
        System.out.println("Bullets: " + bulletsNum);
    }
}