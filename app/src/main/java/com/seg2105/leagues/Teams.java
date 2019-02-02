package com.seg2105.leagues;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Icon;
import android.net.Uri;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Teams {


    public static ArrayList<Team> TEAMS = new ArrayList<Team>();
    public static ArrayList<Team> ACTIVE_TEAMS = new ArrayList<>();
    public static Map<String, Team> TEAMS_MAP = new HashMap<String, Team>();
    public static int teamNumber = 0;
    static int numberOfTeams = 0;
    static int numberOfActiveTeams = 0;
    static boolean isInit = false;
    public static void init(Context context) {
        if (context == null)
            return;

        Resources res = context.getResources();

        String[] englishTeams = res.getStringArray(R.array.english_teams);
        String[] italianTeams = res.getStringArray(R.array.italian_teams);
        String[] spanishTeams = res.getStringArray(R.array.spanish_teams);

        String[] englishTeamsIcons = res.getStringArray(R.array.english_teams_icons);
        String[] italianTeamsIcons = res.getStringArray(R.array.italian_teams_icons);
        String[] spanishTeamsIcons = res.getStringArray(R.array.spanish_teams_icons);

        isInit = true;

        for (int i = 0; i < englishTeams.length; i++)
        {
            addTeam(new Team(Integer.toString(teamNumber++), englishTeams[i], Team.Country.England, Team.League.Premier_League, res.getIdentifier(englishTeamsIcons[i], "drawable", context.getPackageName())));
        }

        for (int i = 0; i < italianTeams.length; i++)
        {
            addTeam(new Team(Integer.toString(teamNumber++), italianTeams[i], Team.Country.Italy, Team.League.Serie_A, res.getIdentifier(italianTeamsIcons[i], "drawable", context.getPackageName())));
        }

        for (int i = 0; i < spanishTeams.length; i++)
        {
            addTeam(new Team(Integer.toString(teamNumber++), spanishTeams[i], Team.Country.Spain, Team.League.La_Liga, res.getIdentifier(spanishTeamsIcons[i], "drawable", context.getPackageName())));
        }

        isInit = false;


    }


    public static void addTeam(Team team) {
        if (!isInit)
            System.out.println();

        switch(team.getName()) {
            case "Arsenal FC":
                team.setActive(true);
                TEAMS.add(0, team);
                break;
            case "Chelsea FC":
                team.setActive(true);
                TEAMS.add(1, team);
                break;
            case "Liverpool FC":
                team.setActive(true);
                TEAMS.add(2, team);
                break;
            case "Manchester United":
                team.setActive(true);
                TEAMS.add(3, team);
                break;
            case "AC Milan":
                team.setActive(true);
                TEAMS.add(4, team);
                break;
            case "Juventus":
                team.setActive(true);
                TEAMS.add(5, team);
                break;
            case "FC Barcelona":
                team.setActive(true);
                TEAMS.add(6, team);
                break;
            case "Real Madrid":
                team.setActive(true);
                TEAMS.add(7,team);
                break;
            default:
                TEAMS.add(team);
        }
        TEAMS_MAP.put(team.id, team);
        numberOfTeams++;

        if (team.isActive())
            ACTIVE_TEAMS.add(team);
    }


    public static void readFromFile(ObjectInputStream in) throws IOException, ClassNotFoundException {
        numberOfTeams = in.readInt();
        numberOfActiveTeams = in.readInt();
        teamNumber = in.readInt();

        TEAMS = (ArrayList<Team>) in.readObject();
        TEAMS_MAP = (HashMap<String, Team>) in.readObject();
        ACTIVE_TEAMS = (ArrayList<Team>) in.readObject();

/*        for (int i = 0; i < numberOfTeams; i++) {
            Team team = (Team) in.readObject();
            if (team != null) {
                TEAMS.add(team);
                TEAMS_MAP.put(team.getId(), team);
            }

        }

        for (int i = 0; i < numberOfActiveTeams; i++)
            ACTIVE_TEAMS.add((Team) in.readObject());*/


    }

    public static void writToFile(ObjectOutputStream out) throws IOException {
        out.writeInt(numberOfTeams);
        out.writeInt(numberOfActiveTeams);
        out.writeInt(teamNumber);

        out.writeObject(TEAMS);
        out.writeObject(TEAMS_MAP);
        out.writeObject(ACTIVE_TEAMS);

/*        for (int i = 0; i < TEAMS.size(); i++)
            out.writeObject(TEAMS.get(i));

        for (int i = 0; i < ACTIVE_TEAMS.size(); i++)
            out.writeObject(ACTIVE_TEAMS.get(i));*/
    }



    public static class Team implements Serializable {
        public enum Country {
            England(0),
            Italy(1),
            Spain(2),
            Germany(3),
            France(4);

            final int index;

            Country(int index) {
                this.index = index;
            }

            public int getIndex() {
                return index;
            }
        }

        public enum League {
            Premier_League(0),
            Serie_A(1),
            La_Liga(2),
            Bundesliga(3),
            Ligue_1(4);

            final int index;

            League(int index) {
                this.index = index;
            }

            public int getIndex() {
                return index;
            }
        }

        public enum IconSource {
            Resource,
            URI
        }

        private String id;
        private String name;
        private Country country;


        private League league;

        private IconSource iconSource = IconSource.Resource;
        private Uri iconUri = null;

        private int icon;

        private boolean active = false;
        private Rankings.Ranking ranking = null;
        public Team(String id, String name, Country country, League league, int icon) {
            this.id = id;
            this.name = name;
            this.country = country;
            this.league = league;
            this.icon = icon;
            ranking = new Rankings.Ranking(this);
        }

        public Uri getIconUri() {
            return iconUri;
        }

        public void setIconUri(Uri iconUri) {
            this.iconUri = iconUri;
        }

        public IconSource getIconSource() {
            return iconSource;
        }

        public void setIconSource(IconSource iconSource) {
            this.iconSource = iconSource;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Country getCountry() {
            return country;
        }

        public void setCountry(Country country) {
            this.country = country;
        }
        public League getLeague() {
            return league;
        }

        public void setLeague(League league) {
            this.league = league;
        }


        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public Rankings.Ranking getRanking() {
            return ranking;
        }

        public void setRanking(Rankings.Ranking ranking) {
            this.ranking = ranking;
        }

        @Override
        public String toString() {
            return name;
        }

        private void readObject(ObjectInputStream objIn) throws IOException, ClassNotFoundException {
            this.id = objIn.readUTF();
            this.name = objIn.readUTF();
            this.country = Country.valueOf(objIn.readUTF());
            this.league = League.valueOf(objIn.readUTF());
            this.icon = objIn.readInt();
            this.active = objIn.readBoolean();
            this.ranking = (Rankings.Ranking) objIn.readObject();
            this.iconSource = IconSource.valueOf(objIn.readUTF());
            if (iconSource == IconSource.URI) {
                this.iconUri = Uri.parse(objIn.readUTF());
            }
        }

        private void writeObject(ObjectOutputStream objOut) throws IOException {
            objOut.writeUTF(id);
            objOut.writeUTF(name);
            objOut.writeUTF(country.toString());
            objOut.writeUTF(league.toString());
            objOut.writeInt(icon);
            objOut.writeBoolean(active);
            objOut.writeObject(ranking);
            objOut.writeUTF(iconSource.toString());
            if (iconSource == IconSource.URI) {
                objOut.writeUTF(iconUri.toString());
            }
        }
    }
}
