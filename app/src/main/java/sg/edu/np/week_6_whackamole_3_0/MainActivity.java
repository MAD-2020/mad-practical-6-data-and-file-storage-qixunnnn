package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private static final String FILENAME = "MainActivity.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    private EditText et_username;
    private EditText et_password;
    private Button btn_create;
    private TextView tv_signup;

    private MyDBHandler dbHandler = new MyDBHandler(this,null,null,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_username = findViewById(R.id.editText_username);
        et_password = findViewById(R.id.editText_password);
        btn_create = findViewById(R.id.loginBtn);
        tv_signup = findViewById(R.id.tv_newUser);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = et_username.getText().toString();
                String password = et_password.getText().toString();

                Log.v(TAG, FILENAME + ": Logging in with: " + username + ": " + password);

                if (!isValidUser(username,password)){
                    Toast.makeText(MainActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                    Log.v(TAG, FILENAME + ": Invalid user!");
                }
                else
                {
                    Log.v(TAG, FILENAME + ": Valid User! Logging in");
                    Intent i = new Intent(getApplicationContext(),Main3Activity.class);
                    i.putExtra("loginUser",username);
                    startActivity(i);
                }

            }
        });

        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, FILENAME + ": Create new user!");
                Intent i = new Intent(getApplicationContext(),Main2Activity.class);
                startActivity(i);
            }
        });

    }


    protected void onStop() {
        super.onStop();
        finish();
    }

    public boolean isValidUser(String userName, String password) {

        UserData dbData = dbHandler.findUser(userName);

        if(dbData == null)
        {
            return false;
        }

        Log.v(TAG, FILENAME + ": Running Checks..." + dbData.getMyUserName() + ": " + dbData.getMyPassword() +" <--> "+ userName + " " + password);

        if (dbData.getMyPassword().equals(password) != true)
        {
            return false;
        }

        return true;

    }
}
