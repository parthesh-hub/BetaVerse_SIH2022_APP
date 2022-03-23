package com.example.kaaryakhoj;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.util.Locale;
import java.util.Map;

public class MyProfileFragment extends Fragment {

    FirebaseUser currentUser;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    View layoutview;
    TextView profilepage_name, profilepage_address, profilepage_pincode, profilepage_upi, profilepage_phone,
            getProfilepage_experience, profilepage_skillrating ;
    RatingBar profilepage_workrating;
    Button logoutbtn, chngphotobtn;
    String dbfirstname, dblastname, dbaddress, dbpincode, dbupi, dbphone, dbexp;
    String dbworkrating, dbpro;
    FirebaseFirestore db;
    LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        System.out.println(currentUser);
        loadingDialog = new LoadingDialog(getActivity());
        loadLocale();

        layoutview = inflater.inflate(R.layout.activity_profile_page, container, false);

        profilepage_name = (TextView) layoutview.findViewById(R.id.profilepage_name);
        profilepage_address = (TextView) layoutview.findViewById(R.id.profilepage_address);
        profilepage_pincode = (TextView) layoutview.findViewById(R.id.profilepage_pincode);
        profilepage_upi = (TextView) layoutview.findViewById(R.id.profilepage_upi);
        profilepage_phone = (TextView) layoutview.findViewById(R.id.profilepage_phone);
        profilepage_workrating = (RatingBar) layoutview.findViewById(R.id.profilepage_workRating);
        getProfilepage_experience = (TextView) layoutview.findViewById(R.id.profilepage_experience);
//        profilepage_skillrating = (TextView) layoutview.findViewById(R.id.profilepage_skillrating);
        logoutbtn = (Button) layoutview.findViewById(R.id.profilepage_logoutbtn);
        //chngphotobtn = (Button) layoutview.findViewById(R.id.changeprofilephotobutton);
        getDetails(currentUser.getPhoneNumber());



        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(getActivity(), LoginPage.class));
                getActivity().finish();
            }
        });


        return layoutview;
    }

    private void getDetails(String phoneno) {

        db = FirebaseFirestore.getInstance();
        final JSONObject[] jsonObject = new JSONObject[1];
//        dbphone = currentUser.getPhoneNumber();

        loadingDialog.startLoadingDialog();
        DocumentReference docRef1 = db.collection("user").document(phoneno);

        docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Map<String, Object> user = document.getData();
                        dbfirstname = (String) user.get("Firstname");
                        dblastname = (String) user.get("Lastname");
                        dbaddress = (String) user.get("Address");
                        dbpincode = (String) user.get("Pincode");
                        dbupi = (String) user.get("UpiId");
                        dbworkrating = (String) user.get("overall_rating");
                        dbexp = (String) user.get("experience");
                        dbpro = (String) user.get("Skills Rating");

//                        try {
//                            jsonObject[0] = new JSONObject(dbpro);
////                            System.out.println("JSONOBJECT: "+ jsonObject[0].getJSONObject("Painting"));
////                            profilepage_skillrating.setText(jsonObject[0].toString());
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }

                    }
                } else {
                    System.out.println("TASK FAILED");
                }
                profilepage_name.setText(dbfirstname+" "+dblastname);
                profilepage_address.setText(dbaddress);
                profilepage_pincode.setText(dbpincode);
                profilepage_upi.setText(dbupi);
                profilepage_phone.setText(phoneno);
                try{
                    Integer rating = Integer.parseInt(dbworkrating);
                    profilepage_workrating.setRating(rating);}
                catch (Exception e){
                    profilepage_workrating.setRating(0);
                }

                getProfilepage_experience.setText(dbexp);


                loadingDialog.dismissDialog();

            }
        });

    }



    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getResources().updateConfiguration(config, getActivity().getResources().getDisplayMetrics());

        //save data to shared prefernces
        SharedPreferences.Editor editor = this.getActivity().getSharedPreferences("Settings", Activity.MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    //load language stored in Shared Preferences
    public void loadLocale(){
        SharedPreferences prefs = this.getActivity().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang","");
        setLocale(language);
    }

}

