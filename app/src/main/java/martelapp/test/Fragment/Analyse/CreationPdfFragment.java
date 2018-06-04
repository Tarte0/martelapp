package martelapp.test.Fragment.Analyse;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import martelapp.test.R;

/**
 * Fragment utilisé pour le popUp pour afficher "Création des pdf en cours"
 */

public class CreationPdfFragment extends DialogFragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.view_page_creation_pdf, null);

        return view;
    }
}
