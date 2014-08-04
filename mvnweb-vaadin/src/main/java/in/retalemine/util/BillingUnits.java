package in.retalemine.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.measure.quantity.Dimensionless;
import javax.measure.unit.SystemOfUnits;
import javax.measure.unit.Unit;
import javax.measure.unit.UnitFormat;

import org.jscience.economics.money.Currency;

public final class BillingUnits extends SystemOfUnits {

	private static HashSet<Unit<?>> UNITS = new HashSet<Unit<?>>(4);
	public static final Currency INR;

	private BillingUnits() {
	}

	public static BillingUnits getInstance() {
		return INSTANCE;
	}

	private static final BillingUnits INSTANCE = new BillingUnits();

	public static final Unit<Dimensionless> PIECE = retaSI(Unit.ONE);

	// TODO
	// need to check the need for pkt
	// if needed what is the right implementation
	// current implementation is ambiguous as 1-dz to 1-pkt considered as 1pcs
	// public static final Unit<Dimensionless> PACKET = PIECE.alternate("pkt");

	public static final Unit<Dimensionless> DOZEN = retaSI(PIECE.times(12));

	static {
		UnitFormat.getInstance().label(BillingUnits.PIECE, "pcs");
		UnitFormat.getInstance().label(BillingUnits.DOZEN, "dz");
		INR = new Currency("INR");
	}

	@Override
	public Set<Unit<?>> getUnits() {
		return Collections.unmodifiableSet(UNITS);
	}

	private static <U extends Unit<?>> U retaSI(U unit) {
		UNITS.add(unit);
		return unit;
	}

}
