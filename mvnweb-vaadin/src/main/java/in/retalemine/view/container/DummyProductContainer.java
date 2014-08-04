package in.retalemine.view.container;

import in.retalemine.util.Rupee;
import in.retalemine.view.VO.ProductVO;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.measure.Measure;
import javax.measure.quantity.Mass;
import javax.measure.quantity.Quantity;
import javax.measure.quantity.Volume;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;

import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;

public class DummyProductContainer {

	public static List<ProductVO<? extends Quantity>> DummyProductList() {

		String[][] massProductList = { { "Sugar", "45" },
				{ "Toor Dhall", "75" }, { "Sun Light Soap", "15" } };
		String[][] volumeProductList = { { "Sunflower Oil", "150" },
				{ "Ghee", "200" } };

		List<ProductVO<? extends Quantity>> productList = new ArrayList<ProductVO<? extends Quantity>>();
		ProductVO<Mass> massProductVO;
		for (String[] massProduct : massProductList) {
			Set<Amount<Money>> unitRates = new LinkedHashSet<Amount<Money>>(3);
			unitRates.add(Amount.valueOf(Double.parseDouble(massProduct[1]),
					Rupee.INR));
			unitRates.add(Amount.valueOf(
					Double.parseDouble(massProduct[1]) + 20, Rupee.INR));
			massProductVO = new ProductVO<Mass>(massProduct[0],
					Measure.valueOf(1.0, SI.KILOGRAM), unitRates);

			productList.add(massProductVO);
		}

		ProductVO<Volume> volumeProductVO;
		for (String[] volumeProduct : volumeProductList) {
			Set<Amount<Money>> unitRates = new LinkedHashSet<Amount<Money>>(3);
			unitRates.add(Amount.valueOf(Double.parseDouble(volumeProduct[1]),
					Rupee.INR));
			unitRates.add(Amount.valueOf(
					Double.parseDouble(volumeProduct[1]) + 10, Rupee.INR));

			volumeProductVO = new ProductVO<Volume>(volumeProduct[0],
					Measure.valueOf(1.0, NonSI.LITER), unitRates);
			productList.add(volumeProductVO);
		}

		return productList;
	}
}
