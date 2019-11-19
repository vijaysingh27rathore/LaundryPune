package com.ranaus.laundrypune;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class UsersLogin extends AppCompatActivity implements View.OnClickListener {
    private EditText username,password;
    private Button btnLogin;
    private TextView forgotPassword,signUP;
    private String objId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login_activity);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        forgotPassword = (TextView) findViewById(R.id.forgot_password);
        signUP = (TextView) findViewById(R.id.btn_signup);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_login:
                signInUserPart();
                break;

            case R.id.forgot_password:

                break;

            case R.id.btn_signup:
                Intent signUpIntent = new Intent(this,MainActivity.class);
                startActivity(signUpIntent);
                finish();
                break;
        }
    }

    private void signInUserPart()
    {
        String user = username.getText().toString().toLowerCase();

        final ParseQuery<ParseObject> userquery = new ParseQuery<ParseObject>("AppUsername");
        userquery.whereEqualTo("username",user);

        final ProgressDialog userdialog = new ProgressDialog(this);
        userdialog.setMessage("Checking User...");
        userdialog.show();

        userquery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null)
                {
                    for (ParseObject user : objects)
                    {
                        objId = user.getObjectId();

                        userquery.getFirstInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject object, ParseException e) {
                                if (e == null)
                                {
                                    String verifyPassword = object.get("password")+"";
                                    String enteredPassword = null;
                                    enteredPassword = password.getText().toString();
                                    if (enteredPassword.equals(verifyPassword))
                                    {
                                        Intent loginIntent = new Intent(UsersLogin.this,Menu.class);
                                        startActivity(loginIntent);
                                        finish();
                                    }
                                    if (!enteredPassword.equals(verifyPassword))
                                    {
                                        FancyToast.makeText(UsersLogin.this, "Incorrect Password !!! "
                                                , Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                                    }
                                }
                            }
                        });
                    }
                }
                userdialog.dismiss();

                if (objects.size() == 0)
                {
                    FancyToast.makeText(UsersLogin.this, "User Not Found !!!"
                            , Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                    Intent intent = new Intent(UsersLogin.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}