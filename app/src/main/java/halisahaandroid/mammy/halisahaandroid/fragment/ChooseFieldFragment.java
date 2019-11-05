package halisahaandroid.mammy.halisahaandroid.fragment;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import halisahaandroid.mammy.halisahaandroid.http.HttpHandler;
import halisahaandroid.mammy.halisahaandroid.R;
import halisahaandroid.mammy.halisahaandroid.model.cityInfo;


public class ChooseFieldFragment extends Fragment {
    List<String> cities;
    List<String> towns;
    List<String> townsId;
    EditText chooseDate;
    MaterialSpinner chooseTownSpinner;
    MaterialSpinner chooseProvinceSpinner;
    String choosenProvince = "1";
    int intChoosenProvince = 0;
    String urlTown;
    String myDate="";
    Class fragmentClass;
    Calendar myCalendar;
    Bundle args;
    public static Fragment fragment;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choose_field, container, false);
        args = new Bundle();
        chooseTownSpinner = view.findViewById(R.id.spinner2);
        chooseProvinceSpinner = (MaterialSpinner) view.findViewById(R.id.spinner);
        chooseDate = view.findViewById(R.id.chooseDateText);
        SharedPreferences preferences =  this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity() ,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Program Hazırlanıyor...");
        progressDialog.show();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 1000);
        new GetContacts().execute();


        chooseProvinceSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Seçilen İl : " + item + "  "+position, Snackbar.LENGTH_LONG).show();
                choosenProvince = Integer.toString(position);
                intChoosenProvince = position;
                urlTown = "http://halisaha.appoint.online/api/District/GetDistricts?param="+position;
                final ProgressDialog progressDialog = new ProgressDialog(getActivity() ,R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("İlçeler Hazırlanıyor...");
                progressDialog.show();
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                            }
                        }, 1000);

                    new GetTowns().execute();


                args.putInt("city_id",position);
                //Bundle args = new Bundle();
                //args.putInt("doctor_id",position);
                //ResultChooseFieldFragment newResultChooseFieldFragment = new ResultChooseFieldFragment();
                //newResultChooseFieldFragment.setArguments(args);
                //editor.putInt("cityID", position);
//              startActivityForResult(intent)

            }
        });




        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        chooseDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        chooseTownSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Seçilen ilçe : " + item +"  "+ townsId.get(position), Snackbar.LENGTH_LONG).show();
                editor.putInt("townID", Integer.parseInt(townsId.get(position)));
                args.putInt("town_id", Integer.parseInt(townsId.get(position)));
            }
        });

        Button queryButton = view.findViewById(R.id.btn_query);

        queryButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {   if(intChoosenProvince != 0 && !myDate.equals("")) {


                fragmentClass = ResultChooseFieldFragment.class;

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
            else{Toast.makeText(getContext(), "Geçerli bir il ve tarih seçtiğinizden emin olun. ", Toast.LENGTH_LONG).show();}

            }
        });



        return view;
    }
    private void updateLabel() {
        String myFormat = "MM-dd-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        chooseDate.setText(sdf.format(myCalendar.getTime()));
        args.putString("choosenDate", String.valueOf(chooseDate.getText()));
        myDate = String.valueOf(chooseDate.getText());
    }

    private class GetContacts extends AsyncTask<Void, Void, ArrayList<cityInfo>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(RecyclerView.this, "Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected ArrayList<cityInfo> doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url = "http://ojs.okmport.com/api/city/getcityes";
            String jsonStr = sh.makeServiceCall(url);
            ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
            cities = new ArrayList<>();

            if (jsonStr != null) {
                try {
                    JSONArray jsonarray = new JSONArray(jsonStr);

                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject c = jsonarray.getJSONObject(i);
                        String ID = c.getString("ID");
                        String ILADI = c.getString("ILADI");
                        cities.add(ILADI);
                    }
                } catch (final JSONException e) {
                }

            }


            //chooseProvinceSpinner.setItems(cities);
            return null;
        }
        protected void onPostExecute(ArrayList<cityInfo> result) {
            super.onPostExecute(result);chooseProvinceSpinner.setItems(cities);
        }
    }

    private class GetTowns extends AsyncTask<Void, Void, ArrayList<cityInfo>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(RecyclerView.this, "Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected ArrayList<cityInfo> doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url = urlTown;
            String jsonStr = sh.makeServiceCall(url);
            towns = new ArrayList<>();
            townsId = new ArrayList<>();

            if (jsonStr != null) {
                try {
                    JSONArray jsonarray = new JSONArray(jsonStr);

                    for (int i = 0; i < jsonarray.length(); i++) {

                        JSONObject c = jsonarray.getJSONObject(i);
                        String ID = c.getString("ID");
                        String ilceADI = c.getString("ILCENAME");
                        towns.add(ilceADI);
                        townsId.add(ID);

                    }
                } catch (final JSONException e) {
                }

            }


            return null;
        }
        protected void onPostExecute(ArrayList<cityInfo> result) {
            super.onPostExecute(result);
            chooseTownSpinner.setItems(towns);
        }
    }
}
