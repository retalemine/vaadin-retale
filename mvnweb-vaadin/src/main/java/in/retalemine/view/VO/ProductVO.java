package in.retalemine.view.VO;

import java.util.ArrayList;
import java.util.List;

import javax.measure.Measure;
import javax.measure.quantity.Quantity;

import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;

public class ProductVO{

	//private static final long serialVersionUID = -764415971860198921L;
	
	protected String productName;
	protected Measure<Double, ? extends Quantity> productUnit;
	protected String productDescription;
	private List<Amount<Money>> unitPrices = new ArrayList<Amount<Money>>();

	public ProductVO() {
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Measure<Double, ? extends Quantity> getProductUnit() {
		return productUnit;
	}

	public void setProductUnit(Measure<Double, ? extends Quantity> productUnit) {
		this.productUnit = productUnit;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public List<Amount<Money>> getUnitPrices() {
		return unitPrices;
	}

	public void setUnitPrices(List<Amount<Money>> unitPrices) {
		this.unitPrices = unitPrices;
	}

}
