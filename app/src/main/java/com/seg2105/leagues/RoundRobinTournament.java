package com.seg2105.leagues;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;

public class RoundRobinTournament extends Tournament{

    private Teams.Team firstTeam;
    private LinkedList<Teams.Team> firstGroup;
    private LinkedList<Teams.Team> secondGroup;

    public RoundRobinTournament(int numberOfTeams) {
        super(numberOfTeams, TournamentType.Round_Robin);
        numberOfRounds = (numberOfTeams - 1);
        firstTeam = playingTeams.remove(0);
        firstGroup = new LinkedList<>(playingTeams.subList(0, playingTeams.size() / 2));
        secondGroup = new LinkedList<>(playingTeams.subList(playingTeams.size() / 2, playingTeams.size()));

        generateSchedule();
    }
    @Override
    protected void generateSchedule() {

        Round round = new Round();
        int matchesPerRound = activeTeams.size()/2;
        currentRound++;

        round.addMatch(new Match(firstTeam, secondGroup.get(secondGroup.size() - 1)));
        for (int i = 1; i < matchesPerRound; i++)
            round.addMatch(new Match(firstGroup.get(i - 1), secondGroup.get((secondGroup.size() - 1) - i)));

        secondGroup.add(firstGroup.remove());
        firstGroup.add(secondGroup.remove());

        rounds.add(round);

    }

    protected int getIndex(int index) {
        return (index + (currentRound - 1)) % (activeTeams.size()/2);
    }

    @Override
    protected void playRound() {
        for (Match match : rounds.get(currentRound - 1).getMatches()) {
            match.playMatch();
        }

    }

    @Override
    protected boolean nextRound() {
        if (!hasNextRound())
            return false;

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
        firstTeam = (Teams.Team) in.readObject();
        firstGroup = (LinkedList<Teams.Team>) in.readObject();
        secondGroup = (LinkedList<Teams.Team>) in.readObject();

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
        out.writeObject(firstTeam);
        out.writeObject(firstGroup);
        out.writeObject(secondGroup);
    }
}
