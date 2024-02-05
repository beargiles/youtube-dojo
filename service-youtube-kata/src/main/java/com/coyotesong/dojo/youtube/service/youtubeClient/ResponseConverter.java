package com.coyotesong.dojo.youtube.service;

import com.coyotesong.dojo.youtube.model.Channel;
import com.coyotesong.dojo.youtube.model.Playlist;

public interface ResponseConverter {
    Channel convertChannel(com.google.api.services.youtube.model.Channel channel);

    Playlist convertPlaylist(com.google.api.services.youtube.model.Playlist playlist);
}
