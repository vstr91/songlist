package br.com.vostre.songlist.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import br.com.vostre.songlist.databinding.LinhaArtistasBinding;
import br.com.vostre.songlist.model.Artista;
import br.com.vostre.songlist.view.viewHolder.ArtistaViewHolder;

public class ArtistaAdapter extends RecyclerView.Adapter<ArtistaViewHolder> {

    public List<Artista> artistas;
    Context ctx;

    public ArtistaAdapter(List<Artista> artistas, Context context){
        this.artistas = artistas;
        ctx = context;
        setHasStableIds(true);
    }

    @Override
    public ArtistaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        LinhaArtistasBinding itemBinding =
                LinhaArtistasBinding.inflate(layoutInflater, parent, false);
        return new ArtistaViewHolder(itemBinding, ctx);
    }

    @Override
    public void onBindViewHolder(ArtistaViewHolder holder, int position) {
        Artista artista = artistas.get(position);
        holder.bind(artista);
    }

    @Override
    public int getItemCount() {

        if(artistas == null){
            return 0;
        } else{
            return artistas.size();
        }


    }

}
