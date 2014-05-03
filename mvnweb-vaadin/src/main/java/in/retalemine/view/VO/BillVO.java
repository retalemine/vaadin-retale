package in.retalemine.view.VO;

import in.retalemine.util.Rupee;

import java.util.Date;

import javax.measure.quantity.Quantity;

import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;

import com.vaadin.data.util.BeanItemContainer;

public class BillVO {

	private Integer billNo;
	private Date billDate;
	private BeanItemContainer<BillItemVO<? extends Quantity, ? extends Quantity>> billItems;
	private Amount<Money> subTotal;
	private String taxType;
	private Double taxPercent;
	private Amount<Money> total;
	private String payMode;
	private Boolean isDelayedPay;
	private Boolean isPaid;
	private Date paidDate;
	private String customerName;
	private Integer contactNo;
	private String address;
	private Boolean isDoorDelivery;

	public BillVO() {
		this.billNo = 1;
		this.billDate = new Date();
		this.billItems = new BeanItemContainer<BillItemVO<? extends Quantity, ? extends Quantity>>(
				BillItemVO.class);
		this.subTotal = Amount.valueOf(0.0, Rupee.INR);
		this.taxPercent = 0.0;
		this.total = Amount.valueOf(0.0, Rupee.INR);
	}

}
