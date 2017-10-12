package kang.classfiter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import lecho.lib.hellocharts.view.LineChartView;

public class MainActivity extends AppCompatActivity {
    public static Helper helper;
    boolean st = false;
    private int id = 0;
    private ServiceConnection connection;
    private CollectDataService.MyBinder binder;
    private LineChartView chartView;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        init();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "service:" + st, Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                if (st) {
                    stopMyService();
                } else {
                    startMyService();
                    helper.sendEmptyMessage(13);
                }
            }
        });
        //myStart

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*

*/
    public void init() {
        helper = new Helper(this);
        helper.setStarttime(System.currentTimeMillis());
        //helper.sendEmptyMessage(5);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                int page = position + 1;
                if (page == 1) {
                    helper.setMsgView((TextView) MainActivity.this.findViewById(R.id.section_label));
                    helper.setResultview((TextView) MainActivity.this.findViewById(R.id.section_labe2));
                } else if (page == 2) {
                    helper.setChartView((LineChartView) MainActivity.this.findViewById(R.id.chart));
                    helper.setResultview((TextView) MainActivity.this.findViewById(R.id.secRes));
                } else if (page == 3) {
                    helper.setMsgView((TextView) MainActivity.this.findViewById(R.id.section_labe3));
                    helper.setResultview((TextView) MainActivity.this.findViewById(R.id.section_labe4));
                }

                Message msg = new Message();
                msg.what = 10;
                msg.arg1 = page;
                helper.sendMessage(msg);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void startMyService() {
        if (!st) {
            Intent intent = new Intent(this, CollectDataService.class);
            intent.putExtra("id", id);
            startService(intent);
            connection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    binder = (CollectDataService.MyBinder) iBinder;
                    binder.sethelper(helper);
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    binder = null;
                }
            };
            bindService(intent, connection, Context.BIND_AUTO_CREATE);
            st = true;

        }
    }

    public void stopMyService() {
        if (st) {
            stopService(new Intent(this, CollectDataService.class));
            if (connection != null)
                unbindService(connection);
            st = false;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = null;
            int page = getArguments().getInt(ARG_SECTION_NUMBER);

            if (page == 1) {
                rootView = inflater.inflate(R.layout.fragment_main, container, false);
                TextView textView1 = (TextView) rootView.findViewById(R.id.section_label);
                TextView textView2 = (TextView) rootView.findViewById(R.id.section_labe2);
                helper.setResultview(textView2);
                helper.setMsgView(textView1);
                helper.sendEmptyMessage(11);
            } else if (page == 2) {
                rootView = inflater.inflate(R.layout.fragmentchart, container, false);
                TextView textView = (TextView) rootView.findViewById(R.id.secRes);
                LineChartView chartView = (LineChartView) rootView.findViewById(R.id.chart);
                textView.setText("断论: no start");


            } else if (page == 3) {
                rootView = inflater.inflate(R.layout.fragmentthird, container, false);
                TextView textView1 = (TextView) rootView.findViewById(R.id.section_labe3);
                TextView textView2 = (TextView) rootView.findViewById(R.id.section_labe4);
                textView2.setMovementMethod(ScrollingMovementMethod.getInstance());
                textView1.setText("start time: \n end time:\n");
                textView2.setText("wait to start");


            } else {
                rootView = inflater.inflate(R.layout.fragment_main, container, false);
                TextView textView = (TextView) rootView.findViewById(R.id.section_label);

                textView.setText("false: " + page);
            }
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
