package in.retalemine.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class UnitUtil {

	private Set<String> unitMassSet = new HashSet<String>(Arrays.asList("kg",
			"g"));
	private Set<String> unitVolumeSet = new HashSet<String>(Arrays.asList("L",
			"mL"));
	private Set<String> unitLengthSet = new HashSet<String>(Arrays.asList("m",
			"ft", "in"));
	private Set<String> unitPcsSet = new HashSet<String>(Arrays.asList("pcs",
			"dz"));
	
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

}
