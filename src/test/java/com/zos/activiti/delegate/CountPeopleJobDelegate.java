package com.zos.activiti.delegate;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

@Slf4j
public class CountPeopleJobDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        log.info("正在查人数 {}", execution.getId());
        if (execution.getProcessDefinitionId().startsWith("errorStartPrimary")) {
            throw new BpmnError("errorCode");
        }
    }
}
