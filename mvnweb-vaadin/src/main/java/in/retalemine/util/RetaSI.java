package in.retalemine.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.measure.quantity.Dimensionless;
import javax.measure.unit.SystemOfUnits;
import javax.measure.unit.Unit;
import javax.measure.unit.UnitFormat;

public final class RetaSI extends SystemOfUnits {

	private static HashSet<Unit<?>> UNITS = new HashSet<Unit<?>>();

	private RetaSI() {
	}

	public static RetaSI getInstance() {
		return INSTANCE;
	}

	private static final RetaSI INSTANCE = new RetaSI();

	public static final Unit<Dimensionless> PIECE = retaSI(Unit.ONE);

	public static final Unit<Dimensionless> PACKET = PIECE.alternate("pkt");

	public static final Unit<Dimensionless> DOZEN = retaSI(PIECE.times(12));

	static {
		UnitFormat.getInstance().label(RetaSI.PIECE, "pcs");
		UnitFormat.getInstance().label(RetaSI.DOZEN, "dz");
	}

	@Override
	public Set<Unit<?>> getUnits() {
		return Collections.unmodifiableSet(UNITS);
	}

	/**
	 * Adds a new unit to the collection.
	 * 
	 * @param unit
	 *            the unit being added.
	 * @return <code>unit</code>.
	 */
	private static <U extends Unit<?>> U retaSI(U unit) {
		UNITS.add(unit);
		return unit;
	}

}
