package id.co.qualitas.qubes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.model.NavDrawerItem;

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    List<NavDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void updateSelected(int position) {
        for (int i = 0; i < data.size(); i++) {
            if (i == position) data.get(i).setShowNotify(true);
            else data.get(i).setShowNotify(false);
        }
        notifyDataSetChanged();
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_view_nav_drawer, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NavDrawerItem current = data.get(position);
        holder.title.setText(current.getTitle());
        switch (position) {
            case 0:
                holder.icon.setImageResource(R.drawable.ic_home_selector);
//                if (current.isShowNotify()) {
//                    holder.icon.setImageResource(R.drawable.homehover);
//                } else {
//                    holder.icon.setImageResource(R.drawable.ic_home_selector);
//                }
                break;
            case 1:
                holder.icon.setImageResource(R.drawable.ic_target_selector);
//                if (current.isShowNotify()) {
//                    holder.icon.setImageResource(R.drawable.targethover);
//                } else {
//                    holder.icon.setImageResource(R.drawable.ic_target_selector);
//                }
                break;
            case 2:
                holder.icon.setImageResource(R.drawable.ic_orderplan_selector);
//                if (current.isShowNotify()) {
//                    holder.icon.setImageResource(R.drawable.orderplanhover);
//                } else {
//                    holder.icon.setImageResource(R.drawable.ic_orderplan_selector);
//                }
                break;
            case 3:
                holder.icon.setImageResource(R.drawable.selector_ic_promotion);
//                if (current.isShowNotify()) {
//                    holder.icon.setImageResource(R.drawable.ic_promote_hover);
//                } else {
//                    holder.icon.setImageResource(R.drawable.ic_promote);
//                }
                break;
            case 4:
                holder.icon.setImageResource(R.drawable.ic_activity_selector);
//                if (current.isShowNotify()) {
//                    holder.icon.setImageResource(R.drawable.visithover);
//                } else {
//                    holder.icon.setImageResource(R.drawable.ic_visit_selector);
//                }
                break;
            case 5:
                holder.icon.setImageResource(R.drawable.ic_summary_selector);
//                if (current.isShowNotify()) {
//                    holder.icon.setImageResource(R.drawable.summaryhover);
//                } else {
//                    holder.icon.setImageResource(R.drawable.ic_summary_selector);
//                }
                break;
            case 6:
                holder.icon.setImageResource(R.drawable.ic_profile_selector);
//                if (current.isShowNotify()) {
//                    holder.icon.setImageResource(R.drawable.profilehover);
//                } else {
//                    holder.icon.setImageResource(R.drawable.ic_profile_selector);
//                }
                break;
            case 7:
                holder.icon.setImageResource(R.drawable.ic_sync_black_24dp);
//                if (current.isShowNotify()) {
//                    holder.icon.setImageResource(R.drawable.profilehover);
//                } else {
//                    holder.icon.setImageResource(R.drawable.ic_profile_selector);
//                }
                break;
            case 8:
                holder.icon.setImageResource(R.drawable.ic_feedback_24dp);
//                if (current.isShowNotify()) {
//                    holder.icon.setImageResource(R.drawable.profilehover);
//                } else {
//                    holder.icon.setImageResource(R.drawable.ic_profile_selector);
//                }
                break;
            case 9:
                holder.icon.setImageResource(R.drawable.ic_settings_black_24dp);
//                if (current.isShowNotify()) {
//                    holder.icon.setImageResource(R.drawable.profilehover);
//                } else {
//                    holder.icon.setImageResource(R.drawable.ic_profile_selector);
//                }
                break;
        }

        if (current.isShowNotify()) {
//            holder.layout_menu_drawer.setBackgroundResource(R.drawable.bg_pureblue);
            holder.layout_menu_drawer.setBackgroundColor(context.getResources().getColor(R.color.veryLightGray2));
//            holder.title.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            holder.layout_menu_drawer.setBackgroundColor(context.getResources().getColor(R.color.veryLightGray));
//            holder.title.setTextColor(context.getResources().getColor(R.color.pureBlue));
//            if (position % 2 == 0) {
//                holder.layout_menu_drawer.setBackgroundResource(R.drawable.gradient_menu_even);
//            } else {
//                holder.layout_menu_drawer.setBackgroundResource(R.drawable.gradient_menu_odd);
//            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;
        RelativeLayout layout_menu_drawer;
        RelativeLayout menu_background;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            icon = (ImageView) itemView.findViewById(R.id.imgIcon);
            menu_background = (RelativeLayout) itemView.findViewById(R.id.l1);
            layout_menu_drawer = (RelativeLayout) itemView.findViewById(R.id.layout_menu_drawer);
        }
    }
}
