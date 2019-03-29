package com.judgelee.splitlayoutmanager;

import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author: judgelee
 * Date: 2019/3/20
 * Desc:
 */
public class SplitLayoutManager extends RecyclerView.LayoutManager {

  public static final int HORIZONTAL = OrientationHelper.HORIZONTAL;
  public static final int VERTICAL = OrientationHelper.VERTICAL;

  private int mOrientation = HORIZONTAL;
  private int mSpanCount = 1;

  private OrientationHelper mOrientationHelper;

  /**
   * RecyclerView 滚动的距离，向左为正值，向右为负值
   */
  private int mScrollingOffset;
  /**
   * 当前显示的item为[mFirstVisiblePos, mLastVisiblePos)
   */
  private int mFirstVisiblePos = 0;
  private int mLastVisiblePos = 0;

  public SplitLayoutManager(int orientation, int spanCount) {
    setOrientation(orientation);
    setSpan(spanCount);
  }

  private void setOrientation(int orientation) {
    if (mOrientation != orientation || mOrientationHelper == null) {
      mOrientationHelper = OrientationHelper.createOrientationHelper(this, orientation);
      mOrientation = orientation;
      requestLayout();
    }
  }

  private void setSpan(int spanCount) {
    if (mSpanCount == spanCount) {
      return;
    }
    mSpanCount = spanCount;
    requestLayout();
  }

  @Override
  public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
    if (getItemCount() == 0) {
      detachAndScrapAttachedViews(recycler);
      return;
    }
    if (getChildCount() == 0 && state.isPreLayout()) {
      return;
    }
    detachAndScrapAttachedViews(recycler);
    mFirstVisiblePos = 0;
    mLastVisiblePos = 0;
    mScrollingOffset = 0;

    fill(recycler, state);
  }

  /**
   * 填充RecyclerView
   *
   * @param recycler
   * @param state
   */
  private void fill(RecyclerView.Recycler recycler, RecyclerView.State state) {
    // 添加新出现的View并布局
    addViewToFillStart(recycler, state);
    addViewToFillEnd(recycler, state);
  }

  /**
   * 从mFirstVisiblePos向前依次添加View，直到RecyclerView顶部
   *
   * @param recycler
   * @param state
   */
  private void addViewToFillStart(RecyclerView.Recycler recycler, RecyclerView.State state) {
    final int firstVisibleIndex = mFirstVisiblePos;
    for (int i = firstVisibleIndex - 1; i >= 0; i--) {
      if (isItemVisibleInParent(i)) {
        View view = recycler.getViewForPosition(i);
        addView(view);
        mFirstVisiblePos--;
        if (mOrientation == VERTICAL) {
          measureChildWithSpan(view);
          int width = getDecoratedMeasuredWidth(view);
          int height = getDecoratedMeasuredHeight(view);
          int left = getPaddingLeft();
          int top = getSpanSpace() * i + (getSpanSpace() - height) / 2 - mScrollingOffset;
          layoutDecoratedWithMargins(view, left, top, left + width, top + height);
        } else {
          measureChildWithSpan(view);
          int width = getDecoratedMeasuredWidth(view);
          int height = getDecoratedMeasuredHeight(view);
          int left = getSpanSpace() * i + (getSpanSpace() - width) / 2 - mScrollingOffset;
          int top = getPaddingTop();
          layoutDecoratedWithMargins(view, left, top, left + width, top + height);
        }
      }
    }
  }

  /**
   * 从mLastVisiblePos向后依次添加View，直到RecyclerView底部
   *
   * @param recycler
   * @param state
   */
  private void addViewToFillEnd(RecyclerView.Recycler recycler, RecyclerView.State state) {
    final int lastVisibleIndex = mLastVisiblePos;
    for (int i = lastVisibleIndex; i < getItemCount(); i++) {
      if (isItemVisibleInParent(i)) {
        View view = recycler.getViewForPosition(i);
        addView(view);
        mLastVisiblePos++;
        if (mOrientation == VERTICAL) {
          measureChildWithSpan(view);
          int width = getDecoratedMeasuredWidth(view);
          int height = getDecoratedMeasuredHeight(view);
          int left = getPaddingLeft();
          int top = getSpanSpace() * i + (getSpanSpace() - height) / 2 - mScrollingOffset;
          layoutDecoratedWithMargins(view, left, top, left + width, top + height);
        } else {
          measureChildWithSpan(view);
          int width = getDecoratedMeasuredWidth(view);
          int height = getDecoratedMeasuredHeight(view);
          int left = getSpanSpace() * i + (getSpanSpace() - width) / 2 - mScrollingOffset;
          int top = getPaddingTop();
          layoutDecoratedWithMargins(view, left, top, left + width, top + height);
        }
      } else {
        break;
      }
    }
  }

  private boolean isItemVisibleInParent(int index) {
    final int startOffset = index * getSpanSpace();
    return startOffset >= mScrollingOffset - getSpanSpace()
        && startOffset < mScrollingOffset + mOrientationHelper.getTotalSpace();
  }

  private void measureChildWithSpan(View view) {
    final RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();

    final int verticalInsets =
        getTopDecorationHeight(view) + getBottomDecorationHeight(view) + lp.topMargin + lp.bottomMargin;
    final int horizontalInsets =
        getLeftDecorationWidth(view) + getRightDecorationWidth(view) + lp.leftMargin + lp.rightMargin;

    final int widthSpec;
    final int heightSpec;
    if (mOrientation == VERTICAL) {
      widthSpec = getChildMeasureSpec(getWidth(), getWidthMode(), horizontalInsets, lp.width,
          canScrollHorizontally());
      heightSpec = getChildMeasureSpec(getSpanSpace(), View.MeasureSpec.EXACTLY, verticalInsets, lp.height,
          canScrollVertically());
    } else {
      widthSpec = getChildMeasureSpec(getSpanSpace(), View.MeasureSpec.EXACTLY, horizontalInsets, lp.width,
          canScrollHorizontally());
      heightSpec = getChildMeasureSpec(getHeight(), getHeightMode(), verticalInsets, lp.height,
          canScrollVertically());
    }
    if (shouldMeasureChild(view, widthSpec, heightSpec, lp)) {
      view.measure(widthSpec, heightSpec);
    }
  }

  boolean shouldMeasureChild(View child, int widthSpec, int heightSpec, RecyclerView.LayoutParams lp) {
    return child.isLayoutRequested()
        || !isMeasurementCacheEnabled()
        || !isMeasurementUpToDate(child.getWidth(), widthSpec, lp.width)
        || !isMeasurementUpToDate(child.getHeight(), heightSpec, lp.height);
  }

  private static boolean isMeasurementUpToDate(int childSize, int spec, int dimension) {
    final int specMode = View.MeasureSpec.getMode(spec);
    final int specSize = View.MeasureSpec.getSize(spec);
    if (dimension > 0 && childSize != dimension) {
      return false;
    }
    switch (specMode) {
      case View.MeasureSpec.UNSPECIFIED:
        return true;
      case View.MeasureSpec.AT_MOST:
        return specSize >= childSize;
      case View.MeasureSpec.EXACTLY:
        return specSize == childSize;
    }
    return false;
  }

  int getSpanSpace() {
    return mOrientationHelper == null ? 0 : mOrientationHelper.getTotalSpace() / mSpanCount;
  }

  @Override
  public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
    if (mOrientation == HORIZONTAL) {
      return 0;
    }
    int realOffset = dy;
    int spanSpace = mOrientationHelper.getTotalSpace() / mSpanCount;
    int totalHeight = spanSpace * getItemCount();
    if (realOffset + mScrollingOffset > totalHeight - mOrientationHelper.getTotalSpace()) {
      realOffset = totalHeight - mOrientationHelper.getTotalSpace() - mScrollingOffset;
    } else if (realOffset + mScrollingOffset < 0) {
      realOffset = -mScrollingOffset;
    }
    mScrollingOffset += realOffset;
    recycleOutsideChild(recycler, dy);
    offsetChildrenVertical(-realOffset);
    fill(recycler, state);
    return realOffset;
  }

  @Override
  public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
    if (mOrientation == VERTICAL) {
      return 0;
    }
    int realOffset = dx;
    int spanSpace = mOrientationHelper.getTotalSpace() / mSpanCount;
    int totalWidth = spanSpace * getItemCount();
    if (realOffset + mScrollingOffset > totalWidth - mOrientationHelper.getTotalSpace()) {
      realOffset = totalWidth - mOrientationHelper.getTotalSpace() - mScrollingOffset;
    } else if (realOffset + mScrollingOffset < 0) {
      realOffset = -mScrollingOffset;
    }
    mScrollingOffset += realOffset;
    recycleOutsideChild(recycler, dx);
    offsetChildrenHorizontal(-realOffset);
    fill(recycler, state);
    return realOffset;
  }

  /**
   * 处理滚动事件，回收移出屏幕的View
   *
   * @param recycler
   * @param dy
   */
  private void recycleOutsideChild(RecyclerView.Recycler recycler, int dy) {
    int childCount = getChildCount();
    if (childCount == 0 || dy == 0) {
      return;
    }
    for (int i = childCount - 1; i >= 0; i--) {
      View child = getChildAt(i);
      int position = getPosition(child);
      if (!isItemVisibleInParent(position)) {
        removeAndRecycleView(child, recycler);
        if (dy > 0) {
          mFirstVisiblePos++;
        } else {
          mLastVisiblePos--;
        }
      }
    }
  }

  @Override
  public boolean canScrollVertically() {
    return mOrientation == VERTICAL;
  }

  @Override
  public boolean canScrollHorizontally() {
    return mOrientation == HORIZONTAL;
  }

  @Override
  public RecyclerView.LayoutParams generateDefaultLayoutParams() {
    if (mOrientation == HORIZONTAL) {
      return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
    } else {
      return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
  }

  @Override
  public boolean isAutoMeasureEnabled() {
    return true;
  }
}
