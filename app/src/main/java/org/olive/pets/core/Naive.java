package org.olive.pets.core;

import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instance;

public class Naive
        extends weka.classifiers.bayes.NaiveBayes
        implements Classifier
{
    public Posture classify(weka.core.Instance inst)
    {
        try{
            int idx = (int)this.classifyInstance(inst);
            return Posture.values()[idx];
        }
        catch(Exception e){
            return Posture.Unknown;
        }
    }

    public void build(weka.core.Instances train) throws Exception
    {
        this.buildClassifier(train);
    }

    public double[] belief(weka.core.Instance inst) throws Exception
    {
        return this.distributionForInstance(inst);
    }
}
