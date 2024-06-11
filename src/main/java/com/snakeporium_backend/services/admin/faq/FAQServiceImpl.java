package com.snakeporium_backend.services.admin.faq;


import com.snakeporium_backend.dto.FAQDto;
import com.snakeporium_backend.entity.FAQ;
import com.snakeporium_backend.entity.Product;
import com.snakeporium_backend.repository.FAQRepository;
import com.snakeporium_backend.repository.ProductRepository;
import com.snakeporium_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FAQServiceImpl implements FAQService {

    private final FAQRepository faqRepository;

    private final ProductRepository productRepository;

    public FAQDto postFAQ(Long productId, FAQDto faqDto) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            FAQ faq = new FAQ();

            faq.setQuestion(faqDto.getQuestion());
            faq.setAnswer(faqDto.getAnswer());
            faq.setProduct(product.get());

            return faqRepository.save(faq).getFAQDto();
        }
        return null;
    }
}
