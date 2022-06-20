package com.ipiecoles.java.java350.repository;

import com.ipiecoles.java.java350.model.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeRepository extends JpaRepository<Employe, Long> {
    //On récupère le dernier matricule , ordonnée par les chiffres du matricul
    @Query("select max(substring(matricule,2)) from Employe")
    String findLastMatricule();

    Employe findByMatricule(String matricule);

    @Query("select avg(performance) from Employe where SUBSTRING(matricule,0,1) = ?1 ")
    Double avgPerformanceWhereMatriculeStartsWith(String premiereLettreMatricule);
}
