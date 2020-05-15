package NacXo.ChattingApp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import NacXo.ChattingApp.Fragments.GroupFragment;
import NacXo.ChattingApp.R;

public class GroupChatActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ImageButton SendMsgBtn;
    private EditText userMsgInput;
    private ScrollView mscrollview;
    private TextView displayText;
    private String groupName,currentUserId,currentUserName;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef,GroupRef,GroupMessageKeyRef;
    private String currentDate,currentTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        groupName = getIntent().getStringExtra("groupName").toString();

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        GroupRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(groupName);

        IntializeFields();

        GetUserInfo();

        SendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SavemsgIntoToDatabase();

                userMsgInput.setText("");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        GroupRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists())
                {
                    DisplayMessage(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void DisplayMessage(DataSnapshot dataSnapshot)
    {
        Iterator iterator = dataSnapshot.getChildren().iterator();

        while (iterator.hasNext())
        {
            String chatDate = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatMessage = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatName = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatTime = (String) ((DataSnapshot)iterator.next()).getValue();
            displayText.setText(chatName +":\n" +chatMessage+"\n" +chatTime +"     "+chatDate+"\n\n\n");
        }
    }

    private void SavemsgIntoToDatabase()
    {
        String text = userMsgInput.getText().toString();
        String MegKey = GroupRef.push().getKey();
        if(TextUtils.isEmpty(text))
        {
            Toast.makeText(this, "Please Write Msg First!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Calendar ccalfordate = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd,yyyy");
            currentDate = simpleDateFormat.format(ccalfordate.getTime());

            Calendar ccalfortime = Calendar.getInstance();
            SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm a");
            currentTime = simpleTimeFormat.format(ccalfortime.getTime());

            HashMap<String,Object> map = new HashMap<>();
            GroupRef.updateChildren(map);

            GroupMessageKeyRef = GroupRef.child(MegKey);
            HashMap<String,Object> map1 = new HashMap<>();
            map1.put("name",currentUserName);
            map1.put("message",text);
            map1.put("date",currentDate);
            map1.put("time",currentTime);
            GroupMessageKeyRef.updateChildren(map1);

        }
    }

    private void GetUserInfo()
    {
        userRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    currentUserName = dataSnapshot.child("name").getKey().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void IntializeFields()
    {
        mToolbar = findViewById(R.id.group_chat_appbar);
        mToolbar.setTitle(groupName);
        getSupportActionBar();
        SendMsgBtn = findViewById(R.id.send_msg_btn);
        userMsgInput = findViewById(R.id.input_group_msg);
        displayText = findViewById(R.id.group_chat_text_display);
        mscrollview = findViewById(R.id.myscroll_view);
    }
}
