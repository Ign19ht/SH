package com.example.alevtina.sh;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressLint("ValidFragment")
public class FieldListExcercises extends Fragment {

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private Button saveCheked, backToMain, findClear;
    private EditText find;
    private ArrayList<Product> fields = new ArrayList<>();
    private String findName = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_field_list, null);

        backToMain = (Button) view.findViewById(R.id.backToMain);
        saveCheked = (Button) view.findViewById(R.id.savecheked);
        find = (EditText) view.findViewById(R.id.find);
        findClear = view.findViewById(R.id.find_clear);

        SetArrayList();

        adapter = new ItemAdapter(fields, false); // sfdererfreferfcerferf
        adapter.checked = new boolean[fields.size()];
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
                        getFragmentManager().beginTransaction().replace(MainActivity.ID_FRAGMENT,
                                new SecondFragment(false)).commit(); // sfcedfcercferc
                    }
                }
        );

        saveCheked.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (adapter.checkproduct.size() > 0) {
                            SetSelectList(adapter.checkproduct);
                            adapter.checkproduct.clear();
                            SecondFragment.DataChange();
                            getFragmentManager().beginTransaction().replace(MainActivity.ID_FRAGMENT,
                                    new SecondFragment(false)).commit(); // fcvedfcercfrfc
                        } else {
                            Toast.makeText(getActivity(), "Вы ничего не выбрали!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        findClear.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        find.setText("");
                        findClear.setVisibility(View.INVISIBLE);
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
                SetArrayList();
                adapter.checked = new boolean[fields.size()];
                adapter.notifyDataSetChanged();
                if (find.getText().length() != 0) {
                    findClear.setVisibility(View.VISIBLE);
                } else {
                    findClear.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    void SetArrayList() {
        fields.clear();
        Cursor cursor;
        cursor = MainActivity.mDb.rawQuery("SELECT * FROM exercises", //sdfedfvrvrfvrfv
                null);
        cursor.moveToFirst();
        if (findName.length() == 0) {
            while (!cursor.isAfterLast()) {
                int kall = cursor.getInt(1);
                String name = cursor.getString(0);
                fields.add(new Product(kall, name));
                cursor.moveToNext();
            }
        } else {
            while (!cursor.isAfterLast()) {
                int kall = cursor.getInt(1);
                String name = cursor.getString(0);
                int index = name.lastIndexOf(findName);
                findName = findName.substring(0, 1).toUpperCase() + findName.substring(1);
                int index2 = name.lastIndexOf(findName);
                if (index != -1 || index2 != -1) {
                    fields.add(new Product(kall, name));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
    }

    void SetSelectList(HashMap<String, Integer> data) {
        String date;
//            date = SupportClass.GetDate(SecondFragment.stepProducts);
            date = SupportClass.GetDate(SecondFragment.stepExercises);
        ArrayList<Product> list = new ArrayList<>();
//            if (SecondFragment.selectProducts.containsKey(date)) {
//                list.addAll(SecondFragment.selectProducts.get(date));
//            }
            if (SecondFragment.selectExercises.containsKey(date)) {
                list.addAll(SecondFragment.selectExercises.get(date));
            }
        for (Map.Entry<String, Integer> dat : data.entrySet()) {
            int kall = SupportClass.GetKallIsDB(dat.getKey(), false); // sfhedbcdjcnd
            list.add(new Product(kall, dat.getKey(), dat.getValue()));
        }
//            SecondFragment.selectProducts.put(date, list);
//            SupportClass.ProductSave(getActivity());
            SecondFragment.selectExercises.put(date, list);
            SupportClass.ExerciseSave(getActivity());
    }
}
