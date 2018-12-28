package com.example.alevtina.sh;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private int flag;

    ArrayList<Product> products = new ArrayList<>();
    HashMap<String, Product> checkproduct = new HashMap<>();

    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView name, kall, attribute;
        EditText amt;
        CheckBox check;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            kall = itemView.findViewById(R.id.kall);
            name = itemView.findViewById(R.id.name);
            check = itemView.findViewById(R.id.checkbox);
            amt = itemView.findViewById(R.id.amt);
            attribute = itemView.findViewById(R.id.attribute);
        }
    }

    public ItemAdapter(ArrayList<Product> products, int flag) {
        this.products = products;
        this.flag = flag;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder itemViewHolder, int i) {
        final Product product = products.get(i);
        final int kallint = product.getkall();
        itemViewHolder.name.setText(product.getname());
        itemViewHolder.kall.setText(Integer.toString(kallint));
        if (checkproduct.containsKey(product.getname())) {
            itemViewHolder.check.setChecked(true);
        }
        switch (flag) {
            case 1:
                itemViewHolder.attribute.setText("г");
                itemViewHolder.amt.setText("100");
                break;
            case 2:
                itemViewHolder.attribute.setText("мин");
                itemViewHolder.amt.setText("10");
                break;
        }
        itemViewHolder.amt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int grammint;
                if(itemViewHolder.amt.getText().length() == 0) grammint = 0;
                else
                grammint = Integer.parseInt(itemViewHolder.amt.getText().toString());
                switch (flag) {
                    case 1: itemViewHolder.kall.setText(Integer.toString(kallint * grammint / 100)); break;
                    case 2: itemViewHolder.kall.setText(Integer.toString(kallint * grammint / 10)); break;
                }
                if (itemViewHolder.check.isChecked()) {
                    Product data = new Product(kallint, Integer.parseInt(itemViewHolder.amt.getText().toString()));
                    checkproduct.put(product.getname(), data);
                }
            }
        });
        itemViewHolder.check.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Product data = new Product(kallint, Integer.parseInt(itemViewHolder.amt.getText().toString()));
                        if(isChecked) {
                            if (!checkproduct.containsKey(product.getname())) {
                                checkproduct.put(product.getname(), data);
                            }
                        } else {
                            if (checkproduct.containsKey(product.getname()))
                            checkproduct.remove(product.getname());
                        }
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
