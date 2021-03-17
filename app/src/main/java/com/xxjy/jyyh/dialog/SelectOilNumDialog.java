package com.xxjy.jyyh.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.OilNumAdapter;
import com.xxjy.jyyh.adapter.SelectOilNoAdapter;
import com.xxjy.jyyh.databinding.DialogSelectOilNumBinding;
import com.xxjy.jyyh.entity.OilNumBean;
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
    private String mCheckOilGasId = "92";           //当前选中的油号id

    public SelectOilNumDialog(Context context, String checkOilGasId, View view, ViewGroup rootView) {
        this.mContext = context;
        this.mView = view;
        this.mCheckOilGasId = checkOilGasId;
//        mBinding = DialogSelectOilNumBinding.bind(
//                View.inflate(mContext,R.layout.dialog_select_oil_num, null));
        mBinding = DialogSelectOilNumBinding.bind(
                LayoutInflater.from(mContext).inflate(R.layout.dialog_select_oil_num, rootView, false));
        init();
//        initData();
    }

    public void setCheckData(String checkOilGasId) {
        this.mCheckOilGasId = checkOilGasId;
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
//        for (int i = 0; i < 7; i++) {
//            OilNumCheckEntity entity= new OilNumCheckEntity();
//            entity.setKey(1);
//            mList.add(entity);
//        }
//        mBinding.noRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
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

    public void setData(List<OilNumBean> oilNumBeans) {
        dispatchData(oilNumBeans);
    }

    /**
     * @param oilNumBeans 初始化油号筛选列表
     */
    private void dispatchData(List<OilNumBean> oilNumBeans) {
        final List<OilNumCheckEntity> listBeen = getList(oilNumBeans);
        RecyclerView recyclerView = mBinding.noRecyclerView;
        SelectOilNoAdapter oilNoAdapter = new SelectOilNoAdapter(listBeen, mCheckOilGasId);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 4);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return oilNoAdapter.getItemViewType(position) == SelectOilNoAdapter.TYPETIELE
                        ? layoutManager.getSpanCount() : 1;
            }
        });
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(oilNoAdapter);
        oilNoAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (listBeen.get(position).getItemType() == SelectOilNoAdapter.TYPECONTENT) {
                mCheckOilGasId = listBeen.get(position).getOilListBeen().getId();
                if (mOnItemClickedListener != null) {
                    mOnItemClickedListener.onOilNumClick(adapter, view, position, listBeen.get(position).getOilListBeen().getOilNum(), mCheckOilGasId);
                }
                mOilNumDialog.dismiss();
            }
        });
    }

    /**
     * @param oilNumBeans
     * @return 油号筛选列表的list
     */
    private static List<OilNumCheckEntity> getList(List<OilNumBean> oilNumBeans) {
        List<OilNumCheckEntity> list = new ArrayList<>();
        if (oilNumBeans == null || oilNumBeans.size() <= 0) {
            return list;
        }
        List<OilNumBean> datas = oilNumBeans;
        for (int i = 0; i < datas.size(); i++) {
            OilNumCheckEntity oilListBean = new OilNumCheckEntity();
            oilListBean.setKey(SelectOilNoAdapter.TYPETIELE);
            oilListBean.setType(datas.get(i).getType());
            list.add(oilListBean);
            for (int j = 0; j < datas.get(i).getOilList().size(); j++) {
                oilListBean = new OilNumCheckEntity();
                oilListBean.setKey(SelectOilNoAdapter.TYPECONTENT);
                oilListBean.setOilListBeen(datas.get(i).getOilList().get(j));
                oilListBean.setType(datas.get(i).getType());
                list.add(oilListBean);
            }
        }
        return list;
    }

    public void show() {
        mOilNumDialog.show();
    }

    public interface OnItemClickedListener {
        void onOilNumClick(BaseQuickAdapter adapter, View view, int position, String oilNum, String mCheckOilGasId);
    }

    private OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.mOnItemClickedListener = onItemClickedListener;
    }
}
