package com.own.bb.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Setter;

/**
 * 统一定义entity基类. <br>
 * 基类统一定义id的属性名称、数据类型、列名映射及生成策略. <br>
 */
@MappedSuperclass
public class BaseEntity implements Serializable, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String TIMEZONE = "GMT+08:00";
	/**
	 * 
	 */
	@Id
	@Setter
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	/**
	 * 操作版本(乐观锁,用于并发控制)
	 */
	@Setter
	protected Integer version;

	/**
	 * 记录创建者用户登录名
	 */
	@Setter
	protected String createUser;
	/**
	 * 记录创建时间
	 */
	@Setter
	protected Date createTime;

	/**
	 * 记录更新用户 用户登录名
	 */
	@Setter
	protected String updateUser;
	/**
	 * 记录更新时间
	 */
	@Setter
	protected Date updateTime;
	/**
	 * 状态可用 0：可用 1：已删除
	 */
	@Setter
	protected Integer isDelete;

	public BaseEntity() {
		super();
	}

	/**
	 * 版本号(乐观锁)
	 */
	@Version
	public Integer getVersion() {
		return version;
	}

	/**
	 * 记录创建者 用户登录名
	 */
	public String getCreateUser() {
		return createUser;
	}

	/**
	 * 记录创建时间.
	 */
	// 设定JSON序列化时的日期格式
	@JsonFormat(pattern = DATE_TIME_FORMAT, timezone = TIMEZONE)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 记录更新用户 用户登录名
	 */
	public String getUpdateUser() {
		return updateUser;
	}

	/**
	 * 记录更新时间
	 */
	// 设定JSON序列化时的日期格式
	@JsonFormat(pattern = DATE_TIME_FORMAT, timezone = TIMEZONE)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getUpdateTime() {
		return updateTime;
	}

	public Long getId() {
		return id;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public BaseEntity clone() {
		BaseEntity o = null;
		try {
			o = (BaseEntity) super.clone();// Object中的clone()识别出你要复制的是哪一个对象。
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
