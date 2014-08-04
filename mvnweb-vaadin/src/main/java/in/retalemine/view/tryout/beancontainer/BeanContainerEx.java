package in.retalemine.view.tryout.beancontainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class BeanContainerEx extends VerticalLayout {

	private static final long serialVersionUID = -6877740455977807471L;
	private static final Logger logger = LoggerFactory
			.getLogger(BeanContainerEx.class);

	public BeanContainerEx() {

		logger.info("{} - Initializing", getClass().getSimpleName());
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
