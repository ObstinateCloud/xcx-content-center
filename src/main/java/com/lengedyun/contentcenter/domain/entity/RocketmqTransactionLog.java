package com.lengedyun.contentcenter.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "rocketmq_transaction_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RocketmqTransactionLog {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 事务id
     */
    @Column(name = "transaction_id")
    private String transactionId;

    /**
     * 日志
     */
    @Column(name = "log_content")
    private String logContent;


}