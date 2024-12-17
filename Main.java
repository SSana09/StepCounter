    import java.io.IOException;
    import java.nio.file.DirectoryStream;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.util.ArrayList;
    import java.util.List;


    public class Main {
        public static void main(String[] args) throws IOException {
            double rmse = testAllFiles("src/2024/");
            System.out.println("The mean error is: " + rmse + " steps.");
        }


        private static double testAllFiles(String folderPath) {
            try {
                DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(folderPath), "*.csv");
                double errorSum = 0;
                int numFiles = 0;

                System.out.printf("%-50s %-10s %-10s %-10s%n", "file name", "predicted", "actual", "error");
                for (Path filePath : directoryStream) {
                    String filename = filePath.getFileName().toString();
                    // String content = new String(Files.readAllBytes(filePath));
                    int predictedSteps = countSteps(filename);  //    ← this is your method
                    int realSteps = getRealStepsFromFilename(filePath);
                    numFiles++;

                    int error = realSteps - predictedSteps;
                    errorSum += error * error;       // to calcualte mean *squared* error

                    System.out.printf("%-50s %-10s %-10s %-10s%n", filePath, predictedSteps, realSteps, error);
                }

                double meanSquaredError = Math.sqrt(errorSum / numFiles);
                return meanSquaredError;
            } catch (IOException e) {
                System.err.println("Error reading files: " + e.getMessage());
            }


            return -1;
        }


        private static int getRealStepsFromFilename(Path filePath) {
            String filename = filePath.getFileName().toString().toLowerCase();
            String numString = filename.replaceAll("[^\\d]", "");
            int realSteps = Integer.parseInt(numString);
            return realSteps;
        }

        private static int countSteps(String name) {
            ArrayList<String> content = Methods.readFile(name);
            Algorithm a = new Algorithm(content);
            return a.countSteps();
        }
    }
