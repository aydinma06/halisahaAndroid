package halisahaandroid.mammy.halisahaandroid.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


import java.util.regex.Pattern;

import halisahaandroid.mammy.halisahaandroid.R;

public class SignUpActivity extends AppCompatActivity {

    EditText signUpNameText;
    EditText signUpSurnameText;
    EditText signUpEmailText;
    EditText signUpPhoneNumberText;
    EditText signUpBirthDateText;
    EditText signUpPasswordText;
    EditText signUpPasswordAgainText;
    Button signupButton;
    String resultSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        signUpNameText = findViewById(R.id.signUpNameText);
        signUpSurnameText = findViewById(R.id.signUpSurnameText);
        signUpEmailText = findViewById(R.id.signUpEmailText);
        signUpPhoneNumberText = findViewById(R.id.signUpPhoneText);
        signUpBirthDateText = findViewById(R.id.signUpBirthDateText);
        signUpPasswordText = findViewById(R.id.signUpPassword1Text);
        signUpPasswordAgainText = findViewById(R.id.signUpPassword2Text);
        signupButton = findViewById(R.id.btn_signup);



        if(isConnected()){
            //Toast.makeText(getBaseContext(), "Connected", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getBaseContext(), "notConnected", Toast.LENGTH_LONG).show();
        }

        signupButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                whenClickedSignUpButton();
            }
        });

    }

    void whenClickedSignUpButton(){
        Log.d("SignUpActivity", "Sign Up");

        if (!isValuesValid()) {
            onSignUpFailed();
            return;
        }
        new HttpAsyncTask().execute("http://halisaha.appoint.online/api/user/userOperation");
        signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Kayıt Yapılıyor...");//
        progressDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if(resultSignUp.equals("\"unsuccess_sameemail\""))
                            onSignUpFailed();
                        else
                        onSignUpSuccess();

                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public boolean isValuesValid() {
        boolean isValid = true;

        String signUpName = signUpNameText.getText().toString();
        String signUpSurname = signUpSurnameText.getText().toString();
        String signUpEmail = signUpEmailText.getText().toString();
        String signUpPhone = signUpPhoneNumberText.getText().toString();
        String signUpBDate = signUpBirthDateText.getText().toString();
        String signUpPass1 = signUpPasswordText.getText().toString();
        String signUpPass2 = signUpPasswordAgainText.getText().toString();
        Pattern bDatePattern = Pattern.compile("^(0[1-9]|1[012])[-/.](0[1-9]|[12][0-9]|3[01])[-/.](19|20)\\d\\d$" );// dd/mm/yyyy
        Pattern phonePattern = Pattern.compile("^(05)[0-9][0-9][1-9]([0-9]){6}$");

        if (signUpName.isEmpty() || signUpName.length() < 3 || signUpName.length() > 50) {
            signUpNameText.setError("Geçersiz ad");
            isValid = false;
        } else {
            signUpNameText.setError(null);
        }

        if (signUpSurname.isEmpty() || signUpSurname.length() < 2 || signUpSurname.length() > 50) {
            signUpSurnameText.setError("Geçersiz soyad");
            isValid = false;
        } else {
            signUpSurnameText.setError(null);
        }

        if (signUpEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(signUpEmail).matches()) {
            signUpEmailText.setError("Geçersiz e-mail");
            isValid = false;
        } else {
            signUpEmailText.setError(null);
        }

        if (signUpPhone.isEmpty() ||   !phonePattern.matcher(signUpPhone).matches()) {
            signUpPhoneNumberText.setError("Telefon numaranız geçersiz");
            isValid = false;
        } else {
            signUpPhoneNumberText.setError(null);
        }

        if (signUpBDate.isEmpty() || !bDatePattern.matcher(signUpBDate).matches() ) {
            signUpBirthDateText.setError("Geçersiz doğum tarihi formatı");
            isValid = false;
        } else {
            signUpBirthDateText.setError(null);
        }

        if (signUpPass1.isEmpty() || signUpPass1.length() < 8 || signUpPass1.length() > 16) {
            signUpPasswordText.setError("Şifreniz 8 ila 16 hane arasında olmalıdır");
            isValid = false;
        } else {
            signUpPasswordText.setError(null);
        }

        if (signUpPass2.isEmpty() || signUpPass2.length() < 8 || signUpPass2.length() > 16) {
            signUpPasswordAgainText.setError("Şifreniz 8 ila 16 hane arasında olmalıdır");
            isValid = false;
        } else {
            signUpPasswordAgainText.setError(null);
        }

        if (signUpPass1.isEmpty()||signUpPass2.isEmpty()||!signUpPass2.equals(signUpPass1)) {
            signUpPasswordAgainText.setError("Şifreler uyuşmuyor");
            signUpPasswordText.setError("Şifreler uyuşmuyor");
            isValid = false;
        } else {
            signUpPasswordAgainText.setError(null);
            signUpPasswordText.setError(null);
        }

        return isValid;
    }

    public void onSignUpSuccess() {
        signupButton.setEnabled(true);
        Toast.makeText(getBaseContext(), "Kayıt Başarılı", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(intent, 0);
        overridePendingTransition(R.anim.animation_totheleft, R.anim.animation_totheright);
        finish();
    }

    public void onSignUpFailed() {
        Toast.makeText(getBaseContext(), "Kayıt Başarısız", Toast.LENGTH_LONG).show();
        signupButton.setEnabled(true);
    }

    public void onBackPressed() {
        AlertDialog.Builder dialogBuilder= new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme_Dark_Dialog));
        dialogBuilder.setTitle(R.string.warning_message);
        dialogBuilder.setMessage(R.string.exit_message);

        dialogBuilder.setPositiveButton("Evet",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent,0);
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
    public String POST(String url){
        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            //jsonObject.accumulate("COMPANY_ID", 0);
            //jsonObject.accumulate("AGE", 15);
            //jsonObject.accumulate("ADDRESS", "Eryaman");
            jsonObject.accumulate("FIRSTNAME", signUpNameText.getText().toString());
            jsonObject.accumulate("SURNAME", signUpSurnameText.getText().toString() );
            jsonObject.accumulate("EMAIL", signUpEmailText.getText().toString() );
            jsonObject.accumulate("MOBILEPHONE", signUpPhoneNumberText.getText().toString() );
            jsonObject.accumulate("BIRTHDAY", signUpBirthDateText.getText().toString() );
            jsonObject.accumulate("PASSWORD", signUpPasswordText.getText().toString() );
            jsonObject.accumulate("OPERATION", "INSERT" );
            json = jsonObject.toString();
            StringEntity se = new StringEntity(json,"UTF-8");
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
        resultSignUp = result;


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

}
