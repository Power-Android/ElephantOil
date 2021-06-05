package com.xxjy.jyyh.ui.car;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.CarServeDiscountAdapter;
import com.xxjy.jyyh.adapter.OilDiscountAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.databinding.ActivityCarServeConfirmOrderBinding;
import com.xxjy.jyyh.entity.OilDiscountEntity;

import java.util.ArrayList;
import java.util.List;

public class CarServeConfirmOrderActivity extends BindingActivity<ActivityCarServeConfirmOrderBinding,CarServeViewModel> {

    private List<OilDiscountEntity> mDiscountList = new ArrayList<>();
    private CarServeDiscountAdapter mDiscountAdapter;
    @Override
    protected void initView() {
        setTransparentStatusBar(mBinding.backIv, true);
        initDiscount();
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void onViewClicked(View view) {

    }

    @Override
    protected void dataObservable() {

    }
    private void initDiscount(){
        //优惠列表
        for (int i = 0; i < 4; i++) {
            mDiscountList.add(new OilDiscountEntity(0, "请选择加油金额", "请选择加油金额",
                    "请选择加油金额", 0, false, false));
        }
        mBinding.discountRecyclerView.setLayoutManager(
                new LinearLayoutManager(this));
        mDiscountAdapter = new CarServeDiscountAdapter(R.layout.adapter_oil_discount, mDiscountList);
        mBinding.discountRecyclerView.setAdapter(mDiscountAdapter);
    }
}