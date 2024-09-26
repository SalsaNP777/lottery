package com.salsa.lottery.dto.request.transaction;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TransactionSearchRequest {
    private String user;
    private String lottery;
}
