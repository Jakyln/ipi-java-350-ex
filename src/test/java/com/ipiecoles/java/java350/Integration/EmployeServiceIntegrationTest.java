package com.ipiecoles.java.java350.Integration;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import com.ipiecoles.java.java350.service.EmployeService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
public class EmployeServiceIntegrationTest {

    @Autowired
    EmployeService employeService;

    @Autowired
    EmployeRepository employeRepository;

    @Test
    public void testEmbauche1Employe() throws EmployeException {
        //Given
        //Employe employe = new Employe("Smith","Marc","M1501", LocalDate.now(),2000d,2,12d);
        //employeRepository.save(employe);

        //When
        employeService.embaucheEmploye("Smith","Tim",Poste.COMMERCIAL, NiveauEtude.MASTER,1.0);
        String matricule = employeRepository.findLastMatricule();
        Employe employe = employeRepository.findByMatricule("C" +matricule);

        //Then
        Assertions.assertThat(employe.getMatricule()).isEqualTo("C00001");
        Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(1.0);
        Assertions.assertThat(employe.getNom()).isEqualTo("Smith");
        Assertions.assertThat(employe.getPrenom()).isEqualTo("Tim");
        Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employe.getPerformance()).isEqualTo(Entreprise.PERFORMANCE_BASE);
        Assertions.assertThat(employe.getSalaire()).isEqualTo(2129.71);
    }

    @Test
    public void testAvgPerformanceWhereMatriculeStartsWith() {
        //Given
        Employe employe = new Employe("Smith","Marc","C1501", LocalDate.now(),2000d,2,12d);
        employeRepository.save(employe);

        //When
        Double performance = employeRepository.avgPerformanceWhereMatriculeStartsWith("C");

        //Then
        Assertions.assertThat(performance).isEqualTo(2);
    }

    @Test
    public void testOneDateGetNbRtt(){
        //Given
        Employe employe = new Employe("Smith","Jack","M6501",LocalDate.of(2022,4,20),2210.40,2,1d);
        //When
        LocalDate date = LocalDate.of(2022,2,15);
        int nbRtt = employe.getNbRtt(date);
        //Then
        Assertions.assertThat(nbRtt).isEqualTo(10);

    }
}
