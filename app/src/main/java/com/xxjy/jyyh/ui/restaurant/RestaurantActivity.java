package com.xxjy.jyyh.ui.restaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.LifeDiscountAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.databinding.ActivityRestaurantBinding;
import com.xxjy.jyyh.entity.OilDiscountEntity;

import java.util.ArrayList;
import java.util.List;

public class RestaurantActivity extends BindingActivity<ActivityRestaurantBinding, RestaurantViewModel> {
    private List<OilDiscountEntity> mDiscountList = new ArrayList<>(4);
    private LifeDiscountAdapter mDiscountAdapter;

    @Override
    protected void initView() {
        //优惠列表
        for (int i = 0; i < 4; i++) {
            mDiscountList.add(new OilDiscountEntity(0, "请输入消费金额","请输入消费金额",
                    "请输入消费金额", 0, false, false));
        }
        mBinding.discountRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDiscountAdapter = new LifeDiscountAdapter(R.layout.adapter_oil_discount, mDiscountList);
        mBinding.discountRecyclerView.setAdapter(mDiscountAdapter);
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
}