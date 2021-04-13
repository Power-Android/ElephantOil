package com.xxjy.jyyh.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.OilGunAdapter;
import com.xxjy.jyyh.adapter.OilNumAdapter;
import com.xxjy.jyyh.adapter.OilTypeAdapter;
import com.xxjy.jyyh.databinding.DialogOilNumLayoutBinding;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.entity.OilTypeEntity;
import com.xxjy.jyyh.utils.OilUtils;
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
    private List<OilEntity.StationsBean.OilPriceListBean> mList = new ArrayList<>();
    private int selectPosition = 0;
    private String mOilNo;

    private List<OilEntity.StationsBean.OilPriceListBean> mOilNumGasData = new ArrayList<>(); //汽油
    private List<OilEntity.StationsBean.OilPriceListBean> mOilNumDieselData = new ArrayList<>();  //柴油
    private List<OilEntity.StationsBean.OilPriceListBean> mOilNumNaturalData = new ArrayList<>(); //天然气
    private List<OilTypeEntity> mOilTypeList = new ArrayList<>();//油类型
    private List<OilEntity.StationsBean.OilPriceListBean> mOilNumList = new ArrayList<>();//油号列表
    private List<OilEntity.StationsBean.OilPriceListBean.GunNosBean> mOilGunList = new ArrayList<>();//油枪列表
    private OilTypeAdapter mOilTypeAdapter;
    private OilNumAdapter mOilNumAdapter;
    private OilGunAdapter mOilGunAdapter;


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
        //如果没有选中，默认第一个选中
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).isSelected()) {
                selectPosition = i;
                break;
            }
        }
        mList.get(selectPosition).setSelected(true);
        mOilNo = mList.get(selectPosition).getOilNo() + "";

        //油类型列表
        mBinding.oilTypeRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        mOilTypeAdapter = new OilTypeAdapter(R.layout.adapter_oil_num_layout, mOilTypeList);
        mBinding.oilTypeRecyclerView.setAdapter(mOilTypeAdapter);
        mOilTypeAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mOnItemClickedListener != null) {
                mOnItemClickedListener.onOilTypeClick(adapter, view, position, mOilNumAdapter, mOilGunAdapter);
            }

        });

        //油号列表
        mBinding.oilNumRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        mOilNumAdapter = new OilNumAdapter(R.layout.adapter_oil_num_layout, mOilNumList);
        mBinding.oilNumRecyclerView.setAdapter(mOilNumAdapter);
        mOilNumAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mOnItemClickedListener != null) {
                mOnItemClickedListener.onOilNumClick(adapter, view, position, mOilGunAdapter);
            }
        });

        //枪号列表
        mBinding.oilGunRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        mOilGunAdapter = new OilGunAdapter(R.layout.adapter_oil_num_layout, mOilGunList);
        mBinding.oilGunRecyclerView.setAdapter(mOilGunAdapter);
        mOilGunAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mOnItemClickedListener != null) {
                mOnItemClickedListener.onOilGunClick(adapter, view, position);
            }
        });

        dispatchOilData(mStationsBean);

        mBinding.queryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickedListener != null){
                    mOnItemClickedListener.onQuickClick(view, mOilNumAdapter, mOilGunAdapter);
                }
            }
        });

        mBinding.cancelIv.setOnClickListener(view -> {
            if (mOnItemClickedListener != null) {
                mOnItemClickedListener.closeAll();
            }
        });
    }

    private void dispatchOilData(OilEntity.StationsBean stationsBean) {
        //清空数据
        mOilTypeList.clear();
        mOilNumGasData.clear();
        mOilNumDieselData.clear();
        mOilNumNaturalData.clear();

        for (OilEntity.StationsBean.OilPriceListBean oilNumBean : stationsBean.getOilPriceList()) {
            if (OilUtils.isOilNumDiesel(oilNumBean)) {//柴油
                mOilNumDieselData.add(oilNumBean);
            } else if (OilUtils.isOilNumNatural(oilNumBean)) {//天然气
                mOilNumNaturalData.add(oilNumBean);
            } else {//汽油
                mOilNumGasData.add(oilNumBean);
            }
        }

        if (!mOilNumGasData.isEmpty()) {
            mOilTypeList.add(new OilTypeEntity("汽油", mOilNumGasData));
        }
        if (!mOilNumDieselData.isEmpty()) {
            mOilTypeList.add(new OilTypeEntity("柴油", mOilNumDieselData));
        }
        if (!mOilNumNaturalData.isEmpty()) {
            mOilTypeList.add(new OilTypeEntity("天然气", mOilNumNaturalData));
        }

        for (int i = 0; i < stationsBean.getOilPriceList().size(); i++) {
            if (String.valueOf(stationsBean.getOilPriceList().get(i).getOilNo()).equals(mOilNo)) {
                Integer oilType = stationsBean.getOilPriceList().get(i).getOilType();
                checkOilType(oilType);
            }
        }

        //已选择列表
        for (int i = 0; i < mOilTypeList.size(); i++) {
            if (mOilTypeList.get(i).isSelect()) {
                List<OilEntity.StationsBean.OilPriceListBean> oilPriceList = mOilTypeList.get(i).getOilPriceList();
                for (int j = 0; j < oilPriceList.size(); j++) {
                    if (String.valueOf(oilPriceList.get(j).getOilNo()).equals(mOilNo)) {
//                        mBinding.oilNumTv.setText(oilPriceList.get(j).getOilName());
//                        mBinding.oilLiterTv.setText("¥" + oilPriceList.get(j).getPriceYfq() + "/L");
//                        mBinding.oilPriceTv.setText("油站价：￥" + oilPriceList.get(j).getPriceOfficial());
//                        mBinding.oilPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
//                        mOilCheckedList.add(oilPriceList.get(j).getOilName() +
//                                getOilKind(oilPriceList.get(j).getOilType() + ""));
                    }
                }
                //更新油号列表
                mOilNumList = mOilTypeList.get(i).getOilPriceList();
                mOilNumAdapter.setNewData(mOilNumList);
            }
        }

        //油号列表
        for (int k = 0; k < mOilNumList.size(); k++) {
            if (TextUtils.equals(String.valueOf(mOilNumList.get(k).getOilNo()), String.valueOf(mOilNo))) {
                mOilNumList.get(k).setSelected(true);
                //更新油枪列表
                mOilGunList = mOilNumList.get(k).getGunNos();
                mOilGunAdapter.setNewData(mOilGunList);
            }
        }
    }

    private void checkOilType(Integer oilType) {
        if (oilType == 1) {
            for (int j = 0; j < mOilTypeList.size(); j++) {
                if (mOilTypeList.get(j).getOilTypeName().equals("汽油")) {
                    mOilTypeList.get(j).setSelect(true);
                }
            }
        }
        if (oilType == 2) {
            for (int j = 0; j < mOilTypeList.size(); j++) {
                if (mOilTypeList.get(j).getOilTypeName().equals("柴油")) {
                    mOilTypeList.get(j).setSelect(true);
                }
            }
        }
        if (oilType == 3) {
            for (int j = 0; j < mOilTypeList.size(); j++) {
                if (mOilTypeList.get(j).getOilTypeName().equals("天然气")) {
                    mOilTypeList.get(j).setSelect(true);
                }
            }
        }

        mOilTypeAdapter.setNewData(mOilTypeList);
    }


    public interface OnItemClickedListener {
        //油类型
        void onOilTypeClick(BaseQuickAdapter adapter, View view, int position, OilNumAdapter oilNumAdapter, OilGunAdapter oilGunAdapter);
        //油号
        void onOilNumClick(BaseQuickAdapter adapter, View view, int position, OilGunAdapter oilGunAdapter);
        //枪号
        void onOilGunClick(BaseQuickAdapter adapter, View view, int position);

        void onQuickClick(View view, OilNumAdapter oilNumAdapter, OilGunAdapter oilGunAdapter);

        void closeAll();
    }

    private OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.mOnItemClickedListener = onItemClickedListener;
    }
}
