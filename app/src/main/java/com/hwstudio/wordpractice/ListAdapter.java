package com.hwstudio.wordpractice;

import android.graphics.Color;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private final String[] localDataSet;
    private final int textSize;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private int originalColor;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = view.findViewById(R.id.itemTextView);
            originalColor = textView.getCurrentTextColor();
        }

        public TextView getTextView() {
            return textView;
        }

        public String getText() {
            return textView.getText().toString();
        }

        public void highlightString() {
//            originalColor = textView.getCurrentTextColor();
            textView.setTextColor(Color.RED);
        }

        public void clearHighlight() {
            textView.setTextColor(originalColor);
        }
    }

    public ListAdapter(String[] dataSet, int getTextSize) {
        localDataSet = dataSet;
        textSize = getTextSize;
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);
        return new ListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.getTextView().setTextSize(textSize);
        viewHolder.getTextView().setText(localDataSet[position]);
    }

    @Override
    public int getItemCount() {
        return localDataSet.length;
    }
}
