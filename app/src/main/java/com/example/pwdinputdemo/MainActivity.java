package com.example.pwdinputdemo;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Method;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author miluyuan
 * @date 2019/10/31 11:08
 * <p>
 * 密码输入框
 */
public class MainActivity extends AppCompatActivity {

    private PasswordView mPasswordView;
    private NumberKeyboardView mKeyboardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
    }

    private void initView() {
        mPasswordView = findViewById(R.id.pwdInput);
        mKeyboardView = findViewById(R.id.keyboard);
    }

    private void initListener() {
        mKeyboardView.setOnKeyboardClickListener(new NumberKeyboardView.OnKeyboardClickListener() {
            @Override
            public void onClick(int keyCode, String insert) {

                if (keyCode == NumberKeyboardView.KEYCODE_BOTTOM_RIGHT) {
                    mPasswordView.delete();
                }
                // 左下角按键和数字按键的点击事件，输入文字
                else {
                    mPasswordView.append(insert);
                }
            }
        });

        mPasswordView.setOnInputCompleteListener(new PasswordView.OnInputCompleteListener() {
            @Override
            public void onInputComplete(String password) {
                Toast.makeText(MainActivity.this, password, Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPasswordView.showPassword(!mPasswordView.isShowPassword());
            }
        });
    }

    /**
     * 隐藏系统键盘
     *
     * @param editText
     */
    public static void hideSystemSofeKeyboard(Context context, EditText editText) {
        try {
            Class<EditText> cls = EditText.class;
            Method setShowSoftInputOnFocus;
            setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            setShowSoftInputOnFocus.setAccessible(true);
            setShowSoftInputOnFocus.invoke(editText, false);

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 如果软键盘已经显示，则隐藏
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}
