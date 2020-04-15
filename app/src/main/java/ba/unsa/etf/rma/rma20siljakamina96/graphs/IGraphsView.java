package ba.unsa.etf.rma.rma20siljakamina96.graphs;

import com.github.mikephil.charting.data.BarData;

public interface IGraphsView {
    void setConsumptionBarChart(BarData barData);

    void setEarningsBarChart(BarData barData);
}
