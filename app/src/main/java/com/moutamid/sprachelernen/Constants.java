package com.moutamid.sprachelernen;

import com.fxn.stash.Stash;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.moutamid.sprachelernen.R;
import com.moutamid.sprachelernen.models.UserModel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Window;

import androidx.appcompat.app.AlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Constants {

    static Dialog dialog;
    public static final String DATEFORMAT = "dd/MM/yyyy";
    public static final String USER = "USER";
    public static final String STASH_USER = "STASH_USER";
    public static final String TOPIC_ID = "TOPIC_ID";
    public static final String LEVEL = "LEVEL";
    public static final String TOPIC = "TOPIC";
    public static final String SELECT = "SELECT";
    public static final String TOPICS = "TOPICS";
    public static final String URDU = "URDU";
    public static final String CONTENT = "CONTENT";
    public static final String EXERCISE = "EXERCISE";
    public static final String PASS = "PASS";
    public static final String Speaking = "Speaking";
    public static final String Reading = "Reading";
    public static final String Vocabulary = "Vocabulary";
    public static final String Writing = "Writing";
    public static final String SHOW_TOOLBAR = "SHOW_TOOLBAR";
    public static final String TRIAL_QUESTIONS = "TRIAL_QUESTIONS";

    public static String getFormattedDate(long date){
        return new SimpleDateFormat(DATEFORMAT, Locale.getDefault()).format(date);
    }

    public static void initDialog(Context context){
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
    }

    public static void showDialog(){
        dialog.show();
    }

    public static void dismissDialog(){
        dialog.dismiss();
    }

    public static String getLanguage() {
        UserModel userModel = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);

        String lang = Constants.URDU;

        if (userModel.getLanguage().equals("ur")){
            lang = Constants.URDU;
        }

        return lang;
    }

    public static void checkApp(Activity activity) {
        String appName = "sprachelernen";

        new Thread(() -> {
            URL google = null;
            try {
                google = new URL("https://raw.githubusercontent.com/Moutamid/Moutamid/main/apps.txt");
            } catch (final MalformedURLException e) {
                e.printStackTrace();
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(google != null ? google.openStream() : null));
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String input = null;
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if ((input = in != null ? in.readLine() : null) == null) break;
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                stringBuffer.append(input);
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String htmlData = stringBuffer.toString();

            try {
                JSONObject myAppObject = new JSONObject(htmlData).getJSONObject(appName);

                boolean value = myAppObject.getBoolean("value");
                String msg = myAppObject.getString("msg");

                if (value) {
                    activity.runOnUiThread(() -> {
                        new AlertDialog.Builder(activity)
                                .setMessage(msg)
                                .setCancelable(false)
                                .show();
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }).start();
    }
    public static FirebaseAuth auth() {
        return FirebaseAuth.getInstance();
    }

    public static DatabaseReference databaseReference() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("sprachelernen");
        db.keepSynced(true);
        return db;
    }

    public static StorageReference storageReference(String auth) {
        StorageReference sr = FirebaseStorage.getInstance().getReference().child("sprachelernen").child(auth);
        return sr;
    }

}
