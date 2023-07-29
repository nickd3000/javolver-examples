package com.physmo.javolverexamples.programming;

import com.physmo.javolver.Individual;
import com.physmo.javolver.breedingstrategy.BreedingStrategyCrossover;
import com.physmo.javolver.breedingstrategy.BreedingStrategyUniform;
import com.physmo.javolver.mutationstrategy.MutationStrategyShuffle;
import com.physmo.javolver.mutationstrategy.MutationStrategySimple;
import com.physmo.javolver.mutationstrategy.MutationStrategySwap;
import com.physmo.javolver.selectionstrategy.SelectionStrategyTournament;
import com.physmo.javolver.solver.Javolver;
import com.physmo.javolverexamples.programming.simplemachinie.SimpleMachine2;
import com.physmo.minvio.BasicDisplay;
import com.physmo.minvio.BasicDisplayAwt;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class TestProgramGraph {

    static int populationSize = 1500;
    static int batchSize = 10;
    ProgramEvaluator programEvaluator = new FunctionEvaluator();
    //ProgramEvaluator programEvaluator = new ProblemMaxOfThree();

    public static void main(String[] args) throws Exception {
        TestProgramGraph testProgram = new TestProgramGraph();
        testProgram.go();
    }

    public void go() {
        BasicDisplay bd = new BasicDisplayAwt(400, 400);
        // FunctionEvaluator.class
//        Javolver testEvolver = Javolver.builder()
//                .dnaSize(150)
//                .populationTargetSize(populationSize)
//                .keepBestIndividualAlive(false)
//                .addMutationStrategy(new MutationStrategySimple(2, 0.5))
//                .addMutationStrategy(new MutationStrategySwap(0.1, 2))
//                .addMutationStrategy(new MutationStrategyShuffle(2))
//                //.addMutationStrategy(new MutationStrategyRandomize(0.1))
//                .setSelectionStrategy(new SelectionStrategyTournament(0.15))
//                .setBreedingStrategy(new BreedingStrategyCrossover())
//                .scoreFunction(this::calculateScore)
//                .build();
        Javolver testEvolver = Javolver.builder()
                .dnaSize(200)
                .populationTargetSize(populationSize)
                .keepBestIndividualAlive(true)
                .addMutationStrategy(new MutationStrategySimple(10, 1.5, 0.99))
                .addMutationStrategy(new MutationStrategySimple(1, 10.0, 0.1))
                .addMutationStrategy(new MutationStrategySwap(0.1, 2))
                //.addMutationStrategy(new MutationStrategyShuffle(2))
                //.addMutationStrategy(new MutationStrategyRandomize(0.1))
                .setSelectionStrategy(new SelectionStrategyTournament(0.25))
                //.setSelectionStrategy(new SelectionStrategyRoulette())
                //.setBreedingStrategy(new BreedingStrategyCrossover())
                .setBreedingStrategy(new BreedingStrategyUniform())
                .scoreFunction(this::calculateScore)
                .build();

        int iteration = 0;

        for (int j = 0; j < 50000; j++) {

            for (int i = 0; i < batchSize; i++) {
                testEvolver.doOneCycle();
                iteration++;
            }

            Individual ind = testEvolver.getBestScoringIndividual();
            String report = "score: "+ind.getScore();

            System.out.print("iteration: " + iteration + "  "+report);
            //testEvolver.report();
            System.out.println(testEvolver.getBestScoringIndividual().toString());


            //((GeneProgram)ind).render(bd);
            render(bd, ind);
        }

        System.out.print("END ");
    }

    public double calculateScore(Individual individual) {

        SimpleMachine2 sm = new SimpleMachine2();

        double score = 0, stepScore = 0;

        for (int x = 0; x < 400; x+=10) {
            //sm = new SimpleMachine();
            sm.reset();
            setupSimpleMachineFromDNA(sm, individual);
            int xx = (int) (x+(Math.random()*10));
            programEvaluator.preEvaluateStep(sm, individual.getDna(), xx );
            runSimpleMachine(sm);
            stepScore = programEvaluator.evaluate(sm, individual.getDna(), xx);
            score += stepScore;

            //consoleOutput+=" "+stepScore;
        }

        return score;
    }


    boolean useOffsetLoader = true;


    // Setup and also run simple machine, return console result.
    public void setupSimpleMachineFromDNA(SimpleMachine2 sm, Individual individual) {
        if (useOffsetLoader) {
            setupSimpleMachineFromDNA_Offset( sm,  individual);
            return;
        }

        List<Integer> list = new LinkedList<>();

        for (int i = 0; i < individual.getDna().getData().length / 2; i++) {
            int instruction = (int) (individual.getDna().getDouble(i * 2) * 30.0); //250
            double position = BasicDisplay.clamp(0, 1.0, (individual.getDna().getDouble((i * 2) + 1)));
            int iPosition = (int) (list.size() * position);
            list.add(iPosition, instruction);
        }

        int i = 0;
        for (Integer instruction : list) {
            sm.memory[i++] = instruction;
        }

    }

    // Offset style
    // first section contains pointers to later sectins of code
    public void setupSimpleMachineFromDNA_Offset(SimpleMachine2 sm, Individual individual) {
        int controlLength = 50;
        int outputInidex=0;
        for (int i = 0; i < controlLength ; i+=2) {
            int position = (int) (individual.getDna().getDouble(i ) * 10);
            int length = (int) (individual.getDna().getDouble(i+1 ) * 5);
            if (length<1) length=1;

            for (int j=position+controlLength;j<position+controlLength+length;j++) {
                if (j>=individual.getDna().getSize() || j<0) continue;
                int instruction = (int) (Math.abs(individual.getDna().getDouble(j)) * 10);
                sm.memory[outputInidex++] = instruction;
            }

            if (outputInidex>sm.memSize) break;

        }
    }

    public void runSimpleMachine(SimpleMachine2 sm) {
        double scorePenalty = 0;
        int maxCycles = 200 * 2;
        int cycleCount = 0;
        for (int i = 0; i < maxCycles; i++) {
            cycleCount++;
            int result = sm.runCycle();
//            if (sm.getMaxHits() > 20) {
//                scorePenalty = 20;
//                break;
//            }

            if (result == 1) break;
        }

        //String consoleOutput = sm.console;

        double cyclePenalty = (double) cycleCount / (double) maxCycles;

        //return sm.console;
    }

    public void render(BasicDisplay bd, Individual individual) {

        SimpleMachine2 sm = null;
        int numberOfSteps = programEvaluator.getNumberOfSteps();
        double score = 0, stepScore = 0;

        bd.cls(Color.lightGray);

        for (int x = 0; x < 400; x++) {
            sm = new SimpleMachine2();

            setupSimpleMachineFromDNA(sm, individual);
            programEvaluator.preEvaluateStep(sm, individual.getDna(), x);
            runSimpleMachine(sm);

            programEvaluator.render(sm, individual.getDna(), bd, x);

        }

        bd.repaint();

    }
}
