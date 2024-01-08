package com.moutamid.sprachelernen.activities;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.moutamid.sprachelernen.BaseSecureActivity;
import com.moutamid.sprachelernen.Constants;
import com.moutamid.sprachelernen.databinding.ActivityPdfBinding;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class PDFActivity extends BaseSecureActivity {
    ActivityPdfBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.initDialog(this);
        Constants.showDialog();

        binding.toolbar.title.setText("Model Paper");
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        String paper = getIntent().getStringExtra(Constants.PAPER);

        Constants.databaseReference().child(Constants.getLanguage()).child(Constants.PAPER).child(paper)
                .get().addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()) {
                        String link = dataSnapshot.getValue(String.class);
                        new RetrivePDFfromUrl().execute(link);
                    }
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void displayFromUrl(InputStream url) {
        binding.pdf.fromStream(url)
                .defaultPage(0)
                .onPageChange((page, pageCount) -> {})
                .enableAnnotationRendering(true)
                .onLoad(nbPages -> Constants.dismissDialog())
                .onError(t -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
                })
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(10)
                .load();
    }

    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Constants.dismissDialog();
                    Toast.makeText(PDFActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            displayFromUrl(inputStream);
        }
    }

}