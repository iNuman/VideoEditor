package com.numan.videoeditor.custom_views;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;


public class EmptyRecyclerView extends RecyclerView {
    private RecyclerView.AdapterDataObserver dataObserver;
    private View emptyView;

    public EmptyRecyclerView(Context context) {
        this(context, null);
    }

    public EmptyRecyclerView(Context context, AttributeSet attr) {
        this(context, attr, 0);
    }

    public EmptyRecyclerView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        this.dataObserver = new AdapterDataObserver() {
            @SuppressLint("WrongConstant")
            public void onChanged() {
                Adapter<?> adapter = EmptyRecyclerView.this.getAdapter();
                if (adapter != null && EmptyRecyclerView.this.emptyView != null) {
                    if (adapter.getItemCount() == 0) {
                        EmptyRecyclerView.this.emptyView.setVisibility(0);
                        EmptyRecyclerView.this.setVisibility(8);
                        return;
                    }
                    EmptyRecyclerView.this.emptyView.setVisibility(8);
                    EmptyRecyclerView.this.setVisibility(0);
                }
            }
        };
    }

    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(this.dataObserver);
        }
        this.dataObserver.onChanged();
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }
}
