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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class HomeFragment extends Fragment {

    static TreeMap<String, Integer> weightArray = new TreeMap<>();

    public static int user_age = 1, user_height = 1, user_weight = 1, user_gender = -1, targetWeight = -1, targetKall = 2500;
    private static TextView countRecdView, countSpentView, resultView, targetKallView;
    private static int countRecd = 0, countSpent = 0;
    static TextView stepCount;
    private TextView weight;
    static View indicator;
    private LineChart mChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);

        countRecdView = (TextView) view.findViewById(R.id.countrecd);
        countSpentView = (TextView) view.findViewById(R.id.countspent);
        resultView = (TextView) view.findViewById(R.id.result);
        stepCount = view.findViewById(R.id.stepcount);
        indicator = view.findViewById(R.id.indicator);
        weight = view.findViewById(R.id.weight);
        targetKallView = view.findViewById(R.id.targetKall);

        mChart =  view.findViewById(R.id.lineChart);

        targetKall = (int) (10 * user_weight + 6.25 * user_height - 5 * user_age);
        if (user_gender == 1) {
            targetKall += 5;
        } else {
            targetKall -= 161;
        }
        targetKall *= 1.5;

        weight.setText(Integer.toString(user_weight) + " kg");
        targetKallView.setText(Integer.toString(targetKall) + " kkal");
        stepCount.setText(Integer.toString(MyService.numSteps));

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);

        LimitLine targetLine = new LimitLine(targetWeight, "Ваша цель");
        targetLine.setLineWidth(4f);
        targetLine.setLineColor(Color.GREEN);
        targetLine.enableDashedLine(10f, 10f, 0f);
        targetLine.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        targetLine.setTextSize(15f);

        ArrayList<Entry> yValues = new ArrayList<>();
        String[] valuesString;
        int max, min;

        if (weightArray.size() == 1) {
            valuesString = new String[3];
            String date = weightArray.firstKey();
            String reverse = SupportClass.ReverseDate(date);
            for (int i = -1; i < 2; i++) {
                valuesString[i + 1] = SupportClass.ReverseDate(SupportClass.GetDate(reverse, i)).substring(5,10);
            }
            int value = weightArray.get(date);
            max = value;
            min = value;
            yValues.add(new Entry(1, value));
        } else {
            valuesString = new String[weightArray.size()];
            ArrayList<Integer> values = new ArrayList<>();
            int index = 0;
            for (Map.Entry<String, Integer> item : weightArray.entrySet()) {
                yValues.add(new Entry(index, item.getValue()));
                valuesString[index] = item.getKey().substring(5, 10);
                values.add(item.getValue());
                index++;
            }
            values.add(targetWeight);
            max = Collections.max(values);
            min = Collections.min(values);
        }

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(targetLine);
        leftAxis.setAxisMinimum(min - 5);
        leftAxis.setAxisMaximum(max + 5);
        leftAxis.enableGridDashedLine(10f, 10f, 0);
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

        LineDataSet weightSet = new LineDataSet(yValues, "Ваш вес(кг)");
        weightSet.setFillAlpha(110);
        weightSet.setColor(Color.rgb(61, 135, 255));
        weightSet.setLineWidth(3f);
        weightSet.setValueTextSize(15f);
        weightSet.setValueTextColor(Color.BLACK);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(weightSet);

        LineData data = new LineData(dataSets);

        mChart.setData(data);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(valuesString));
        xAxis.setGranularity(1f);

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
        stepCount.setText(Integer.toString(MyService.numSteps));
        countSpent += MyService.numSteps * 0.00035 * user_weight;
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
