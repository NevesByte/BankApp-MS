package com.painel_bank_ms.painel_ms.transferencia.dto;

import java.util.List;

public record FeedTransferenciaDto(
    List<ItemTransferenciaDto> feedItems,
    int page,
    int pageSize,
    int totalPages,
    int totalElements) {
}
