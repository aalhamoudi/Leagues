package com.seg2105.leagues;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class KnockoutTournament extends Tournament{

    public KnockoutTournament(int numberOfTeams) {
        super(numberOfTeams, TournamentType.Knockout);
        numberOfRounds = (int) (Math.log10(numberOfTeams) / Math.log10(2));
        generateSchedule();

    }

    @Override
    protected void generateSchedule() {
        ArrayList<Teams.Team> teamsLeft = new ArrayList<>(playingTeams);
        Round round = new Round();

        while (teamsLeft.size() >= 2)
        {
            round.addMatch(new Match(teamsLeft.remove(random(teamsLeft.size())), teamsLeft.remove(random(teamsLeft.size()))));
        }

        rounds.add(round);
        currentRound++;

    }

    @Override
    public void playRound() {
        for (Match match : rounds.get(currentRound - 1).getMatches()) {
            match.playMatch();
        }

    }

    @Override
    protected boolean nextRound() {
        if (!hasNextRound())
            return false;

        for (Match match : rounds.get(currentRound - 1).getMatches()) {
            if (!match.isDraw())
                playingTeams.remove(match.getLoser());

        }
        generateSchedule();

        return true;
    }

    @Override
    protected boolean hasNextRound() {
        return !(currentRound == numberOfRounds);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        type = TournamentType.valueOf(in.readUTF());
        teams = (ArrayList<Teams.Team>) in.readObject();
        activeTeams = (ArrayList<Teams.Team>) in.readObject();
        playingTeams = (ArrayList<Teams.Team>) in.readObject();
        rounds = (ArrayList<Round>) in.readObject();
        rankings = (Rankings) in.readObject();
        numberOfTeams = in.readInt();
        numberOfRounds = in.readInt();
        currentRound = in.readInt();

    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeUTF(type.toString());
        out.writeObject(teams);
        out.writeObject(activeTeams);
        out.writeObject(playingTeams);
        out.writeObject(rounds);
        out.writeObject(rankings);
        out.writeInt(numberOfTeams);
        out.writeInt(numberOfRounds);
        out.writeInt(currentRound);
    }
}
