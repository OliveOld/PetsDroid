package org.olive.pets.core;

import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSource;

public class Dummy {

    // https://weka.wikispaces.com/Use+Weka+in+your+Java+code
    public static void dumdum() throws Exception {

        // load data
        Instances learn = DataSource.read("./L5.arff");
        learn.setClassIndex(learn.numAttributes() - 1);

        NaiveBayes cls = new NaiveBayes();
        cls.buildClassifier(learn);

        {
            int i = 0;

            // train NaiveBayes
            while (i < learn.size()){
                Instance current = learn.instance(i);
                cls.updateClassifier(current);
                i = i+1;
            }
            System.out.println(i);
        }

        // output generated model
        System.out.println(cls);

        Instances test = DataSource.read("./Tset.arff");
        test.setClassIndex(test.numAttributes() - 1);

        int[] a = new int[4];

        for (int i = 0; i < test.numInstances(); i++) {
            double pred = cls.classifyInstance(test.instance(i));
            double[] dist = cls.distributionForInstance(test.instance(i));


            if(i % 200 == 0){
                System.out.print((i+1) + " - ");
                System.out.print(test.instance(i).toString(test.classIndex()) + " - ");
                System.out.print(test.classAttribute().value((int) pred) + " - ");

                a[0] = (int)(dist[0]*100);
                a[1] = (int)(dist[1]*100);
                a[2] = (int)(dist[2]*100);
                a[3] = (int)(dist[3]*100);

                System.out.println(Utils.arrayToString(a));
            }
        }

    }
}
