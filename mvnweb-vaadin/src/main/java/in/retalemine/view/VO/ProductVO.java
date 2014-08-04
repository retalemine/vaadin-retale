package in.retalemine.view.VO;

import in.retalemine.view.constants.BillingConstants;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.measure.Measure;
import javax.measure.quantity.Quantity;

import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;

public class ProductVO<Q extends Quantity> {

	private final String productName;
	private final Measure<Double, Q> productUnit;
	private final String productDescription;
	private Set<Amount<Money>> unitRates;

	public ProductVO(String productName, Measure<Double, Q> productUnit,
			Set<Amount<Money>> unitRates) {
		this.productName = productName;
		this.productUnit = productUnit;
		this.productDescription = productName
				+ BillingConstants.PRODUCT_DESC_DIVIDER + productUnit;
		this.unitRates = unitRates;
	}

	public static <T extends Quantity> ProductVO<T> valueOf(String productName,
			Measure<Double, T> productUnit, Set<Amount<Money>> unitRates) {
		return new ProductVO<T>(productName, productUnit, unitRates);
	}

	public String getProductName() {
		return productName;
	}

	public Measure<Double, Q> getProductUnit() {
		return productUnit;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public Set<Amount<Money>> getUnitRates() {
		if (null == unitRates) {
			unitRates = new LinkedHashSet<Amount<Money>>(3);
		}
		return unitRates;
	}

	public void setUnitRates(Set<Amount<Money>> unitRates) {
		this.unitRates = unitRates;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((productDescription == null) ? 0 : productDescription
						.hashCode());
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
		if (!(obj instanceof ProductVO)) {
			return false;
		}
		ProductVO<?> other = (ProductVO<?>) obj;
		if (productDescription == null) {
			if (other.productDescription != null) {
				return false;
			}
		} else if (!productDescription.equals(other.productDescription)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("<ProductVO><productName>");
		builder.append(productName);
		builder.append("</productName><productUnit>");
		builder.append(productUnit);
		builder.append("</productUnit><productDescription>");
		builder.append(productDescription);
		builder.append("</productDescription><unitRates>");
		builder.append(unitRates);
		builder.append("</unitRates></ProductVO>");
		return builder.toString();
	}

}
