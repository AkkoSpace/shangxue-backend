package space.akko.springbootinit.model.vo;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import space.akko.springbootinit.model.entity.Transaction;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易视图
 *
 * @author titan
 */
@Data
public class TransactionVO implements Serializable {


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
     * 包装类转对象
     *
     * @param transactionVO
     * @return
     */
    public static Transaction wrapper(TransactionVO transactionVO) {
        if (transactionVO == null) {
            return null;
        }
        Transaction transaction = new Transaction();
        BeanUtils.copyProperties(transactionVO, transaction);
        return transaction;
    }
}
