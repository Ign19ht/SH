package com.example.alevtina.sh;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private static TextView countRecdView, countSpentView, resultView;
    private static int countRecd = 0, countSpent = 0;
    private Button profile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);

        countRecdView = (TextView) view.findViewById(R.id.countrecd);
        countSpentView = (TextView) view.findViewById(R.id.countspent);
        resultView = (TextView) view.findViewById(R.id.result);
        profile = view.findViewById(R.id.profile);

        profile.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getFragmentManager().beginTransaction().replace(MainActivity.ID_FRAGMENT, new RegistratorFragment()).commit();
                    }
                }
        );

        SetStats();
        return view;
    }


    static void SetStats() {
        SetCounts();
        countRecdView.setText(Integer.toString(countRecd));
        countSpentView.setText(Integer.toString(countSpent));
        resultView.setText(Integer.toString(countRecd - countSpent));
    }

    private static void SetCounts() {
        countRecd = 0;
        countSpent = 0;
        ArrayList<Product> list = new ArrayList<>();
        String time = SupportClass.GetTime();
        if (SecondFragment.selectProducts.containsKey(time)) {
            list.addAll(SecondFragment.selectProducts.get(time));
            for (int i = 0; i < list.size(); i++) {
                countRecd += SupportClass.KallCalculator(list.get(i).getkall(), list.get(i).getgramm(), true);
            }
        }
        list.clear();
        if (SecondFragment.selectExercises.containsKey(time)) {
            list.addAll(SecondFragment.selectExercises.get(time));
            for (int i = 0; i < list.size(); i++) {
                countSpent += SupportClass.KallCalculator(list.get(i).getkall(), list.get(i).getgramm(), false);
            }
        }
    }
}
