package com.physmo.javolverexamples.programming;

import com.physmo.javolver.Chromosome;
import com.physmo.javolverexamples.programming.simplemachinie.SimpleMachine2;
import com.physmo.minvio.BasicDisplay;

import java.util.Random;

public class ProblemSumTwo implements ProgramEvaluator {

    Random random = new Random();
    int maxValueRange = 99;
    int input1 = 0;
    int input2 = 0;

    int inputIndex = 0;
    int outputIndex = 5;

    @Override
    public void preEvaluateStep(SimpleMachine2 sm, Chromosome dna, double step) {
        input1 = random.nextInt(maxValueRange/2);
        input2 = random.nextInt(maxValueRange/2);

        if (random.nextDouble()<0.0) {
            double rnd =random.nextDouble();
            if (rnd<0.3) {
                input1 = 50;
                input2 = 25;
            } else if (rnd<0.6) {
                input1 = 20;
                input2 = 10;
            } else {
                input1 = 30;
                input2 = 60;
            }
        }

        sm.memory[inputIndex+1] = input1;
        sm.memory[inputIndex+2] = input2;

        sm.pc=10;
    }

    @Override
    public double evaluate(SimpleMachine2 sm, Chromosome dna, double step) {
        double output = sm.regD;
        double target = input1+input2;
        double diff = (Math.abs(output - target));
        diff = (maxValueRange-diff)/(double)(maxValueRange);
        if (diff<0) diff=0;
        if (diff>1) diff=1;

        double score = diff*diff;

        if (output == target) score+=10;
        return score;
    }



    @Override
    public int getNumberOfSteps() {
        return 0;
    }

    @Override
    public void render(SimpleMachine2 sm, Chromosome dna, BasicDisplay bd, double step) {

    }

    @Override
    public int getOutputValueHash(SimpleMachine2 sm, Chromosome dna) {
        return (int) sm.regB;
    }

    @Override
    public String report(SimpleMachine2 sm, Chromosome dna) {
        double output = sm.regD;
        double target = input1+input2;

        //String str = "["+sm.memory[inputIndex+0]+","+sm.memory[inputIndex+1]+","+sm.memory[inputIndex+2]+"]";
        String result = " expecting "+(target)+" got "+output;
        return result;
    }
}
