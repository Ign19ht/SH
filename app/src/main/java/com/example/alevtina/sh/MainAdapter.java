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
import android.widget.TextView;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

    private boolean flag;
    ArrayList<Product> products = new ArrayList<>();

    public static class MainViewHolder extends RecyclerView.ViewHolder{

        TextView name, kall, attribute;
        EditText amt;
        Button clear;

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
        mainViewHolder.name.setText(product.getname());
        mainViewHolder.kall.setText(product.getkall());
        mainViewHolder.amt.setText(product.getgramm());
        if (flag) {
            mainViewHolder.attribute.setText("г");
        } else {
            mainViewHolder.attribute.setText("мин");
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
                if(mainViewHolder.amt.getText().length() == 0) grammint = 0;
                else
                    grammint = Integer.parseInt(mainViewHolder.amt.getText().toString());
                if (flag) {
                    mainViewHolder.kall.setText(Integer.toString(product.getkall() * grammint / 100));
                } else {
                    mainViewHolder.kall.setText(Integer.toString(product.getkall() * grammint / 10));
                }
            }
        });
        mainViewHolder.clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.selectProducts.remove(i);
                MainActivity.SetStats();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

//    void SetCounts(MainViewHolder mainViewHolder) {
//        if(flag)
//        MainActivity.countRecd = 0;
//        else
//        MainActivity.countSpent = 0;
//        for (int i = 0; i < products.size(); i++) {
//            if (flag)
//            MainActivity.countRecd += Integer.parseInt(mainViewHolder.kall.getText().toString());
//            else
//            MainActivity.countSpent += products.get(i).getkall();
//        }
//    }
}
