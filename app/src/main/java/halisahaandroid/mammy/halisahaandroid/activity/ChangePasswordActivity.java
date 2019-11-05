package halisahaandroid.mammy.halisahaandroid.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
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

import halisahaandroid.mammy.halisahaandroid.R;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText oldPasswordText;
    EditText newPasswordText1;
    EditText newPasswordText2;
    Button changePasswordButton;
    String resultChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        oldPasswordText = findViewById(R.id.oldPasswordText);
        newPasswordText1 = findViewById(R.id.newPasswordText1);
        newPasswordText2 = findViewById(R.id.newPasswordText2);
        changePasswordButton = findViewById(R.id.btn_changePassword);

        if(isConnected()){
            //Toast.makeText(getBaseContext(), "Connected", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getBaseContext(), "notConnected", Toast.LENGTH_LONG).show();
        }

        changePasswordButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                whenClickedChangePasswordButton();
            }
        });

    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialogBuilder= new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme_Dark_Dialog));
        dialogBuilder.setTitle(R.string.warning_message);
        dialogBuilder.setMessage(R.string.logout_message);

        dialogBuilder.setPositiveButton("Evet",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivityForResult(intent,7898);
                overridePendingTransition(R.anim.animation_totheleft, R.anim.animation_totheright);
                //finish();
            }
        });

        dialogBuilder.setNegativeButton("Hayır",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogBuilder.show();
    }

    void whenClickedChangePasswordButton()
    {
        if (!isValuesValid()) {
            onFailed();
            return;
        }

        changePasswordButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(ChangePasswordActivity.this,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Şifre Değiştiriliyor...");
        progressDialog.show();
        new HttpAsyncTask().execute("http://halisaha.appoint.online/api/user/userOperation");
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public boolean isValuesValid() {
        boolean isValid = true;

        String oldPassword = oldPasswordText.getText().toString();
        String newPassword1 = newPasswordText1.getText().toString();
        String newPassword2 = newPasswordText2.getText().toString();

        if (oldPassword.isEmpty() ||  oldPassword.length() < 8 || oldPassword.length() > 16) { // Eski şifrenin eşitliğini kontrol et
            oldPasswordText.setError("Eski şifreniz hatalı");
            isValid = false;
        } else {
            oldPasswordText.setError(null);
        }

        if (newPassword1.isEmpty() ) { // Eski şifrenin eşitliğini kontrol et
            newPasswordText1.setError("Yeni şifre kısmı boş bırakılamaz");
            isValid = false;
        } else {
            newPasswordText1.setError(null);
        }

        if (newPassword1.length() < 8 || newPassword1.length() > 16) { // Eski şifrenin eşitliğini kontrol et
            newPasswordText1.setError("Yeni şifrenizin uzunluğu 8 ila 16 hane arasında olmalıdır");
            isValid = false;
        } else {
            newPasswordText1.setError(null);
        }

        if (newPassword2.isEmpty() ) { // Eski şifrenin eşitliğini kontrol et
            newPasswordText2.setError("Yeni şifre kısmı boş bırakılamaz");
            isValid = false;
        } else {
            newPasswordText2.setError(null);
        }

        if (newPassword1.length() < 8 || newPassword1.length() > 16) { // Eski şifrenin eşitliğini kontrol et
            newPasswordText1.setError("Yeni şifrenizin uzunluğu 8 ila 16 hane arasında olmalıdır");
            isValid = false;
        } else {
            if(!newPassword1.equals(newPassword2))
            {
                newPasswordText1.setError("Şifreler Uyuşmuyor");
                newPasswordText2.setError("Şifreler Uyuşmuyor");
                isValid = false;
            }
            else {
            newPasswordText1.setError(null);
            }

        }


        return isValid;
    }

    public void onSuccess() {
        changePasswordButton.setEnabled(true);
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        startActivityForResult(intent, 0);
        overridePendingTransition(R.anim.animation_totheleft, R.anim.animation_totheright);
        finish();
    }

    public void onFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        changePasswordButton.setEnabled(true);
    }

    public String POST(String url){
        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            int userID = preferences.getInt("userID",0);
            jsonObject.accumulate("ID", userID );
            jsonObject.accumulate("PASSWORD", newPasswordText1.getText().toString() );
            jsonObject.accumulate("OPERATION", "UPDATE" );
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
        resultChange = result;


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
