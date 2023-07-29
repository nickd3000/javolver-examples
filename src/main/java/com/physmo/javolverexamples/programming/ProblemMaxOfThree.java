package com.physmo.javolverexamples.programming;

import com.physmo.javolver.Chromosome;
import com.physmo.javolverexamples.programming.simplemachinie.SimpleMachine2;
import com.physmo.minvio.BasicDisplay;

import java.util.Random;

public class ProblemMaxOfThree implements ProgramEvaluator {

    Random random = new Random();
    int maxValueRange = 99;
    int maxValue = 0;

    int inputIndex = 0;

    @Override
    public void preEvaluateStep(SimpleMachine2 sm, Chromosome dna, double step) {

        for (int i=0;i<3;i++) {
            sm.memory[inputIndex+i] = random.nextInt(maxValueRange);
        }

        if (random.nextDouble()<0.3) {
            double rnd =random.nextDouble();
            if (rnd<0.3) {
                sm.memory[inputIndex] = 15;
                sm.memory[inputIndex+1] = 0;
                sm.memory[inputIndex+2] = 0;
            } else if (rnd<0.6) {
                sm.memory[inputIndex] = 0;
                sm.memory[inputIndex+1] = 16;
                sm.memory[inputIndex+2] = 0;
            } else {
                sm.memory[inputIndex] = 0;
                sm.memory[inputIndex+1] = 0;
                sm.memory[inputIndex+2] = 17;
            }
        }

        maxValue=0;
        for (int i=0;i<3;i++) {
            if (sm.memory[inputIndex+i]>maxValue) maxValue=sm.memory[inputIndex+i];
        }

        sm.pc=10;
    }

    @Override
    public double evaluate(SimpleMachine2 sm, Chromosome dna, double step) {
        double output = sm.regB;
        double diff = (Math.abs(output - maxValue));
        diff = (maxValueRange-diff)/(double)maxValueRange;
        if (diff<0) diff=0;
        if (diff>1) diff=1;

        double score = diff*diff*diff;

        if (output == maxValue) score+=25;
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
        String str = "["+sm.memory[inputIndex+0]+","+sm.memory[inputIndex+1]+","+sm.memory[inputIndex+2]+"]";
        String result = " expecting "+maxValue+" got "+sm.regB+ (maxValue==sm.regB?" *":"");
        return str+result;
    }
}
