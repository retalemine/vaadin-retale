package in.retalemine.view.converter;

import java.util.Locale;

import org.jscience.economics.money.Currency;
import org.jscience.physics.amount.Amount;

import com.vaadin.data.util.converter.Converter;

@SuppressWarnings("rawtypes")
public class AmountConverter implements Converter<String, Amount> {

	private static final long serialVersionUID = -3485544610168611605L;

	@Override
	public Amount convertToModel(String value,
			Class<? extends Amount> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		return Amount.valueOf(
				Double.parseDouble(value.replaceAll("[^0-9]", "")),
				Currency.valueOf(value.replaceAll("[0-9]", "")));
	}

	@Override
	public String convertToPresentation(Amount value,
			Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		return value.toString();
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
