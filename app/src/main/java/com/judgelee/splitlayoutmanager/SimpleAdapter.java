package com.judgelee.splitlayoutmanager;

import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

/**
 * Author: judgelee
 * Date: 2019/3/20
 * Desc:
 */
public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder> {

  private List<String> mTitles;

  public SimpleAdapter(List<String> titles) {
    mTitles = titles;
  }

  @NonNull
  @Override
  public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup group, int i) {
    View itemView = LayoutInflater.from(group.getContext()).inflate(R.layout.item_simple, group, false);
    return new SimpleViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull SimpleViewHolder holder, int i) {
    holder.titleTv.setText(mTitles.get(i));
    Random random = new Random();
    holder.titleTv.setBackgroundColor(Color.rgb(random.nextInt(256),
        random.nextInt(256), random.nextInt(256)));
  }

  @Override
  public int getItemCount() {
    return mTitles.size();
  }

  public static class SimpleViewHolder extends RecyclerView.ViewHolder {

    TextView titleTv;

    public SimpleViewHolder(@NonNull View itemView) {
      super(itemView);
      titleTv = itemView.findViewById(R.id.tv_tile);
    }
  }
}
