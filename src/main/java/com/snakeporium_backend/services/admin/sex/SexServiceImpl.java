package com.snakeporium_backend.services.admin.sex;

import com.snakeporium_backend.dto.CategoryDto;
import com.snakeporium_backend.dto.ProductDto;
import com.snakeporium_backend.entity.Category;
import com.snakeporium_backend.entity.Product;
import com.snakeporium_backend.entity.Sex;
import com.snakeporium_backend.repository.CategoryRepository;
import com.snakeporium_backend.repository.SexRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SexServiceImpl implements SexService {

    private final SexRepository sexRepository;




    public List<Sex> getAllSexes() {
        return sexRepository.findAll();
    }
}
