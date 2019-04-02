package com.example.alevtina.sh;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    static HashMap<String, Integer> weightArray = new HashMap<>();

    private static TextView countRecdView, countSpentView, resultView;
    private static int countRecd = 0, countSpent = 0;
    private LinearLayout profile;
    static TextView stepCount;
    private TextView weight;
    static View indicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);

        countRecdView = (TextView) view.findViewById(R.id.countrecd);
        countSpentView = (TextView) view.findViewById(R.id.countspent);
        resultView = (TextView) view.findViewById(R.id.result);
        profile = view.findViewById(R.id.profile);
        stepCount = view.findViewById(R.id.stepcount);
        indicator = view.findViewById(R.id.indicator);
        weight = view.findViewById(R.id.weight);

        weight.setText(Integer.toString(MainActivity.user_weight) + " kg");
        stepCount.setText(Integer.toString(MyService.numSteps));

        int i = 0;

        for(Map.Entry<String, Integer> item : weightArray.entrySet()) {
            i++;
        }

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

    static void SetColor(int sp, int rec){
        if ((rec - sp >= 1950) && (rec-sp <= 2050)){
            indicator.setBackgroundResource(R.drawable.indicator_green);
        } else {
            indicator.setBackgroundResource(R.drawable.indicator_red);
        }
    }

    static void SetStats() {
        SetCounts();
        countRecdView.setText(Integer.toString(countRecd) + " kkal");
        countSpentView.setText(Integer.toString(countSpent) + " kkal");
        resultView.setText(Integer.toString(Math.abs(countRecd - countSpent)) + " kkal");
        SetColor(countSpent, countRecd);
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
