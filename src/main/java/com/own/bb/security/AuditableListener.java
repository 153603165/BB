package com.own.bb.security;

import java.util.Date;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.own.bb.common.LoginParam;
import com.own.bb.common.entity.BaseEntity;

import lombok.extern.java.Log;

/**
 *
 * 审计监听.
 * <p>
 * 在实体保存时添加审计信息，比如创建人、创建时间、最后修改人，最后修改时间等
 *
 */
@SuppressWarnings({ "unused" })
@Log
public class AuditableListener {

	@PrePersist
	@PreUpdate
	@PostUpdate
	@PostPersist
	public void prePersist(Object object) {
		setAuditableInfo(object);
	}

	private void setAuditableInfo(Object object) {

		if (object == null)
			return;

		if (!(object instanceof BaseEntity))
			return;

		BaseEntity entity = (BaseEntity) object;
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String loginName = (String) request.getSession().getAttribute(LoginParam.LOGIN_NAME);
		if (entity.getCreateTime() == null && entity.getCreateUser() == null) {
			entity.setCreateUser(loginName);
			entity.setCreateTime(new Date());
		}
		entity.setUpdateUser(loginName);
		entity.setUpdateTime(new Date());

	}
}