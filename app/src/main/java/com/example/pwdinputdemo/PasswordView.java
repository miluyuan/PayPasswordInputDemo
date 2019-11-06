package com.example.pwdinputdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @author miluyuan
 * @date 2019/10/31 11:08
 * <p>
 * 密码输入框
 */
public class PasswordView extends View {
    /**
     * 密码长度
     */
    private int mPwdLength;
    private float mStrokeWidth, mLatticeLength, mDotRadius, mTextSize;
    private Paint mStrokePaint;
    private Paint mDotPaint;
    private StringBuilder mPassword = new StringBuilder();
    private OnInputCompleteListener mListener;
    private boolean mShowPassword;
    private Rect mTemp;
    private int mCurrentStrokeColor;
    private int mStrokeColor;
    private int mPadding;

    public PasswordView(Context context) {
        this(context, null);
    }

    public PasswordView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PasswordView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PasswordView, defStyleAttr, 0);
        mLatticeLength = ta.getDimension(R.styleable.PasswordView_pwdLatticeLength, getPx(32));
        mStrokeWidth = ta.getDimension(R.styleable.PasswordView_pwdStrokeWidth, getPx(0.5f));
        mDotRadius = ta.getDimension(R.styleable.PasswordView_pwdDotRadius, getPx(3));
        mTextSize = ta.getDimension(R.styleable.PasswordView_pwdTextSize, getPx(14));
        mStrokeColor = ta.getColor(R.styleable.PasswordView_pwdStrokeColor, Color.BLACK);
        mCurrentStrokeColor = ta.getColor(R.styleable.PasswordView_pwdCurrentStrokeColor, mStrokeColor);
        mPwdLength = ta.getInt(R.styleable.PasswordView_pwdLength, 6);
        ta.recycle();

        mStrokePaint = new Paint();
        mStrokePaint.setAntiAlias(true);
        mStrokePaint.setColor(mStrokeColor);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setStrokeWidth(mStrokeWidth);

        mDotPaint = new Paint();
        mDotPaint.setAntiAlias(true);
        mDotPaint.setColor(Color.BLACK);
        mDotPaint.setStyle(Paint.Style.FILL);

        mTemp = new Rect();

        if (isInEditMode()) {
            mPassword = new StringBuilder("ab");
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mPadding = Math.max(2, getPaddingLeft());
        float h = mLatticeLength + mStrokeWidth + mPadding * 2;
        float w = mLatticeLength * mPwdLength + mStrokeWidth + mPadding * 2;

        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        //当宽度大于限定宽度时取限定宽度
        if (w > measureWidth) {
            w = measureWidth;
            mLatticeLength = (w - mStrokeWidth - mPadding * 2) / mPwdLength;
            h = mLatticeLength + mStrokeWidth;
        }
        super.onMeasure(MeasureSpec.makeMeasureSpec((int) w, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec((int) h, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float start = mStrokeWidth / 2 + mPadding;
        float top = start;
        //画边框
        for (int i = 0; i < mPwdLength; i++) {
            float left = start + mLatticeLength * i;
            float right = mLatticeLength + left;
            float height = mLatticeLength + top;
            mStrokePaint.setColor(mStrokeColor);
            canvas.drawRect(left, top, right, height, mStrokePaint);
        }

        //画最后一个密码位置所在边框
        if (mPassword.length() > 0) {
            float left = start + mLatticeLength * (mPassword.length() - 1);
            float right = mLatticeLength + left;
            float height = mLatticeLength + top;
            mStrokePaint.setColor(mCurrentStrokeColor);
            canvas.drawRect(left, top, right, height, mStrokePaint);
        }

        if (mShowPassword) {
            //画字
            mDotPaint.setTextSize(mTextSize);
            for (int i = 0; i < mPassword.length(); i++) {
                String c = String.valueOf(mPassword.charAt(i));
                mDotPaint.getTextBounds(c, 0, 1, mTemp);
                float width = mTemp.right - mTemp.left;
                float height = mTemp.bottom - mTemp.top;
                float left = start + mLatticeLength * i;
                canvas.drawText(c, left + (mLatticeLength - width) / 2, top + (mLatticeLength + height) / 2, mDotPaint);
            }
        } else {
            //画点
            for (int i = 0; i < mPassword.length(); i++) {
                float left = start + mLatticeLength * i;
                float right = mLatticeLength + left;
                float height = mLatticeLength + top;
                canvas.drawCircle((left + right) / 2, (top + height) / 2, mDotRadius, mDotPaint);
            }
        }
    }

    public void showPassword(boolean isShow) {
        mShowPassword = isShow;
        invalidate();
    }

    public boolean isShowPassword() {
        return mShowPassword;
    }

    public void delete() {
        if (mPassword.length() == 0) {
            return;
        }
        mPassword.deleteCharAt(mPassword.length() - 1);
        invalidate();
    }

    public void append(String value) {
        if (mPassword.length() == mPwdLength) {
            return;
        }
        mPassword.append(value);
        if (mPassword.length() == mPwdLength && mListener != null) {
            mListener.onInputComplete(mPassword.toString());
        }
        invalidate();
    }

    private float getPx(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    public void setOnInputCompleteListener(OnInputCompleteListener listener) {
        mListener = listener;
    }

    public interface OnInputCompleteListener {
        /**
         * 输入密码完成
         *
         * @param password 密码
         */
        void onInputComplete(String password);
    }
}
