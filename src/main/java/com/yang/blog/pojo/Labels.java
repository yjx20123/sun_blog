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
@Table(name = "tb_labels")
public class Labels implements Serializable,Cloneable {
    @Id
    private String id;
    @Column(name = "`name`")
    private String name;
    @Column(name = "`count`")
    private int count;
    @Column(name ="create_time")
    private Date createTime;
    @Column(name = "update_time")
    private Date updateTime;
}
