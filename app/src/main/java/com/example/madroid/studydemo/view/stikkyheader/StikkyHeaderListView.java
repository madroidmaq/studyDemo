package com.example.madroid.studydemo.view.stikkyheader;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Space;

public class StikkyHeaderListView extends StikkyHeader {

    private final ListView mListView;
    private AbsListView.OnScrollListener mDelegateOnScrollListener;

    StikkyHeaderListView(final Context context, final ListView listView, final View header, final int mMinHeightHeader, final HeaderAnimator headerAnimator) {

        this.mContext = context;
        this.mListView = listView;
        this.mHeader = header;
        this.mMinHeightHeader = mMinHeightHeader;
        this.mHeaderAnimator = headerAnimator;

        init();
    }

    private void init() {
        createFakeHeader();
        measureHeaderHeight();
        setupAnimator();
        setStickyOnScrollListener();
    }


    protected void createFakeHeader() {

        mFakeHeader = new Space(mContext);

        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        mFakeHeader.setLayoutParams(lp);

        mListView.addHeaderView(mFakeHeader);
    }

    private void setStickyOnScrollListener() {

        StickyOnScrollListener mStickyOnScrollListener = new StickyOnScrollListener();
        mListView.setOnScrollListener(mStickyOnScrollListener);

    }

    private class StickyOnScrollListener implements AbsListView.OnScrollListener {

        private int mScrolledYList = 0;
        private int mScrolledState ;

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (mDelegateOnScrollListener != null) {
                mDelegateOnScrollListener.onScrollStateChanged(view, scrollState);
                mScrolledState = scrollState   ;
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            mScrolledYList = -calculateScrollYList();

            //notify the animator
            mHeaderAnimator.onScroll(mScrolledYList ,mScrolledState);

            if (mDelegateOnScrollListener != null) {
                mDelegateOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        }

        private int calculateScrollYList() {
            View c = mListView.getChildAt(0);
            if (c == null) {
                return 0;
            }

            //TODO support more than 1 header?

            int firstVisiblePosition = mListView.getFirstVisiblePosition();
            int top = c.getTop();

            int headerHeight = 0;
            if (firstVisiblePosition >= 1) { //TODO >= number of header
                headerHeight = mHeightHeader;
            }

            return -top + firstVisiblePosition * c.getHeight() + headerHeight;
        }

    }

    public void setOnScrollListener(final AbsListView.OnScrollListener onScrollListener) {
        mDelegateOnScrollListener = onScrollListener;
    }


}
