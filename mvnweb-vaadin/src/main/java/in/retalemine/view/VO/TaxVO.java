package in.retalemine.view.VO;

public class TaxVO {

	private final String taxType;
	private final double taxPercent;

	public TaxVO(String taxType, double taxPercent) {
		this.taxType = taxType;
		this.taxPercent = taxPercent;
	}

	public String getTaxType() {
		return taxType;
	}

	public double getTaxPercent() {
		return taxPercent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(taxPercent);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((taxType == null) ? 0 : taxType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof TaxVO)) {
			return false;
		}
		TaxVO other = (TaxVO) obj;
		if (Double.doubleToLongBits(taxPercent) != Double
				.doubleToLongBits(other.taxPercent)) {
			return false;
		}
		if (taxType == null) {
			if (other.taxType != null) {
				return false;
			}
		} else if (!taxType.equals(other.taxType)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return taxType + "(" + taxPercent + " %)";
	}

}
