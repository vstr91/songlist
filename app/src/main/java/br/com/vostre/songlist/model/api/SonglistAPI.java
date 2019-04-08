package br.com.vostre.songlist.model.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SonglistAPI {

    // artistas - musicbrainz
    @GET("artist/")
    Call<String> buscaArtistas(@Query("query") String nome, @Query("fmt") String formato);

}
