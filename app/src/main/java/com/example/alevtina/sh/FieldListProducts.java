package com.example.alevtina.sh;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
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
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FieldListProducts extends Fragment {

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private Button saveCheked, backToMain, findClear;
    private EditText find;
    private ArrayList<Product> fields = new ArrayList<>();
    private String findName = "";
    private Button meet, vegan, milk, sweets, all, fruits, grass, bakes, drink, cereal, soup, seafood;
    final private static int ALL = -1, MILK = 4, MEET = 6, FRUITS = 1, VEGAN = 2,
            SWEETS = 11, GRASS = 3, BAKES = 8, DRINK = 7, CEREAL = 9, SOUP = 0, SEAFOOD = 5;
    private int type = ALL;

    public FieldListProducts() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_field_list_products, container, false);

        backToMain = (Button) view.findViewById(R.id.backToMain_pr);
        saveCheked = (Button) view.findViewById(R.id.savecheked_pr);
        find = (EditText) view.findViewById(R.id.find_pr);
        findClear = view.findViewById(R.id.find_clear_pr);
        all = view.findViewById(R.id.all);
        milk = view.findViewById(R.id.milk);
        meet = view.findViewById(R.id.meet);
        fruits = view.findViewById(R.id.fruits);
        vegan = view.findViewById(R.id.vegan);
        sweets = view.findViewById(R.id.sweets);
        bakes = view.findViewById(R.id.bakes);
        grass = view.findViewById(R.id.grass);
        soup = view.findViewById(R.id.soup);
        cereal = view.findViewById(R.id.cereal);
        drink = view.findViewById(R.id.drink);
        seafood = view.findViewById(R.id.seafood);

        all.setEnabled(false);
        all.setBackgroundResource(R.drawable.oval_dark);

        SetArrayList();

        adapter = new ItemAdapter(fields, true); // sfdererfreferfcerferf
        adapter.checked = new boolean[fields.size()];
        recyclerView = view.findViewById(R.id.recyclerview_pr);
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
                                new SecondFragment(true)).commit(); // sfcedfcercferc
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
                                    new SecondFragment(true)).commit(); // fcvedfcercfrfc
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

        all.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SetEnable();
                        all.setEnabled(false);
                        all.setBackgroundResource(R.drawable.oval_dark);
                        type = ALL;
                        SetArrayList();
                        adapter.checked = new boolean[fields.size()];
                        adapter.notifyDataSetChanged();
                    }
                }
        );

        milk.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SetEnable();
                        milk.setEnabled(false);
                        milk.setBackgroundResource(R.drawable.oval_dark);
                        type = MILK;
                        SetArrayList();
                        adapter.checked = new boolean[fields.size()];
                        adapter.notifyDataSetChanged();
                    }
                }
        );

        meet.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SetEnable();
                        meet.setEnabled(false);
                        meet.setBackgroundResource(R.drawable.oval_dark);
                        type = MEET;
                        SetArrayList();
                        adapter.checked = new boolean[fields.size()];
                        adapter.notifyDataSetChanged();
                    }
                }
        );

        fruits.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SetEnable();
                        fruits.setEnabled(false);
                        fruits.setBackgroundResource(R.drawable.oval_dark);
                        type = FRUITS;
                        SetArrayList();
                        adapter.checked = new boolean[fields.size()];
                        adapter.notifyDataSetChanged();
                    }
                }
        );

        vegan.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SetEnable();
                        vegan.setEnabled(false);
                        vegan.setBackgroundResource(R.drawable.oval_dark);
                        type = VEGAN;
                        SetArrayList();
                        adapter.checked = new boolean[fields.size()];
                        adapter.notifyDataSetChanged();
                    }
                }
        );

        drink.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SetEnable();
                        drink.setEnabled(false);
                        drink.setBackgroundResource(R.drawable.oval_dark);
                        type = DRINK;
                        SetArrayList();
                        adapter.checked = new boolean[fields.size()];
                        adapter.notifyDataSetChanged();
                    }
                }
        );

        sweets.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SetEnable();
                        sweets.setEnabled(false);
                        sweets.setBackgroundResource(R.drawable.oval_dark);
                        type = SWEETS;
                        SetArrayList();
                        adapter.checked = new boolean[fields.size()];
                        adapter.notifyDataSetChanged();
                    }
                }
        );

        grass.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SetEnable();
                        grass.setEnabled(false);
                        grass.setBackgroundResource(R.drawable.oval_dark);
                        type = GRASS;
                        SetArrayList();
                        adapter.checked = new boolean[fields.size()];
                        adapter.notifyDataSetChanged();
                    }
                }
        );

        bakes.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SetEnable();
                        bakes.setEnabled(false);
                        bakes.setBackgroundResource(R.drawable.oval_dark);
                        type = BAKES;
                        SetArrayList();
                        adapter.checked = new boolean[fields.size()];
                        adapter.notifyDataSetChanged();
                    }
                }
        );

        soup.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SetEnable();
                        soup.setEnabled(false);
                        soup.setBackgroundResource(R.drawable.oval_dark);
                        type = SOUP;
                        SetArrayList();
                        adapter.checked = new boolean[fields.size()];
                        adapter.notifyDataSetChanged();
                    }
                }
        );

        seafood.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SetEnable();
                        seafood.setEnabled(false);
                        seafood.setBackgroundResource(R.drawable.oval_dark);
                        type = SEAFOOD;
                        SetArrayList();
                        adapter.checked = new boolean[fields.size()];
                        adapter.notifyDataSetChanged();
                    }
                }
        );

        cereal.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SetEnable();
                        cereal.setEnabled(false);
                        cereal.setBackgroundResource(R.drawable.oval_dark);
                        type = CEREAL;
                        SetArrayList();
                        adapter.checked = new boolean[fields.size()];
                        adapter.notifyDataSetChanged();
                    }
                }
        );

    }

    void SetEnable() {
        final View[] viewList = new View[]{all, meet, milk, fruits, vegan, drink, seafood, cereal, soup, sweets, grass, bakes};
        for (View item : viewList) {
            item.setEnabled(true);
            item.setBackgroundResource(R.drawable.oval);
        }
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
        if (type == ALL) {
            cursor = MainActivity.mDb.rawQuery("SELECT * FROM products", //sdfedfvrvrfvrfv
                    null);
        } else {
            String string = String.valueOf(type);
            cursor = MainActivity.mDb.rawQuery("SELECT * FROM products WHERE type = " + string, //sdfedfvrvrfvrfv
                    null);
        }
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
        date = SupportClass.GetDate(SecondFragment.stepProducts);
//        date = SupportClass.GetDate(SecondFragment.stepExercises);
        ArrayList<Product> list = new ArrayList<>();
        if (SecondFragment.selectProducts.containsKey(date)) {
            list.addAll(SecondFragment.selectProducts.get(date));
        }
//        if (SecondFragment.selectExercises.containsKey(date)) {
//            list.addAll(SecondFragment.selectExercises.get(date));
//        }
        for (Map.Entry<String, Integer> dat : data.entrySet()) {
            int kall = SupportClass.GetKallIsDB(dat.getKey(), true); // sfhedbcdjcnd
            list.add(new Product(kall, dat.getKey(), dat.getValue()));
        }
        SecondFragment.selectProducts.put(date, list);
        SupportClass.ProductSave(getActivity());
//        SecondFragment.selectExercises.put(date, list);
//        SupportClass.ExerciseSave(getActivity());
    }

}
