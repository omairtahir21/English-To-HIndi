package com.sn.aichat.englishtohindi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private boolean isFavorite = false;
    private String englishWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView detailBack = findViewById(R.id.BACKDetail);
        detailBack.setOnClickListener(v -> {
            Intent intent = new Intent(DetailActivity.this, Dictionary.class);
            startActivity(intent);
        });

        TextView textViewEnglish = findViewById(R.id.textViewEnglishWord);
        TextView textViewHindi = findViewById(R.id.textViewHindiWord);
        TextView textViewHindiMeaning = findViewById(R.id.textViewHindiMeans);
        TextView textViewType = findViewById(R.id.textViewWordType);
        TextView textViewEnglishMeaning = findViewById(R.id.textViewEnglishMeans);


        englishWord = getIntent().getStringExtra("english");
        String Hindi = getIntent().getStringExtra("Hindi");
        String hindiMeaning = getIntent().getStringExtra("hindiMeaning");
        String type = getIntent().getStringExtra("type");
        String englishMeaning = getIntent().getStringExtra("englishMeaning");

        textViewEnglish.setText(englishWord);
        textViewHindi.setText(Hindi);
        textViewHindiMeaning.setText(hindiMeaning);
        textViewType.setText(type);
        textViewEnglishMeaning.setText(englishMeaning);

        ImageView favoriteIcon = findViewById(R.id.imageView2);
        favoriteIcon.setOnClickListener(v -> toggleFavorite());
    }

    private void toggleFavorite() {
        if (!isFavorite) {
            addToFavorites();
        } else {
            removeFromFavorites();
        }
    }

    private void addToFavorites() {
        DBManager1.getInstance(this).addFavorite(englishWord);
        ImageView favoriteIcon = findViewById(R.id.imageView2);
        favoriteIcon.setImageResource(R.drawable.ic_fav);
        isFavorite = true;
        Toast.makeText(DetailActivity.this, "Added to Favorites", Toast.LENGTH_SHORT).show();
    }

    private void removeFromFavorites() {
        DBManager1.getInstance(this).removeFavoriteWord(englishWord);
        ImageView favoriteIcon = findViewById(R.id.imageView2);
        favoriteIcon.setImageResource(R.drawable.ic_unfav);
        isFavorite = false;
        Toast.makeText(DetailActivity.this, "Removed from Favorites", Toast.LENGTH_SHORT).show();
    }
}
