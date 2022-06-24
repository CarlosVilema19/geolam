package com.riobamba.geolam.modelo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.riobamba.geolam.Listado;
import com.riobamba.geolam.R;

import org.w3c.dom.Text;

import java.util.List;

public class ListadoLugarUsuarioAdaptador extends RecyclerView.Adapter<ListadoLugarUsuarioAdaptador.ViewHolder> {

    private final Context mCtx;
    private final List<ListadoLugarUsuario> lugarList;

    public interface OnItemClickListener{
        void onItemClick(ListadoLugarUsuario item);
    }

    public ListadoLugarUsuarioAdaptador(Context mCtx, List<ListadoLugarUsuario> lugarList){
        this.mCtx = mCtx;
        this.lugarList = lugarList;
    }
    View view1;
    public void viewEjem (View v)
    {
        this.view1 = v;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nombreLugar;
        private final TextView direccionLugar;
        private final TextView telefonoLugar;
        private final ImageView imagenLugar;
        private final TextView informacionLugar;
        private final TextView categoriaLugar;
        private final TextView tipologiaLugar;




        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            nombreLugar = view.findViewById(R.id.tvNombreLugar);
            direccionLugar = view.findViewById(R.id.tvDireccion);
            telefonoLugar = view.findViewById(R.id.tvTelefono);
            imagenLugar = view.findViewById(R.id.imagenlugar);
            informacionLugar = view.findViewById(R.id.tvInformacion);
            categoriaLugar = view.findViewById(R.id.tvCategoria);
            tipologiaLugar = view.findViewById(R.id.tvTipologia);


        }

        /*public TextView getTextView() {
            return textView;
        }*/
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_lugar_medico, null);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ListadoLugarUsuario listadoLugar = lugarList.get(position);
        //Cargar Imagen
        Glide.with(mCtx)
                .load(listadoLugar.getImagenLugar())
                .into(viewHolder.imagenLugar);
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.nombreLugar.setText(listadoLugar.getNombreLugar());
        viewHolder.direccionLugar.setText(listadoLugar.getDireccionLugar());
        viewHolder.telefonoLugar.setText(listadoLugar.getTelefonoLugar());
        viewHolder.informacionLugar.setText(listadoLugar.getInformacionLugar());
        viewHolder.categoriaLugar.setText(listadoLugar.getCategoriaLugar());
        viewHolder.tipologiaLugar.setText(listadoLugar.getTipologiaLugar());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return lugarList.size();
    }
}



