package com.hhhhhx.autotouch.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hhhhhx.autotouch.R;
import com.hhhhhx.autotouch.bean.ItemEvent;
import com.hhhhhx.autotouch.bean.TouchPoint;
import com.hhhhhx.autotouch.utils.SpUtils;

import java.util.List;

public class TouchPointAdapterItem extends RecyclerView.Adapter<TouchPointAdapterItem.TouchPointHolder> implements View.OnClickListener {

    private static final String TAG = "TouchPointAdapterPlus";
    private List<TouchPoint> tList;
    private OnItemClickListener onItemClickListener;
//    private int touchPosition = -1;

    public TouchPointAdapterItem() {

    }

    public void setTouchPointListList(List<TouchPoint> tList) {
        this.tList = tList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override // 这个函数主要是用来加载子项布局（fruit_item）
    public TouchPointHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_touch_point2, parent, false);
        view.setOnClickListener(this);
        return new TouchPointHolder(view);
    }

    // 自定义handler 类
    public static class TouchPointHolder extends RecyclerView.ViewHolder {
        // 成员
        TextView tvName, tvOffset;
        Button btDel;
        // 构造方法
        public TouchPointHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvOffset = itemView.findViewById(R.id.tv_offset);
            btDel = itemView.findViewById(R.id.bt_del);
        }
    }


    @SuppressLint("SetTextI18n")
    @Override //  这个方法是用来对传入的子项布局进行赋值的
    public void onBindViewHolder(@NonNull TouchPointHolder holder, int position) {
        TouchPoint touchPoint = getItem(position);

        holder.itemView.setTag(position);
        holder.tvName.setText(position+"：" + touchPoint.getName());
        holder.tvOffset.setText("间隔(" + touchPoint.getDelay() + "s)");
        holder.btDel.setOnClickListener(view ->{
            ItemEvent.postItemDelAction(position);
        });
    }

    @Override
    public int getItemCount() {
        return tList == null ? 0 : tList.size();
    }

    public TouchPoint getItem(int position) {
        return tList.get(position);
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            int postion = (int) v.getTag();
            TouchPoint touchPoint = getItem(postion);
            onItemClickListener.onItemClick(v, postion, touchPoint);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position,TouchPoint touchPoint);
    }
}

