package br.com.vostre.songlist.listener;

import org.json.JSONArray;

import br.com.vostre.songlist.util.APIRetorno;

public interface APIListener {

    public void OnAPIResult(JSONArray result, APIRetorno tipo, int pagina);

}
