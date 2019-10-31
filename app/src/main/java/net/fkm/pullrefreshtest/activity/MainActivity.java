package net.fkm.pullrefreshtest.activity;

import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.fkm.pullrefreshtest.R;
import net.fkm.pullrefreshtest.adapter.MovieAdapter;
import net.fkm.pullrefreshtest.entity.MovieBaseModel;
import net.fkm.pullrefreshtest.entity.MovieDataModel;
import net.fkm.pullrefreshtest.utils.HttpConstants;
import net.fkm.pullrefreshtest.utils.L;
import net.fkm.pullrefreshtest.utils.OkHttpUtils;
import net.fkm.pullrefreshtest.utils.ToastUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends BaseActivity {

    @BindView(R.id.loading_view)
    ImageView mLoadingView;
    @BindView(R.id.homeMovieList)
    RecyclerView homeMovieList;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.loading_view_ll)
    LinearLayout loading_view_ll;
    @BindView(R.id.error_view)
    RelativeLayout error_view;

    private boolean refreshType;
    private int page;
    private int oldListSize;
    private int newListSize;
    private int addListSize;
    private MovieAdapter adapter;
    private List<MovieDataModel> mList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

        ButterKnife.bind(this);
        AnimationDrawable anim = (AnimationDrawable) mLoadingView.getDrawable();
        anim.start();

    }

    @Override
    protected void initData() {

        // 开启自动加载功能（非必须）
        refreshLayout.setEnableAutoLoadMore(true);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshType = true;
                        page = 1;
                        movieListRequest(page);
                        refreshLayout.finishRefresh();
                        refreshLayout.resetNoMoreData();//setNoMoreData(false);
                    }
                }, 2000);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshType = false;
                        if (page > 2) {
                            ToastUtil.showToast("暂无更多的数据啦");
                            // 将不会再次触发加载更多事件
                            refreshLayout.finishLoadMoreWithNoMoreData();
                            return;
                        }
                        movieListRequest(page);
                        refreshLayout.setEnableLoadMore(false);

                        refreshLayout.finishLoadMore();
                    }
                }, 2000);
            }
        });
        //触发自动刷新
        refreshLayout.autoRefresh();

    }

    private void movieListRequest(int page) {

        String url = String.format(HttpConstants.MOVIE_URL, page);
        OkHttpUtils.excute(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                L.i("onFailure:" + e.getMessage());
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        error_view.setVisibility(View.VISIBLE);
                        homeMovieList.setVisibility(View.GONE);
                        loading_view_ll.setVisibility(View.GONE);
                        ToastUtil.showToast("服务器异常，数据请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                L.i("onResponse:" + res);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        parsingMovieListJson(res);
                    }
                });
            }
        });

    }

    private void parsingMovieListJson(String t) {

        if (refreshType) {
            mList.clear();
            oldListSize = 0;
        } else {
            oldListSize = mList.size();
        }
        Gson gson = new Gson();
        MovieBaseModel movieBaseModel = gson.fromJson(t, MovieBaseModel.class);
        List<MovieDataModel> movieDataModelList = movieBaseModel.getData();
        for (MovieDataModel movieDataModel : movieDataModelList) {
            MovieDataModel data = new MovieDataModel();
            data.setMovieName(movieDataModel.getMovieName());
            data.setPoster(movieDataModel.getPoster());
            data.setTag(movieDataModel.getTag());
            data.setUpdateStatus(movieDataModel.getUpdateStatus());
            mList.add(data);
        }

        newListSize = mList.size();
        addListSize = newListSize - oldListSize;

        if (refreshType) {
            homeMovieList.setLayoutManager(new GridLayoutManager(this, 3));
            adapter = new MovieAdapter(this, mList);
            homeMovieList.setAdapter(adapter);
        } else {
            adapter.notifyItemRangeInserted(mList.size() - addListSize, mList.size());
            adapter.notifyItemRangeChanged(mList.size() - addListSize, mList.size());
            refreshLayout.setEnableLoadMore(true);
        }

        page++;
        homeMovieList.setVisibility(View.VISIBLE);
        error_view.setVisibility(View.GONE);
        loading_view_ll.setVisibility(View.GONE);

        adapter.setItemClikListener(new MovieAdapter.OnItemClikListener() {
            @Override
            public void onItemClik(View view, int position) {
                String movieName = mList.get(position).getMovieName();
                ToastUtil.showToast(movieName);
            }

            @Override
            public void onItemLongClik(View view, int position) {

            }
        });


    }

}
