package com.example.kaaryakhoj;

import static com.example.kaaryakhoj.SetsActivity.setsIDs;
import static com.example.kaaryakhoj.SetsAdapter.Selectedid;
import static com.example.kaaryakhoj.TutorialsFragment.catList;
import static com.example.kaaryakhoj.TutorialsFragment.selected_cat_index;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ScoreActivity extends AppCompatActivity {

    private TextView score;
    private Button done;
    JSONObject scoreJson = new JSONObject();
    JSONObject innerJson = new JSONObject();
    String catName,score_str,setId;
    JSONObject obj1 ;

    FirebaseFirestore db= FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_score);

        score = findViewById(R.id.sa_score);
        done = findViewById(R.id.sa_done);

         score_str = getIntent().getStringExtra("SCORE");
        score.setText(score_str);
         catName = catList.get(selected_cat_index).getName();
         setId = Selectedid.get(Selectedid.size()-1);
//        String setId = setsIDs
        System.out.println("ids "+setId);
        DocumentReference docRef = db.collection("user").document("+919158346466");
        docRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Map<String, Object> job=  document.getData();
                                String obj = (String) job.get("Skills Rating");
                                if(obj==null)
                                {
                                    setScore();
                                }
                                else {
                                    try {

                                        obj1 = new JSONObject(obj);
                                        System.out.println(obj1.toString());

                                        updateScore();
                                    } catch (Throwable t) {
                                        // Log.e("My App", "Could not parse malformed JSON: \"" + json + "\"");
                                    }
                                }
                            } else {

                            }
                        }
                    }
                });



        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ScoreActivity.this,FirstPage.class);
                ScoreActivity.this.startActivity(intent);
                ScoreActivity.this.finish();


            }
        });

    }

    private void updateScore() throws JSONException {

        JSONObject var = (JSONObject) obj1.get(catName);
        int s = 0,c=0;
        String d="";

        System.out.println("S "+s);
        var.put(setId,score_str.substring(0,1));
        for(int i=0;i<setsIDs.size();i++)
        {
            System.out.println("SetIDS"+setsIDs.get(i));
            if(var.has(setsIDs.get(i))) {

                d= (String) var.get(setsIDs.get(i));
                c= Integer.parseInt(d);
                System.out.println("C "+c);
                if (c > 0) {
                    s += 1;

                }

            }
        }
        var.put("Rating",s);
        obj1.put(catName,var);

        db.collection("user").document("+919158346466")
                .update("Skills Rating",obj1.toString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("Success");

//                                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }

    private void setScore() {
        int s = 0,c=0;
        String d="";
        try {
           if(Integer.parseInt(score_str.substring(0,1))>0)
           {
               c=1;
           }

            innerJson.put("Rating",c);
            innerJson.put(setId,score_str.substring(0,1));
            scoreJson.put(catName,innerJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //System.out.println(scoreJson);
        Map<String,Object> user = new HashMap<>();
        user.put("Skills Rating",scoreJson.toString());
        db.collection("user").document("+919158346466")
                .update("Skills Rating",scoreJson.toString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("Success");

//                                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        //save data to shared prefernces
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    //load language stored in Shared Preferences
    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang","");
        setLocale(language);
    }
}

