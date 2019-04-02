package com.example.alevtina.sh;

import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.view.View;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class SupportClass {

    public static final String SAVE_DATA = "save_dats";
    public static final String SAVE_PRODUCT = "save_products";
    public static final String SAVE_EXERCISE = "save_exercises";
    public static final String SAVE_WEIGHT = "save_weight";

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
            OutputStreamWriter output = new OutputStreamWriter(file, "utf8");
            for (Map.Entry<String, ArrayList<Product>> list : SecondFragment.selectProducts.entrySet()) {
                output.write(list.getKey() + "\n");
                for (Product product : list.getValue()) {
                    output.write(product.getname() + "\n");
                    output.write(product.getgramm() + "\n");
                }
                output.write("|\n");
            }
            output.close();
        } catch (Exception e) {
            System.out.println("ERROR IN PRODUCTS SAVE :");
            e.printStackTrace();
        }
    }

    public static void ExerciseSave(Context context) {
        try {
            OutputStream file = context.openFileOutput(SAVE_EXERCISE, Context.MODE_PRIVATE);
            OutputStreamWriter output = new OutputStreamWriter(file, "utf8");
            for (Map.Entry<String, ArrayList<Product>> list : SecondFragment.selectExercises.entrySet()) {
                output.write(list.getKey() + "\n");
                for (Product product : list.getValue()) {
                    output.write(product.getname() + "\n");
                    output.write(product.getgramm() + "\n");
                }
                output.write("|\n");
            }
            output.close();
        } catch (Exception e) {
            System.out.println("ERROR IN EXERCISES SAVE :");
            e.printStackTrace();
        }
    }

    public static void WeightSave(Context context) {
        try {
            OutputStream file = context.openFileOutput(SAVE_WEIGHT, Context.MODE_PRIVATE);
            OutputStreamWriter output = new OutputStreamWriter(file, "utf8");
            for (Map.Entry<String, Integer> item : HomeFragment.weightArray.entrySet()) {
                output.write(item.getKey());
                output.write(item.getValue());
            }
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void DataSave(Context context) {
        try {
            OutputStream file = context.openFileOutput(SAVE_DATA, Context.MODE_PRIVATE);
            OutputStreamWriter output = new OutputStreamWriter(file, "utf8");
            output.write(MainActivity.user_weight);
            output.write(MainActivity.user_height);
            output.write(MainActivity.user_age);
            output.write(MainActivity.user_gender);
            output.write(MainActivity.target);
            output.close();
        } catch (Exception e) {
            System.out.println("ERROR IN DATA SAVE :");
            e.printStackTrace();
        }
    }

    public static String GetDate(int countDay) {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DATE, countDay);
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        String date = format.format(calendar.getTime());
        return date;
    }

    public static String GetDate() {
        Date today = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String date = dateFormat.format(today);
        return date;
    }

    public static boolean CompareDates(String string, int count) {
        Calendar today = Calendar.getInstance();
        today.add(Calendar.DATE, -count);
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        Calendar calendar = Calendar.getInstance();
        Date day;
        try {
            day = format.parse(string);
            calendar.setTime(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.before(today);
    }



    public static boolean ItsToDay(String date) {
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        String toDay = format.format(today);
        Date day;
        try {
            today = format.parse(toDay);
            day = format.parse(date);
            return today.equals(day);
        } catch (Exception e) {
            System.out.println("SPASI I SOHRANI");
            e.printStackTrace();
        }
        return false;
    }

    public static int GetKallIsDB(String name, boolean flag) {
        Cursor cursor;
        cursor = MainActivity.mDb.rawQuery("SELECT * FROM " + (flag ? "products" : "exercises")
                + " WHERE name=\"" + name + "\"", null);
        cursor.moveToFirst();
        int kall = cursor.getInt(1);
        cursor.close();
        return kall;
    }

    public static void ViewVisibility(boolean visible, View[] view) {
        for (int i = 0; i < view.length; i++) {
            view[i].setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
    }
}
