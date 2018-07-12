# LoginWithFacebook
This Demo app will show how to login with facebook and fetch the data like image ,birthday,email and total friends.

Add Dependency at App level Gradle file:


//for setting the image
    implementation 'com.github.bumptech.glide:glide:4.7.1'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.7.1'
//for butterKnife
implementation 'com.jakewharton:butterknife:8.8.1'
annotationProcessor 'com.jakewharton:butterknife-compiler:8.8.1'
// for the facebook sdk
implementation 'com.facebook.android:facebook-android-sdk:4.34.0'

Add Strings
Goto >> Res folder >> values folder>>> string.xml

then add these 2 strings
<string name="facebook_app_id">1726637274121397</string>            
<string name="fb_login_protocol_scheme">fb1726637274121397</string>

you will get app_id and fb_login_protocol_scheme by creating the project on Developer.facebook.com

Add Metadata and permissions in Manifest File:

permission:
  <uses-permission android:name="android.permission.INTERNET"/>
  
Metadata and Activity:

    <meta-data android:name="com.facebook.sdk.ApplicationId"
        android:value="@string/facebook_app_id"/>

    <activity android:name="com.facebook.FacebookActivity"
        android:configChanges=
            "keyboard|keyboardHidden|screenLayout|screenSize|orientation"
        android:label="@string/app_name" />

    <activity
        android:name="com.facebook.CustomTabActivity"
        android:exported="true">
        <intent-filter>
            <action android:name="android.intent.action.VIEW" />
            <category android:name="android.intent.category.DEFAULT" />
            <category android:name="android.intent.category.BROWSABLE" />
            <data android:scheme="@string/fb_login_protocol_scheme" />
        </intent-filter>
    </activity>


Last and most important past You have to generate keyhash value:
1. You need to install openssl in your computer
2. Goto C:\Program Files\Java\jre1.8.0_161\bin using command prompt
3. Goto openssl >>then bin >> copy that path 
path openssl:  E:\Software\openssl\bin\openssl.exe 
4. enter this command>> 
keytool -exportcert -alias androiddebugkey -keystore "C:\Users\"Username_Of_Your_Computer_Or_Laptop"\.android\debug.keystore" | "Paste_OpenSSl_Path" sha1 -binary |"Paste_OpenSSl_Path " base64
5.after this command prompt asked 
Enter KeyStore Password:

![facebook_login_harsh_key](https://user-images.githubusercontent.com/25931598/42662188-4be7ef3c-864e-11e8-86b3-b7883edc3967.PNG)
