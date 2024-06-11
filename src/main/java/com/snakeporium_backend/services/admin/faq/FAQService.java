package com.snakeporium_backend.services.admin.faq;

import com.snakeporium_backend.dto.FAQDto;

public interface FAQService {



    FAQDto postFAQ(Long productId, FAQDto faqDto);
}
