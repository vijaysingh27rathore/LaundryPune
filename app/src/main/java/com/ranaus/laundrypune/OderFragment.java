package com.ranaus.laundrypune;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class OderFragment extends Fragment {

    Spinner spinnerHr,spinnerMin,spinnerAm;
    String selectedHr,selectedMin,selectedAm;
    EditText name,address,phone,extra;
    String API;
    Button btnSubmit;
    String objAPI,phoneCheck;
    boolean value = false;

    public OderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        CheckAPI();
        View view = inflater.inflate(R.layout.fragment_oder, container, false);

        spinnerHr = view.findViewById(R.id.spinner_hr);
        spinnerMin = view.findViewById(R.id.spinner_min);
        spinnerAm = view.findViewById(R.id.spinner_am);
        name = view.findViewById(R.id.name);
        address = view.findViewById(R.id.address);
        phone = view.findViewById(R.id.phone_number);
        extra = view.findViewById(R.id.extra);

        btnSubmit = view.findViewById(R.id.btn_signup);

        ArrayAdapter<String> arrayAdapterHr = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.hour_list));
        arrayAdapterHr.setDropDownViewResource(R.layout.color_white);
        spinnerHr.setAdapter(arrayAdapterHr);

        ArrayAdapter<String> arrayAdapterMin = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.minute_list));
        arrayAdapterMin.setDropDownViewResource(R.layout.color_white);
        spinnerMin.setAdapter(arrayAdapterMin);

        ArrayAdapter<String> arrayAdapterAm = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.am_pm));
        arrayAdapterAm.setDropDownViewResource(R.layout.color_white);
        spinnerAm.setAdapter(arrayAdapterAm);

        spinnerHr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedHr = spinnerHr.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerMin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMin = spinnerMin.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerAm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAm = spinnerAm.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().equals("") || address.getText().toString().equals("") ||
                phone.getText().toString().equals(""))
                {
                    Toast.makeText(getContext(), "Please Complete all Field !!!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (value == true)
                    {
                        phoneCheck = phone.getText().toString();
                        if (phoneCheck.length() == 10)
                        {
                            try {

                                UserOrderPlace();

                                // Construct data
                                String apiKey = "apikey=" + objAPI;
                                String message = "&message=" + name.getText().toString()+" with address "+address.getText().toString()
                                        +" and contact number is "+phone.getText().toString()+" kindly contact him/her on scheduled time " +
                                        selectedHr+" : " +selectedMin+" "+selectedAm;
                                String sender = "&sender=" + "TXTLCL";
                                String numbers = "&numbers=" + "919765076749";

                                // Send data
                                HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
                                String data = apiKey + numbers + message + sender;
                                conn.setDoOutput(true);
                                conn.setRequestMethod("POST");
                                conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
                                conn.getOutputStream().write(data.getBytes("UTF-8"));
                                final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                final StringBuffer stringBuffer = new StringBuffer();
                                String line;
                                while ((line = rd.readLine()) != null) {
                                    FancyToast.makeText(getContext(),line.toString(),FancyToast.LENGTH_LONG,
                                            FancyToast.SUCCESS,true).show();
                                }
                                rd.close();
                            } catch (Exception e) {
                                FancyToast.makeText(getContext(),e.toString(),FancyToast.LENGTH_LONG,
                                        FancyToast.ERROR,true).show();
                            }

                        }
                        else {
                            FancyToast.makeText(getContext(), "Enter 10 digit Phone Number !!"
                                    , FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                        }
                    }
                }
                //UserOrderPlace();
            }
        });
        StrictMode.ThreadPolicy st = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(st);

        return view;
    }

    private void UserOrderPlace()
    {
        ParseObject parseObject = new ParseObject("AppUserRequest");
        parseObject.put("Name",name.getText().toString());
        parseObject.put("Address",address.getText().toString());
        parseObject.put("PhoneNumber",phone.getText().toString());
        parseObject.put("Extra",extra.getText().toString());
        parseObject.put("Hr",selectedHr);
        parseObject.put("Min",selectedMin);
        parseObject.put("AmPm",selectedAm);
        parseObject.put("status","active");

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Saving, Please Wait...");
        progressDialog.show();
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null)
                {
                    Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                    name.setText("");
                    address.setText("");
                    phone.setText("");
                    extra.setText("");
                }
                else
                {
                    Toast.makeText(getContext(), "Error!!!", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    private void UserInfoRetrive()
    {
        ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("AppUsername");
        parseQuery.whereEqualTo("username","");
    }

    protected void CheckAPI()
    {
        ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("JaiAPI");
        parseQuery.getInBackground("sEmxK51K0L", new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null)
                {
                    objAPI = object.get("API")+"";
                    value = true;
                }
                else
                {

                    FancyToast.makeText(getContext(), "No Internet Connection !!"
                            , FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                }
            }
        });
    }

}
