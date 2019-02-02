package com.seg2105.leagues;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

public class HelpDialog extends DialogFragment {
    String[] league = {
    "A mixed tournament is created by default on application start, to start a new tournament click the \'Start New Tournament\' menu option, and select one of the following three types:\n\n" +
    "Knockout: the losing team is eliminated from the tournament, last team standing wins the championship\n\n" +
    "Round Robin: every team plays every other team, the team with highest points wins the championship\n\n" +
    "Mixed: the losing team is eliminated from the tournament until only 4 remain, afterwards, every remaining team plays every other remaining team, the team with highest points wins the championship",
    "In the Rounds tab, the matches of current round are displayed, to play the matches click on the play button in bottom right of the screen\n\n",
    "Once a round has played, the view is updated to reflect the scores, and play button turn into a reload button, which loads the next round\n\n",
    "When the tournament is over, a popup dialog is shown indicating the champion, and the play/reload button is disabled, until a new tournament is started\n\n",
    "In the Rankings tab, the rankings are displayed, and are updated automatically when a round is played\n\n", 
    "To view to modify the teams, select the Teams option from the drawer\n\n",
    "To view help content, choose the Help option from the drawer\n\n"
    };
    
    String[] teams = {
    "All available teams are displayed here (164 English, Italian, and Spanish teams are included by default)\n\n",
    "The toggle buttons at the right portion of each teams determines wither the team is active or not, only active teams participate in the tournament (only 8 teams are active by default)\n\n",
    "To view a teams details click on the team, which opens the details view displaying the team icon, name, country, and league\n\n",
    "To edit a team, go to the detail view and click on the edit button, afterwards, click on the edit button again to save the modifications (you can click on the icon to replace it with an image from the device)\n\n" +
    "To delete teams, check the checkbox of teams you wish to delete and click on the delete button at bottom right of the screen\n\n",
    "To add new team, click on the add team button at bottom right of the screen, enter the team\\'s details, and then click on the edit button to save it\n\n",
     "Teams are saved on the devices, and are loaded on startup\n\n"
    };
    String[][] text = {league, teams};
    int selection = -1, index = -1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        selection = getArguments().getInt("selection", 0);
        index = getArguments().getInt("index", 0);
        builder.setTitle("Help").setMessage(text[selection][index]).setNegativeButton("Dismiss", (dialog, id) -> dialog.cancel());

        if (index + 1 != text[selection].length) {
            builder.setPositiveButton("Next", (dialog, id) -> {
                showDialog(getActivity(), selection, index + 1);

            });
        }

        if (index > 0) {
            builder.setNeutralButton("Back", (dialog, id) -> {
                showDialog(getActivity(), selection, index - 1);
            });
        }


        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public static void showDialog(Activity activity, int selection, int index) {
        HelpDialog helpDialog = new HelpDialog();
        Bundle args = new Bundle();
        args.putInt("selection", selection);
        args.putInt("index", index);
        helpDialog.setArguments(args);
        helpDialog.show(activity.getFragmentManager(), "help");

    }
}