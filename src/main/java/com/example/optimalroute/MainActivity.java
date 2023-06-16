package com.example.optimalroute;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.optimalroute.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public static Place findClosest(List<Place> list, Person person){
        Place nextPlace = null;
        int minDistance  = Integer.MAX_VALUE;
        for(Place p : list){
            int distance = (person.getXPos()-p.getX())*(person.getXPos()-p.getX())+(person.getYPos()-p.getY())*(person.getYPos()-p.getY());
            if(distance < minDistance){
                minDistance = distance;
                nextPlace = p;
            }
        }
        return nextPlace;
    }

    public static int goNext(Person person, String[][] grid, int distance, Place place){
        int actualRowPerson = grid.length - person.getYPos();
        int actualColPerson = person.getXPos()-1;
        int actualRowDes = grid.length - place.getY();
        int actualColDes = place.getX()-1;

        grid[actualRowDes][actualColDes] = "D ";

        if(actualColPerson <= actualColDes){
            for(int i = actualColPerson+1; i < actualColDes; i++){
                grid[actualRowPerson][i] = "-> ";
                distance++;
            }
        }else{
            for(int i = actualColPerson-1; i > actualColDes; i--){
                grid[actualRowPerson][i] = "<- ";
                distance++;
            }
        }
        if(actualRowPerson >= actualRowDes){
            for(int i = actualRowPerson; i > actualRowDes; i--){
                grid[i][actualColDes] = "|   ";
                distance++;
            }
        }else{
            for(int i = actualRowPerson; i < actualRowDes; i++){
                grid[i][actualColDes] = "|   ";
                distance++;
            }
        }

        person.setXPos(place.getX());
        person.setYPos(place.getY());
        distance++;
        return distance;
    }

    public void Go(View v){
        Button b = findViewById(R.id.generate);
        b.setEnabled(false);
        b.setText("Check out!");

        /*
         * basic setup
         */
        int distance = 0;
        String[][] grid = new String[10][10];
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid.length; j++){
                grid[i][j] = ".   ";
            }
        }

        /*
         * take current position
         */
        TextView tv = findViewById(R.id.YourLocation);
        String[] xAndY = tv.getText().toString().split(" ");
        int x = Integer.parseInt(xAndY[0]);
        int y = Integer.parseInt(xAndY[1]);
        Person person = new Person(x,y);

        /*
         * set up destinations
         */
        List<Place> places = new ArrayList<>();
        places.add(new Place(5, 9)); //gym
        places.add(new Place(2, 7)); //dining hall
        places.add(new Place(2, 2)); //library
        places.add(new Place(9, 1)); //classroom


        int actualRowPerson = grid.length - person.getYPos();
        int actualColPerson = person.getXPos()-1;
        grid[actualRowPerson][actualColPerson] = "U!";

        /*
         * travel process
         */
        while(!places.isEmpty()){
            Place nextOne = findClosest(places,person);
            distance += goNext(person,grid,distance,nextOne);
            places.remove(nextOne);
        }

        String output = "";
        for(String[] sArr : grid){
            for(String s : sArr){
                output += s;
            }
            output += "\n";
        }

        /*
         * display
         */
        TextView displayWindow = findViewById(R.id.display);
        displayWindow.setText(output);
        displayWindow.setTextSize(20);
        displayWindow.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

    }
}
class Place {

    private int xP;
    private int yP;

    public Place(int x, int y) {
        xP = x;
        yP = y;
    }

    public int getX() {
        return xP;
    }

    public int getY() {
        return yP;
    }
}

class Person{
    private int xPos;
    private int yPos;

    public Person(int x, int y) {
        xPos = x;
        yPos = y;
    }

    public int getXPos(){
        return xPos;
    }

    public int getYPos(){
        return yPos;
    }

    public void setXPos(int x){
        xPos = x;
    }

    public void setYPos(int y){
        yPos = y;
    }

}
