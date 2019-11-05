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

import halisahaandroid.mammy.halisahaandroid.R;

public class ForgetPasswordActivity extends AppCompatActivity {

    Button takePasswordButton;
    EditText takePasswordEmailText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        takePasswordButton = findViewById(R.id.btn_takePassword);
        takePasswordEmailText = findViewById(R.id.userEmailText);


        takePasswordButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                whenClickedTakePasswordButtonButton();
            }
        });
    }

    @Override
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

    void whenClickedTakePasswordButtonButton()
    {

        if (!isValuesValid()) {
            onFailed();
            return;
        }

        takePasswordButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(ForgetPasswordActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("E-mail Gönderiliyor...");
        progressDialog.show();
        new HttpAsyncTask().execute("http://halisaha.appoint.online/api/Email/SendEmail");
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onSuccess();
                        //onFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    boolean isValuesValid() {
        boolean valid = true;

        String email = takePasswordEmailText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            takePasswordEmailText.setError("Geçerli bir e-mail adresi giriniz");
            valid = false;
        } else {
            takePasswordEmailText.setError(null);
        }
        return valid;
    }

    public void onSuccess() {
        takePasswordButton.setEnabled(true);
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        startActivityForResult(intent, 0);
        overridePendingTransition(R.anim.animation_totheleft, R.anim.animation_totheright);
        finish();
    }

    public void onFailed() {
        Toast.makeText(getBaseContext(), "Take Password failed", Toast.LENGTH_LONG).show();

        takePasswordButton.setEnabled(true);
    }



    public String POST(String url){
        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("EMAIL", takePasswordEmailText.getText().toString() );
            jsonObject.accumulate("TYPE", 1 );
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
