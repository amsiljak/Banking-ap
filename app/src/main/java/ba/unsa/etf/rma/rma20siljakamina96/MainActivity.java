package ba.unsa.etf.rma.rma20siljakamina96;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.Calendar;

import ba.unsa.etf.rma.rma20siljakamina96.account.BudgetFragment;
import ba.unsa.etf.rma.rma20siljakamina96.data.Account;
import ba.unsa.etf.rma.rma20siljakamina96.data.Transaction;
import ba.unsa.etf.rma.rma20siljakamina96.detail.TransactionDetailFragment;
import ba.unsa.etf.rma.rma20siljakamina96.graphs.GraphsFragment;
import ba.unsa.etf.rma.rma20siljakamina96.list.TransactionListFragment;
import ba.unsa.etf.rma.rma20siljakamina96.util.ConnectivityBroadcastReceiver;

public class MainActivity extends AppCompatActivity implements TransactionListFragment.OnItemClick, TransactionListFragment.OnAddButtonClick,
        TransactionDetailFragment.OnTransactionModify, TransactionDetailFragment.OnTransactionAddOrDelete,
        TransactionListFragment.OnSwipeLeft, TransactionListFragment.OnSwipeRight,
        BudgetFragment.OnSwipeLeft, BudgetFragment.OnSwipeRight,
        GraphsFragment.OnSwipeRight, GraphsFragment.OnSwipeLeft, TransactionDetailFragment.OnAddButtonClick, ConnectivityBroadcastReceiver.MyBroadcastListener {

    private ConnectivityBroadcastReceiver receiver = new ConnectivityBroadcastReceiver(this);
    private IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");

    private boolean twoPaneMode=false;
    private TransactionListFragment listFragment;
    private TransactionDetailFragment detailFragment;
    private BudgetFragment budgetFragment;
    private GraphsFragment graphsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //dohvatanje FragmentManager-a
        FragmentManager fragmentManager = getSupportFragmentManager();
        FrameLayout details = findViewById(R.id.transaction_detail);
        //slucaj layouta za ˇsiroke ekrane
        if (details != null) {
            twoPaneMode = true;
            detailFragment = (TransactionDetailFragment)
                    fragmentManager.findFragmentById(R.id.transaction_detail);
            //provjerimo da li je fragment detalji vec kreiran
            if (detailFragment==null) {
            //kreiramo novi fragment FragmentDetalji ukoliko vec nije kreiran
                detailFragment = new TransactionDetailFragment();
                fragmentManager.beginTransaction().
                        replace(R.id.transaction_detail,detailFragment)
                        .commit();
            }

        } else {
            twoPaneMode = false;
        }
        //Dodjeljivanje fragmenta MovieListFragment
        listFragment = (TransactionListFragment)
                fragmentManager.findFragmentByTag("list");
        //provjerimo da li je ve´c kreiran navedeni fragment
        if (listFragment==null){
            //ukoliko nije, kreiramo
            listFragment = new TransactionListFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.transactions_list,listFragment,"list")
                    .commit();
        }else{
            //sluˇcaj kada mijenjamo orijentaciju uredaja
            //iz portrait (uspravna) u landscape (vodoravna)
            //a u aktivnosti je bio otvoren fragment MovieDetailFragment
            //tada je potrebno skinuti MovieDetailFragment sa steka
            //kako ne bi bio dodan na mjesto fragmenta MovieListFragment
            fragmentManager.popBackStack(null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

    }

    @Override
    public void onResume() {

        super.onResume();
        registerReceiver(receiver, filter);

    }


    @Override
    public void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    public void onItemClicked(Transaction transaction, String change) {
        //Priprema novog fragmenta FragmentDetalji
        Bundle arguments = new Bundle();
        arguments.putParcelable("transaction", transaction);
        if(change != null) arguments.putString("change", change);
        TransactionDetailFragment detailFragment = new TransactionDetailFragment();
        detailFragment.setArguments(arguments);
        if (twoPaneMode){
            //Sluˇcaj za ekrane sa ˇsirom dijagonalom
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transaction_detail, detailFragment)
                    .commit();
        }
        else{
            //Sluˇcaj za ekrane sa poˇcetno zadanom ˇsirinom
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transactions_list,detailFragment)
                    .addToBackStack(null)
                    .commit();
            //Primijetite liniju .addToBackStack(null)
        }
    }
    @Override
    public void onAddButtonClicked(Account account) {
        Bundle arguments = new Bundle();
        arguments.putParcelable("account", account);
        detailFragment = new TransactionDetailFragment();
        detailFragment.setArguments(arguments);
        if (twoPaneMode){
            //Sluˇcaj za ekrane sa ˇsirom dijagonalom
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transaction_detail, detailFragment)
                    .commit();
        }
        else{
            //Sluˇcaj za ekrane sa poˇcetno zadanom ˇsirinom
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transactions_list,detailFragment)
                    .addToBackStack(null)
                    .commit();
            //Primijetite liniju .addToBackStack(null)
        }
    }

    @Override
    public void onTransactionModified() {
        listFragment.getTransactionPresenter().getTransactions("All","Price - Ascending", Calendar.getInstance());
        listFragment.getTransactionPresenter().setAccount();
    }


    void openList() {
        Bundle arguments = new Bundle();
        listFragment = new TransactionListFragment();
        listFragment.setArguments(arguments);
        if (twoPaneMode){
            //Sluˇcaj za ekrane sa ˇsirom dijagonalom
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transactions_list, listFragment)
                    .commit();
        }
        else{
            //Sluˇcaj za ekrane sa poˇcetno zadanom ˇsirinom
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transactions_list,listFragment)
                    .addToBackStack(null)
                    .commit();
            //Primijetite liniju .addToBackStack(null)
        }
    }
    void openBudgetDetails() {
        Bundle arguments = new Bundle();
        budgetFragment = new BudgetFragment();
        budgetFragment.setArguments(arguments);
        if (twoPaneMode){
            //Sluˇcaj za ekrane sa ˇsirom dijagonalom
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transactions_list, budgetFragment)
                    .commit();
        }
        else{
            //Sluˇcaj za ekrane sa poˇcetno zadanom ˇsirinom
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transactions_list,budgetFragment)
                    .addToBackStack(null)
                    .commit();
            //Primijetite liniju .addToBackStack(null)
        }
    }
    void openGraphs() {
        Bundle arguments = new Bundle();
        graphsFragment = new GraphsFragment();
        graphsFragment.setArguments(arguments);
        if (twoPaneMode){
            //Sluˇcaj za ekrane sa ˇsirom dijagonalom
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transactions_list,graphsFragment)
                    .commit();
        }
        else{
            //Sluˇcaj za ekrane sa poˇcetno zadanom ˇsirinom
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transactions_list,graphsFragment)
                    .addToBackStack(null)
                    .commit();
            //Primijetite liniju .addToBackStack(null)
        }
    }

    @Override
    public void openListFragmentFromGraphs() {
        openList();
    }

    @Override
    public void openBudgetFragmentFromGraphs() {
        openBudgetDetails();
    }

    @Override
    public void openGraphsFragmentFromBudget() {
        openGraphs();
    }

    @Override
    public void openListFragmentFromBudget() {
        openList();
    }

    @Override
    public void openBudgetFragmentFromList() {
        openBudgetDetails();
    }

    @Override
    public void openGraphsFragmentFromList() {
        openGraphs();
    }

    @Override
    public void onTransactionAddedOrDeleted() {
        openList();
    }

    @Override
    public void doSomething() {
        listFragment.uploadToServis();
        budgetFragment.uploadToServis();
    }
}
