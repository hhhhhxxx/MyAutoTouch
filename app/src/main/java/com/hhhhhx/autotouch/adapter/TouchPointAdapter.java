package com.hhhhhx.autotouch.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hhhhhx.autotouch.R;
import com.hhhhhx.autotouch.bean.TouchPoint;

import java.util.List;

public class TouchPointAdapter extends RecyclerView.Adapter<TouchPointAdapter.TouchPointHolder> implements View.OnClickListener {

    private List<List<TouchPoint>> tListList;
    private OnItemClickListener onItemClickListener;
//    private int touchPosition = -1;

    public TouchPointAdapter() {

    }

    public void setTouchPointListList(List<List<TouchPoint>> tListList) {
        this.tListList = tListList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override // 这个函数主要是用来加载子项布局（fruit_item）
    public TouchPointHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_touch_point, parent, false);
        view.setOnClickListener(this);
        return new TouchPointHolder(view);
    }

    // 自定义handler 类
    public static class TouchPointHolder extends RecyclerView.ViewHolder {
        // 成员
        TextView tvName, tvOffset;
        Button btStop;
        // 构造方法
        public TouchPointHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvOffset = itemView.findViewById(R.id.tv_offset);
            btStop = itemView.findViewById(R.id.bt_stop);
        }
    }


    @SuppressLint("SetTextI18n")
    @Override //  这个方法是用来对传入的子项布局进行赋值的
    public void onBindViewHolder(@NonNull TouchPointHolder holder, int position) {
        holder.itemView.setTag(position);
        List<TouchPoint> tList = getItem(position);

        TouchPoint touchPoint = tList.get(0);

        holder.tvName.setText("第一个操作：" + touchPoint.getName());
        holder.tvOffset.setText("间隔(" + touchPoint.getDelay() + "s)");
//        holder.btStop.setVisibility(touchPosition == position ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return tListList == null ? 0 : tListList.size();
    }

    public List<TouchPoint> getItem(int position) {
        return tListList.get(position);
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            int postion = (int) v.getTag();
            List<TouchPoint> tList = getItem(postion);
            onItemClickListener.onItemClick(v, postion, tList);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position,List<TouchPoint> tList);
    }
}
