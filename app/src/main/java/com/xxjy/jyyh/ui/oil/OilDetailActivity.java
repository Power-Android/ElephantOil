package com.xxjy.jyyh.ui.oil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.OilCheckedAdapter;
import com.xxjy.jyyh.adapter.OilNumAdapter;
import com.xxjy.jyyh.adapter.OilStationFlexAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.databinding.ActivityOilDetailBinding;
import com.xxjy.jyyh.dialog.OilAmountDialog;
import com.xxjy.jyyh.dialog.OilGunDialog;
import com.xxjy.jyyh.dialog.OilPayDialog;
import com.xxjy.jyyh.dialog.OilTipsDialog;

import java.util.ArrayList;
import java.util.List;

public class OilDetailActivity extends BindingActivity<ActivityOilDetailBinding, OilViewModel> {
    private List<String> mTagList = new ArrayList<>();
    private List<String> mOilNumList = new ArrayList<>();
    private List<String> mOilCheckedList = new ArrayList<>();
    private OilNumAdapter mOilNumAdapter;
    private OilGunDialog mOilGunDialog;
    private OilAmountDialog mOilAmountDialog;
    private OilTipsDialog mOilTipsDialog;
    private OilPayDialog mOilPayDialog;

    @Override
    protected void initView() {
        setTransparentStatusBar(mBinding.backIv, false);

        //标签列表
        for (int i = 0; i < 2; i++) {
            mTagList.add("加油免费洗车");
        }
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(this);
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
        flexboxLayoutManager.setAlignItems(AlignItems.FLEX_START);
        mBinding.oilTagRecyclerView.setLayoutManager(flexboxLayoutManager);
        OilStationFlexAdapter flexAdapter =
                new OilStationFlexAdapter(R.layout.adapter_oil_detail_tag, mTagList);
        mBinding.oilTagRecyclerView.setAdapter(flexAdapter);

        //已选择列表
        for (int i = 0; i < 2; i++) {
            mOilCheckedList.add("");
        }
        mBinding.oilCheckRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        OilCheckedAdapter oilCheckedAdapter = new OilCheckedAdapter(R.layout.adapter_oil_checked, mOilCheckedList);
        mBinding.oilCheckRecyclerView.setAdapter(oilCheckedAdapter);

        //油号列表
        for (int i = 0; i < 7; i++) {
            mOilNumList.add("");
        }
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mOilNumAdapter = new OilNumAdapter(R.layout.adapter_oil_num_layout, mOilNumList);
        mBinding.recyclerView.setAdapter(mOilNumAdapter);
        mOilNumAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mOilGunDialog != null){
                mOilGunDialog.show();
            }
        });

        initDialog();

    }

    private void initDialog() {
        mOilGunDialog = new OilGunDialog(this);
        mOilGunDialog.setOnItemClickedListener((adapter, view, position) -> {
            if (mOilAmountDialog != null){
                mOilAmountDialog.show();
            }
        });

        mOilAmountDialog = new OilAmountDialog(this);
        mOilAmountDialog.setOnItemClickedListener(new OilAmountDialog.OnItemClickedListener() {
            @Override
            public void onOilAmountClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onOilDiscountClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onCreateOrder(View view) {
                if (mOilTipsDialog != null){
                    mOilTipsDialog.show(view);
                }
            }
        });

        mOilTipsDialog = new OilTipsDialog(this);
        mOilTipsDialog.setOnItemClickedListener(view -> {
            mOilTipsDialog.dismiss();
            if (mOilPayDialog != null){
                mOilPayDialog.show();
            }
        });

        mOilPayDialog = new OilPayDialog(this);
        mOilPayDialog.setOnItemClickedListener(new OilPayDialog.OnItemClickedListener() {
            @Override
            public void onOilPayTypeClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onCloseAllClick() {
                mOilAmountDialog.dismiss();
                mOilGunDialog.dismiss();
            }
        });
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