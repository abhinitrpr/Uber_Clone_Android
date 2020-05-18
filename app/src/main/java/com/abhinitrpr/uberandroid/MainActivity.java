package com.abhinitrpr.uberandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Switch userTypeSwitch;
   FirebaseAuth mAuth = FirebaseAuth.getInstance();
   DatabaseReference User = FirebaseDatabase.getInstance().getReference().child("users");




   public void redirectActivity() {
       if (mAuth.getCurrentUser() != null) {
           User.child(mAuth.getCurrentUser().getUid()).child("riderOrDriver").addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   String userType = String.valueOf(dataSnapshot.getValue());


                   if (userType.equals("rider")) {

                       Intent intent = new Intent(getApplicationContext(), RiderActivity.class);
                       startActivity(intent);
                   } else{
                       Intent intent = new Intent(getApplicationContext(), ViewRequestsActivity.class);
                       startActivity(intent);
                   }
               }


               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });
       }
   }

    public void getStarted(View view) {

        Log.i("SwitchType", String.valueOf(userTypeSwitch.isChecked()));

        String userType = "rider";

        if (userTypeSwitch.isChecked()) {
            userType = "driver";
        }
        if (mAuth.getCurrentUser() != null) {
            User.child(mAuth.getCurrentUser().getUid()).child("riderOrDriver").setValue(userType);

            Log.i("Info", "Redirecting as" + userType);

            redirectActivity();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        userTypeSwitch = findViewById(R.id.userTypeSwitch);

        if (mAuth.getCurrentUser() == null){

            mAuth.signInAnonymously()
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.i("Signin message", "signInAnonymously:success");


                            } else {
                                // If sign in fails, display a message to the user.
                                Log.i("Signin message", "signInAnonymously:failure", task.getException());


                            }

                            // ...
                        }
                    });
        } else {
            User.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.hasChild("riderOrDriver")) {

                        User.child(mAuth.getCurrentUser().getUid()).child("riderOrDriver").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String userType = String.valueOf(dataSnapshot.getValue());

                                Log.i("Info", "Redirecting as"+userType);

                                redirectActivity();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });







        }

    }
}
