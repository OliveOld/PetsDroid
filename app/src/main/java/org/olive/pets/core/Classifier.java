package org.olive.pets.core;

import weka.core.Instance;
import weka.core.Instances;

public interface Classifier
{
    public Posture classify(weka.core.Instance inst);
    public void build(weka.core.Instances train) throws Exception;
    public double[] belief(weka.core.Instance inst) throws Exception;
}
