package calc.isima.fr.tp1_modele_calculatrice;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static calc.isima.fr.tp1_modele_calculatrice.CalcActivity.SAVE_FILE_NAME;

public class HistoriqueActivity extends ListActivity {
    public static final String INFO_EXPRESSION = "Opération";
    public static final String INFO_RESULT = "Resultat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_activity);

        String[] from = {INFO_EXPRESSION, INFO_RESULT};                                             // Champ pour lire le MAP
        int[] to = {android.R.id.text1, android.R.id.text2};                                        // ID des textview dans la liste

        List<Map<String, String>> data = getHistory();                                              // Lecture de l'historique

        SimpleAdapter simpleAdapter;                                                                // déclaration simpleadapter
        simpleAdapter = new SimpleAdapter(this,                                              // Instantiation adapter
                        data,
                        android.R.layout.simple_list_item_2,
                        from,
                        to);
        setListAdapter(simpleAdapter);                                                              // Application de l'adapter
    }

    /**
     * Lire le fichier d'historique et retourner des données structurées lisible pour l'adapter
     * @return listCalculs : liste des opérations sauvegardées dans l'historique
     */
    public List<Map<String, String>> getHistory(){
        List<Map<String, String>> listCalculs = new ArrayList<>();                                  // Déclaration listCalculs
        String[] info;
        Map<String, String> map;
        try {
            FileInputStream fileInputStream = openFileInput(SAVE_FILE_NAME);                        // Ouverture fichier (si inexistant voir catch)
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream)); // Déclaration buffer de lecture

            String line_cour = bufferedReader.readLine();                                           // Lecture de la ligne
            while (line_cour != null) {                                                             // tant que l'on est pas à la fin
                info = line_cour.split("\t");                                                 // Récupération des champs calculs et résultats
                map = new HashMap<>();                                                              // création nouvelle map
                map.put(INFO_EXPRESSION, info[0]);                                                  // Ajout expression dans map
                map.put(INFO_RESULT, info[1]);                                                      // Ajout resultat dans map
                listCalculs.add(map);                                                               // Ajout map dans liste calculs

                line_cour = bufferedReader.readLine();                                              // Lecture ligne suivante
            }
            IOUtils.closeQuietly(fileInputStream);                                                  // fereture fichier de lecture
        } catch (FileNotFoundException ignored){                                                    // Si fichier inexistant
            // Ne pas générer le toast si le fichier n'existe pas
        } catch ( IOException ioe ) {                                                               // Si erreur dans la lecture du fichier
            String toast_message = getString(R.string.toast_erreur_lecture_hist);                        // Récupération message erreur
            Toast.makeText(this, toast_message, Toast.LENGTH_LONG).show();                   // Information user erreur lecture
        }
        return listCalculs;
    }
}
