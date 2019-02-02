package com.seg2105.leagues;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.FileNotFoundException;


public class TeamDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";

    private Teams.Team team;
    boolean newTeam = false;
    static boolean activated = false;
    public TeamDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            if (getArguments().getString(ARG_ITEM_ID).equals("-1"))
            {
                if (activated) {
                    activated = false;
                    team = new Teams.Team(Integer.toString(Teams.teamNumber++), "Team", Teams.Team.Country.England, Teams.Team.League.Premier_League, R.drawable.arsenal_fc);
                    Teams.addTeam(team);
                    newTeam = true;
                    TeamListActivity.teamListActivity.recyclerView.getAdapter().notifyDataSetChanged();
                }
                else
                    team = Teams.TEAMS.get(Teams.TEAMS.size() - 1);
            }
            else
                team = Teams.TEAMS_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(team.getName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.team_detail, container, false);

        ImageView teamIcon = (ImageView) rootView.findViewById(R.id.teamDetailsIcon);
        if (team.getIconSource() == Teams.Team.IconSource.Resource)
            teamIcon.setImageResource(team.getIcon());
        else {
            try {
                teamIcon.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(team.getIconUri())), 256, 256, true));
            }
            catch (FileNotFoundException e) {

            }
        }

        EditText teamName = (EditText) rootView.findViewById(R.id.teamDetailsName);
        teamName.setText(team.getName());

        Spinner countrySpinner = (Spinner) rootView.findViewById(R.id.countrySpinner);
        Spinner leagueSpinner = (Spinner) rootView.findViewById(R.id.leagueSpinner);

        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.countries, R.layout.support_simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> leagueAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.leagues, R.layout.support_simple_spinner_dropdown_item);

        countryAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        leagueAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        countrySpinner.setAdapter(countryAdapter);
        leagueSpinner.setAdapter(leagueAdapter);

        countrySpinner.setEnabled(false);
        leagueSpinner.setEnabled(false);

        countrySpinner.setSelection(team.getCountry().getIndex());
        leagueSpinner.setSelection(team.getLeague().getIndex());

        if (newTeam) {
            TeamDetailActivity.isEditing = true;
            teamIcon.setOnClickListener((view) -> startActivityForResult(TeamDetailActivity.iconPickerIntent, TeamDetailActivity.SELECT_ICON));
            teamName.setInputType(InputType.TYPE_CLASS_TEXT);
            countrySpinner.setEnabled(true);
            leagueSpinner.setEnabled(true);
        }

        return rootView;
    }
}
