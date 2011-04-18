

package cave2.structures;

import libnoiseforjava.module.ModuleBase;

/**
 * Module that measures the amount of getValue calls.
 * @author Colonel 32
 */
public class SampleMeasurer extends ModuleBase
{
	public int sampleCount;

	public SampleMeasurer(ModuleBase source)
	{
		super(1);
		sourceModules[0] = source;
		sampleCount = 0;
	}

	public double getValue(double x, double y, double z)
	{
		sampleCount++;
		return sourceModules[0].getValue(x, y, z);
	}
}
