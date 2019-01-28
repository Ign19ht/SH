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

    private boolean flag;

    ArrayList<Product> products = new ArrayList<>();
    HashMap<String, Integer> checkproduct = new HashMap<>();
    boolean[] checked;

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

    public ItemAdapter(ArrayList<Product> products, boolean flag) {
        this.products = products;
        this.flag = flag;
        checked = new boolean[products.size()];
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder itemViewHolder, final int i) {
        final Product product = products.get(i);
        final int kallint = product.getkall();
        itemViewHolder.name.setText(product.getname());
        itemViewHolder.check.setChecked(checked[i]);
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
                if (flag) {
                    itemViewHolder.kall.setText(Integer.toString(MainActivity.KallCalculator(product.getkall(), grammint, true)));
                } else {
                    itemViewHolder.kall.setText(Integer.toString(MainActivity.KallCalculator(product.getkall(), grammint, false)));
                }
                if (checked[i]) {
                    checkproduct.put(product.getname(), grammint);
                }
            }
        });
        itemViewHolder.check.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checked[i] = itemViewHolder.check.isChecked();
                        if(checked[i]) {
                            if (!checkproduct.containsKey(product.getname())) {
                                checkproduct.put(product.getname(), Integer.parseInt(itemViewHolder.amt.getText().toString()));
                            }
                        } else {
                            if (checkproduct.containsKey(product.getname()))
                                checkproduct.remove(product.getname());
                        }

                    }
                }
        );
        if (checkproduct.containsKey(product.getname())) {
            checked[i] = true;
            itemViewHolder.amt.setText(Integer.toString(checkproduct.get(product.getname())));
        } else {
            checked[i] = false;
            if (flag) {
                itemViewHolder.attribute.setText("г");
                itemViewHolder.amt.setText("100");
            } else {
                itemViewHolder.attribute.setText("ч");
                itemViewHolder.amt.setText("1");
            }
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
