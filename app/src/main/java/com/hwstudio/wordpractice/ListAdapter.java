package com.hwstudio.wordpractice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private String[] localDataSet;
    private int textSize;
    private boolean hasBackground;
    private int[] color = new int[5];

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = view.findViewById(R.id.itemTextView);
        }

        public TextView getTextView() {
            return textView;
        }
        
        public String getText(){
            textView.setTextColor(Color.RED);
            return textView.getText().toString();
        }
    }
    
    public ListAdapter(String[] dataSet, int getTextSize, boolean getHasBackground) {
        localDataSet = dataSet;
        textSize = getTextSize;
        hasBackground = getHasBackground;
          color[0] = R.color.gray0;
                    color[1] = R.color.gray1;
                    color[2] = R.color.gray2;
                    color[3] = R.color.gray3;
                    color[4] = R.color.gray4;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);
                  
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextView().setTextSize(textSize);
        viewHolder.getTextView().setText(localDataSet[position]);
        if (hasBackground){
            viewHolder.getTextView().setBackgroundResource(color[position%5]);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.length;
    }
}

