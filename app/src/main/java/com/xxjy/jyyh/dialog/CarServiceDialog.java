package com.xxjy.jyyh.dialog;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.CarSpecAdapter;
import com.xxjy.jyyh.adapter.CarTypeAdapter;
import com.xxjy.jyyh.databinding.DialogCarServiceLayoutBinding;
import com.xxjy.jyyh.entity.CarServeProductsBean;
import com.xxjy.jyyh.entity.CarServeStoreDetailsBean;
import com.xxjy.jyyh.entity.CarTypeEntity;
import com.xxjy.jyyh.ui.car.CarServeConfirmOrderActivity;
import com.xxjy.jyyh.utils.toastlib.MyToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author power
 * @date 2021/6/7 10:08 上午
 * @project ElephantOil
 * @description:
 */
public class CarServiceDialog extends BottomSheetDialog {
    private Context mContext;
    private BottomSheetBehavior mBehavior;
    private DialogCarServiceLayoutBinding mBinding;
    private CarServeStoreDetailsBean mCardStoreInfoVo;
    private CarTypeAdapter mCarTypeAdapter;
    private CarSpecAdapter mCarSpecAdapter;
    private List<CarTypeEntity> carTypeData = new ArrayList<>();
    private List<String> classData;
    private Map<String, List<CarServeProductsBean>> mProductCategory;
    private int selectPosition = 0;
    private List<CarServeProductsBean> mList = new ArrayList<>();
    private CarServeProductsBean mCarServeProductsBean;

    public CarServiceDialog(Context context, CarServeStoreDetailsBean carServeStoreBean) {
        super(context, R.style.bottom_sheet_dialog);
        this.mContext = context;
        this.mCardStoreInfoVo = carServeStoreBean;
        mBinding = DialogCarServiceLayoutBinding.bind(
                LayoutInflater.from(mContext).inflate(R.layout.dialog_car_service_layout, null));
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
        if (mCardStoreInfoVo == null || mCardStoreInfoVo.getProductCategory() == null){
            MyToast.showError(mContext, "暂无该门店数据");
            return;
        }
        mProductCategory = mCardStoreInfoVo.getProductCategory();
        classData = new ArrayList<>(mCardStoreInfoVo.getProductCategory().keySet());
        carTypeData.clear();
        for (int i = 0; i < classData.size(); i++) {
            carTypeData.add(i, new CarTypeEntity(classData.get(i), 0 == i));
        }
        mList = mProductCategory.get(classData.get(0));
        assert mList != null;
        mList.get(0).setSelect(true);
        //服务价格
        mBinding.itemOrinPriceTv.setText("¥" + mList.get(0).getSalePrice());
        mBinding.itemPriceTv.setText("¥" + mList.get(0).getLinePrice());

        //服务类型列表
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(mContext);
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
        flexboxLayoutManager.setAlignItems(AlignItems.FLEX_START);
        mBinding.carTypeRecyclerView.setLayoutManager(flexboxLayoutManager);
        mCarTypeAdapter = new CarTypeAdapter(R.layout.adapter_oil_num_layout, carTypeData);
        mBinding.carTypeRecyclerView.setAdapter(mCarTypeAdapter);
        mCarTypeAdapter.setOnItemClickListener((adapter, view, position) -> {
            List<CarTypeEntity> data = adapter.getData();
            selectPosition = position;
            for (int i = 0; i < data.size(); i++) {
                data.get(i).setSelect(false);
            }
            data.get(position).setSelect(true);
            mCarTypeAdapter.notifyDataSetChanged();

            mList = mProductCategory.get(classData.get(selectPosition));
            mList.get(0).setSelect(true);
            mCarSpecAdapter.setNewData(mList);

            //服务价格
            mBinding.itemOrinPriceTv.setText("¥" + mList.get(0).getSalePrice());
            mBinding.itemPriceTv.setText("¥" + mList.get(0).getLinePrice());
        });

        //服务规格列表
        FlexboxLayoutManager flexboxLayoutManager1 = new FlexboxLayoutManager(mContext);
        flexboxLayoutManager1.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager1.setJustifyContent(JustifyContent.FLEX_START);
        flexboxLayoutManager1.setAlignItems(AlignItems.FLEX_START);
        mBinding.carSpecRecyclerView.setLayoutManager(flexboxLayoutManager1);
        mCarSpecAdapter = new CarSpecAdapter(R.layout.adapter_oil_num_layout, mList);
        mBinding.carSpecRecyclerView.setAdapter(mCarSpecAdapter);
        mCarSpecAdapter.setOnItemClickListener((adapter, view, position) -> {
            for (int i = 0; i < mList.size(); i++) {
                mList.get(i).setSelect(false);
            }
            mList.get(position).setSelect(true);
            //服务价格
            mBinding.itemOrinPriceTv.setText("¥" + mList.get(position).getSalePrice());
            mBinding.itemPriceTv.setText("¥" + mList.get(position).getLinePrice());
            mCarSpecAdapter.notifyDataSetChanged();
        });

        mBinding.queryTv.setOnClickListener(v -> {
            for (int i = 0; i < mCarSpecAdapter.getData().size(); i++) {
                if (mCarSpecAdapter.getData().get(i).isSelect()){
                    mCarServeProductsBean = mList.get(i);
                }
            }
            CarServeConfirmOrderActivity.openPage(mContext, mCardStoreInfoVo.getCardStoreInfoVo(), mCarServeProductsBean, null);
            dismiss();
        });

        mBinding.cancelIv.setOnClickListener(v -> dismiss());
    }

    @Override
    public void dismiss() {
        for (int i = 0; i < mList.size(); i++) {
            mList.get(i).setSelect(false);
        }
        super.dismiss();
    }
}
