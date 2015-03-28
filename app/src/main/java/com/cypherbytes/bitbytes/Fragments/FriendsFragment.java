package com.cypherbytes.bitbytes.Fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cypherbytes.bitbytes.R;

/**
 * Created by travis on 3/27/15.
 */
public class FriendsFragment extends ListFragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        return rootView;
    }
}
