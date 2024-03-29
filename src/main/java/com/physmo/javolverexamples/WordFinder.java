package com.physmo.javolverexamples;


import com.physmo.javolver.Individual;
import com.physmo.javolver.breedingstrategy.BreedingStrategyUniform;
import com.physmo.javolver.mutationstrategy.MutationStrategySimple;
import com.physmo.javolver.selectionstrategy.SelectionStrategyTournament;
import com.physmo.javolver.solver.Javolver;
import com.physmo.javolver.solver.Solver;

public class WordFinder {
    public static final String targetWord = "EVOLUTION OF A WORD";

    public static void main(String[] args) {

        Solver solver = Javolver.builder()
                .dnaSize(targetWord.length())
                .populationTargetSize(50)
                .keepBestIndividualAlive(true)
                .addMutationStrategy(new MutationStrategySimple(1, 0.55))
                .setSelectionStrategy(new SelectionStrategyTournament(0.3))
                .setBreedingStrategy(new BreedingStrategyUniform())
                .scoreFunction(WordFinder::calculateScore)
                .build();

        // Run evolution until we get exact solution.
        for (int j = 0; j < 500; j++) {
            // Perform one evolution step.
            solver.doOneCycle();

            Individual best = solver.getBestScoringIndividual();
            boolean exactMatch = toString(best).trim().equals(targetWord);

            // Print output every so often.
            if (j % 5 == 0 || exactMatch) {
                System.out.printf("[%s] Iteration %d Score %6.2f %n", toString(best), j, solver.getBestScoringIndividual().getScore());
            }

            // Stop if we have an exact match.
            if (exactMatch) break;
        }

        System.out.print("END");
    }

    // Compare each character in the string to the target string and return a score.
    // Each character gets a higher score the closer it is to the target character.
    public static double calculateScore(Individual individual) {
        double total = 0.0;
        for (int i = 0; i < targetWord.length(); i++) {
            total += getScoreForCharacter(individual.getDna().getChar(i), targetWord.charAt(i));
        }
        return total;
    }

    // TODO: move this into scoring class in Javolver
    public static double getScoreForCharacter(char a, char b) {
        int maxDiff = 15;
        int diff = Math.abs(a - b);
        if (diff > maxDiff) return 0.0;
        return ((maxDiff - diff) / 10.0);
    }

    public static String toString(Individual individual) {
        String str = "";
        for (int i = 0; i < individual.getDna().getData().length; i++) {
            str = str + individual.getDna().getChar(i);
        }
        return str;
    }
}
