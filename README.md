# SplitLayoutManager

定制RecyclerView指定水平和垂直方向，均分布局的SplitLayoutManager，每个Item居中显示


支持指定SpanCount和Orientation，使SpanCount个数的Item均分RecyclerView的高度或宽度（根据Orientation来确定）
并且每个Item在均分的空间内居中显示，同样的，SplitLayoutManager实现了RecyclerView的复用机制

# 使用

```Java
SplitLayoutManager layoutManager = new SplitLayoutManager(SplitLayoutManager.HORIZONTAL, 4);
recyclerView.setLayoutManager(layoutManager);
```

# 效果一览
对比下LinearLayoutManager，GridLayoutManager，SplitLayoutManager的效果
![SplitLayoutManager效果图](image/SplitLayoutManager.gif)
