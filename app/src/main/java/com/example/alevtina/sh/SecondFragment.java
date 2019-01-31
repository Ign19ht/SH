package com.example.alevtina.sh;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("ValidFragment")
public class SecondFragment extends Fragment {

    static boolean flag;
    static boolean visible = false, order = true;
//    public static HashMap<String, ArrayList<Product>> selectProducts = new HashMap<>();
    public static ArrayList<Product> selectProducts = new ArrayList<>();
//    public static HashMap<String, ArrayList<Product>> selectExercises = new HashMap<>();
    public static ArrayList<Product> selectExercises = new ArrayList<>();
    static RecyclerView recyclerView;
    static private MainAdapter adapter;
    private TextView textView, cancel, deleteAll, date;
    private Button addFiled, edit, pasteDate, nextDate;
    static int stepProducts = 0, stepExercises = 0;

    @SuppressLint("ValidFragment")
    public SecondFragment(boolean flag) {
        SecondFragment.flag = flag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, null);

        deleteAll = view.findViewById(R.id.delete_all);
        textView = view.findViewById(R.id.text);
        edit = view.findViewById(R.id.edit);
        addFiled = view.findViewById(R.id.addFields);
        cancel = view.findViewById(R.id.cancel);
        pasteDate = view.findViewById(R.id.past_date);
        nextDate = view.findViewById(R.id.next_date);
        date = view.findViewById(R.id.date);

        if(flag) {
            textView.setText("Продукты");
        } else {
            textView.setText("Упражнения");
        }

        date.setText(SupportClass.GetTime());

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
//        pasteDate.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        boolean order = true;
//                        if (flag) {
//                            if (stepProducts - 1 == 15) order = false;
//                        } else {
//                            if (stepExercises - 1 == 15) order = false;
//                        }
//                        if (order) {
//                            if (flag) {
//                                stepProducts --;
//                                String today = SupportClass.GetTime();
//                                int day =
//                            }
//                        }
//                    }
//                }
//        );
        addFiled.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getFragmentManager().beginTransaction().replace(MainActivity.ID_FRAGMENT, new FieldListFragment(flag)).commit();
                        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    }
                }
        );
        edit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteAll.setVisibility(View.VISIBLE);
                        cancel.setVisibility(View.VISIBLE);
                        edit.setVisibility(View.INVISIBLE);
                        addFiled.setVisibility(View.INVISIBLE);
                        visible = true;
                        order = false;
                        DataChange();
                        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    }
                }
        );
        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteAll.setVisibility(View.INVISIBLE);
                        cancel.setVisibility(View.INVISIBLE);
                        edit.setVisibility(View.VISIBLE);
                        addFiled.setVisibility(View.VISIBLE);
                        visible = false;
                        DataChange();
                        order = true;
                    }
                }
        );
        deleteAll.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                        alert.setTitle("Вы хотите полностью очистить список?")
                                .setMessage("Данные нельзя будет потом восстановить")
                                .setIcon(R.drawable.ic_delete_black_24dp)
                                .setCancelable(true)
                                .setPositiveButton("Удалить все",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                deleteAll.setVisibility(View.INVISIBLE);
                                                cancel.setVisibility(View.INVISIBLE);
                                                edit.setVisibility(View.VISIBLE);
                                                addFiled.setVisibility(View.VISIBLE);
                                                visible = false;
                                                if (flag) {
                                                    selectProducts.clear();
                                                } else {
                                                    selectExercises.clear();
                                                }
                                                DataChange();
//                                                SupportClass.SetDataSaves(getActivity());
                                            }
                                        })
                                .setNegativeButton("Отменить",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                        AlertDialog alertDialog = alert.create();
                        alertDialog.show();
                    }
                }
        );
    }

}
