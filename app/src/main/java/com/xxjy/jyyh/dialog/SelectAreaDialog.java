package com.xxjy.jyyh.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.SelectAreaAdapter;
import com.xxjy.jyyh.databinding.DialogAreaCheckedBinding;
import com.xxjy.jyyh.entity.AreaBean;
import com.xxjy.jyyh.entity.DistanceEntity;

import java.util.ArrayList;
import java.util.List;

import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.Layer;
import per.goweii.anylayer.dialog.DialogLayer;
import per.goweii.anylayer.popup.PopupLayer;

public class SelectAreaDialog {
    private Context mContext;
    private DialogLayer mOilNumDialog;
    private DialogAreaCheckedBinding mBinding;
    private List<AreaBean> mList = new ArrayList<>();
    private SelectAreaAdapter selectAreaAdapter;
    private int lastPosition = 0;
    private View mView;

    public SelectAreaDialog(Context context, View view, ViewGroup rootView,List<AreaBean> list) {
        this.mContext = context;
        this.mView = view;
        this.mList=list;
        mBinding = DialogAreaCheckedBinding.bind(
                LayoutInflater.from(mContext).inflate(R.layout.dialog_area_checked, rootView, false));
        init();
        initData();
    }

    private void init() {
        if (mOilNumDialog == null) {
            mOilNumDialog = AnyLayer.popup(mView)
                    .align(PopupLayer.Align.Direction.VERTICAL,
                            PopupLayer.Align.Horizontal.CENTER,
                            PopupLayer.Align.Vertical.BELOW,
                            false)
                    .contentView(mBinding.getRoot())
                    .backgroundDimDefault()
                    .animStyle(DialogLayer.AnimStyle.TOP)
                    .gravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);

        }
        mOilNumDialog.onDismissListener(new Layer.OnDismissListener() {
            @Override
            public void onDismissing(@NonNull Layer layer) {

            }

            @Override
            public void onDismissed(@NonNull Layer layer) {
            }
        });
    }


    public void setSelectPosition(int position) {
        mList.get(lastPosition).setChecked(false);
        lastPosition = position;
        mList.get(position).setChecked(true);
        selectAreaAdapter.notifyDataSetChanged();
    }

    private void initData() {
        mList.add(0,new AreaBean("全市","-1",true));
        initView();
    }

    private void initView() {
        //Pop
        RecyclerView recyclerView = mBinding.recyclerView;
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 4);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        selectAreaAdapter = new SelectAreaAdapter(R.layout.adapter_condition_screening, mList);
        recyclerView.setAdapter(selectAreaAdapter);

        selectAreaAdapter.setOnItemClickListener((adapter, view, position) -> {
            List<AreaBean> data = adapter.getData();
            if (lastPosition != -1) {
                data.get(lastPosition).setChecked(false);
            }
            lastPosition = position;
            data.get(position).setChecked(true);
            selectAreaAdapter.notifyDataSetChanged();
            if (mOnItemClickedListener != null) {
                mOnItemClickedListener.onClick(adapter, view, position,  data.get(position));
            }
            mOilNumDialog.dismiss();

        });
    }

    public void show() {
        mOilNumDialog.show();
    }

    public interface OnItemClickedListener {
        void onClick(BaseQuickAdapter adapter, View view, int position, AreaBean areaBean);
    }

    private OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.mOnItemClickedListener = onItemClickedListener;
    }
}
