package com.example.prabin.agriculturearcgis.NavigationTasks.UserDataInput;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prabin.agriculturearcgis.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Calendar;

public class DataInputActivity extends AppCompatActivity {

    //https://www.androidtutorialpoint.com/basics/dynamically-add-and-remove-views-in-android
    TableLayout table;
    Button btnDate;
    TextView tvName, tvEmail;

    FirebaseAuth mAuth;
    SignInButton btnGoogleSignin;
    GoogleSignInClient mGoogleSignInClient;

    String userName;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_input);

        table = findViewById(R.id.data_input_table_fields);
        btnDate = findViewById(R.id.data_date_select);
        tvName = findViewById(R.id.data_profile_username);
        tvEmail = findViewById(R.id.data_profile_email);

        btnGoogleSignin = findViewById(R.id.data_btn_signin);

        showDatePickerDialog();
        addDefaultFields();

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        checkUserStatus();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnGoogleSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });

        findViewById(R.id.data_profile_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(DataInputActivity.this)
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                signOutFromGoogle();
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .show();
            }
        });

        iterateTable();
    }


    private void checkUserStatus() {
        FirebaseUser user = mAuth.getCurrentUser();
        btnGoogleSignin.setVisibility(View.GONE);
        tvName.setVisibility(View.GONE);
        tvEmail.setVisibility(View.GONE);

        if (user == null) {
            btnGoogleSignin.setVisibility(View.VISIBLE);
        } else {
            tvName.setVisibility(View.VISIBLE);
            tvEmail.setVisibility(View.VISIBLE);
            userName = user.getDisplayName();
            userEmail = user.getEmail();
            tvName.setText(userName);
            tvEmail.setText(userEmail);
        }
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1234);
    }

    private void signOutFromGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(DataInputActivity.this, gso);

        //signout google account
        mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //signout from firebase
                FirebaseAuth.getInstance().signOut();
                checkUserStatus();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1234) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    checkUserStatus();
                                } else {
                                    Toast.makeText(DataInputActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } catch (ApiException e) {
                // Google Sign In failed or cancelled
            }
        }
    }

    private void iterateTable() {

    }

    private void showDatePickerDialog() {

        int startYear = Calendar.getInstance().get(Calendar.YEAR);
        int startMonth = Calendar.getInstance().get(Calendar.MONTH);
        int startDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        btnDate.setText(startYear + "/" + (startMonth + 1) + "/" + startDay);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                btnDate.setText(year + "/" + (month + 1) + "/" + day);
            }
        }, startYear, startMonth, startDay);     //start year, month, day

        datePickerDialog.show();
    }

    private void addDefaultFields() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < 5; i++) {

            View rowView = inflater.inflate(R.layout.data_input_field, null);

            Spinner spinnerCrop = rowView.findViewById(R.id.data_crop_select);
            spinnerCrop.setSelection(i);

            Spinner spinnerUnit = rowView.findViewById(R.id.data_unit);
            spinnerUnit.setSelection(1);        //Set Quintal as initial selection
            table.addView(rowView, table.getChildCount());
        }
    }

    public void addField(View view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.data_input_field, null);

        Spinner spinnerUnit = rowView.findViewById(R.id.data_unit);
        spinnerUnit.setSelection(1);        //Set Quintal as initial selection
        table.addView(rowView, table.getChildCount());
    }

    public void removeField(View view) {
        table.removeView((View) view.getParent().getParent());
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setMessage("Any changes will be discarded. Continue?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DataInputActivity.super.onBackPressed();
                    }
                }).setNegativeButton("Cancel", null)
                .show();
    }

}
