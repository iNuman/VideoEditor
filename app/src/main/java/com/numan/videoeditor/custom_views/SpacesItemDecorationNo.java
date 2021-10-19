package com.numan.videoeditor.custom_views;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;


public class SpacesItemDecorationNo extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecorationNo(int space) {
        this.space = space;
    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = this.space;
        outRect.right = this.space;
        outRect.bottom = this.space;
        outRect.top = this.space;
    }
}
