package ba.unsa.etf.rma.rma20siljakamina96.graphs;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;

import ba.unsa.etf.rma.rma20siljakamina96.OnSwipeTouchListener;
import ba.unsa.etf.rma.rma20siljakamina96.R;

public class GraphsFragment extends Fragment implements IGraphsView{
    private OnSwipeLeft onSwipeLeft;
    private OnSwipeRight onSwipeRight;
    private ConstraintLayout graphsLayout;
    private IGraphsPresenter presenter;

    public IGraphsPresenter getPresenter() {
        if (presenter == null) {
            presenter = new GraphsPresenter(this, getActivity());
        }
        return presenter;
    }
    private BarChart consumptionBarChart;
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
        graphsLayout = (ConstraintLayout) fragmentView.findViewById(R.id.graphsLayout);

        graphsLayout.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            @Override
            public void onSwipeLeft() {
                onSwipeLeft.openListFragmentFromGraphs();
            }
            @Override
            public void onSwipeRight() {
                onSwipeRight.openBudgetFragmentFromGraphs();
            }
        });

        consumptionBarChart = (BarChart) fragmentView.findViewById(R.id.chart);
        getPresenter().putDataToBarData("Month");

        return fragmentView;
    }
    @Override
    public void setConsumptionBarChart(BarData barData) {
        consumptionBarChart.setData(barData);
        consumptionBarChart.setFitBars(true); // make the x-axis fit exactly all bars
        consumptionBarChart.invalidate(); // refresh
    }
}
