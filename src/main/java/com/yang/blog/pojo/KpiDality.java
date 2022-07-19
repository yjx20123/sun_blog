package com.yang.blog.pojo;



import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;

@Entity
@Table(name="tb_kpi_daily")
public class KpiDality implements Serializable,Cloneable{
    /** ID */
    @Id
    private String id ;
    /** 浏览量 */
    private Integer viewCount ;
    /** 更新时间 */
    private Date updateTime ;
    /** 用户ID */
    private String userId ;

    /** ID */
    public String getId(){
        return this.id;
    }
    /** ID */
    public void setId(String id){
        this.id = id;
    }
    /** 浏览量 */
    public Integer getViewCount(){
        return this.viewCount;
    }
    /** 浏览量 */
    public void setViewCount(Integer viewCount){
        this.viewCount = viewCount;
    }
    /** 更新时间 */
    public Date getUpdateTime(){
        return this.updateTime;
    }
    /** 更新时间 */
    public void setUpdateTime(Date updateTime){
        this.updateTime = updateTime;
    }
    /** 用户ID */
    public String getUserId(){
        return this.userId;
    }
    /** 用户ID */
    public void setUserId(String userId){
        this.userId = userId;
    }
}
