package id.co.qualitas.qubes.adapter;//package id.co.qualitas.qubes.adapter;
//
///**
// * Created by Wiliam on 4/9/2018.
// */
//        import android.support.constraint.ConstraintLayout;
//        import android.support.v7.widget.LinearLayoutManager;
//        import android.support.v7.widget.RecyclerView;
//        import android.util.SparseBooleanArray;
//        import android.view.LayoutInflater;
//        import android.view.View;
//        import android.view.View.OnClickListener;
//        import android.view.ViewGroup;
//        import android.widget.ImageView;
//        import android.widget.ProgressBar;
//        import android.widget.TextView;
//        import android.widget.Toast;
//
//        import java.util.ArrayList;
//        import java.util.List;
//
//        import id.co.qualitas.qubes.R;
//        import id.co.qualitas.qubes.fragment.HomeFragment;
//        import id.co.qualitas.qubes.listener.OnLoadMoreListener;
//        import id.co.qualitas.qubes.model.OutletResponse;
//
//
//public class DataAdapter extends RecyclerView.Adapter {
//    private HomeFragment mcontext;
//    private final int VIEW_ITEM = 1;
//    private final int VIEW_PROG = 0;
//
//    private List<OutletResponse> mDataset = new ArrayList<>();
//
//    // The minimum amount of items to have below your current scroll position
//// before loading more.
//    private int visibleThreshold = 5;
//    private int lastVisibleItem, totalItemCount;
//    private boolean loading;
//    private OnLoadMoreListener onLoadMoreListener;
//    private SparseBooleanArray selectedItems;
//    private HomeFragment.OnRecyclerViewItemClickListener listener;
//
//    public void setOnItemClickListener(HomeFragment.OnRecyclerViewItemClickListener listener) {
//        this.listener = listener;
//    }
//    public DataAdapter(List<OutletResponse> students, HomeFragment homeFragment, RecyclerView recyclerView) {
//        mDataset = students;
//        mcontext = homeFragment;
//
//        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
//
//            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
//                    .getLayoutManager();
//
//
//            recyclerView
//                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
//                        @Override
//                        public void onScrolled(RecyclerView recyclerView,
//                                               int dx, int dy) {
//                            super.onScrolled(recyclerView, dx, dy);
//
//                            totalItemCount = linearLayoutManager.getItemCount();
//                            lastVisibleItem = linearLayoutManager
//                                    .findLastVisibleItemPosition();
//                            if (!loading
//                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
//                                // End has been reached
//                                // Do something
//                                if (onLoadMoreListener != null) {
//                                    onLoadMoreListener.onLoadMore();
//                                }
//                                loading = true;
//                            }
//                        }
//                    });
//        }
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return mDataset.get(position) != null ? VIEW_ITEM : VIEW_PROG;
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
//                                                      int viewType) {
//        RecyclerView.ViewHolder vh;
//        if (viewType == VIEW_ITEM) {
//            View v = LayoutInflater.from(parent.getContext()).inflate(
//                    R.layout.row_view_outlet_p, parent, false);
//
//            vh = new DataObjectHolder(v);
//        } else {
//            View v = LayoutInflater.from(parent.getContext()).inflate(
//                    R.layout.progressbar_item, parent, false);
//
//            vh = new ProgressViewHolder(v);
//        }
//        return vh;
//    }
//
//    @Override
//    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
//        if (holder instanceof DataObjectHolder) {
//
//            ((DataObjectHolder) holder).index.setText(String.valueOf(position + 1));
//            if (mDataset.get(position).isColor()) {
//                ((DataObjectHolder) holder).row.setBackgroundColor(mcontext.getResources().getColor(R.color.grey2));
//                ((DataObjectHolder) holder).txtClient.setTextColor(mcontext.getResources().getColor(R.color.grey3));
//                ((DataObjectHolder) holder).imgArrow.setVisibility(View.INVISIBLE);
//            } else {
//                ((DataObjectHolder) holder).row.setBackgroundColor(0x00000000);
//                ((DataObjectHolder) holder).txtClient.setTextColor(mcontext.getResources().getColor(R.color.grey3));
//                ((DataObjectHolder) holder).imgArrow.setVisibility(View.VISIBLE);
//            }
//            ((DataObjectHolder) holder).txtClient.setText(mDataset.get(position).getOutletName());
//            ((DataObjectHolder) holder).parentView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ((DataObjectHolder) holder).row.setBackgroundColor(mcontext.getResources().getColor(R.color.grey2));
//                    ((DataObjectHolder) holder).listener.onRecyclerViewItemClicked(position, Integer.parseInt(String.valueOf(getItemId(position))));
//                }
//            });
//        } else {
//            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
//        }
//    }
//
//    public void setLoaded() {
//        loading = false;
//    }
//
//    @Override
//    public int getItemCount() {
//        return mDataset.size();
//    }
//
//    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
//        this.onLoadMoreListener = onLoadMoreListener;
//    }
//
//
//    //
//    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        TextView txtClient, index;
//        ImageView imgArrow;
//        ConstraintLayout parentView, row;
//        SparseBooleanArray selectedItems;
//
//        public DataObjectHolder(View v) {
//            super(v);
////            itemView.setOnClickListener();
//            txtClient = itemView.findViewById(R.id.txtClient);
//            index = itemView.findViewById(R.id.index);
//            imgArrow = itemView.findViewById(R.id.imgArrow);
//            parentView = itemView.findViewById(R.id.item_layout);
//            row = itemView.findViewById(R.id.constraint);
//            parentView.setSelected(false);
//        }
//
//        @Override
//        public void onClick(View v) {
//            if (selectedItems.get(getAdapterPosition(), false)) {
//                selectedItems.delete(getPosition());
//                parentView.setSelected(false);
//            } else {
//                selectedItems.put(getAdapterPosition(), true);
//                parentView.setSelected(true);
//            }
//        }
//    }
//
//    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
//        public ProgressBar progressBar;
//
//        public ProgressViewHolder(View v) {
//            super(v);
//            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
//        }
//    }
//
//}
