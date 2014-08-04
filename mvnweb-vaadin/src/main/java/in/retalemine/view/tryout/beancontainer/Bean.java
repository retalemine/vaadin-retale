package in.retalemine.view.tryout.beancontainer;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bean implements Serializable {
	private static final long serialVersionUID = 4163843400292862569L;
	private static final Logger logger = LoggerFactory.getLogger(Bean.class);

	String name;
	double energy; // Energy content in kJ/100g

	public Bean(String name, double energy) {
		logger.info("{} - Initializing", getClass().getSimpleName());
		this.name = name;
		this.energy = energy;
	}

	public String getName() {
		logger.info("{} - Inside getName", getClass().getSimpleName());
		return name;
	}

	public void setName(String name) {
		logger.info("{} - Inside setName", getClass().getSimpleName());
		this.name = name;
	}

	public double getEnergy() {
		logger.info("{} - Inside getEnergy", getClass().getSimpleName());
		return energy;
	}

	public void setEnergy(double energy) {
		logger.info("{} - Inside setEnergy", getClass().getSimpleName());
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
		logger.info("{} - hashcode - {}", getClass().getSimpleName(), result);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		logger.info("{} - equals check", getClass().getSimpleName());
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Bean - [name=");
		builder.append(name);
		builder.append(", energy=");
		builder.append(energy);
		builder.append("]");
		return builder.toString();
	}

}
