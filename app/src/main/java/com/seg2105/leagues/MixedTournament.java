package com.seg2105.leagues;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;

public class MixedTournament extends Tournament{

    public static final int SWITCHING_THRESHOLD = 4;
    private Teams.Team firstTeam;
    private LinkedList<Teams.Team> firstGroup;
    private LinkedList<Teams.Team> secondGroup;

    public MixedTournament(int numberOfTeams) {
        super(numberOfTeams, TournamentType.Mixed);
        numberOfRounds = (int) (Math.log10(numberOfTeams/SWITCHING_THRESHOLD) / Math.log10(2)) + (SWITCHING_THRESHOLD - 1);
        generateSchedule();

    }

    @Override
    protected void generateSchedule() {
        Round round = new Round();
        currentRound++;
        int matchesThisRound = (int) Math.ceil(playingTeams.size()/2.0);

        if (playingTeams.size() <= SWITCHING_THRESHOLD)
        {
            if (firstTeam == null) {
                firstTeam = playingTeams.remove(0);
                firstGroup = new LinkedList<>(playingTeams.subList(0, playingTeams.size() / 2));
                secondGroup = new LinkedList<>(playingTeams.subList(playingTeams.size() / 2, playingTeams.size()));
            }

            round.addMatch(new Match(firstTeam, secondGroup.get(secondGroup.size() - 1)));
            for (int i = 1; i < matchesThisRound; i++)
                round.addMatch(new Match(firstGroup.get(i - 1), secondGroup.get((secondGroup.size() - 1) - i)));

            secondGroup.add(firstGroup.remove());
            firstGroup.add(secondGroup.remove());
        }

        else
        {
            ArrayList<Teams.Team> teamsLeft = new ArrayList<>(playingTeams);
            for (int i = 0; i < matchesThisRound; i++)
            {
                round.addMatch(new Match(teamsLeft.remove(random(teamsLeft.size())), teamsLeft.remove(random(teamsLeft.size()))));
            }

        }

        rounds.add(round);
    }



    @Override
    protected boolean nextRound() {

        if (!hasNextRound())
            return false;

        if (playingTeams.size() > SWITCHING_THRESHOLD) {
            for (Match match : rounds.get(currentRound - 1).getMatches()) {
                if (match.isDraw()) {
                    Teams.Team toBeRemoved = match.getTeam1().getRanking().getPoints() > match.getTeam2().getRanking().getPoints()? match.getTeam1() : match.getTeam2();
                    playingTeams.remove(toBeRemoved);
                }
                else
                    playingTeams.remove(match.getLoser());
            }
        }
        generateSchedule();

        return true;
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
