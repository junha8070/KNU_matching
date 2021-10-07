package com.example.knu_matching.main;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.knu_matching.R;
import com.example.knu_matching.UserAccount;
import com.example.knu_matching.membermanage.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MypageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Context context;
    private Button btn_modify, btn_PwdChg;
    private TextView tv_name, tv_rate, tv_nickname, tv_email, tv_major, tv_studentId, tv_number, tv_leave, tv_signout;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = user != null ? user.getUid() : null;
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    final DocumentReference dbRef = db.collection("Account").document(user.getEmail().replace(".", ">"));
    public String strEmail, strPassword, strNick, strMaojr, strStudentId, strPhoneNumber, strStudentName;
    UserAccount account = new UserAccount();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public MypageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment4.
     */
    // TODO: Rename and change types and number of parameters
    public static MypageFragment newInstance(String param1, String param2) {
        MypageFragment fragment = new MypageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_mypage_fragment, container, false);
        btn_PwdChg = (Button) v.findViewById(R.id.btn_PwdChg);
        btn_modify = (Button) v.findViewById(R.id.btn_modify);
        tv_name = (TextView) v.findViewById(R.id.tv_name);
        tv_rate = (TextView) v.findViewById(R.id.tv_rate);
        tv_nickname = (TextView) v.findViewById(R.id.tv_nickname);
        tv_email = (TextView) v.findViewById(R.id.tv_email);
        tv_major = (TextView) v.findViewById(R.id.tv_major);
        tv_studentId = (TextView) v.findViewById(R.id.tv_studentId);
        tv_number = (TextView) v.findViewById(R.id.tv_number);
        tv_leave = (TextView) v.findViewById(R.id.tv_leave);
        tv_signout = (TextView) v.findViewById(R.id.tv_signout);

        dbRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                    UserAccount userAccount = snapshot.toObject(UserAccount.class);
                    strStudentName = userAccount.getStudentName();
                    strEmail = userAccount.getEmailId();
                    strPassword = userAccount.getPassword();
                    strNick = userAccount.getNickName();
                    strMaojr = userAccount.getMajor();
                    strStudentId = userAccount.getStudentId();
                    strPhoneNumber = userAccount.getPhoneNumber();
                    tv_nickname.setText(strNick);
                    tv_email.setText(strEmail);
                    tv_major.setText(strMaojr);
                    tv_studentId.setText(strStudentId);
                    tv_number.setText(strPhoneNumber);
                    tv_name.setText(strStudentName);
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });



//        tv_name.setText(account.getEmailId(FirebaseUser));

        tv_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFirebaseAuth.signOut();
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), InfoModify.class);
                startActivityResult.launch(intent);
            }
        });

        btn_PwdChg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ChangePassWord.class);
                startActivity(intent);
            }
        });

        return v;
    }

    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Toast.makeText(getContext(), "테스트", Toast.LENGTH_SHORT).show();
                    }
                }
            });

}