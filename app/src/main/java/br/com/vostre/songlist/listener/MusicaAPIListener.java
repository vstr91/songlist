package br.com.vostre.songlist.listener;

import org.json.JSONArray;

import br.com.vostre.songlist.model.pojo.MusicaQuantidade;
import br.com.vostre.songlist.util.APIRetorno;

public interface MusicaAPIListener {

    public void OnMusicaAPIResult(MusicaQuantidade musicaQuantidade, boolean varias);

}
