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
import com.xxjy.jyyh.adapter.OilDistanceAdapter;
import com.xxjy.jyyh.adapter.SelecBusinessStatusAdapter;
import com.xxjy.jyyh.databinding.DialogBusinessStatusCheckedBinding;
import com.xxjy.jyyh.databinding.DialogOilDistanceCheckedBinding;
import com.xxjy.jyyh.entity.BusinessStatusBean;
import com.xxjy.jyyh.entity.DistanceEntity;

import java.util.ArrayList;
import java.util.List;

import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.Layer;
import per.goweii.anylayer.dialog.DialogLayer;
import per.goweii.anylayer.popup.PopupLayer;

public class SelectBusinessStatusDialog {
    private Context mContext;
    private DialogLayer mOilNumDialog;
    private DialogBusinessStatusCheckedBinding mBinding;
    private List<BusinessStatusBean> mList = new ArrayList<>();
    private SelecBusinessStatusAdapter selecBusinessStatusAdapter;
    private int lastPosition = 1;
    private View mView;

    public SelectBusinessStatusDialog(Context context, View view, ViewGroup rootView) {
        this.mContext = context;
        this.mView = view;
        mBinding = DialogBusinessStatusCheckedBinding.bind(
                LayoutInflater.from(mContext).inflate(R.layout.dialog_business_status_checked, rootView, false));
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
        selecBusinessStatusAdapter.notifyDataSetChanged();
    }

    private void initData() {
//    5km内、10km内、15km内、20km内、50km内、不限距离
        mList.add(new BusinessStatusBean("全部", -1, false));
        mList.add(new BusinessStatusBean("营业中", 0, true));
        mList.add(new BusinessStatusBean("休息中", 1, false));
        initView();
    }

    private void initView() {
        //Pop
        RecyclerView recyclerView = mBinding.recyclerView;
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 4);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        selecBusinessStatusAdapter = new SelecBusinessStatusAdapter(R.layout.adapter_condition_screening, mList);
        recyclerView.setAdapter(selecBusinessStatusAdapter);

        selecBusinessStatusAdapter.setOnItemClickListener((adapter, view, position) -> {
            List<BusinessStatusBean> data = adapter.getData();
            if (lastPosition != -1) {
                data.get(lastPosition).setChecked(false);
            }
            lastPosition = position;
            data.get(position).setChecked(true);
            selecBusinessStatusAdapter.notifyDataSetChanged();
            if (mOnItemClickedListener != null) {
                mOnItemClickedListener.onClick(adapter, view, position, data.get(position));
            }
            mOilNumDialog.dismiss();

        });
    }

    public void show() {
        mOilNumDialog.show();
    }

    public interface OnItemClickedListener {
        void onClick(BaseQuickAdapter adapter, View view, int position, BusinessStatusBean bean);
    }

    private OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.mOnItemClickedListener = onItemClickedListener;
    }
}
