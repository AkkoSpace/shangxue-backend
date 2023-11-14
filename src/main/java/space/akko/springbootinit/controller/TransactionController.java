package space.akko.springbootinit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import space.akko.springbootinit.common.BaseResponse;
import space.akko.springbootinit.common.DeleteRequest;
import space.akko.springbootinit.common.ErrorCode;
import space.akko.springbootinit.common.ResultUtils;
import space.akko.springbootinit.constant.CommonConstant;
import space.akko.springbootinit.exception.BusinessException;
import space.akko.springbootinit.exception.ThrowUtils;
import space.akko.springbootinit.model.dto.transaction.TransactionAddRequest;
import space.akko.springbootinit.model.dto.transaction.TransactionQueryRequest;
import space.akko.springbootinit.model.dto.transaction.TransactionUpdateRequest;
import space.akko.springbootinit.model.entity.Transaction;
import space.akko.springbootinit.model.entity.User;
import space.akko.springbootinit.service.TransactionService;
import space.akko.springbootinit.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 交易接口
 */
@RestController
@RequestMapping("/transaction")
@Slf4j
public class TransactionController {

    @Resource
    private TransactionService transactionService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建
     *
     * @param transactionAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addTransaction(@RequestBody TransactionAddRequest transactionAddRequest, HttpServletRequest request) {
        if (transactionAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Transaction transaction = new Transaction();
        BeanUtils.copyProperties(transactionAddRequest, transaction);
        transactionService.validTransaction(transaction, true);
        User loginUser = userService.getLoginUser(request);
        transaction.setUserId(loginUser.getId());
        boolean result = transactionService.save(transaction);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newTransactionId = transaction.getId();
        return ResultUtils.success(newTransactionId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTransaction(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Transaction oldTransaction = transactionService.getById(id);
        ThrowUtils.throwIf(oldTransaction == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldTransaction.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = transactionService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param transactionUpdateRequest
     * @return
     */
    @PostMapping("/update")
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateTransaction(@RequestBody TransactionUpdateRequest transactionUpdateRequest) {
        if (transactionUpdateRequest == null || transactionUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Transaction transaction = new Transaction();
        BeanUtils.copyProperties(transactionUpdateRequest, transaction);
        // 参数校验
        transactionService.validTransaction(transaction, false);
        long id = transactionUpdateRequest.getId();
        // 判断是否存在
        Transaction oldTransaction = transactionService.getById(id);
        ThrowUtils.throwIf(oldTransaction == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = transactionService.updateById(transaction);
        return ResultUtils.success(result);
    }

    /**
     * 分页获取列表
     *
     * @param transactionQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<Transaction>> listTransactionByPage(TransactionQueryRequest transactionQueryRequest, HttpServletRequest request) {
        if (transactionQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Transaction transactionQuery = new Transaction();
        BeanUtils.copyProperties(transactionQueryRequest, transactionQuery);
        long current = transactionQueryRequest.getCurrent();
        long size = transactionQueryRequest.getPageSize();
        String sortField = transactionQueryRequest.getSortField();
        String sortOrder = transactionQueryRequest.getSortOrder();
        String description = transactionQuery.getDescription();
        String transactionId = transactionQuery.getTransactionId();
        // description, transactionId 支持模糊搜索
        transactionQuery.setDescription(null);
        transactionQuery.setTransactionId(null);
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<Transaction> queryWrapper = new QueryWrapper<>(transactionQuery);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.like(StringUtils.isNotBlank(transactionId), "transactionId", transactionId);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<Transaction> transactionPage = transactionService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(transactionPage);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<Transaction> getTransactionById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Transaction transaction = transactionService.getById(id);
        if (transaction == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(transaction);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param transactionQueryRequest
     * @param request
     * @return
     */
//    @PostMapping("/list/page/vo")
//    public BaseResponse<Page<TransactionVO>> listTransactionVOByPage(@RequestBody TransactionQueryRequest transactionQueryRequest,
//            HttpServletRequest request) {
//        long current = transactionQueryRequest.getCurrent();
//        long size = transactionQueryRequest.getPageSize();
//        // 限制爬虫
//        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
//        Page<Transaction> transactionPage = transactionService.page(new Page<>(current, size),
//                transactionService.getQueryWrapper(transactionQueryRequest));
//        return ResultUtils.success(transactionService.getTransactionVOPage(transactionPage, request));
//    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param transactionQueryRequest
     * @param request
     * @return
     */
//    @PostMapping("/my/list/page/vo")
//    public BaseResponse<Page<TransactionVO>> listMyTransactionVOByPage(@RequestBody TransactionQueryRequest transactionQueryRequest,
//            HttpServletRequest request) {
//        if (transactionQueryRequest == null) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        User loginUser = userService.getLoginUser(request);
//        transactionQueryRequest.setUserId(loginUser.getId());
//        long current = transactionQueryRequest.getCurrent();
//        long size = transactionQueryRequest.getPageSize();
//        // 限制爬虫
//        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
//        Page<Transaction> transactionPage = transactionService.page(new Page<>(current, size),
//                transactionService.getQueryWrapper(transactionQueryRequest));
//        return ResultUtils.success(transactionService.getTransactionVOPage(transactionPage, request));
//    }

    // endregion

    /**
     * 分页搜索（从 ES 查询，封装类）
     *
     * @param transactionQueryRequest
     * @param request
     * @return
     */
//    @PostMapping("/search/page/vo")
//    public BaseResponse<Page<TransactionVO>> searchTransactionVOByPage(@RequestBody TransactionQueryRequest transactionQueryRequest,
//            HttpServletRequest request) {
//        long size = transactionQueryRequest.getPageSize();
//        // 限制爬虫
//        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
//        Page<Transaction> transactionPage = transactionService.searchFromEs(transactionQueryRequest);
//        return ResultUtils.success(transactionService.getTransactionVOPage(transactionPage, request));
//    }

    /**
     * 编辑（用户）
     *
     * @param transactionEditRequest
     * @param request
     * @return
     */
//    @PostMapping("/edit")
//    public BaseResponse<Boolean> editTransaction(@RequestBody TransactionEditRequest transactionEditRequest, HttpServletRequest request) {
//        if (transactionEditRequest == null || transactionEditRequest.getId() <= 0) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        Transaction transaction = new Transaction();
//        BeanUtils.copyProperties(transactionEditRequest, transaction);
//        List<String> tags = transactionEditRequest.getTags();
//        if (tags != null) {
//            transaction.setTags(GSON.toJson(tags));
//        }
//        // 参数校验
//        transactionService.validTransaction(transaction, false);
//        User loginUser = userService.getLoginUser(request);
//        long id = transactionEditRequest.getId();
//        // 判断是否存在
//        Transaction oldTransaction = transactionService.getById(id);
//        ThrowUtils.throwIf(oldTransaction == null, ErrorCode.NOT_FOUND_ERROR);
//        // 仅本人或管理员可编辑
//        if (!oldTransaction.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
//            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
//        }
//        boolean result = transactionService.updateById(transaction);
//        return ResultUtils.success(result);
//    }

}
