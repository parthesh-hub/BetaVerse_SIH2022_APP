package com.example.kaaryakhoj;

import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;

public class myviewholder extends RecyclerView.ViewHolder {
    StyledPlayerView simpleExoPlayerView;
    Player simpleExoPlayer;
    ExoPlayer player;
    TextView vtitleview;

    public myviewholder(@NonNull View itemView) {
        super(itemView);
        vtitleview=itemView.findViewById(R.id.vtitle);
        simpleExoPlayerView=itemView.findViewById(R.id.exoplayerview);
        System.out.println("Inside myviewholder!!!!");
    }

    void prepareexoplayer(Application application, String videotitle, String videourl)
    {
        System.out.println("prepareexploreStarted");
        try
        {
            vtitleview.setText(videotitle);
//            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
//            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
//            simpleExoPlayer =(Player) new ExoPlayer.Builder(application).build();

            Uri videoURI = Uri.parse(videourl);

//            DefaultHttpDataSource.Factory dataSourceFactory = new DefaultHttpDataSource.Factory("exoplayer_video")
//            String playerInfo = Util.getUserAgent(application, "kaaryakhoj");
//            DataSource.Factory dataSourceFactory = new DefaultHttpDataSource.Factory(playerInfo);
            DataSource.Factory dataSourceFactory = new DefaultHttpDataSource.Factory();
//            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(videoURI));
//

            player = new ExoPlayer.Builder(application).build();
//                player.setPlayer(player);
//            simpleExoPlayer.prepare(mediaSource);
//            simpleExoPlayer.setPlayWhenReady(false);

            // Create a player instance.
            simpleExoPlayerView.setPlayer(player);
            // Set the media source to be played.
            player.setMediaSource(mediaSource);
            // Prepare the player.
            player.prepare();
            player.setPlayWhenReady(false);

            System.out.println("prepareexploreEnded");


        }catch (Exception ex)
        {
            System.out.println("Exception");
            Log.d("Exoplayer Crashed", ex.getMessage().toString());
        }
    }
}