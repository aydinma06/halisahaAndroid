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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import halisahaandroid.mammy.halisahaandroid.http.HttpHandler;
import halisahaandroid.mammy.halisahaandroid.R;
import halisahaandroid.mammy.halisahaandroid.adapter.appointmentAdapter;
import halisahaandroid.mammy.halisahaandroid.model.appointmentInfo;


public class AppointmentFragment extends Fragment implements appointmentAdapter.ListItemClickListener, SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView rAppointmentList;
    ArrayList<appointmentInfo> appointments;
    private static final int NUM_LIST_ITEMS = 100;
    private appointmentAdapter myAdapter;
    String resultCalendar;
    private Toast mToast;
    private Bundle b;
    private String startDate;
    private String resultRequest;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);
        final SwipeRefreshLayout swipe = view.findViewById(R.id.swiperefresh);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                //new HttpAsyncTask().execute("http://halisaha.appoint.online/api/FField_Calendar/GetFootballFields");

                new getAppointmentInfos().execute();
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                swipe.setRefreshing(false);
                            }
                        }, 2500);

            }
        });


        rAppointmentList = view.findViewById(R.id.rv_appointment);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rAppointmentList.setLayoutManager(layoutManager);
        rAppointmentList.setHasFixedSize(true);
        myAdapter = new appointmentAdapter(NUM_LIST_ITEMS, this);
        rAppointmentList.setAdapter(myAdapter);

        new getAppointmentInfos().execute();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sorgunuza Göre Halı Sahalar Hazırlanıyor...");
        progressDialog.show();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 1000);
        return view;
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
                myAdapter = new appointmentAdapter(NUM_LIST_ITEMS, this);
                rAppointmentList.setAdapter(myAdapter);
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

    } private class getAppointmentInfos extends AsyncTask<Void, Void, Void> implements appointmentAdapter.ListItemClickListener {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(RecyclerView.this, "Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            int myID = preferences.getInt("userID",0);
            HttpHandler sh = new HttpHandler();
            String url = "http://halisaha.appoint.online/api/Actions/GetUserActions?param="+myID;
            String jsonStr = sh.makeServiceCall(url);
            appointments = new ArrayList<appointmentInfo>();

            if (jsonStr != null) {
                try {
                    JSONArray jsonarray = new JSONArray(jsonStr);

                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject c = jsonarray.getJSONObject(i);
                        String fieldID = c.getString("COMPANYNAME");
                        int actionType = c.getInt("ACTION_TYPE");
                        String startDate = c.getString("STARTDATE");
                        String finishDate = c.getString("FINISHDATE");
                        int footballFieldID = c.getInt("FOOTBALL_FIELD_ID");
                        int calendarID = c.getInt("CALENDAR_ID");
                        startDate=startDate.substring(0,10) + " "+startDate.substring(11,16);
                        finishDate=finishDate.substring(11,16);
                        appointments.add(new appointmentInfo(fieldID, actionType,startDate,finishDate,footballFieldID,calendarID));
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

            myAdapter = new appointmentAdapter(8, this);
            myAdapter.setAppointmentInfo(appointments);
            rAppointmentList.setAdapter(myAdapter);
        }




        @Override
        public void onListItemClick(int clickedItemIndex) {
            if (mToast != null) {
                mToast.cancel();
            }

            appointmentInfo choosenAppoinment = appointments.get(clickedItemIndex);
            String choosenDate = choosenAppoinment.getFieldID()+" " + choosenAppoinment.getStartDate() + "-" + choosenAppoinment.getFinishDate();
            startDate = choosenAppoinment.getfieldName();
            String toastMessage = "Calendar2 " + choosenDate;
            //mToast = Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_LONG);
//
            //mToast.show();
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_DeviceDefault_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(getContext());
            }
            builder.setTitle("Randevu İptal İsteği")
                    .setMessage(choosenDate +"saati için randevunuzu iptal etmek istediğinizden emin misiniz ?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //new FieldCalendarFragment.HttpAsyncTask2().execute("http://halisaha.appoint.online/api/Calendar/CreateNewCalendar");
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
