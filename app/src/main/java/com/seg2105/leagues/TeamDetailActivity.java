package com.seg2105.leagues;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;

/**
 * An activity representing a single Team detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * match details are presented side-by-side with a list of items
 * in a {@link TeamListActivity}.
 */
public class TeamDetailActivity extends AppCompatActivity {

    static boolean isEditing = false;
    static Intent iconPickerIntent = new Intent(Intent.ACTION_PICK);
    static final int SELECT_ICON = 100;
    private Bitmap selectedTeamIcon = null;
    private Uri selectedImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener((view) -> toggleEditing());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            arguments.putString(TeamDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(TeamDetailFragment.ARG_ITEM_ID));
            TeamDetailFragment fragment = new TeamDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.team_detail_container, fragment)
                    .commit();
        }

        iconPickerIntent.setType("image/*");


    }

    private void reloadFragment()
    {
        Bundle arguments = new Bundle();
        arguments.putString(TeamDetailFragment.ARG_ITEM_ID,
                getIntent().getStringExtra(TeamDetailFragment.ARG_ITEM_ID));
        TeamDetailFragment fragment = new TeamDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.team_detail_container, fragment)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_ICON:
                if(resultCode == RESULT_OK){
                    try {
                        selectedImage = imageReturnedIntent.getData();
                        InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                        selectedTeamIcon = BitmapFactory.decodeStream(imageStream);
                        selectedTeamIcon = Bitmap.createScaledBitmap(selectedTeamIcon, 128, 128, true);
                        ImageView teamIcon = (ImageView) findViewById(R.id.teamDetailsIcon);
                        teamIcon.setImageBitmap(selectedTeamIcon);
                    }
                    catch (FileNotFoundException e) {

                    }
                }
        }
    }


    public void toggleEditing() {
        ImageView teamIcon = (ImageView) findViewById(R.id.teamDetailsIcon);
        EditText teamName = (EditText) findViewById(R.id.teamDetailsName);
        Spinner countrySpinner = (Spinner) findViewById(R.id.countrySpinner);
        Spinner leagueSpinner = (Spinner) findViewById(R.id.leagueSpinner);


        if (!countrySpinner.isEnabled()) {
            isEditing = true;
            teamIcon.setOnClickListener((v) -> startActivityForResult(iconPickerIntent, SELECT_ICON));
            teamName.setInputType(InputType.TYPE_CLASS_TEXT);
            countrySpinner.setEnabled(true);
            leagueSpinner.setEnabled(true);
        }

        else {
            isEditing = false;
            teamIcon.setOnClickListener((v) -> {
            });
            teamName.setInputType(InputType.TYPE_NULL);
            countrySpinner.setEnabled(false);
            leagueSpinner.setEnabled(false);


            Teams.Team team = null;
            if (getIntent().getStringExtra(TeamDetailFragment.ARG_ITEM_ID).equals("-1"))
                team = Teams.TEAMS.get(Teams.TEAMS.size() - 1);
            else
                team = Teams.TEAMS_MAP.get(getIntent().getStringExtra(TeamDetailFragment.ARG_ITEM_ID));
            if (selectedImage != null) {
                team.setIconUri(selectedImage);
                team.setIconSource(Teams.Team.IconSource.URI);
            }

            team.setName(teamName.getText().toString());
            team.setCountry(Teams.Team.Country.valueOf(countrySpinner.getSelectedItem().toString()));
            team.setLeague(Teams.Team.League.valueOf(leagueSpinner.getSelectedItem().toString().replace(' ', '_')));

            reloadFragment();
            if (TeamListActivity.teamListActivity != null)
                TeamListActivity.teamListActivity.recyclerView.getAdapter().notifyDataSetChanged();
        }





    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, TeamListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
