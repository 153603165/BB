package com.own.bb.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;

import com.own.bb.common.entity.AduitBaseEntity;
import com.own.bb.security.AuditableListener;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
public class User extends AduitBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	@Excel(name = "姓名", orderNum = "0")
	private String username;

	@Column(nullable = false)
	@Excel(name = "密码", orderNum = "1")
	private String password;

	@Excel(name = "性别", replace = { "男_1", "女_2" }, orderNum = "2")
	private String sex;

	@Excel(name = "生日", exportFormat = "yyyy-MM-dd", orderNum = "3")
	private Date birthday;

	@Excel(name = "描述", orderNum = "4")
	private String description;
}
