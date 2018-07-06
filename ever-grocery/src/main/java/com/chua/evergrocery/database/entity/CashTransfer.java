package com.chua.evergrocery.database.entity;import java.util.Calendar;import java.util.Date;import javax.persistence.Basic;import javax.persistence.Column;import javax.persistence.Entity;import javax.persistence.EnumType;import javax.persistence.Enumerated;import javax.persistence.FetchType;import javax.persistence.JoinColumn;import javax.persistence.ManyToOne;import javax.persistence.Table;import javax.persistence.Temporal;import javax.persistence.TemporalType;import javax.persistence.Transient;import org.hibernate.annotations.NotFound;import org.hibernate.annotations.NotFoundAction;import org.hibernate.annotations.Where;import com.chua.evergrocery.UserContextHolder;import com.chua.evergrocery.database.entity.base.BaseObject;import com.chua.evergrocery.enums.Status;import com.chua.evergrocery.serializer.json.UserSerializer;import com.chua.evergrocery.utility.DateUtil;import com.chua.evergrocery.utility.format.CurrencyFormatter;import com.chua.evergrocery.utility.format.DateFormatter;import com.fasterxml.jackson.databind.annotation.JsonSerialize;/** * @author  Adrian Jasper K. Chua * @version 1.0 * @since   30 June 2018 */@Entity(name = "CashTransfer")@Table(name = CashTransfer.TABLE_NAME)public class CashTransfer extends BaseObject {	private static final long serialVersionUID = 3605585176715719872L;		public static final String TABLE_NAME = "cash_transfer";		@JsonSerialize(using = UserSerializer.class)	private User cashFrom;		@JsonSerialize(using = UserSerializer.class)	private User cashTo;		private Float amount;		private Date transferredOn;		private Status status;	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)	@JoinColumn(name = "cash_from_id")	@Where(clause = "valid = 1")	@NotFound(action = NotFoundAction.IGNORE)	public User getCashFrom() {		return cashFrom;	}	public void setCashFrom(User cashFrom) {		this.cashFrom = cashFrom;	}	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)	@JoinColumn(name = "cash_to_id")	@Where(clause = "valid = 1")	@NotFound(action = NotFoundAction.IGNORE)	public User getCashTo() {		return cashTo;	}		@Transient	public Boolean isTransferToMe() {		return cashTo.getId().equals(UserContextHolder.getUser().getId());	}	public void setCashTo(User cashTo) {		this.cashTo = cashTo;	}	@Basic	@Column(name = "amount")	public Float getAmount() {		return amount;	}		@Transient	public String getFormattedAmount() {		return CurrencyFormatter.pesoFormat(getAmount());	}	public void setAmount(Float amount) {		this.amount = amount;	}	@Temporal(value = TemporalType.TIMESTAMP)	@Column(name = "transferred_on")	public Date getTransferredOn() {		return transferredOn;	}		@Transient	public String getFormattedTransferredOn() {		final String formattedTransferredOn;		Calendar cal = Calendar.getInstance();		cal.setTime(transferredOn);				if(cal.getTimeInMillis() == DateUtil.getDefaultDateInMillis()) {			formattedTransferredOn = "n/a";		} else {			formattedTransferredOn = DateFormatter.longFormat(transferredOn);		}		return formattedTransferredOn;	}	public void setTransferredOn(Date transferedOn) {		this.transferredOn = transferedOn;	}	@Enumerated(EnumType.STRING)	@Column(name = "status", length = 50)	public Status getStatus() {		return status;	}	public void setStatus(Status status) {		this.status = status;	}}
