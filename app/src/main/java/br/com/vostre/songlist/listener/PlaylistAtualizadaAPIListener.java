package br.com.vostre.songlist.listener;

import br.com.vostre.songlist.model.pojo.MusicaQuantidade;

public interface PlaylistAtualizadaAPIListener {

    public void OnPlaylistAtualizadaAPIResult(boolean atualizou, String idPlaylist);

}
