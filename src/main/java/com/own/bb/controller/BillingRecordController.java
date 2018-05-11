package com.own.bb.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.own.bb.common.domain.BillingRecordVo;
import com.own.bb.entity.BillingRecord;
import com.own.bb.exception.impl.NormalException;
import com.own.bb.service.BillingRecordService;
import com.own.bb.utils.ExcelUtil;
import com.own.bb.utils.PageUtil;

@Controller
@RequestMapping("/billingRecord")
public class BillingRecordController extends BasicController {
	@Autowired
	BillingRecordService billingRecordService;

	@RequestMapping(value = "")
	public String description() {
		return "billingRecord/billingRecord";
	}

	@ResponseBody
	@RequestMapping(value = "/getBillingRecords")
	public ResponseEntity<String> getBillingRecords(@RequestParam(required = true) int page,
			@RequestParam(required = true) int rows, @RequestParam(required = true) String sort,
			@RequestParam(required = true) String order, @RequestParam(required = false) String regName,
			@RequestParam(required = false) String dateFrom, @RequestParam(required = false) String dateTo)
			throws UnsupportedEncodingException {
		Pageable pageable = new PageRequest(page - 1, rows, Sort.Direction.fromString(order), sort);
		Page<BillingRecord> billingRecords = billingRecordService.getBillingRecords(pageable, regName, dateFrom,
				dateTo);
		Map<String, Object> page2Map = PageUtil.page2Map(billingRecords);
		return new ResponseEntity<String>(JSONObject.toJSONString(page2Map), HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping(value = "/delBillingRecord")
	public ResponseEntity delBillingRecord(@RequestParam(required = false, value = "ids[]") @RequestBody Long[] ids) {
		billingRecordService.delBillingRecord(ids);
		return new ResponseEntity(HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping(value = "/removeByCondition")
	public ResponseEntity removeByCondition(@RequestParam(required = false) String regName,
			@RequestParam(required = false) String dateFrom, @RequestParam(required = false) String dateTo) {
		billingRecordService.removeByCondition(regName, dateFrom, dateTo);
		return new ResponseEntity(HttpStatus.OK);
	}

	/**
	 * 文件上传具体实现方法（单文件上传）
	 *
	 * @param file
	 * @return
	 * 
	 * @author 单红宇(CSDN CATOOP)
	 * @throws NormalException
	 * @create 2017年3月11日
	 */
	@RequestMapping(value = "/uploadBillingRecord", method = RequestMethod.POST)
	@ResponseBody
	public void uploadBillingRecord(@RequestParam("file") MultipartFile file, HttpServletResponse response)
			throws NormalException {
		List<BillingRecordVo> personList = ExcelUtil.importExcel(file, 1, 1, BillingRecordVo.class);
		List<BillingRecord> noDataBillingRecords = new ArrayList<>();
		for (BillingRecordVo billingRecordVo : personList) {
			BillingRecord br = new BillingRecord();
			BeanUtils.copyProperties(billingRecordVo, br);
			if (billingRecordService.getDistinctBillingRecord(br) == null) {
				noDataBillingRecords.add(br);
			}
		}
		if (noDataBillingRecords.size() > 0) {
			billingRecordService.insert(noDataBillingRecords);
		}
	}
}
