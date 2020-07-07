package com.example.corona.ui.notification;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.corona.R;

public class NotificationCounter {



    private TextView notificationNumber;


    public final int MAX_NUMBER =99;
    public int notification_number_counter = 0;

    public NotificationCounter(View view){
        notificationNumber = view.findViewById(R.id.notificationNumber);
    }

    public void increaseNumber(){

        notification_number_counter++;

        if(notification_number_counter> MAX_NUMBER){
            Log.d("Counter", "Maximum Number Reached!");
        }else{
            notificationNumber.setText(String.valueOf(notification_number_counter));
        }

    }


}
