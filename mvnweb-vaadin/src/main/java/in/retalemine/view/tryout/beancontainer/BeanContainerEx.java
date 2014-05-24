package in.retalemine.view.tryout.beancontainer;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class BeanContainerEx extends VerticalLayout {

	private static final long serialVersionUID = -6877740455977807471L;

	public BeanContainerEx() {

		BeanContainer<String, Bean> beans = new BeanContainer<String, Bean>(
				Bean.class);

		beans.setBeanIdProperty("name");

		beans.addBean(new Bean("Mung bean", 1452.0));
		beans.addBean(new Bean("Chickpea", 686.0));
		beans.addBean(new Bean("Lentil", 1477.0));
		beans.addBean(new Bean("Soybean", 1866.0));
		beans.addBean(new Bean("Common bean", 129.0));
		beans.addBean(new Bean("Soybean", 1866.0));
		beans.addBean(new Bean("common bean", 129.0));
		beans.addBean(new Bean("Lentil", 1474.0));
		beans.addBean(new Bean("lentil", 1477.0));

		Table table = new Table("Beans of All Sorts - by name", beans);
		addComponent(table);
	}

}
