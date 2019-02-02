package com.seg2105.leagues;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class RankingsFragment extends Fragment {


    public RankingsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rankings, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rankings_list);
        assert recyclerView != null;

        Rankings rankings = League.getTournament().getRankings();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new RankingsAdapter(rankings));
        return view;
    }


    private class RankingsAdapter extends RecyclerView.Adapter<RankingsAdapter.ViewHolder> {
        private Rankings rankings;
        private ViewHolder holder;

        public RankingsAdapter(Rankings rankings) {
            this.rankings = rankings;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rankings_list_content, parent, false);
            holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.rankingsPosition.setText(Integer.toString(position + 1));
            holder.rankingsTeam.setText(rankings.getRanking(position).getTeam().getName());
            holder.rankingsPlayed.setText(Integer.toString(rankings.getRanking(position).getPlayed()));
            holder.rankingsWon.setText(Integer.toString(rankings.getRanking(position).getWon()));
            holder.rankingsDrawn.setText(Integer.toString(rankings.getRanking(position).getDrawn()));
            holder.rankingsLost.setText(Integer.toString(rankings.getRanking(position).getLost()));
            holder.rankingsGoalsFor.setText(Integer.toString(rankings.getRanking(position).getGoalsFor()));
            holder.rankingsGoalsAgainst.setText(Integer.toString(rankings.getRanking(position).getGoalsAgainst()));
            holder.rankingsGoalsDifference.setText(Integer.toString(rankings.getRanking(position).getGoalsDifference()));
            holder.rankingsPoints.setText(Integer.toString(rankings.getRanking(position).getPoints()));


        }

        @Override
        public int getItemCount() {
            return rankings.getRankings().size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private final View view;
            private final TextView rankingsPosition;
            private final TextView rankingsTeam;
            private final TextView rankingsPlayed;
            private final TextView rankingsWon;
            private final TextView rankingsDrawn;
            private final TextView rankingsLost;
            private final TextView rankingsGoalsFor;
            private final TextView rankingsGoalsAgainst;
            private final TextView rankingsGoalsDifference;
            private final TextView rankingsPoints;


            public ViewHolder(View rankingView) {
                super(rankingView);
                view = rankingView;
                rankingsPosition = (TextView) view.findViewById(R.id.rankingsPosition);
                rankingsTeam = (TextView) view.findViewById(R.id.rankingsTeam);
                rankingsPlayed = (TextView) view.findViewById(R.id.rankingsPlayed);
                rankingsWon = (TextView) view.findViewById(R.id.rankingsWon);
                rankingsDrawn = (TextView) view.findViewById(R.id.rankingsDrawn);
                rankingsLost = (TextView) view.findViewById(R.id.rankingsLost);
                rankingsGoalsFor = (TextView) view.findViewById(R.id.rankingsGoalsFor);
                rankingsGoalsAgainst = (TextView) view.findViewById(R.id.rankingsGoalsAgainst);
                rankingsGoalsDifference = (TextView) view.findViewById(R.id.rankingsGoalDifference);
                rankingsPoints = (TextView) view.findViewById(R.id.rankingsPoints);

            }

            @Override
            public String toString() {
                return "Position: " + rankingsPosition.getText().toString();
            }
        }
    }
}
