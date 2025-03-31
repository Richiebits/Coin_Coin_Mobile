package adapteur;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.coin_coin_mobile.R;

import modele.Depot;
import java.util.List;

public class DepotAdapter extends ArrayAdapter<Depot> {
    private List<Depot> depots;  // Liste des personnes à afficher
    private Context contexte;  // Contexte de l'application
    private int viewResourceId;  // Identifiant de la mise en page des éléments de la Liste
    private Resources ressources;  // Accès aux ressources de l'application

    public DepotAdapter(@NonNull Context contexte, int viewResourceId, @NonNull List<Depot> depots) {
        super(contexte, viewResourceId, depots);
        this.contexte = contexte;
        this.viewResourceId = viewResourceId;
        this.ressources = contexte.getResources();
        this.depots = depots;
    }

    @Override
    public int getCount() {
        return this.depots.size();
    }


    @SuppressLint("NewApi")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        // Si la vue n'est pas réutilisable, on l'inflate (création de la vue à partir du fichier XML)
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) contexte.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(this.viewResourceId, parent, false);
        }

        // Récupération de l'objet depots correspondant à la position actuelle
        final Depot depot = this.depots.get(position);

        if (depot != null) {
            // Récupération des éléments graphiques définis dans le fichier XML de mise en page
            final TextView tvId = view.findViewById(R.id.tvId);
            final TextView tvDate = view.findViewById(R.id.tvDate);
            final TextView tvMontant = view.findViewById(R.id.tvMontant);

            // Affectation des valeurs aux TextViews
            tvId.setText(depot.getId());
            tvDate.setText(depot.getDate());
            tvMontant.setText(depot.getMontant());

        }
        return view; // Retourne la vue modifiée pour affichage dans la liste
    }
}