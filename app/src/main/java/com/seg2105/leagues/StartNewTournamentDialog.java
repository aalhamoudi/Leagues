package com.seg2105.leagues;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;

public class StartNewTournamentDialog extends DialogFragment {
    int tournamentType = -1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setPositiveButton("Start Tournament", (dialog, id) -> {
                    Tournament tournament;
                    switch (tournamentType)
                    {
                        case 0:
                            tournament = new KnockoutTournament(Teams.ACTIVE_TEAMS.size());
                            break;
                        case 1:
                            tournament = new RoundRobinTournament(Teams.ACTIVE_TEAMS.size());
                            break;
                        case 2:
                            tournament = new MixedTournament(Teams.ACTIVE_TEAMS.size());
                            break;
                        default:
                            tournament = new KnockoutTournament(Teams.ACTIVE_TEAMS.size());
                    }

                    League.setTournament(tournament);


                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel())
                .setTitle("Tournament Type")
                .setSingleChoiceItems(new CharSequence[] {"Knockout", "Round Robin", "Mixed"}, 0, (dialog, which) -> tournamentType = which);

        return builder.create();
    }


}
