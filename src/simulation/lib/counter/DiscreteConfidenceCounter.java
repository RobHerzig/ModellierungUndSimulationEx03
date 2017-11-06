package simulation.lib.counter;

/**
 * This class implements a discrete time confidence counter
 */
public class DiscreteConfidenceCounter extends DiscreteCounter{
    /*
     * TODO Problem 3.1.2 - implement this class according to the given class diagram!
     * Hint: see section 4.4 in course syllabus
     */


	/*	Row 1: degrees of freedom
     *  Row 2: alpha 0.01
     *  Row 3: alpha 0.05
     *  Row 4: alpha 0.10
     */
    private double tAlphaMatrix[][] = new double[][]{
            {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 1000000},
            {63.657, 9.925, 5.841, 4.604, 4.032, 3.707, 3.499, 3.355, 3.250, 3.169, 2.845, 2.750, 2.704, 2.678, 2.660, 2.648, 2.639, 2.632, 2.626, 2.576},
            {12.706, 4.303, 3.182, 2.776, 2.571, 2.447, 2.365, 2.306, 2.262, 2.228, 2.086, 2.042, 2.021, 2.009, 2.000, 1.994, 1.990, 1.987, 1.984, 1.960},
            {6.314, 2.920, 2.353, 2.132, 2.015, 1.943, 1.895, 1.860, 1.833, 1.812, 1.725, 1.697, 1.684, 1.676, 1.671, 1.667, 1.664, 1.662, 1.660, 1.645}};

    private double alpha = 0.05;
    
    //Are constructors necessary or does java simply allow this based on the parent class?
    
    public DiscreteConfidenceCounter(String variable) {
		super(variable,  "counter type: discrete confidence counter");
	}

	public DiscreteConfidenceCounter(String variable, String type, double alpha) {
		super(variable, type);
		this.alpha = alpha;
	}
	
	public DiscreteConfidenceCounter(String variable, double alpha) {
		super(variable, "counter type: discrete confidence counter");
		this.alpha = alpha;
	}
	
    
    private double getAlpha() {
		return alpha;
	}

	private void setAlpha(double alpha) {
		this.alpha = alpha;
	}
	
	public double getZ(long numSamples){
		
		//choose row in which to look for the value (in our t table)
		int row = 1; //if alpha < 0.05 -> stays at last row in matrix
		//otherwise: below 0.1 -> second row, else first row
		if (alpha < 0.05){
			row = 3;
		}else if (alpha < 0.1){
			row = 2;
		}
		
		//initialize high and low x
		int xHigh = tAlphaMatrix[0].length -1;
		int xLow = tAlphaMatrix[0].length -1;
		
		for(int i = 0; i < tAlphaMatrix[0].length; i++){
			if(numSamples > tAlphaMatrix[0][i]) 
				continue;
			else if(numSamples == tAlphaMatrix[0][i]) 
				return tAlphaMatrix[row][i];
			
			//between two values, set higher and lower one
			xHigh = i;
			xLow = i-1;
			break;
		}

		if (xLow == xHigh) 
			return tAlphaMatrix[row][xLow];

		return linearInterpolation(tAlphaMatrix[0][xLow], tAlphaMatrix[0][xHigh], tAlphaMatrix[row][xLow],tAlphaMatrix[row][xHigh], numSamples);
	}
	
	//TODO: Implement this
	private double linearInterpolation(double nlow, double nhigh, double zlow, 
			double zhigh, long numSamples){
		return zlow + ((zhigh - zlow)/(nhigh -nlow)) * (numSamples - nlow);
	}
            
    /**
     * @see Counter#report()
     * Uncomment this function when you have implemented this class for reporting.
     */
    /*@Override
    public String report() {
        String out = super.report();
        out += ("" + "\talpha = " + alpha + "\n" +
                "\tt(1-alpha/2) = " + getT(getNumSamples() - 1) + "\n" +
                "\tlower bound = " + getLowerBound() + "\n" +
                "\tupper bound = " + getUpperBound());
        return out;
    }*/

    /**
     * @see Counter#csvReport(String)
     * Uncomment this function when you have implemented this class for reporting.
     */
    /*@Override
    public void csvReport(String outputdir) {
        String content = observedVariable + ";" + getNumSamples() + ";" + getMean() + ";" + getVariance() + ";" + getStdDeviation() + ";" +
                getCvar() + ";" + getMin() + ";" + getMax() + ";" + alpha + ";" + getT(getNumSamples() - 1) + ";" + getLowerBound() + ";" +
                getUpperBound() + "\n";
        String labels = "#counter ; numSamples ; MEAN; VAR; STD; CVAR; MIN; MAX;alpha;t(1-alpha/2);lowerBound;upperBound\n";
        writeCsv(outputdir, content, labels);
    }*/

}
