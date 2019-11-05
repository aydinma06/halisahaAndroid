package halisahaandroid.mammy.halisahaandroid.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import halisahaandroid.mammy.halisahaandroid.adapter.calendarAdapter;
import halisahaandroid.mammy.halisahaandroid.model.calendarInfo;

public class FieldCalendarFragment extends Fragment implements calendarAdapter.ListItemClickListener, SwipeRefreshLayout.OnRefreshListener{

    private static final int NUM_LIST_ITEMS = 100;
    private calendarAdapter myAdapter;
    private RecyclerView rCalendarList;
    ArrayList<calendarInfo> calendar;
    String resultCalendar;
    private Toast mToast;
    private Bundle b;
    private String startDate;
    private String resultRequest;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_field_calendar, container, false);
        final SwipeRefreshLayout swipe = view.findViewById(R.id.swiperefresh);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                new HttpAsyncTask().execute("http://halisaha.appoint.online/api/FField_Calendar/GetFootballFields");

                new getFieldInfos().execute();
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                swipe.setRefreshing(false);
                            }
                        }, 2500);

            }
        });


        rCalendarList = view.findViewById(R.id.rv_calendar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rCalendarList.setLayoutManager(layoutManager);
        rCalendarList.setHasFixedSize(true);
        myAdapter = new calendarAdapter(NUM_LIST_ITEMS, this);
        rCalendarList.setAdapter(myAdapter);
        new HttpAsyncTask().execute("http://halisaha.appoint.online/api/FField_Calendar/GetFootballFields");

        new getFieldInfos().execute();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sorgunuza Göre Takvim Hazırlanıyor...");
        progressDialog.show();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 3000);
        return view;
    }

    public String POST(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            Bundle b = new Bundle();
            b = getArguments();
            int fieldID = b.getInt("fieldID");
            String startDate = b.getString("choosenDate");
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("FIELD_ID", fieldID);
            jsonObject.accumulate("STARTDATE", startDate);
            json = jsonObject.toString();
            StringEntity se = new StringEntity(json);
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
                //makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
            } else {
                result = "Did not work!";
                //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        resultCalendar = result;


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
                myAdapter = new calendarAdapter(NUM_LIST_ITEMS, this);
                rCalendarList.setAdapter(myAdapter);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        if (mToast != null) {
            mToast.cancel();
        }
        //String choosendate = b.getString("choosenDate");
        //calendarInfo choosenField = calendar.get(clickedItemIndex);
        String toastMessage = "Calendar";
        mToast = Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_LONG);

        mToast.show();

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
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }


    public String requestCalendar(String url){
        InputStream inputStream = null;
        String result = "";
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            int myID = preferences.getInt("userID", 0);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            b = getArguments();
            String startDate2 = b.getString("choosenDate");
            String fullStartDate = startDate2+" "+startDate+":00.000";
            int fieldID = b.getInt("fieldID");
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("ACTIVE", myID );
            jsonObject.accumulate("STARTDATE", fullStartDate );
            jsonObject.accumulate("FIELD_ID", fieldID );
            jsonObject.accumulate("ACTION_TYPE", 2);
            json = jsonObject.toString();
            StringEntity se = new StringEntity(json);
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            if(inputStream != null){
                result = convertInputStreamToString(inputStream);
                //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
                }
            else{
                result = "Did not work!";
                //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        resultRequest = result;


        return result;

    }

    private class HttpAsyncTask2 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return requestCalendar(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
        }
    }



    private class getFieldInfos extends AsyncTask<Void, Void, Void> implements calendarAdapter.ListItemClickListener {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(RecyclerView.this, "Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            String jsonStr = resultCalendar;
            calendar = new ArrayList<calendarInfo>();

            if (jsonStr != null) {
                try {
                    JSONArray jsonarray = new JSONArray(jsonStr);

                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject c = jsonarray.getJSONObject(i);
                        int fieldID = c.getInt("FIELD_ID");
                        String startDate = c.getString("STARTDATE");
                        startDate = startDate.substring(11,16);
                        String finishDate = c.getString("FINISHDATE");
                        int actionType = c.getInt("ACTION_TYPE");
                        finishDate = finishDate.substring(11,16);
                        calendar.add(new calendarInfo(startDate, finishDate, fieldID,actionType));
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

            myAdapter = new calendarAdapter(8, this);
            myAdapter.setCalendarInfo(calendar);
            rCalendarList.setAdapter(myAdapter);
        }

        @Override
        public void onListItemClick(int clickedItemIndex) {
            if (mToast != null) {
                mToast.cancel();
            }

            calendarInfo choosenCalendar = calendar.get(clickedItemIndex);
            String choosenDate = choosenCalendar.getfieldName() + "-" + choosenCalendar.getfieldAddress();
            startDate = choosenCalendar.getfieldName();
            String toastMessage = "Calendar2 " + choosenDate;
            mToast = Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_LONG);

            mToast.show();
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_DeviceDefault_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(getContext());
            }
            builder.setTitle("Randevu Alma İsteği")
                    .setMessage(choosenDate +"saati için randevu başvurusunda bulunmak istediğinizden emin misiniz ?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            new HttpAsyncTask2().execute("http://halisaha.appoint.online/api/Calendar/CreateNewCalendar");
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }

    }

}
