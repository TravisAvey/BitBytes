package com.cypherbytes.bitbytes.ui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.cypherbytes.bitbytes.DialogFragments.AlertDialogFragment;
import com.cypherbytes.bitbytes.DialogFragments.SignupDialogFragment;
import com.cypherbytes.bitbytes.DialogFragments.SuccessDialogFragment;
import com.cypherbytes.bitbytes.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class SignUpActivity extends ActionBarActivity
{

    @InjectView(R.id.usernameField) EditText mUsername;
    @InjectView(R.id.passwordField) EditText mPassword;
    @InjectView(R.id.emailField) EditText mEmail;
    @InjectView(R.id.signUpProgressBar) ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.inject(this);

        mProgressBar.setVisibility(View.INVISIBLE);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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

   @OnClick(R.id.signUpButton)
    public void signUpButton(View view)
    {
        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();
        String email = mEmail.getText().toString();

        username = username.trim();
        password = password.trim();
        email = email.trim();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty())
        {
            SignupDialogFragment dialog = new SignupDialogFragment();
            dialog.show(getFragmentManager(), "error_dialog");
        }
        else
        {
            //create new user
            mProgressBar.setVisibility(View.VISIBLE);
            ParseUser user = new ParseUser();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.signUpInBackground(new SignUpCallback()
            {
                @Override
                public void done(ParseException e)
                {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    if (e == null)
                    {
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        // something went wrong
                        AlertDialogFragment dialog = new AlertDialogFragment();
                        dialog.setMessage(e.getMessage());
                        dialog.show(getFragmentManager(), "error_dialog");
                    }
                }
            });
        }
    }
}
