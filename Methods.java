import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Methods {
    public static ArrayList<String> readFile(String filename) {
        ArrayList<String> lines = new ArrayList<>();
        try{
            List<String> contents = Files.readAllLines(Paths.get("src/2024/"+filename));
            lines.addAll(contents);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static ArrayList<Double> getColumnAtList(int col, ArrayList<String> lines) {
        ArrayList<Double> values = new ArrayList<>();
        for (int i=1; i<lines.size(); i++) {
            String s = lines.get(i);
            String[] vals = s.split(",");
            double v = Double.parseDouble(vals[col]);
            values.add(v);
        }
        return values;
    }

    public static double get3DMag(double x, double y, double z) {
        return Math.sqrt((x*x)+(y*y)+(z*z));
    }

    public static ArrayList<Integer> getPeakIndexes(ArrayList<Double> data) {
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i=1; i<data.size()-1; i++) {
            if (data.get(i)>data.get(i-1)) {
                if (data.get(i)> data.get(i+1))
                    indexes.add(i);
            }
        }
        return indexes;
    }

    public static ArrayList<Double> getValuesAt(ArrayList<Integer> peakLocations, ArrayList<Double> data) {
        ArrayList<Double> valsAt = new ArrayList<>();
        for (int i: peakLocations) {
            double acc = data.get(i);
            valsAt.add(acc);
        }
        return valsAt;
    }

    public static ArrayList<Double> return3D (ArrayList<Double> xCO, ArrayList<Double> yCO, ArrayList<Double> zCO) {
        ArrayList<Double> threeD = new ArrayList<Double>();
        for (int i=0; i<zCO.size(); i++) {
            double x = xCO.get(i);
            double y = yCO.get(i);
            double z = zCO.get(i);
            threeD.add(Methods.get3DMag(x, y, z));
        }
        return threeD;
    }

    public static double calcLocalAvg(ArrayList<Double> data, int index) {
        double sum=0;
        int c=0;
        for (int i=index; i<index+25; i++) {
            sum+=data.get(i);
            c++;
        }
        return sum/c;
    }

    public static double calcAbsAvg(ArrayList<Double> data) {
        double sum=0;
        int c=0;
        for (double d: data) {
            sum+=d;
            c++;
        }
        return sum/c;
    }

    public static double getValueAtPercent(ArrayList<Double> data) {
        ArrayList<Double> test = new ArrayList<>();
        test.addAll(data);
        Collections.sort(test);
        int s = test.size();
        s*=0.95;
        return test.get(s);
    }

    public static double bestThreshold(double one, double two, double three, ArrayList<Double>data) {
        double val = getValueAtPercent(data);
        double diff1 = Math.abs(val-one);
        double diff2 = Math.abs(val-two);
        double diff3 = Math.abs(val-three);
        double best = Math.min(diff3, Math.min(diff2, diff1));
        if (best==diff1)
            return one;
        else if (best==diff2)
            return two;
        else return three;
    }

    public static ArrayList<Double> get3DAcc(ArrayList<String> lines) {
        ArrayList<Double> X = Methods.getColumnAtList(0, lines);
        ArrayList<Double> Y = Methods.getColumnAtList(1, lines);
        ArrayList<Double> Z = Methods.getColumnAtList(2, lines);
        ArrayList<Double> acc3D = new ArrayList<>();
        for (int i = 0; i< Z.size(); i++) {
            double x = X.get(i);
            double y = Y.get(i);
            double z = Z.get(i);
            double val = Methods.get3DMag(x, y, z);
            acc3D.add(val);
        }
        return acc3D;
    }

    public static ArrayList<Double> get3DGyro(ArrayList<String> lines) {
        ArrayList<Double> X = Methods.getColumnAtList(3, lines);
        ArrayList<Double> Y = Methods.getColumnAtList(4, lines);
        ArrayList<Double> Z = Methods.getColumnAtList(5, lines);
        ArrayList<Double> gyro3D = new ArrayList<>();
        for (int i = 0; i< Z.size(); i++) {
            double x = X.get(i);
            double y = Y.get(i);
            double z = Z.get(i);
            double val = Methods.get3DMag(x, y, z);
            gyro3D.add(val);
        }
        return gyro3D;
    }

    public static ArrayList<Double> smooth(ArrayList<Double> values) {
        ArrayList<Double> out = new ArrayList<>();
        for (int i=0; i<values.size()-3; i++) {
            double sum=(values.get(i)+values.get(i+1)+values.get(i+2));
            out.add(sum/3);
        }
        return out;
    }

    public static int getTimeThresh(ArrayList<Integer> indexes) {
        int sum=0;
        int prev=0;
        for (int i=0; i<indexes.size(); i++) {
            sum+=(indexes.get(i)-prev);
            prev= indexes.get(i);
        }
        return sum/indexes.size();
    }

}