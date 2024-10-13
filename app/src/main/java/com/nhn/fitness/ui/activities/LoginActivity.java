package com.nhn.fitness.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.nhn.fitness.R;
import com.nhn.fitness.data.dto.UserDTO;
import com.nhn.fitness.data.room.AppDatabase;
import com.nhn.fitness.data.shared.AppSettings;
import com.nhn.fitness.data.shared.SessionManager;
import com.nhn.fitness.service.rest.RestApiHelper;
import com.nhn.fitness.ui.base.BaseActivity;
import com.nhn.fitness.ui.dialogs.PendingProcessDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private AppCompatButton mLoginButton;
    private TextView mRegisterTextView;
    private RestApiHelper mRestApiHelper;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        initObserver();
    }

    private void initViews() {
        mRestApiHelper = RestApiHelper.getInstance();

        mUsernameEditText = findViewById(R.id.et_username);
        mPasswordEditText = findViewById(R.id.et_password);
        mLoginButton = findViewById(R.id.btn_login);
        mRegisterTextView = findViewById(R.id.tv_register);
    }

    private void initObserver() {
        mLoginButton.setOnClickListener(v -> {
            String username = mUsernameEditText.getText().toString();
            String password = mPasswordEditText.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                showToast("Tài khoản hoặc mật khẩu không được để trống");
                return;
            }

            final PendingProcessDialog dialog = new PendingProcessDialog();
            dialog.show(getSupportFragmentManager(), PendingProcessDialog.TAG);
            mRestApiHelper.login(username, password, new Callback<UserDTO>() {
                @Override
                public void onResponse(@NonNull Call<UserDTO> call, @NonNull Response<UserDTO> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        //TODO fetch info cua account ve
                        UserDTO body = response.body();
                        SessionManager.getInstance().setCurrentUser(body);
                        AppSettings.getInstance().setLoggedIn(true);
                        AppSettings.getInstance().setLoggedInId(body.getId());
                        // fetch data
                        AppDatabase.getInstance().fetchAllData(v -> {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        });
                    } else {
                        showToast("Tài khoản hoặc mật khẩu không chính xác/hoặc không tồn tại");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<UserDTO> call, @NonNull Throwable t) {
                    dialog.dismiss();
                    showToast("Có lỗi xảy ra, vui lòng thử lại sau" + t.getMessage());
                }
            });
        });
        mRegisterTextView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
