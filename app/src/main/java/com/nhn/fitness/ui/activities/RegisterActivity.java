package com.nhn.fitness.ui.activities;

import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.nhn.fitness.R;
import com.nhn.fitness.data.dto.UserDTO;
import com.nhn.fitness.service.rest.RestApiHelper;
import com.nhn.fitness.ui.base.BaseActivity;
import com.nhn.fitness.ui.dialogs.PendingProcessDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity {
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private EditText mConfirmPasswordEditText;
    private EditText mEmailEditText;
    private EditText mPhoneEditText;
    private EditText mNameEditText;
    private AppCompatButton mRegisterButton;
    private RestApiHelper mRestApiHelper;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        initViews();
        initObserver();
    }

    private void initViews() {
        mRestApiHelper = RestApiHelper.getInstance();

        mUsernameEditText = findViewById(R.id.et_username);
        mPasswordEditText = findViewById(R.id.et_password);
        mConfirmPasswordEditText = findViewById(R.id.et_confirm_password);
        mEmailEditText = findViewById(R.id.et_email);
        mPhoneEditText = findViewById(R.id.et_phone);
        mRegisterButton = findViewById(R.id.btn_register);
        mNameEditText = findViewById(R.id.et_name);
    }

    private void initObserver() {
        mRegisterButton.setOnClickListener(v -> {
            String username = mUsernameEditText.getText().toString();
            String password = mPasswordEditText.getText().toString();
            String confirmPassword = mConfirmPasswordEditText.getText().toString();
            String email = mEmailEditText.getText().toString();
            String phone = mPhoneEditText.getText().toString();
            String name = mNameEditText.getText().toString();

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                showToast("Vui lòng điền đầy đủ thông tin");
                return;
            }
            if (!password.equals(confirmPassword)) {
                showToast("Mật khẩu không khớp");
                return;
            }
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(username);
            userDTO.setPassword(password);
            userDTO.setEmail(email);
            userDTO.setPhoneNumber(phone);
            userDTO.setName(name);
            final PendingProcessDialog dialog = new PendingProcessDialog();
            dialog.show(getSupportFragmentManager(), PendingProcessDialog.TAG);
            mRestApiHelper.register(userDTO, new Callback<UserDTO>() {
                @Override
                public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                    if (response.isSuccessful()) {
                        dialog.dismiss();
                        showToast("Đăng ký thành công");
                        finish();
                    } else {
                        dialog.dismiss();
                        showToast("Đăng ký thất bại: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<UserDTO> call, Throwable t) {
                    dialog.dismiss();
                    showToast("Có lỗi xảy ra, vui lòng thử lại sau: " + t.getMessage());
                }
            });
        });
    }
}
