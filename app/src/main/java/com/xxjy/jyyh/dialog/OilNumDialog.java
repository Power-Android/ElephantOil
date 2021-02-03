package com.xxjy.jyyh.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import com.blankj.utilcode.util.ClickUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.OilNumAdapter;
import com.xxjy.jyyh.databinding.DialogOilNumLayoutBinding;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.utils.UiUtils;
import com.xxjy.jyyh.utils.toastlib.MyToast;

import java.util.ArrayList;
import java.util.List;

/**
 * @author power
 * @date 1/21/21 8:26 PM
 * @project ElephantOil
 * @description:
 */
public class OilNumDialog extends BottomSheetDialog {
    private Context mContext;
    private OilEntity.StationsBean mStationsBean;
    private BottomSheetBehavior mBehavior;
    private DialogOilNumLayoutBinding mBinding;
    private OilNumAdapter mOilNumAdapter;
    private List<OilEntity.StationsBean.OilPriceListBean> mList = new ArrayList<>();
    private int selectPosition = 0;

    public OilNumDialog(Context context, OilEntity.StationsBean stationsBean) {
        super(context, R.style.bottom_sheet_dialog);
        this.mContext = context;
        this.mStationsBean = stationsBean;
        mBinding = DialogOilNumLayoutBinding.bind(
                LayoutInflater.from(mContext).inflate(R.layout.dialog_oil_num_layout, null));
        init();
        initData();
    }

    private void init() {
        getWindow().getAttributes().windowAnimations =
                R.style.bottom_sheet_dialog;
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        setContentView(mBinding.getRoot());
        mBehavior = BottomSheetBehavior.from((View) mBinding.getRoot().getParent());
        mBehavior.setSkipCollapsed(true);
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void initData() {
        if (mStationsBean.getOilPriceList() == null || mStationsBean.getOilPriceList().size() <= 0) {
            MyToast.showError(mContext, "暂无该油站数据");
            return;
        }
        mList = mStationsBean.getOilPriceList();
        //默认第一个选中
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).isSelected()){
                selectPosition = i;
                break;
            }
        }
        mList.get(selectPosition).setSelected(true);
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        mOilNumAdapter = new OilNumAdapter(R.layout.adapter_oil_num_layout, mList);
        mBinding.recyclerView.setAdapter(mOilNumAdapter);
        mOilNumAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mOnItemClickedListener != null) {
                UiUtils.canClickViewStateDelayed(view, 1000);
                mOnItemClickedListener.onOilNumClick(adapter, view, position);
            }
        });
        mBinding.cancelIv.setOnClickListener(view ->{
            if (mOnItemClickedListener != null){
                mOnItemClickedListener.closeAll();
            }
        } );
    }

    public interface OnItemClickedListener {
        void onOilNumClick(BaseQuickAdapter adapter, View view, int position);
        void closeAll();
    }

    private OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.mOnItemClickedListener = onItemClickedListener;
    }
}
