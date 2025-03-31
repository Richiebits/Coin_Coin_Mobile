package adapteur;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.coin_coin_mobile.R;

import modele.Depense;
import java.util.List;

public class DepenseAdapter extends ArrayAdapter<Depense> {
    private List<Depense> depenses;  // Liste des personnes à afficher
    private Context contexte;  // Contexte de l'application
    private int viewResourceId;  // Identifiant de la mise en page des éléments de la Liste
    private Resources ressources;  // Accès aux ressources de l'application

    public DepenseAdapter(@NonNull Context contexte, int viewResourceId, @NonNull List<Depense> depenses) {
        super(contexte, viewResourceId, depenses);
        this.contexte = contexte;
        this.viewResourceId = viewResourceId;
        this.ressources = contexte.getResources();
        this.depenses = depenses;
    }

    @Override
    public int getCount() {
        return this.depenses.size();
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

        // Récupération de l'objet depenses correspondant à la position actuelle
        final Depense depense = this.depenses.get(position);

        if (depense != null) {
            // Récupération des éléments graphiques définis dans le fichier XML de mise en page
            final TextView tvId = view.findViewById(R.id.tvId);
            final TextView tvDate = view.findViewById(R.id.tvDate);
            final TextView tvMontant = view.findViewById(R.id.tvMontant);

            // Affectation des valeurs aux TextViews
            tvId.setText(depense.getId());
            tvDate.setText(depense.getDate());
            tvMontant.setText(depense.getMontant());

        }
        return view; // Retourne la vue modifiée pour affichage dans la liste
    }
}