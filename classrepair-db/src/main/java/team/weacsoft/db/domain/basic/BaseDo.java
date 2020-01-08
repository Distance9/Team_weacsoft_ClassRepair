package team.weacsoft.db.domain.basic;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import team.weacsoft.db.domain.converter.DateConverter;
import team.weacsoft.db.domain.converter.StateConverter;

import javax.persistence.*;

//todo  统计分析表
//todo  故障设备类型表 存图标 默认为""

/**
 * 每个表都有的字段
 * @author GreenHatHG
 **/

@Data
@Entity
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseDo {

    @Id
    @ExcelIgnore
    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    /**
     * 一般1为启用，0为停用，-1为删除,特殊情况除外
     */
    @ExcelProperty(value = "状态", converter = StateConverter.class)
    @Column(nullable = false)
    private int state = 1;

    /**
     * 创建时间
     */
    @JsonIgnore
    @ExcelProperty(value = "创建时间", converter = DateConverter.class)
    @Column(nullable = false)
    private long createTime;

    /**
     * 更新时间
     */
    @JsonIgnore
    @ExcelProperty(value = "更新时间", converter = DateConverter.class)
    @Column(nullable = false)
    private long updateTime;

    /**
     * 删除时间
     */
    @JsonIgnore
    @ExcelProperty(value = "删除时间", converter = DateConverter.class)
    @Column(nullable = false)
    private long deleteTime;

    @PrePersist
    protected void onCreate() {
        createTime = System.currentTimeMillis();
        updateTime = System.currentTimeMillis();
        deleteTime = (long)0;
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = System.currentTimeMillis();
    }
}