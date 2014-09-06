package in.retalemine.util;

import in.retalemine.view.VO.BillItemVO;
import in.retalemine.view.VO.TaxVO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.measure.Measure;
import javax.measure.converter.ConversionException;
import javax.measure.converter.UnitConverter;
import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanContainer;

public class BillingComputationUtil {

	private static final Logger logger = LoggerFactory
			.getLogger(BillingComputationUtil.class);

	private static final List<Set<String>> validUnitsGroup = new ArrayList<Set<String>>() {
		private static final long serialVersionUID = 4846226789583481150L;

		{
			add(new LinkedHashSet<String>(Arrays.asList("kg", "g")));
			add(new LinkedHashSet<String>(Arrays.asList("L", "mL")));
			add(new LinkedHashSet<String>(Arrays.asList("m", "ft", "in")));
			add(new LinkedHashSet<String>(Arrays.asList("pcs", "dz"))); // "pkt"
			// Following units pcs, pkt, dz applicable for all other units
			// should be the last entry and used by {@link getValidAltUnitList}
		}
	};
	private static final HashMap<String, String> validUnitsMapper = new HashMap<String, String>() {
		private static final long serialVersionUID = 2572938028394300264L;
		{
			put("kg", "kg");
			put("g", "g");
			put("gram", "g");
			put("l", "L");
			put("lt", "L");
			put("litre", "L");
			put("liter", "L");
			put("ml", "mL");
			put("m", "m");
			put("meter", "m");
			put("ft", "ft");
			put("foot", "ft");
			put("in", "in");
			put("inch", "in");
			put("inches", "in");
			put("pcs", "pcs");
			put("pc", "pcs");
			put("piece", "pcs");
			put("pieces", "pcs");
			// put("pkt", "pkt");
			// put("packet", "pkt");
			put("dz", "dz");
			put("dozen", "dz");
			put("dozens", "dz");
		}
	};

	private static final Map<String, Double> taxPercentMap = new HashMap<String, Double>() {

		private static final long serialVersionUID = 570223805652706264L;

		{
			put("VAT", 4.0);
			put("Sales Tax", 5.0);
			put("Service Tax", 12.0);
		}
	};

	private BillingComputationUtil() {
	}

	public static String getValidUnit(String unit) {
		return validUnitsMapper.get(unit.toLowerCase());
	}

	public static Set<String> getValidUnits() {
		Set<String> validUnits = new HashSet<String>(validUnitsGroup.size() * 3);
		for (Set<String> units : validUnitsGroup) {
			validUnits.addAll(units);
		}
		return validUnits;
	}

	public static Set<String> getValidUnits(String unit) {
		for (Set<String> units : validUnitsGroup) {
			if (units.contains(unit)) {
				units.addAll(validUnitsGroup.get(validUnitsGroup.size() - 1));
				return units;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <U extends Quantity, V extends Quantity, W extends Quantity> Measure<Double, ? extends Quantity> computeNetQuantity(
			Measure<Double, U> productUnit, Measure<Double, V> quantity1,
			Measure<Double, W> quantity2) {
		if (quantity1.getUnit().equals(quantity2.getUnit())) {
			logger.debug("equal units");
			return Measure.valueOf(quantity1.getValue() + quantity2.getValue(),
					quantity1.getUnit());
		} else {
			logger.debug("not equal units");
			String units[] = getValidUnits(productUnit.getUnit().toString())
					.toArray(new String[0]);
			return computeNetQuantity(productUnit,
					quantity1.to((Unit<V>) Unit.valueOf(units[0])),
					quantity2.to((Unit<W>) Unit.valueOf(units[0])));
		}
	}

	public static <U extends Quantity, V extends Quantity> Amount<Money> computeAmount(
			Measure<Double, U> unitQuantity, Amount<Money> unitRate,
			Measure<Double, V> netQuantity) {
		if (unitQuantity.getUnit().isCompatible(netQuantity.getUnit())) {
			UnitConverter toNetQuantityUnit = unitQuantity.getUnit()
					.getConverterTo(netQuantity.getUnit());
			return unitRate.times(netQuantity.getValue()
					/ toNetQuantityUnit.convert(unitQuantity.getValue()));
		} else {
			if (BillingUnits.PIECE.equals(netQuantity.getUnit())) {
				return unitRate.times(netQuantity.getValue());
			}
			// else if
			// (RetaSI.PACKET.equals(netQuantity.getUnit())) {
			// return unitPrice.times(
			// netQuantity.getValue());
			// }
			else if (BillingUnits.DOZEN.equals(netQuantity.getUnit())) {
				return unitRate.times(netQuantity.getValue() * 12);
			} else {
				throw new ConversionException(String.format(
						"Failed conversion : %s -> %s", unitQuantity.getUnit(),
						netQuantity.getUnit()));
			}
		}
	}

	public static Set<TaxVO> getBillingTaxSet() {
		Set<TaxVO> taxSet = new HashSet<TaxVO>();
		for (Map.Entry<String, Double> tax : taxPercentMap.entrySet()) {
			taxSet.add(new TaxVO(tax.getKey(), tax.getValue()));
		}
		return taxSet;
	}

	@SuppressWarnings("unchecked")
	public static <U> Container getContainerSource(Collection<U> options,
			Container container) {
		if (options != null) {
			if (container instanceof BeanContainer) {
				for (final Iterator<U> i = options.iterator(); i.hasNext();) {
					((BeanContainer<?, U>) container).addBean(i.next());
				}
			} else {
				for (final Iterator<U> i = options.iterator(); i.hasNext();) {
					container.addItem(i.next());
				}
			}

		}
		return container;
	}

	public static Amount<Money> computeSubAmount(
			List<BillItemVO<? extends Quantity, ? extends Quantity>> billItemIds) {
		Amount<Money> subAmount = Amount.valueOf(0.0, BillingUnits.INR);
		for (BillItemVO<? extends Quantity, ? extends Quantity> billItemVO : billItemIds) {
			subAmount = subAmount.plus(billItemVO.getAmount());
		}
		return subAmount;
	}

	public static Amount<Money> computeTotalAmount(Amount<Money> subTotal,
			List<TaxVO> taxVOList) {
		double taxPercent = 0.0;
		for (TaxVO taxVO : taxVOList) {
			taxPercent += taxVO.getTaxPercent();
		}
		return subTotal.times((100 + taxPercent) / 100);
	}
}
