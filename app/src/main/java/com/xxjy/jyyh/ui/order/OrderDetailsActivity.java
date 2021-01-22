package com.xxjy.jyyh.ui.order;


import android.graphics.Paint;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.databinding.ActivityOrderDetailsBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderDetailsActivity extends BindingActivity<ActivityOrderDetailsBinding, OrderDetailsViewModel> {

    private QMUIPopup mNormalPopup;
    @Override
    protected void initView() {
        mBinding.topLayout.setTitle("订单详情");
        mBinding.topLayout.addLeftImageButton(R.drawable.arrow_back_black,
                R.id.qmui_topbar_item_left_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBinding.useMethodView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mBinding.useMethodView.getPaint().setAntiAlias(true);

        mBinding.orderManageLayout.setOnClickListener(this::onViewClicked);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.order_manage_layout:
                String[] listItems = new String[]{
                        "申请退款",
                        "开取发票",
                };
                List<String> data = new ArrayList<>();

                Collections.addAll(data, listItems);
                ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.adapter_order_manage_layout, data);
                AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (mNormalPopup != null) {
                            mNormalPopup.dismiss();
                        }
                    }
                };
                mNormalPopup = QMUIPopups.listPopup(this,
                        QMUIDisplayHelper.dp2px(this, 80),
                        QMUIDisplayHelper.dp2px(this, 300),
                        adapter,
                        onItemClickListener)
                        .animStyle(QMUIPopup.ANIM_GROW_FROM_CENTER)
                        .preferredDirection(QMUIPopup.DIRECTION_TOP)
                        .shadow(true)
                        .offsetYIfTop(QMUIDisplayHelper.dp2px(this, 5))
                        .skinManager(QMUISkinManager.defaultInstance(this))
                        .borderWidth(0)
                        .onDismiss(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                            }
                        })
                        .show(view);

                break;
        }
    }

    @Override
    protected void dataObservable() {

    }
}