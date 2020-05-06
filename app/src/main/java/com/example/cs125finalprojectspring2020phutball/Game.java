package com.example.cs125finalprojectspring2020phutball;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class Game extends AppCompatActivity implements InputDialog.InputDialogListener {
    private TextView gameState;
    private Button movePhutball;
    private Button addPlayer;

    private FirebaseDatabase database;
    private DatabaseReference players;
    private DatabaseReference ref;
    private ArrayList<Tile> playersArray = new ArrayList<>();
    private static int playerCount = 1;

    private int xCoord;
    private int yCoord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        database = FirebaseDatabase.getInstance();
        players = database.getReference("players");
        players.child("phutball0").setValue(new Tile(8,10));

        gameState = findViewById(R.id.gamestate_textview);
        movePhutball = findViewById(R.id.move_phutball_button);
        movePhutball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(openCoordinateInputDialog()) {
                    if(checkValidSpace(xCoord, yCoord) && checkValidMove(xCoord, yCoord)) {
                        movePhutball(xCoord, yCoord);
                        Snackbar.make(view, "Phutball Moved", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        Snackbar.make(view, "please enter valid coordinates", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            }
        });
        addPlayer = findViewById(R.id.add_player_button);
        addPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCoordinateInputDialog();
                if(checkValidSpace(xCoord, yCoord)) {
                    players.child("player" + xCoord + "," + yCoord).setValue(new Tile(xCoord, yCoord));
                    playerCount++;
                    printArray();
                    Snackbar.make(view, "Player Added to field", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    Snackbar.make(view, "Please enter valid coordinates", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });

        /*
        GridLayout grid = findViewById(R.id.gridLayout);
        grid.setColumnCount(15);
        grid.setRowCount(20);
        ImageView image = new ImageView(this);
        GridLayout.LayoutParams lays = new GridLayout.LayoutParams();
        lays.width = grid.getMeasuredWidth() / 15;
        lays.height = grid.getMeasuredHeight() / 20;
        image.setLayoutParams(lays);
        //for (int i = 0; i < 300; i++) {
        //    grid.addView(image, i);
        //}
        */
    }

    public boolean checkValidSpace(int x, int y) {
        retrieveDataSnapshot();
        //check if its out of bounds first
        if(x < 0 || x > 15 || y < 0 || y > 19) {
            return false;
        }
        for (Tile player : playersArray) {
            if (player.getX() == x && player.getY() == y) {
                return false;
            }
        }
        return true;
    }

    private void collectPlayersData(Map<String,Object> playersData) {

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : playersData.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            int xValue = Integer.parseInt(singleUser.get("x").toString());
            int yValue = Integer.parseInt(singleUser.get("y").toString());
            if(!(playersArray.contains(new Tile(xValue, yValue)))) {
                playersArray.add(new Tile(xValue, yValue));
            }
        }
    }

    public void removePlayer(int x, int y) {
        players.child("player" + x + "," + y).removeValue();
    }

    public void retrieveDataSnapshot(){
        ref = FirebaseDatabase.getInstance().getReference().child("players");
        ref.addListenerForSingleValueEvent(
            new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Get map of users in datasnapshot
                    collectPlayersData((Map<String,Object>) dataSnapshot.getValue());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //handle databaseError
                }
            }
        );
    }

    public void movePhutball(int x, int y) {
        players.child("phutball0").setValue(new Tile(x,y));
        //TODO remove all the players on the path
    }

    public boolean openCoordinateInputDialog() {
        InputDialog inputDialog = new InputDialog();
        inputDialog.show(getSupportFragmentManager(), "input dialog");
        return true;
    }

    @Override
    public void applyCoords(int x, int y) {
        xCoord = x;
        yCoord = y;
    }

    public void printArray() {
        for (Tile tile : playersArray) {
            Log.i("MyApp",tile.getString());
        }
    }

    //checks that every space between the move coordinates and the phutball contains a player
    public boolean checkValidMove(int x, int y) {
        //check that every space between move coords and current phutball is a player, if not return false
        int ballX = 0;
        int ballY = 0;
        if (checkValidSpace(x, y) == false) {
            return false;
        }
        if (ballX != x && ballY != y) {
            if (x - ballX != y - ballY) {
                return false;
            }
        }
        boolean output = false;
        if (x == ballX) {
            if (y - ballY > 0) {
                for (int i = ballY + 1; i < y; i++) {
                    for (Tile player : playersArray) {
                        if (player.getX() == x) {
                            if (player.getY() == i) {
                                output = true;
                                break;
                            } else {
                                output = false;
                            }
                        }
                    }
                }
                return true;
            } else {
                for (int i = ballY - 1; i > y; i--) {
                    for (Tile player : playersArray) {
                        if (player.getX() == x) {
                            if (player.getY() == i) {
                                output = true;
                                break;
                            } else {
                                output = false;
                            }
                        }
                    }
                }
                return output;
            }
        } else if (y == ballY) {
            if (x - ballX > 0) {
                for (int i = ballX + 1; i < x; i++) {
                    for (Tile player : playersArray) {
                        if (player.getY() == y) {
                            if (player.getX() == i) {
                                output = true;
                                break;
                            } else {
                                output = false;
                            }
                        }
                    }
                }
                return output;
            } else {
                for (int i = ballX - 1; i > x; i--) {
                    for (Tile player : playersArray) {
                        if (player.getY() == y) {
                            if (player.getY() == i) {
                                output = true;
                                break;
                            } else {
                                output = false;
                            }
                        }
                    }
                }
                return output;
            }
        } else {
            if (x - ballX > 0 && y - ballY > 0) {
                for (int i = 0; i < x - ballX; i++) {
                    for (Tile player : playersArray) {
                        if (player.getX() == x + i && player.getY() == y + i) {
                            output = true;
                            break;
                        } else {
                            output = false;
                        }
                    }
                }
            } else if (x - ballX < 0 && y - ballY > 0) {
                for (int i = 0; i < x - ballX; i++) {
                    for (Tile player : playersArray) {
                        if (player.getX() == x - i && player.getY() == y + i) {
                            output = true;
                            break;
                        } else {
                            output = false;
                        }
                    }
                }
            } else if (x - ballX < 0 && y - ballY < 0) {
                for (int i = 0; i < x - ballX; i++) {
                    for (Tile player : playersArray) {
                        if (player.getX() == x - i && player.getY() == y - i) {
                            output = true;
                            break;
                        } else {
                            output = false;
                        }
                    }
                }
            } else if (x - ballX > 0 && y - ballY < 0) {
                for (int i = 0; i < x - ballX; i++) {
                    for (Tile player : playersArray) {
                        if (player.getX() == x + i && player.getY() == y - i) {
                            output = true;
                            break;
                        } else {
                            output = false;
                        }
                    }
                }
            }
        }
        return output;}
}
