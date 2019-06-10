package br.com.vostre.songlist.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import br.com.vostre.songlist.databinding.LinhaMusicasBinding;
import br.com.vostre.songlist.databinding.LinhaPlaylistsBinding;
import br.com.vostre.songlist.model.pojo.MusicaQuantidade;
import br.com.vostre.songlist.model.pojo.Playlist;
import br.com.vostre.songlist.view.viewHolder.MusicaViewHolder;
import br.com.vostre.songlist.view.viewHolder.PlaylistViewHolder;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistViewHolder> {

    public List<Playlist> playlists;
    Context ctx;

    public PlaylistAdapter(List<Playlist> playlists, Context context){
        this.playlists = playlists;
        ctx = context;
    }

    @Override
    public PlaylistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        LinhaPlaylistsBinding itemBinding =
                LinhaPlaylistsBinding.inflate(layoutInflater, parent, false);
        return new PlaylistViewHolder(itemBinding, ctx);
    }

    @Override
    public void onBindViewHolder(PlaylistViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);
        holder.bind(playlist);
    }

    @Override
    public int getItemCount() {

        if(playlists == null){
            return 0;
        } else{
            return playlists.size();
        }


    }

}
