package com.cypherbytes.bitbytes.ui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cypherbytes.bitbytes.DialogFragments.AlertDialogFragment;
import com.cypherbytes.bitbytes.DialogFragments.SignupDialogFragment;
import com.cypherbytes.bitbytes.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class LoginActivity extends ActionBarActivity
{

    @InjectView(R.id.passwordField) TextView mPassword;
    @InjectView(R.id.userIdField) TextView mUsername;
    @InjectView(R.id.loginProgressBar) ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        mProgressBar.setVisibility(View.INVISIBLE);

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.signUpTextView)
    public void startSignUpActivity(View view)
    {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.loginButton)
    public void loginActivity(View view)
    {
        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();

        username = username.trim();
        password = password.trim();

        if (username.isEmpty() || password.isEmpty())
        {
            SignupDialogFragment dialog = new SignupDialogFragment();
            dialog.show(getFragmentManager(), "error_dialog");
        }
        else
        {
           // Login!
            mProgressBar.setVisibility(View.VISIBLE);
            ParseUser.logInInBackground(username, password, new LogInCallback()
            {
                @Override
                public void done(ParseUser user, ParseException e)
                {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    if( e == null)
                    {
                        // success login
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        AlertDialogFragment dialog = new AlertDialogFragment();
                        dialog.setMessage(e.getMessage());
                        dialog.show(getFragmentManager(), "error_dialog");
                    }
                }
            });
        }
    }
}
