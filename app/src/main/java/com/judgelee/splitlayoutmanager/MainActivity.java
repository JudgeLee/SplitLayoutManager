package com.judgelee.splitlayoutmanager;

import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private List<String> mTitles;
  private RecyclerView mRecyclerView;
  private TextView mTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mTitles = new ArrayList<>();
    for (int i = 0; i < 50; i++) {
      mTitles.add("TextView" + i);
    }
    mRecyclerView = findViewById(R.id.recycler_view);
    mRecyclerView.setAdapter(new SimpleAdapter(mTitles));
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    mTextView = findViewById(R.id.text);
    mTextView.setText("LinearLayoutManager Vertical");
  }

  public void onClickLinearLayoutVertical(View view) {
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    mTextView.setText("LinearLayoutManager Vertical");
  }

  public void onClickLinearLayoutHorizontal(View view) {
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    mTextView.setText("LinearLayoutManager Horizontal");
  }

  public void onClickGridLayoutHorizontal(View view){
    mRecyclerView.setLayoutManager(new GridLayoutManager(this, 6));
    mTextView.setText("GridLayoutManager Horizontal");
  }

  public void onClickGridLayoutVertical(View view) {
    mRecyclerView.setLayoutManager(new GridLayoutManager(this, 6, LinearLayoutManager.HORIZONTAL, false));
    mTextView.setText("GridLayoutManager Vertical");
  }

  public void onClickSplitLayoutVertical(View view) {
    mRecyclerView.setLayoutManager(new SplitLayoutManager(SplitLayoutManager.VERTICAL, 6));
    mTextView.setText("SplitLayoutManager Vertical\n 纵向6个Item均分");
  }

  public void onClickSplitLayoutHorizontal(View view) {
    mRecyclerView.setLayoutManager(new SplitLayoutManager(SplitLayoutManager.HORIZONTAL, 6));
    mTextView.setText("SplitLayoutManager Horizontal\n 横向6个Item均分");
  }
}
