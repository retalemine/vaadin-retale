package in.retalemine.view.VO;

import javax.measure.Measure;
import javax.measure.converter.UnitConverter;
import javax.measure.quantity.Quantity;

import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;

public class BillItemVO<U extends Quantity, V extends Quantity> {

	private Integer serialNo;
	private String productName;
	private Measure<Double, U> productUnit;
	private String productDescription;
	private Amount<Money> unitPrice;
	private Measure<Double, V> quantity;
	private Amount<Money> amount;

	public Integer getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Measure<Double, U> getProductUnit() {
		return productUnit;
	}

	public void setProductUnit(Measure<Double, U> productUnit) {
		this.productUnit = productUnit;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public Amount<Money> getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Amount<Money> unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Measure<Double, V> getQuantity() {
		return quantity;
	}

	public void setQuantity(Measure<Double, V> quantity) {
		this.quantity = quantity;
	}

	public Amount<Money> getAmount() {
		return amount;
	}

	public void setAmount(Amount<Money> amount) {
		this.amount = amount;
	}

	public static <U extends Quantity, V extends Quantity> BillItemVO<U, V> valueOf(
			Integer serialNo, String productName,
			Measure<Double, U> productUnit, String productDescription,
			Amount<Money> unitPrice, Measure<Double, V> quantity) {
		UnitConverter quantityToUnit = quantity.getUnit().getConverterTo(
				productUnit.getUnit());
		BillItemVO<U, V> billItemVO = new BillItemVO<U, V>();

		billItemVO.setSerialNo(serialNo);
		billItemVO.setProductName(productName);
		billItemVO.setProductUnit(productUnit);
		billItemVO.setProductDescription(productDescription);
		billItemVO.setUnitPrice(unitPrice);
		billItemVO.setQuantity(quantity);
		billItemVO.setAmount(unitPrice.times(quantityToUnit.convert(quantity
				.getValue()) / productUnit.getValue()));
		return billItemVO;
	}

}
