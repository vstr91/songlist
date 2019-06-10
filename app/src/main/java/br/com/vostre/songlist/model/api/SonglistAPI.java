package br.com.vostre.songlist.model.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SonglistAPI {

    // artistas - musicbrainz
    @GET("artist/")
    Call<String> buscaArtistas(@Query("query") String nome, @Query("fmt") String formato);

    // setlists - setlistfm
    @Headers({"x-api-key: d5f1bc87-b725-4c94-922e-e6fd8a167947", "Accept: application/json"})
    @GET("artist/{mbid}/setlists")
    Call<String> buscaSetlists(@Path("mbid") String id, @Query("p") Integer pagina);

    // pega usuario logado - spotify
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("me")
    Call<String> carregaUsuarioLogado(@Header("Authorization") String accessToken);

    // busca playlists - spotify
    @Headers({"Content-Type: application/json"})
    @GET("me/playlists")
    Call<String> buscaPlaylists(@Header("Authorization") String accessToken);

    // cria playlist - spotify
    @Headers({"Content-Type: application/json"})
    @POST("users/{user_id}/playlists")
    Call<String> criaPlaylist(@Header("Authorization") String accessToken, @Path("user_id") String id,
                              @Body String dados);

    // busca musica - spotify
    @Headers({"Content-Type: application/json"})
    @GET("search")
Call<String> buscaMusica(@Header("Authorization") String accessToken, @Query(value = "q", encoded = true) String pesquisa,
                         @Query("type") String tipo);

    // adiciona musica a playlist - spotify
    @Headers({"Content-Type: application/json"})
    @POST("playlists/{playlist_id}/tracks")
    Call<String> adicionaMusicaPlaylist(@Header("Authorization") String accessToken,
                                        @Path("playlist_id") String idPlaylist, @Body String musicas);

    // remove playlist - spotify
    @Headers({"Content-Type: application/json"})
    @DELETE("playlists/{playlist_id}/followers")
    Call<String> removePlaylist(@Header("Authorization") String accessToken, @Path("playlist_id") String id);
}
