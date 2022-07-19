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
@Table(name = "tb_refresh_token")
public class RefreshToken implements Serializable,Cloneable {
    @Id
    private String id;
    @Column(name ="refreah_token" )
    private String refreahToken;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "token_key")
    private String tokenKey;
    @Column(name="create_time")
    private Date createtime;
    @Column(name = "update_time")
    private Date updatetime;
}
