package com.cypherbytes.bitbytes.Social;

import android.app.ListActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import com.cypherbytes.bitbytes.DialogFragments.AlertDialogFragment;
import com.cypherbytes.bitbytes.R;
import com.cypherbytes.bitbytes.constants.ParseConstants;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class EditFriendsActivity extends ListActivity
{
    @InjectView(R.id.friendsProgressBar) ProgressBar mProgressBar;
    protected List<ParseUser> mUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friends);
        ButterKnife.inject(this);

        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        mProgressBar.setVisibility(View.VISIBLE);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.orderByAscending(ParseConstants.KEY_USERNAME);
        query.setLimit(1000);
        query.findInBackground(new FindCallback<ParseUser>()
        {
            @Override
            public void done(List<ParseUser> users, ParseException e)
            {
                mProgressBar.setVisibility(View.INVISIBLE);
                if(e==null)
                {
                    // yay success
                    mUsers = users;
                    String[] usernames = new String[mUsers.size()];
                    int i=0;
                    for(ParseUser user : mUsers)
                    {
                        usernames[i] = user.getUsername();
                        i++;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditFriendsActivity.this, android.R.layout.simple_list_item_checked, usernames);
                    setListAdapter(adapter);
                } else {
                    // something went wrong
                    AlertDialogFragment dialog = new AlertDialogFragment();
                    dialog.setMessage(e.getMessage());
                    dialog.show(getFragmentManager(), "error_dialog");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_friends, menu);
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
}
