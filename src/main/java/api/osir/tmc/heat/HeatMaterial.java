package api.osir.tmc.heat;

public class HeatMaterial {
	private float specificHeat, meltTemp;

	public HeatMaterial(float specificHeat, float meltTemp) {
		this.specificHeat = specificHeat;
		this.meltTemp = meltTemp;
	}

	public float getSpecificHeat() {
		return this.specificHeat;
	}

	public float getMeltTemp() {
		return this.meltTemp;
	}
}