package com.sn.aichat.englishtohindi;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class Dictionary extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WordsAdapter wordsAdapter;
    private DBManager1 dbManager;
    private EditText editTextSearch;
    private static final int SPEECH_REQUEST_CODE = 100;
    private Switch switchLabel;
    private ImageView micIcon;
    private boolean isSpeechInputDone = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dictionary);

        dbManager = DBManager1.getInstance(this);

        recyclerView = findViewById(R.id.recyclerViewSearchResults);
        ImageView backButton = findViewById(R.id.BACKDICTIONARY);
        switchLabel = findViewById(R.id.switchLabel);
        TextView textView = findViewById(R.id.textView);
        editTextSearch = findViewById(R.id.editTextSearch);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        wordsAdapter = new WordsAdapter(new ArrayList<>(), this::onWordClick);
        recyclerView.setAdapter(wordsAdapter);

        switchLabel.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String searchText = editTextSearch.getText().toString();
            List<NameModel> updatedWords;
            if (isChecked) {
                updatedWords = dbManager.getHindiWords();
                switchLabel.setText("Hindi");
                textView.setText("  शब्दकोश");
            } else {
                updatedWords = dbManager.getEnglishWords();
                switchLabel.setText("English");
                textView.setText("  Dictionary");
            }
            updateRecyclerView(updatedWords, searchText);
        });

        switchLabel.setOnClickListener(v -> editTextSearch.getText().clear());

        editTextSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editTextSearch.getText().toString().isEmpty()) {
                    micIcon.setImageResource(R.drawable.ic_mic);
                    // Hide the clear icon if EditText is empty
                    isSpeechInputDone = false;
                } else {
                    // Show the clear icon if EditText has text
                    micIcon.setImageResource(R.drawable.ic_clear);
                    isSpeechInputDone = true;
                }
                String searchText = charSequence.toString();
                List<NameModel> words;
                if (switchLabel.isChecked()) {
                    words = dbManager.getHindiWords();
                } else {
                    words = dbManager.getEnglishWords();
                }
                updateRecyclerView(words, searchText);
            }
            public void afterTextChanged(Editable editable) {}
        });

        List<NameModel> englishWords = dbManager.getEnglishWords();
        updateRecyclerView(englishWords, "");

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(Dictionary.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        micIcon = findViewById(R.id.micIcon); // Initialize micIcon

        micIcon.setOnClickListener(v -> {
            if (!isSpeechInputDone) {
                startSpeechRecognition();
            } else {
                editTextSearch.getText().clear(); // Clear the EditText text
                toggleMicOrClearIcon(); // Toggle icon after clearing the text
            }
        });
    }
    private void toggleMicOrClearIcon() {
        if (editTextSearch.getText().toString().isEmpty()) {
            micIcon.setImageResource(R.drawable.ic_mic);
            isSpeechInputDone = false;
        } else {
            micIcon.setImageResource(R.drawable.ic_clear);
            isSpeechInputDone = true;
        }
    }

    private void startSpeechRecognition() {
        String languageCode = switchLabel.isChecked() ? "hi-IN" : "en-US";

        Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode);
        speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to search...");

        try {
            startActivityForResult(speechIntent, SPEECH_REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Speech recognition not supported on this device", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> speechResult = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (speechResult != null && !speechResult.isEmpty()) {
                String spokenText = speechResult.get(0);
                editTextSearch.setText(spokenText);
                isSpeechInputDone = true;
                toggleMicOrClearIcon();
            }
        }
    }

    private void updateRecyclerView(List<NameModel> updatedWords, String searchText) {
        List<NameModel> filteredWords = new ArrayList<>();
        for (NameModel word : updatedWords) {
            if (word.getWords().toLowerCase().startsWith(searchText.toLowerCase())) {
                filteredWords.add(word);
            }
        }
        if (wordsAdapter != null) {
            wordsAdapter.updateWords(filteredWords);
        } else {
            wordsAdapter = new WordsAdapter(filteredWords, this::onWordClick);
            recyclerView.setAdapter(wordsAdapter);
        }
    }

    public void onWordClick(NameModel word) {
        NameModel details;
        if (switchLabel.isChecked()) {
            details = dbManager.getWordDetailsHindi(word.getWords());
        } else {
            details = dbManager.getWordDetailsEnglish(word.getWords());
        }
        NameModel meaningDetails = dbManager.getMeaningDetailsByPosition(details.getId());
        dbManager.addToHistory(word.getWords());
        Intent intent = new Intent(Dictionary.this, DetailActivity.class);
        intent.putExtra("word", word.getWords());
        intent.putExtra("english", details.getWords());
        intent.putExtra("Hindi", details.getMeaning());
        intent.putExtra("type", meaningDetails.getType());
        intent.putExtra("hindiMeaning", meaningDetails.getHindiMeaning());
        intent.putExtra("englishMeaning", meaningDetails.getEnglishMeaning());
        intent.putExtra("check","Dictionary");
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbManager.close();
    }
}
