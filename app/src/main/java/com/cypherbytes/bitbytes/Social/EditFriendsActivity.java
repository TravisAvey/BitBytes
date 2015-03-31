package com.cypherbytes.bitbytes.Social;

import android.app.ListActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.cypherbytes.bitbytes.DialogFragments.AlertDialogFragment;
import com.cypherbytes.bitbytes.R;
import com.cypherbytes.bitbytes.constants.ParseConstants;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class EditFriendsActivity extends ListActivity
{
    @InjectView(R.id.friendsProgressBar) ProgressBar mProgressBar;
    protected List<ParseUser> mUsers;

    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friends);
        ButterKnife.inject(this);

        mProgressBar.setVisibility(View.INVISIBLE);

        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    @Override
    protected void onResume()
    {
        super.onResume();



        mProgressBar.setVisibility(View.VISIBLE);

        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);

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

                    addFriendCheckmarks();
                } else {
                    // something went wrong
                    Log.e("onResume", "at line 88", e);
                    AlertDialogFragment dialog = new AlertDialogFragment();
                    dialog.setMessage(e.getMessage());
                    dialog.show(getFragmentManager(), "error_dialog");
                }
            }
        });
    }

    private void addFriendCheckmarks()
    {
        mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>()
        {
            @Override
            public void done(List<ParseUser> friends, ParseException e)
            {
                if (e == null)
                {
                    // list returned -- look for a match
                    for (int i=0;i<mUsers.size();i++)
                    {
                        ParseUser user = mUsers.get(i);

                        for(ParseUser friend : friends)
                        {
                            if(friend.getObjectId().equals(user.getObjectId()))
                            {
                                getListView().setItemChecked(i, true);
                            }
                        }
                    }
                } else {
                    Log.e("addFriendCheckmarks", "at line 119", e);
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

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);

        if(getListView().isItemChecked(position))
        {
            // add friend
            mFriendsRelation.add(mUsers.get(position));
            mCurrentUser.saveInBackground(new SaveCallback()
            {
                @Override
                public void done(ParseException e)
                {
                    if (e != null)
                    {
                        // error!
                        Log.e("onItemListClick", "at line 169", e);
                        AlertDialogFragment dialog = new AlertDialogFragment();
                        dialog.setMessage(e.getMessage());
                        dialog.show(getFragmentManager(), "error_dialog");
                    }
                }
            });
        } else {
            // remove friend
        }

    }
}
