package com.example.myapplication.auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout majorInputLayout;
    private TextInputLayout titleInputLayout;
    private RadioGroup roleRadioGroup;
    private RadioButton studentRadioButton;
    private RadioButton teacherRadioButton;

    private TextInputLayout usernameInputLayout;
    private TextInputEditText usernameEditText;
    private TextInputLayout passwordInputLayout;
    private TextInputEditText passwordEditText;
    private TextInputLayout nameInputLayout;
    private TextInputEditText nameEditText;
    private RadioGroup genderRadioGroup;
    private RadioButton maleRadioButton;
    private RadioButton femaleRadioButton;
    private TextInputLayout phoneInputLayout;
    private TextInputEditText phoneEditText;
    private TextInputLayout emailInputLayout;
    private TextInputEditText emailEditText;
    private TextInputLayout departmentInputLayout;
    private TextInputEditText departmentEditText;
    private TextInputEditText majorEditText;
    private TextInputEditText titleEditText;

    private ImageView avatarImageView;  // 用于显示头像的ImageView

    private static final int PICK_IMAGE_REQUEST = 1;  // 请求代码，用于选择图片
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        majorInputLayout = findViewById(R.id.majorInputLayout);
        titleInputLayout = findViewById(R.id.titleInputLayout);
        roleRadioGroup = findViewById(R.id.roleRadioGroup);
        studentRadioButton = findViewById(R.id.studentRadioButton);
        teacherRadioButton = findViewById(R.id.teacherRadioButton);

        usernameInputLayout = findViewById(R.id.usernameInputLayout);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        passwordEditText = findViewById(R.id.passwordEditText);
        nameInputLayout = findViewById(R.id.nameInputLayout);
        nameEditText = findViewById(R.id.nameEditText);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        maleRadioButton = findViewById(R.id.maleRadioButton);
        femaleRadioButton = findViewById(R.id.femaleRadioButton);
        phoneInputLayout = findViewById(R.id.phoneInputLayout);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailInputLayout = findViewById(R.id.emailInputLayout);
        emailEditText = findViewById(R.id.emailEditText);
        departmentInputLayout = findViewById(R.id.departmentInputLayout);
        departmentEditText = findViewById(R.id.departmentEditText);
        majorEditText = findViewById(R.id.majorEditText);
        titleEditText = findViewById(R.id.titleEditText);
        registerButton = findViewById(R.id.registerButton);

        avatarImageView = findViewById(R.id.avatarImageView);  // 初始化头像ImageView

        titleInputLayout.setVisibility(View.GONE); // 默认隐藏职称输入框（针对学生）

        // 设置角色选择监听器，根据角色选择显示不同的输入框
        roleRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.studentRadioButton) {
                titleInputLayout.setVisibility(View.GONE);
                majorInputLayout.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.teacherRadioButton) {
                majorInputLayout.setVisibility(View.GONE);
                titleInputLayout.setVisibility(View.VISIBLE);
            }
        });

        // 注册按钮点击监听器
        registerButton.setOnClickListener(v -> registerUser());
        // 头像ImageView点击事件，打开图库选择图片
        avatarImageView.setOnClickListener(v -> openImageChooser());
        // 设置窗口Insets监听器，确保适应边缘到边缘的布局
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    //注册逻辑
    private void registerUser() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String department = departmentEditText.getText().toString().trim();
        String major = majorEditText.getText().toString().trim();
        String title = titleEditText.getText().toString().trim();

        String gender = "";
        if (maleRadioButton.isChecked()) {
            gender = "男";
        } else if (femaleRadioButton.isChecked()) {
            gender = "女";
        }

        if (TextUtils.isEmpty(username)) {
            usernameInputLayout.setError("用户名不能为空");
            return;
        } else {
            usernameInputLayout.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            passwordInputLayout.setError("密码不能为空");
            return;
        } else {
            passwordInputLayout.setError(null);
        }

        if (TextUtils.isEmpty(name)) {
            nameInputLayout.setError("姓名不能为空");
            return;
        } else {
            nameInputLayout.setError(null);
        }

        if (TextUtils.isEmpty(gender)) {
            Toast.makeText(this, "请选择性别", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            phoneInputLayout.setError("手机号不能为空");
            return;
        } else if (!isValidPhoneNumber(phone)) {
            phoneInputLayout.setError("手机号必须是11位数字");
            return;
        } else {
            phoneInputLayout.setError(null);
        }

        if (TextUtils.isEmpty(email)) {
            emailInputLayout.setError("邮箱不能为空");
            return;
        } else if (!isValidEmail(email)) {
            emailInputLayout.setError("邮箱必须是前缀+@ujs.com");
            return;
        } else {
            emailInputLayout.setError(null);
        }

        if (TextUtils.isEmpty(department)) {
            departmentInputLayout.setError("部门不能为空");
            return;
        } else {
            departmentInputLayout.setError(null);
        }

        if (studentRadioButton.isChecked()) {
            if (!isValidStudentID(username)) {
                usernameInputLayout.setError("学生学号必须是8位数字，以22开头");
                return;
            } else {
                usernameInputLayout.setError(null);
            }
        } else if (teacherRadioButton.isChecked()) {
            if (!isValidEmployeeID(username)) {
                usernameInputLayout.setError("教师工号必须是8位数字，以11开头");
                return;
            } else {
                usernameInputLayout.setError(null);
            }
        }

        if (studentRadioButton.isChecked() && TextUtils.isEmpty(major)) {
            majorInputLayout.setError("专业不能为空");
            return;
        } else {
            majorInputLayout.setError(null);
        }

        if (teacherRadioButton.isChecked() && TextUtils.isEmpty(title)) {
            titleInputLayout.setError("职称不能为空");
            return;
        } else {
            titleInputLayout.setError(null);
        }

        // Placeholder for data submission (e.g., API call, database storage)
        Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    // 打开图片选择器
    private void openImageChooser() {
        // 打开系统图库选择图片
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");  // 只选择图片
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    //图片加载
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();  // 获取选择的图片URI
            try {
                // 使用Glide库加载选中的图片到ImageView
                Glide.with(this)
                        .load(imageUri)
                        .circleCrop()
                        .into(avatarImageView);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "图片加载失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isValidPhoneNumber(String phone) {
        return phone.matches("^\\d{11}$"); // Check if the phone number is 11 digits
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9._%+-]+@ujs\\.com$"); // Simple regex for ujs.com email
    }

    private boolean isValidStudentID(String username) {
        return username.matches("^22\\d{6}$"); // Check if student ID starts with 22 and has 8 digits
    }

    private boolean isValidEmployeeID(String username) {
        return username.matches("^11\\d{6}$"); // Check if employee ID starts with 11 and has 8 digits
    }
}
