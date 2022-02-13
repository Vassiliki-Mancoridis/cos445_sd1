import java.util.Arrays;
import java.util.List;
import java.lang.Math;

public class Student_vm8 implements Student {
   private class SchoolPref implements Comparable<SchoolPref> {

      private int index;
      private double pref;
      private boolean listed;

      public SchoolPref(int i, double p) {
         index = i;
         pref = p;
         listed = false;
      }

      public void setListed(boolean bool) {
         listed = bool;
      }

      // sorts based on synergy
      public int compareTo(SchoolPref n) {
         int ret = Double.compare(n.pref, pref);
         return (ret == 0) ? (Integer.compare(index, n.index)) : ret;
      }
   }

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

         // T is smaller -> synergist approach
         if ((T * 1.0 / W ) < 0.4) {
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
         else if ((W * 1.0 / T) < 0.4){
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
            return ret;
         }

      // no "dominant strategy"
      else {
         // calculate true preferences (synergy plus quality for each u)
         SchoolPref[] preferences = new SchoolPref[N];

         for (int i = 0; i != schools.size(); ++i) {
            preferences[i] = new SchoolPref(i, schools.get(i) + synergies.get(i));
         }

         Arrays.sort(preferences); // schools sorted by quality

         // fill up remaining 9 slots
          int slotsLeft = 9;
          int i=0;
          int retIndex = 0;
          double p;

          // weighted coin distribution

          while (slotsLeft > 0) {
              if (i >= N) i= 0; // continuously loop through schools until preference list is filled
             // probability we should apply to school i (our standing for the school)
             p = (1.2) * ((aptitude + synergies.get(preferences[i].index)) / (S + W));
             // we should apply to school i
             if (Math.random() < p && preferences[i].listed == false) {
                ret[retIndex] = preferences[i].index;
                preferences[i].setListed(true);
                slotsLeft--;
                retIndex++;
             } 
             // otherwise, we do not apply to school i
             i++;
          } 
          
         return ret;
      }
   }
}
