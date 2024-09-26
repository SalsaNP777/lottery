package com.salsa.lottery.service;

import com.salsa.lottery.dto.request.transaction.TransactionRequest;
import com.salsa.lottery.dto.request.transaction.TransactionSearchRequest;
import com.salsa.lottery.dto.response.ControllerResponse;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
    ControllerResponse<?> createNewTransaction(TransactionRequest request);
    ControllerResponse<?> getAllTransactionWithPage(Pageable pageable, TransactionSearchRequest request);
    ControllerResponse<?> winnerUser(TransactionRequest request);
}
