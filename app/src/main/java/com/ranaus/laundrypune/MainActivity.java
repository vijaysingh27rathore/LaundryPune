package com.ranaus.laundrypune;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText name,usr,password,confirmPassword,phoneNumber,OTP;
    private Button btnSignUp;
    private TextView login,getOTP;
    public String userOTP,objId,objAPI,phoneCheck;
    boolean value = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = (EditText) findViewById(R.id.name);
        usr = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirm_password);
        phoneNumber = (EditText) findViewById(R.id.phone_number);
        btnSignUp = (Button) findViewById(R.id.btn_signup);
        login = (TextView) findViewById(R.id.btn_signin);
        OTP = (EditText) findViewById(R.id.otp);
        getOTP = (TextView) findViewById(R.id.get_otp);

        CheckAPI();

            getOTP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (value == true)
                    {
                        phoneCheck = phoneNumber.getText().toString();
                        if (phoneCheck.length() == 10)
                        {
                            OTPGenerate();
                            try {
                                OTPGenerate();

                                // Construct data
                                String apiKey = "apikey=" + objAPI;
                                String message = "&message=" + "Your OTP for Laundry Pune is "+userOTP+"  \n\n form RANAUS PVT LTD";
                                String sender = "&sender=" + "TXTLCL";
                                String numbers = "&numbers=" + "91"+phoneNumber.getText().toString();

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
                                    FancyToast.makeText(MainActivity.this,line.toString(),FancyToast.LENGTH_LONG,
                                            FancyToast.SUCCESS,true).show();
                                }
                                rd.close();
                            } catch (Exception e) {
                                FancyToast.makeText(MainActivity.this,e.toString(),FancyToast.LENGTH_LONG,
                                        FancyToast.ERROR,true).show();
                            }

                        }
                        else {
                            FancyToast.makeText(MainActivity.this, "Enter 10 digit Phone Number !!"
                                    , FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                        }
                    }
                    }
            });

            StrictMode.ThreadPolicy st = new StrictMode.ThreadPolicy.Builder().build();
            StrictMode.setThreadPolicy(st);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_signup:

                //UserCheck();
                if (usr.getText().toString().equals("") || name.getText().toString().equals("") || password.getText().toString().equals("")
                        || confirmPassword.getText().toString().equals("") || phoneNumber.getText().toString().equals(""))
                {
                    FancyToast.makeText(this,"Please Fill All Field !!!", FancyToast.LENGTH_SHORT,
                            FancyToast.ERROR,true).show();
                }

                else
                {
                    if (OTP.getText().toString().equals(""))
                    {
                        FancyToast.makeText(this,"Enter OTP", FancyToast.LENGTH_SHORT,
                                FancyToast.ERROR,true).show();
                    }
                    else
                        OTPRetrive();

                }
                break;

            case R.id.btn_signin:
                Intent signInIntent = new Intent(this,UsersLogin.class);
                startActivity(signInIntent);
                finish();
                break;

            case R.id.get_otp:
                 //OTPGenerate();
                break;
        }
    }

    private void UserCheck()
    {
        final String user = usr.getText().toString().toLowerCase();

        ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("AppUsername");
        parseQuery.whereEqualTo("username",user);

        final ProgressDialog dialogUserCheck = new ProgressDialog(this);
        dialogUserCheck.setMessage(" Checking Users...");
        dialogUserCheck.show();
        parseQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (object == null)
                {
                    UserCreation();
                }
                else {
                    FancyToast.makeText(MainActivity.this, "User " + user+" exist !!!"
                            , Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();

                }
                dialogUserCheck.dismiss();
            }
        });
    }

    private void UserCreation()
    {
        String user = usr.getText().toString().toLowerCase();
        ParseObject parseObject = new ParseObject("AppUsername");
        parseObject.put("username",user);
        parseObject.put("fullName",name.getText().toString().toUpperCase());
        parseObject.put("password",password.getText().toString());
        parseObject.put("phoneNumber",phoneNumber.getText().toString());

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing Up...");
        progressDialog.show();

        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null)
                {
                    DeleteOTP();
                    FancyToast.makeText(MainActivity.this, "User Created !!!", Toast.LENGTH_SHORT,
                            FancyToast.INFO, true).show();
                    Intent intent = new Intent(MainActivity.this,Menu.class);
                    startActivity(intent);
                }
                else
                    FancyToast.makeText(MainActivity.this, "Unknown error: "
                            + e.getMessage(), Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                progressDialog.dismiss();
            }
        });
    }

    private void DeleteOTP() {
        final ParseQuery<ParseObject> parseQueryOTPDelete = new ParseQuery<ParseObject>("OTP");
        parseQueryOTPDelete.getInBackground(objId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null)
                {
                    object.deleteInBackground();
                }
            }
        });
    }

    private void OTPGenerate()
    {
        Random random = new Random();
        String id = String.format("%04d",random.nextInt(9999));
        userOTP = id;
        ParseObject parseObjectOTP = new ParseObject("OTP");
        parseObjectOTP.put("OTP",id);

        parseObjectOTP.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null)
                {
                    //Toast.makeText(MainActivity.this, "OTP sent to +91"+phoneNumber.getText().toString(), Toast.LENGTH_SHORT).show();
                    FancyToast.makeText(MainActivity.this, "OTP sent to +91"+phoneNumber.getText().toString()
                            , FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show();
                }
                else
                {
                    FancyToast.makeText(MainActivity.this, "Unknown error: "
                            , FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                }
            }
        });
    }

    private void OTPRetrive()
    {
        String tempOTP = OTP.getText().toString();
        final ParseQuery<ParseObject> parseQueryOTP = new ParseQuery<ParseObject>("OTP");
        parseQueryOTP.whereEqualTo("OTP",tempOTP);

        parseQueryOTP.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null)
                {
                    for (ParseObject otp : objects)
                    {
                        objId = otp.getObjectId();
                        parseQueryOTP.getFirstInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject object, ParseException e) {
                                if (e == null)
                                {
                                    String verityOTP = object.get("OTP")+"";
                                    String enteredOTP = OTP.getText().toString();
                                    if (enteredOTP.equals(verityOTP))
                                    {
                                        UserCheck();
                                        Toast.makeText(MainActivity.this, "Done!!!", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(MainActivity.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                    }
                    if (objects.size() == 0)
                    {
                        FancyToast.makeText(MainActivity.this, "Incorrect OTP"
                                , FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                    }
                }
            }
        });
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
                    String api = objAPI;
                    /*Toast.makeText(MainActivity.this, "API Check", Toast.LENGTH_SHORT).show();*/
                    value = true;
                }
                else
                {

                    FancyToast.makeText(MainActivity.this, "No Internet Connection !!"
                            , FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                }
            }
        });
    }
}
