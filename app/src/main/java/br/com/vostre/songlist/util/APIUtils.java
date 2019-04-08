package br.com.vostre.songlist.util;

import br.com.vostre.songlist.model.api.SonglistAPI;
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



}
