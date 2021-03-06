package cn.ittiger.demo;

import cn.ittiger.demo.ui.CommonRecyclerView;
import cn.ittiger.demo.util.UIUtil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: laohu on 2016/7/25
 * @site: http://ittiger.cn
 */
public class RecyclerViewActivity extends ListActivity implements
        CommonRecyclerView.OnItemLongClickListener, CommonRecyclerView.LoadMoreListener {

    private View mLoadMoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mRecyclerView.setOnItemLongClickListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
        mLoadMoreView = getLoadMoreView();
    }

    @Override
    public List<String> getData() {

        List<String> list = new ArrayList<>(10);
        list.add("RecyclerView add HeaderView");
        list.add("RecyclerView add FooterView");
        list.add("RecyclerView Item Click");
        list.add("RecyclerView Item Long Click");
        list.add("Pull load more");
        return list;
    }

    private View getView(String text) {

        TextView textView = (TextView) LayoutInflater.from(this).inflate(R.layout.demo_header_view, mRecyclerView, false);
        textView.setText(text);
        return textView;
    }

    private View getLoadMoreView() {

        View view = LayoutInflater.from(this).inflate(R.layout.loadmore_layout, mRecyclerView, false);
        return view;
    }

    @Override
    public void onItemClick(int position, View itemView) {

        switch (position) {
            case 0://RecyclerView Load More
                if(!mAdapter.isHeaderViewEnable()) {
                    mAdapter.enableHeaderView();
                }
                mAdapter.addHeaderView(getView("HeaderView:" + (mAdapter.getHeaderViewCount() + 1)));
                break;
            case 1://RecyclerView Load More
                if(!mAdapter.isFooterViewEnable()) {
                    mAdapter.enableFooterView();
                }
                mAdapter.addFooterView(getView("FooterView:" + (mAdapter.getFooterViewCount() + 1)));
                break;
            case 2://RecyclerView Item Click
                UIUtil.showToast(this, "这是Item Click");
                break;
            case 4://Pull load more
                if(!mAdapter.isFooterViewEnable()) {
                    mAdapter.enableFooterView();
                }
                mAdapter.addFooterView(mLoadMoreView);
                mRecyclerView.setEnableAutoLoadMore(true);
                if(mRecyclerView.getLastVisiblePosition() <= mAdapter.getItemCount()) {
                    mLoadMoreView.setVisibility(View.GONE);
                }
                UIUtil.showToast(this, "上拉加载更多");
                break;
        }
    }

    @Override
    public void onItemLongClick(int position, View itemView) {

        switch (position) {
            case 3://RecyclerView Item Long Click
                UIUtil.showToast(this, "这是Item Long Click");
                break;
        }
    }

    int count = 0;
    @Override
    public void onLoadMore() {

        if(mLoadMoreView.getVisibility() == View.GONE) {
            mLoadMoreView.setVisibility(View.VISIBLE);
        }
        new Thread(){
            @Override
            public void run() {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final List<String> list = new ArrayList<>();
                for(int i = 0; i < 5; i++) {
                    list.add("Load More Data:" + (count + i + 1));
                }
                count += 5;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addAll(list);
                        if (mRecyclerView.getLastVisiblePosition() <= mAdapter.getItemCount()) {
                            mLoadMoreView.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }.start();
    }
}
