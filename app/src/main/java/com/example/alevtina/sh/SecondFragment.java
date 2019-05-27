package com.example.alevtina.sh;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("ValidFragment")
public class SecondFragment extends Fragment {

    static boolean flag;
    static boolean visible = false, order = true;
    public static HashMap<String, ArrayList<Product>> selectProducts = new HashMap<>();
    public static HashMap<String, ArrayList<Product>> selectExercises = new HashMap<>();
    private static ArrayList<Product> selectFields = new ArrayList<>();
    static RecyclerView recyclerView;
    static private MainAdapter adapter;
    private static TextView textView, cancel, deleteAll, dateView;
    private static Button addFiled, pasteDate, nextDate, refresh;
    static int stepProducts = 0, stepExercises = 0;
    private static Button edit;

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
        dateView = view.findViewById(R.id.date);
        refresh = view.findViewById(R.id.refresh);

        textView.setText(flag ? "Продукты" : "Упражнения");

        String today = SupportClass.GetDate(flag ? stepProducts : stepExercises);
        dateView.setText(today);

        selectFields.clear();

        if (flag) {
            if (selectProducts.containsKey(today)) {
                selectFields.addAll(selectProducts.get(today));
            }
        } else {
            if (selectExercises.containsKey(today)) {
                selectFields.addAll(selectExercises.get(today));
            }
        }

        if (selectFields.size() == 0) {
            edit.setVisibility(View.INVISIBLE);
        }

        if ((flag ? stepProducts : stepExercises) != 0) {
            refresh.setVisibility(View.VISIBLE);
        }

        adapter = new MainAdapter(selectFields, flag);
        recyclerView = view.findViewById(R.id.mainrecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        Listener();

        return view;
    }

    public static void DataChange() {
        String date = SupportClass.GetDate(flag ? stepProducts : stepExercises);
        selectFields.clear();
        if (flag) {
            if (selectProducts.containsKey(date)) {
                selectFields.addAll(selectProducts.get(date));
            }
        } else {
            if (selectExercises.containsKey(date)) {
                selectFields.addAll(selectExercises.get(date));
            }
        }
        if (selectFields.size() == 0) {
            SupportClass.ViewVisibility(false, new View[]{cancel, deleteAll, edit});
            addFiled.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }

    void Listener() {
        refresh.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SupportClass.ViewVisibility(true, new View[]{edit, addFiled});
                        SupportClass.ViewVisibility(false, new View[]{refresh});
                        if(flag) {
                            stepProducts = 0;
                        } else {
                            stepExercises = 0;
                        }
                        String date = SupportClass.GetDate();
                        dateView.setText(date);
                        DataChange();
                    }
                }
        );
        nextDate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean order = true;
                        if ((flag ? stepProducts : stepExercises) + 1 == 8) order = false;
                        if (order) {
                            if (flag) {
                                stepProducts++;
                            } else {
                                stepExercises++;
                            }
                            String date = SupportClass.GetDate(flag ? stepProducts : stepExercises);
                            dateView.setText(date);
                            DataChange();
                            if ((flag ? stepProducts : stepExercises) >= 0 && selectFields.size() != 0) {
                                SupportClass.ViewVisibility(true, new View[]{edit, addFiled});
                            }

                            if ((flag ? stepProducts : stepExercises) == 0) {
                                refresh.setVisibility(View.INVISIBLE);
                            } else {
                                refresh.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
        );
        pasteDate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean order = true;
                        if ((flag ? stepProducts : stepExercises) - 1 == -15) order = false;
                        if (order) {
                            if (flag) {
                                stepProducts--;
                            } else {
                                stepExercises--;
                            }
                            String date = SupportClass.GetDate(flag ? stepProducts : stepExercises);
                            dateView.setText(date);
                            DataChange();
                            if ((flag ? stepProducts : stepExercises) < 0) {
                                SupportClass.ViewVisibility(false, new View[]{edit, addFiled});
                            }
                            if ((flag ? stepProducts : stepExercises) == 0) {
                                refresh.setVisibility(View.INVISIBLE);
                            } else {
                                refresh.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
        );
        addFiled.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (flag) {
                            getFragmentManager().beginTransaction().replace(MainActivity.ID_FRAGMENT, new FieldListProducts()).commit();
                        } else{
                            getFragmentManager().beginTransaction().replace(MainActivity.ID_FRAGMENT, new FieldListExcercises()).commit();
                        }

                    }
                }
        );
        edit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        visible = true;
                        order = false;
                        DataChange();
                        SupportClass.ViewVisibility(true, new View[]{deleteAll, cancel});
                        SupportClass.ViewVisibility(false, new View[]{addFiled, refresh, edit});
                    }
                }
        );
        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SupportClass.ViewVisibility(false, new View[]{deleteAll, cancel});
                        SupportClass.ViewVisibility(true, new View[]{addFiled, edit});
                        if ((flag ? stepProducts : stepExercises) != 0) {
                            refresh.setVisibility(View.VISIBLE);
                        }
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
                                                SupportClass.ViewVisibility(false, new View[]{deleteAll, cancel});
                                                SupportClass.ViewVisibility(true, new View[]{addFiled, edit});
                                                visible = false;
                                                String date = SupportClass.GetDate(flag ? stepProducts : stepExercises);
                                                if (flag) {
                                                    selectProducts.get(date).clear();
                                                } else {
                                                    selectExercises.get(date).clear();
                                                }
                                                DataChange();
                                                order = true;
                                                if (flag) {
                                                    SupportClass.ProductSave(getActivity());
                                                } else {
                                                    SupportClass.ExerciseSave(getActivity());
                                                }

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
