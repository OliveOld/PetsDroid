package charting.interfaces.dataprovider;

import  charting.components.YAxis.AxisDependency;
import  charting.data.BarLineScatterCandleBubbleData;
import  charting.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(AxisDependency axis);
    boolean isInverted(AxisDependency axis);
    
    float getLowestVisibleX();
    float getHighestVisibleX();

    BarLineScatterCandleBubbleData getData();
}
