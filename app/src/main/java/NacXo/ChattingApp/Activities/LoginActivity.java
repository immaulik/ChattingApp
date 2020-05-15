package NacXo.ChattingApp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import NacXo.ChattingApp.R;

public class LoginActivity extends AppCompatActivity {
    FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;
    private EditText useremail,userpassword;
    private TextView NewAccountLink,ForgotPassword;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        InitializeFields();

        mAuth = FirebaseAuth.getInstance();

        mCurrentUser = mAuth.getCurrentUser();
    }

    private void InitializeFields() {
        useremail = findViewById(R.id.login_email);
        userpassword = findViewById(R.id.login_password);
        NewAccountLink = findViewById(R.id.login_new_account);
        ForgotPassword = findViewById(R.id.forgot_password_link);
        progressDialog = new ProgressDialog(this);
    }

    private void sendUsertoMain()
    {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void sendToRegister(View view) {
        Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }

    public void usingPhone(View view) {

    }

    public void loginUser(View view) {
        AllowUserToLogin();
    }

    private void AllowUserToLogin() {
        String email = useremail.getText().toString();
        String password = userpassword.getText().toString();
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please enter email-id", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            progressDialog.setTitle("Sign In");
            progressDialog.setMessage("Please Wait....");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(LoginActivity.this, "Logged in SuccessFul", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            sendUsertoMain();
                        }
                        else
                        {
                            String error = task.getException().toString();
                            Toast.makeText(LoginActivity.this, "Error : "+error, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
        }
    }
}
