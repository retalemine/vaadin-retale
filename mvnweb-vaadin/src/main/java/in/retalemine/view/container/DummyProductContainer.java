package in.retalemine.view.container;

import in.retalemine.util.Rupee;
import in.retalemine.view.VO.ProductVO;

import java.util.ArrayList;
import java.util.List;

import javax.measure.Measure;
import javax.measure.quantity.Mass;
import javax.measure.quantity.Quantity;
import javax.measure.quantity.Volume;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;

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
			massProductVO = new ProductVO<Mass>();
			massProductVO.setProductName(massProduct[0]);
			massProductVO.setProductUnit(Measure.valueOf(1.0, SI.KILOGRAM));
			massProductVO.setProductDescription(massProductVO.getProductName()
					+ " - " + massProductVO.getProductUnit());
			massProductVO.getUnitPrices().add(
					Amount.valueOf(Double.parseDouble(massProduct[1]),
							Rupee.INR));
			massProductVO.getUnitPrices().add(
					Amount.valueOf(Double.parseDouble(massProduct[1]) + 20,
							Rupee.INR));
			productList.add(massProductVO);
		}

		ProductVO<Volume> volumeProductVO;
		for (String[] volumeProduct : volumeProductList) {
			volumeProductVO = new ProductVO<Volume>();
			volumeProductVO.setProductName(volumeProduct[0]);
			volumeProductVO.setProductUnit(Measure.valueOf(1.0, NonSI.LITER));
			volumeProductVO.setProductDescription(volumeProductVO
					.getProductName()
					+ " - "
					+ volumeProductVO.getProductUnit());
			volumeProductVO.getUnitPrices().add(
					Amount.valueOf(Double.parseDouble(volumeProduct[1]),
							Rupee.INR));
			volumeProductVO.getUnitPrices().add(
					Amount.valueOf(Double.parseDouble(volumeProduct[1]) + 10,
							Rupee.INR));
			productList.add(volumeProductVO);
		}

		return productList;
	}
}
