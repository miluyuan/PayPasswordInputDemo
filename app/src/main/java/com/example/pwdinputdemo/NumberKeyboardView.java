package com.example.pwdinputdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author miluyuan
 * @date 2019/10/31 9:38
 * <p>
 * 数字密码键盘
 */
public class NumberKeyboardView extends KeyboardView implements KeyboardView.OnKeyboardActionListener {
    /**
     * 键盘左下角按键的Keycode。
     */
    public static final int KEYCODE_BOTTOM_LEFT = -11;
    /**
     * 键盘右下角按键的Keycode。
     */
    public static final int KEYCODE_BOTTOM_RIGHT = Keyboard.KEYCODE_DELETE;

    private OnKeyboardClickListener mListener;

    public NumberKeyboardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        Keyboard keyboard = new Keyboard(context, R.xml.keyboard_number);
        setKeyboard(keyboard);

        setOnKeyboardActionListener(this);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        List<Keyboard.Key> keys = getKeyboard().getKeys();
        for (Keyboard.Key key : keys) {
            int code = key.codes[0];
            if (KEYCODE_BOTTOM_RIGHT == code) {
                drawSpecialKeyBg(canvas, key);
                drawSpecialKeyIcon(canvas, key);
            } else if (KEYCODE_BOTTOM_LEFT == code) {
                drawSpecialKeyBg(canvas, key);
            }
        }
    }

    /**
     * 打乱键盘按键
     */
    public void shuffleKeyboard() {
        List<Character> names = new ArrayList<>();
        List<Keyboard.Key> keys = getKeyboard().getKeys();
        //记录需要打乱的key
        for (Keyboard.Key key : keys) {
            if (key.codes[0] != KEYCODE_BOTTOM_LEFT && key.codes[0] != KEYCODE_BOTTOM_RIGHT) {
                names.add((char) key.codes[0]);
            }
        }
        Collections.shuffle(names);

        int index = 0;
        for (Keyboard.Key key : keys) {
            if (key.codes[0] != KEYCODE_BOTTOM_LEFT && key.codes[0] != KEYCODE_BOTTOM_RIGHT) {
                Character character = names.get(index++);
                key.codes[0] = character;
                key.label = String.valueOf(character);
            }
        }

        invalidate();
    }

    private void drawSpecialKeyIcon(Canvas canvas, Keyboard.Key key) {
        Drawable icon = key.icon;
        if (icon == null) {
            return;
        }
        int width = icon.getMinimumWidth();
        int height = icon.getMinimumHeight();
        int x = key.x + (key.width / 2 - width / 2);
        int y = key.y + (key.height / 2 - height / 2);
        icon.setBounds(x, y, x + width, y + height);
        icon.draw(canvas);
    }

    private void drawSpecialKeyBg(Canvas canvas, Keyboard.Key key) {
        ColorDrawable drawable = new ColorDrawable();
        if (getBackground() instanceof ColorDrawable) {
            drawable.setColor(((ColorDrawable) getBackground()).getColor());
        }
        int[] drawableState = key.getCurrentDrawableState();
        drawable.setState(drawableState);
        drawable.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
        drawable.draw(canvas);
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        if (mListener == null) {
            return;
        }
        String insertText = "";
        if (primaryCode != KEYCODE_BOTTOM_RIGHT) {
            insertText = Character.toString((char) primaryCode);
        }
        mListener.onClick(primaryCode, insertText);
    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    public void setOnKeyboardClickListener(OnKeyboardClickListener listener) {
        mListener = listener;
    }

    public interface OnKeyboardClickListener {
        /**
         * @param keyCode 编码
         * @param insert  内容
         */
        void onClick(int keyCode, String insert);
    }
}
