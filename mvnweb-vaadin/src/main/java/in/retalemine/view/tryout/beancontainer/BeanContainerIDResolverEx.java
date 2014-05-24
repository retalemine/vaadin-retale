package in.retalemine.view.tryout.beancontainer;

import com.vaadin.data.util.AbstractBeanContainer.BeanIdResolver;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class BeanContainerIDResolverEx extends VerticalLayout {

	private static final long serialVersionUID = -678706567990466923L;

	public BeanContainerIDResolverEx() {

		BeanContainer<Object, Bean> beans = new BeanContainer<Object, Bean>(
				Bean.class);

		beans.setBeanIdResolver(new BeanIdResolver<Object, Bean>() {

			private static final long serialVersionUID = 4520974921459435936L;

			@Override
			public Object getIdForBean(Bean bean) {
				return bean;
			}

		});

		beans.addBean(new Bean("Mung bean", 1452.0));
		beans.addBean(new Bean("Chickpea", 686.0));
		beans.addBean(new Bean("Lentil", 1477.0));
		beans.addBean(new Bean("Soybean", 1866.0));
		beans.addBean(new Bean("Common bean", 129.0));
		beans.addBean(new Bean("Soybean", 1866.0));
		beans.addBean(new Bean("common bean", 129.0));
		beans.addBean(new Bean("Lentil", 1474.0));
		beans.addBean(new Bean("lentil", 1477.0));

		Table table = new Table("Beans of All Sorts - by name and value", beans);
		addComponent(table);
	}

}
