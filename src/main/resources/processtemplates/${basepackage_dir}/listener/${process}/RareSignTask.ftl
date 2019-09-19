package ${basepackage}.listener.${process};

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import com.asiainfo.rms.workflow.listener.baseLinstener.CompleteSignTaskListener;

import lombok.extern.log4j.Log4j;

/**
 * ${processGM.processName}
 * 
 * @author 01Studio
 */
@Log4j
@Transactional(rollbackOn = Exception.class)
@Component("${processGM.signRareListener.implementation}")
public class RareSignTask extends CompleteSignTaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void extraMethod(long orderId, String objId, String objType, String methodType) throws Exception {
		if (log.isInfoEnabled()) {
			log.info("后置任务extraMethod:"+orderId+",业务ID:"+objId+",业务类型:"+objType);
		}
	}

}
