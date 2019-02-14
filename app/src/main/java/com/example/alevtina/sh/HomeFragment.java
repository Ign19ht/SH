package com.example.alevtina.sh;

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
    private TextView stepCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);

        countRecdView = (TextView) view.findViewById(R.id.countrecd);
        countSpentView = (TextView) view.findViewById(R.id.countspent);
        resultView = (TextView) view.findViewById(R.id.result);
        profile = view.findViewById(R.id.profile);
        stepCount = view.findViewById(R.id.stepcount);

        stepCount.setText(Integer.toString(MyService.numSteps));

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
        String date = SupportClass.GetDate();
        if (SecondFragment.selectProducts.containsKey(date)) {
            list.addAll(SecondFragment.selectProducts.get(date));
            for (int i = 0; i < list.size(); i++) {
                int kall = list.get(i).getkall();
                int gramm = list.get(i).getgramm();
                countRecd += SupportClass.KallCalculator(kall, gramm, true);
            }
        }
        list.clear();
        if (SecondFragment.selectExercises.containsKey(date)) {
            list.addAll(SecondFragment.selectExercises.get(date));
            for (int i = 0; i < list.size(); i++) {
                int kall = list.get(i).getkall();
                int gramm = list.get(i).getgramm();
                countSpent += SupportClass.KallCalculator(kall, gramm, false);
            }
        }
    }
}
