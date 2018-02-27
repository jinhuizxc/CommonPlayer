package com.example.jh.commonplayer.net;

/**
 * Created by jinhui on 2018/2/27.
 * Email:1004260403@qq.com
 *
 * 网络状态
 */

public class NetEvent {

    /**
     * 当前网络状态  <=1：无网络 2：移动网络 3：Wifi
     */
    public int status;

    public NetEvent(int status) {
        this.status = status;
    }

}
