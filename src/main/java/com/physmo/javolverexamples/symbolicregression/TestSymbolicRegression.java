package com.physmo.javolverexamples.symbolicregression;

import com.physmo.javolver.Individual;
import com.physmo.javolver.Scoring;
import com.physmo.javolver.breedingstrategy.BreedingStrategyCrossover;
import com.physmo.javolver.breedingstrategy.BreedingStrategyUniform;
import com.physmo.javolver.mutationstrategy.MutationStrategySimple;
import com.physmo.javolver.mutationstrategy.MutationStrategySwap;
import com.physmo.javolver.selectionstrategy.SelectionStrategyRoulette;
import com.physmo.javolver.selectionstrategy.SelectionStrategyTournament;
import com.physmo.javolver.solver.Javolver;

public class TestSymbolicRegression {

    static int dnaSize = 200;
    static int populationSize = 500;
    static int maxDepth = 4;
    static double penaltyPerStatement = 0.01;
    static TreeParser treeParser = new TreeParser(maxDepth);
    static Remapper registerRemapper = new Remapper(0, 1, -10, 10);
    Javolver solver;
    TestCase testCase;

    public static void main(String[] args) {
        TestSymbolicRegression testSymbolicRegression = new TestSymbolicRegression();
        testSymbolicRegression.init();
        testSymbolicRegression.run();
    }

    public static String dnaPrinter(Individual individual) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < individual.getDna().getSize(); i++) {
            output.append(String.format("%6.2f ", individual.getDna().getDouble(i)));
        }
        return output.toString();
    }

    public static void cullSimilar() {

    }

    public void init() {

        dnaSize = (int) Math.pow(2, (maxDepth + 1)) + 10;
        System.out.println("Setting DNA size to " + dnaSize);

        testCase = new TestCase(0, 20, 20, x -> Math.sin(x * x) * 30);
//(Math.sin((x + 2)) * 5) + x + (Math.cos(x * 2) * 3)

        solver = Javolver.builder()
                .dnaSize(dnaSize)
                .populationTargetSize(populationSize)
                .keepBestIndividualAlive(true)
                .addMutationStrategy(new MutationStrategySimple(2, 2, 1.0))
//                .addMutationStrategy(new MutationStrategySimple(1, 0.20, 1.0))
//                .addMutationStrategy(new MutationStrategySimple(1, 1.0, 0.3))
//                .addMutationStrategy(new MutationStrategySwap(0.2, 8))
                .addMutationStrategy(new MutationStrategySwap(0.2, 2))
                .setSelectionStrategy(new SelectionStrategyTournament(0.1))
                //.setSelectionStrategy(new SelectionStrategyRoulette())
                .setBreedingStrategy(new BreedingStrategyUniform())
                //.setBreedingStrategy(new BreedingStrategyCrossover())
                .scoreFunction(this::calculateScore)
                .dnaInitializer(this::dnaInitializer)
                .build();

        solver.setTemperature(1);
    }

    private void run() {

        // Run evolution until we get exact solution.
        for (int j = 0; j < 50000; j++) {
            // Perform one evolution step.
            solver.doOneCycle();

            Individual best = solver.getBestScoringIndividual();

            // Print output every so often.
            if (j % 500 == 0 && j > 0) {
                System.out.printf("[%s] Score %6.2f %n", j, best.getScore());
                System.out.println(describe(best));
                //System.out.println(dnaPrinter(best));
            }

            if (j % 1500 == 0) {
//                for (Individual individual : solver.getPool()) {
//                    System.out.println("  "+ describe(individual));
//                }
                CullSimilar cullSimilar = new CullSimilar();
                cullSimilar.run(solver);
//                System.out.println("====================   culled   ===================");
//                for (Individual individual : solver.getPool()) {
//                    System.out.println("  "+ describe(individual));
//                }
                solver.increasePopulation(populationSize);
//                System.out.println("====================   regenerated   ===================");
//                for (Individual individual : solver.getPool()) {
//                    System.out.println("  "+ describe(individual));
//                }
            }
        }

        System.out.print("END");

    }

    public double dnaInitializer(int i) {
        return Math.random() * 5;
    }

    public void setRegistersFromIndividual(Individual individual) {
        treeParser.cA = registerRemapper.remap(individual.getDna().getData()[dnaSize - 1]);
        treeParser.cB = registerRemapper.remap(individual.getDna().getData()[dnaSize - 2]);
        treeParser.cC = registerRemapper.remap(individual.getDna().getData()[dnaSize - 3]);
        treeParser.cD = registerRemapper.remap(individual.getDna().getData()[dnaSize - 4]);
    }

    public String describe(Individual individual) {

        TreeNode tree = treeParser.buildTree(individual.getDna().getData());

        setRegistersFromIndividual(individual);

        return treeParser.describe(tree);
    }

    public double calculateScore(Individual individual) {

        TreeNode tree = treeParser.buildTree(individual.getDna().getData());
        double totalScore = 0;
        setRegistersFromIndividual(individual);
        for (int i = 0; i < testCase.numEntries; i++) {

            double x = testCase.inputs[i];
            double expected = testCase.outputs[i];
            treeParser.varX = x;
            double result = treeParser.parse(tree);

            double score = Scoring.scoreValue(result, expected, 10) * 10;
            totalScore += score;
        }
        totalScore /= testCase.numEntries;

        // Penalty for number of operators.
        int numNodes = treeParser.countNodes(tree);
        totalScore -= numNodes * penaltyPerStatement;

        //if (numNodes < 2) totalScore *= 0.9;

        return totalScore;

    }


}



