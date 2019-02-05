package com.example.alevtina.sh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

    ArrayList<Product> products = new ArrayList<>();
    boolean flag;

    public static class MainViewHolder extends RecyclerView.ViewHolder {

        TextView name, kall, attribute;
        EditText amt;
        ImageButton clear;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.mainname);
            kall = itemView.findViewById(R.id.mainkall);
            amt = itemView.findViewById(R.id.mainamt);
            clear = itemView.findViewById(R.id.clear);
            attribute = itemView.findViewById(R.id.mainattribute);
        }
    }

    public MainAdapter(ArrayList<Product> products, boolean flag) {
        this.products = products;
        this.flag = flag;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_item, viewGroup, false);
        return new MainAdapter.MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder mainViewHolder, final int i) {
        final Product product = products.get(i);
        mainViewHolder.attribute.setText(flag ? "г" : "ч");
        mainViewHolder.clear.setVisibility(SecondFragment.visible ? View.VISIBLE : View.INVISIBLE);
        mainViewHolder.amt.setEnabled(!SecondFragment.visible);
        mainViewHolder.amt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int grammint;
                int temp;
                if (mainViewHolder.amt.getText().length() == 0) grammint = 0;
                else
                    grammint = Integer.parseInt(mainViewHolder.amt.getText().toString());
                temp = SupportClass.KallCalculator(product.getkall(), grammint, flag);
                mainViewHolder.kall.setText(Integer.toString(temp));
                if (SecondFragment.order) {
                    ArrayList<Product> list = new ArrayList<>();
                    String day = SupportClass.GetNewDate(flag ? SecondFragment.stepProducts : SecondFragment.stepExercises);
                    if (flag) {
                        SecondFragment.selectProducts.get(day).set(i, new Product(product.getkall(), product.getname(), grammint));
                        SupportClass.ProductSave(mainViewHolder.itemView.getContext());
                    } else {
                        SecondFragment.selectExercises.get(day).set(i, new Product(product.getkall(), product.getname(), grammint));
                        SupportClass.ExerciseSave(mainViewHolder.itemView.getContext());
                    }
//                    try {
//                        if (flag) {
//                            list.addAll(SecondFragment.selectProducts.get(SupportClass.GetNewDate(SecondFragment.stepProducts)));
//                            list.set(i, new Product(product.getkall(), product.getname(), grammint));
//                            SecondFragment.selectProducts.put(SupportClass.GetNewDate(SecondFragment.stepProducts), list);
//                            SupportClass.ProductSave(mainViewHolder.itemView.getContext());
//                        } else {
//                            list.addAll(SecondFragment.selectExercises.get(SupportClass.GetNewDate(SecondFragment.stepExercises)));
//                            list.set(i, new Product(product.getkall(), product.getname(), grammint));
//                            SecondFragment.selectExercises.put(SupportClass.GetNewDate(SecondFragment.stepExercises), list);
//                            SupportClass.ExerciseSave(mainViewHolder.itemView.getContext());
//                        }
//                    } catch (Exception e) {
//
//                    }
                }
            }
        });
        mainViewHolder.clear.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Product> list = new ArrayList<>();
                if (flag) {
                    list.addAll(SecondFragment.selectProducts.get(SupportClass.GetNewDate(SecondFragment.stepProducts)));
                    list.remove(i);
                    SecondFragment.selectProducts.put(SupportClass.GetNewDate(SecondFragment.stepProducts), list);
                    SupportClass.ProductSave(mainViewHolder.itemView.getContext());
                } else {
                    list.addAll(SecondFragment.selectExercises.get(SupportClass.GetNewDate(SecondFragment.stepExercises)));
                    list.remove(i);
                    SecondFragment.selectExercises.put(SupportClass.GetNewDate(SecondFragment.stepExercises), list);
                    SupportClass.ExerciseSave(mainViewHolder.itemView.getContext());
                }
                SecondFragment.DataChange();
            }
        });
        mainViewHolder.name.setText(product.getname());
        mainViewHolder.kall.setText(Integer.toString(product.getkall()));
        mainViewHolder.amt.setText(Integer.toString(product.getgramm()));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
