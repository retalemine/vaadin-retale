package in.retalemine.view.tryout.beancontainer;

import java.io.Serializable;

public class Bean implements Serializable {
	private static final long serialVersionUID = 4163843400292862569L;

	String name;
	double energy; // Energy content in kJ/100g

	public Bean(String name, double energy) {
		System.out.println("entered");
		this.name = name;
		this.energy = energy;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getEnergy() {
		return energy;
	}

	public void setEnergy(double energy) {
		this.energy = energy;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(energy);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((name == null) ? 0 : name.toLowerCase().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bean other = (Bean) obj;
		if (Double.doubleToLongBits(energy) != Double
				.doubleToLongBits(other.energy))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equalsIgnoreCase(other.name))
			return false;
		return true;
	}

}
