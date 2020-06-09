package sg.edu.np.week_6_whackamole_3_0;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class MyDBHandler extends SQLiteOpenHelper {
    /*
        The Database has the following properties:
        1. Database name is WhackAMole.db
        2. The Columns consist of
            a. Username
            b. Password
            c. Level
            d. Score
        3. Add user method for adding user into the Database.
        4. Find user method that finds the current position of the user and his corresponding
           data information - username, password, level highest score for each level
        5. Delete user method that deletes based on the username
        6. To replace the data in the database, we would make use of find user, delete user and add user

        The database shall look like the following:

        Username | Password | Level | Score
        --------------------------------------
        User A   | XXX      | 1     |    0
        User A   | XXX      | 2     |    0
        User A   | XXX      | 3     |    0
        User A   | XXX      | 4     |    0
        User A   | XXX      | 5     |    0
        User A   | XXX      | 6     |    0
        User A   | XXX      | 7     |    0
        User A   | XXX      | 8     |    0
        User A   | XXX      | 9     |    0
        User A   | XXX      | 10    |    0
        User B   | YYY      | 1     |    0
        User B   | YYY      | 2     |    0

     */

    private static final String FILENAME = "MyDBHandler.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    private static final String DATABASE_NAME = "WhackAMole.db";

    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_ACCOUNT = "Accounts";
    private static final String COLUMN_USERNAME = "Username";
    private static final String COLUMN_PASSWORD = "Password";
    private static final String COLUMN_LEVEL = "Level";
    private static final String COLUMN_SCORE = "Score";

    //private static final String TABLE

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context,DATABASE_NAME,factory,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {

        String CREATE_ACCOUNTS_TABLE = "CREATE TABLE " + TABLE_ACCOUNT +
                "(" + COLUMN_USERNAME + " TEXT," +
                COLUMN_PASSWORD + " TEXT," +
                COLUMN_LEVEL + " INTEGER," +
                COLUMN_SCORE + " INTEGER" + ")";

        db.execSQL(CREATE_ACCOUNTS_TABLE);
        Log.v(TAG, "DB Created: " + CREATE_ACCOUNTS_TABLE);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
        onCreate(db);
    }

    public void addUser(UserData userData)
    {
            ContentValues values = new ContentValues();
            for (int i = 0; i<userData.getLevels().size();i++){
                values.put(COLUMN_USERNAME, userData.getMyUserName());
                values.put(COLUMN_PASSWORD, userData.getMyPassword());
                values.put(COLUMN_LEVEL, userData.getLevels().get(i));
                values.put(COLUMN_SCORE, userData.getScores().get(i));

                SQLiteDatabase db = this.getWritableDatabase();
                Log.v(TAG, FILENAME + ": Adding data for Database: " + values.toString());
                db.insert(TABLE_ACCOUNT,null,values);
                db.close();
            }
    }

    public UserData findUser(String username)
    {
        String query = "SELECT * FROM " + TABLE_ACCOUNT + " WHERE " + COLUMN_USERNAME + " = \"" + username + "\"";
        Log.v(TAG, FILENAME +": Find user form database: " + query);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        UserData userData = new UserData();
        ArrayList<Integer> tempLevel = new ArrayList<>();
        ArrayList<Integer> tempScore = new ArrayList<>();


        if (cursor.moveToFirst()){
            userData.setMyUserName(cursor.getString(0));
            userData.setMyPassword(cursor.getString(1));
            do {
                tempLevel.add(cursor.getInt(2));
                tempScore.add(cursor.getInt(3));
            }while (cursor.moveToNext());
            cursor.close();

            userData.setLevels(tempLevel);
            userData.setScores(tempScore);
            Log.v(TAG, FILENAME + ": QueryData: " + userData.getLevels().toString() + userData.getScores().toString());

        }else{
            userData = null;
            Log.v(TAG, FILENAME+ ": No data found!");
        }
        db.close();
        return userData;
    }

    public boolean deleteAccount(String username) {

        String query = "SELECT * FROM " + TABLE_ACCOUNT + " WHERE " + COLUMN_USERNAME + " =\"" + username + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        UserData userData = new UserData();

        if (cursor.moveToFirst()){
            do {
                db.delete(TABLE_ACCOUNT, COLUMN_USERNAME + " = ?", new String[]{username});
            }while (cursor.moveToNext());
        }
        else{
            return false;
        }
        Log.v(TAG, FILENAME + ": Database delete user: " + query);

        cursor.close();
        db.close();
        return true;
    }
}
