package ba.unsa.etf.rma.rma20siljakamina96.graphs;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    private RadioButton monthButton;

    private BarChart consumptionBarChart;
    private BarChart earningsBarChart;
    private BarChart totalChart;

    public IGraphsPresenter getPresenter() {
        if (presenter == null) {
            presenter = new GraphsPresenter(this, getActivity());
        }
        return presenter;
    }

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
        totalChart = (BarChart)fragmentView.findViewById(R.id.totalChart);

        radioGroup = (RadioGroup) fragmentView.findViewById(R.id.radioGroup);
        monthButton = (RadioButton)fragmentView.findViewById(R.id.month);

        monthButton.setChecked(true);
        getPresenter().getData("Month");

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i) {
                    case R.id.day:
                        presenter.getData("Day");
//                        getPresenter().putConsumptionDataToBarData("Day");
//                        getPresenter().putEarningsDataToBarData("Day");
//                        getPresenter().putTotalDataToBarData("Day");
                        break;
                    case R.id.week:
                        presenter.getData("Week");
//                        getPresenter().putConsumptionDataToBarData("Week");
//                        getPresenter().putEarningsDataToBarData("Week");
//                        getPresenter().putTotalDataToBarData("Week");
                        break;
                    case R.id.month:
                        presenter.getData("Month");
//                        getPresenter().putConsumptionDataToBarData("Month");
//                        getPresenter().putEarningsDataToBarData("Month");
//                        getPresenter().putTotalDataToBarData("Month");
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

    @Override
    public void setTotalBarChart(BarData barData) {
        totalChart.setData(barData);
        totalChart.setFitBars(true); // make the x-axis fit exactly all bars
        totalChart.invalidate(); // refresh
    }
}
