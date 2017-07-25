package com.example.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {
    private static final String KEY_CURRENT_ID = "currentId";
    private static final String KEY_IS_CHEATER = "isCheater";
    private static final String TAG = "QuizActivity";
    private static final int REQUEST_CODE_CHEAT = 0;
    private Button mTrueButton;
    private Button mFalseButton;//Command+1自动导包
    private Button mNextButton;
    private Button mBackButton;
    private Button mCheatButton;
    private TextView mTextView;
    private Question[] mQuestions = {
            new Question(R.string.quiz_1,true),
            new Question(R.string.quiz_2,true),
            new Question(R.string.quiz_3,false)
    };
    private int mCurrentId = 0;
    private boolean mIsCheater;
    //实现更新试题的操作
    private void updateTextView() {
        int resId = mQuestions[mCurrentId].getTextResId();
        mTextView.setText(resId);
    }
    //判断答案是否正确的操作
    private void checkAnswer(boolean userAnswer) {
        boolean answer = mQuestions[mCurrentId].isAnswerTrue();
        int messageResId;
        if (mIsCheater) {//判断是否作弊
            messageResId = R.string.judge_toast;
        } else {
            if (userAnswer == answer) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }
        Toast.makeText(QuizActivity.this, messageResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Log.d(TAG,"onCreate called");
        //恢复旧状态保存的值
        if (savedInstanceState != null) {
            mCurrentId = savedInstanceState.getInt(KEY_CURRENT_ID);
            mIsCheater = savedInstanceState.getBoolean(KEY_IS_CHEATER);
        }

        mTextView = (TextView) findViewById(R.id.quiz_text_show);
        updateTextView();
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentId = (mCurrentId+1) % mQuestions.length;
                updateTextView();
            }
        });
        //正确按钮
        mTrueButton = (Button) findViewById(R.id.button_true);
        mTrueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });
        //错误按钮
        mFalseButton = (Button) findViewById(R.id.button_false);
        mFalseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });
        //下一题按钮
        mNextButton = (Button) findViewById(R.id.button_next);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentId = (mCurrentId+1) % mQuestions.length;
                mIsCheater = false;
                updateTextView();
            }
        });
        //上一题按钮
        mBackButton = (Button) findViewById(R.id.button_back);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentId--;
                if (mCurrentId < 0) {
                    mCurrentId = 2;
                }
                mIsCheater = false;
                updateTextView();
            }
        });
        //查看答案按钮
        mCheatButton = (Button) findViewById(R.id.button_cheat);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestions[mCurrentId].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this,answerIsTrue);
                startActivityForResult(intent,REQUEST_CODE_CHEAT);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart called");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG,"onRestart called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume called");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CURRENT_ID,mCurrentId);
        outState.putBoolean(KEY_IS_CHEATER,mIsCheater);
        Log.d(TAG,"onSaveInstanceState called");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }
}
