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
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

import ba.unsa.etf.rma.rma20siljakamina96.OnSwipeTouchListener;
import ba.unsa.etf.rma.rma20siljakamina96.R;

public class GraphsFragment extends Fragment {
    private OnSwipeLeft onSwipeLeft;
    private OnSwipeRight onSwipeRight;
    private ConstraintLayout graphsLayout;

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
        List<BarEntry> entries = new ArrayList<BarEntry>();
        entries.add(new BarEntry(0f, 30f));

        BarDataSet dataSet = new BarDataSet(entries, "Potrošnja"); // add entries to dataset
        dataSet.setColor(Color.RED);
        dataSet.setValueTextColor(Color.BLACK); // styling, ...

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f); // set custom bar width
        consumptionBarChart.setData(barData);
        consumptionBarChart.setFitBars(true); // make the x-axis fit exactly all bars
        consumptionBarChart.invalidate(); // refresh

        return fragmentView;
    }
    public void setConsumptionBarChart(BarData barData) {
        consumptionBarChart.setData(barData);
        consumptionBarChart.setFitBars(true); // make the x-axis fit exactly all bars
        consumptionBarChart.invalidate(); // refresh
    }
}
