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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.dao.CounselorDAO;
import com.example.myapplication.model.Counselor;

import java.util.Arrays;

public class EditCounselorActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_PROFILE = 1;
    private static final int REQUEST_CODE_PICK_CERTIFICATE = 2;

    private EditText nameEditText;
    private Spinner qualificationsSpinner, specializationSpinner;
    private RadioGroup genderGroup;
    private ImageView profileImageView, certificateImageView;
    private Button saveButton;
    private CounselorDAO counselorDAO;
    private int counselorId;
    private Counselor currentCounselor;

    private String profileImagePath, certificateImagePath;

    private final String[] specializations = {"不限", "心理咨询", "儿童心理", "婚姻家庭", "职场心理", "学生心理"};
    private final String[] qualifications = {"不限", "国家二级", "国家三级", "国际认证", "心理学博士", "资深导师"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_counselor);

        // 绑定 UI 组件
        nameEditText = findViewById(R.id.counselorName);
        qualificationsSpinner = findViewById(R.id.spinnerQualification);
        specializationSpinner = findViewById(R.id.spinnerSpecialization);
        genderGroup = findViewById(R.id.genderGroup);
        profileImageView = findViewById(R.id.counselorAvatar);
        certificateImageView = findViewById(R.id.certificateImage);
        saveButton = findViewById(R.id.btnSave);

        // 初始化数据库访问对象
        counselorDAO = new CounselorDAO(this);
        counselorId = getIntent().getIntExtra("counselor_id", -1);

        // 初始化 Spinner 适配器
        ArrayAdapter<String> qualificationsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, qualifications);
        ArrayAdapter<String> specializationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, specializations);
        qualificationsSpinner.setAdapter(qualificationsAdapter);
        specializationSpinner.setAdapter(specializationAdapter);

        // 加载咨询师数据
        if (counselorId != -1) {
            loadCounselorData();
        } else {
            Toast.makeText(this, "未找到咨询师信息", Toast.LENGTH_SHORT).show();
            finish();
        }

        // 头像、证书点击选择图片
        profileImageView.setOnClickListener(v -> pickImage(REQUEST_CODE_PICK_PROFILE));
        certificateImageView.setOnClickListener(v -> pickImage(REQUEST_CODE_PICK_CERTIFICATE));
        saveButton.setOnClickListener(v -> saveCounselorData());
    }

    /**
     * 加载咨询师数据
     */
    private void loadCounselorData() {
        currentCounselor = counselorDAO.getCounselorBasicInfoById(counselorId);

        if (currentCounselor != null) {
            nameEditText.setText(currentCounselor.getName());

            // 设置 Spinner 选中项
            int qualificationIndex = Arrays.asList(qualifications).indexOf(currentCounselor.getQualifications());
            int specializationIndex = Arrays.asList(specializations).indexOf(currentCounselor.getSpecialization());

            qualificationsSpinner.setSelection(qualificationIndex >= 0 ? qualificationIndex : 0);
            specializationSpinner.setSelection(specializationIndex >= 0 ? specializationIndex : 0);

            if ("男".equals(currentCounselor.getGender())) {
                ((RadioButton) findViewById(R.id.genderMale)).setChecked(true);
            } else {
                ((RadioButton) findViewById(R.id.genderFemale)).setChecked(true);
            }

            // 加载头像和证书
            Glide.with(this)
                    .load(currentCounselor.getAvatarUrl())
                    .placeholder(R.drawable.emoji)
                    .error(R.drawable.emoji)
                    .into(profileImageView);

            Glide.with(this)
                    .load(currentCounselor.getCertificateUrl())
                    .placeholder(R.drawable.emoji)
                    .error(R.drawable.emoji)
                    .into(certificateImageView);
        } else {
            Toast.makeText(this, "该咨询师不存在", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * 选择图片
     */
    private void pickImage(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 处理图片选择结果
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                if (requestCode == REQUEST_CODE_PICK_PROFILE) {
                    profileImagePath = imageUri.toString();
                    Glide.with(this).load(profileImagePath).into(profileImageView);
                } else if (requestCode == REQUEST_CODE_PICK_CERTIFICATE) {
                    certificateImagePath = imageUri.toString();
                    Glide.with(this).load(certificateImagePath).into(certificateImageView);
                }
            }
        }
    }

    /**
     * 保存咨询师数据
     */
    private void saveCounselorData() {
        String newName = nameEditText.getText().toString().trim();
        String newGender = ((RadioButton) findViewById(genderGroup.getCheckedRadioButtonId())).getText().toString();
        String newQualifications = qualificationsSpinner.getSelectedItem().toString();
        String newSpecialization = specializationSpinner.getSelectedItem().toString();

        if (newName.isEmpty()) {
            Toast.makeText(this, "姓名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // 更新咨询师信息
        currentCounselor.setName(newName);
        currentCounselor.setGender(newGender);
        currentCounselor.setQualifications(newQualifications);
        currentCounselor.setSpecialization(newSpecialization);
        if (profileImagePath != null) currentCounselor.setAvatarUrl(profileImagePath);
        if (certificateImagePath != null) currentCounselor.setCertificateUrl(certificateImagePath);

        boolean success = counselorDAO.updateCounselorBasicInfo(currentCounselor);

        if (success) {
            Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
            Intent resultIntent = new Intent();
            resultIntent.putExtra("updated_counselor_id", counselorId);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }
}
