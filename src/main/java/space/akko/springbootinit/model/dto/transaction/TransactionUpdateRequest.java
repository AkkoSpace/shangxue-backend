package space.akko.springbootinit.model.dto.transaction;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 更新请求
 */
@Data
public class TransactionUpdateRequest implements Serializable {

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
     * 交易状态：0-未付款，1-已付款，2-已取消，3-已退款
     */
    private Integer status;

    /**
     * 描述
     */
    private String description;

}
