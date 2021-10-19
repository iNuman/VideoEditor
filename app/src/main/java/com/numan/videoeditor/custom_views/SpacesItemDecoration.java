package com.numan.videoeditor.custom_views;

import android.graphics.Rect;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;


public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, ExpandIconView.State state) {
        outRect.left = this.space;
        outRect.right = this.space;
        outRect.bottom = this.space;
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        int span = 1;
        if (manager instanceof GridLayoutManager) {
            span = ((GridLayoutManager) manager).getSpanCount();
        }
        if (parent.getChildLayoutPosition(view) < span) {
            outRect.top = this.space * 2;
        } else {
            outRect.top = 0;
        }
    }
}
