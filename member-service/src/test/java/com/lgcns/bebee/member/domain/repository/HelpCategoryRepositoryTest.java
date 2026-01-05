package com.lgcns.bebee.member.domain.repository;

import com.lgcns.bebee.member.domain.entity.HelpCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.lgcns.bebee.member.domain.entity.Member;
import com.lgcns.bebee.member.domain.entity.MemberHelpCategory;
import com.lgcns.bebee.member.domain.repository.MemberRepository;
import com.lgcns.bebee.member.domain.repository.MemberHelpCategoryRepository;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("local")
class HelpCategoryRepositoryTest {

    @Autowired
    private HelpCategoryRepository helpCategoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberHelpCategoryRepository memberHelpCategoryRepository;

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

    @Test
    void findByMember_Id_ShouldReturnCategories() {
        // Arrange
        Member member = Member.create(
                "test_unique@test.com", "password", "testName", "testNickUnique",
                LocalDate.of(2000, 1, 1), "MALE", "010-9999-9999",
                "HELPER", "Seoul", BigDecimal.valueOf(37.5), BigDecimal.valueOf(127.0), "11110");
        memberRepository.save(member);

        // Use existing seeded category or create one if empty (Assuming seed exists
        // from previous test context)
        List<HelpCategory> categories = helpCategoryRepository.findAll();
        if (categories.isEmpty()) {
            // Fallback if no seed
            HelpCategory newCat = HelpCategory.create("TestHelp");
            helpCategoryRepository.save(newCat);
            categories = List.of(newCat);
        }
        HelpCategory category = categories.get(0);

        MemberHelpCategory mhc = MemberHelpCategory.create(member, category);
        memberHelpCategoryRepository.save(mhc);

        // Act
        List<MemberHelpCategory> results = memberHelpCategoryRepository.findByMember_Id(member.getId());

        // Assert
        assertThat(results).isNotEmpty();
        assertThat(results.get(0).getHelpCategory().getHelpType()).isEqualTo(category.getHelpType());
    }
}
