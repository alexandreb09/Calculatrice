package calc.isima.fr.tp1_modele_calculatrice;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

/**
 * Activity qui affiche la calculatrice.
 */
public class CalcActivity extends Activity {

    private static final String TAG = CalcActivity.class.getName();

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calc_activity);


    }

    /**
     * Création du menu.
     * @param menu Menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.calc, menu);

        return true;
    }

    /**
     * Méthode appelée lorsque l'utilisateur clique sur un des éléments du menu.
     * @param item Elément cliqué.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Méthode à appeler pour sauvegarder l'expression et son résultat dans l'historique, lorsque l'utilisateur clique
     * sur le bouton Save.
     */
    private void onSave() {
        // TODO
    }

    /**
     * Méthode à appeler lorsque l'utilisateur clique sur un nombre.
     * @param numberId Identifiant de la View cliquée.
     */
    private void onNumberClicked(int numberId) {
        // TODO
    }

    /**
     * Méthode à appeler lorsque l'utilisateur clique sur un opérateur, sur la virgule ou sur le bouton Supprimer.
     * @param operatorId Identifiant de la View cliquée.
     */
    private void onOperatorClicked(int operatorId) {
        // TODO
    }

    /**
     * Cette méthode doit mettre à jour l'expression et éventuellement rafraichir l'affichage du résultat.
     * @param val Nouveau caractère à ajouter à la fin de l'expression (un nombre, un opérateur ou une virgule)
     * @param isOperator True si le caractère est un opérateur (y compris une virgule), ou false si c'est un nombre.
     */
    private void updateExpression(String val, boolean isOperator) {
        if (isOperator && expressionEndsWithOperator()) {
            expression = StringUtils.substring(expression, 0, -1);
        }

        // TODO : Mettre à jour l'expression et la vue

        if (!isOperator) {
            updateResult();
        }
    }

    /**
     * Cette méthode doit indiquer si l'expression se termine par un opérateur.
     * @return True si l'expression se termine par un opérateur, false sinon.
     */
    private boolean expressionEndsWithOperator() {
        // TODO

        return false;
    }

    /**
     * Cette méthode doit évaluer l'expression mathématique afin de retourner un résultat sous forme d'une
     * chaîne de caractères.
     * @return Résultat.
     */
    private String evaluateExpression() {
        String eval = "";

        // TODO

        return eval;
    }

    /**
     * Cette méthode doit mettre à jour la View d'affichage du résultat.
     */
    private void updateResult() {
        // TODO Mettre à jour la vue
    }
}
