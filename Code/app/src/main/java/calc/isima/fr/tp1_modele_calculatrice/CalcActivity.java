package calc.isima.fr.tp1_modele_calculatrice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.udojava.evalex.Expression;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;



/**
 * Activity qui affiche la calculatrice.
 */

public class CalcActivity extends Activity implements View.OnClickListener, View.OnLongClickListener {
    // private static final String TAG = CalcActivity.class.getName();                                 // TAG pour identifier classe dans les logs


    /** Nom du fichier dans lequel seront stockés les expressions et les résultats correspondants. */
    public static final String SAVE_FILE_NAME = "history";

    /** Identifiants des Views qui permettent de manipuler les chiffres 0 à 9. */
    private int[] numbers = {
            R.id.number1, R.id.number2, R.id.number3,
            R.id.number4, R.id.number5, R.id.number6,
            R.id.number7, R.id.number8, R.id.number9,
            R.id.number0
    };

    /** Identifiants des Views qui permettent de manipuler les opérateurs (+, -, *, /), la virgule et la touche de suppression. */
    private int[] operators = {
            R.id.opAdd, R.id.opMin,
            R.id.opMult, R.id.opDiv,
            R.id.opDel, R.id.comma
    };

    /** Expression mathématique créée par l'utilisateur (par exemple : 5+9/4) */
    private String expression = "";
    /** View qui permet d'afficher le résultat de l'évaluation de l'expression. */
    private TextView resultView;
    /** View qui permet d'afficher l'expression mathématique à l'utilisateur. */
    private TextView expressionView;

    Button myButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calc_activity);                                                     // Construction vue

        resultView = findViewById(R.id.resultat);                                                   // Association ResltView à la vue
        expressionView = findViewById(R.id.operation);                                              // Association expressionView à la vue

        for (int number : numbers) {                                                                // Pour tous les nombres
            myButton = findViewById(number);                                                        // Association du boutton à la vue
            myButton.setOnClickListener(this);                                                      // Ajout methode onClick
        }

        for (int pointer : operators) {                                                             // Pour chaque opérateurs
            myButton = findViewById(pointer);                                                       // Association du boutton à la vue
            myButton.setOnClickListener(this);                                                      // Ajout methode onClick
        }

        myButton = findViewById(R.id.save);                                                         // Association boutton save à la vue
        myButton.setOnClickListener(this);                                                          // Ajout methode onClick
        myButton  = findViewById(R.id.opDel);                                                       // Association boutton save à la vue
        myButton.setOnLongClickListener(this);                                                      // Ajout methode onLongClick
    }

    /**
     * Création du menu.
     * @param menu Menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.calc, menu);                                               // Transformation fichier menu <xml> en objet java
        return true;
    }

    /**
     * Méthode appelée lorsque l'utilisateur clique sur un des éléments du menu.
     * @param item Elément cliqué.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {                                                                 // Selon l'item selectionné dans le menu
            case (R.id.historique_id):                                                              // Si appuie sur item historique
                Intent historique_act = new Intent(this,HistoriqueActivity.class);     // Création nouvelle intent
                startActivity(historique_act);                                                      // Lancement nouvelle activité
                break;
            case (R.id.vider_historique_id):                                                        // Si appuie sur item vider historique
                viderHistorique();                                                                  // Suppression historique
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Action à exécuter lors de l'appui sur la vue
     * @param v : view sur laquelle on étudie un clique
     */
    @Override
    public void onClick(View v){
        int view_id = v.getId();                                                                    // Récupération ID

        if (ArrayUtils.contains(operators,view_id)){                                                // S'il s'agit d'un opérateur
            onOperatorClicked(view_id);
        }
        else if (R.id.save == view_id && !expression.equals("")){                                   // Si appuie sur bouton sauvegarde et expression non vide
            onSave();                                                                               // Sauvegarde calcul
        }
        else {                                                                                      // Sinon (appui sur un bouton)
            onNumberClicked(view_id);                                                               // Traitement appui sur bouton
        }
    }

    /**
     * Ré-écriture de la méthode onLongClick : remise à zero des opérations et résultats
     * @param v : view sur laquelle on étudie un long clicque
     * @return boolean : false
     */
    @Override
    public boolean onLongClick(View v) {
        expression = "";                                                                            // Réinitialisation expression
        resultView.setText("");                                                                     // Reinitialisation resultat dans la vue
        expressionView.setText("");                                                                 // Réinitialisation expression dans la vue
        return false;
    }


    /**
     * Méthode à appeler pour sauvegarder l'expression et son résultat dans l'historique, lorsque l'utilisateur clique
     * sur le bouton Save.
     */
    private void onSave() {
        try {
            String resultat = resultView.getText().toString();                                      // Récupération résultat depuis la vue
            // Note : On aurait pu le récupérer depuis le tableau des ID

            String ope_sauvegardee = expression.concat("\t").concat(resultat).concat("\n");         // Opération qui est sauvegardée

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.openFileOutput(SAVE_FILE_NAME, Context.MODE_APPEND));   // Ouverture fichier
            outputStreamWriter.write(ope_sauvegardee);                                              // Ecriture dans le fichier

            IOUtils.closeQuietly(outputStreamWriter);                                               // Fermuture fichier

            String toastTextReussie = getString(R.string.toast_sauvegarde_reussi);                                       // Message d'information sauvegarde réussie
            display_toast(toastTextReussie);                                                        // Création notification (toast)
        } catch (IOException e) {                                                                   // Si une erreur se produit
            display_toast(getString(R.string.toast_sauvegarde_echouee));                                                 // Avertissement de l'utilisateur
        }
    }

    /**
     * Fonction utilitaire permettant d'afficher un toast
     * @param toast_message Message à afficher dans le toast
     */
    public void display_toast(String toast_message){
        Toast.makeText(this, toast_message, Toast.LENGTH_LONG).show();                       // Création toast
    }

    /**
     * Méthode effacant l'historique : supprime le fichier de sauvegarde et en informe l'utilisateur
     */
    public void viderHistorique(){
        File file = new File(getFilesDir(), SAVE_FILE_NAME);                                        // Création objet Fichier associé au fichier txt
        boolean deleted = file.delete();                                                            // Suppression fichier
        String resultMessage = getString(R.string.toast_supp_reussie);                                             // Message d'information par défaut (réussite)
        if (!deleted){                                                                              // Si la suppression a échoué
            resultMessage = getString(R.string.toast_supp_echouee);                  // Message d'erreur
        }
        display_toast(resultMessage);                                                               // Information utilisateur du résultat de la suppression
    }

    /**
     * Méthode à appeler lorsque l'utilisateur clique sur un nombre.
     * @param numberId Identifiant de la View cliquée.
     */
    private void onNumberClicked(int numberId) {
        Button b = findViewById(numberId);                                                          // Récupération du bouton
        String charToAdd = b.getText().toString();                                                  // Récupération du text à partir du texte afficher dans la vue
        updateExpression(charToAdd, false);                                                // MAJ expression
    }

    /**
     * Méthode appelée lorsque l'utilisateur clique sur un opérateur, sur la virgule ou sur le bouton Supprimer.
     * @param operatorId Identifiant de la View cliquée.
     */
    private void onOperatorClicked(int operatorId) {
        Button b = findViewById(operatorId);                                                        // Récupération bouton associé à la vue
        String charToAdd = b.getText().toString();                                                  // Récupération du texte à partir du texte afficher dans la vue
        if (expression.equals("")){                                                                 // Si expression vide
            if (operatorId == operators[1]){                                                        // seul soustraction est possible (nb negatif)
                updateExpression(charToAdd, true);                                        // Traitement nombre
            }
        }
        else {
            if (operators[4] == operatorId){                                                        // Si appui sur suppression
                if (!expression.equals("")) {                                                       // Si l'expression est non vide
                    expression = expression.substring(0, expression.length() - 1);                  // Suppression dernier element
                    expressionView.setText(expression);                                             // MAJ Expression
                    if (expression.equals("") || (!expression.equals("") && expressionEndsWithOperator() )){
                        resultView.setText("");                                                     // MAJ résultat
                    }
                    else {
                        updateResult();                                                             // MAJ résultat
                    }
                }
            }
            else if (operators[5] == operatorId){                                                   // S'il s'agit d'une virgule
                if (ajoutCommaPossible()){                                                          // Si l'ajout est possible
                    updateExpression(".", true);                                       // Applique traitement
                }
            }
            else{                                                                                   // Pour les autres opérateurs
                updateExpression(charToAdd, true);                                        // Ajout normal
            }
        }
    }

    /**
     * Cette méthode doit mettre à jour l'expression et éventuellement rafraichir l'affichage du résultat.
     * @param val Nouveau caractère à ajouter à la fin de l'expression (un nombre, un opérateur ou une virgule)
     * @param isOperator True si le caractère est un opérateur (y compris une virgule), ou false si c'est un nombre.
     */
    private void updateExpression(String val, boolean isOperator) {
        // Si on ajoute un opérateur différent du point et que l'expression se termine par un opérateur
        // (si on ajoute par un point, on ajoute l'ajoutera sans supprimer le dernier élément
        if (isOperator && expressionEndsWithOperator() && !val.equals(".")){
            expression = StringUtils.substring(expression, 0, -1);                        // On supprime le dernier élément
        }
        expression += val;                                                                          // Ajout nouveau caractère
        expressionView.setText(expression);                                                         // MAJ affichage

        if (!isOperator) {                                                                          // Si on ajoute un nombre
            updateResult();                                                                         // MAJ résultat
        }
    }

    /**
     * Cette méthode doit indiquer si l'expression se termine par un opérateur.
     * @return True si l'expression se termine par un opérateur, false sinon.
     */
    private boolean expressionEndsWithOperator() {
        boolean rep = true;
        if (!expression.equals("")) {
            int lenght = expression.length();                                                       // Longueur expression
            char last_letter = expression.charAt(lenght - 1);                                       // Dernier caractère
            rep = isOperator(last_letter);                                                          // est il un opérateur
        }
        return rep;
    }

    /**
     * Cette méthode doit évaluer l'expression mathématique afin de retourner un résultat sous forme d'une
     * chaîne de caractères.
     * @return Résultat.
     */
    private String evaluateExpression() {
        String eval = "";                                                                           // Initialisation résultat
        if (!expression.equals("")){                                                                // Si expression non vide
            try{                                                                                    // Tentative ...
                eval += new Expression(expression).eval().toPlainString();                          // ... d'évaluation
            }
            catch (Exception e){                                                                    // Si erreur
                eval = getString(R.string.erreur);                                                                  // message d'erreur retourné
            }
        }
        return eval;
    }

    /**
     * Cette méthode doit mettre à jour la View d'affichage du résultat.
     */
    private void updateResult() {
        resultView.setText(evaluateExpression());                                                   // MAJ vue du résultat de l'évaluation
    }

    /**
     * Vérifier si un caractère est un opérateur
     * @param c : caractère à tester
     * @return boolean
     */
    private boolean isOperator(char c){
        return c == '*' || c == '/' || c == '-' ||c == '+';
    }

    /**
     * Indique si l'ajout d'une virgule est possible
     * @return boolean
     */
    private boolean ajoutCommaPossible(){
        int i = expression.length()-1;                                                              // index dernier élément
        boolean rep = true;                                                                         // init réponse
        // Tant que l'on est pas au début et que l'on ne trouve ni d'opérateur ni de point
        while (i >=0 && expression.charAt(i) != '.' && !isOperator(expression.charAt(i))){
            i--;                                                                                    // On remonte la chaine de caract
        }
        if (i>=0){                                                                                  // Si on n'est pas au début
            rep = expression.charAt(i) != '.';                                                      // On regarde si l'élément sur lequel on est un point
        }
        return rep;
    }
}
