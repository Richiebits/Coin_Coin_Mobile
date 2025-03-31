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

import java.util.List;
import modele.Retrait;

public class RetraitAdapter extends ArrayAdapter<Retrait> {
    private List<Retrait> retraits;  // Liste des personnes à afficher
    private Context contexte;  // Contexte de l'application
    private int viewResourceId;  // Identifiant de la mise en page des éléments de la Liste
    private Resources ressources;  // Accès aux ressources de l'application

    public RetraitAdapter(@NonNull Context contexte, int viewResourceId, @NonNull List<Retrait> retraits) {
        super(contexte, viewResourceId, retraits);
        this.contexte = contexte;
        this.viewResourceId = viewResourceId;
        this.ressources = contexte.getResources();
        this.retraits = retraits;
    }

    @Override
    public int getCount() {
        return this.retraits.size();
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

        // Récupération de l'objet retraits correspondant à la position actuelle
        final Retrait retrait = this.retraits.get(position);

        if (retrait != null) {
            // Récupération des éléments graphiques définis dans le fichier XML de mise en page
            final TextView tvId = view.findViewById(R.id.tvId);
            final TextView tvDate = view.findViewById(R.id.tvDate);
            final TextView tvMontant = view.findViewById(R.id.tvMontant);

            // Affectation des valeurs aux TextViews
            tvId.setText(retrait.getId());
            tvDate.setText(retrait.getDate());
            tvMontant.setText(retrait.getMontant());

        }
        return view; // Retourne la vue modifiée pour affichage dans la liste
    }
}
