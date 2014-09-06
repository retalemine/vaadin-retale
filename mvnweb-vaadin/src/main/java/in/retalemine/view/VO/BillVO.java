package in.retalemine.view.VO;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.measure.quantity.Quantity;

import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;

public class BillVO {

	// private Integer billNo;
	private Date billDate;
	private List<BillItemVO<? extends Quantity, ? extends Quantity>> billItems;
	private Amount<Money> subTotal;
	private List<TaxVO> taxes;
	private Amount<Money> total;
	private CustomerVO customerInfo;
	private Boolean isDoorDelivery;
	private PaymentMode payMode;
	private Boolean isDelayedPay;
	private Boolean isPaid;
	private Date paidDate;

	public BillVO() {
		taxes = new LinkedList<TaxVO>();
		payMode = PaymentMode.CASH;
		isDelayedPay = false;
		isDoorDelivery = false;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public List<BillItemVO<? extends Quantity, ? extends Quantity>> getBillItems() {
		return billItems;
	}

	public void setBillItems(
			List<BillItemVO<? extends Quantity, ? extends Quantity>> billItems) {
		this.billItems = billItems;
	}

	public Amount<Money> getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(Amount<Money> subTotal) {
		this.subTotal = subTotal;
	}

	public List<TaxVO> getTaxes() {
		return taxes;
	}

	public void setTaxes(List<TaxVO> taxes) {
		this.taxes = taxes;
	}

	public Amount<Money> getTotal() {
		return total;
	}

	public void setTotal(Amount<Money> total) {
		this.total = total;
	}

	public CustomerVO getCustomerInfo() {
		return customerInfo;
	}

	public void setCustomerInfo(CustomerVO customerInfo) {
		this.customerInfo = customerInfo;
	}

	public Boolean getIsDoorDelivery() {
		return isDoorDelivery;
	}

	public void setIsDoorDelivery(Boolean isDoorDelivery) {
		this.isDoorDelivery = isDoorDelivery;
	}

	public PaymentMode getPayMode() {
		return payMode;
	}

	public void setPayMode(PaymentMode payMode) {
		this.payMode = payMode;
	}

	public Boolean getIsDelayedPay() {
		return isDelayedPay;
	}

	public void setIsDelayedPay(Boolean isDelayedPay) {
		this.isDelayedPay = isDelayedPay;
	}

	public Boolean getIsPaid() {
		return isPaid;
	}

	public void setIsPaid(Boolean isPaid) {
		this.isPaid = isPaid;
	}

	public Date getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(Date paidDate) {
		this.paidDate = paidDate;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("<BillVO><billDate>");
		builder.append(billDate);
		builder.append("</billDate><billItems>");
		builder.append(billItems);
		builder.append("</billItems><subTotal>");
		builder.append(subTotal);
		builder.append("</subTotal><taxes>");
		builder.append(taxes);
		builder.append("</taxes><total>");
		builder.append(total);
		builder.append("</total><customerInfo>");
		builder.append(customerInfo);
		builder.append("</customerInfo><isDoorDelivery>");
		builder.append(isDoorDelivery);
		builder.append("</isDoorDelivery><payMode>");
		builder.append(payMode);
		builder.append("</payMode><isDelayedPay>");
		builder.append(isDelayedPay);
		builder.append("</isDelayedPay><isPaid>");
		builder.append(isPaid);
		builder.append("</isPaid><paidDate>");
		builder.append(paidDate);
		builder.append("</paidDate></BillVO>");
		return builder.toString();
	}

}
