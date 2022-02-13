// calculate synergy + quality score of uni's (student's true preferences)
// for each uni
    // calculate synergy + quality score
    
// sort uni's in order of this score

// keep running number of schools left to apply to, initially numLeft = 9
// make a placeholder for safety = null;
// while safety is null, go thru uni's in order of pref
    // calculate V = E[number of students who would beat us if they applied]
    // if V == 0, then current uni is favorite safety
        // safety = uni; 
    // calculate p = (1-V/N) = prob we "should" apply at this uni
    // flip a random coin with p = prob of heads
    // if heads, we apply to this school
    // decrease numLeft (# schools we will apply to)
    
// 


/* some sample libraries that we may not need! */
import java.util.Arrays;
import java.util.List;
import java.lang.Math;

public class Student_vm8 implements Student {
   private class School implements Comparable<School> {
        
      private int name; // the "name" of the school (which is an int index)
      private double preference; // students true preference for this school
      private double rivals; // the "V" score = E[number of students who would beat us if they applied]
      private boolean haveApplied;

      public School(int i, double p) {
         name = i;
         preference = p;
         haveApplied = false;
      }

      public void setRivals(double r) {
          rivals = r;
      }

      public double getRivals() {
         return rivals;
      }

      public int getName() {
         return name;
      }
      
      public void setHaveApplied(boolean bool) {
         haveApplied = bool;
      }

      public boolean getHaveApplied() {
         return haveApplied;
      }

      public int compareTo(School n) { // smaller pairs are higher quality
         int ret = Double.compare(n.preference, preference);
         return (ret == 0) ? (Integer.compare(name, n.name)) : ret; 
      }
   }

   public int[] getApplications(int N, double S, double T, double W,
      double aptitude, List<Double> schools, List<Double> synergies) {
         int[] ret = new int[10];

         // calculate true preferences (synergy plus quality for each u)
         School[] preferences = new School[N];
         for (int i = 0; i < N; i++) {
            preferences[i] = new School(i, schools.get(i) + synergies.get(i));
         }
         Arrays.sort(preferences);
         
         // calculate percentiles, Vs for each school, find our safety school
         int safetyIndexPref = -1;
         double lowestV = Double.POSITIVE_INFINITY;
         for (int i = 0; i < N; i++) {
            int schoolName = preferences[i].getName(); // "name" of school in input array (index)
            double percentile = (synergies.get(schoolName) + aptitude) * 1.0 / (S + W);
            double v = Math.floor(schools.get(schoolName) * (1.0 / T) * (1 - percentile) * N);
            if (v < lowestV) {
               safetyIndexPref = i;
               lowestV = v;
            }
            preferences[i].setRivals(v);
         }
         
         // set that we have applied to our safety
         ret[9] = preferences[safetyIndexPref].getName();
         preferences[safetyIndexPref].setHaveApplied(true);

         // fill up remaining 9 slots
         int slotsLeft = 9;
         int i=0;
         int retIndex = 0;
         double p;
         
         // weighted coin distribution
         
         while (slotsLeft > 0) {
             if (i >= N) i=0; // continuously loop through schools until preference list is filled
            // probability we should apply to school i
            p = 1.0 - (preferences[i].getRivals() * 1.0 / N);
            // we should apply to school i
            if (Math.random() < p && preferences[i].getHaveApplied() == false) {
               ret[retIndex] = preferences[i].getName();
               preferences[i].setHaveApplied(true);
               slotsLeft--;
               retIndex++;
            } 
            // otherwise, we do not apply to school i
            i++;
         } 

         // 9 schools above our safety school (or below, if there aren't enough above!)         
         /*
         int q = 1;
         for (int j = 0; j < 10; j++) {
            if (safetyIndexPref + j >= N) {
               ret[j] = preferences[safetyIndexPref - q].getName();
               q++;
            } else {
               ret[j] = preferences[safetyIndexPref + j].getName();
            }
         }
         */
         // uniform distribution
         


         return ret;
      }
}
