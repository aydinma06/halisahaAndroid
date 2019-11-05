package halisahaandroid.mammy.halisahaandroid.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import halisahaandroid.mammy.halisahaandroid.R;
import halisahaandroid.mammy.halisahaandroid.adapter.rViewAdapter;
import halisahaandroid.mammy.halisahaandroid.model.fieldInfo;

public class ResultChooseFieldFragment extends Fragment implements rViewAdapter.ListItemClickListener{
    private static final int NUM_LIST_ITEMS = 100;
    private rViewAdapter myAdapter;
    private RecyclerView rFieldList;
    ArrayList<fieldInfo> fields;
    String resultChooseField;
    private Toast mToast;
    private Bundle b;
    public static Fragment fragment;
    Class fragmentClass;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result_choose_field, container, false);


        rFieldList = view.findViewById(R.id.rv_fields);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rFieldList.setLayoutManager(layoutManager);
        rFieldList.setHasFixedSize(true);
        myAdapter = new rViewAdapter(NUM_LIST_ITEMS, this);
        rFieldList.setAdapter(myAdapter);
        new HttpAsyncTask().execute("http://halisaha.appoint.online/api/Compapineses_FFields/GetFootballFields");
        final ProgressDialog progressDialog = new ProgressDialog(getActivity() ,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sorgunuza Göre Halı Sahalar Hazırlanıyor...");
        progressDialog.show();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 1000);
        new getFieldInfos().execute();
        return view;
    }

    public String POST(String url){
        InputStream inputStream = null;
        String result = "";
        try {
            //SharedPreferences preferences = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
            b = getArguments();
            int cityID = b.getInt("city_id");
            int townID = b.getInt("town_id");
            //int cityID = preferences.getInt("cityID", -1);
            //int townID = preferences.getInt("townID", -int cityID = b.getInt("city_id");1);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("CITY_ID", cityID );
            jsonObject.accumulate("DISTRICT_ID",townID);
            jsonObject.accumulate("OPERATION", "2" );
            json = jsonObject.toString();
            StringEntity se = new StringEntity(json);
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            if(inputStream != null){
                result = convertInputStreamToString(inputStream);
                //makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
            }
            else{
                result = "Did not work!";
                //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        resultChooseField = result;


        return result;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId) {
            /*
             * When you click the reset menu item, we want to start all over
             * and display the pretty gradient again. There are a few similar
             * ways of doing this, with this one being the simplest of those
             * ways. (in our humble opinion)
             */
            case R.id.action_refresh:
                // COMPLETED (14) Pass in this as the ListItemClickListener to the GreenAdapter constructor
                myAdapter = new rViewAdapter(NUM_LIST_ITEMS, this);
                rFieldList.setAdapter(myAdapter);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        if (mToast != null) {
            mToast.cancel();}
        String toastMessage = "Item #" + clickedItemIndex + " clicked.";
        mToast = Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_LONG);

        mToast.show();

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


    private class getFieldInfos extends AsyncTask<Void, Void, Void> implements rViewAdapter.ListItemClickListener {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(RecyclerView.this, "Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            String jsonStr = resultChooseField;
            fields = new ArrayList<fieldInfo>();

            if (jsonStr != null) {
                try {
                    JSONArray jsonarray = new JSONArray(jsonStr);

                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject c = jsonarray.getJSONObject(i);
                        int fieldID = c.getInt("FF_ID");
                        String fieldName = c.getString("NAME");
                        String fieldAdress = c.getString("ADDRESS");
                        String fieldPhone = c.getString("PHONENUMBER");
                        fields.add(new fieldInfo(fieldName, fieldAdress, fieldID,fieldPhone));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            myAdapter = new rViewAdapter(8, this);
            myAdapter.setUserInfo(fields);
            rFieldList.setAdapter(myAdapter);
        }

        @Override
        public void onListItemClick(int clickedItemIndex) {
            if (mToast != null) {
                mToast.cancel();
            }
            String choosendate = b.getString("choosenDate");
            fieldInfo choosenField = fields.get(clickedItemIndex);
            //String toastMessage = choosenField.getfieldName() + " adlı halısahayı " + choosendate + " tarihi için seçtiniz. " + clickedItemIndex;
            //mToast = Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_LONG);

            //mToast.show();
            Bundle args = new Bundle();
            args.putInt("fieldID",choosenField.getFieldID());
            args.putString("choosenDate",choosendate);

            fragmentClass = FieldCalendarFragment.class;

            try {
                fragment = (Fragment) fragmentClass.newInstance();
                fragment.setArguments(args);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (fragment != null) {
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();

                fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(R.id.frameLayout, fragment).commit();

            }

        }
    }

}
