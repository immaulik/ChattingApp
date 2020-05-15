package NacXo.ChattingApp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import NacXo.ChattingApp.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {
    private EditText Username,UserStatus;
    private CircleImageView imageProfileimage;
    private String mCurrentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        InitializeFields();

        Username.setVisibility(View.INVISIBLE);
        RetriveUserInfo();
    }

    private void RetriveUserInfo()
    {
        rootRef.child("Users").child(mCurrentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name")) &&
                                (dataSnapshot.hasChild("status")) && (dataSnapshot.hasChild("image")))
                        {
                            String Rname = dataSnapshot.child("name").getValue().toString();
                            String Rstatus = dataSnapshot.child("status").getValue().toString();
                            String Rimage = dataSnapshot.child("image").getValue().toString();

                            Username.setText(Rname);
                            UserStatus.setText(Rstatus);
                        }
                        else if((dataSnapshot.exists())  && (dataSnapshot.hasChild("name")) &&
                                (dataSnapshot.hasChild("status")))
                        {
                            String Rname = dataSnapshot.child("name").getValue().toString();
                            String Rstatus = dataSnapshot.child("status").getValue().toString();

                            Username.setText(Rname);
                            UserStatus.setText(Rstatus);
                        }
                        else
                        {
                            Username.setVisibility(View.VISIBLE);
                            Toast.makeText(SettingActivity.this, "Please set & Update Yout Profile Information!!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void InitializeFields() {
        Username = findViewById(R.id.set_user_name);
        UserStatus = findViewById(R.id.set_profile_status);
        imageProfileimage = findViewById(R.id.set_profile_image);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUserID = mAuth.getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();
    }

    public void updateAccountSetting(View view)
    {
        updateSetting();
    }

    private void updateSetting()
    {
        String setusername = Username.getText().toString();
        String setuserstatus = UserStatus.getText().toString();

        if(TextUtils.isEmpty(setusername))
        {
            Toast.makeText(this, "Please write username", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(setuserstatus))
        {
            Toast.makeText(this, "Please write status", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String,String> profileMap = new HashMap<>();
            profileMap.put("uid",mCurrentUserID);
            profileMap.put("name",setusername);
            profileMap.put("status",setuserstatus);
            rootRef.child("Users").child(mCurrentUserID).setValue(profileMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(SettingActivity.this, "Profile Updated SuccesssFully!!", Toast.LENGTH_SHORT).show();
                                sendUsertoMain();
                            }
                            else
                            {
                                String error = task.getException().toString();
                                Toast.makeText(SettingActivity.this, "Error :"+error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
    private void sendUsertoMain()
    {
        Intent intent = new Intent(SettingActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}
