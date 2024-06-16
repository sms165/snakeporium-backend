package com.snakeporium_backend.controller.admin;

import com.snakeporium_backend.dto.CategoryDto;
import com.snakeporium_backend.entity.Category;
import com.snakeporium_backend.entity.Sex;
import com.snakeporium_backend.services.admin.sex.SexService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminSexController {

    private final SexService sexService;




    @GetMapping("/sex")
    public ResponseEntity<List<Sex>> getAllSexes() {
        return ResponseEntity.ok(sexService.getAllSexes());
    }
}
