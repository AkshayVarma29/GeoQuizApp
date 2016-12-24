package com.example.thinkwizardx.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX1 = "index";
    private static final String KEY_INDEX2 = "cheatmessage";
    private static final int REQUEST_CODE_CHEAT = 0;

    int mCurrentIndex = 0;
    TextView questionTextView;
    Button trueButton;
    Button falseButton;
    Button nextButton;
    Button resetButton;
    boolean userAnswered;
    Button cheatButton;
    boolean isCheater;

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        questionTextView.setText(question);
    }

    private void hideToggle() {
        nextButton.setAlpha(0.4f);
    }

    private void checkAnswer(boolean userAnswer) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId;

        if(isCheater){
            messageResId = R.string.judgement_toast;
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        }else if (userAnswer == answerIsTrue) {
            messageResId = R.string.correct_toast;
        } else {
            messageResId = R.string.incorrect_toast;
        }
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK){
            return;
        }
        if(requestCode == REQUEST_CODE_CHEAT){
            if(data == null){
                return;
            }
            isCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "OnCreate Method called");
        setContentView(R.layout.activity_quiz);

        if(savedInstanceState!=null){
            mCurrentIndex =savedInstanceState.getInt(KEY_INDEX1,0);
            isCheater = savedInstanceState.getBoolean(KEY_INDEX2);
        }

        //Change the question text
        questionTextView = (TextView) findViewById(R.id.question_text);

        //Display Question at the startup
        updateQuestion();

        //Find the button views
        trueButton = (Button) findViewById(R.id.true_button);
        falseButton = (Button) findViewById(R.id.false_button);
        nextButton = (Button) findViewById(R.id.next_button);
        resetButton = (Button) findViewById(R.id.reset_button);
        cheatButton = (Button) findViewById(R.id.cheat_button);

        //OnClick Listener for cheat button
        cheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent i = CheatActivity.newIntent(QuizActivity.this,answerIsTrue);
                startActivityForResult(i,REQUEST_CODE_CHEAT);
            }
        });

        //OnClick Action for next button
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userAnswered) {
                    if (mCurrentIndex != 4) {
                        mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                        isCheater = false;
                        updateQuestion();
                        userAnswered = false;
                        hideToggle();
                    }
                }
            }
        });

        //OnClick Action for trye button
        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userAnswered) {
                    userAnswered = true;
                    checkAnswer(true);
                    if (mCurrentIndex != 4) {
                        nextButton.setAlpha(1f);
                    }
                }
            }
        });

        //OnClick Action for false button
        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userAnswered) {
                    userAnswered = true;
                    checkAnswer(false);
                    if (mCurrentIndex != 4) {
                        nextButton.setAlpha(1f);
                    }
                }
            }
        });

        //OnClick Action for Reset button
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAnswered = false;
                hideToggle();
                mCurrentIndex = 0;
                updateQuestion();
                isCheater = false;
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX1, mCurrentIndex);
        savedInstanceState.putBoolean(KEY_INDEX2,isCheater);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}