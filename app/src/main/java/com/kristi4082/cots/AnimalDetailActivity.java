package com.kristi4082.cots;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Aprilita on 12/6/2016.
 */


public class AnimalDetailActivity extends AppCompatActivity implements ImageUtil.OnImageLoadFinish, View.OnClickListener  {
    private Animal animal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        animal = (Animal) getIntent().getSerializableExtra("animal");
        if(animal == null)
            finish();

        getSupportActionBar().setTitle(animal.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        int counter = PreferenceUtil.with(this)
                .viewCountAdd(animal.getName())
                .getCount(animal.getName());
        String strCounter = getString(R.string.anda_telah_melihat_) + " " + animal.getName() + " " + counter +"x";
        TextView textView = (TextView) findViewById(R.id.textview_view_counter);
        textView.setText(strCounter);
        String strSource = getString(R.string.sumber_) + " " +animal.getImageUrl();
        TextView tvSource = (TextView) findViewById(R.id.textview_source);
        tvSource.setText(strSource);

        if(!animal.getImageUrl().equalsIgnoreCase("foto tidak tersedia")) {
            ImageView imageView = (ImageView) findViewById(R.id.imageview);
            ImageUtil.with(this).from(animal).into(imageView, this);
        }else{
            findViewById(R.id.progress_bar).setVisibility(View.GONE);
            Toast.makeText(AnimalDetailActivity.this, R.string.tidak_ada_foto, Toast.LENGTH_SHORT).show();
        }

        findViewById(R.id.button_open_browser).setOnClickListener(this);
    }

    @Override
    public void onSuccess(Bitmap bitmap) {
        StorageUtil.saveBitmap(animal.getName()+".png", bitmap);
        findViewById(R.id.progress_bar).setVisibility(View.GONE);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_open_browser){
            if(!animal.getImageUrl().equalsIgnoreCase("foto tidak tersedia")) {
                Uri uri = Uri.parse(animal.getImageUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                if (intent.resolveActivity(getPackageManager()) != null)
                    startActivity(intent);
                else
                    Toast.makeText(AnimalDetailActivity.this, R.string.can_not_open_browser, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(AnimalDetailActivity.this, R.string.tidak_ada_foto, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
