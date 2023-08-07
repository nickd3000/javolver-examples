import com.physmo.javolver.Scoring;
import com.physmo.javolverexamples.symbolicregression.TreeParser;
import org.junit.Test;

public class TestScoring {
    @Test
    public void t1() {

        Scoring.scoreValue(0,5, 10);
        for (int i=0;i<10;i++) {
            System.out.println(runScoreValue(i,5,3));
        }
        for (int i=0;i<10;i++) {
            System.out.println(runScoreValue(i,9,3));
        }
    }

    public String runScoreValue(double actual, double expected, double range) {
        double result = Scoring.scoreValue(actual,expected, range);
        return String.format("%f %f %f %f",actual,expected,range,result);
    }

    @Test
    public void locatorTest() {
        doit("");
        doit("L");
        doit("R");
        doit("LL");
        doit("RR");
        doit("LRL");
    }

    public void doit(String str) {
        int v = TreeParser.getIndexFromLocatorString(str);
        System.out.println(str+"  "+v);
    }
}
