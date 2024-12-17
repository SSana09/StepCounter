import Plot.PlotWindow;
import Plot.ScatterPlot;

import java.util.ArrayList;
public class Algorithm {

    private static ArrayList<String> lines;
    private static ArrayList<Double> acc3D;
    private static ArrayList<Double> gyro3D;
    private static ArrayList<Integer> peakIndexAcc;
    private static ArrayList<Double> peakPointAcc;
    private static ScatterPlot plt;
    private PlotWindow window;

    public Algorithm(ArrayList<String> lines) {
        this.lines = lines;
        acc3D = Methods.get3DAcc(this.lines);
        gyro3D = Methods.get3DGyro(this.lines);
        peakIndexAcc = Methods.getPeakIndexes(acc3D);
        peakPointAcc = Methods.getValuesAt(peakIndexAcc, acc3D);
        plt = new ScatterPlot(100, 100, 1100, 700);
        window = PlotWindow.getWindowFor(plt, 1200,800);
    }

    public int numSteps(ArrayList<Double> vals, double threshold, int timeThresh) {
        int steps = 0;
        for (int i=1; i<vals.size()-1; i++) {
            double c = vals.get(i);
            double pr = vals.get(i-1);
            double ne = vals.get(i+1);
            int lastStep=0;
            if (pr<c && c>ne) {
                if (c>threshold) {
                    if (i-lastStep>=timeThresh) {
                        steps++;
                        lastStep=i;
                    }
                }
            }
        }
        return steps;
    }

    public int numSteps(ArrayList<Double> vals) {
        int steps=0;
        for (int i=1; i< vals.size()-1; i++) {
            double c = vals.get(i);
            double pr = vals.get(i-1);
            double ne = vals.get(i+1);
            if (pr<c && c>ne) {
                steps++;
            }
        }
        return steps;
    }


    public void plotData() {
        for (int i=0; i< acc3D.size(); i++) {
            double x = i;
            double y = acc3D.get(i);
            plt.plot(0, x, y).strokeColor("red").strokeWeight(1).style("-");
        }


        for (int i=0; i< gyro3D.size(); i++) {
            double x = i;
            double y = gyro3D.get(i);
            plt.plot(1, x, y).strokeColor("black").strokeWeight(1).style("-");
        }


        for (int i = 0; i < peakIndexAcc.size(); i++) {
            double index = peakIndexAcc.get(i);
            double value = peakPointAcc.get(i);
            plt.plot(2, index, value).strokeColor("blue").strokeWeight(4).style(".");
        }
    }


    public void outputGraph() {
        window.show();
    }


    public int countSteps() {
        double avg1 = Methods.calcAbsAvg(acc3D);
        double avg2 = Methods.calcAbsAvg(peakPointAcc);
        double tempAvg=0;
        ArrayList<Double> smoothed = acc3D;
        for (int i=0; i<70; i++) {
            smoothed = Methods.smooth(smoothed);
        }
        double avg3 = Methods.calcAbsAvg(smoothed);


//        for (int i=0; i<acc3D.size()-25; i++) {
//            tempAvg = Methods.calcLocalAvg(acc3D, 25);
//            plt.plot(3, i, avg1).strokeColor("black").strokeWeight(2).style(".");
//            plt.plot(3, i, avg2).strokeColor("black").strokeWeight(2).style(".");
//            plt.plot(3, i, tempAvg).strokeColor("black").strokeWeight(2).style(".");
//        }


        ArrayList<Integer> smoothPeakIndex = Methods.getPeakIndexes(smoothed);
        int timeThresh = Methods.getTimeThresh(smoothPeakIndex);
        double threshold = Methods.bestThreshold(avg1, avg2, avg3, peakPointAcc);
        int steps = numSteps(acc3D, threshold, timeThresh);
        return steps;
    }
}
