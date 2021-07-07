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
import com.xxjy.jyyh.adapter.SelectCarTypeAdapter;
import com.xxjy.jyyh.adapter.SelectProductCategoryAdapter;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.databinding.DialogAreaCheckedBinding;
import com.xxjy.jyyh.databinding.DialogCarTypeCheckedBinding;
import com.xxjy.jyyh.entity.CarServeCategoryBean;
import com.xxjy.jyyh.entity.CarTypeBean;

import java.util.ArrayList;
import java.util.List;

import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.Layer;
import per.goweii.anylayer.dialog.DialogLayer;
import per.goweii.anylayer.popup.PopupLayer;

public class SelectCarTypeDialog {
    private Context mContext;
    private DialogLayer mDialog;
    private DialogCarTypeCheckedBinding mBinding;
    private List<CarTypeBean> mList = new ArrayList<>();
    private SelectCarTypeAdapter adapter;
    private int lastPosition = 0;
    private View mView;

    public SelectCarTypeDialog(Context context, View view, ViewGroup rootView, List<CarTypeBean> list) {
        this.mContext = context;
        this.mView = view;
        this.mList = list;
        mBinding = DialogCarTypeCheckedBinding.bind(
                LayoutInflater.from(mContext).inflate(R.layout.dialog_car_type_checked, rootView, false));
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

    public void setDismissingEvent(boolean isCanDismissing) {
        if (mDialog != null) {
            mDialog.cancelableOnClickKeyBack(isCanDismissing);
            mDialog.cancelableOnTouchOutside(isCanDismissing);
        }

    }

    public void setSelectPosition(int position) {
        mList.get(lastPosition).setChecked(false);
        lastPosition = position;
        mList.get(position).setChecked(true);
        adapter.notifyDataSetChanged();
    }

    private void initData() {
        initView();
    }

    private void initView() {
        //Pop
        RecyclerView recyclerView = mBinding.recyclerView;
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        adapter = new SelectCarTypeAdapter(R.layout.adapter_car_type, mList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((adapter, view, position) -> {
            List<CarTypeBean> data = adapter.getData();
            if (lastPosition != -1) {
                data.get(lastPosition).setChecked(false);
            }
            lastPosition = position;
            data.get(position).setChecked(true);
            UserConstants.setCarType(data.get(position).getValue());
            adapter.notifyDataSetChanged();
            if (mOnItemClickedListener != null) {
                mOnItemClickedListener.onClick(adapter, view, position, data.get(position));
            }
            mDialog.dismiss();

        });
    }

    public void show() {
        if (UserConstants.getCarType() == -1) {
            setDismissingEvent(false);
        }else{
            setDismissingEvent(true);
        }
        mDialog.show();
    }

    public interface OnItemClickedListener {
        void onClick(BaseQuickAdapter adapter, View view, int position, CarTypeBean bean);
    }

    private OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.mOnItemClickedListener = onItemClickedListener;
    }
}
