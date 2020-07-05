package com.example.corona.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ImageView;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatDialogFragment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.corona.R;

public class ExampleDialog extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        ImageView image = new ImageView(getContext());
        image.setImageResource(R.drawable.coronawatch);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("تنبيه")
                .setMessage("حذار لقد دخلت منطقة خطرة عليك توخ الحذر")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).
                setView(image);
        return builder.create();
    }
}
