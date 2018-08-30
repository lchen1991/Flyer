package com.ttdevs.flyer.utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * @author ttdevs
 */
public abstract class OnRecyclerLoadMoreListener extends RecyclerView.OnScrollListener{
        
    private LinearLayoutManager mLayoutManager;
    private int mItemCount, mLastCompletely, mLastLoad;
 
    /**
     * load more
     */
    public abstract void onLoadMore();
 
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
 
            mItemCount = mLayoutManager.getItemCount();
            mLastCompletely = mLayoutManager.findLastCompletelyVisibleItemPosition();
        } else {
            return;
        }
 
        if (mLastLoad != mItemCount && mItemCount == mLastCompletely + 1) {
//            LogUtils.debug(String.format("****************load more****************"));
//            LogUtils.debug(String.format("mItemCount:%d \t mLastCompletely:%d", mItemCount, mLastCompletely));
//            LogUtils.debug(String.format("****************load more****************"));
 
            mLastLoad = mItemCount;
            onLoadMore();
        }
    }
}