package com.example.coinquilini;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BolletteAdapter extends RecyclerView.Adapter<BolletteAdapter.ViewHolder>
{

    Context context;
    List<BolletteModel> bolletta_list;

    public BolletteAdapter(Context context,List<BolletteModel> bolletta_list)
    {
        this.context = context;
        this.bolletta_list = bolletta_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(bolletta_list != null && bolletta_list.size() > 0)
        {
            BolletteModel model = bolletta_list.get(position);
            holder.tipo_tv.setText(model.getTipo());
            holder.importo_tv.setText(model.getImporto());
        }
        else return;

    }

    @Override
    public int getItemCount() {
        return bolletta_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView tipo_tv;
        TextView importo_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tipo_tv = itemView.findViewById(R.id.tipo_tv);
            importo_tv = itemView.findViewById(R.id.importo_tv);

        }
    }

}
