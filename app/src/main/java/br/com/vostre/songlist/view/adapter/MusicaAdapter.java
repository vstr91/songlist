package br.com.vostre.songlist.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import br.com.vostre.songlist.databinding.LinhaArtistasBinding;
import br.com.vostre.songlist.databinding.LinhaMusicasBinding;
import br.com.vostre.songlist.model.Artista;
import br.com.vostre.songlist.model.Musica;
import br.com.vostre.songlist.model.pojo.MusicaQuantidade;
import br.com.vostre.songlist.view.viewHolder.ArtistaViewHolder;
import br.com.vostre.songlist.view.viewHolder.MusicaViewHolder;

public class MusicaAdapter extends RecyclerView.Adapter<MusicaViewHolder> {

    public List<MusicaQuantidade> musicas;
    Context ctx;

    public MusicaAdapter(List<MusicaQuantidade> musicas, Context context){
        this.musicas = musicas;
        ctx = context;
    }

    @Override
    public MusicaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        LinhaMusicasBinding itemBinding =
                LinhaMusicasBinding.inflate(layoutInflater, parent, false);
        return new MusicaViewHolder(itemBinding, ctx);
    }

    @Override
    public void onBindViewHolder(MusicaViewHolder holder, int position) {
        MusicaQuantidade musica = musicas.get(position);
        holder.bind(musica, position+1);
    }

    @Override
    public int getItemCount() {

        if(musicas == null){
            return 0;
        } else{
            return musicas.size();
        }


    }

}
