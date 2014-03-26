package in.retalemine.view.tryout;

import com.vaadin.data.util.IndexedContainer;

public class DummyTableData {

	private DummyTableData() {

	}

	@SuppressWarnings("unchecked")
	public static IndexedContainer createDummyDatasource() {
		IndexedContainer ic = new IndexedContainer();

		String[] fieldNames = new String[] { "First Name", "Last Name",
				"Company", "Mobile Phone", "Work Phone", "Home Phone",
				"Work Email", "Home Email", "Street", "City", "Zip", "State",
				"Country" };

		for (String p : fieldNames) {
			ic.addContainerProperty(p, String.class, "");
		}

		/* Create dummy data by randomly combining first and last names */
		String[] fnames = { "Peter", "Alice", "Joshua", "Mike", "Olivia",
				"Nina", "Alex", "Rita", "Dan", "Umberto", "Henrik", "Rene",
				"Lisa", "Marge" };
		String[] lnames = { "Smith", "Gordon", "Simpson", "Brown", "Clavel",
				"Simons", "Verne", "Scott", "Allison", "Gates", "Rowling",
				"Barks", "Ross", "Schneider", "Tate" };
		for (int i = 0; i < 1000; i++) {
			Object id = ic.addItem();
			ic.getContainerProperty(id, "First Name").setValue(
					fnames[(int) (fnames.length * Math.random())]);
			ic.getContainerProperty(id, "Last Name").setValue(
					lnames[(int) (lnames.length * Math.random())]);
		}

		return ic;
	}

	public static Object[] getVisibleColumnsList() {
		return new Object[] { "First Name", "Last Name", "Company" };
	}
}
