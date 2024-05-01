package com.example.universe.ui.book;

import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookCustomItemDecoration extends RecyclerView.ItemDecoration {
    private final int firstItemMarginTop; // Margin for the first item
    private final int otherItemsMarginTop; // Margin for other items

    public BookCustomItemDecoration(int firstItemMarginTop, int otherItemsMarginTop) {
        this.firstItemMarginTop = firstItemMarginTop;
        this.otherItemsMarginTop = otherItemsMarginTop;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);

        if (position == 0) { // First item in the RecyclerView
            outRect.top = firstItemMarginTop; // Apply unique margin to the first item
        } else {
            outRect.top = otherItemsMarginTop; // Apply different margin to other items
        }
    }
}
