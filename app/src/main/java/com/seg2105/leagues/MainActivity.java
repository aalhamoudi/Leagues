package com.seg2105.leagues;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new RoundsFragment();
                case 1:
                    return new RankingsFragment();

            }

            return null;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Rounds";
                case 1:
                    return "Rankings";

            }
            return null;
        }
    }

    private static boolean helpViewed = false;
    static boolean helpEnabled = true;
    @SuppressLint("ValidFragment")
    public class StartNewTournamentDialog extends DialogFragment {
        int tournamentType = -1;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setPositiveButton("Start Tournament", (dialog, id) -> {
                Tournament tournament;
                switch (tournamentType) {
                    case 0:
                        tournament = new KnockoutTournament(Teams.ACTIVE_TEAMS.size());
                        break;
                    case 1:
                        tournament = new RoundRobinTournament(Teams.ACTIVE_TEAMS.size());
                        break;
                    case 2:
                        tournament = new MixedTournament(Teams.ACTIVE_TEAMS.size());
                        break;
                    default:
                        tournament = new KnockoutTournament(Teams.ACTIVE_TEAMS.size());
                }

                League.setTournament(tournament);
                fab.setEnabled(true);
                pagerAdapter.notifyDataSetChanged();
                roundPlayed = false;
                fab.setImageResource(R.drawable.ic_play_arrow_black_24dp);

            })
                    .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel())
                    .setTitle("Tournament Type")
                    .setSingleChoiceItems(new CharSequence[]{"Knockout", "Round Robin", "Mixed"}, 0, (dialog, which) -> tournamentType = which);

            return builder.create();
        }
    }

    private final Context context = this;
    PagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private boolean roundPlayed = false;
    private FloatingActionButton fab;

    File file = null;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (file != null) {
            try {
                FileOutputStream fout = new FileOutputStream(file);
                ObjectOutputStream out = new ObjectOutputStream(fout);
                Teams.writToFile(out);
                out.writeUTF(League.getTournament().getType().toString());
                out.writeObject(League.getTournament());
                out.close();
                fout.close();
            } catch (Exception e) {

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        League.context = this;

        file = new File(context.getFilesDir(), "leagues.data");
        if (file.exists()) {
            try {
                FileInputStream fin = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(fin);
                Teams.readFromFile(in);

                // Saving tournament details doesn't work
/*                Tournament.TournamentType type = Tournament.TournamentType.valueOf(in.readUTF());
                switch(type)
                {
                    case Knockout:
                        League.setTournament((KnockoutTournament) in.readObject());
                        break;
                    case Round_Robin:
                        League.setTournament((RoundRobinTournament) in.readObject());
                        break;
                    case Mixed:
                        League.setTournament((MixedTournament) in.readObject());
                        break;
                }*/

                in.close();
                fin.close();

            }
            catch (Exception e) {
                Toast.makeText(context, "File couldn't be read", Toast.LENGTH_LONG).show();

            }
        }





        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                RecyclerView roundsRecyclerView = (RecyclerView) findViewById(R.id.match_list);
                RecyclerView.Adapter roundsAdapter = roundsRecyclerView.getAdapter();
                RecyclerView rankingsRecyclerView = (RecyclerView) findViewById(R.id.rankings_list);
                RecyclerView.Adapter rankinkgsAdapter = rankingsRecyclerView.getAdapter();


                if (!roundPlayed) {
                    League.getTournament().playRound();
                    roundsAdapter.notifyDataSetChanged();
                    rankinkgsAdapter.notifyDataSetChanged();
                    roundPlayed = true;

                    if (League.getTournament().hasNextRound())
                        fab.setImageResource(R.drawable.ic_cached_black_24dp);
                    else {
                        fab.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        fab.setEnabled(false);
                        Teams.Team winner = League.getTournament().getRankings().getRanking(0).getTeam();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Championship Winner").setMessage(winner.getName()).setIcon(winner.getIcon()).show();
                    }
                } else {
                    League.getTournament().nextRound();
                    pagerAdapter.notifyDataSetChanged();
                    roundPlayed = false;
                    fab.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        pagerAdapter = new PagerAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(pagerAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 1)
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                else
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        if (helpEnabled && !helpViewed) {
            helpViewed = true;
            HelpDialog.showDialog(this, 0, 0);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar match clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_start_new_tournament) {
            DialogFragment dialog = new StartNewTournamentDialog();
            dialog.show(getFragmentManager(), "StartNewTournamentDialogFragment");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view match clicks here.
        int id = item.getItemId();
        Intent intent = null;

        if (id == R.id.nav_league) {
            intent = new Intent(this, this.getClass());

        } else if (id == R.id.nav_teams) {
            intent = new Intent(this, TeamListActivity.class);

        } else if (id == R.id.nav_help) {
            intent = new Intent(this, HelpActivity.class);
        }

        startActivity(intent);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
