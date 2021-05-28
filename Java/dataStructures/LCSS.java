package tree;
import java.util.Scanner ;
public class LCSS{
   public static void main(String[] args){
         Scanner scn = new Scanner(System.in);
         
         System.out.print("Enter a sequence: ");
         String s1 = scn.nextLine().toUpperCase();   
         System.out.print("Enter a sequence: ");         
         String s2 = scn.nextLine().toUpperCase();   
         System.out.println( lcss(s1,s2) );
   }
   
   
   public static String lcss(String s1, String s2){
      int[][] lengths = new int[s1.length()][s2.length()];
      char[][] arrows = new char[s1.length()][s2.length()]; //u= up, l = left
      														//d = diagonal up left
      
      for(int r=0; r<s1.length(); r++){
         for(int c=0; c<s2.length(); c++){
           // arrows[r][c]='|';
            if(s1.charAt(r)==s2.charAt(c)){ //equal      
               if(r-1<0 || c-1<0){
                  lengths[r][c] = 1;
                  arrows[r][c] = '-'; //beginning of subsequence
               } else {
                  lengths[r][c] = 1+lengths[r-1][c-1];
                  arrows[r][c] = 'd';
               }
            } else { //not equal
               if(r==0){      //if both b and r are zero, nothing changes
                  if(c!=0){
                     lengths[r][c] = lengths[r][c-1];
                     arrows[r][c] = 'l';            
                  }
               } else if(c==0){
                  lengths[r][c] = lengths[r-1][c];
               //  System.out.println(s1.charAt(r) + " " + s2.charAt(c));
                  arrows[r][c] = 'u';
               }
               else if(lengths[r-1][c] > lengths[r][c-1]){
                  lengths[r][c] = lengths[r-1][c];
                  arrows[r][c] = 'u';
               } else {
                  lengths[r][c] = lengths[r][c-1];
                  arrows[r][c] = 'l';
               }
            }//end not equal
      
         }//end for loop
      }//end outer for loop
      
      //Have all arrows and costs
      String s = "";
      int r=s1.length()-1;
      int c=s2.length()-1;      
      char chr = arrows[r][c];
      while(chr!='|'){
         if(chr=='d' || chr=='-') {
            s = s1.charAt(r) + s;
            r--;
            c--;
         }
         if(chr=='u') r--;
         if(chr=='l') c--;  
         
         if (r<0 ||c <0) break;
         chr = arrows[r][c];
      }
      
      
      return s;
      
      
   
   }//end lcss
   
   



}//end class