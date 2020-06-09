package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Main4Activity extends AppCompatActivity implements View.OnClickListener{

    private static final String FILENAME = "Main4Activity.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    CountDownTimer readyTimer;
    boolean isReadyRunning = true;
    CountDownTimer newMolePlaceTimer;
    boolean isMoleRunning = false;

    private int advancedScore;
    private TextView txtScore;
    private String currentUser;
    private TextView backBtn;
    private int level;

    private MyDBHandler db = new MyDBHandler(this,null,null,1);

    private void readyTimer(){
        readyTimer = new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long l) {

                final Toast toast = Toast.makeText(Main4Activity.this, "Get Ready In "+ l/1000 + " seconds", Toast.LENGTH_SHORT);
                Log.v(TAG, "Ready CountDown!" + l/ 1000);
                buttonsControl(false);
                toast.show();

                Timer lag = new Timer();
                lag.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                },1000);
            }

            @Override
            public void onFinish() {
                Toast.makeText(getApplicationContext(),"GO!", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Ready CountDown Complete!");
                readyTimer.cancel();
                isReadyRunning = false;

                buttonsControl(true);
                placeMoleTimer();
            }
        };
        readyTimer.start();
    }
    private void placeMoleTimer() {

        newMolePlaceTimer = new CountDownTimer(setTimerByLevel(), 1000) {
            @Override
            public void onTick(long l) {
                isMoleRunning = true;
            }

            @Override
            public void onFinish() {
                Log.v(TAG, "New Mole Location");
                setNewMole();
                newMolePlaceTimer.start();
            }
        };
        newMolePlaceTimer.start();
    }
    private static final int[] BUTTON_IDS = {

            R.id.T_btnLeft, R.id.T_btnCenter, R.id.T_btnRight,
            R.id.M_btnLeft, R.id.M_btnCenter, R.id.M_btnRight,
            R.id.B_btnLeft, R.id.B_btnCenter, R.id.B_btnRight
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        Intent intent = getIntent();
        currentUser = intent.getStringExtra("currentUser");
        level = intent.getIntExtra("level",0);
        Log.v(TAG,"Current selected level: " + level);

        txtScore = (TextView) findViewById(R.id.txtScore);
        backBtn = findViewById(R.id.backBtn);

        for(final int id : BUTTON_IDS){
            findViewById(id).setOnClickListener(this);
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserScore();

                Intent i = new Intent(getApplicationContext(),Main3Activity.class);
                i.putExtra("loginUser",currentUser);
                startActivity(i);
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        readyTimer();
    }
    private void doCheck(Button checkButton)
    {
        if (checkButton.getText() == "*")
        {
            advancedScore++;
            Log.v(TAG,"Hit, score added!");
        }
        else {
            advancedScore--;
            Log.v(TAG,"Missed, score deducted");
        }
    }

    public void setNewMole()
    {
        Random ran = new Random();
        Random ran2 = new Random();

        int randomLocation = ran.nextInt(9);

        Button b;
        for(final int id : BUTTON_IDS){
            b = findViewById(id);
            b.setText("O");
        }

        if (level >= 6){
            int randomLocation2 = ran2.nextInt(9);
            Button selectedBut = findViewById(BUTTON_IDS[randomLocation2]);
            selectedBut.setText("*");
        }

        Button selectedBut = findViewById(BUTTON_IDS[randomLocation]);
        selectedBut.setText("*");
    }

    private void updateUserScore()
    {
        if (isReadyRunning)
            readyTimer.cancel();

        if (isMoleRunning)
            newMolePlaceTimer.cancel();

        UserData userData = db.findUser(currentUser);

        if (userData.getScores().get(level - 1) < advancedScore) {
            userData.getScores().set(level - 1, advancedScore);

            db.deleteAccount(userData.getMyUserName());
            db.addUser(userData);
            Log.v(TAG, FILENAME + ": Update User Score...");
        }

    }
    public void buttonsControl(boolean butCon)
    {
        Button b;
        if (butCon){
            for(final int id : BUTTON_IDS){
                b = findViewById(id);
                b.setClickable(true);
            }
        }
        else{
            for(final int id : BUTTON_IDS) {
                b = findViewById(id);
                b.setClickable(false);
            }
        }
    }


    @Override
    public void onClick(View v) {
        for(int i=0;i<BUTTON_IDS.length;i++)
        {
            if (v.getId() == BUTTON_IDS[i])
            {
                Button clickedBut = findViewById(BUTTON_IDS[i]);
                doCheck(clickedBut);
                break;
            }
        }
        txtScore.setText(Integer.toString(advancedScore));
    }
    public int setTimerByLevel(){

        return (11-level)*1000;
    }
}
