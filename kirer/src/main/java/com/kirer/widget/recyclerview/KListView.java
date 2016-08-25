package com.kirer.widget.recyclerview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.kirer.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinwb on 2016/8/22.
 */
public class KListView extends FrameLayout implements SwipeRefreshLayout.OnRefreshListener {

    private boolean loadMoreEnabled = true;
    private boolean isLoading = false;

    public KListView(Context context) {
        super(context);
        init();
    }

    public KListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private SwipeRefreshLayout swipeView;
    private RecyclerView listView;
    private LinearLayout footerView;

    private void init() {
        initSwipeView();
        initListView();
        initLoadMoreFooterView();
        addView(swipeView);
    }

    private void initSwipeView() {
        swipeView = new SwipeRefreshLayout(getContext());
        swipeView.setOnRefreshListener(this);
    }

    private void initListView() {
        listView = new RecyclerView(getContext());
        listView.addOnScrollListener(new OnScrollListener());
        swipeView.addView(listView);
    }

    private void initLoadMoreFooterView() {
        footerView = new LinearLayout(getContext());
        footerView.setGravity(Gravity.CENTER);
        footerView.setPadding(10, 10, 10, 10);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.BOTTOM;
        footerView.setLayoutParams(lp);
        ProgressBar progressBar = new ProgressBar(getContext());
//        progressBar.setLayoutParams(new ViewGroup.LayoutParams(DisplayUtils.dp2px(36),DisplayUtils.dp2px(36)));
        footerView.addView(progressBar);
//        TextView tipTv = new TextView(getContext());
//        tipTv.setText("正在加载...");
//        footerView.addView(tipTv);
        footerView.setVisibility(GONE);
//        footerView.setBackgroundColor(Color.parseColor("#e7e7e7"));
        addFooterView(footerView);
    }

    private boolean shouldAdjustSpanSize = false;

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        listView.setLayoutManager(layoutManager);
        if (layoutManager instanceof GridLayoutManager || layoutManager instanceof StaggeredGridLayoutManager) {
            this.shouldAdjustSpanSize = true;
            if (adapter != null) {
                adapter.adjustSpanSize(listView);
            }
        }
    }

    private WrapAdapter adapter;
    private ArrayList<View> mTmpHeaderView = new ArrayList<>();
    private ArrayList<View> mTmpFooterView = new ArrayList<>();

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter instanceof WrapAdapter) {
            this.adapter = (WrapAdapter) adapter;
            listView.setAdapter(adapter);
        } else {
            this.adapter = new WrapAdapter(adapter);
            for (View view : mTmpHeaderView) {
                this.adapter.addHeaderView(view);
            }
            if (mTmpHeaderView.size() > 0) {
                mTmpHeaderView.clear();
            }

            for (View view : mTmpFooterView) {
                this.adapter.addFooterView(view);
            }
            if (mTmpFooterView.size() > 0) {
                mTmpFooterView.clear();
            }

            listView.setAdapter(this.adapter);
        }

        if (shouldAdjustSpanSize) {
            this.adapter.adjustSpanSize(listView);
        }

        adapter.registerAdapterDataObserver(mDataObserver);
        mDataObserver.onChanged();
    }

    public void addHeaderView(View view) {
        if (null == view) {
            throw new IllegalArgumentException("the view to add must not be null!");
        } else if (adapter == null) {
            mTmpHeaderView.add(view);
        } else {
            adapter.addHeaderView(view);
        }
    }

    public void addFooterView(View view) {
        if (null == view) {
            throw new IllegalArgumentException("the view to add must not be null!");
        } else if (adapter == null) {
            mTmpFooterView.add(view);
        } else {
            adapter.addFooterView(view, true);
        }
    }

    public void setLoadMoreEnabled(boolean enabled) {
        this.loadMoreEnabled = enabled;
    }

    public void setRefreshingEnabled(boolean enabled) {
        this.swipeView.setEnabled(enabled);
    }

    public void setRefreshing(boolean refreshing) {
        L.d("setRefreshing --> " + refreshing);
        this.swipeView.setRefreshing(refreshing);
    }

    public void setLoadingMore(boolean loadingMore) {
        L.d("setLoadingMore --> " + loadingMore);
        isLoading = loadingMore;
        this.footerView.setVisibility(loadingMore ? VISIBLE : GONE);
    }

    private LoadingListener listener;

    public void setLoadingListener(LoadingListener listener) {
        this.listener = listener;
    }

    public interface LoadingListener {

        void onRefresh();

        void onLoadMore();
    }

    @Override
    public void onRefresh() {
        if (listener != null) {
            listener.onRefresh();
        }
    }

    private class OnScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE && listener != null && !isLoading && loadMoreEnabled) {
                RecyclerView.LayoutManager layoutManager = listView.getLayoutManager();
                int lastVisibleItemPosition;
                if (layoutManager instanceof GridLayoutManager) {
                    lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                    ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                    lastVisibleItemPosition = findMax(into);
                } else {
                    lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                }
                if (layoutManager.getChildCount() > 0 && lastVisibleItemPosition >= layoutManager.getItemCount() - 1 && layoutManager.getItemCount() > layoutManager.getChildCount()) {
                    isLoading = true;
                    footerView.setVisibility(View.VISIBLE);
                    listener.onLoadMore();
                }
            }
        }

        private int findMax(int[] lastPositions) {
            int max = lastPositions[0];
            for (int value : lastPositions) {
                if (value > max) {
                    max = value;
                }
            }
            return max;
        }
    }

    private final RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {

        @Override
        public void onChanged() {
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            adapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            adapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            adapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            adapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            adapter.notifyItemMoved(fromPosition, toPosition);
        }
    };

    public class WrapAdapter<T extends RecyclerView.Adapter> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final T mRealAdapter;
        private boolean isStaggeredGrid;

        // Defines available view type integers for headers and footers.
        private static final int BASE_HEADER_VIEW_TYPE = -1 << 10;
        private static final int BASE_FOOTER_VIEW_TYPE = -1 << 11;

        private ArrayList<FixedViewInfo> mHeaderViewInfos = new ArrayList<>();
        private ArrayList<FixedViewInfo> mFooterViewInfos = new ArrayList<>();

        /**
         * A class that represents a fixed view in a list, for example a header at the top
         * or a footer at the bottom.
         */
        public class FixedViewInfo {
            /**
             * The view to add to the list
             */
            public View view;
            /**
             * The data backing the view. This is returned from {RecyclerView.Adapter#getItemViewType(int)}.
             */
            public int viewType;
        }

        /**
         * Constructor.
         *
         * @param adapter the adapter to wrap
         */
        public WrapAdapter(T adapter) {
            super();
            mRealAdapter = adapter;
        }

        /**
         * Gets the real adapter
         *
         * @return T:
         */
        public T getWrappedAdapter() {
            return mRealAdapter;
        }

        /**
         * Adds a header view
         *
         * @param view
         */
        public void addHeaderView(View view) {
            if (null == view) {
                throw new IllegalArgumentException("the view to add must not be null!");
            }
            final FixedViewInfo info = new FixedViewInfo();
            info.view = view;
            info.viewType = BASE_HEADER_VIEW_TYPE + mHeaderViewInfos.size();
            mHeaderViewInfos.add(info);
            notifyDataSetChanged();
        }

        /**
         * Adds a footer view
         *
         * @param view
         */
        public void addFooterView(View view, boolean reverse) {
            if (null == view) {
                throw new IllegalArgumentException("the view to add must not be null!");
            }
            final FixedViewInfo info = new FixedViewInfo();
            info.view = view;
            info.viewType = BASE_FOOTER_VIEW_TYPE + mFooterViewInfos.size();
            mFooterViewInfos.add(info);
            if (reverse) {
                for (int i = 0; i < mFooterViewInfos.size(); i++) {
                    FixedViewInfo fixedViewInfo = mFooterViewInfos.get(i);
                    fixedViewInfo.viewType = BASE_FOOTER_VIEW_TYPE + mFooterViewInfos.size() - i - 1;
                }
            }
            notifyDataSetChanged();
        }

        /**
         * Adds a footer view
         *
         * @param view
         */
        public void addFooterView(View view) {
            addFooterView(view, false);
        }

        /**
         * gets the headers view
         *
         * @return List:
         * @version 1.0
         */
        public List<View> getHeadersView() {
            List<View> viewList = new ArrayList<View>(getHeadersCount());
            for (FixedViewInfo fixedViewInfo : mHeaderViewInfos) {
                viewList.add(fixedViewInfo.view);
            }
            return viewList;
        }

        /**
         * gets the footers view
         *
         * @return List:
         * @version 1.0
         */
        public List<View> getFootersView() {
            List<View> viewList = new ArrayList<View>(getHeadersCount());
            for (FixedViewInfo fixedViewInfo : mFooterViewInfos) {
                viewList.add(fixedViewInfo.view);
            }
            return viewList;
        }

        /**
         * adjust the GridLayoutManager SpanSize
         *
         * @param recycler
         * @version 1.0
         */
        public void adjustSpanSize(RecyclerView recycler) {
            if (recycler.getLayoutManager() instanceof GridLayoutManager) {
                final GridLayoutManager layoutManager = (GridLayoutManager) recycler.getLayoutManager();
                layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

                    @Override
                    public int getSpanSize(int position) {
                        boolean isHeaderOrFooter =
                                isHeaderPosition(position) || isFooterPosition(position);
                        return isHeaderOrFooter ? layoutManager.getSpanCount() : 1;
                    }

                });
            }

            if (recycler.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                this.isStaggeredGrid = true;
            }
        }

        /**
         * Setting the visibility of the header views
         *
         * @param shouldShow
         * @version 1.0
         */
        public void setHeaderVisibility(boolean shouldShow) {
            for (FixedViewInfo fixedViewInfo : mHeaderViewInfos) {
                fixedViewInfo.view.setVisibility(shouldShow ? View.VISIBLE : View.GONE);
            }
            notifyDataSetChanged();
        }

        /**
         * Setting the visibility of the footer views
         *
         * @param shouldShow
         * @version 1.0
         */
        public void setFooterVisibility(boolean shouldShow) {
            for (FixedViewInfo fixedViewInfo : mFooterViewInfos) {
                fixedViewInfo.view.setVisibility(shouldShow ? View.VISIBLE : View.GONE);
            }
            notifyDataSetChanged();
        }

        /**
         * get the count of headers
         *
         * @return number of headers
         * @version 1.0
         */
        public int getHeadersCount() {
            return mHeaderViewInfos.size();
        }

        /**
         * get the count of footers
         *
         * @return the number of footers
         * @version 1.0
         */
        public int getFootersCount() {
            return mFooterViewInfos.size();
        }

        private boolean isHeader(int viewType) {
            return viewType >= BASE_HEADER_VIEW_TYPE
                    && viewType < (BASE_HEADER_VIEW_TYPE + mHeaderViewInfos.size());
        }

        private boolean isFooter(int viewType) {
            return viewType >= BASE_FOOTER_VIEW_TYPE
                    && viewType < (BASE_FOOTER_VIEW_TYPE + mFooterViewInfos.size());
        }

        private boolean isHeaderPosition(int position) {
            return position < mHeaderViewInfos.size();
        }

        private boolean isFooterPosition(int position) {
            return position >= mHeaderViewInfos.size() + mRealAdapter.getItemCount();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            if (isHeader(viewType)) {
                int whichHeader = Math.abs(viewType - BASE_HEADER_VIEW_TYPE);
                View headerView = mHeaderViewInfos.get(whichHeader).view;
                return createHeaderFooterViewHolder(headerView);

            } else if (isFooter(viewType)) {
                int whichFooter = Math.abs(viewType - BASE_FOOTER_VIEW_TYPE);
                View footerView = mFooterViewInfos.get(whichFooter).view;
                return createHeaderFooterViewHolder(footerView);

            } else {
                return mRealAdapter.onCreateViewHolder(viewGroup, viewType);
            }
        }

        private RecyclerView.ViewHolder createHeaderFooterViewHolder(View view) {
            if (isStaggeredGrid) {
                StaggeredGridLayoutManager.LayoutParams params = new StaggeredGridLayoutManager.LayoutParams(
                        StaggeredGridLayoutManager.LayoutParams.MATCH_PARENT, StaggeredGridLayoutManager.LayoutParams.WRAP_CONTENT);
                params.setFullSpan(true);
                view.setLayoutParams(params);
            }
            return new RecyclerView.ViewHolder(view) {
            };
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            if (position < mHeaderViewInfos.size()) {
                // Headers don't need anything special

            } else if (position < mHeaderViewInfos.size() + mRealAdapter.getItemCount()) {
                // This is a real position, not a header or footer. Bind it.
                mRealAdapter.onBindViewHolder(viewHolder, position - mHeaderViewInfos.size());

            } else {
                // Footers don't need anything special
            }
        }

        @Override
        public int getItemCount() {
            return mHeaderViewInfos.size() + mRealAdapter.getItemCount() + mFooterViewInfos.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (isHeaderPosition(position)) {
                return mHeaderViewInfos.get(position).viewType;

            } else if (isFooterPosition(position)) {
                return mFooterViewInfos.get(position - mHeaderViewInfos.size()
                        - mRealAdapter.getItemCount()).viewType;

            } else {
                return mRealAdapter.getItemViewType(position - mHeaderViewInfos.size());
            }
        }
    }
}
