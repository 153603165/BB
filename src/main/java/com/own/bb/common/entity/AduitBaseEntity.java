package com.own.bb.common.entity;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

import lombok.Setter;

/**
 * 统一定义审核的entity基类. <br>
 */
@MappedSuperclass
public class AduitBaseEntity extends BaseEntity implements Serializable, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Setter
	protected Integer aduitStatus;

	public Integer getAduitStatus() {
		return aduitStatus;
	}

}
