package com.example.alevtina.sh;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
public class AddSpentActivity extends Fragment {

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private Button backToMain ,saveRect;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_add_recd, null);

        ArrayList<Product> works = new ArrayList<>();
        works.add(new Product(100, "приседания"));
        works.add(new Product(20, "отжимания"));
        works.add(new Product(150, "подтягивания"));

        adapter = new ItemAdapter(works, 2);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        backToMain = (Button) view.findViewById(R.id.backToMain);
        saveRect = (Button) view.findViewById(R.id.saveRect);

        ListenerOnButton();

        return view;
    }

    void ListenerOnButton() {
        backToMain.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getFragmentManager().beginTransaction().remove(AddSpentActivity.this).commit();
                    }
                }
        );

        saveRect.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.SetStats();
                        getFragmentManager().beginTransaction().remove(AddSpentActivity.this).commit();
                    }
                }
        );
    }

}
