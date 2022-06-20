package com.ipiecoles.java.java350.repository;

import com.ipiecoles.java.java350.model.Employe;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;

@SpringBootTest
public class EmployeRepositoryTest {

    @Autowired
    EmployeRepository employeRepository;

    //fonction executé avant et apres chaque test
    @BeforeEach
    @AfterEach
    public void purge(){
        employeRepository.deleteAll();
    }

    @Test
    public void testFindLastMatriculeWithoutEmploye(){
        //Given
        //When
        String matricule = employeRepository.findLastMatricule();
        //Then

        //Car y'a pas de BDD
        Assertions.assertThat(matricule).isNull();

    }

    @Test
    public void testFindLastMatriculeWith1Employe(){
        //Given
        Employe employe = new Employe("Smith","Marc","M1501", LocalDate.now(),2000d,2,12d);
        employeRepository.save(employe);

        //When
        String matricule = employeRepository.findLastMatricule();
        employe.setMatricule(matricule);
        //Then

        //Car y'a pas de BDD
        Assertions.assertThat(matricule).isEqualTo("1501");
    }
    @Test
    public void testFindLastMatriculeWith3Employe(){
        //Given
        Employe employe1 = new Employe("Smith","John","L1522", LocalDate.now(),2000d,2,12d);
        Employe employe2 = new Employe("Smith","Carl","M1543", LocalDate.now(),2000d,2,12d);
        Employe employe3 = new Employe("Smith","Marc","Y1964", LocalDate.now(),2000d,2,12d);
        employeRepository.saveAll(Arrays.asList(employe1,employe2,employe3));
        //When
        String matricule = employeRepository.findLastMatricule();
        //Then

        //Car y'a pas de BDD
        Assertions.assertThat(matricule).isEqualTo("1964");
    }
}
