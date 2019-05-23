package com.example.alevtina.sh;

import  android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private DatabaseHelper mDBHelper;
    public static SQLiteDatabase mDb;
    final static int ID_FRAGMENT = R.id.frgmCont;
    static final String FIRST_SAVE = "saved_first";
    static boolean first;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDBHelper = new DatabaseHelper(this);
        bottomNavigationView = findViewById(R.id.navigation);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        first = sharedPreferences.getBoolean(FIRST_SAVE, true);

        startService(new Intent(this, MyService.class));

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

        ReadData();

        ArrayList<String> keys = new ArrayList<>();

        for (Map.Entry<String, ArrayList<Product>> list : SecondFragment.selectProducts.entrySet()) {
            if (SupportClass.CompareDates(list.getKey(), 15)) {
                keys.add(list.getKey());
            }
        }

        for (String date : keys) {
            SecondFragment.selectProducts.remove(date);
        }
        keys.clear();

        for (Map.Entry<String, ArrayList<Product>> list : SecondFragment.selectExercises.entrySet()) {
            if (SupportClass.CompareDates(list.getKey(), 15)) {
                keys.add(list.getKey());
            }
        }

        for (String date : keys) {
            SecondFragment.selectExercises.remove(date);
        }

        while (HomeFragment.weightArray.size() > 10) {
            String date = HomeFragment.weightArray.firstKey();
            HomeFragment.weightArray.remove(date);
        }

        SupportClass.ProductSave(this);
        SupportClass.ExerciseSave(this);

        ListenerOnButton();

        if (first) {
            findViewById(R.id.navigation_profil).performClick();
            findViewById(R.id.navigation_home).setEnabled(false);
            findViewById(R.id.navigation_recd).setEnabled(false);
            findViewById(R.id.navigation_spent).setEnabled(false);
            findViewById(R.id.navigation_profil).setEnabled(false);
        } else {
            findViewById(R.id.navigation_home).performClick();
        }
    }

    void ListenerOnButton() {
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.navigation_home:
                                getSupportFragmentManager().beginTransaction().replace(ID_FRAGMENT,
                                        new HomeFragment()).commit();
                                return  true;
                            case R.id.navigation_recd:
                                getSupportFragmentManager().beginTransaction().replace(ID_FRAGMENT,
                                        new SecondFragment(true)).commit();
                                return  true;
                            case R.id.navigation_spent:
                                getSupportFragmentManager().beginTransaction().replace(ID_FRAGMENT,
                                        new SecondFragment(false)).commit();
                                return  true;
                            case R.id.navigation_profil:
                                getSupportFragmentManager().beginTransaction().replace(ID_FRAGMENT,
                                        new RegistratorFragment()).commit();
                                return  true;
                        }
                        return false;
                    }
                }
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        save();
        stopService(new Intent(this, MyService.class));
        startService(new Intent(this, MyService.class));
    }

    void save() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putInt(MyService.SAVED_TEXT, MyService.numSteps);
        ed.putString(MyService.SAVED_DATE, SupportClass.GetDate());
        ed.commit();
    }

    String load() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getString(MyService.SAVED_DATE, "09.05.1945");
    }

    public void ReadData() {
        try {
            InputStream file = openFileInput(SupportClass.SAVE_DATA);
            InputStreamReader input = new InputStreamReader(file, "utf8");
            HomeFragment.user_weight = input.read();
            HomeFragment.user_height = input.read();
            HomeFragment.user_age = input.read();
            HomeFragment.user_gender = input.read();
            HomeFragment.targetWeight = input.read();
            if (!SupportClass.ItsToDay(load())) {
                MyService.numSteps = 0;
                save();
            }
            input.close();
        } catch (Exception e) {
            System.out.println("ERROR IN READ DATA :");
            e.printStackTrace();
        }
        try {
            InputStream file = openFileInput(SupportClass.SAVE_PRODUCT);
            InputStreamReader input = new InputStreamReader(file, "utf8");
            BufferedReader buffer = new BufferedReader(input);
            ArrayList<Product> list = new ArrayList<>();
            String time = buffer.readLine();
            String name = buffer.readLine();
            while (name != null) {
                if (name.equals("|")) {
                    ArrayList<Product> array = new ArrayList<>();
                    array.addAll(list);
                    SecondFragment.selectProducts.put(time, array);
                    time = buffer.readLine();
                    list.clear();
                } else {
                    int kall = SupportClass.GetKallIsDB(name, true);
                    int gramm = Integer.parseInt(buffer.readLine());
                    list.add(new Product(kall, name, gramm));
                }
                name = buffer.readLine();
            }
            buffer.close();
        } catch (Exception e) {
            System.out.println("ERROR IN READ PRODUCTS :");
            e.printStackTrace();
        }
        try {
            InputStream file = openFileInput(SupportClass.SAVE_EXERCISE);
            InputStreamReader input = new InputStreamReader(file, "utf8");
            BufferedReader buffer = new BufferedReader(input);
            ArrayList<Product> list = new ArrayList<>();
            String time = buffer.readLine();
            String name = buffer.readLine();
            while (name != null) {
                if (name.equals("|")) {
                    ArrayList<Product> array = new ArrayList<>();
                    array.addAll(list);
                    SecondFragment.selectExercises.put(time, array);
                    time = buffer.readLine();
                    list.clear();
                } else {
                    int kall = SupportClass.GetKallIsDB(name, false);
                    int gramm = Integer.parseInt(buffer.readLine());
                    list.add(new Product(kall, name, gramm));
                }
                name = buffer.readLine();
            }
            buffer.close();
        } catch (Exception e) {
            System.out.println("ERROR IN READ EXERCISES :");
            e.printStackTrace();
        }
        try{
            InputStream file = openFileInput(SupportClass.SAVE_WEIGHT);
            InputStreamReader input = new InputStreamReader(file, "utf8");
            char[] buffer = new char[10];
            input.read(buffer, 0, 10);
            String date = String.valueOf(buffer);
            System.out.println(date);
            int weight = input.read();
            while (weight != -1) {
                HomeFragment.weightArray.put(date, weight);
                input.read(buffer, 0, 10);
                date = String.valueOf(buffer);
                weight = input.read();
            }
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
