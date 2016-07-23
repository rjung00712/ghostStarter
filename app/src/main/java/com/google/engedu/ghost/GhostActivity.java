package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();

    private String wordFrag = "";
    TextView wordFragView;
    TextView label;
    Button challenge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
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
                String dictWord = dictionary.getAnyWordStartingWith(wordFrag);
                if(currFrag.length() >= 4 && dictionary.isWord(currFrag)) {
                    label.setText("Youser wins!!");
                } else if(dictWord != null) {
                    label.setText("Computer wins!!: " + dictWord);
                } else if(dictWord == null) {
                    label.setText("Youser wins!!");
                }
            }
        });

        onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

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

        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        // Do computer turn stuff then make it the user's turn again
        // if fragment is a word and atleast 4 characters in length
        if(wordFrag.length() >= 4 && dictionary.isWord(wordFrag)) {
            label.setText("I, the computer, won!!");
        } else {
            if(dictionary.getAnyWordStartingWith(wordFrag)== null) {
                label.setText("You're bluffing!! It's not a word. You lose haha!!");
            } else {
                label.setText(dictionary.getAnyWordStartingWith(wordFrag).
                        substring(0, wordFrag.length()+1));
            }
        }

        userTurn = true;
        label.setText(USER_TURN);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode < 29 || keyCode > 54) {
            return super.onKeyUp(keyCode, event);
        }

        wordFragView = (TextView) findViewById(R.id.ghostText);
        label = (TextView) findViewById(R.id.gameStatus);
        wordFrag = wordFrag.concat(event.getDisplayLabel() + "");
        wordFrag = wordFrag.toLowerCase();

        wordFragView.setText(wordFrag);
        if(dictionary.isWord(wordFrag.toLowerCase())) {
            label.setText("Is a word");
        }

        userTurn = false;
        label.setText(COMPUTER_TURN);
        computerTurn();

        return false;
    }


}
