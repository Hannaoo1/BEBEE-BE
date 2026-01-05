package com.lgcns.bebee.member.domain.repository;

import com.lgcns.bebee.member.domain.entity.HelpCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("local")
class HelpCategoryRepositoryTest {

    @Autowired
    private HelpCategoryRepository helpCategoryRepository;

    @Test
    void findByHelpType_ShouldFindSeededData() {
        String target = "방문 목욕";
        Optional<HelpCategory> result = helpCategoryRepository.findByHelpType(target);

        if (result.isPresent()) {
            System.out.println("Found: " + result.get().getHelpType());
        } else {
            System.out.println("NOT FOUND: " + target);
            helpCategoryRepository.findAll().forEach(cat -> {
                System.out.println("Existing: [" + cat.getHelpType() + "] Length: " + cat.getHelpType().length());
            });
        }

        assertThat(result).isPresent();
    }
}
