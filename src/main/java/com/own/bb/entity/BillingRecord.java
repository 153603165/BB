package com.own.bb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;

import com.own.bb.common.entity.BaseEntity;
import com.own.bb.security.AuditableListener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EntityListeners(AuditableListener.class)
public class BillingRecord extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private String companyName;

	@Column(nullable = false)
	private String regName;

	private String individualName;

	private String creditCode;

	private Double taxAmount;
	
	private Double noTaxAmount;
	
	private Double tax;
}
