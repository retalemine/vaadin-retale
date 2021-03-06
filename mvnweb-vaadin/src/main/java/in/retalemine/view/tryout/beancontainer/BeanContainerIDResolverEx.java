package in.retalemine.view.tryout.beancontainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.util.AbstractBeanContainer.BeanIdResolver;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class BeanContainerIDResolverEx extends VerticalLayout {

	private static final long serialVersionUID = -678706567990466923L;
	private static final Logger logger = LoggerFactory
			.getLogger(BeanContainerIDResolverEx.class);

	public BeanContainerIDResolverEx() {
		logger.info("{} - Initializing", getClass().getSimpleName());
		BeanContainer<Object, Bean> beans = new BeanContainer<Object, Bean>(
				Bean.class);

		beans.setBeanIdResolver(new BeanIdResolver<Object, Bean>() {

			private static final long serialVersionUID = 4520974921459435936L;

			@Override
			public Object getIdForBean(Bean bean) {
				logger.info("{} - IdResolver", getClass().getSimpleName());
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
		beans.addBean(new Bean("Common bean", 130.0));
		beans.addBean(new Bean("common bean", 141.0));

		logger.info("{} Starts", getClass().getSimpleName());
		logger.info("{} - items ids {}\n{}", getClass().getSimpleName(),
				beans.getItemIds(), beans.getItemIds().toArray());
		logger.info("{} Ends", getClass().getSimpleName());
		if (beans.containsId(new Bean("Common bean", 130.0))) {
			logger.info("{} - contains Common bean", getClass().getSimpleName());
		}
		
		if (beans.containsId("Soybean")) {
			logger.info("{} - contains Soybean", getClass().getSimpleName());
		}

		Table table = new Table("Beans of All Sorts - by name", beans);
		addComponent(table);
	}

}
