package com.hwstudio.wordpractice;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private final List<String> localDataSet;
    private final int textSize;
    private OnWordClickedListener onWordClickedListener;

    public interface OnWordClickedListener {
        void onWordClicked(int position);
    }

    public void setOnWordClickedListener(OnWordClickedListener listener) {
        onWordClickedListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final int originalColor;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.itemTextView);
            originalColor = textView.getCurrentTextColor();
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onWordClickedListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onWordClickedListener.onWordClicked(position);
                        }
                    }
                }
            });
        }

        public TextView getTextView() {
            return textView;
        }

/*        public String getText() {
            return textView.getText().toString();
        }*/

        public void highlightString() {
            textView.setTextColor(Color.RED);
        }

        public void clearHighlight() {
            textView.setTextColor(originalColor);
        }
    }

    public ListAdapter(List<String> dataSet, int getTextSize) {
        localDataSet = dataSet;
        textSize = getTextSize;
    }

    @Override
    @NonNull
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);
        return new ListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.getTextView().setTextSize(textSize);
        viewHolder.getTextView().setText(localDataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
