package br.com.vostre.songlist.util;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.vostre.songlist.listener.APIListener;
import br.com.vostre.songlist.listener.MusicaAPIListener;
import br.com.vostre.songlist.listener.PlaylistAPIListener;
import br.com.vostre.songlist.listener.PlaylistAtualizadaAPIListener;
import br.com.vostre.songlist.listener.PlaylistsAPIListener;
import br.com.vostre.songlist.model.Artista;
import br.com.vostre.songlist.model.Musica;
import br.com.vostre.songlist.model.api.SonglistAPI;
import br.com.vostre.songlist.model.pojo.MusicaQuantidade;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class APIUtils {

    public static SonglistAPI criaInstanciaAPI(String baseUrl){

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(baseUrl)
                .build();

        return retrofit.create(SonglistAPI.class);
    }

    public static void consultaArtistas(String artista, final APIListener listener) throws JSONException {

        SonglistAPI api = APIUtils.criaInstanciaAPI(Constants.API_MUSICBRAINZ);
        Call<String> call = api.buscaArtistas("artist:"+artista, "json");

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                JSONObject array;

//                System.out.println("url: "+call.request().url());

                try {
                    array = new JSONObject(response.body());
                    listener.OnAPIResult(array.getJSONArray("artists"), APIRetorno.ARTISTA, -1);

//                    JSONArray artistas = array.getJSONArray("artists");
//
//                    for(int i = 0; i < artistas.length(); i++){
//                        System.out.println("ARTISTA: "+artistas.getJSONObject(i).getString("name")
//                                +" | "+artistas.getJSONObject(i).optString("disambiguation"));
//                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("AA: "+call.request().url()+" | "+t.getMessage());
                listener.OnAPIResult(null, APIRetorno.ARTISTA, -1);
            }
        });

    }

    public static void consultaSetlists(String idArtista, final APIListener listener,
                                        final int pagina) throws JSONException{

        SonglistAPI api = APIUtils.criaInstanciaAPI(Constants.API_SETLISTFM);
        Call<String> call = api.buscaSetlists(idArtista, pagina);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                JSONObject array;

                //System.out.println("url: "+call.request().url());

                try {

                    if(response.body() != null){
                        array = new JSONObject(response.body());
                    } else{
                        array = null;
                    }


                    if(array != null){
                        listener.OnAPIResult(array.getJSONArray("setlist"), APIRetorno.SETLIST, pagina);
                    } else{
                        listener.OnAPIResult(null, APIRetorno.SETLIST, pagina);
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //System.out.println("AA: "+call.request().url()+" | "+t.getMessage());
                listener.OnAPIResult(null, APIRetorno.SETLIST, pagina);
            }
        });

    }

    public static void consultaUsuarioLogado(String token, final APIListener listener) throws JSONException {

        SonglistAPI api = APIUtils.criaInstanciaAPI(Constants.API_SPOTIFY);
        Call<String> call = api.carregaUsuarioLogado("Bearer "+token);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                JSONObject array;

                System.out.println("url: "+call.request().url());

                try {
                    array = new JSONObject(response.body());
                    JSONArray a = new JSONArray();
                    listener.OnAPIResult(a.put(array), APIRetorno.USUARIO, -1);

//                    JSONArray artistas = array.getJSONArray("artists");
//
//                    for(int i = 0; i < artistas.length(); i++){
//                        System.out.println("ARTISTA: "+artistas.getJSONObject(i).getString("name")
//                                +" | "+artistas.getJSONObject(i).optString("disambiguation"));
//                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("AA: "+call.request().url()+" | "+t.getMessage());
                listener.OnAPIResult(null, APIRetorno.ARTISTA, -1);
            }
        });

    }

    public static void buscarPlaylists(Context ctx, final PlaylistsAPIListener listener) throws JSONException {

        String token = PreferenceUtils.carregarPreferencia(ctx, "access_token");

        SonglistAPI api = APIUtils.criaInstanciaAPI(Constants.API_SPOTIFY);
        Call<String> call = api.buscaPlaylists("Bearer "+token);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                JSONObject obj;

                System.out.println("url: "+call.request().url());

                try {
                    obj = new JSONObject(response.body());
                    JSONArray a = new JSONArray();
//                    listener.OnPlaylistsAPIResult(a.put(array));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("AA: "+call.request().url()+" | "+t.getMessage());
//                listener.OnAPIResult(null, APIRetorno.ARTISTA, -1);
            }
        });

    }

    public static void criarPlaylist(final Context ctx, String nome,
                                     final List<MusicaQuantidade> musicas,
                                     final MusicaAPIListener listener,
                                     final PlaylistAPIListener pListener) {

        final String id = PreferenceUtils.carregarPreferencia(ctx, "id");
        final String token = PreferenceUtils.carregarPreferencia(ctx, "access_token");

        String dados = "{\"name\": \""+nome+"\", \"description\": \"Criada pelo Songlist!\"}";

        SonglistAPI api = APIUtils.criaInstanciaAPI(Constants.API_SPOTIFY);
        Call<String> call = api.criaPlaylist("Bearer "+token, id, dados);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                JSONObject obj;

                System.out.println("url: "+call.request().url());

                try {
                    obj = new JSONObject(response.body());

                    String idPlaylist = obj.getString("id");

                    pListener.OnPlaylistAPIResult(idPlaylist);

//                    if(idPlaylist != null && !idPlaylist.isEmpty()){
//                        adicionarMusicasPlaylist(ctx, id, token, idPlaylist, musicas, listener, pListener);
//                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("AA: "+call.request().url()+" | "+t.getMessage());
//                listener.OnAPIResult(null, APIRetorno.ARTISTA, -1);
            }
        });

    }

    public static void removerPlaylist(final Context ctx, String idPlaylist) {

        final String token = PreferenceUtils.carregarPreferencia(ctx, "access_token");

        SonglistAPI api = APIUtils.criaInstanciaAPI(Constants.API_SPOTIFY);
        Call<String> call = api.removePlaylist("Bearer "+token, idPlaylist);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("AA: "+call.request().url()+" | "+t.getMessage());
            }
        });

    }

    public static void adicionarMusicasPlaylist(Context ctx, String id, String token, String idPlaylist,
                                                List<MusicaQuantidade> musicas,
                                                MusicaAPIListener listener, PlaylistAPIListener pListener){

        for(MusicaQuantidade m : musicas){
            buscarMusica(ctx, m, listener, false);
        }

        pListener.OnPlaylistAPIResult(idPlaylist);

    }

    public static void buscarMusica(Context ctx, final MusicaQuantidade m,
                                    final MusicaAPIListener listener, final boolean varias){

        if(m.getMusica().getIdSpotify() != null && m.getMusica().getIdSpotify() != "-1"){
            listener.OnMusicaAPIResult(m, varias);
        } else{
            final String token = PreferenceUtils.carregarPreferencia(ctx, "access_token");

            String consulta = m.getMusica().getNome()+"+artist:"+m.getArtista();

            SonglistAPI api = APIUtils.criaInstanciaAPI(Constants.API_SPOTIFY);
            Call<String> call = api.buscaMusica("Bearer "+token, consulta, "track");

            System.out.println(call.request().url());

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    System.out.println(response);

                    if(response.body() != null){
                        try {
                            JSONObject obj = new JSONObject(response.body());

                            JSONArray musicas = obj.getJSONObject("tracks").getJSONArray("items");

                            String id = "-1";

                            if(musicas.length() > 0){
                                JSONObject musica = musicas.getJSONObject(0);
                                id = musica.getString("id");
                            }

                            m.getMusica().setIdSpotify(id);

                            listener.OnMusicaAPIResult(m, varias);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else{
                        m.getMusica().setIdSpotify("-1");
                        listener.OnMusicaAPIResult(m, varias);
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    System.out.println(call);
                }
            });
        }

    }

    public static void adicionarMusicas(Context ctx, final String idPlaylist, List<String> musicas,
                                        final PlaylistAtualizadaAPIListener listener){

        final String token = PreferenceUtils.carregarPreferencia(ctx, "access_token");

        String jsonMusicas = "{\"uris\":[";

        for(String m : musicas){
            jsonMusicas = jsonMusicas.concat("\"spotify:track:"+m+"\",");
        }

        jsonMusicas = jsonMusicas.substring(0, jsonMusicas.length()-1);

        jsonMusicas = jsonMusicas.concat("]}");

        SonglistAPI api = APIUtils.criaInstanciaAPI(Constants.API_SPOTIFY);
        Call<String> call = api.adicionaMusicaPlaylist("Bearer "+token, idPlaylist, jsonMusicas);

        System.out.println(call.request().url());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println(response);

                if(response.code() == 201){
                    listener.OnPlaylistAtualizadaAPIResult(true, idPlaylist);
                } else{
                    listener.OnPlaylistAtualizadaAPIResult(false, idPlaylist);
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println(call);
            }
        });

    }

}
