package com.yang.blog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_friends")
public class FriendLink implements Serializable, Cloneable {
    @Id
    private String id;
    @Column(name = "`name`")
    private String name;
    @Column(name = "`logo`")
    private String logo;
    @Column(name = "`url`")
    private String url;
    @Column(name = "`order`")
    private int order=1;
    @Column(name = "`state`")
    private String state;
    @Column(name = "`create_time`")
    private Date createTime;
    @Column(name = "`update_time`")
    private Date updateTime;

}
