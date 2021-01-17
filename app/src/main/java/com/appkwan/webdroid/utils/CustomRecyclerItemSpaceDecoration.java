package com.appkwan.webdroid.utils;

import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Objects;

public class CustomRecyclerItemSpaceDecoration extends RecyclerView.ItemDecoration {
    private static final int VERTICAL = 0;
    private static final int HORIZONTAL = 1;
    private static final int GRID = 2;

    private int mVerticalSpace = 0;
    private int mHorizontalSpace = 0;
    private int mVerticalStartSpace = 0;
    private int mVerticalEndSpace = 0;
    private int mHorizontalStartSpace = 0;
    private int mHorizontalEndSpace = 0;


    public CustomRecyclerItemSpaceDecoration() {
    }

    public CustomRecyclerItemSpaceDecoration(int mSpaceTop, int mSpaceBottom, int mSpaceStart, int mSpaceEnd, int mItemsVerticalSpace, int mItemsHorizontalSpace) {
        this.mVerticalSpace = mItemsVerticalSpace;
        this.mHorizontalSpace = mItemsHorizontalSpace;
        this.mVerticalStartSpace = mSpaceTop;
        this.mVerticalEndSpace = mSpaceBottom;
        this.mHorizontalStartSpace = mSpaceStart;
        this.mHorizontalEndSpace = mSpaceEnd;
    }

    public void setVerticalSpace(int mVerticalSpace) {
        this.mVerticalSpace = mVerticalSpace;
    }

    public void setHorizontalSpace(int mHorizontalSpace) {
        this.mHorizontalSpace = mHorizontalSpace;
    }

    public void setVerticalStartSpace(int mVerticalStartSpace) {
        this.mVerticalStartSpace = mVerticalStartSpace;
    }

    public void setVerticalEndSpace(int mVerticalEndSpace) {
        this.mVerticalEndSpace = mVerticalEndSpace;
    }

    public void setHorizontalStartSpace(int mHorizontalStartSpace) {
        this.mHorizontalStartSpace = mHorizontalStartSpace;
    }

    public void setHorizontalEndSpace(int mHorizontalEndSpace) {
        this.mHorizontalEndSpace = mHorizontalEndSpace;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {
        int position = parent.getChildViewHolder(view).getAdapterPosition();
        int itemCount = state.getItemCount();

        switch (resolveDisplayMode(parent.getLayoutManager())) {
            case VERTICAL:
                outRect.left = mHorizontalStartSpace;
                outRect.right = mHorizontalEndSpace;
                outRect.bottom = mVerticalSpace;
                if (position == 0) {
                    outRect.top = mVerticalStartSpace;
                }
                if (position == itemCount - 1) {
                    outRect.bottom = mVerticalEndSpace;
                }
                break;
            case HORIZONTAL:
                outRect.top = mVerticalStartSpace;
                outRect.bottom = mVerticalEndSpace;
                outRect.right = mHorizontalSpace;
                if (position == 0) {
                    outRect.left = mHorizontalStartSpace;
                }
                if (position == itemCount - 1) {
                    outRect.right = mHorizontalEndSpace;
                }
                break;
            case GRID:
                int spanCount = ((GridLayoutManager) Objects.requireNonNull(parent.getLayoutManager())).getSpanCount();
                outRect.bottom = mVerticalSpace;
                outRect.right = mHorizontalSpace;
                if (position < spanCount) {
                    outRect.top = mVerticalStartSpace;
                }
                if (position % spanCount == 0) {
                    outRect.left = mHorizontalStartSpace;
                }

                if (position % spanCount == spanCount - 1) {
                    outRect.right = mHorizontalEndSpace;
                }
                if (position >= itemCount - spanCount) {
                    outRect.bottom = mVerticalEndSpace;
                }
                break;
        }
    }

    private int resolveDisplayMode(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) return GRID;
        if (layoutManager.canScrollHorizontally()) return HORIZONTAL;
        return VERTICAL;
    }

}
