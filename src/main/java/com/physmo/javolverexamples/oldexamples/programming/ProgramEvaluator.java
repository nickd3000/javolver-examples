package com.physmo.javolverexamples.oldexamples.programming;

import com.physmo.javolver.Chromosome;
import com.physmo.javolverexamples.programming.SimpleMachine;
import com.physmo.minvio.BasicDisplay;


public interface ProgramEvaluator {
    void preEvaluateStep(com.physmo.javolverexamples.programming.SimpleMachine sm, Chromosome dna, double step);
    double evaluate(com.physmo.javolverexamples.programming.SimpleMachine sm, Chromosome dna, double step);
    int getNumberOfSteps();
    void render(SimpleMachine sm, Chromosome dna, BasicDisplay bd, double step);
}
