package com.example.vid_me_app;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.List;



public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.VideoViewHolder> {
    List<Video> call;
    OnItemClickListener mItemClickListener;
    RecyclerViewAdapter(List<Video> call){
        this.call = call;

    }
    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_row,parent,false);
        VideoViewHolder videoViewHolder = new VideoViewHolder(v);
        return videoViewHolder;
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {

        Uri image_uri = Uri.parse(call.get(position).getThumbnail_url());
        Context context = holder.thumbview.getContext();
        Picasso.with(context).load(image_uri).into(holder.thumbview);
        holder.video_name.setText(call.get(position).getTitle());
        holder.video_like.setText("Likes :"+String.valueOf(call.get(position).getScore()));

    }

    @Override
    public int getItemCount() {
        return call.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;

        TextView video_name;
        TextView video_like;

        ImageView thumbview;
        public VideoViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
           thumbview = (ImageView)itemView.findViewById(R.id.thumb_view);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            video_name = (TextView) itemView.findViewById(R.id.Videoname_textView);
            video_like = (TextView) itemView.findViewById(R.id.like_textview);
        }

        @Override
        public void onClick(View v) {
mItemClickListener.onItemClick(v,getAdapterPosition());

        }
    }
    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;


    }
}
