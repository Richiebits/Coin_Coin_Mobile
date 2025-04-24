package adapteur;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.coin_coin_mobile.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.List;

import modele.Projet;

public class ProjetAdapter extends ArrayAdapter {

    private List<Projet> projets;
    private Context context;
    private int viewResourceId;
    private Resources resources;

    public ProjetAdapter(@NonNull Context context, int viewResourceId, @NonNull List<Projet> projets) {
        super(context, viewResourceId, projets);
        this.context = context;
        this.resources = context.getResources();
        this.viewResourceId = viewResourceId;
        this.projets = projets;
    }
    @Override
    public int getCount(){return projets.size();}
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;
        if (view == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(this.viewResourceId, parent, false);
        }

        final Projet projet = this.projets.get(position);
        if (view != null){
            String nomP = projet.getNom();
            final TextView tvNomProjet = view.findViewById(R.id.tvNomProjet);
            final ImageView ivProjet = view.findViewById(R.id.imageProjet);
            tvNomProjet.setText(projet.getNom());
            if (nomP.equalsIgnoreCase("motoneige")){
                ivProjet.setImageResource(R.drawable.motoneige);
            } else if (nomP.equalsIgnoreCase("auto")){
                ivProjet.setImageResource(R.drawable.auto);
            }else if (nomP.equalsIgnoreCase("GPU")||nomP.equalsIgnoreCase("carte graphique")||nomP.equalsIgnoreCase("graphic card")){
                ivProjet.setImageResource(R.drawable.gpu);

            } else {
                ivProjet.setImageResource(R.drawable.dollar);
            }

        }
        return view;
    }
























}
