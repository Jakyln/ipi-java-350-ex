package com.ipiecoles.java.java350.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmployeRepositoryTest {

    @Autowired
    EmployeRepository employeRepository;

    @Test
    public void testFindMatricule(){
        //Given
        //When
        String matricule = employeRepository.findLastMatricule();
        //Then

        //Car y'a pas de BDD
        Assertions.assertThat(matricule).isNull();

    }
}
