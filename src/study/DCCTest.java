package study;

import simulation.lib.counter.DiscreteConfidenceCounter;
import simulation.lib.randVars.RandVar;
import simulation.lib.randVars.continous.ErlangK;
import simulation.lib.randVars.continous.Exponential;
import simulation.lib.randVars.continous.HyperExponential;
import simulation.lib.randVars.continous.Normal;
import simulation.lib.rng.StdRNG;

/*
 * TODO Problem 3.1.3 and 3.1.4 - implement this class
 */
public class DCCTest {

    public static void main(String[] args) {
        testDCC();
    }

    public static void testDCC() {
        StdRNG rng = new StdRNG(System.currentTimeMillis());
        
        Normal distributionNormal = new Normal(rng);
        ErlangK distributionErlang = new ErlangK(rng, 10, 1);
        Exponential distributionExp = new Exponential(rng, 10, 10);
        HyperExponential distributionHyperExp = new HyperExponential(rng);
        
        runThroughDistribution(distributionNormal);
        runThroughDistribution(distributionErlang);
        runThroughDistribution(distributionExp);
        runThroughDistribution(distributionHyperExp);
    }
    
    static void runThroughDistribution(RandVar distribution) {
    	DiscreteConfidenceCounter counter = new DiscreteConfidenceCounter("COUNTER TEST RUNS");
    	
    	double mean = 10;
        double[] cVars = new double[]{0.25, 0.5, 1, 2, 4};
        int[] sampleSizes = new int[]{5, 10, 50, 100};
        double[]alphas = new double[]{0.1, 0.05}; //two-sided means half the alphas?! not sure TODO:
        
        System.out.println("DISTRIBUTION = " + distribution.getType());
        
        for (double a : alphas) {
        	counter.setAlpha(a);
        	for (double cv : cVars) {
        		if(distribution.getType() != "Exponential"){
        			if(distribution.getType() == "HyperExponential") {
        				distribution.setMean(mean);
        				distribution.setCvar(cv);
        			} else {
        				distribution.setMeanAndCvar(mean, cv);
        			}
        		} else { //Exponential distribution can only handle a CV of 1 (mean must be equal to stdDev)
            			distribution.setMeanAndCvar(mean, 1);
        		}
				for (int sam : sampleSizes) {
					int meanCorrectCounter = 0;
					for(int exp = 0; exp < 500; exp++) {
						counter.reset();
						for(int i = 0; i < sam; i++) {
				        	double rv = distribution.getRV();
				        	counter.count(rv);
				        }
						if(mean <= counter.getUpperBound() && mean >= counter.getLowerBound()) {
//							System.out.println("LOWER: " + counter.getLowerBound() + " MEAN: " + counter.getMean() + " UPPER: " + counter.getUpperBound());
							meanCorrectCounter++;
						}
						
//						System.out.println("t for alpha = "+a+", sampleSize = "+sam+", cVar = "+ cv + ", mean = 10 -> " +  counter.getT(sam));
//						System.out.println(counter.report());
					}
					double fraction = (double)meanCorrectCounter/500d;
					if(fraction > 0.0){
						System.out.println("A fraction of " +fraction + " had a mean in bounds with parameters:");
						System.out.println("alpha = "+a+", sampleSize = "+sam+", cVar = "+ cv + ", mean = 10  and t is:" +  counter.getT(sam));			
					}
				}
			}
		}
    }
}
