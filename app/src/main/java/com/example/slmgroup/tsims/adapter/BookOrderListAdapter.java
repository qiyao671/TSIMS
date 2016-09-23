package com.example.slmgroup.tsims.adapter;

import android.content.Context;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.slmgroup.tsims.R;
import com.example.slmgroup.tsims.model.BookOrderInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvqiyao (amorfatilay@163.com).
 * 2016/9/18 10:59.
 * 类描述：
 */
public class BookOrderListAdapter extends BaseQuickAdapter<BookOrderInfo> {
    boolean isEditMode = false;
    List<String> isCheckedList = new ArrayList<String>();
    public BookOrderListAdapter(Context context, List<BookOrderInfo> data) {
        super(R.layout.item_book_order_list, data);
//        setCheckedList();
    }

    public List<String> getIsCheckedList() {
        return isCheckedList;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, BookOrderInfo bookOrderInfo) {
        baseViewHolder
                .setText(R.id.tvBookName, bookOrderInfo.getIsbn().isEmpty() ? "该课程无教材" : bookOrderInfo.getBookName())
                .setText(R.id.tvCourseName, bookOrderInfo.getCourseName())
                .setChecked(R.id.cbIsOrdered, bookOrderInfo.getIsSelect().equals(BookOrderInfo.VALUE_ORDERED))
                .setText(R.id.tvPrice, bookOrderInfo.getPrice() + "")
                .setVisible(R.id.cbIsOrdered, isEditMode && !bookOrderInfo.getIsbn().isEmpty())
                .setVisible(R.id.tvYuan, !bookOrderInfo.getIsbn().isEmpty())
                .setVisible(R.id.ivOrdered, bookOrderInfo.getIsSelect().equals(BookOrderInfo.VALUE_ORDERED))
                .setOnCheckedChangeListener(R.id.cbIsOrdered, new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (isCheckedList.size() > 0) {
                            isCheckedList.set(baseViewHolder.getAdapterPosition(), b ? BookOrderInfo.VALUE_ORDERED : BookOrderInfo.VALUE_NOT_ORDERED);
                        }
                    }
                });
    }

    public boolean isEditMode() {
        return isEditMode;
    }

    public void setEditMode(boolean editMode) {
        isEditMode = editMode;
    }

    public void setCheckedList() {
        if (isCheckedList.size() == getData().size()) {
            for (int i = 0; i < getData().size(); i++) {
                isCheckedList.set(i, getData().get(i).getIsSelect());
            }
        }
        if (isCheckedList.size() == 0) {
            for (int i = 0; i < getData().size(); i++) {
                isCheckedList.add(i, getData().get(i).getIsSelect());
            }
        }
    }

    public void setDataListOrdered() {
        for (int i = 0; i < getData().size(); i++) {
            getData().get(i).setIsSelect(isCheckedList.get(i));
        }
    }
}
