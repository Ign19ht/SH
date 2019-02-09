package com.example.alevtina.sh;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
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
    public static int user_age = 1, user_height = 1, user_weight = 1, user_gender = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDBHelper = new DatabaseHelper(this);
        bottomNavigationView = findViewById(R.id.navigation);

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
            if (SupportClass.CompareDates(list.getKey())) {
                keys.add(list.getKey());
            }
        }

        for (int i = 0; i < keys.size(); i++) {
            SecondFragment.selectProducts.remove(keys.get(i));
        }
        keys.clear();

        for (Map.Entry<String, ArrayList<Product>> list : SecondFragment.selectExercises.entrySet()) {
            if (SupportClass.CompareDates(list.getKey())) {
                keys.add(list.getKey());
            }
        }

        for (int i = 0; i < keys.size(); i++) {
            SecondFragment.selectExercises.remove(keys.get(i));
        }

        SupportClass.ProductSave(this);
        SupportClass.ExerciseSave(this);

        ListenerOnButton();

        if (user_gender == -1) {
            getSupportFragmentManager().beginTransaction().replace(ID_FRAGMENT, new RegistratorFragment()).commit();
            findViewById(R.id.navigation_home).setEnabled(false);
            findViewById(R.id.navigation_recd).setEnabled(false);
            findViewById(R.id.navigation_spent).setEnabled(false);
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
                        }
                        return false;
                    }
                }
        );
    }

    public void ReadData() {
        try {
            InputStream file = openFileInput(SupportClass.SAVE_DATA);
            InputStreamReader input = new InputStreamReader(file, "utf8");
            MainActivity.user_weight = input.read();
            MainActivity.user_height = input.read();
            MainActivity.user_age = input.read();
            MainActivity.user_gender = input.read();
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
    }
}
