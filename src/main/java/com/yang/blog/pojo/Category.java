package com.yang.blog.pojo;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="tb_categories")
public class Category implements Serializable,Cloneable{
    @Id
    private String id;
    @Column(name ="`name`")
    private String name;
    @Column(name = "`pinyin`")
    private String pinyin;
    @Column(name = "`description`")
    private String description;
    @Column(name = "`order`")
    private int order=1;
    @Column(name = "`status`")
    private String status;
    @Column(name = "`create_time`")
    private Date createTime;
    @Column(name = "`update_time`")
    private Date updateTime;
}
