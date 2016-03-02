 
import java.util.Comparator;
 
public class WordComparator implements Comparator<Word>{
 
    @Override
    public int compare(Word w1, Word w2) {
 
        int rank1 = w1.getValue();
        int rank2 = w2.getValue();
 
        if (rank1 > rank2){
            return -1;
        }else if (rank1 < rank2){
            return +1;
        }else{
            return 0;
        }
    }
}