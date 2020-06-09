package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    /* Hint:
        1. This is the create new user page for user to log in
        2. The user can enter - Username and Password
        3. The user create is checked against the database for existence of the user and prompts
           accordingly via Toastbox if user already exists.
        4. For the purpose the practical, successful creation of new account will send the user
           back to the login page and display the "User account created successfully".
           the page remains if the user already exists and "User already exist" toastbox message will appear.
        5. There is an option to cancel. This loads the login user page.
     */

    private static final String FILENAME = "Main2Activity.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    private Button cancel;
    private Button create;
    private EditText et_username;
    private EditText et_password;

    MyDBHandler db = new MyDBHandler(this,null,null,1);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        et_username = findViewById(R.id.editText_username);
        et_password = findViewById(R.id.editText_password);
        cancel = findViewById(R.id.cancelBtn);
        create = findViewById(R.id.loginBtn);

        final ArrayList<Integer> AllLevels = new ArrayList<>();
        final ArrayList<Integer> AllScores = new ArrayList<>();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentToHome();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if (db.findUser(et_username.getText().toString()) == null){
                    UserData userData = new UserData();
                    userData.setMyUserName(et_username.getText().toString());
                    userData.setMyPassword(et_password.getText().toString());
                    for (int i = 0; i<10; i++)
                    {
                        AllScores.add(0);
                        AllLevels.add(i + 1);
                    }
                    userData.setScores(AllScores);
                    userData.setLevels(AllLevels);
                    db.addUser(userData);

                    Toast.makeText(Main2Activity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                    Log.v(TAG, FILENAME + ": New user created successfully!");
                    IntentToHome();
                }
                else{
                    Toast.makeText(Main2Activity.this, "User already exist during new user creation!", Toast.LENGTH_SHORT).show();

                }
            }
        });

        /* Hint:
            This prepares the create and cancel account buttons and interacts with the database to determine
            if the new user created exists already or is new.
            If it exists, information is displayed to notify the user.
            If it does not exist, the user is created in the DB with default data "0" for all levels
            and the login page is loaded.

            Log.v(TAG, FILENAME + ": New user created successfully!");
            Log.v(TAG, FILENAME + ": User already exist during new user creation!");

         */
    }
    public void IntentToHome(){
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }

    protected void onStop() {
        super.onStop();
        finish();
    }
}
