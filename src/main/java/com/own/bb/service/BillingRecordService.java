package com.own.bb.service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.own.bb.dao.BillingRecordRepository;
import com.own.bb.entity.BillingRecord;

@Service
@Transactional(rollbackFor = Exception.class)
public class BillingRecordService {
	@Autowired
	BillingRecordRepository billingRecordRepository;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdfmat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static DecimalFormat df = new DecimalFormat("######0.00");

	public Page<BillingRecord> getBillingRecords(Pageable pageable, String regName, String dateFrom, String dateTo) {
		Page<BillingRecord> billingRecordPage = billingRecordRepository.findAll(getPageDate(regName, dateFrom, dateTo),
				pageable);
		return billingRecordPage;
	}

	private Specification<BillingRecord> getPageDate(String regName, String dateFrom, String dateTo) {
		return (root, query, cb) -> {
			List<Predicate> list = new ArrayList<Predicate>();
			if (null != regName && !"".equals(regName)) {
				list.add(cb.equal(root.get("regName").as(String.class), regName));
			}
			try {
				if (StringUtils.isNotEmpty(dateFrom)) {
					list.add(cb.greaterThanOrEqualTo(root.get("createTime"),
							sdfmat.parse(sdfmat.format(sdf.parse(dateFrom).getTime()))));
				}
				if (StringUtils.isNotEmpty(dateTo)) {
					list.add(cb.lessThanOrEqualTo(root.get("createTime"),
							sdfmat.parse(sdfmat.format(sdf.parse(dateTo).getTime() + 86400000))));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Predicate[] p = new Predicate[list.size()];
			return cb.and(list.toArray(p));
		};
	}

	public void removeByCondition(String regName, String dateFrom, String dateTo) {
		List<BillingRecord> findAll = billingRecordRepository.findAll(this.getPageDate(regName, dateFrom, dateTo));
		billingRecordRepository.delete(findAll);
	}

	/**
	 * 查询重复的数据
	 * 
	 * @param br
	 * @return
	 */
	public BillingRecord getDistinctBillingRecord(BillingRecord br) {
		BillingRecord billingRecord = billingRecordRepository.findOne(this.getByfindRepeat(br));
		return billingRecord;
	}

	private Specification<BillingRecord> getByfindRepeat(BillingRecord br) {
		return (root, query, cb) -> {
			Predicate predicate = cb.conjunction();

			Predicate p1 = cb.equal(root.get("createTime").as(Date.class), br.getCreateTime());
			predicate.getExpressions().add(p1);

			Predicate p2 = cb.equal(root.get("companyName").as(String.class), br.getCompanyName());
			predicate.getExpressions().add(p2);

			Predicate p3 = cb.equal(root.get("individualName").as(String.class), br.getIndividualName());
			predicate.getExpressions().add(p3);

			Predicate p4 = cb.equal(root.get("regName").as(String.class), br.getRegName());
			predicate.getExpressions().add(p4);

			Predicate p5 = cb.equal(root.get("creditCode").as(String.class), br.getCreditCode());
			predicate.getExpressions().add(p5);

			Predicate p6 = cb.equal(root.get("taxAmount").as(Double.class),
					Double.parseDouble(df.format(br.getTaxAmount())));
			predicate.getExpressions().add(p6);

			Predicate p7 = cb.equal(root.get("noTaxAmount").as(Double.class),
					Double.parseDouble(df.format(br.getNoTaxAmount())));
			predicate.getExpressions().add(p7);

			Predicate p8 = cb.equal(root.get("tax").as(Double.class), Double.parseDouble(df.format(br.getTax())));
			predicate.getExpressions().add(p8);

			return predicate;
		};
	}

	// 删除
	public void delBillingRecord(Long[] ids) {
		for (Long id : ids) {
			billingRecordRepository.delete(id);
		}
	}

	// 插入
	public void insert(List<BillingRecord> billingRecords) {
		billingRecordRepository.save(billingRecords);
	}
}
