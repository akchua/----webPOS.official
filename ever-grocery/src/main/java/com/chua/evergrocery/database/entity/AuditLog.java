package com.chua.evergrocery.database.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.chua.evergrocery.database.entity.base.BaseObject;
import com.chua.evergrocery.enums.AuditLogType;
import com.chua.evergrocery.serializer.json.UserSerializer;
import com.chua.evergrocery.utility.DateUtil;
import com.chua.evergrocery.utility.format.CurrencyFormatter;
import com.chua.evergrocery.utility.format.DateFormatter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 10, 2019
 */
@Entity(name = "AuditLog")
@Table(name = AuditLog.TABLE_NAME)
public class AuditLog extends BaseObject {

	private static final long serialVersionUID = -6870765576270550986L;
	
	public static final String TABLE_NAME = "audit_log";
	
	@JsonSerialize(using = UserSerializer.class)
	private User subjectOfAudit;
	
	private Date auditStart;
	
	private Date auditEnd;
	
	private Float amount;
	
	private AuditLogType auditLogType;
	
	private Integer transactionCount;
	
	private Float withheldCash;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "subject_id")
	@Where(clause = "valid = 1")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getSubjectOfAudit() {
		return subjectOfAudit;
	}

	public void setSubjectOfAudit(User subjectOfAudit) {
		this.subjectOfAudit = subjectOfAudit;
	}

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "audit_start")
	public Date getAuditStart() {
		return auditStart;
	}
	
	@Transient
	public String getAuditPeriod() {
		if(DateUtil.isSameDay(auditStart, auditEnd)) {
			return DateFormatter.longFormat(auditStart) + " - " + DateFormatter.timeOnlyFormat(auditEnd);
		} else {
			return DateFormatter.longFormat(auditStart) + " - " + DateFormatter.longFormat(auditEnd);
		}
	}

	public void setAuditStart(Date auditStart) {
		this.auditStart = auditStart;
	}

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "audit_end")
	public Date getAuditEnd() {
		return auditEnd;
	}

	public void setAuditEnd(Date auditEnd) {
		this.auditEnd = auditEnd;
	}

	@Basic
	@Column(name = "amount")
	public Float getAmount() {
		return amount;
	}
	
	@Transient
	public String getFormattedAmount() {
		return CurrencyFormatter.pesoFormat(amount);
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "log_type", length = 50)
	public AuditLogType getAuditLogType() {
		return auditLogType;
	}

	public void setAuditLogType(AuditLogType auditLogType) {
		this.auditLogType = auditLogType;
	}

	@Basic
	@Column(name = "transaction_count")
	public Integer getTransactionCount() {
		return transactionCount;
	}

	public void setTransactionCount(Integer transactionCount) {
		this.transactionCount = transactionCount;
	}

	@Basic
	@Column(name = "withheld_cash")
	public Float getWithheldCash() {
		return withheldCash;
	}
	
	@Transient
	public String getFormattedWithheldCash() {
		return CurrencyFormatter.pesoFormat(withheldCash);
	}

	public void setWithheldCash(Float withheldCash) {
		this.withheldCash = withheldCash;
	}
}
