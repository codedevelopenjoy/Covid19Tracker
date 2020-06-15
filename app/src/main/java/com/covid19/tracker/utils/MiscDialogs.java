package com.covid19.tracker.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import com.covid19.tracker.R;
import androidx.appcompat.app.AlertDialog;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class MiscDialogs {

    @SuppressLint("InflateParams")
    public static void showSources(final Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = LayoutInflater.from(context).inflate(R.layout.sources_layout, null, false);
        view.findViewById(R.id.link1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.covid19india.org"));
                context.startActivity(intent);
            }
        });
        view.findViewById(R.id.link2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://corona.lmao.ninja/v2/countries"));
                context.startActivity(intent);
            }
        });

        dialog.setContentView(view);
        dialog.show();

        Window window = dialog.getWindow();
        if (window != null)
            window.setLayout(MATCH_PARENT, WRAP_CONTENT);

    }

    @SuppressLint("InflateParams")
    public static void showDevelopers(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.developers, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setCancelable(true);
        builder.create().show();
    }
}
