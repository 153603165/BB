package com.own.bb.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.own.bb.entity.BillingRecord;

/**
 * 用户管理
 */
public interface BillingRecordRepository
		extends JpaRepository<BillingRecord, Long>, JpaSpecificationExecutor<BillingRecord> {
	@Modifying
	@Query(value = " update BillingRecord set isDelete=1 where id in (:ids)")
	void deleteActivity(@Param("ids") Long[] ids);
}
