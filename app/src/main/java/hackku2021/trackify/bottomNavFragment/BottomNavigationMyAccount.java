package hackku2021.trackify.bottomNavFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;

import hackku2021.trackify.R;
import hackku2021.trackify.login.LoginActivity;


public class BottomNavigationMyAccount extends Fragment {

    private CardView cardViewLogout;
    private FirebaseUser user;
    private ProgressBar userInfoProgressBarSpinner;
    private ImageView imageViewProfilePhoto, imageButtonEditUserData;
    private TextView textViewUserName,
    textViewTotalVehicleCount, textViewGoodVehicleCount, textViewBadVehicleCount;
    private EditText editTextUserName;
    private LinearLayout linearLayoutButton;
    private Button buttonSave, buttonCancel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_navigation_my_account, container, false);

        cardViewLogout = view.findViewById(R.id.userInfoLogOutCardView);
        userInfoProgressBarSpinner = view.findViewById(R.id.myAccountProgressSpinner);
        imageViewProfilePhoto = view.findViewById(R.id.userInfoProfilePicture);
        textViewUserName = view.findViewById(R.id.userInfoUserName);
        editTextUserName = view.findViewById(R.id.userInfoEditTextUserName);
        textViewTotalVehicleCount = view.findViewById(R.id.textViewTotalVehicleCount);
        textViewGoodVehicleCount = view.findViewById(R.id.textViewGoodCount);
        textViewBadVehicleCount = view.findViewById(R.id.textViewBadCount);
        imageButtonEditUserData = view.findViewById(R.id.userInfoEditButton);
        user = FirebaseAuth.getInstance().getCurrentUser();
        linearLayoutButton = view.findViewById(R.id.userInfoButtonLinearLayout);
        buttonCancel = view.findViewById(R.id.userInfoEditButtonCancel);
        buttonSave = view.findViewById(R.id.userInfoEditButtonSave);

        setImageViewProfilePhoto();
        loadUserData();
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cardViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutMethod();
            }
        });

        imageButtonEditUserData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditLayout();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEditInfoButtonMethodCall();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCancelLayout();
            }
        });

    }

    private void setEditLayout()
    {
        textViewUserName.setVisibility(View.GONE);
        editTextUserName.setVisibility(View.VISIBLE);
        imageButtonEditUserData.setVisibility(View.GONE);
        linearLayoutButton.setVisibility(View.VISIBLE);
        editTextUserName.setText(textViewUserName.getText());
    }

    private void setCancelLayout()
    {
        textViewUserName.setVisibility(View.VISIBLE);
        editTextUserName.setVisibility(View.GONE);
        imageButtonEditUserData.setVisibility(View.VISIBLE);
        linearLayoutButton.setVisibility(View.GONE);
    }

    public void logOutMethod()
    {
        AuthUI.getInstance()
                .signOut(getActivity().getApplicationContext())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        Toast.makeText(getActivity().getApplicationContext(), "Signout Success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
    }

    private void setImageViewProfilePhoto()
    {
            if (user.getPhotoUrl() != null) {
                for (UserInfo profile : user.getProviderData())
                    if (GoogleAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                        Uri photoUrl = user.getPhotoUrl();
                        String originalPieceOfUrl = "s96-c";
                        String newPieceOfUrlToAdd = "s400-c";

                        String photoPath = photoUrl.toString();
                        String newString = photoPath.replace(originalPieceOfUrl, newPieceOfUrlToAdd);
                        Glide.with(getActivity().getApplicationContext()).load(newString)
                                .into(imageViewProfilePhoto);
                    }
            }
        }


    private void loadUserData()
    {
        textViewUserName.setText(user.getDisplayName());
        userInfoProgressBarSpinner.setVisibility(View.GONE);
    }

    public void saveEditInfoButtonMethodCall() {
        String name = editTextUserName.getText().toString();

        if (name.isEmpty())
            Toast.makeText(getActivity().getApplicationContext(), "Fill the above field to continue.", Toast.LENGTH_SHORT).show();
        else {
            save(name);
        }
    }

    void save(String name) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userInfoProgressBarSpinner.setVisibility(View.VISIBLE);


        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        assert user != null;
        user.updateProfile(profileChangeRequest)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                           // Toast.makeText(getActivity().getApplicationContext(), "Information Updated", Toast.LENGTH_SHORT).s;
                            userInfoProgressBarSpinner.setVisibility(View.GONE);
                            setCancelLayout();
                            loadUserData();
                        }
                    }
                });
    }

}
