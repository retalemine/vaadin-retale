package in.retalemine.view.VO;

import in.retalemine.util.BillingComputationUtil;

import java.util.Set;

import javax.measure.Measure;
import javax.measure.quantity.Quantity;

import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;

public class BillItemVO<U extends Quantity, V extends Quantity> extends
		ProductVO<U> {

	private Amount<Money> unitRate;
	private Measure<Double, V> netQuantity;
	private Amount<Money> amount;

	public BillItemVO(String productName, Measure<Double, U> productUnit,
			Set<Amount<Money>> unitRates, Amount<Money> unitRate,
			Measure<Double, V> netQuantity) {
		super(productName, productUnit, unitRates);
		this.unitRate = unitRate;
		this.netQuantity = netQuantity;
		this.amount = BillingComputationUtil.computeAmount(productUnit,
				unitRate, netQuantity);

	}

	public static <U extends Quantity, V extends Quantity> BillItemVO<U, V> valueOf(
			ProductVO<U> productVO, Amount<Money> unitRate,
			Measure<Double, V> netQuantity) {
		return new BillItemVO<U, V>(productVO.getProductName(),
				productVO.getProductUnit(), productVO.getUnitRates(), unitRate,
				netQuantity);
	}

	public Amount<Money> getUnitRate() {
		return unitRate;
	}

	public void setUnitRate(Amount<Money> unitRate) {
		this.unitRate = unitRate;
	}

	public Measure<Double, V> getNetQuantity() {
		return netQuantity;
	}

	public void setNetQuantity(Measure<Double, V> netQuantity) {
		this.netQuantity = netQuantity;
	}

	public Amount<Money> getAmount() {
		return amount;
	}

	public void setAmount(Amount<Money> amount) {
		this.amount = amount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((unitRate == null) ? 0 : unitRate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof BillItemVO)) {
			return false;
		}
		BillItemVO<?, ?> other = (BillItemVO<?, ?>) obj;
		if (unitRate == null) {
			if (other.unitRate != null) {
				return false;
			}
		} else if (!unitRate.equals(other.unitRate)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(super.toString());
		builder.append("<BillItemVO><unitRate>");
		builder.append(unitRate);
		builder.append("</unitRate><netQuantity>");
		builder.append(netQuantity);
		builder.append("</netQuantity><amount>");
		builder.append(amount);
		builder.append("</amount></BillItemVO>");
		return builder.toString();
	}

}
