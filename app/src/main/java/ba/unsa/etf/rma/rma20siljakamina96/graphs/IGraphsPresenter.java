package ba.unsa.etf.rma.rma20siljakamina96.graphs;

import java.util.Map;

public interface IGraphsPresenter {

    Map<Float, Float> putConsumptionDataToBarData(String timeUnit);

    Map<Float, Float> putEarningsDataToBarData(String timeUnit);

    void putTotalDataToBarData(String timeUnit);
}
