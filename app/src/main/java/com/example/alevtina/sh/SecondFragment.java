package com.example.alevtina.sh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class SecondFragment extends Fragment {

    static boolean flag;
    static boolean visible = false, order = true;
    public static ArrayList<Product> selectProducts = new ArrayList<>();
    public static ArrayList<Product> selectExercises = new ArrayList<>();
    static RecyclerView recyclerView;
    static private MainAdapter adapter;
    private TextView textView, cancel;
    private Button addFiled, edit;

    @SuppressLint("ValidFragment")
    public SecondFragment(boolean flag) {
        SecondFragment.flag = flag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, null);

        textView = view.findViewById(R.id.text);
        edit = view.findViewById(R.id.edit);
        addFiled = view.findViewById(R.id.addFields);
        cancel = view.findViewById(R.id.cancel);

        if(flag) {
            textView.setText("Ваши продукты");
        } else {
            textView.setText("Ваши упражнения");
        }

        if (flag) {
            adapter = new MainAdapter(selectProducts, true);
        } else {
            adapter = new MainAdapter(selectExercises, false);
        }

        recyclerView = view.findViewById(R.id.mainrecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        Listener();

        return view;
    }

    public static void DataChange() {
        adapter.notifyDataSetChanged();
    }

    void Listener() {
        addFiled.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getFragmentManager().beginTransaction().replace(MainActivity.ID_FRAGMENT, new FieldListFragment(flag)).commit();
                    }
                }
        );
        edit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancel.setVisibility(View.VISIBLE);
                        edit.setVisibility(View.INVISIBLE);
                        addFiled.setVisibility(View.INVISIBLE);
                        visible = true;
                        order = false;
                        DataChange();
                    }
                }
        );
        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancel.setVisibility(View.INVISIBLE);
                        edit.setVisibility(View.VISIBLE);
                        addFiled.setVisibility(View.VISIBLE);
                        visible = false;
                        DataChange();
                        order = true;
                    }
                }
        );
    }

}
