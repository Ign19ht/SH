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
        if (flag) {
            mainViewHolder.attribute.setText("г");
        } else {
            mainViewHolder.attribute.setText("ч");
        }
        if (SecondFragment.visible) {
            mainViewHolder.clear.setVisibility(View.VISIBLE);
            mainViewHolder.amt.setEnabled(false);
        } else {
            mainViewHolder.clear.setVisibility(View.INVISIBLE);
            mainViewHolder.amt.setEnabled(true);
        }
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
                if (flag) {
                    temp = SupportClass.KallCalculator(product.getkall(), grammint, true);
                    mainViewHolder.kall.setText(Integer.toString(temp));
                } else {
                    temp = SupportClass.KallCalculator(product.getkall(), grammint, false);
                    mainViewHolder.kall.setText(Integer.toString(temp));
                }
                if (SecondFragment.order) {
                    if (flag) {
                        SecondFragment.selectProducts.set(i, new Product(product.getkall(), product.getname(), grammint));
                    } else {
                        SecondFragment.selectExercises.set(i, new Product(product.getkall(), product.getname(), grammint));
                    }
                    SupportClass.DataSave(mainViewHolder.itemView.getContext());
                }
            }
        });
        mainViewHolder.clear.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    SecondFragment.selectProducts.remove(i);
                } else {
                    SecondFragment.selectExercises.remove(i);
                }
                SecondFragment.DataChange();
                SupportClass.DataSave(mainViewHolder.itemView.getContext());
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
