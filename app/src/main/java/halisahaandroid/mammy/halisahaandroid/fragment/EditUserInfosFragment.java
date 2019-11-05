package halisahaandroid.mammy.halisahaandroid.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import halisahaandroid.mammy.halisahaandroid.activity.ChangePasswordActivity;


public class EditUserInfosFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    EditText changeNameText;
    EditText changeSurnameText;
    EditText changePhoneText;
    EditText changeBDateText;
    Button changeButton;
    TextView changePasswordLink;
    String resultSignUp;
    int myID;

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_user_infos, container, false);
        final Bundle args = new Bundle();
        final SwipeRefreshLayout swipe = view.findViewById(R.id.swiperefresh2);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                //new HttpAsyncTask().execute("http://halisaha.appoint.online/api/FField_Calendar/GetFootballFields");

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                swipe.setRefreshing(false);
                            }
                        }, 2500);

            }
        });

        changeNameText = view.findViewById(R.id.changeNameText);
        changeSurnameText = view.findViewById(R.id.changeSurnameText);
        changePhoneText = view.findViewById(R.id.changePhoneText);
        changeBDateText = view.findViewById(R.id.changeBirthDateText);
        changeButton = view.findViewById(R.id.btn_change);
        changePasswordLink = view.findViewById(R.id.link_changepassword);

        changePasswordLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivityForResult(intent, 0);
                startActivity(intent);
                //finish();
                //overridePendingTransition(R.anim.animation_totheleft, R.anim.animation_totheright);
            }
        });


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String myName = preferences.getString("userFirstName", "N/A");
        String myLastName = preferences.getString("userLastName", "N/A");
        String myPhone = preferences.getString("userPhoneNum", "N/A");
        String myBDay = preferences.getString("userBDay", "N/A");
        myID = preferences.getInt("userID", 0);
        if(!myBDay.isEmpty()){
        myBDay = myBDay.substring(0,10);}
        changeNameText.setText(myName);
        changeSurnameText.setText(myLastName);
        changePhoneText.setText(myPhone);
        changeBDateText.setText(myBDay);

        changeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                whenClickedChangeUpButton();
            }
        });

        return view;
    }


    void whenClickedChangeUpButton(){


        if (!isValuesValid()) {
            onChangeFailed();
            return;
        }
        new HttpAsyncTask().execute("http://halisaha.appoint.online/api/user/userOperation");
        changeButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Değişiklikler Yapılıyor...");//
        progressDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        //if(resultSignUp.equals("\"unsuccess_sameemail\""))
                           // onChangeFailed();
                        //else
                            onChangeSuccess();

                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public void onChangeSuccess() {
        changeButton.setEnabled(true);
        Toast.makeText(getContext(), "Değişiklikler Başarılı Bir Şekilde Gerçekleştirildi", Toast.LENGTH_LONG).show();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userFirstName",changeNameText.getText().toString());
        editor.putString("userLastName",changeSurnameText.getText().toString());
        editor.putString("userPhoneNum",changePhoneText.getText().toString());
        editor.putString("userBDay",changeBDateText.getText().toString());
        editor.commit();
        //Intent intent = new Intent(getContext(), MainMenuActivity.class);
        //startActivityForResult(intent, 0);
        //startActivity(intent);
        //overridePendingTransition(R.anim.animation_totheleft, R.anim.animation_totheright);
        //finish();
    }

    public void onChangeFailed() {
        Toast.makeText(getContext(), "Kayıt Başarısız", Toast.LENGTH_LONG).show();
        changeButton.setEnabled(true);
    }

    public boolean isValuesValid() {
        boolean isValid = true;

        String Name = changeNameText.getText().toString();
        String Surname = changeSurnameText.getText().toString();
        String Phone = changePhoneText.getText().toString();
        String BDate = changeBDateText.getText().toString();

        Pattern bDatePattern = Pattern.compile("^(19|20)\\d\\d[-/.](0[1-9]|1[012])[-/.](0[1-9]|[12][0-9]|3[01])$" );// dd/mm/yyyy (0[1-9]|1[012]) ; mm
        Pattern phonePattern = Pattern.compile("^(05)[0-9][0-9][1-9]([0-9]){6}$");                                  //(19|20)\d\d ; yy     (0[1-9]|[12][0-9]|3[01]) ; dd

        if (Name.isEmpty() || Name.length() < 3 || Name.length() > 50) {
            changeNameText.setError("Geçersiz ad");
            isValid = false;
        } else {
            changeNameText.setError(null);
        }

        if (Surname.isEmpty() || Surname.length() < 2 || Surname.length() > 50) {
            changeSurnameText.setError("Geçersiz soyad");
            isValid = false;
        } else {
            changeSurnameText.setError(null);
        }

        if (Phone.isEmpty() ||   !phonePattern.matcher(Phone).matches()) {
            changePhoneText.setError("Telefon numaranız geçersiz");
            isValid = false;
        } else {
            changePhoneText.setError(null);
        }

        if (BDate.isEmpty() || !bDatePattern.matcher(BDate).matches() ) {
            changeBDateText.setError("Geçersiz doğum tarihi formatı");
            isValid = false;
        } else {
            changeBDateText.setError(null);
        }


        return isValid;
    }


    public String POST(String url){
        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("ID", myID );
            jsonObject.accumulate("FIRSTNAME", changeNameText.getText().toString() );
            jsonObject.accumulate("SURNAME", changeSurnameText.getText().toString() );
            jsonObject.accumulate("MOBILEPHONE", changePhoneText.getText().toString() );
            jsonObject.accumulate("BIRTHDAY", changeBDateText.getText().toString() );
            jsonObject.accumulate("OPERATION", "UPDATE" );
            json = jsonObject.toString();
            StringEntity se = new StringEntity(json,"UTF-8");
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            if(inputStream != null){
                result = convertInputStreamToString(inputStream);
                Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();}
            else{
                result = "Did not work!";
                Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        resultSignUp = result;

        return result;

    }

    @Override
    public void onRefresh() {

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
