package com.zos.activiti.delegate;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

@Slf4j
public class CompensationJobDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        log.info("补偿事件已经触发");
    }
}
