package com.example.alevtina.sh;

import android.content.Context;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SupportClass {

    public static final String SAVE_DATA = "save_dats";
    public static final String SAVE_PRODUCT = "save_products";
    public static final String SAVE_EXERCISES = "save_exercises";

    public static int KallCalculator(int kall, int gramm, boolean flag) {
        int total;
        if (flag) {
            total = kall * gramm / 100;
            return total;
        } else {
            total = kall * gramm * MainActivity.user_weight;
            return total;
        }
    }

    public static void ProductSave(Context context) {
        try {
            OutputStream file = context.openFileOutput(SAVE_PRODUCT, Context.MODE_PRIVATE);
            OutputStreamWriter output = new OutputStreamWriter(file , "utf8");

        } catch (Exception e) {

        }
    }

    public static void DataSave(Context context) {
        try {
            OutputStream file = context.openFileOutput(SAVE_DATA, Context.MODE_PRIVATE);
            OutputStreamWriter output = new OutputStreamWriter(file , "utf8");
            output.write(MainActivity.user_weight);
            output.write(MainActivity.user_height);
            output.write(MainActivity.user_age);
            output.write(MainActivity.user_gender);
            for (int i = 0; i < SecondFragment.selectProducts.size(); i++) {
                output.write(SecondFragment.selectProducts.get(i).getname() + " ");
                output.write(Integer.toString(SecondFragment.selectProducts.get(i).getgramm()) + " f ");
            }
            for (int i = 0; i < SecondFragment.selectExercises.size(); i++) {
                if (i == 0) {
                    output.write("s");
                }
                output.write(SecondFragment.selectExercises.get(i).getname() + " ");
                output.write(Integer.toString(SecondFragment.selectExercises.get(i).getgramm()) + " f ");
            }
            output.close();
        } catch (Exception e) {
            System.out.println("Ошибка");
            e.printStackTrace();
        }
    }

    public static String GetNewDate(int countDay) {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DATE, countDay);
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        String newDate = format.format(calendar.getTime());
        return newDate;
    }

    public static String GetTime() {
        return new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
    }
}
