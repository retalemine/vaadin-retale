package in.retalemine.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
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
}
