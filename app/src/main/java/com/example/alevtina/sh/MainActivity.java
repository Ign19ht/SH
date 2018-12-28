package com.example.alevtina.sh;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static int countRecd = 0, countSpent = 0;
    private Button toAddRect, toAddSpent;
    private AddRecdActivity addRecdActivity;
    private AddSpentActivity addSpentActivity;
    private static TextView countRecdView, countSpentView, resultView;
    private DatabaseHelper mDBHelper;
    public static SQLiteDatabase mDb;
    private RecyclerView recyclerView;
    private static MainAdapter adapter;
    public static ArrayList<Product> selectProducts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countRecdView = (TextView)findViewById(R.id.countrecd);
        countSpentView = (TextView)findViewById(R.id.countspent);
        resultView = (TextView) findViewById(R.id.result);

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

        adapter = new MainAdapter(selectProducts, true);
        recyclerView = findViewById(R.id.mainrecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        SetStats();
        addRecdActivity = new AddRecdActivity();
        addSpentActivity = new AddSpentActivity();
        ListenerOnButton();

    }

//    public void read(){
//        try {
//            FileInputStream fileIn = openFileInput("productsdb.txt");
//            InputStreamReader reader = new InputStreamReader(fileIn);
//            BufferedReader buffer = new BufferedReader(reader);
//            String line;
//            while ((line = buffer.readLine()) != null) {
//                hyi = line;
//                String[] str = line.split(" ");
//                String key = "";
//                for (int i = 0; i < str.length - 1; i++) {
//                    key += str[i];
//                }
//                productsDB.put(key, Integer.parseInt(str[str.length - 1]));
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static void SetStats() {
        adapter.notifyDataSetChanged();
        countRecd = 0;
        for (int i = 0; i < selectProducts.size(); i++) {

        }
        countRecdView.setText(Integer.toString(countRecd));
        countSpentView.setText(Integer.toString(countSpent));
        resultView.setText(Integer.toString(Math.abs(countRecd - countSpent)));
    }

    void ListenerOnButton() {
        toAddRect = (Button) findViewById(R.id.toAddRect);
        toAddSpent = (Button) findViewById(R.id.toAddSpent);

        toAddRect.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.frgmCont, addRecdActivity).commit();
                    }
                }
        );

        toAddSpent.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.frgmCont, addSpentActivity).commit();
                    }
                }
        );
    }
}
