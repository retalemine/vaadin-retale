package in.retalemine.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UnitUtil {

	public static final List<Set<String>> unitList = new ArrayList<Set<String>>() {
		private static final long serialVersionUID = 4846226789583481150L;

		{
			add(new HashSet<String>(Arrays.asList("kg", "g")));
			add(new HashSet<String>(Arrays.asList("L", "mL")));
			add(new HashSet<String>(Arrays.asList("m", "ft", "in")));
			add(new HashSet<String>(Arrays.asList("pcs", "pkt", "dz")));
			// pcs,pkt,dz should be at last
		}
	};
	private static HashMap<String, String> unitMap = new HashMap<String, String>() {
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
			put("pkt", "pkt");
			put("packet", "pkt");
			put("dz", "dz");
			put("dozen", "dz");
			put("dozens", "dz");
		}
	};

	private UnitUtil() {
	}

	public static String getValidUnit(String key) {
		return unitMap.get(key.toLowerCase());
	}

	public static Set<String> getValidUnitList() {
		Set<String> unitSet = new HashSet<String>();
		unitSet.addAll(unitMap.values());
		return unitSet;
	}

	public static Set<String> getValidAltUnitList(String unit) {
		for (Set<String> set : unitList) {
			if (set.contains(unit)) {
				set.addAll(unitList.get(unitList.size() - 1));
				return set;
			}
		}
		return null;
	}

}
