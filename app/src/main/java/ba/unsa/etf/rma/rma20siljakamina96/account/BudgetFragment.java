package ba.unsa.etf.rma.rma20siljakamina96.account;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import ba.unsa.etf.rma.rma20siljakamina96.OnSwipeTouchListener;
import ba.unsa.etf.rma.rma20siljakamina96.R;

public class BudgetFragment extends Fragment implements IAccountView{
    private TextView budgetText;
    private TextView totalLimitText;
    private TextView monthLimitText;

    private Button saveButton;

    private OnSwipeLeft onSwipeLeft;
    private OnSwipeRight onSwipeRight;

    private IAccountPresenter accountPresenter;

    public IAccountPresenter getAccountPresenter() {
        if (accountPresenter == null) {
            accountPresenter = new AccountPresenter(getActivity(), this);
        }
        return accountPresenter;
    }
    public interface OnSwipeLeft {
        void openGraphsFragmentFromBudget();
    }
    public interface OnSwipeRight{
        void openListFragmentFromBudget();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_budget, container, false);
        budgetText = (TextView)fragmentView.findViewById(R.id.budgetView);
        totalLimitText = (TextView)fragmentView.findViewById(R.id.totalLimitView);
        monthLimitText = (TextView)fragmentView.findViewById(R.id.monthLimitView);

        saveButton = (Button)fragmentView.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(saveClickListener);

        onSwipeLeft = (OnSwipeLeft) getActivity();
        onSwipeRight = (OnSwipeRight) getActivity();

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            fragmentView.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
                @Override
                public void onSwipeLeft() {
                    onSwipeLeft.openGraphsFragmentFromBudget();
                }
                public void onSwipeRight() {
                    onSwipeRight.openListFragmentFromBudget();
                }
            });
        }
        getAccountPresenter().setAccountData();
        return fragmentView;
    }
    private AdapterView.OnClickListener saveClickListener = new AdapterView.OnClickListener() {
        @Override
        public void onClick(View v) {
            accountPresenter.modifyAccount(Double.parseDouble(totalLimitText.getText().toString()), Double.parseDouble(monthLimitText.getText().toString()));
        }
    };
    @Override
    public void setLimits(double totalLimit, double monthLimit) {
        totalLimitText.setText(String.valueOf(totalLimit));
        monthLimitText.setText(String.valueOf(monthLimit));
    }
    @Override
    public void setBudget(String budget) {
        budgetText.setText(budget);
    }
}
