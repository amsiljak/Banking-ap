package ba.unsa.etf.rma.rma20siljakamina96;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import android.widget.FrameLayout;
public class MainActivity extends AppCompatActivity implements TransactionListFragment.OnItemClick, TransactionListFragment.OnAddButtonClick,
        TransactionDetailFragment.OnTransactionModify {
    private boolean twoPaneMode=false;
    TransactionListFragment listFragment;
    TransactionDetailFragment detailFragment;

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
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onItemClicked(Transaction transaction) {
        //Priprema novog fragmenta FragmentDetalji
        Bundle arguments = new Bundle();
        arguments.putParcelable("transaction", transaction);
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
    public void onAddButtonClicked() {
        Bundle arguments = new Bundle();
//        arguments.putParcelable("transaction", transaction);
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
    public void onTransactionModified() {
        listFragment.getPresenter().setTransactions();
        listFragment.getPresenter().setAccount();
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Transaction t = (Transaction) transactionListView.getItemAtPosition(pozicija); //ovo je transakcija u cije smo detalje usli
//        if (requestCode == 1) {
//            if(resultCode == RESULT_OK) {
//                if(data.getStringExtra("action").equals("delete")) financePresenter.deleteTransaction(t);
//                else if(data.getStringExtra("action").equals("save")) {
//                    Transaction transaction = new Transaction((Date) data.getSerializableExtra("date"), data.getDoubleExtra("amount", 0),
//                            data.getStringExtra("title"), (Type) data.getSerializableExtra("type"), data.getStringExtra("description"),
//                            data.getIntExtra("interval", 0),
//                            (Date) data.getSerializableExtra("enddate"));
//                    financePresenter.modifyTransaction(t, transaction);
//                }
//                //ako je korisnik samo izasao bez klika na save ne treba nista uraditi
//            }
//        }
//        //dodavanje nove transakcije
//        else if(requestCode == 2) {
//            if(resultCode == RESULT_OK) {
//                //ovaj uslov je samo u jednom slucaju zadovoljen, a to je da je korisnik klikuno save pri dodavanju nove transakcije
//                if(data.getStringExtra("action").equals("add")) {
//                    Transaction transaction = new Transaction((Date)data.getSerializableExtra("date"), data.getDoubleExtra("amount", 0),
//                            data.getStringExtra("title"), (Type) data.getSerializableExtra("type"), data.getStringExtra("description"),
//                            data.getIntExtra("interval", 0),
//                            (Date) data.getSerializableExtra("enddate"));
//                    financePresenter.addTransaction(transaction);
//                }
//            }
//            //ako je korisnik izasao na dugme back ne treba nista uraditi
//        }
//    }
//    private AdapterView.OnClickListener addTransactionClickListenr = new AdapterView.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Intent transactionDetailIntent = new Intent(MainActivity.this, TransactionDetailActivity.class);
//            transactionDetailIntent.putExtra("calling-activity", 2);
//            double totalPayments = 0;
//            for(Map.Entry <String,Double> el : financePresenter.getMonthlyPayments().entrySet()) {
//                //zbrajam sve potrosnje u toku svih mjeseci radi total limita
//                totalPayments += el.getValue();
//                //saljem podatke o potrosnjama u svim mjesecima u kojima ima potrosnje ne ukljucujuci transakciju koja se modifikuje
//                transactionDetailIntent.putExtra(el.getKey(), el.getValue());
//            }
//            transactionDetailIntent.putExtra("totalPayments", totalPayments);
//            MainActivity.this.startActivityForResult(transactionDetailIntent, 2);
//        }
//    };


//    private AdapterView.OnItemClickListener transactionListItemClickListener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            Intent transactionDetailIntent = new Intent(MainActivity.this, TransactionDetailActivity.class);
//            Transaction transaction = transactionListAdapter.getTransaction(position);
//
//            transactionDetailIntent.putExtra("title", transaction.getTitle());
//            transactionDetailIntent.putExtra("amount", transaction.getAmount());
//            transactionDetailIntent.putExtra("description", transaction.getItemDescription());
//            transactionDetailIntent.putExtra("type", transaction.getType());
//            transactionDetailIntent.putExtra("interval", transaction.getTransactionInterval());
//            transactionDetailIntent.putExtra("date", transaction.getDate());
//            transactionDetailIntent.putExtra("enddate", transaction.getEndDate());
//            transactionDetailIntent.putExtra("calling-activity", 1);
//            double totalPayments = 0;
//            for(Map.Entry <String,Double> el : financePresenter.getMonthlyPayments().entrySet()) {
//                //zbrajam sve potrosnje u toku svih mjeseci radi total limita
//                totalPayments += el.getValue();
//                //saljem podatke o potrosnjama u svim mjesecima u kojima ima potrosnje ne ukljucujuci transakciju koja se modifikuje
//                transactionDetailIntent.putExtra(el.getKey(), el.getValue() - transaction.getAmount());
//            }
//            transactionDetailIntent.putExtra("totalPayments", totalPayments);
//            pozicija = position;
//
//            MainActivity.this.startActivityForResult(transactionDetailIntent, 1);
//        }
//    };
}
