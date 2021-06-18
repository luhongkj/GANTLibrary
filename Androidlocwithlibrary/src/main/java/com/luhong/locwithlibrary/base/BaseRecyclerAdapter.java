package com.luhong.locwithlibrary.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.luhong.locwithlibrary.apadter.RecyclerViewHolder;
import com.luhong.locwithlibrary.listener.OnLoadMoreListener;
import com.luhong.locwithlibrary.listener.OnRecyclerClickListener;
import com.luhong.locwithlibrary.listener.SingleClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ITMG on 2018/2/24.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_COMMON_VIEW = 100001;
    public static final int TYPE_HEADER_VIEW = 100002;
    public static final int TYPE_FOOTER_VIEW = 100003;
    public static final int TYPE_EMPTY_VIEW = 100004;
    public static final int TYPE_DEFAULT_VIEW = 100005;
    private OnLoadMoreListener mLoadMoreListener;
    private OnRecyclerClickListener<T> mItemClickListener;

    protected Context mContext;
    protected List<T> mDataList;
    private boolean mOpenLoadMore;
    private boolean isAutoLoadMore = true;
    private boolean isAddHeader = false;

    private View mLoadingView;
    private View mLoadFailedView;
    private View mLoadEndView;
    private View mEmptyView;

    private RelativeLayout mFooterLayout;

    protected abstract int initLayoutId();

    protected abstract void convert(RecyclerViewHolder holder, T data, int position);

    public BaseRecyclerAdapter(Context context, List<T> dataList, boolean isOpenLoadMore) {
        mContext = context;
        mDataList = dataList == null ? new ArrayList<T>() : dataList;
        mOpenLoadMore = isOpenLoadMore;
    }

    public BaseRecyclerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerViewHolder viewHolder = null;
        switch (viewType) {
            case TYPE_COMMON_VIEW:
                viewHolder = RecyclerViewHolder.create(mContext, initLayoutId(), parent);
                break;
            case TYPE_FOOTER_VIEW:
                if (mFooterLayout == null) {
                    mFooterLayout = new RelativeLayout(mContext);
                }
                viewHolder = RecyclerViewHolder.create(mFooterLayout);
                break;
            case TYPE_EMPTY_VIEW:
                viewHolder = RecyclerViewHolder.create(mEmptyView);
                break;
            case TYPE_DEFAULT_VIEW:
                viewHolder = RecyclerViewHolder.create(new View(mContext));
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_COMMON_VIEW:
                bindCommonItem(holder, position);
                break;
        }
    }

    private void bindCommonItem(RecyclerView.ViewHolder holder, final int position) {
        final RecyclerViewHolder viewHolder = (RecyclerViewHolder) holder;
        convert(viewHolder, mDataList.get(position), position);

        viewHolder.getConvertView().setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (mItemClickListener != null)
                    mItemClickListener.onItemClick(viewHolder, mDataList.get(position), position);
            }
        });
        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mItemClickListener != null)
                    mItemClickListener.onItemLongClick(viewHolder, mDataList.get(position), position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size() + getHeaderViewCount() + getFooterViewCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (mDataList.isEmpty() && mEmptyView != null) {
            return TYPE_EMPTY_VIEW;
        }
        if (isFooterView(position)) {
            return TYPE_FOOTER_VIEW;
        }
        if (mDataList.isEmpty()) {
            return TYPE_DEFAULT_VIEW;
        }
        return TYPE_COMMON_VIEW;
    }

    private boolean isFooterView(int position) {
        return mOpenLoadMore && getItemCount() > 1 && position >= getItemCount() - 1;
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (isFooterView(holder.getLayoutPosition())) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) layoutManager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isFooterView(position)) {
                        return gridManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }

        startLoadMore(recyclerView, layoutManager);
    }


    private void startLoadMore(RecyclerView recyclerView, final RecyclerView.LayoutManager layoutManager) {
        if (!mOpenLoadMore || mLoadMoreListener == null) {
            return;
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!isAutoLoadMore && findLastVisibleItemPosition(layoutManager) + 1 == getItemCount()) {
                        scrollLoadMore();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isAutoLoadMore && findLastVisibleItemPosition(layoutManager) + 1 == getItemCount()) {
                    scrollLoadMore();
                } else if (isAutoLoadMore) {
                    isAutoLoadMore = false;
                }
            }
        });
    }

    private void scrollLoadMore() {
        if (mFooterLayout.getChildAt(0) == mLoadingView) {
            mLoadMoreListener.onLoadMore(false);
        }
    }

    private int findLastVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null);
            return findMax(lastVisibleItemPositions);
        }
        return -1;
    }

    private int findMax(int[] lastVisiblePositions) {
        int max = lastVisiblePositions[0];
        for (int value : lastVisiblePositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private void removeFooterView() {
        mFooterLayout.removeAllViews();
    }

    private void addFooterView(View footerView) {
        if (footerView == null) {
            return;
        }

        if (mFooterLayout == null) {
            mFooterLayout = new RelativeLayout(mContext);
        }
        removeFooterView();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mFooterLayout.addView(footerView, params);
    }

    public T getItem(int position) {
        return mDataList.get(position);
    }

    public void setLoadMoreData(List<T> datas) {
        int size = mDataList.size();
        mDataList.addAll(datas);
        notifyItemInserted(size);
    }

    public void setData(List<T> datas) {
        mDataList.addAll(0, datas);
        notifyDataSetChanged();
    }

    public void setNewData(List<T> datas) {
        mDataList.clear();
        mDataList.addAll(datas);
        notifyDataSetChanged();
    }

    public int getDatasSize() {
        return mDataList.size();
    }

    public void removeData(int position) {
        mDataList.remove(position);
        notifyDataSetChanged();
    }

    public void clearData() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    public List<T> getDataList() {
        return mDataList;
    }

    /**
     * 初始化加载中布局
     *
     * @param loadingView
     */
    public void setLoadingView(View loadingView) {
        mLoadingView = loadingView;
        addFooterView(mLoadingView);
    }

    public void setLoadingView(int loadingId) {
        setLoadingView(inflate(loadingId));
    }

    /**
     * 初始加载失败布局
     *
     * @param loadFailedView
     */
    public void setLoadFailedView(View loadFailedView) {
        if (loadFailedView == null) {
            return;
        }
        mLoadFailedView = loadFailedView;
        addFooterView(mLoadFailedView);
        mLoadFailedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFooterView(mLoadingView);
                mLoadMoreListener.onLoadMore(true);
            }
        });
    }

    public void setLoadFailedView(int loadFailedId) {
        setLoadFailedView(inflate(loadFailedId));
    }

    /**
     * 初始化全部加载完成布局
     *
     * @param loadEndView
     */
    public void setLoadEndView(View loadEndView) {
        mLoadEndView = loadEndView;
        addFooterView(mLoadEndView);
    }

    public void setLoadEndView(int loadEndId) {
        setLoadEndView(inflate(loadEndId));
    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    public int getFooterViewCount() {
        return mOpenLoadMore ? 1 : 0;
    }

    public int getHeaderViewCount() {
        return isAddHeader ? 1 : 0;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        mLoadMoreListener = loadMoreListener;
    }

    public void setOnClickListener(OnRecyclerClickListener<T> itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    private View inflate(int layoutId) {
        if (layoutId <= 0) {
            return null;
        }
        return LayoutInflater.from(mContext).inflate(layoutId, null, false);
    }

    public void removeAllViews() {
        if (mFooterLayout != null) mFooterLayout.removeAllViews();
    }

    public interface IEventListener<T> {
        void onCheck(T data, int position);
    }

}
