package com.lengedyun.contentcenter.rocketmq;


import com.lengedyun.contentcenter.dao.log.RocketmqTransactionLogMapper;
import com.lengedyun.contentcenter.domain.dto.ShareAuditDto;
import com.lengedyun.contentcenter.domain.entity.RocketmqTransactionLog;
import com.lengedyun.contentcenter.domain.entity.Share;
import com.lengedyun.contentcenter.service.share.ShareService;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

/**
 * @title: AddBonusTransactionListener
 * @description: 事务消息
 * @auther: 张健云
 * @date: 2020/10/18 23:02
 */

@RocketMQTransactionListener
public class AddBonusTransactionListener implements RocketMQLocalTransactionListener {

    @Autowired
    private ShareService shareService;

    @Autowired
    private RocketmqTransactionLogMapper rocketmqTransactionLogMapper;

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {

        MessageHeaders headers = msg.getHeaders();
        String transactionId = (String) headers.get(RocketMQHeaders.TRANSACTION_ID);
        Integer shareId = Integer.valueOf(headers.get("share_id").toString());

        try {
            this.shareService.auditShareByIdInDBWithLog(shareId, (ShareAuditDto) arg,transactionId);
            return RocketMQLocalTransactionState.COMMIT;
        }catch (Exception e){
            return RocketMQLocalTransactionState.ROLLBACK;

        }
    }

    /**
     * 回查本地事务状态
     * @param msg
     * @return
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        MessageHeaders headers = msg.getHeaders();
        String transactionId = (String) headers.get(RocketMQHeaders.TRANSACTION_ID);
        RocketmqTransactionLog rocketmqTransactionLog = rocketmqTransactionLogMapper.selectOne(
                RocketmqTransactionLog.builder()
                        .transactionId(transactionId)
                        .build()
        );
        if(rocketmqTransactionLog !=null){
            return RocketMQLocalTransactionState.COMMIT;
        }
        return RocketMQLocalTransactionState.ROLLBACK;
    }
}
