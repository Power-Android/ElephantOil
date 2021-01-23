package com.xxjy.jyyh.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.OilNumAdapter;
import com.xxjy.jyyh.adapter.SelectOilNoAdapter;
import com.xxjy.jyyh.databinding.DialogSelectOilNumBinding;
import com.xxjy.jyyh.entity.OilNumCheckEntity;

import java.util.ArrayList;
import java.util.List;

import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.Layer;
import per.goweii.anylayer.dialog.DialogLayer;
import per.goweii.anylayer.popup.PopupLayer;

/**
 * @author power
 * @date 1/21/21 8:26 PM
 * @project ElephantOil
 * @description:
 */
public class SelectOilNumDialog {
    private Context mContext;
    private DialogLayer mOilNumDialog;
    private DialogSelectOilNumBinding mBinding;
    private List<OilNumCheckEntity> mList = new ArrayList<>();
    private SelectOilNoAdapter mOilNumAdapter;
    private View mView;

    public SelectOilNumDialog(Context context,View view) {
        this.mContext = context;
        this.mView = view;
        mBinding = DialogSelectOilNumBinding.bind(
                View.inflate(mContext,R.layout.dialog_select_oil_num, null));
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

    private void initData() {
        for (int i = 0; i < 7; i++) {
            OilNumCheckEntity entity= new OilNumCheckEntity();
            entity.setType("1");
            mList.add(entity);
        }
        mBinding.noRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
//        mOilNumAdapter = new SelectOilNoAdapter( mList,"");
//        mBinding.noRecyclerView.setAdapter(mOilNumAdapter);
//        mOilNumAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                if (mOnItemClickedListener != null){
//                    mOnItemClickedListener.onOilNumClick(adapter, view, position);
//                }
//            }
//        });

    }

    public void show(){
        mOilNumDialog.show();
    }

    public interface OnItemClickedListener{
        void onOilNumClick(BaseQuickAdapter adapter, View view, int position);
    }

    private OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener){
        this.mOnItemClickedListener = onItemClickedListener;
    }
}
