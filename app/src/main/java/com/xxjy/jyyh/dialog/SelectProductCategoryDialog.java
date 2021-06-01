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
import com.xxjy.jyyh.adapter.SelectProductCategoryAdapter;
import com.xxjy.jyyh.databinding.DialogAreaCheckedBinding;
import com.xxjy.jyyh.entity.AreaBean;
import com.xxjy.jyyh.entity.CarServeCategoryBean;
import com.xxjy.jyyh.entity.ProductClassBean;

import java.util.ArrayList;
import java.util.List;

import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.Layer;
import per.goweii.anylayer.dialog.DialogLayer;
import per.goweii.anylayer.popup.PopupLayer;

public class SelectProductCategoryDialog {
    private Context mContext;
    private DialogLayer mDialog;
    private DialogAreaCheckedBinding mBinding;
    private List<CarServeCategoryBean> mList = new ArrayList<>();
    private SelectProductCategoryAdapter adapter;
    private int lastPosition = 0;
    private View mView;

    public SelectProductCategoryDialog(Context context, View view, ViewGroup rootView, List<CarServeCategoryBean> list) {
        this.mContext = context;
        this.mView = view;
        this.mList=list;
        mBinding = DialogAreaCheckedBinding.bind(
                LayoutInflater.from(mContext).inflate(R.layout.dialog_product_category_checked, rootView, false));
        init();
        initData();
    }

    private void init() {
        if (mDialog == null) {
            mDialog = AnyLayer.popup(mView)
                    .align(PopupLayer.Align.Direction.VERTICAL,
                            PopupLayer.Align.Horizontal.CENTER,
                            PopupLayer.Align.Vertical.BELOW,
                            false)
                    .contentView(mBinding.getRoot())
                    .backgroundDimDefault()
                    .animStyle(DialogLayer.AnimStyle.TOP)
                    .gravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);

        }
        mDialog.onDismissListener(new Layer.OnDismissListener() {
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
        adapter.notifyDataSetChanged();
    }

    private void initData() {
        mList.add(0,new CarServeCategoryBean(-1,"全部服务",true));
        initView();
    }

    private void initView() {
        //Pop
        RecyclerView recyclerView = mBinding.recyclerView;
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 4);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new SelectProductCategoryAdapter(R.layout.adapter_condition_screening, mList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((adapter, view, position) -> {
            List<CarServeCategoryBean> data = adapter.getData();
            if (lastPosition != -1) {
                data.get(lastPosition).setChecked(false);
            }
            lastPosition = position;
            data.get(position).setChecked(true);
            adapter.notifyDataSetChanged();
            if (mOnItemClickedListener != null) {
                mOnItemClickedListener.onClick(adapter, view, position,  data.get(position));
            }
            mDialog.dismiss();

        });
    }

    public void show() {
        mDialog.show();
    }

    public interface OnItemClickedListener {
        void onClick(BaseQuickAdapter adapter, View view, int position, CarServeCategoryBean bean);
    }

    private OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.mOnItemClickedListener = onItemClickedListener;
    }
}
