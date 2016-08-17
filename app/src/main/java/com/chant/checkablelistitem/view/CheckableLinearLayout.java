package com.chant.checkablelistitem.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.LinearLayout;

import com.chant.checkablelistitem.R;

public class CheckableLinearLayout extends LinearLayout implements Checkable {

    private boolean mIsChecked = false;

    private boolean mIsEditing = false;

    private Drawable mCheckboxDrawable;

    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

    public CheckableLinearLayout(Context context) {
        super(context);
        init();
    }

    public CheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CheckableLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mCheckboxDrawable = getResources().getDrawable(R.drawable.dialog_check_mark);
        // 恢复 ViewGroup 的 draw 功能（默认关闭），使 onDraw 方法会被调用
        setWillNotDraw(false);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        // 将 getDrawableState 返回的状态数组设置给 mCheckboxDrawable，并触发重绘
        if (mCheckboxDrawable != null) {
            int[] drawableState = getDrawableState();
            mCheckboxDrawable.setState(drawableState);
            invalidate();
        }
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        // 调用 super 时参数加上状态集的长度
        final int[] drawableState = super.onCreateDrawableState(extraSpace + CHECKED_STATE_SET.length);
        if (isChecked()) {
            // 被 checked 状态下，在 super 返回的数组上追加自己的状态集合
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    public void setChecked(boolean checked) {
        if (mIsChecked != checked) {
            mIsChecked = checked;
            // checked 状态改变时调用 refreshDrawableState()
            refreshDrawableState();
        }
    }

    @Override
    public boolean isChecked() {
        return mIsChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 将 mCheckboxDrawable 画到 Canvas 上
        if (isEditing() && mCheckboxDrawable != null) {
            int left = dpToPx(5);
            mCheckboxDrawable.setBounds(left, getPaddingTop(),
                    left + mCheckboxDrawable.getIntrinsicWidth(),
                    getPaddingTop() + mCheckboxDrawable.getIntrinsicHeight());
            mCheckboxDrawable.draw(canvas);
        }
    }

    public void setEditable(boolean editable) {

    }

    public boolean isEditable() {
        return true;
    }

    public void setEdit(boolean edit) {
        if (mIsEditing != edit) {
            mIsEditing = edit;
            updatePaddingForDrawable(mIsEditing);
        }
    }

    public void toggleEditMode() {
        setEdit(!isEditing());
    }

    public boolean isEditing() {
        return mIsEditing;
    }

    private void updatePaddingForDrawable(boolean isEditing) {
        setPadding(
                isEditing ?
                        getPaddingLeft() + mCheckboxDrawable.getIntrinsicWidth() + dpToPx(10) :
                        getPaddingLeft() - mCheckboxDrawable.getIntrinsicWidth() - dpToPx(10),
                getPaddingTop(), getPaddingRight(), getPaddingBottom()
        );
    }

    private int dpToPx(int dpValue) {
        return (int) (dpValue * Resources.getSystem().getDisplayMetrics().density + 0.5f);
    }
}
