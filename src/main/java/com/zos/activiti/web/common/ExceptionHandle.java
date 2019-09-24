package com.zos.activiti.web.common;

import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.zos.activiti.dto.common.ExecuteResultDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ExceptionHandle {
	
	@ResponseBody
	@ExceptionHandler(value = TransactionSystemException.class)
	public ExecuteResultDTO handle(TransactionSystemException e) {
		ExecuteResultDTO result = new ExecuteResultDTO();
		result.setCode(0);
		Throwable throwable = e.getApplicationException() == null ? e.getCause() : e.getApplicationException();
		if (throwable == null) {
			result.setMsg(e.getMessage());
			log.error(e.getMessage());
		} else {
			result.setMsg(throwable.getCause() == null || throwable.getCause().getMessage() == null  ? throwable.getMessage() : throwable.getCause().getMessage());
			log.error(throwable.getCause() == null || throwable.getCause().getMessage() == null  ? throwable.getMessage() : throwable.getCause().getMessage());
		}
		return result;
	}
}
