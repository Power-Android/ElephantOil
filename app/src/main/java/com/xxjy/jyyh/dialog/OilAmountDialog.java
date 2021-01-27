package com.xxjy.jyyh.dialog;

import android.content.Context;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.KeyboardUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.rxjava.rxlife.RxLife;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.OilAmountAdapter;
import com.xxjy.jyyh.adapter.OilDiscountAdapter;
import com.xxjy.jyyh.adapter.TextWatcherAdapter;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.base.BaseRepository;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.databinding.DialogOilAmountLayoutBinding;
import com.xxjy.jyyh.entity.OilDefaultPriceEntity;
import com.xxjy.jyyh.entity.OilEntity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.functions.Consumer;
import rxhttp.RxHttp;

/**
 * @author power
 * @date 1/22/21 1:25 PM
 * @project ElephantOil
 * @description:
 */
public class OilAmountDialog extends BaseRepository {
    private Context mContext;
    private BaseActivity mActivity;
    private OilEntity.StationsBean mStationsBean;
    private BottomSheetDialog mOilAmountDialog;
    private BottomSheetBehavior mBehavior;
    private DialogOilAmountLayoutBinding mBinding;
    private List<OilDefaultPriceEntity.DefaultAmountBean> mAmountList = new ArrayList<>();
    private List<String> mDiscountList = new ArrayList<>();

    private int oilNoPosition;
    private String mOilAmout = "";//加油金额
    private OilAmountAdapter mOilAmountAdapter;
    private OilDiscountAdapter mDiscountAdapter;

    public OilAmountDialog(Context context, BaseActivity activity, OilEntity.StationsBean stationsBean) {
        this.mContext = context;
        this.mActivity = activity;
        this.mStationsBean = stationsBean;
        mBinding = DialogOilAmountLayoutBinding.bind(
                LayoutInflater.from(mContext).inflate(R.layout.dialog_oil_amount_layout, null));
        init();
        initData();
    }

    private void init() {
        if (mOilAmountDialog == null) {
            mOilAmountDialog = new BottomSheetDialog(mContext, R.style.bottom_sheet_dialog);
            mOilAmountDialog.getWindow().getAttributes().windowAnimations =
                    R.style.bottom_sheet_dialog;
            mOilAmountDialog.setCancelable(true);
            mOilAmountDialog.setCanceledOnTouchOutside(false);
            mOilAmountDialog.setContentView(mBinding.getRoot());
            mBehavior = BottomSheetBehavior.from((View) mBinding.getRoot().getParent());
            mBehavior.setSkipCollapsed(true);
        }
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void initData() {
        mBinding.amountEt.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    //TODO 1.刷新快捷价格list
                    mOilAmout = s.toString();
                }
            }
        });

        KeyboardUtils.registerSoftInputChangedListener(mOilAmountDialog.getWindow(), height -> {
            if (height > 0) {

            } else {
                //TODO 1.刷新快捷价格list 2.刷新优惠券list 3.刷新价格明细
            }
        });

        //快捷价格列表
        mBinding.amountRecyclerView.setLayoutManager(
                new GridLayoutManager(mContext, 3));
        mOilAmountAdapter = new OilAmountAdapter(R.layout.adapter_oil_amount, mAmountList);
        mBinding.amountRecyclerView.setAdapter(mOilAmountAdapter);
        mOilAmountAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mOnItemClickedListener != null) {
                mOnItemClickedListener.onOilAmountClick(adapter, view, position);
            }
        });

        //优惠列表
        for (int i = 0; i < 4; i++) {
            mDiscountList.add("");
        }
        mBinding.discountRecyclerView.setLayoutManager(
                new LinearLayoutManager(mContext));
        mDiscountAdapter = new OilDiscountAdapter(R.layout.adapter_oil_discount, mDiscountList);
        mBinding.discountRecyclerView.setAdapter(mDiscountAdapter);

        mBinding.cancelIv.setOnClickListener(view -> mOilAmountDialog.cancel());
        mBinding.backIv.setOnClickListener(view -> mOilAmountDialog.cancel());
        mDiscountAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mOnItemClickedListener != null) {
                mOnItemClickedListener.onOilDiscountClick(adapter, view, position);
            }
        });

        mBinding.cancelIv.setOnClickListener(view -> mOilAmountDialog.cancel());
        mBinding.backIv.setOnClickListener(view -> mOilAmountDialog.cancel());
        mBinding.createOrderTv.setOnClickListener(view -> {
            if (mOnItemClickedListener != null) {
                mOnItemClickedListener.onCreateOrder(view);
            }
        });
    }

    private void getPlatformCoupon() {
        //0真正可用 1已用 2过期  3时间未到 4 金额未达到
        RxHttp.postForm(ApiService.PLATFORM_COUPON)
                .add("canUse", true)
                .add("rangeType", "2")
                .add("amount", "")
                .add(Constants.GAS_STATION_ID, mStationsBean.getGasId())
                .asResponse(String.class)
                .to(RxLife.to(mActivity))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Throwable {

                    }
                });
    }

    private void getBusinessCoupon() {
        RxHttp.postForm(ApiService.BUSINESS_COUPON)
                .add("canUse", true)
                .add("amount", "")
                .add(Constants.OIL_NUMBER_ID, oilNoPosition)
                .add(Constants.GAS_STATION_ID, mStationsBean.getGasId())
                .asResponse(String.class)
                .to(RxLife.to(mActivity))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Throwable {

                    }
                });
    }

    private void getDefaultPrice(){
        RxHttp.postForm(ApiService.OIL_PRICE_DEFAULT)
                .add(Constants.GAS_STATION_ID, mStationsBean.getGasId())
                .add(Constants.OIL_NUMBER_ID, String.valueOf(mStationsBean.getOilPriceList()
                        .get(oilNoPosition).getOilNo()))
                .asResponse(OilDefaultPriceEntity.class)
                .to(RxLife.toMain(mActivity))
                .subscribe(priceEntity -> {
                    List<OilDefaultPriceEntity.DefaultAmountBean> defaultAmount = priceEntity.getDefaultAmount();
                    mOilAmountAdapter.setNewData(defaultAmount);
                }, throwable -> {

                });
    }

    public void show(int oilNoPosition) {
        mOilAmountDialog.show();
        this.oilNoPosition = oilNoPosition;

        //拿到油号以后才能请求
        getDefaultPrice();//获取快捷价格
//        getPlatformCoupon();//平台优惠券
        getBusinessCoupon();//商家优惠券
//        getBalance();
    }

    public void dismiss() {
        mOilAmountDialog.dismiss();
    }

    public interface OnItemClickedListener {
        void onOilAmountClick(BaseQuickAdapter adapter, View view, int position);

        void onOilDiscountClick(BaseQuickAdapter adapter, View view, int position);

        void onCreateOrder(View view);
    }

    private OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.mOnItemClickedListener = onItemClickedListener;
    }
}
