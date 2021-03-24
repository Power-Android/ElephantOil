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
import com.xxjy.jyyh.databinding.DialogOilDistanceCheckedBinding;
import com.xxjy.jyyh.entity.DistanceEntity;

import java.util.ArrayList;
import java.util.List;

import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.Layer;
import per.goweii.anylayer.dialog.DialogLayer;
import per.goweii.anylayer.popup.PopupLayer;

/**
 * @author power
 * @date 3/20/21 5:07 PM
 * @project ElephantOil
 * @description:
 */
public class SelectSortDialog {
    private Context mContext;
    private DialogLayer mOilNumDialog;
    private DialogOilDistanceCheckedBinding mBinding;
    private List<DistanceEntity> mList = new ArrayList<>();
    private OilDistanceAdapter oilDistanceAdapter;
    private int lastPosition = 0;
    private View mView;

    public SelectSortDialog(Context context, View view, ViewGroup rootView) {
        this.mContext = context;
        this.mView = view;
        mBinding = DialogOilDistanceCheckedBinding.bind(
                LayoutInflater.from(mContext).inflate(R.layout.dialog_oil_distance_checked, rootView, false));
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
        oilDistanceAdapter.notifyDataSetChanged();
    }

    private void initData() {
        mList.add(new DistanceEntity("综合排序", 0, true));
        mList.add(new DistanceEntity("距离优先", 1, false));
        mList.add(new DistanceEntity("价格优先", 2, false));
        initView();
    }

    private void initView() {
        //Pop
        RecyclerView recyclerView = mBinding.recyclerView;
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 4);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        oilDistanceAdapter = new OilDistanceAdapter(R.layout.adapter_oil_distance, mList);
        recyclerView.setAdapter(oilDistanceAdapter);

        oilDistanceAdapter.setOnItemClickListener((adapter, view, position) -> {
            List<DistanceEntity> data = adapter.getData();
            if (lastPosition != -1) {
                data.get(lastPosition).setChecked(false);
            }
            lastPosition = position;
            data.get(position).setChecked(true);
            oilDistanceAdapter.notifyDataSetChanged();
            if (mOnItemClickedListener != null) {
                mOnItemClickedListener.onOilDistanceClick(adapter, view, position, data.get(position));
            }
            mOilNumDialog.dismiss();

        });
    }

    public void show() {
        mOilNumDialog.show();
    }

    public interface OnItemClickedListener {
        void onOilDistanceClick(BaseQuickAdapter adapter, View view, int position, DistanceEntity distanceEntity);
    }

    private OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.mOnItemClickedListener = onItemClickedListener;
    }
}
