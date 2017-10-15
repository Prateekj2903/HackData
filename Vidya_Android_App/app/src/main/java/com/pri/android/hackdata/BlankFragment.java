package com.pri.android.hackdata;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.pri.android.hackdata.constants.Constants;


/**
 * A simple {@link Fragment} subclass.
 */

public class BlankFragment extends Fragment {
    YouTubePlayer youTubePlayer;
    private String VIDEO_ID = "Fe8u2I3vmHU";
    View videoView;
    int videoType;
    Button fullscreenButton;
    Boolean isFullScreen = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);
        videoView = rootView.findViewById(R.id.youtube_layout);
        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();


        youTubePlayerFragment.initialize(Constants.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                if (!wasRestored) {

                    youTubePlayer = player;
                    player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
                    player.loadVideo(VIDEO_ID);
                    player.setShowFullscreenButton(true);
//                        player.full
                    player.play();

                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult error) {
                // YouTube error
                String errorMessage = error.toString();
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                Log.d("errorMessage:", errorMessage);
            }
        });

        return rootView;
    }



    public void detach() {
        if (youTubePlayer != null) {
            youTubePlayer.release();
        }
    }

    public void loadVideo(String id) {
        youTubePlayer.loadVideo(id);
        youTubePlayer.play();
    }

    public boolean exitFullScreen() {
        if (Constants.isFullScreen) {
            youTubePlayer.setFullscreen(false);
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            Constants.isFullScreen = false;
            return false;
        }else {
            return true;
        }
    }



}