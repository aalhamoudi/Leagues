package com.seg2105.leagues;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public abstract class Tournament implements Serializable {
    public static class Match implements Serializable
    {
        private Teams.Team team1;
        private Teams.Team team2;
        private Teams.Team winner = null;
        private Teams.Team loser = null;
        private int team1Score = -1;
        private int team2Score = -1;
        private boolean matchPlayed = false;
        private boolean draw = false;

        public Match(Teams.Team team1, Teams.Team team2)
        {
            this.team1 = team1;
            this.team2 = team2;
        }

        public Teams.Team getTeam1() {return  team1;}

        public Teams.Team getTeam2() {return  team2;}
        public Teams.Team getWinner() {
            if(!matchPlayed)
                playMatch();
            return winner;
        }

        public Teams.Team getLoser()
        {
            if (!matchPlayed)
                playMatch();
            return loser;
        }

        public int getTeam1Score() {

            return  team1Score;
        }

        public int getTeam2Score() {

            return team2Score;
        }

        public boolean isDraw() {
            return draw;
        }



        public Teams.Team playMatch()
        {
            team1Score = randomScore();
            team2Score = randomScore();

            if (team1Score != team2Score) {
                winner = team1Score > team2Score ? team1 : team2;
                loser = team1Score < team2Score? team1 : team2;
            }
            else
                draw = true;

            matchPlayed = true;
            updateRankings(team1.getRanking());
            updateRankings(team2.getRanking());
            return winner;
        }

        private void updateRankings(Rankings.Ranking ranking) {
            ranking.updatePlayed();
            if (isDraw())
                ranking.updateDrawn();
            else {
                if (ranking.getTeam().equals(getWinner()))
                    ranking.updateWon();
                else
                    ranking.updateLost();
            }

            if (ranking.getTeam().equals(getTeam1())) {
                ranking.updateGoalsFor(team1Score);
                ranking.updateGoalsAgainst(team2Score);
            }
            else {
                ranking.updateGoalsFor(team2Score);
                ranking.updateGoalsAgainst(team1Score);
            }

        }

        private int randomScore()
        {
            Random random = new Random();
            return random.nextInt(4);
        }

        public boolean isPlayed() {return matchPlayed;}
        public String toString()
        {
            return getTeam1().getName() + " VS " + getTeam2().getName();
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            matchPlayed = in.readBoolean();
            draw = in.readBoolean();
            team1 = (Teams.Team) in.readObject();
            team2 = (Teams.Team) in.readObject();

            if (matchPlayed) {
                winner = (Teams.Team) in.readObject();
                loser = (Teams.Team) in.readObject();
                team1Score = in.readInt();
                team2Score = in.readInt();
            }
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.writeBoolean(matchPlayed);
            out.writeBoolean(draw);
            out.writeObject(team1);
            out.writeObject(team2);
            if (matchPlayed) {
                out.writeObject(winner);
                out.writeObject(loser);
                out.writeInt(team1Score);
                out.writeInt(team2Score);
            }
        }
    }

    public static class Round implements Serializable
    {
        private ArrayList<Match> matches = new ArrayList<>();

        public Round()
        {

        }

        public void addMatch(Match match) {
            matches.add(match);
        }

        public ArrayList<Match> getMatches()
        {
            return  matches;
        }

        public Match getMatch(int index) {return  matches.get(index);}

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            matches = (ArrayList<Match>) in.readObject();
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.writeObject(matches);
        }
    }


    protected ArrayList<Teams.Team> teams;
    protected ArrayList<Teams.Team> activeTeams;
    protected ArrayList<Teams.Team> playingTeams;
    protected ArrayList<Round> rounds = new ArrayList<>();

    protected Rankings rankings;

    protected int numberOfTeams = -1;
    protected int numberOfRounds = -1;
    protected int currentRound = 0;

    public enum TournamentType {
        Knockout,
        Round_Robin,
        Mixed
    }

    protected TournamentType type;

    public Tournament(int numberOfTeams, TournamentType type)
    {
        this.numberOfTeams = numberOfTeams;
        this.teams = new ArrayList<Teams.Team>(Teams.TEAMS);
        this.activeTeams = new ArrayList<Teams.Team>(Teams.ACTIVE_TEAMS);
        this.playingTeams = new ArrayList<>(Teams.ACTIVE_TEAMS);
        this.rankings = new Rankings(activeTeams);
        this.type = type;

        if ((numberOfTeams % 2) != 0)
        {
            throw new IllegalArgumentException("Number of teams has to be even");
        }


    }

    public TournamentType getType() {
        return type;
    }

    public void setType(TournamentType type) {
        this.type = type;
    }
    protected abstract void generateSchedule();


    protected void playRound() {
        for (Match match : rounds.get(currentRound - 1).getMatches()) {
            match.playMatch();
        }
    }



    protected abstract boolean nextRound();

    protected boolean hasNextRound() {
        return !(currentRound == numberOfRounds);
    }

    protected int random(int max) {
        Random rand = new Random();
        return rand.nextInt(max);
    }

    public Rankings getRankings() {
        return rankings;
    }


    protected Round getCurrentRound()
    {
        return rounds.get(currentRound - 1);
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
