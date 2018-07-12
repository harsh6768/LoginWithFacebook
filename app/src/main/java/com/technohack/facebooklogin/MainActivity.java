package com.technohack.facebooklogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.profileImage) ImageView profileImage;
    @BindView(R.id.emailId) TextView myEmail;
    @BindView(R.id.birthdayId)TextView myBirthday;
    @BindView(R.id.friendsId) TextView myFriends;
    @BindView(R.id.login_button) LoginButton loginButton;

    ProgressDialog progressDialog;

    CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        progressDialog=new ProgressDialog(MainActivity.this);
        //to see the keyHash
        printKeyHash();

        callbackManager=CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("public_profile","email","user_birthday","user_friends"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                progressDialog.setMessage("Retrieving Data");
                progressDialog.show();

                String userId=loginResult.getAccessToken().getUserId();

                GraphRequest graphRequest=GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        progressDialog.dismiss();

                        Log.d("Response:",response.toString());
                        //to get the data custom method
                        getFacebookData(object);
                    }
                });

                Bundle parameters=new Bundle();
                parameters.putString("fields","id,email,birthday,friends");

                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        //if already login
        if(AccessToken.getCurrentAccessToken() != null)
        {
            myEmail.setText(AccessToken.getCurrentAccessToken().getUserId());
        }

    }

    private void getFacebookData(JSONObject object) {
        try {
            URL profile_picture=new URL("https://graph.facebook.com/"+object.getString("id")+"/picture?width=250&height=250");

            //Glide is used to fetch the image and set the image
            Glide.with(this).load(profile_picture.toString()).into(profileImage);

            myEmail.setText(object.getString("email"));
            myBirthday.setText(object.getString("birthday"));
            myFriends.setText("Friends:"+object.getJSONObject("friends").getJSONObject("summary").getString("total_count"));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void printKeyHash() {

            try {

                PackageInfo info=getPackageManager().getPackageInfo("com.technohack.facebooklogin", PackageManager.GET_SIGNATURES);

                for(Signature signature:info.signatures)
                {
                    MessageDigest messageDigest=MessageDigest.getInstance("SHA");
                    messageDigest.update(signature.toByteArray());

                    Log.d("keyhash", Base64.encodeToString(messageDigest.digest(),Base64.DEFAULT));

                }

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode,resultCode,data);

    }
}
