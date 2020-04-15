package ba.unsa.etf.rma.rma20siljakamina96.graphs;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;

import ba.unsa.etf.rma.rma20siljakamina96.OnSwipeTouchListener;
import ba.unsa.etf.rma.rma20siljakamina96.R;

public class GraphsFragment extends Fragment implements IGraphsView{
    private OnSwipeLeft onSwipeLeft;
    private OnSwipeRight onSwipeRight;
    private IGraphsPresenter presenter;

    private RadioGroup radioGroup;
    private RadioButton dayButton;
    private RadioButton weekButton;
    private RadioButton monthButton;

    public IGraphsPresenter getPresenter() {
        if (presenter == null) {
            presenter = new GraphsPresenter(this, getActivity());
        }
        return presenter;
    }
    private BarChart consumptionBarChart;
    private BarChart earningsBarChart;

    public interface OnSwipeLeft {
        void openListFragmentFromGraphs();
    }
    public interface OnSwipeRight {
        void openBudgetFragmentFromGraphs();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_graph, container, false);

        onSwipeLeft = (OnSwipeLeft) getActivity();
        onSwipeRight = (OnSwipeRight) getActivity();

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            fragmentView.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
                @Override
                public void onSwipeLeft() {
                    onSwipeLeft.openListFragmentFromGraphs();
                }
                @Override
                public void onSwipeRight() {
                    onSwipeRight.openBudgetFragmentFromGraphs();
                }
            });
        }

        consumptionBarChart = (BarChart) fragmentView.findViewById(R.id.consumptionChart);
        earningsBarChart = (BarChart) fragmentView.findViewById(R.id.earningsChart);

        radioGroup = (RadioGroup) fragmentView.findViewById(R.id.radioGroup);
        dayButton = (RadioButton) fragmentView.findViewById(R.id.day);
        weekButton = (RadioButton)fragmentView.findViewById(R.id.week);
        monthButton = (RadioButton)fragmentView.findViewById(R.id.month);

        monthButton.setChecked(true);
        getPresenter().putConsumptionDataToBarData("Month");
        getPresenter().putEarningsDataToBarData("Month");

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i) {
                    case R.id.day:
                        getPresenter().putConsumptionDataToBarData("Day");
                        getPresenter().putEarningsDataToBarData("Day");
                        break;
                    case R.id.week:
                        getPresenter().putConsumptionDataToBarData("Week");
                        getPresenter().putEarningsDataToBarData("Week");
                        break;
                    case R.id.month:
                        getPresenter().putConsumptionDataToBarData("Month");
                        getPresenter().putEarningsDataToBarData("Month");
                        break;
                }
            }
        });
        return fragmentView;
    }
    @Override
    public void setConsumptionBarChart(BarData barData) {
        consumptionBarChart.setData(barData);
        consumptionBarChart.setFitBars(true); // make the x-axis fit exactly all bars
        consumptionBarChart.invalidate(); // refresh
    }

    @Override
    public void setEarningsBarChart(BarData barData) {
        earningsBarChart.setData(barData);
        earningsBarChart.setFitBars(true); // make the x-axis fit exactly all bars
        earningsBarChart.invalidate(); // refresh
    }
}
