package com.example.myapplication.home;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import java.util.ArrayList;
import java.util.List;

public class PsychologyTestActivity extends AppCompatActivity {

    private List<Question> questionList;
    private int currentQuestionIndex = 0;
    private int score = 0;

    private TextView questionTextView;
    private RadioGroup answerRadioGroup;
    private RadioButton answer1RadioButton;
    private RadioButton answer2RadioButton;
    private RadioButton answer3RadioButton;
    private RadioButton answer4RadioButton;
    private Button nextButton;
    private Button previousButton;
    private TextView scoreTextView;
    private ProgressBar progressBar;
    private TextView progressPercentageTextView;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psychology_test);

        initQuestions();
        initViews();
        showQuestion();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answerRadioGroup.getCheckedRadioButtonId() == -1) {
                    // 如果没有选择答案，提示用户选择
                    Toast.makeText(PsychologyTestActivity.this, "请选择一个答案", Toast.LENGTH_SHORT).show();
                } else {
                    checkAnswer();
                    if (currentQuestionIndex < questionList.size() - 1) {
                        currentQuestionIndex++;
                        showQuestion();
                    } else {
                        showScore();
                    }
                }
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuestionIndex > 0) {
                    currentQuestionIndex--;
                    showQuestion();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initQuestions() {
        questionList = new ArrayList<>();
        questionList.add(new Question("当你面临一个重要决策时，你通常会：", "仔细分析各种可能性后再做决定", "先做了再说，边做边调整", "听取他人的意见后再做决定", "犹豫不决，很难做出决定", 1));
        questionList.add(new Question("在社交场合中，你更倾向于：", "主动与很多人交流互动", "只和熟悉的几个人交流", "观察别人的交流，自己较少参与", "感到不自在，想尽快离开", 1));
        questionList.add(new Question("当你遇到困难时，你会：", "积极寻找解决办法", "先尝试自己解决，不行再寻求帮助", "寻求他人的帮助", "感到沮丧，不知所措", 1));
        questionList.add(new Question("你对新事物的态度通常是：", "充满好奇，愿意尝试", "有点好奇，但会谨慎对待", "不太感兴趣，更喜欢熟悉的事物", "害怕改变，抗拒新事物", 1));
        questionList.add(new Question("当你和别人发生冲突时，你会：", "冷静地沟通，寻求解决方案", "表达自己的观点，但也会听取对方的意见", "坚持自己的观点，不太容易妥协", "避免冲突，选择退让", 1));
        questionList.add(new Question("你在工作或学习中，通常是：", "有明确的目标和计划，按部就班地完成", "有大致的方向，但更灵活地应对变化", "走一步看一步，没有太多计划", "经常拖延，难以按时完成任务", 1));
        questionList.add(new Question("你在团队合作中，更倾向于：", "主动承担领导角色，带领团队前进", "积极参与讨论，提供有价值的建议", "做好自己负责的部分，配合团队", "不太参与团队活动，比较独立", 1));
        questionList.add(new Question("当你感到压力很大时，你会：", "通过运动、冥想等方式放松自己", "和朋友倾诉，缓解压力", "沉浸在工作或学习中，转移注意力", "选择逃避，不去面对压力", 1));
        questionList.add(new Question("你对自己的未来：", "充满信心，有清晰的规划", "有一些期待，但不确定具体方向", "比较迷茫，不知道未来会怎样", "感到担忧，对未来没有信心", 1));
        questionList.add(new Question("在面对失败时，你会：", "总结经验教训，重新出发", "有点失落，但很快能调整心态", "长时间陷入自责和沮丧中", "从此一蹶不振，不敢再尝试", 1));
    }

    private void initViews() {
        questionTextView = findViewById(R.id.questionTextView);
        answerRadioGroup = findViewById(R.id.answerRadioGroup);
        answer1RadioButton = findViewById(R.id.answer1RadioButton);
        answer2RadioButton = findViewById(R.id.answer2RadioButton);
        answer3RadioButton = findViewById(R.id.answer3RadioButton);
        answer4RadioButton = findViewById(R.id.answer4RadioButton);
        nextButton = findViewById(R.id.nextButton);
        previousButton = findViewById(R.id.previousButton);
        scoreTextView = findViewById(R.id.scoreTextView);
        progressBar = findViewById(R.id.progressBar);
        progressPercentageTextView = findViewById(R.id.progressPercentageTextView);
        backButton = findViewById(R.id.backButton);
    }

    private void showQuestion() {
        Question question = questionList.get(currentQuestionIndex);
        questionTextView.setText(question.getQuestion());
        answer1RadioButton.setText(question.getAnswer1());
        answer2RadioButton.setText(question.getAnswer2());
        answer3RadioButton.setText(question.getAnswer3());
        answer4RadioButton.setText(question.getAnswer4());
        answerRadioGroup.clearCheck();

        int progress = (currentQuestionIndex + 1) * 10;
        if (progress > 100) {
            progress = 100;
        }
        progressBar.setProgress(progress);
        progressPercentageTextView.setText(progress + "%");

        if (currentQuestionIndex == 0) {
            previousButton.setVisibility(View.GONE);
        } else {
            previousButton.setVisibility(View.VISIBLE);
        }
    }

    private void checkAnswer() {
        int selectedId = answerRadioGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            Question question = questionList.get(currentQuestionIndex);
            int correctAnswerIndex = question.getCorrectAnswerIndex();
            if (selectedId == R.id.answer1RadioButton) {
                if (correctAnswerIndex == 1) {
                    score++;
                }
            } else if (selectedId == R.id.answer2RadioButton) {
                if (correctAnswerIndex == 2) {
                    score++;
                }
            } else if (selectedId == R.id.answer3RadioButton) {
                if (correctAnswerIndex == 3) {
                    score++;
                }
            } else if (selectedId == R.id.answer4RadioButton) {
                if (correctAnswerIndex == 4) {
                    score++;
                }
            }
        }
    }

    private void showScore() {
        questionTextView.setVisibility(View.GONE);
        answerRadioGroup.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);
        previousButton.setVisibility(View.GONE);
        scoreTextView.setVisibility(View.VISIBLE);
        scoreTextView.setText("你的分数是：" + score + "分");
        backButton.setVisibility(View.VISIBLE);

        if (score >= 0 && score <= 3) {
            Toast.makeText(this, "得分较低，需加强心理调适，可多交流学习。", Toast.LENGTH_LONG).show();
        } else if (score >= 4 && score <= 7) {
            Toast.makeText(this, "得分中等，有提升空间，多参加社交活动。", Toast.LENGTH_LONG).show();
        } else if (score >= 8 && score <= 10) {
            Toast.makeText(this, "得分很高，保持积极心态面对挑战。", Toast.LENGTH_LONG).show();
        }
    }

    private static class Question {
        private String question;
        private String answer1;
        private String answer2;
        private String answer3;
        private String answer4;
        private int correctAnswerIndex;

        public Question(String question, String answer1, String answer2, String answer3, String answer4, int correctAnswerIndex) {
            this.question = question;
            this.answer1 = answer1;
            this.answer2 = answer2;
            this.answer3 = answer3;
            this.answer4 = answer4;
            this.correctAnswerIndex = correctAnswerIndex;
        }

        public String getQuestion() {
            return question;
        }

        public String getAnswer1() {
            return answer1;
        }

        public String getAnswer2() {
            return answer2;
        }

        public String getAnswer3() {
            return answer3;
        }

        public String getAnswer4() {
            return answer4;
        }

        public int getCorrectAnswerIndex() {
            return correctAnswerIndex;
        }
    }
}