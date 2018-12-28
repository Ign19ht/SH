package com.example.alevtina.sh;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddRecdActivity extends Fragment {

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private Button backToMain ,saveRect;
    private EditText find;
    private ArrayList<Product> products = new ArrayList<>();
    private String findName = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_add_recd, null);

        backToMain = (Button) view.findViewById(R.id.backToMain);
        saveRect = (Button) view.findViewById(R.id.saveRect);
        find = (EditText) view.findViewById(R.id.find);

        SetArrayList();

        adapter = new ItemAdapter(products, 1);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        ListenerOnButton();
        FindListener();
        return view;
    }

    void ListenerOnButton() {
        backToMain.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.checkproduct.clear();
                        getFragmentManager().beginTransaction().remove(AddRecdActivity.this).commit();
                    }
                }
        );

        saveRect.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SetSelectList(adapter.checkproduct);
                        adapter.checkproduct.clear();
                        MainActivity.SetStats();
                        getFragmentManager().beginTransaction().remove(AddRecdActivity.this).commit();
                    }
                }
        );
    }

    void FindListener() {
        find.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                findName = find.getText().toString();
                products.clear();
                SetArrayList();
                adapter.notifyDataSetChanged();
            }
        });
    }

    void SetArrayList() {
        if (findName.length() == 0) {
            Cursor cursor = MainActivity.mDb.rawQuery("SELECT * FROM products", null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                products.add(new Product(cursor.getInt(1), cursor.getString(0) ));
                cursor.moveToNext();
            }
            cursor.close();
        } else {
            Cursor cursor = MainActivity.mDb.rawQuery("SELECT * FROM products", null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                int index = cursor.getString(0).lastIndexOf(findName);
                findName = findName.substring(0, 1).toUpperCase() + findName.substring(1);
                int index2 = cursor.getString(0).lastIndexOf(findName);
                if (index != -1 || index2 != -1) {
                    products.add(new Product(cursor.getInt(1), cursor.getString(0) ));
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
    }

    void SetSelectList(HashMap<String, Product> data) {
        for (Map.Entry<String, Product> dat : data.entrySet()) {
            MainActivity.selectProducts.add(new Product(dat.getValue().getkall(), dat.getKey(), dat.getValue().getgramm()));
        }
    }
}
