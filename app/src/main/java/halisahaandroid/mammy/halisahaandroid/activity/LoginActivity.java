package halisahaandroid.mammy.halisahaandroid.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.ButterKnife;
import halisahaandroid.mammy.halisahaandroid.R;

public class LoginActivity extends AppCompatActivity
{

    private static final int REQUEST_SIGNUP = 0;
    EditText userUserNameText;
    EditText userPasswordText;
    Button loginButton;
    TextView signUpLink;
    TextView forgetPasswordLink;
    String resultLogin = "0";
    int resultLoginId = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); // Başlangıçtaki Klavyeyi kapatmak için

        if(isConnected()){
            //Toast.makeText(getBaseContext(), "Connected", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getBaseContext(), "notConnected", Toast.LENGTH_LONG).show();
        }


        userUserNameText = findViewById(R.id.userUsernameText);
        userPasswordText = findViewById(R.id.userPasswordText);
        loginButton = findViewById(R.id.btn_login);
        signUpLink = findViewById(R.id.link_signup);
        forgetPasswordLink = findViewById(R.id.link_forgetpassword);

        signUpLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.animation_totheleft, R.anim.animation_totheright);
            }
        });

        forgetPasswordLink.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), ForgetPasswordActivity.class);
                startActivityForResult(intent, 0);
                finish();
                overridePendingTransition(R.anim.animation_totheleft, R.anim.animation_totheright);
            }

        });

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                whenClickedLoginButton();
            }
        });


    }
        void whenClickedLoginButton(){
            Log.d("LoginActivity", "Login");

            if (!isValuesValid()) {
                onLoginFailed();
                return;
            }

            loginButton.setEnabled(false);

            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Giriş Yapılıyor...");
            progressDialog.show();
            new HttpAsyncTask().execute("http://halisaha.appoint.online/api/user/userOperation2");

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            if(resultLoginId == 0 )
                                onLoginFailed();
                            else
                                onLoginSuccess();

                            progressDialog.dismiss();
                        }
                    }, 3000);
        }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }



    public boolean isValuesValid() {
        boolean isValid = true;

        String email = userUserNameText.getText().toString();
        String password = userPasswordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userUserNameText.setError("Geçersiz e-mail");
            isValid = false;
        } else {
            userUserNameText.setError(null);
        }

        if (password.isEmpty() || password.length() < 8 || password.length() > 16) {
            userPasswordText.setError("Geçersiz şifre");
            isValid = false;
        } else {
            userPasswordText.setError(null);
        }

        return isValid;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialogBuilder= new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme_Dark_Dialog));
        dialogBuilder.setTitle(R.string.warning_message);
        dialogBuilder.setMessage(R.string.exit_message);

        dialogBuilder.setPositiveButton("Evet",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*moveTaskToBack(true);
                System.exit(0);*/
                Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivityForResult(intent, 0);
                overridePendingTransition(R.anim.animation_totheleft, R.anim.animation_totheright);
                finish();
            }
        });

        dialogBuilder.setNegativeButton("Hayır",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogBuilder.show();
    }

    public void onLoginSuccess() {
        String emailConnectedUser="";
        String firstNameConnectedUser="";
        String lastNameConnectedUser="";
        String phoneNumConnectedUser="";
        String bDayConnectedUser="";
        loginButton.setEnabled(true);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(resultLogin);
            emailConnectedUser = jsonObj.getString("EMAIL");
            firstNameConnectedUser = jsonObj.getString("FIRSTNAME");
            lastNameConnectedUser = jsonObj.getString("SURNAME");
            phoneNumConnectedUser = jsonObj.getString("MOBILEPHONE");
            bDayConnectedUser = jsonObj.getString("BIRTHDAY");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("userID", resultLoginId);
        editor.putString("userEmail",emailConnectedUser);
        editor.putString("userFirstName",firstNameConnectedUser);
        editor.putString("userLastName",lastNameConnectedUser);
        editor.putString("userPhoneNum",phoneNumConnectedUser);
        editor.putString("userBDay",bDayConnectedUser);
        editor.commit();
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        startActivityForResult(intent, 0);
        overridePendingTransition(R.anim.animation_totheleft, R.anim.animation_totheright);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        loginButton.setEnabled(true);
    }

    public String POST(String url){
        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("EMAIL", userUserNameText.getText().toString() );
            jsonObject.accumulate("PASSWORD", userPasswordText.getText().toString() );
            jsonObject.accumulate("OPERATION", "LOGIN" );
            json = jsonObject.toString();
            StringEntity se = new StringEntity(json);
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            if(inputStream != null){
                result = convertInputStreamToString(inputStream);
                Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();}
            else{
                result = "Did not work!";
                Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        resultLogin = result;

        try {
            JSONObject jsonObj = new JSONObject(resultLogin);
            resultLoginId = jsonObj.getInt("ID");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;

    }


    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return POST(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }





}
