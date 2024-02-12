package com.sn.aichat.englishtohindi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.WordViewHolder> {

    private List<NameModel> wordsList;
    private OnItemClickListener listener;

    public WordsAdapter(List<NameModel> wordsList, OnItemClickListener listener) {
        this.wordsList = wordsList;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(NameModel word);
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_word, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        NameModel word = wordsList.get(position);
        holder.textViewWord.setText(word.getWords());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(word);
            }
        });
    }

    @Override
    public int getItemCount() {
        return wordsList.size();
    }

    public void updateWords(List<NameModel> updatedList) {
        wordsList.clear();
        wordsList.addAll(updatedList);
        notifyDataSetChanged();
    }

    static class WordViewHolder extends RecyclerView.ViewHolder {
        TextView textViewWord;

        WordViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewWord = itemView.findViewById(R.id.textViewWord);
        }
    }
}
