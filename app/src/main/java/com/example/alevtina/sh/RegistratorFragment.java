package com.example.alevtina.sh;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegistratorFragment extends Fragment {

    private Button saveUser;
    private ImageButton man, women;
    private EditText age, height, weight, target;
    private int gender = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registrator, null);

        man = view.findViewById(R.id.man);
        women = view.findViewById(R.id.women);
        saveUser = (Button) view.findViewById(R.id.saveuser);
        age = (EditText) view.findViewById(R.id.age);
        height = (EditText) view.findViewById(R.id.height);
        weight = (EditText) view.findViewById(R.id.weight);
        target = (EditText) view.findViewById(R.id.target);

        man.setBackgroundResource(0);
        women.setBackgroundResource(0);

        if (!MainActivity.first) {
            age.setText(Integer.toString(HomeFragment.user_age));
            height.setText(Integer.toString(HomeFragment.user_height));
            weight.setText(Integer.toString(HomeFragment.user_weight));
            target.setText(Integer.toString(HomeFragment.targetWeight));
            if (HomeFragment.user_gender == 1) {
                gender = 1;
                man.setBackgroundResource(R.drawable.mycolor);
                women.setBackgroundResource(0);
            } else {
                gender = 0;
                women.setBackgroundResource(R.drawable.mycolor);
                man.setBackgroundResource(0);
            }
        }

        Listener();

        return view;
    }

    void Listener() {
        man.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gender = 1;
                        man.setBackgroundResource(R.drawable.mycolor);
                        women.setBackgroundResource(0);
                    }
                }
        );
        women.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gender = 0;
                        women.setBackgroundResource(R.drawable.mycolor);
                        man.setBackgroundResource(0);
                    }
                }
        );
        saveUser.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String alertText = "";
                        boolean alert = false;
                        if (gender == -1) {
                            alertText = "Вы не вырали пол!";
                            alert = true;
                        } else if (height.getText().toString().length() == 0) {
                            alertText = "Вы не написали ваш рост";
                            alert = true;
                        } else if (weight.getText().toString().length() == 0) {
                            alertText = "Вы не написали ваш вес!";
                            alert = true;
                        } else if (age.getText().toString().length() == 0) {
                            alertText = "Вы не написали ваш возраст!";
                            alert = true;
                        } else if (target.getText().toString().length() == 0) {
                            alertText = "ВЫ не написали желаемый вес";
                            alert = true;
                        }
                        if (alert) GetAlert(alertText);
                        else {
                            SaveData();
                            save_first();
                            getActivity().findViewById(R.id.navigation_home).performClick();
                        }
                    }
                }
        );
    }

    void save_first() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putBoolean(MainActivity.FIRST_SAVE, false);
        ed.commit();
    }

    void GetAlert(String text) {
        Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
        toast.show();
    }

    void SaveData() {
        getActivity().findViewById(R.id.navigation_home).setEnabled(true);
        getActivity().findViewById(R.id.navigation_recd).setEnabled(true);
        getActivity().findViewById(R.id.navigation_spent).setEnabled(true);
        getActivity().findViewById(R.id.navigation_profil).setEnabled(true);
        HomeFragment.user_gender = gender;
        HomeFragment.user_age = Integer.parseInt(age.getText().toString());
        HomeFragment.user_height = Integer.parseInt(height.getText().toString());
        HomeFragment.user_weight = Integer.parseInt(weight.getText().toString());
        HomeFragment.targetWeight = Integer.parseInt(target.getText().toString());
        String date = SupportClass.GetDate();
        date = SupportClass.ReverseDate(date);
        HomeFragment.weightArray.put(date, Integer.parseInt(weight.getText().toString()));
        SupportClass.DataSave(getActivity());
        SupportClass.WeightSave(getActivity());
    }
}
