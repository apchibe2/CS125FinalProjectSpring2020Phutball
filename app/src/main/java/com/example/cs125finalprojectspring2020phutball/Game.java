package com.example.cs125finalprojectspring2020phutball;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.cs125finalprojectspring2020phutball.gamelogic.Tile;
import com.google.android.material.snackbar.Snackbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Game extends AppCompatActivity {
    private FirebaseDatabase database;

    private static int playerCount = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        final DatabaseReference players = database.getReference("players");
        setContentView(R.layout.activity_game);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        players.child("phutball" + playerCount).setValue(new Tile(8,10, 0));

        TextView gameState = findViewById(R.id.gamestate_textview);
        Button movePhutball = findViewById(R.id.move_phutball_button);
        movePhutball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Button addPlayer = findViewById(R.id.add_player_button);
        addPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                players.child("player" + playerCount).setValue(new Tile(0,0, playerCount));
                playerCount++;
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Button leaveGame = findViewById(R.id.leave_game_button);
        leaveGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Game.this, MainActivity.class));
            }
        });
        GridLayout grid = (GridLayout) findViewById(R.id.gridLayout);
        grid.setColumnCount(15);
        grid.setRowCount(20);
        ImageView image = new ImageView(this);
        GridLayout.LayoutParams lays = new GridLayout.LayoutParams();
        lays.width = grid.getMeasuredWidth() / 15;
        lays.height = grid.getMeasuredHeight() / 20;
        image.setLayoutParams(lays);
        image.setImageResource(R.drawable.box_image);
        for (int i = 0; i < 300; i++) {
            grid.addView(image, i);
        }


        //TODO
        //AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        //builder.setTitle("Ent new player coordinates");
        // I'm using fragment here so I'm using getView() to provide ViewGroup
        // but you can provide here any other instance of ViewGroup from your Fragment / Activity
        //View viewInflated = LayoutInflater.from(getApplicationContext()).inflate(findViewById(_____________), false);
        // Set up the input
        //final EditText xInput = (EditText) viewInflated.findViewById(R.id.xInput);
        //final EditText yInput = (EditText) viewInflated.findViewById(R.id.yInput);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        //builder.setView(viewInflated);

        // Set up the buttons
        //builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            //@Override
            //public void onClick(DialogInterface dialog, int which) {
                //dialog.dismiss();
                //m_Text = input.getText().toString();
            //}
        //});
        //builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            //@Override
            //public void onClick(DialogInterface dialog, int which) {
                //dialog.cancel();
            //}
        //});

        //builder.show();
    }
}
