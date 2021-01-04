package com.hwstudio.wordpractice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class BackgroundAdapter extends RecyclerView.Adapter<BackgroundAdapter.ViewHolder> {

    private final String[] localDataSet;
    private final int textSize;
    private final int[] color = new int[5];

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ConstraintLayout itemLayout;

        public ViewHolder(View view) {
            super(view);
            itemLayout = view.findViewById(R.id.itemLayout);
            textView = view.findViewById(R.id.itemTextView);
        }

        public TextView getTextView() {
            return textView;
        }

        public ConstraintLayout getLayout() {
            return itemLayout;
        }
    }

    public BackgroundAdapter(String[] dataSet, int getTextSize) {//}, boolean getHasBackground) {
        localDataSet = dataSet;
        textSize = getTextSize;
        color[0] = R.color.gray0;
        color[1] = R.color.gray1;
        color[2] = R.color.gray2;
        color[3] = R.color.gray3;
        color[4] = R.color.gray4;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getTextView().setTextSize(textSize);
        viewHolder.getLayout().setBackgroundResource(color[position % 5]);
    }

    @Override
    public int getItemCount() {
        return localDataSet.length;
    }
}

