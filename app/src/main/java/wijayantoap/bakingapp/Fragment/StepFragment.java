package wijayantoap.bakingapp.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import wijayantoap.bakingapp.Activity.MainActivity;
import wijayantoap.bakingapp.Activity.RecipeDescriptionActivity;
import wijayantoap.bakingapp.Model.Baking;
import wijayantoap.bakingapp.Model.Step;
import wijayantoap.bakingapp.R;

import static java.security.AccessController.getContext;
import static wijayantoap.bakingapp.Activity.MainActivity.SELECTED_INDEX;
import static wijayantoap.bakingapp.Activity.MainActivity.SELECTED_RECIPES;
import static wijayantoap.bakingapp.Activity.MainActivity.SELECTED_STEPS;

public class StepFragment extends Fragment {
    public static final String BAKING_APP = "BakingApp";
    public static final String TITLE = "Title";
    public static final String PRESS_BACK_BUTTON_TO_EXIT_FULL_SCREEN = "Press back button to exit full screen";
    public static final String SW600DP_PORT_RECIPE_STEP_DETAIL = "sw600dp-port-recipe_step_detail";
    public static final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";
    public static final String STATE_RESUME_POSITION = "resumePosition";
    public static final String STATE_RESUME_WINDOW = "resumeWindow";
    private SimpleExoPlayer msimpleExoPlayer;
    private BandwidthMeter mBandwidhtMeter;
    private ArrayList<Step> steps = new ArrayList<>();
    private int selectedIndex;
    ArrayList<Baking> baking;
    private Handler mainHandler;
    String recipeName;

    private boolean mExoPlayerFullscreen = false;
    private Dialog mFullScreenDialog;

    private int mResumeWindow;
    private long mResumePosition;

    View rootView;

    @BindView(R.id.txtShortDescription)
    TextView mShortDesc;
    @BindView(R.id.txtDescription)
    TextView mDesc;
    @BindView(R.id.txtURL)
    TextView mUrl;
    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.exoPlayer)
    SimpleExoPlayerView mExoPlayer;
    @BindView(R.id.noVideo)
    TextView nv;
    @BindView(R.id.fabFull)
    FloatingActionButton btnFull;
    @BindView(R.id.main_media_frame)
    FrameLayout mediaFrame;

    public StepFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_step, container, false);
        mainHandler = new Handler();
        mBandwidhtMeter = new DefaultBandwidthMeter();
        baking = new ArrayList<>();

        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
            steps = savedInstanceState.getParcelableArrayList(SELECTED_STEPS);
            selectedIndex = savedInstanceState.getInt(SELECTED_INDEX);
            recipeName = savedInstanceState.getString(TITLE);
        } else {
            steps = getArguments().getParcelableArrayList(SELECTED_STEPS);
            if (steps != null) {
                steps = getArguments().getParcelableArrayList(SELECTED_STEPS);
                selectedIndex = getArguments().getInt(SELECTED_INDEX);
                recipeName = getArguments().getString(TITLE);
            } else {
                baking = getArguments().getParcelableArrayList(SELECTED_RECIPES);
                steps = (ArrayList<Step>) baking.get(0).getSteps();
                selectedIndex = 0;
            }
        }

        ButterKnife.bind(this, rootView);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        mShortDesc.setText(steps.get(selectedIndex).getShortDescription());
        initializePlayer(Uri.parse(steps.get(selectedIndex).getVideoUrl()));
        mDesc.setText(steps.get(selectedIndex).getDescription());
        mUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(steps.get(selectedIndex).getVideoUrl()));
                startActivity(browserIntent);
            }
        });
        btnFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFullscreenDialog();
                Toast.makeText(getActivity(), PRESS_BACK_BUTTON_TO_EXIT_FULL_SCREEN, Toast.LENGTH_SHORT).show();
            }
        });
        if (steps.get(selectedIndex).getVideoUrl().equals("")) {
            //mediaFrame.setVisibility(View.GONE);
            mExoPlayer.setVisibility(View.GONE);
            mUrl.setVisibility(View.GONE);
            btnFull.setVisibility(View.GONE);
            nv.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SELECTED_STEPS, steps);
        outState.putInt(SELECTED_INDEX, selectedIndex);
        outState.putString(TITLE, recipeName);

        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen);
    }

    @Override
    public void onPause() {
        super.onPause();
        mResumeWindow = mExoPlayer.getPlayer().getCurrentWindowIndex();
        mResumePosition = Math.max(0, mExoPlayer.getPlayer().getCurrentPosition());
        pausePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        initializePlayer(Uri.parse(steps.get(selectedIndex).getVideoUrl()));
        startPlayer();
        initFullscreenDialog();
        if (mExoPlayerFullscreen) {
            ((ViewGroup) mExoPlayer.getParent()).removeView(mExoPlayer);
            mFullScreenDialog.addContentView(mExoPlayer, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mFullScreenDialog.show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        pausePlayer();
    }


    private void initializePlayer(Uri mediaUri) {
        TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(mBandwidhtMeter);
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(trackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();

        msimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
        mExoPlayer.setPlayer(msimpleExoPlayer);

        String userAgent = Util.getUserAgent(getContext(), BAKING_APP);
        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
        msimpleExoPlayer.prepare(mediaSource);

        boolean haveResumePosition = mResumeWindow != C.INDEX_UNSET;
        if (haveResumePosition) {
            mExoPlayer.getPlayer().seekTo(mResumeWindow, mResumePosition);
        }
    }

    private void pausePlayer() {
        if (msimpleExoPlayer!=null) {
            msimpleExoPlayer.stop();
            msimpleExoPlayer.release();
            msimpleExoPlayer.setPlayWhenReady(false);
            msimpleExoPlayer.getPlaybackState();
        }
    }

    private void startPlayer() {
        msimpleExoPlayer.setPlayWhenReady(true);
        msimpleExoPlayer.getPlaybackState();
    }

    /**
     * Credit to https://geoffledak.com/blog/2017/09/11/how-to-add-a-fullscreen-toggle-button-to-exoplayer-in-android/
     */
    private void initFullscreenDialog() {

        mFullScreenDialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }

    private void openFullscreenDialog() {

        ((ViewGroup) mExoPlayer.getParent()).removeView(mExoPlayer);
        mFullScreenDialog.addContentView(mExoPlayer, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }


    private void closeFullscreenDialog() {

        ((ViewGroup) mExoPlayer.getParent()).removeView(mExoPlayer);
        ((FrameLayout) rootView.findViewById(R.id.main_media_frame)).addView(mExoPlayer);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
    }

}
