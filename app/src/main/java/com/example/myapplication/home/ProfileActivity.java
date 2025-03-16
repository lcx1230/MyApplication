package com.example.myapplication.home;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.dao.UserDAO;
import com.example.myapplication.model.User;
import com.example.myapplication.utils.UserManager;
import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1; // 选择图片请求码
    private EditText etName, etGender, etPhone, etEmail;
    private ImageView ivAvatar;
    private Button btnSave;
    private Uri imageUri = null; // 存储新的头像 Uri
    private UserDAO userDAO;
    private User currentUser; // 当前用户

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adjust_profile); // 你的布局文件

        // 初始化视图
        etName = findViewById(R.id.et_name);
        etGender = findViewById(R.id.et_gender);
        etPhone = findViewById(R.id.et_phone);
        etEmail = findViewById(R.id.et_email);
        ivAvatar = findViewById(R.id.iv_avatar);
        btnSave = findViewById(R.id.btn_save);

        userDAO = new UserDAO(this);
        currentUser = UserManager.getInstance().getUser(); // 获取当前用户

        // 加载用户信息
        loadUserProfile();

        // 点击头像更换图片
        ivAvatar.setOnClickListener(v -> openImageChooser());

        // 点击保存按钮更新数据
        btnSave.setOnClickListener(v -> updateUserInfo());

        // 返回按钮
        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(v -> finish());
    }

    /**
     * 🔹 加载用户信息
     */
    private void loadUserProfile() {
        if (currentUser != null) {
            etName.setText(currentUser.getName());
            etGender.setText(currentUser.getGender());
            etPhone.setText(currentUser.getPhone());
            etEmail.setText(currentUser.getEmail());

            String avatarUrl = currentUser.getAvatarUrl();
            if (avatarUrl != null && !avatarUrl.isEmpty()) {
                Uri uri = Uri.parse(avatarUrl);
                Glide.with(this)
                        .load(uri)
                        .placeholder(R.drawable.default_avatar)
                        .error(R.drawable.icon)
                        .into(ivAvatar);
            } else {
                ivAvatar.setImageResource(R.drawable.default_avatar);
            }
        } else {
            Toast.makeText(this, "加载用户信息失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 🔹 选择图片
     */
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    /**
     * 🔹 获取选择的图片
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Log.d("ProfileActivity", "新头像 URI: " + imageUri);

            Glide.with(this)
                    .load(imageUri)
                    .into(ivAvatar);
        }
    }

    /**
     * 🔹 更新用户信息
     */
    private void updateUserInfo() {
        String name = etName.getText().toString().trim();
        String gender = etGender.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        // 🔍 校验输入格式
        if (TextUtils.isEmpty(name)) {
            etName.setError("姓名不能为空");
            return;
        }

        if (!"男".equals(gender) && !"女".equals(gender)) {
            etGender.setError("只能填写 '男' 或 '女'");
            return;
        }

        if (TextUtils.isEmpty(phone) || !phone.matches("^\\d{11}$")) {
            etPhone.setError("手机号格式错误");
            return;
        }

        if (TextUtils.isEmpty(email) || !email.matches("^[A-Za-z0-9._%+-]+@ujs\\.com$")) {
            etEmail.setError("邮箱格式错误");
            return;
        }

        // 获取新头像 URI（如果用户上传了新头像）
        String newAvatarUrl = (imageUri != null) ? imageUri.toString() : currentUser.getAvatarUrl();

        // 更新 User 对象
        currentUser.setName(name);
        currentUser.setGender(gender);
        currentUser.setPhone(phone);
        currentUser.setEmail(email);
        currentUser.setAvatarUrl(newAvatarUrl);

        // 更新数据库
        int rowsUpdated = userDAO.updateUserByExit(currentUser);
        if (rowsUpdated > 0) {
            Toast.makeText(this, "个人信息更新成功", Toast.LENGTH_SHORT).show();
            UserManager.getInstance().setUser(currentUser); // 更新全局用户信息
            finish(); // 关闭当前界面
        } else {
            Toast.makeText(this, "更新失败，请重试", Toast.LENGTH_SHORT).show();
        }
    }
}
