package in.retalemine.view.converter;

import in.retalemine.view.component.BillingComponent;

import java.util.Locale;

import org.jscience.physics.amount.Amount;

import com.vaadin.data.util.converter.Converter;

@SuppressWarnings("rawtypes")
public class AmountConverter implements Converter<String, Amount> {

	private static final long serialVersionUID = -3485544610168611605L;

	@Override
	public Amount convertToModel(String value,
			Class<? extends Amount> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		Amount<?> amount = null;
		if (null != value && !value.isEmpty()) {
			try {
				amount = Amount.valueOf(value);
			} catch (Exception e) {
				amount = Amount.valueOf(Double.parseDouble(value),
						BillingComponent.getGlobalCurrency());
			}
		}
		return amount;
	}

	@Override
	public String convertToPresentation(Amount value,
			Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (null != value) {
			return value.toString();
		} else {
			return "";
		}

	}

	@Override
	public Class<Amount> getModelType() {
		return Amount.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}

}
