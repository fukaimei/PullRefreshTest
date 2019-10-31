package net.fkm.pullrefreshtest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


import net.fkm.pullrefreshtest.R;
import net.fkm.pullrefreshtest.entity.MovieDataModel;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private Context mContext;
    private List<MovieDataModel> mList;
    private MovieDataModel data;
    private OnItemClikListener mOnItemClikListener;

    public MovieAdapter(Context context, List<MovieDataModel> mList) {
        this.mContext = context;
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_movie_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        data = mList.get(position);
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.img_default_movie_v)
                .error(R.drawable.img_default_movie_v);
        Glide.with(mContext).load(data.getPoster()).apply(options).into(holder.iv_top_photo);
        holder.tv_name.setText(data.getMovieName());
        holder.tv_update_status.setText(data.getUpdateStatus());
        holder.tv_tag.setText(data.getTag());

        if (mOnItemClikListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClikListener.onItemClik(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClikListener.onItemLongClik(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_top_photo;
        private TextView tv_update_status;
        private TextView tv_name;
        private TextView tv_tag;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_top_photo = (ImageView) itemView.findViewById(R.id.iv_top_photo);
            tv_update_status = (TextView) itemView.findViewById(R.id.tv_update_status);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_tag = (TextView) itemView.findViewById(R.id.tv_tag);
        }
    }

    public interface OnItemClikListener {
        void onItemClik(View view, int position);

        void onItemLongClik(View view, int position);
    }

    public void setItemClikListener(OnItemClikListener mOnItemClikListener) {
        this.mOnItemClikListener = mOnItemClikListener;
    }

}
