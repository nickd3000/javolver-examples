import com.physmo.javolverexamples.symbolicregression.Operator;
import com.physmo.javolverexamples.symbolicregression.TreeNode;
import com.physmo.javolverexamples.symbolicregression.TreeParser;
import org.junit.Test;

public class TestTreeParser {

    @Test
    public void t1() {

        TreeParser treeParser = new TreeParser(4);
        double dna[] = new double[200];

        dna[0] =TreeParser.getDoubleFromOperator(Operator.ADD);
        dna[1] =TreeParser.getDoubleFromOperator(Operator.MUL);
        dna[2] =TreeParser.getDoubleFromOperator(Operator.SUB);
        dna[3] =TreeParser.getDoubleFromOperator(Operator.DIV);
        dna[4] =TreeParser.getDoubleFromOperator(Operator.ONE);
        dna[5] =TreeParser.getDoubleFromOperator(Operator.ONE);
        dna[6] =TreeParser.getDoubleFromOperator(Operator.TWO);
        dna[7] =TreeParser.getDoubleFromOperator(Operator.ONE);
        dna[8] =TreeParser.getDoubleFromOperator(Operator.ONE);
        dna[9] =TreeParser.getDoubleFromOperator(Operator.ONE);
        dna[10] =TreeParser.getDoubleFromOperator(Operator.TWO);
        dna[11] =TreeParser.getDoubleFromOperator(Operator.ONE);


        TreeNode tree = treeParser.buildTree(dna);

        String described = treeParser.describe(tree);

        System.out.println(described);

        double result = treeParser.parse(tree);
        System.out.println("Result:"+result);
    }

}
