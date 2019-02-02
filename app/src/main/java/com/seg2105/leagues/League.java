package com.seg2105.leagues;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class League {
    public static Tournament tournament = null;
    public static Context context = null;

    static public void init() {
            if (Teams.TEAMS.size() == 0)
                Teams.init(context);
            tournament = new MixedTournament(Teams.ACTIVE_TEAMS.size());



    }

    public static Tournament getTournament()
    {
        if (tournament == null)
            init();

        return tournament;
    }

    public static void setTournament(Tournament tournament) {
        League.tournament = tournament;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        tournament = (Tournament) in.readObject();
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(tournament);
    }
}
