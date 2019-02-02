package com.seg2105.leagues;


import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class RoundsFragment extends Fragment {


    public RoundsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_rounds, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.match_list);
        assert recyclerView != null;
        Tournament.Round round = League.getTournament().getCurrentRound();
        TextView roundNumber = (TextView) view.findViewById(R.id.roundNumber);
        roundNumber.setText("Round " + League.getTournament().currentRound);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new MatchAdapter(round));
        return view;
    }



    public class MatchAdapter
            extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {

        private final Tournament.Round round;
        private ViewHolder holder;

        public MatchAdapter(Tournament.Round round) {
            this.round = round;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.match_list_content, parent, false);
            holder = new ViewHolder(view);
            return holder;
        }



        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.match = round.getMatch(position);
            holder.versus.setText("VS");

            holder.team1Icon.setImageResource(round.getMatch(position).getTeam1().getIcon());
            holder.team2Icon.setImageResource(round.getMatch(position).getTeam2().getIcon());

            holder.team1Name.setText(round.getMatch(position).getTeam1().getName());
            holder.team2Name.setText(round.getMatch(position).getTeam2().getName());

            if (round.getMatch(position).isPlayed()) {
                holder.team1Score.setText(Integer.toString(round.getMatch(position).getTeam1Score()));
                holder.team2Score.setText(Integer.toString(round.getMatch(position).getTeam2Score()));

                if (round.getMatch(position).isDraw())
                {
                    holder.team1Score.setTextColor(Color.BLUE);
                    holder.team2Score.setTextColor(Color.BLUE);
                }
                else if (round.getMatch(position).getWinner().equals(round.getMatch(position).getTeam1()))
                {
                    holder.team1Score.setTextColor(Color.GREEN);
                    holder.team2Score.setTextColor(Color.RED);
                }
                else {
                    holder.team1Score.setTextColor(Color.RED);
                    holder.team2Score.setTextColor(Color.GREEN);
                }
            }
            else
            {
                holder.team1Score.setText("-");
                holder.team2Score.setText("-");
            }

        }

        @Override
        public int getItemCount() {
            return round.getMatches().size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View view;
            public Tournament.Match match;
            public final ImageView team1Icon;
            public final ImageView team2Icon;

            public final TextView team1Name;
            public final TextView team2Name;

            public final TextView team1Score;
            public final TextView team2Score;

            public final TextView versus;

            public ViewHolder(View view) {
                super(view);
                this.view = view;
                versus = (TextView) view.findViewById(R.id.versus);

                team1Icon = (ImageView) view.findViewById(R.id.team1Icon);
                team2Icon = (ImageView) view.findViewById(R.id.team2Icon);

                team1Name = (TextView) view.findViewById(R.id.team1Name);
                team2Name = (TextView) view.findViewById(R.id.team2Name);

                team1Score = (TextView) view.findViewById(R.id.team1Score);
                team2Score = (TextView) view.findViewById(R.id.team2Score);


            }

            @Override
            public String toString() {
                return super.toString() + " '" + "VS"  + "'";
            }
        }
    }
}
