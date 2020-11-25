package com.lengedyun.contentcenter.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "mid_user_share")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MidUserShare {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * share.id
     */
    @Column(name = "share_id")
    private Integer shareId;

    /**
     * user.id
     */
    @Column(name = "user_id")
    private Integer userId;

}