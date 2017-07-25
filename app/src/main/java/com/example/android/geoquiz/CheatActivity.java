package com.example.android.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {
    private static final String EXTRA_ANSWER_IS_TRUE =
            "com.example.android.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN =
            "com.example.android.geoquiz.answer_shown";
    private static final String KEY_BUTTON_CLICKED =
            "button_clicked";
    private TextView mTextView;
    private boolean mAnswerIsTrue;
    private Button mShowAnswer;
    private boolean mIsClicked;
    //创建要传递的intent 在父Activity中调用
    public static Intent newIntent(Context context, boolean answerIsTrue) {
        Intent i = new Intent(context,CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE,answerIsTrue);
        return i;
    }
    //设置返回父Activity的信息
    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN,isAnswerShown);
        setResult(RESULT_OK,data);
    }
    //对返回给父Activity的Intent对象进行解析 解析为boolean类型 在父Activity中调用
    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN,false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        //恢复旧状态的值
        if (savedInstanceState != null) {
            mIsClicked = savedInstanceState.getBoolean(KEY_BUTTON_CLICKED);
            if (mIsClicked) {
                setAnswerShownResult(true);
            }
        }

        //获取Intent当中的信息(答案)，第二个参数是指定默认值
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE,false);

        mTextView = (TextView) findViewById(R.id.answerTextView);

        mShowAnswer = (Button) findViewById(R.id.showAnswerButton);
        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnswerIsTrue) {
                    mTextView.setText(R.string.button_true);
                } else {
                    mTextView.setText(R.string.button_false);
                }
                mIsClicked = true;
                setAnswerShownResult(mIsClicked);
                //如果运行SDK版本大于LOLLIPOP 可以展示动画按钮
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cx = mShowAnswer.getWidth()/2;
                    int cy = mShowAnswer.getHeight()/2;
                    float radius = mShowAnswer.getWidth();
                    Animator anim = ViewAnimationUtils
                            .createCircularReveal(mShowAnswer, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mShowAnswer.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_BUTTON_CLICKED,mIsClicked);

    }
}
