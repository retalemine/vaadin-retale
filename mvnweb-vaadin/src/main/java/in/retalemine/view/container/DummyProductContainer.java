package in.retalemine.view.container;

import in.retalemine.util.Rupee;
import in.retalemine.view.VO.ProductVO;

import java.util.ArrayList;
import java.util.List;

import javax.measure.Measure;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;

import org.jscience.physics.amount.Amount;

public class DummyProductContainer {

	public static List<ProductVO> DummyProductList() {

		String[][] massProductList = { { "Sugar", "45" },
				{ "Toor Dhall", "75" }, { "Sun Light Soap", "15" } };
		String[][] volumeProductList = { { "Sunflower Oil", "150" },
				{ "Ghee", "200" } };

		List<ProductVO> productList = new ArrayList<ProductVO>();
		ProductVO productVO;
		for (String[] massProduct : massProductList) {
			productVO = new ProductVO();
			productVO.setProductName(massProduct[0]);
			productVO.setProductUnit(Measure.valueOf(1.0, SI.KILOGRAM));
			productVO.setProductDescription(productVO.getProductName() + " - "
					+ productVO.getProductUnit());
			productVO.getUnitPrices().add(
					Amount.valueOf(Double.parseDouble(massProduct[1]),
							Rupee.INR));
			productVO.getUnitPrices().add(
					Amount.valueOf(Double.parseDouble(massProduct[1]) + 20,
							Rupee.INR));
			productList.add(productVO);
		}

		for (String[] volumeProduct : volumeProductList) {
			productVO = new ProductVO();
			productVO.setProductName(volumeProduct[0]);
			productVO.setProductUnit(Measure.valueOf(1.0, NonSI.LITER));
			productVO.setProductDescription(productVO.getProductName() + " - "
					+ productVO.getProductUnit());
			productVO.getUnitPrices().add(
					Amount.valueOf(Double.parseDouble(volumeProduct[1]),
							Rupee.INR));
			productVO.getUnitPrices().add(
					Amount.valueOf(Double.parseDouble(volumeProduct[1]) + 10,
							Rupee.INR));
			productList.add(productVO);
		}

		return productList;
	}
}
