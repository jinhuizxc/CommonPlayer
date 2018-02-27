package com.example.jh.commonplayer;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.d.commenplayer.CommenPlayer;
import com.d.commenplayer.listener.IPlayerListener;
import com.d.commenplayer.listener.OnNetListener;
import com.d.commenplayer.ui.ControlLayout;
import com.d.commenplayer.util.MLog;
import com.d.commenplayer.util.MUtil;
import com.example.jh.commonplayer.net.NetConstans;
import com.example.jh.commonplayer.net.NetEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by jinhui on 2018/2/27.
 * Email:1004260403@qq.com
 *
 * EventBus 的作用？
 * android封装成库
 */

public class SimpleActivity extends AppCompatActivity {

    CommenPlayer player;
    private boolean ignoreNet;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_simple);
        initView();
        initPlayer();
    }

    private void initPlayer() {
        player.setLive(false);
        player.setOnNetListener(new OnNetListener() {
            @Override
            public void onIgnoreMobileNet() {
                ignoreNet = true;
            }
        }).setOnPlayerListener(new IPlayerListener() {
            @Override
            public void onLoading() {
                player.getControl().setState(ControlLayout.STATE_LOADING);
            }

            @Override
            public void onCompletion(IMediaPlayer mp) {
                player.getControl().setState(ControlLayout.STATE_COMPLETION);
            }

            @Override
            public void onPrepared(IMediaPlayer mp) {
                if (!ignoreNet && NetConstans.NET_STATUS == NetConstans.CONNECTED_MOBILE) {
                    player.pause();
                    player.getControl().setState(ControlLayout.STATE_MOBILE_NET);
                } else {
                    player.getControl().setState(ControlLayout.STATE_PREPARED);
                }
            }

            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                player.getControl().setState(ControlLayout.STATE_ERROR);
                return false;
            }

            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                return false;
            }

            @Override
            public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sarNum, int sarDen) {

            }
        });
        player.play(getResources().getString(R.string.url1));
    }

    private void initView() {
        player = (CommenPlayer) findViewById(R.id.player);
        findViewById(R.id.btn_view_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SimpleActivity.this, ListActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ViewGroup.LayoutParams lp = player.getLayoutParams();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            player.setLayoutParams(lp);
        } else {
            lp.height = MUtil.dip2px(getApplicationContext(), 180);
            player.setLayoutParams(lp);
        }
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetEvent(NetEvent event) {
        if (event == null || isFinishing()) {
            return;
        }
        MLog.e("dsiner: Net_" + event.status);
    }

    @Override
    public void onBackPressed() {
        MLog.e("dsiner: Net_");
        // E/CommenPlayer: [SimpleActivity.java][onBackPressed][149]dsiner: Net_
        if (player != null && player.onBackPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (player != null){
            player.onDestroy();
        }
    }
}

