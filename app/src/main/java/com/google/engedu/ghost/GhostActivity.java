package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import android.os.Handler;
import java.util.logging.LogRecord;

public class GhostActivity extends AppCompatActivity  {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();

    private String wordFrag = "";
    private TextView wordFragView;
    private TextView label;
    private Button challenge;
    private boolean userWentFirst = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();

        if(savedInstanceState != null) {
            wordFrag = savedInstanceState.getString(wordFrag);
        }

        try {
            InputStream inputStream = assetManager.open("words.txt");
//            dictionary = new FastDictionary(inputStream);
            dictionary = new SimpleDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }

        challenge = (Button) findViewById(R.id.button);
        challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currFrag = wordFrag;
                String dictWord = dictionary.getGoodWordStartingWith(wordFrag);
                if(currFrag.length() >= 4 && dictionary.isWord(currFrag)) {
                    label.setText("You-sir wins!!");
                } else if(dictWord != null) {
                    label.setText("Computer wins!!: " + dictWord);
                } else if(dictWord == null) {
                    label.setText("You-sir wins!!");
                }
            }
        });

        onStart(null);
    }

//    @Override
//    public void onSavedInstanceState(Bundle savedInstanceState) {
//        savedInstanceState.putString(wordFrag, wordFrag.toString());
//        //savedInstanceState.putString(label);
//
//        super.onSaveInstanceState(savedInstanceState);
//    }
//a
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_ghost, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        if(userTurn) {
            userWentFirst = true;
        }

        wordFragView = (TextView) findViewById(R.id.ghostText);
        wordFragView.setText("");
        label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
        label = (TextView) findViewById(R.id.gameStatus);
        userTurn = false;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(dictionary.isWord(wordFrag.toLowerCase())){
                    label.setText("I, the compooter, win!!");
                }
                else {
                    String dictWord = dictionary.getGoodWordStartingWith(wordFrag.toLowerCase());
                    if(dictWord == null) {
                        if(wordFrag.length() >= 4) {
                            label.setText("You can't bluff this computer!!");
                            // do Something, close or something
                        } else {
                            label.setText("Not a word, must be at least 4 characters");
                        }

                    }
                    else {
                        wordFrag = dictWord.substring(0, wordFrag.length()+1);
                        wordFragView.setText(wordFrag);
                        userTurn = true;
                        label.setText(USER_TURN);
                    }
                }
            }
        }, 2000);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        userTurn = true;
        if(keyCode < 29 || keyCode > 54) {  // if it's a alphabet char
            return super.onKeyUp(keyCode, event);
        }

        wordFragView = (TextView) findViewById(R.id.ghostText);
        label = (TextView) findViewById(R.id.gameStatus);
        wordFrag = wordFrag.concat(event.getDisplayLabel() + "");
        wordFrag = wordFrag.toLowerCase();
        wordFragView.setText(wordFrag);
        label.setText(COMPUTER_TURN);

        computerTurn();
        return super.onKeyUp(keyCode, event);
    }

    public boolean userFirst() { return this.userWentFirst; }

    public boolean isUserTurn() { return this.userTurn; }
}
