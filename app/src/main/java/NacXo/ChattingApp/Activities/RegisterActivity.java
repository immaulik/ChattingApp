package NacXo.ChattingApp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import NacXo.ChattingApp.R;

public class RegisterActivity extends AppCompatActivity {
    private EditText useremail,userpassword;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference rootRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        InitializeFields();
        mAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();
    }

    private void InitializeFields() {
        useremail = findViewById(R.id.Register_email);
        userpassword = findViewById(R.id.Register_password);
        progressDialog = new ProgressDialog(this);
    }

    public void sendToLogin(View view)
    {
        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    public void createAccount(View view) {
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
            progressDialog.setTitle("Creating New Account");
            progressDialog.setMessage("Please Wait , While We are creating your Account!!");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        String currentUserid = mAuth.getCurrentUser().getUid();
                        rootRef.child("Users").child(currentUserid).setValue("");
                        Toast.makeText(RegisterActivity.this, "SuccessFully Registered", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        sendUsertoMain();
                    }
                    else
                    {
                        String error = task.getException().toString();
                        Toast.makeText(RegisterActivity.this, "Error : "+error, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }

    private void sendUsertoMain()
    {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
