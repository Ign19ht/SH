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
public class FieldListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private Button saveCheked, backToMain, findClear;
    private EditText find;
    private ArrayList<Product> fields = new ArrayList<>();
    private String findName = "";
    boolean flag;

    @SuppressLint("ValidFragment")
    public FieldListFragment(boolean flag) {
        this.flag = flag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_field_list, null);

        backToMain = (Button) view.findViewById(R.id.backToMain);
        saveCheked = (Button) view.findViewById(R.id.savecheked);
        find = (EditText) view.findViewById(R.id.find);
        findClear = view.findViewById(R.id.find_clear);

        SetArrayList();

        adapter = new ItemAdapter(fields, flag);
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
                        getFragmentManager().beginTransaction().replace(MainActivity.ID_FRAGMENT, new SecondFragment(flag)).commit();
                        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
                            getFragmentManager().beginTransaction().replace(MainActivity.ID_FRAGMENT, new SecondFragment(flag)).commit();
                            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        } else {
                            Toast.makeText(getActivity(), "Вы ничего не выбрали!", Toast.LENGTH_SHORT)
                                    .show();
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
                fields.clear();
                SetArrayList();
                adapter.notifyDataSetChanged();
                findClear.setVisibility(View.VISIBLE);
            }
        });
    }

    void SetArrayList() {
        Cursor cursor;
        if (flag) {
            cursor = MainActivity.mDb.rawQuery("SELECT * FROM products", null);
        } else {
            cursor = MainActivity.mDb.rawQuery("SELECT * FROM exercises", null);
        }
        cursor.moveToFirst();
        if (findName.length() == 0) {
            while (!cursor.isAfterLast()) {
                fields.add(new Product(cursor.getInt(1), cursor.getString(0)));
                cursor.moveToNext();
            }
            cursor.close();
        } else {
            while (!cursor.isAfterLast()) {
                int index = cursor.getString(0).lastIndexOf(findName);
                findName = findName.substring(0, 1).toUpperCase() + findName.substring(1);
                int index2 = cursor.getString(0).lastIndexOf(findName);
                if (index != -1 || index2 != -1) {
                    fields.add(new Product(cursor.getInt(1), cursor.getString(0)));
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
    }

    void SetSelectList(HashMap<String, Integer> data) {
        for (Map.Entry<String, Integer> dat : data.entrySet()) {
            Cursor cursor;
            if (flag) {
                cursor = MainActivity.mDb.rawQuery("SELECT * FROM products WHERE name=\"" + dat.getKey() + "\"", null);
            } else {
                cursor = MainActivity.mDb.rawQuery("SELECT * FROM exercises WHERE name=\"" + dat.getKey() + "\"", null);
            }
            cursor.moveToFirst();
            if (flag) {
                SecondFragment.selectProducts.add(new Product(cursor.getInt(1), dat.getKey(), dat.getValue()));
            } else {
                SecondFragment.selectExercises.add(new Product(cursor.getInt(1), dat.getKey(), dat.getValue()));
            }
            cursor.close();
        }
//        try {
//            OutputStreamWriter output = new OutputStreamWriter(getActivity().openFileOutput("save_dats", Context.MODE_PRIVATE), "utf8");
//            output.write(MainActivity.user_weight);
//            output.write(MainActivity.user_height);
//            output.write(MainActivity.user_age);
//            output.write(MainActivity.user_gender);
//            for (int i = 0; i < SecondFragment.selectProducts.size(); i++) {
//                output.write(SecondFragment.selectProducts.get(i).getname() + " ");
//                output.write(Integer.toString(SecondFragment.selectProducts.get(i).getkall()) + " f ");
//            }
//            for (int i = 0; i < SecondFragment.selectExercises.size(); i++) {
//                if (i == 0) {
//                    output.write("s ");
//                }
//                output.write(SecondFragment.selectExercises.get(i).getname() + " ");
//                output.write(Integer.toString(SecondFragment.selectExercises.get(i).getkall()) + " f ");
//            }
//            output.close();
//        } catch (Exception e) {
//            System.out.println("Ошибка");
//            e.printStackTrace();
//        }
    }
}
