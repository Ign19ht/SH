package com.example.alevtina.sh;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class RegistratorFragment extends Fragment {

    private Button man, women, saveUser;
    private EditText age, height, weight;
    private boolean clickGender = false;
    private int gender;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registrator, null);

        man = (Button) view.findViewById(R.id.man);
        women = (Button) view.findViewById(R.id.women);
        saveUser = (Button) view.findViewById(R.id.saveuser);
        age = (EditText) view.findViewById(R.id.age);
        height = (EditText) view.findViewById(R.id.height);
        weight = (EditText) view.findViewById(R.id.weight);

        Listener();

        return view;
    }

    void Listener() {
        man.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickGender = true;
                        man.setBackgroundColor(Color.argb(0, 0, 255, 0));
                        women.setBackgroundColor(Color.argb(0, 255, 0, 0));
                        gender = 1;
                    }
                }
        );
        women.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickGender = true;
                        women.setBackgroundColor(Color.argb(0, 0, 255, 0));
                        man.setBackgroundColor(Color.argb(0, 255, 0, 0));
                        gender = 0;
                    }
                }
        );
        saveUser.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String alertText = "";
                        boolean alert = false;
                        if (!clickGender) {
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
                        }
                        if (alert) GetAlert(alertText);
                        else {
                            SaveData();
                        }
                    }
                }
        );
    }

    void GetAlert(String text) {
        Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
        toast.show();
    }

    void SaveData() {
        if (MainActivity.user_gender == -1) {
            getActivity().findViewById(R.id.navigation_home).setEnabled(true);
            getActivity().findViewById(R.id.navigation_recd).setEnabled(true);
            getActivity().findViewById(R.id.navigation_spent).setEnabled(true);
        }
        MainActivity.user_gender = gender;
        MainActivity.user_age = Integer.parseInt(age.getText().toString());
        MainActivity.user_height = Integer.parseInt(height.getText().toString());
        MainActivity.user_weight = Integer.parseInt(weight.getText().toString());
        getFragmentManager().beginTransaction().replace(MainActivity.ID_FRAGMENT, new HomeFragment()).commit();
        SupportClass.DataSave(getActivity());
    }
}
