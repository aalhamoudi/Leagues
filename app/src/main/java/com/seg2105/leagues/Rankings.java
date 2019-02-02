package com.seg2105.leagues;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class Rankings implements Serializable{

    public static class Ranking implements Serializable{
        private Teams.Team team;
        private int position = -1;
        private int played = 0;
        private int won = 0;
        private int drawn = 0;
        private int lost = 0;
        private int goalsFor = 0;
        private int goalsAgainst = 0;
        private int goalsDifference = 0;
        private int points = 0;

        public Ranking(Teams.Team team) {
            this.team = team;
        }

        // Getters
        public Teams.Team getTeam() {
            return team;
        }

        public int getPosition() {
            return position;
        }

        public int getPlayed() {
            return played;
        }

        public int getWon() {
            return won;
        }

        public int getDrawn() {
            return drawn;
        }

        public int getLost() {
            return lost;
        }

        public int getGoalsFor() {
            return goalsFor;
        }

        public int getGoalsAgainst() {
            return goalsAgainst;
        }

        public int getGoalsDifference() {
            return goalsDifference;
        }

        public int getPoints() {
            return points;
        }


        // Setters & Updaters
        public void setPosition(int position) {
            if (position > 0)
                this.position = position;
            else
                throw new IllegalArgumentException("Invalid Position");
        }

        public void updatePlayed() {
            this.played++;
        }

        public void updateWon() {
            this.won++;
            updatePoints();
        }

        public void updateDrawn() {
            this.drawn++;
            updatePoints();
        }

        public void updateLost() {
            this.lost++;
        }

        public void updateGoalsFor(int goalsFor) {
            updateGoalsDifference();
            this.goalsFor += goalsFor;
        }

        public void updateGoalsAgainst(int goalsAgainst) {
            updateGoalsDifference();
            this.goalsAgainst += goalsAgainst;
        }

        private void updateGoalsDifference() {
            this.goalsDifference = goalsFor - goalsAgainst;
        }

        private void updatePoints() {
            this.points = 3 * getWon() + 1 * getDrawn();
            updatePosition(this);
        }

        private void readObject(ObjectInputStream objIn) throws IOException, ClassNotFoundException {
            this.team = (Teams.Team) objIn.readObject();
            this.position = objIn.readInt();
            this.played = objIn.readInt();
            this.won = objIn.readInt();
            this.drawn = objIn.readInt();
            this.lost = objIn.readInt();
            this.goalsFor = objIn.readInt();
            this.goalsAgainst = objIn.readInt();
            this.goalsDifference = objIn.readInt();
            this.points = objIn.readInt();
        }


        private void writeObject(ObjectOutputStream objOut) throws IOException {
            objOut.writeObject(team);
            objOut.writeInt(position);
            objOut.writeInt(played);
            objOut.writeInt(won);
            objOut.writeInt(drawn);
            objOut.writeInt(lost);
            objOut.writeInt(goalsFor);
            objOut.writeInt(goalsAgainst);
            objOut.writeInt(goalsDifference);
            objOut.writeInt(points);

        }
    }

    private static LinkedList<Ranking> rankings = new LinkedList<>();

    public  Rankings(ArrayList<Teams.Team> teams) {
        for (Teams.Team team : teams) {
            Ranking ranking = new Ranking(team);
            team.setRanking(ranking);
            rankings.add(ranking);
        }
    }

    public LinkedList<Ranking> getRankings() {
        return rankings;
    }
    public Ranking  getRanking(int index) {return  rankings.get(index);}

    public static void updatePosition(Ranking ranking) {
        int position = rankings.indexOf(ranking);
        if (position == -1)
            throw new IllegalArgumentException("Ranking was not found");



        while (position != 0 && ranking.getPoints() > rankings.get(--position).getPoints()) {
            Ranking temp = rankings.get(position);
            rankings.set(position, ranking);
            rankings.set(position + 1, temp);
        }

    }

    private void readObject(ObjectInputStream objIn) throws IOException, ClassNotFoundException {
        rankings = new LinkedList<>();
        Object temp = null;
        while ((temp = objIn.readObject()) != null)
            rankings.add((Ranking)temp);

    }

    private void writeObject(ObjectOutputStream objOut) throws IOException {
        for (Ranking ranking : rankings)
            objOut.writeObject(ranking);
    }

}