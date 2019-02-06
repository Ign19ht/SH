package com.example.alevtina.sh;

import android.content.Context;
import android.database.Cursor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SupportClass {

    public static final String SAVE_DATA = "save_dats";
    public static final String SAVE_PRODUCT = "save_products";
    public static final String SAVE_EXERCISE = "save_exercises";

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

    public static void ReadData(Context context) {
        try {
            InputStream file = context.openFileInput(SupportClass.SAVE_DATA);
            InputStreamReader input = new InputStreamReader(file, "utf8");
            MainActivity.user_weight = input.read();
            MainActivity.user_height = input.read();
            MainActivity.user_age = input.read();
            MainActivity.user_gender = input.read();
            input.close();
        } catch (Exception e) {
            System.out.println("ERROR IN READ DATA :");
            e.printStackTrace();
        }
        try {
            InputStream file = context.openFileInput(SupportClass.SAVE_PRODUCT);
            InputStreamReader input = new InputStreamReader(file, "utf8");
            BufferedReader buffer = new BufferedReader(input);
            ArrayList<Product> list = new ArrayList<>();
            String time = buffer.readLine();
            String name = buffer.readLine();
            while (name != null) {
                if (name.equals("|")) {
                    ArrayList<Product> array = new ArrayList<>();
                    array.addAll(list);
                    SecondFragment.selectProducts.put(time, array);
                    time = buffer.readLine();
                    list.clear();
                } else {
                    int kall = GetKallIsDB(name, true);
                    int gramm = Integer.parseInt(buffer.readLine());
                    list.add(new Product(kall, name, gramm));
                }
                name = buffer.readLine();
            }
            buffer.close();
        } catch (Exception e) {
            System.out.println("ERROR IN READ PRODUCTS :");
            e.printStackTrace();
        }
        try {
            InputStream file = context.openFileInput(SupportClass.SAVE_EXERCISE);
            InputStreamReader input = new InputStreamReader(file, "utf8");
            BufferedReader buffer = new BufferedReader(input);
            ArrayList<Product> list = new ArrayList<>();
            String time = buffer.readLine();
            String name = buffer.readLine();
            while (name != null) {
                if (name.equals("|")) {
                    ArrayList<Product> array = new ArrayList<>();
                    array.addAll(list);
                    SecondFragment.selectExercises.put(time, array);
                    time = buffer.readLine();
                    list.clear();
                } else {
                    int kall = GetKallIsDB(name, false);
                    int gramm = Integer.parseInt(buffer.readLine());
                    list.add(new Product(kall, name, gramm));
                }
                name = buffer.readLine();
            }
            buffer.close();
        } catch (Exception e) {
            System.out.println("ERROR IN READ EXERCISES :");
            e.printStackTrace();
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
            System.out.println("ERROR IN PRODUCST SAVE :");
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

    public static void DataSave(Context context) {
        try {
            OutputStream file = context.openFileOutput(SAVE_DATA, Context.MODE_PRIVATE);
            OutputStreamWriter output = new OutputStreamWriter(file, "utf8");
            output.write(MainActivity.user_weight);
            output.write(MainActivity.user_height);
            output.write(MainActivity.user_age);
            output.write(MainActivity.user_gender);
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

    public static int GetKallIsDB(String name, boolean flag) {
        Cursor cursor;
        cursor = MainActivity.mDb.rawQuery("SELECT * FROM " + (flag ? "products" : "exercises")
                + " WHERE name=\"" + name + "\"", null);
        cursor.moveToFirst();
        int kall = cursor.getInt(1);
        cursor.close();
        return kall;
    }
}
