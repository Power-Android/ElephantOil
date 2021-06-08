package com.xxjy.jyyh.dialog;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

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
    private int selectPosition1 = 0;
    private List<CarServeProductsBean> mList = new ArrayList<>();

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

        //服务类型列表
        mBinding.carTypeRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        mCarTypeAdapter = new CarTypeAdapter(R.layout.adapter_oil_num_layout, carTypeData);
        mBinding.carTypeRecyclerView.setAdapter(mCarTypeAdapter);
        mCarTypeAdapter.setOnItemClickListener((adapter, view, position) -> {
            List<CarTypeEntity> data = adapter.getData();
            selectPosition = position;
            for (int i = 0; i < data.size(); i++) {
                data.get(i).setSelect(false);
            }
            data.get(position).setSelect(true);
            mList = mProductCategory.get(classData.get(selectPosition));
            mCarTypeAdapter.notifyDataSetChanged();
        });

        mList = mProductCategory.get(classData.get(selectPosition1));
        assert mList != null;
        mList.get(0).setSelect(true);
        //服务价格
        mBinding.itemOrinPriceTv.setText("¥" + mList.get(0).getLinePrice());
        mBinding.itemPriceTv.setText("¥" + mList.get(0).getSalePrice());

        //服务规格列表
        mBinding.carSpecRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        mCarSpecAdapter = new CarSpecAdapter(R.layout.adapter_oil_num_layout, mList);
        mBinding.carSpecRecyclerView.setAdapter(mCarSpecAdapter);
        mCarSpecAdapter.setOnItemClickListener((adapter, view, position) -> {
            List<CarServeProductsBean> data = adapter.getData();
            selectPosition1 = position;
            for (int i = 0; i < data.size(); i++) {
                data.get(i).setSelect(false);
            }
            data.get(position).setSelect(true);
            //服务价格
            mBinding.itemOrinPriceTv.setText("¥" + mList.get(position).getLinePrice());
            mBinding.itemPriceTv.setText("¥" + mList.get(position).getSalePrice());
            mCarSpecAdapter.notifyDataSetChanged();
        });

        mBinding.queryTv.setOnClickListener(v -> {
            mContext.startActivity(new Intent(mContext, CarServeConfirmOrderActivity.class));
            dismiss();
        });
    }
}
