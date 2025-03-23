package com.example.myapplication.auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.dao.CounselorDAO;
import com.example.myapplication.model.Counselor;

public class AddCounselorActivity extends AppCompatActivity {

    private ImageView counselorAvatar;
    private EditText counselorName;
    private RadioGroup genderGroup;
    private Spinner professionSpinner, qualificationSpinner;
    private ImageView uploadCertificateButton;

    private Uri aUri = null; // 头像 URI
    private Uri cUri = null; // 证书 URI

    private CounselorDAO counselorDAO;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_counselor);

        // 绑定控件
        counselorAvatar = findViewById(R.id.counselorAvatar);
        counselorName = findViewById(R.id.counselorName);
        genderGroup = findViewById(R.id.genderGroup);
        professionSpinner = findViewById(R.id.professionSpinner);
        qualificationSpinner = findViewById(R.id.qualificationSpinner);
        uploadCertificateButton = findViewById(R.id.certificateImage);

        // 初始化 CounselorDAO
        counselorDAO = new CounselorDAO(this);

        // 初始化下拉框数据
        setupSpinners();

        // 头像点击事件（可以实现上传头像）
        counselorAvatar.setOnClickListener(v -> openImageChooser());

        // 上传资质证书按钮点击事件
        uploadCertificateButton.setOnClickListener(v -> openImageChooser());

        // 添加按钮点击事件
        findViewById(R.id.btnAddCounselor).setOnClickListener(v -> addCounselor());
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    /**
     * 处理选择的头像或证书
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                // 判断是头像还是证书
                if (aUri == null) {
                    aUri = selectedImageUri;
                    Glide.with(this).load(aUri).circleCrop().into(counselorAvatar); // 显示头像
                } else {
                    cUri = selectedImageUri;
                    Glide.with(this).load(cUri).into(uploadCertificateButton); // 显示证书
                }
            }
        }
    }

    /**
     * 添加心理咨询师的逻辑
     */
    private void addCounselor() {
        // 获取输入的姓名
        String name = counselorName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
            return;
        }

        // 获取性别
        String gender = "男"; // 默认是男
        int selectedGenderId = genderGroup.getCheckedRadioButtonId();
        if (selectedGenderId == R.id.genderFemale) {
            gender = "女";
        }

        // 获取专业和资质
        String profession = professionSpinner.getSelectedItem().toString();
        String qualification = qualificationSpinner.getSelectedItem().toString();

        // 获取头像路径（此处简化为 URI 路径）
        String avatarUrl = aUri != null ? aUri.toString() : "";
        String certificateUrl = cUri != null ? cUri.toString() : "";

        // 获取可用时间（这里可以模拟一个固定的时间，或者通过输入框来获取）
        String availableTime = "[]"; // JSON 字符串

        // 创建 Counselor 对象
        Counselor counselor = new Counselor();
        counselor.setName(name);
        counselor.setGender(gender);
        counselor.setQualifications(qualification);
        counselor.setSpecialization(profession);
        counselor.setAvatarUrl(avatarUrl);
        counselor.setCertificateUrl(certificateUrl);
        counselor.setAvailableTime(availableTime);

        // 将 Counselor 插入数据库
        long counselorId = counselorDAO.insertCounselorWithCertificate(counselor);
        if (counselorId > 0) {
            Toast.makeText(this, "心理咨询师添加成功", Toast.LENGTH_SHORT).show();
            finish(); // 完成后关闭当前界面
        } else {
            Toast.makeText(this, "添加失败，请重试", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupSpinners() {
        // 专业列表
        String[] professions = {"心理咨询", "儿童心理", "婚姻家庭", "职场心理", "学生心理"};
        ArrayAdapter<String> professionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, professions);
        professionSpinner.setAdapter(professionAdapter);

        // 资质列表
        String[] qualifications = {"国家二级", "国家三级", "国际认证", "心理学博士", "资深导师"};
        ArrayAdapter<String> qualificationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, qualifications);
        qualificationSpinner.setAdapter(qualificationAdapter);
    }
}
