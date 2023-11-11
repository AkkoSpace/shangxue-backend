package space.akko.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import space.akko.springbootinit.model.entity.Post;
import space.akko.springbootinit.model.entity.Transaction;

/**
* @author titan
* @description 针对表【transaction(交易)】的数据库操作Service
* @createDate 2023-05-21 16:32:16
*/
public interface TransactionService extends IService<Transaction> {

    /**
     * 校验
     *
     * @param transaction 交易
     * @param add 新增
     */
    void validTransaction(Transaction transaction, boolean add);

}
