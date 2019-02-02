package com.seg2105.leagues;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class TeamListActivity extends AppCompatActivity {

    static TeamListActivity teamListActivity = null;
    private static boolean helpViewed = false;
    private boolean mTwoPane;
    private FloatingActionButton fab;
        RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_list);
        teamListActivity = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener((view) -> {
            TeamDetailFragment.activated = true;
            startDetailActivity(this, Integer.toString(-1));
        });

        recyclerView = (RecyclerView) findViewById(R.id.team_list);
        assert recyclerView != null;
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(Teams.TEAMS));

        if (findViewById(R.id.team_detail_container) != null) {
            mTwoPane = true;
        }
        if (MainActivity.helpEnabled && !helpViewed) {
            HelpDialog.showDialog(this, 1, 0);
            helpViewed = true;
        }

    }

    private void startDetailActivity(Activity activity, String value) {
        Intent intent = new Intent(teamListActivity, TeamDetailActivity.class);
        intent.putExtra(TeamDetailFragment.ARG_ITEM_ID, value);
        teamListActivity.startActivity(intent);
        recyclerView.getAdapter().notifyDataSetChanged();
    }


    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Teams.Team> teams;
        private final ArrayList<Teams.Team> teamsToBeDeleted = new ArrayList<>();

        public SimpleItemRecyclerViewAdapter(List<Teams.Team> teams) {
            this.teams = teams;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.team_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.team = teams.get(position);
            if (holder.team.getIconSource() == Teams.Team.IconSource.Resource)
                holder.teamIcon.setImageResource(teams.get(position).getIcon());
            else {
                try {
                    holder.teamIcon.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeStream(getContentResolver().openInputStream(holder.team.getIconUri())), 256, 256, true));
                }
                catch (FileNotFoundException e) {

                }
            }

            holder.teamName.setText(teams.get(position).getName());
            holder.teamSwitch.setChecked(teams.get(position).isActive());
            holder.teamSwitch.setOnCheckedChangeListener((switchView, isChecked) -> {
                teams.get(position).setActive(isChecked);
                if (isChecked)
                    Teams.ACTIVE_TEAMS.add(teams.get(position));
                else
                    Teams.ACTIVE_TEAMS.remove(teams.get(position));

            });
            holder.deleteCheckBox.setChecked(false);

            holder.deleteCheckBox.setOnCheckedChangeListener((view, isChecked) -> {
                if (isChecked) {
                    teamsToBeDeleted.add(teams.get(position));
                    if (teamsToBeDeleted.size() == 1)
                    {
                        fab.setImageResource(R.drawable.ic_delete_black_24dp);
                        fab.setOnClickListener((fabView) -> {

                            teams.removeAll(teamsToBeDeleted);
                            teamsToBeDeleted.clear();
                            notifyDataSetChanged();
                            fab.setImageResource(R.drawable.ic_add_black_24dp);
                            fab.setOnClickListener((innerFabView) -> {
                                startDetailActivity(teamListActivity, Integer.toString(-1));
                            });
                        });
                    }
                }
                else {
                    teamsToBeDeleted.remove(teams.get(position));
                    if (teamsToBeDeleted.size() == 0)
                    {
                        fab.setImageResource(R.drawable.ic_add_black_24dp);
                        fab.setOnClickListener((fabView) -> {
                            startDetailActivity(teamListActivity, Integer.toString(-1));
                        });
                    }
                }
            });

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(TeamDetailFragment.ARG_ITEM_ID, holder.team.getId());
                        TeamDetailFragment fragment = new TeamDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.team_detail_container, fragment)
                                .commit();
                    } else {
                        startDetailActivity(teamListActivity, holder.team.getId());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return teams.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public Teams.Team team;
            public final ImageView teamIcon;
            public final TextView teamName;
            public final Switch teamSwitch;
            public final CheckBox deleteCheckBox;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                teamIcon = (ImageView) view.findViewById(R.id.teamIcon);
                teamName = (TextView) view.findViewById(R.id.teamName);
                teamSwitch = (Switch) view.findViewById(R.id.teamSwitch);
                deleteCheckBox = (CheckBox) view.findViewById(R.id.deleteCheckBox);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + teamName.getText() + "'";
            }
        }
    }
}
