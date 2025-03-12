package com.example.myapplication.auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.dao.UserDAO;
import com.example.myapplication.model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout majorInputLayout, titleInputLayout;
    private RadioGroup roleRadioGroup, genderRadioGroup;
    private RadioButton studentRadioButton, teacherRadioButton, maleRadioButton, femaleRadioButton;
    private TextInputLayout usernameInputLayout, passwordInputLayout, nameInputLayout, phoneInputLayout, emailInputLayout, departmentInputLayout;
    private TextInputEditText usernameEditText, passwordEditText, nameEditText, phoneEditText, emailEditText, departmentEditText, majorEditText, titleEditText;
    private ImageView avatarImageView;
    private Button registerButton;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri avatarUri = null; // 头像 URI

    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userDAO = new UserDAO(this); // 初始化 UserDAO

        // 绑定 UI 组件
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
        avatarImageView = findViewById(R.id.avatarImageView);

        titleInputLayout.setVisibility(View.GONE); // 默认隐藏职称输入框

        // 角色选择监听
        roleRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.studentRadioButton) {
                titleInputLayout.setVisibility(View.GONE);
                majorInputLayout.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.teacherRadioButton) {
                majorInputLayout.setVisibility(View.GONE);
                titleInputLayout.setVisibility(View.VISIBLE);
            }
        });

        // 头像选择
        avatarImageView.setOnClickListener(v -> openImageChooser());

        // 注册按钮点击事件
        registerButton.setOnClickListener(v -> registerUser());
    }

    /**
     * 选择图片
     */
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    /**
     * 处理选择的头像
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            avatarUri = data.getData();
            Glide.with(this).load(avatarUri).circleCrop().into(avatarImageView);
        }
    }

    /**
     * 用户注册逻辑
     */
    private void registerUser() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String department = departmentEditText.getText().toString().trim();
        String major = majorEditText.getText().toString().trim();
        String title = titleEditText.getText().toString().trim();
        String role = studentRadioButton.isChecked() ? "学生" : "教师";

        String gender = maleRadioButton.isChecked() ? "男" : "女";
        String avatarUrl = (avatarUri != null) ? avatarUri.toString() : "";

        // 验证输入
        if (TextUtils.isEmpty(username)) {
            usernameInputLayout.setError("用户名不能为空");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordInputLayout.setError("密码不能为空");
            return;
        }
        if (TextUtils.isEmpty(name)) {
            nameInputLayout.setError("姓名不能为空");
            return;
        }
        if (TextUtils.isEmpty(phone) || !phone.matches("^\\d{11}$")) {
            phoneInputLayout.setError("手机号格式错误");
            return;
        }
        if (TextUtils.isEmpty(email) || !email.matches("^[A-Za-z0-9._%+-]+@ujs\\.com$")) {
            emailInputLayout.setError("邮箱格式错误");
            return;
        }
        if (TextUtils.isEmpty(department)) {
            departmentInputLayout.setError("部门不能为空");
            return;
        }

        if (studentRadioButton.isChecked() && TextUtils.isEmpty(major)) {
            majorInputLayout.setError("专业不能为空");
            return;
        }
        if (teacherRadioButton.isChecked() && TextUtils.isEmpty(title)) {
            titleInputLayout.setError("职称不能为空");
            return;
        }

        // 创建 User 对象
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        user.setGender(gender);
        user.setPhone(phone);
        user.setEmail(email);
        user.setAvatarUrl(avatarUrl);
        user.setDepartment(department);
        user.setMajor(studentRadioButton.isChecked() ? major : "");
        user.setTitle(teacherRadioButton.isChecked() ? title : "");
        user.setRole(role);

        // 存入数据库
        long result = userDAO.insertUser(user);
        if (result != -1) {
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            Toast.makeText(this, "注册失败，用户名已存在", Toast.LENGTH_SHORT).show();
        }
    }
}
