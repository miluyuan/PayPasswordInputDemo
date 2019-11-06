package com.example.pwdinputdemo;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Method;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       final PasswordView passwordView = findViewById(R.id.pwdInput);

        final EditText editText = findViewById(R.id.et);
        NumberKeyboardView keyboardView = findViewById(R.id.keyboard);
        hideSystemSofeKeyboard(this, editText);
        keyboardView.setOnKeyboardClickListener(new NumberKeyboardView.OnKeyboardClickListener() {
            @Override
            public void onClick(int keyCode, String insert) {
                /*// 右下角按键的点击事件，删除一位输入的文字
                int start = editText.getSelectionStart();
                if (keyCode == MyKeyboardView.KEYCODE_BOTTOM_RIGHT) {
                    if (start > 0) {
                        editText.getText().delete(start - 1, start);
                    }
                }
                // 左下角按键和数字按键的点击事件，输入文字
                else {
                    editText.getEditableText().insert(start, insert);
                }*/

                if (keyCode == NumberKeyboardView.KEYCODE_BOTTOM_RIGHT) {
                    passwordView.delete();
                }
                // 左下角按键和数字按键的点击事件，输入文字
                else {
                    passwordView.append(insert);
                }
            }
        });


        passwordView.setOnInputCompleteListener(new PasswordView.OnInputCompleteListener() {
            @Override
            public void onInputComplete(String password) {
                Toast.makeText(MainActivity.this, password, Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordView.showPassword(!passwordView.isShowPassword());
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
//        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}
