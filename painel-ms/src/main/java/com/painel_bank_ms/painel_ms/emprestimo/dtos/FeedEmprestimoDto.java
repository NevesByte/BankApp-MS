package com.painel_bank_ms.painel_ms.emprestimo.dtos;

import java.util.List;

public record FeedEmprestimoDto(
                      List<ItemEmprestimoDto> feedItems,
                      int page,
                      int pageSize,
                      int totalPages,
                      int totalElements) {
                        
}
