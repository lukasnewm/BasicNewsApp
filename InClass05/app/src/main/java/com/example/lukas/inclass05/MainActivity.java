package com.example.lukas.inclass05;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//Lukas Newman
//Emulated On Api 25

public class MainActivity extends AppCompatActivity implements GetJSONAsync.ArticleData {
    TextView showCatagory;
    TextView showAmmount;
    TextView showDescription;
    TextView showTitle;
    TextView showPub;
    ArrayList<Article> theArticles = new ArrayList<>();

    ProgressBar newImageLoad;

    ImageView showImage;
    ImageButton btnNext;
    ImageButton btnPrev;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showCatagory = findViewById(R.id.textCatagory);
        showCatagory = findViewById(R.id.textCatagory);
        showTitle = findViewById(R.id.textTitle);
        showPub = findViewById(R.id.textPub);
        showAmmount = findViewById(R.id.textAmount);
        showDescription = findViewById(R.id.textDescription);
        showImage = findViewById(R.id.imageView);
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);

        btnNext.setEnabled(false);
        btnPrev.setEnabled(false);
        btnNext.setBackgroundResource(R.drawable.next);
        btnPrev.setBackgroundResource(R.drawable.prev);
        showImage.setImageResource(android.R.color.transparent);

        findViewById(R.id.btnGO).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected()){

                    final CharSequence[] options = {"Business", "Entertainment", "General", "Health", "Science", "Sports", "Technology"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //System.out.println(options[i].toString());
                            Log.d("demo",options[i].toString());
                            showCatagory.setText(options[i]);
                            new GetJSONAsync(MainActivity.this).execute(options[i]+"&apiKey=1966f9bdfcc345b1b66e0aa4d0468675");
                            count = 0;
                        }
                    });
                    builder.create();
                    builder.show();



                    //new GetJSONAsync().execute();

                } else {
                    Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextArticle();
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevArticle();
            }
        });


    }

    public void showArticle(){
        showDescription.setText(theArticles.get(count).description.toString());
        showTitle.setText(theArticles.get(count).title.toString());
        showPub.setText(theArticles.get(count).pub.toString());
        //Show and dismiss a dialog here and check for connection again before loading image
        if (isConnected()) {
            showImage.setVisibility(View.VISIBLE);
            newImageLoad = new ProgressBar(this);
            newImageLoad.setVisibility(View.VISIBLE);
            newImageLoad.setProgress(50);
            Picasso.get().load(theArticles.get(count).url).into(showImage);
            newImageLoad.setProgress(100);
            //newImageLoad.setMax(100);
            newImageLoad.setVisibility(View.INVISIBLE);
        } else {
            Toast.makeText(this, "No Internet to load Image", Toast.LENGTH_SHORT).show();
            showImage.setVisibility(View.INVISIBLE);
        }
        int disCount = count + 1;
        int totalArticles = theArticles.size();
        showAmmount.setText(disCount + " of " + totalArticles);

        if (theArticles.size() > 1){
            btnNext.setEnabled(true);
            btnPrev.setEnabled(true);
        } else {
            btnPrev.setEnabled(false);
            btnNext.setEnabled(false);
        }

    }

    public void nextArticle(){
        count++;
        showArticle();

        if(count == theArticles.size() - 1){
            //btnNext.setVisibility(View.INVISIBLE);
            count = -1;
        }

    }

    public void prevArticle(){
        if(count == 0){
           // btnPrev.setVisibility(View.INVISIBLE);
            count = theArticles.size() - 1;
            showArticle();
        } else {
            count--;
            showArticle();
        }

    }

    public void passData(ArrayList<Article> someArticles){
        theArticles = someArticles;
        Log.d("demo", someArticles.toString());
        if (theArticles.size() > 0) {
            showArticle();
        } else {
            Toast.makeText(this, "No News", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

}
