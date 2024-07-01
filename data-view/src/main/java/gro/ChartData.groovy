package gro

/**
 * @description:
 * @author ：dukelewis
 * @date: 2024/7/1
 * @Copyright ： https://github.com/DukeLewis
 */

import org.knowm.xchart.*
import javax.swing.SwingUtilities

class ChartData {
    static XYChart chart = new XYChartBuilder()
            .width(800)
            .height(600)
            .title("Real-Time Data")
            .xAxisTitle("Time")
            .yAxisTitle("Value")
            .build()
    static List<Double> xData = []
    static List<Double> yData = []
    static SwingWrapper<XYChart> sw

    static {
        yData << 0.0
        xData << 0.0
        chart.addSeries("Data Series", xData, yData)
        sw = new SwingWrapper(chart)
        sw.displayChart()
    }

    static void updateData(double newData) {
        SwingUtilities.invokeLater {
            xData << xData.size() + 1D
            yData << newData
            chart.updateXYSeries("Data Series", xData, yData, null)
            sw.repaintChart()
        }
    }
}

