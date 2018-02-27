package com.example.jh.commonplayer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;

import com.d.commenplayer.CommenPlayer;
import com.d.commenplayer.adapter.AdapterPlayer;
import com.d.commenplayer.listener.OnShowThumbnailListener;
import com.d.lib.xrv.adapter.CommonAdapter;
import com.d.lib.xrv.adapter.CommonHolder;
import com.example.jh.commonplayer.R;
import com.example.jh.commonplayer.model.PlayerModel;

import java.util.List;

/**
 * Created by jinhui on 2018/2/27.
 * Email:1004260403@qq.com
 */

public class PlayerAdapter extends CommonAdapter<PlayerModel> {

    private CommenPlayer player;

//    public PlayerAdapter(Context context, List<PlayerModel> datas, int layoutId) {
//        super(context, datas, layoutId);
//    }
//
//    public PlayerAdapter(Context context, List<PlayerModel> datas, MultiItemTypeSupport<PlayerModel> multiItemTypeSupport, CommenPlayer player) {
//        super(context, datas, multiItemTypeSupport);
//        this.player = player;
//    }

    public PlayerAdapter(Context context, List<PlayerModel> datas, int layoutId, CommenPlayer player) {
        super(context, datas, layoutId);
        this.player = player;
    }

    @Override
    public void convert(int position, CommonHolder holder, PlayerModel item) {
        AdapterPlayer aPlayer = holder.getView(R.id.aplayer);
        aPlayer.setLive(false);
        aPlayer.setUrl(item.url);
        aPlayer.setThumbnail(new OnShowThumbnailListener() {
            @Override
            public void onShowThumbnail(ImageView ivThumbnail) {
                ivThumbnail.setBackgroundColor(Color.parseColor("#303F9F"));
            }
        });
        aPlayer.with(player);
    }
}
