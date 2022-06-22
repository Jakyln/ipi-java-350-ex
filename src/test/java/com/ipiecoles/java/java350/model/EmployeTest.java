package com.ipiecoles.java.java350.model;

import com.ipiecoles.java.java350.exception.EmployeException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class EmployeTest {
    @Test
    public void testGetNbAnneesAncienneteDateEmbaucheNow() {
        //Given
        Employe employe = new Employe();
        employe.setDateEmbauche(LocalDate.now());

        //When
        int nbAnneeAnciennete = employe.getNombreAnneeAnciennete();

        //Then
        Assertions.assertThat(nbAnneeAnciennete).isZero();
    }


    @Test
    public void testGetNbAnneesAncienneteDateEmbauchePassee(){
        //Given
        //Date d'embauche 10 ans dans le passé
        Employe employe = new Employe();
        employe.setDateEmbauche(LocalDate.now().minusYears(10));
        //employe.setDateEmbauche(LocalDate.of(2012, 4, 26)); //Pas bon...
        //When
        Integer nbAnneesAnciennete = employe.getNombreAnneeAnciennete();
        //Then
        // => 10
        Assertions.assertThat(nbAnneesAnciennete).isEqualTo(10);
    }

    @Test
    public void testGetNbAnneesAncienneteDateEmbaucheFuture(){
        //Given
        //Date d'embauche 2 ans dans le futur
        Employe employe = new Employe();
        employe.setDateEmbauche(LocalDate.now().plusYears(2));
        //When
        Integer nbAnneesAnciennete = employe.getNombreAnneeAnciennete();
        //Then
        // => 0
        Assertions.assertThat(nbAnneesAnciennete).isZero();
    }
    @Test
    public void testGetNbAnneesAncienneteDateEmbaucheNull(){
        //Given
        Employe employe = new Employe();
        employe.setDateEmbauche(null);
        //When
        Integer nbAnneesAnciennete = employe.getNombreAnneeAnciennete();
        //Then
        // => 0
        Assertions.assertThat(nbAnneesAnciennete).isZero();
    }


    @ParameterizedTest
    @CsvSource({
            "'M12346',0,1,1.0,1700.0",
            "'T12346',0,1,1.0,1000.0",
            "'T12346',0,2,1.0,2300.0",
            ",0,1,1.0,1000.0",
            "'T12346',0,,1.0,1000.0"
    })
    public void testGetPrimeAnnuelleManagerPerformanceBasePleinTemps(
            String matricule,
            Integer nbAnneesAnciennete,
            Integer performance,
            Double tauxActivite,
            Double prime
    ){
        //Given
        Employe employe = new Employe("Doe", "John", matricule,
                LocalDate.now().minusYears(nbAnneesAnciennete), 2500d, performance, tauxActivite);
        //When
        Double primeObtenue = employe.getPrimeAnnuelle();
        //Then
        Assertions.assertThat(primeObtenue).isEqualTo(prime);
    }

    //Excercice 1
    @Test
    public void testAugmenterSalaire() throws EmployeException {
        //Given
        Employe employe = new Employe("Smith","Jack","M6501",LocalDate.of(2022,4,20),2210.40,2,1d);
        //When
        double coefficientAugmentation = 0.9;
        employe.augmenterSalaire(coefficientAugmentation);
        //Then
        Assertions.assertThat(employe.getSalaire()).isEqualTo(4199.76);
    }

    //Excercice 2
    @ParameterizedTest
    @CsvSource({
            "2019,0.5,4",
            "2021,1,10",
            "2022,0.5,5",
            "2032,1,11",
    })
    public void testMultipleDateGetNbRtt(
            int year,
            double tempsPartiel,
            int nbJoursRtt
    ){
        //Given
        Employe employe = new Employe("Smith","Jack","M6501",LocalDate.of(year,4,20),2210.40,2,tempsPartiel);
        //When
        LocalDate date = LocalDate.of(year,2,15);
        int nbRtt = employe.getNbRtt(date);
        //Then
        Assertions.assertThat(nbRtt).isEqualTo(nbJoursRtt);
    }

}
