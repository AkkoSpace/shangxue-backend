package space.akko.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易
 *
 * @author titan
 * @TableName transaction
 */
@TableName(value = "transaction")
@Data
public class Transaction implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 交易日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date transactionDate;

    /**
     * 交易单号
     */
    private String transactionId;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 交易状态：0-未付款，1-已付款，2-已取消，3-已退款
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;
}
