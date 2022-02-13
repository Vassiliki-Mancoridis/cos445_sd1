import java.util.Arrays;
import java.util.List;
import java.lang.Math;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Student_vm8 implements Student {

   private class SchoolSynergy implements Comparable<SchoolSynergy> {
        
      private int index;
      private double synergy;

      public SchoolSynergy(int i, double p) {
         index = i;
         synergy = p;
      }

      // sorts based on synergy
      public int compareTo(SchoolSynergy n) {
         int ret = Double.compare(n.synergy, synergy);
         return (ret == 0) ? (Integer.compare(index, n.index)) : ret; 
      }
   }

   private class SchoolQuality implements Comparable<SchoolQuality> {
        
      private int index;
      private double quality;

      public SchoolQuality(int i, double p) {
         index = i;
         quality = p;
      }

      // sorts based on synergy
      public int compareTo(SchoolQuality n) {
         int ret = Double.compare(n.quality, quality);
         return (ret == 0) ? (Integer.compare(index, n.index)) : ret; 
      }
   }

   public int[] getApplications(int N, double S, double T, double W,
      double aptitude, List<Double> schools, List<Double> synergies) {
         int[] ret = new int[10];

         try {
            Path fileName = Path.of("vikimarina" + Double.+ ".txt");
            String content  = Double.toString(W * 1.0/T);
            Files.writeString(fileName, content);
         } catch(IOException e) {
         }

         // T is smaller -> synergist approach
         System.out.print("this is w/t: ");
         System.out.println((W * 1.0/T));
         if (W > T) {
            // calculate true preferences (synergy plus quality for each u)
            SchoolSynergy[] preferences = new SchoolSynergy[N];

            for (int i = 0; i != synergies.size(); ++i) {
               preferences[i] = new SchoolSynergy(i, synergies.get(i));
            }

            Arrays.sort(preferences); // schools sorted by synergy

            for (int i = 0; i != 10; ++i) {
               ret[i] = preferences[i].index;
            }
            
            return ret;
         } 
         
         // In this case, T (quality of universities) is dominant
         else {
            // calculate true preferences (synergy plus quality for each u)
            SchoolQuality[] preferences = new SchoolQuality[N];

            for (int i = 0; i != schools.size(); ++i) {
               preferences[i] = new SchoolQuality(i, schools.get(i));
            }

            Arrays.sort(preferences); // schools sorted by quality

            double percentile = (aptitude * 1.0) / S;
            int guaranteeSchoolNum = (int) Math.floor(N * percentile);
            ret[9] = preferences[guaranteeSchoolNum].index;
            int k = guaranteeSchoolNum;
            boolean goingDown = false;
            
            // Assign universities to the output array
            for (int i = 8; i >= 0; i--) {
               if (k == N || goingDown == true) {
                  if (k == N) {
                     k = guaranteeSchoolNum;
                     goingDown = true;
                  }
                  k = k - 1;
               } else {
                  k = k + 1;
               }
               ret[i] = preferences[k].index;
            }
         }
         
         return ret;
      }
}
