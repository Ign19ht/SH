package com.example.alevtina.sh;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private DatabaseHelper mDBHelper;
    public static SQLiteDatabase mDb;
    final static int ID_FRAGMENT = R.id.frgmCont;
    public static int user_age, user_height, user_weight = 1, user_gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDBHelper = new DatabaseHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

        try {
            InputStreamReader input = new InputStreamReader(openFileInput("save_dats"), "utf8");
            user_weight = input.read();
            user_height = input.read();
            user_age = input.read();
            user_gender = input.read();
            int i=input.read();
            String str = "";
            while(i != -1 && (char)i != 's'){
                if (((char)i) == 'f') {
                    String[] sp = str.split(" ");
                    String name = "";
                    for (int j = 0; j < sp.length - 1; j++) {
                        if (j > 0) name += " ";
                        name += sp[j];
                    }
                    int gramm = Integer.parseInt(sp[sp.length - 1]);
                    SecondFragment.selectProducts.add(new Product(GetKallIsDB(name,true), name, gramm));
                    str += input.read();
                    str = "";
                } else {
                    str += (char)i;
                }
                i = input.read();
            }
            if ((char)i == 's') {
                i = input.read();
                while(i != -1){
                    if (((char)i) == 'f') {
                        String[] sp = str.split(" ");
                        String name = "";
                        for (int j = 0; j < sp.length - 1; j++) {
                            if (j > 0) name += " ";
                            name += sp[j];
                        }
                        int gramm = Integer.parseInt(sp[sp.length - 1]);
                        SecondFragment.selectExercises.add(new Product(GetKallIsDB(name,false), name, gramm));
                        str = "";
                    } else {
                        str += (char)i;
                    }
                    i = input.read();
                }
            }
            input.close();
        } catch (Exception e) {
            System.out.println("SCHITIVANIE");
            System.out.println(e.getMessage());
        }

        if (user_gender == -1) {
            getSupportFragmentManager().beginTransaction().replace(ID_FRAGMENT, new RegistratorFragment()).commit();
            findViewById(R.id.navigation_home).setEnabled(false);
            findViewById(R.id.navigation_recd).setEnabled(false);
            findViewById(R.id.navigation_spent).setEnabled(false);
        } else {
            getSupportFragmentManager().beginTransaction().replace(ID_FRAGMENT, new HomeFragment()).commit();
        }

        bottomNavigationView = findViewById(R.id.navigation);

        ListenerOnButton();
    }

    void ListenerOnButton() {
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.navigation_home:
                                getSupportFragmentManager().beginTransaction().replace(ID_FRAGMENT, new HomeFragment()).commit();
                                HomeFragment.SetStats();
                                break;
                            case R.id.navigation_recd:
                                getSupportFragmentManager().beginTransaction().replace(ID_FRAGMENT, new SecondFragment(true)).commit();
                                break;
                            case R.id.navigation_spent:
                                getSupportFragmentManager().beginTransaction().replace(ID_FRAGMENT, new SecondFragment(false)).commit();
                                break;
                        }
                        return false;
                    }
                }
        );
    }

    public static int KallCalculator(int kall, int gramm, boolean flag) {
        int total;
        if (flag) {
            total = kall * gramm / 100;
            return total;
        } else {
            total = kall * gramm * user_weight;
            return total;
        }
    }

    int GetKallIsDB(String name, boolean flag) {
        Cursor cursor;
        if (flag) {
            cursor = mDb.rawQuery("SELECT * FROM products WHERE name=\"" + name + "\"", null);
        } else {
            cursor = mDb.rawQuery("SELECT * FROM exercises WHERE name=\"" + name + "\"", null);
        }
        cursor.moveToFirst();
        int kall = cursor.getInt(1);
        cursor.close();
        return kall;
    }
}
