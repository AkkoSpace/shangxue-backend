package space.akko.springbootinit.model.dto.transaction;

import lombok.Data;
import lombok.EqualsAndHashCode;
import space.akko.springbootinit.common.PageRequest;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TransactionQueryRequest extends PageRequest implements Serializable {

    /**
     * 自增主键
     */
    private Integer id;

    /**
     * 交易日期
     */
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
     * 交易状态
     */
    private Integer status;

}
