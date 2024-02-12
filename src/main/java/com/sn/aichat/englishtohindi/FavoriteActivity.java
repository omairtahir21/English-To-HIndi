    package com.sn.aichat.englishtohindi;

    import android.os.Bundle;
    import android.widget.ArrayAdapter;
    import android.widget.ListView;
    import android.widget.Toast;

    import androidx.appcompat.app.AppCompatActivity;

    import java.util.ArrayList;
    import java.util.List;

    public class FavoriteActivity extends AppCompatActivity {

        private ListView listView;
        private ArrayAdapter<String> adapter;
        private List<String> favoriteWordsList;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_favorite);

            listView = findViewById(R.id.listViewFavoriteWords);
            favoriteWordsList = new ArrayList<>();

            fetchFavoriteWords();
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, favoriteWordsList);
            listView.setAdapter(adapter);
        }

        private void fetchFavoriteWords() {
            try {
                List<NameModel> favoriteWords = DBManager1.getInstance(this).getFavoriteWords();

                for (NameModel word : favoriteWords) {
                    favoriteWordsList.add(word.getWords());
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error fetching favorite words", Toast.LENGTH_SHORT).show();
            }
        }
    }
